package com.asiainfo.database;

/**   
 * InnoDB redo log
 * - innodb的引擎实现中，为了实现事务的持久性，构建了重做日志系统。重做日志由两部分组成：内存日志缓冲区(redo log buffer)和重做日志文件。
 * - 日志缓冲区是为了加快写日志的速度，而重做日志文件为日志数据提供持久化的作用。
 * - 在innodb的重做日志系统中，为了更好实现日志的易恢复性、安全性和持久化性，引入了以下几个概念：LSN、log block、日志文件组、checkpoint和归档日志。
 * 1. LSN
 * - 在innodb中的重做日志系统中，定义一个LSN序号，其代表的意思是日志序号。LSN在引擎中定义的是一个dulint_t类型值，相当于unsigned long(64),
 * 
 * 2. Log Block
 * - innodb在日志系统里面定义了log block的概念，其实log block就是一个512字节的数据块，这个数据块包括块头、日志信息和块的checksum。
 * - 在日志恢复的时候，innodb会对加载的block进行checksum校验，以免在恢复过程中数据产生错误。事务的日志写入是基于块的，如果事务的日志大小小于496字节，
 * - 那么会合其他的事务日志合并在一个块中，如果事务日志的大小大于496字节，那么会以496为长度进行分离存储。
 * 
 * 3. 重做日志
 * - innodb在重做日志实现当中，设计了3个层模块，即redo log buffer、group files和archive files
 * - redo log buffer 重做日志的日志内存缓冲区，新写入的日志都是先写入到这个地方。redo log buffer中数据同步到磁盘上，必须进行刷盘操作。
 * - group files 重做日志文件组，一般由3个同样大小的文件组成。3个文件的写入是依次循环的，每个日志文件写满后，即写下一个，日志文件如果都写满时，会覆盖第一次重新写。
 * - archive files 归档日志文件，是对重做日志文件做增量备份，它是不会覆盖以前的日志信息。
 * 
 * 4. checkpoint
 * - checkpoint是日志的检查点，其作用就是在数据库异常后，redo log是从这个点的信息获取到LSN,并对检查点以后的日志和PAGE做重做恢复。
 * - 当日志缓冲区写入的日志LSN距离上一次生成检查点的LSN达到一定差距的时候，就会开始创建检查点，创建检查点首先会将内存中的表的脏数据写入到硬盘，
 * - 然后再将redo log buffer中小于本次检查点的LSN的日志也写入硬盘。
 * 
 * 5. 日志写入和日志保护机制
 * -  innodb有四种日志刷盘行为, 分别是异步redo log buffer刷盘、同步redo log buffer刷盘、异步建立checkpoint刷盘和同步建立checkpoint刷盘。
 * -  innodb中，刷盘行为是非常耗磁盘IO的，innodb对刷盘做了一套非常完善的策略。
 * 
 * 5.1 重做日志刷盘选项
 * - 在innodb引擎中有个全局变量srv_flush_log_at_trx_commit,这个全局变量是控制flushdisk的策略，也就是确定调不调用fsync这个函数，什么时候掉这个函数。
 * - 选项：
 * - 0 每隔1秒由MasterThread控制重做日志模块调用log_flush_to_disk来刷盘，好处是提高了效率，坏处是1秒内如果数据库崩溃，日志和数据会丢失。
 * - 1 每次写入重做日志后，都调用fsync来进行日志写入磁盘。好处是每次日志都写入了磁盘，数据可靠性大大提高，坏处是每次调用fsync会产生大量的磁盘IO，影响数据库性能。
 * - 2 每次写入重做日志后，都将日志写入日志文件的page cache。这种情况如果物理机崩溃后，所有的日志都将丢失。
 * 
 * 5.2 日志刷盘保护
 * - 由于重做日志是一个组内多文件重复写的一个过程，那么意味日志如果不及时写盘和创建checkpoint，就有可能会产生日志覆盖。
 * - innodb定义了一个日志保护机制，在存储引擎会定时调用log_check_margins日志函数来检查保护机制。
 * 
 * 6. 重做日志恢复数据的流程(checkpoint方式)
 * a. MySQL启动的时候，先会从数据库文件中读取出上次保存最大的LSN。
 * b. 调用recv_recovery_from_checkpoint_start，并将最大的LSN作为参数传入函数当中。
 * c. 函数会先最近建立checkpoint的日志组，并读取出对应的checkpoint信息。
 * d. 通过checkpoint lsn和传入的最大LSN进行比较，如果相等，不进行日志恢复数据，如果不相等，进行日志恢复。
 * e. 在启动恢复之前，先会同步各个日志组的archive归档状态。
 * f. 在开始恢复时，先会从日志文件中读取2M的日志数据到log_sys->buf，然后对这2M的数据进行scan,校验其合法性，
 *  - 而后将去掉block header的日志放入recv_sys->buf当中，这个过程称为scan,会改变scanned lsn.
 * g. 在对2M的日志数据scan后，innodb会对日志进行mtr操作解析，并执行相关的mtr函数。如果mtr合法，会将对应的记录数据按space page_no作为KEY存入recv_sys->addr_hash当中。
 * h. 当对scan的日志数据进行mtr解析后，innodb对会调用recv_apply_hashed_log_recs对整个recv_sys->addr_hash进行扫描，
 *  - 并按照日志相对应的操作进行对应page的数据恢复。这个过程会改变recovered_lsn。
 * i. 如果完成第8步后，会再次从日志组文件中读取2M数据，跳到步骤6继续相对应的处理，直到日志文件没有需要恢复的日志数据。
 * j. innodb在恢复完成日志文件中的数据后，会调用recv_recovery_from_checkpoint_finish结束日志恢复操作，主要是释放一些开辟的内存。并进行事务和binlog的处理。
 * 
 * 
 * - innodb_data_home_dir下面有两个文件ib_logfile0和ib_logfile1。MySQL官方手册中将这两个文件叫文InnoDB存储引擎的日志文件；
 * - 每个InnDB存储引擎至少有1个重做日志文件组（group），每个文件组下至少有两个重做日志文件，默认的为ib_logfile0、ib_logfile1；
 * - 日志组中每个重做日志的大小一致，并循环使用；
 * - InnoDB存储引擎先写重做日志文件1，当文件1满了的时候，会自动切换到日志文件2，当重做日志文件2也写满时，会再切换到重做日志文件1；
 * - 为了保证安全和性能，请设置每个重做日志文件设置镜像，并分配到不同的磁盘上面；
 * 
 * 常用设置的参数有：
 * show variables like 'innodb%log%';
 * innodb_mirrored_log_groups   镜像组的数量，默认为1，没有镜像；
 * innodb_log_group_home_dir    日志组所在的路径，默认为data的home目录；
 * innodb_log_files_in_group    日志组的数量，默认为2；
 * innodb_log_file_size         日志组的大小,默认为5M；
 * innodb_log_buffer_size       日志缓冲池的大小，图上为30M;
 * 
 * 
 * 
 * InnoDB undo log
 * Undo Log 是为了实现事务的原子性，在MySQL数据库InnoDB存储引擎中，还用Undo Log来实现多版本并发控制(简称：MVCC)。
 * - 事务的原子性(Atomicity)
 * - 事务中的所有操作，要么全部完成，要么不做任何操作，不能只做部分操作。如果在执行的过程中发生了错误，要回滚(Rollback)到事务开始前的状态，就像这个事务从来没有执行过。
 * 
 * - 原理
 * - Undo Log的原理很简单，为了满足事务的原子性，在操作任何数据之前，首先将数据备份到一个地方（这个存储数据备份的地方称为Undo Log）。
 * - 然后进行数据的修改。如果出现了错误或者用户执行了ROLLBACK语句，系统可以利用Undo Log中的备份将数据恢复到事务开始之前的状态。
 * 
 * 
 * Undo + Redo事务的特点
 * A. 为了保证持久性，必须在事务提交前将Redo Log持久化。
 * B. 数据不需要在事务提交前写入磁盘，而是缓存在内存中。
 * C. Redo Log 保证事务的持久性。
 * D. Undo Log 保证事务的原子性。
 * E. 数据必须要晚于Redo Log写入持久存储。
 * 
 * 
 * IO性能
 * Undo + Redo的设计主要考虑的是提升IO性能。虽说通过缓存数据，减少了写数据的IO。 但是却引入了新的IO，即写Redo Log的IO。
 * Redo Log的IO性能如果不好，就不能起到提高性能的目的。
 * 为了保证Redo Log能够有比较好的IO性能，InnoDB 的 Redo Log的设计有以下几个特点：
 * 1. 尽量保持Redo Log存储在一段连续的空间上。因此在系统第一次启动时就会将日志文件的空间完全分配。以顺序追加的方式记录Redo Log,通过顺序IO来改善性能。
 * 2. 批量写入日志。日志并不是直接写入文件，而是先写入redo log buffer. 当需要将日志刷新到磁盘时(如事务提交),将许多日志一起写入磁盘。
 * 3. 并发的事务共享Redo Log的存储空间，它们的Redo Log按语句的执行顺序，依次交替的记录在一起，以减少日志占用的空间。
 * 4. 因为3的原因,当一个事务将Redo Log写入磁盘时，也会将其他未提交的事务的日志写入磁盘。
 * 5. Redo Log上只进行顺序追加的操作，当一个事务需要回滚时，它的Redo Log记录也不会从Redo Log中删除掉。
 * 
 * 
 * 恢复(Recovery)
 * - 恢复策略
 * - 未提交的事务和回滚了的事务也会记录Redo Log，因此在进行恢复时,这些事务要进行特殊的的处理.
 * - 有2中不同的恢复策略：
 * A. 进行恢复时，只重做已经提交了的事务。
 * B. 进行恢复时，重做所有事务包括未提交的事务和回滚了的事务。然后通过Undo Log回滚那些未提交的事务。
 * 
 * 
 * - InnoDB存储引擎的恢复机制
 * - MySQL数据库InnoDB存储引擎使用了B策略, InnoDB存储引擎中的恢复机制有几个特点：
 * A. 在重做Redo Log时，并不关心事务性。 恢复时，没有BEGIN，也没有COMMIT,ROLLBACK的行为。也不关心每个日志是哪个事务的。
 *  - 尽管事务ID等事务相关的内容会记入Redo Log，这些内容只是被当作要操作的数据的一部分。
 * B. 使用B策略就必须要将Undo Log持久化，而且必须要在写Redo Log之前将对应的Undo Log写入磁盘。
 *  - Undo和Redo Log的这种关联，使得持久化变得复杂起来。为了降低复杂度，InnoDB将Undo Log看作 数据，
 *  - 因此记录Undo Log的操作也会记录到redo log中。这样undo log就可以象数据一样缓存起来，而不用在redo log之前写入磁盘了。
 * C. 既然Redo没有事务性，那岂不是会重新执行被回滚了的事务？确实是这样。同时Innodb也会将事务回滚时的操作也记录到redo log中。
 *  - 回滚操作本质上也是对数据进行修改，因此回滚时对数据的操作也会记录到Redo Log中。一个被回滚了的事务在恢复时的操作就是先redo再undo，因此不会破坏数据的一致性。
 *  
 *  
 * - InnoDB存储引擎中相关的函数
 * Redo: recv_recovery_from_checkpoint_start()
 * Undo: recv_recovery_rollback_active()
 * Undo Log的Redo Log: trx_undof_page_add_undo_rec_log()
 * 
 * 
 * 
 * @author chenzq  
 * @date 2019年5月12日 下午8:23:48
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class RedoUndoLog {

}
