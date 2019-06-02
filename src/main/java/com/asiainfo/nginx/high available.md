# Nginx + keepalived  实现高可用
## keepalived是什么
Keepalived软件起初是专为LVS(Linux Virtual Server)负载均衡软件设计的，用来管理并监控LVS集群系统中各个服务节点的状态，后来又加入了可以实现高可用的VRRP (Virtual Router Redundancy Protocol, 虚拟路由器冗余协议）功能。因此，Keepalived除了能够管理LVS软件外，还可以作为其他服务（例如：Nginx、MySQL等）的高可用解决方案软件。
    
## keepalived主要功能
- 管理LVS负载均衡软件
- 实现LVS集群节点的健康检查
- 作为系统网络服务的高可用性（failover）
    
## keepalived故障转移
Keepalived高可用服务之间的故障切换转移，是通过 VRRP 来实现的。
在 Keepalived服务正常工作时，Master节点会不断地向backup节点发送（广播的方式）心跳消息，用以告诉Backup节点自己还活着；当 Master节点发生故障时，就无法发送心跳消息，Backup节点也就因此无法继续检测到来自主 Master节点的心跳了，于是调用自身的接管程序，接管Master节点的 IP资源及服务。而当主Master节点恢复时，Backup节点又会释放Master节点故障时自身接管的IP资源及服务，恢复到原来的备用角色。
    
说明：keepalived的主从切换和redis的主从切换是不一样的，keepalived的主节点挂了以后，从节点变为主节点，之前的主节点恢复以后继续做主节点。redis的主节点挂了以后，重新恢复以后变为从节点。
    
## keepalived高可用架构
keepalived + nginx    
![keepalived](../../../../resources/images/nginx/nginx-keepalived.jpg)
    
lvs + keepalived + nginx    
![lvs](../../../../resources/images/nginx/nginx-lvs.jpg)
    

虚拟ip(VIP):0.10，对外提供服务的ip，也可称作浮动ip    
192.168.0.102：nginx + keepalived master 主    
192.168.0.103：nginx + keepalived backup 从    
192.168.0.102：tomcat-8080    
192.168.0.103：tomcat-8081    

    
# keepalived 安装配置
`wget https://www.keepalived.org/software/keepalived-2.0.15.tar.gz`
    
`tar zxvf keepalived-2.0.15.tar.gz`
    
## 编译配置
`./configure --prefix=/usr/local/workspace/keepalived`
    
如果系统提示警告 *** `WARNING - this build will not support IPVS with IPv6. Please install libnl/libnl-3 dev libraries to support IPv6 with IPVS.`
    
`yum -y install libnl libnl-devel`
     
 如果系统提示错误 `configure: error: libnfnetlink headers missing`
    
`yum install -y libnfnetlink-devel`

## 编译安装
`make && make install`
    
到此keepalived安装完成，但是接下来还有最关键的一步，如果这一步没有做，后面启动keepalived的时候会报找不到配置文件的错误。
    
进入安装目录的etc目录下，将keepalived相应的配置文件拷贝到系统相应的目录当中。keepalived启动时会从/etc/keepalived目录下查找keepalived.conf配置文件
    
```
mkdir /etc/keepalived
cp /usr/local/workspace/keepalived/etc/keepalived/keepalived.conf /etc/keepalived
```

## 修改keepalived主节点192.168.0.102的/etc/keepalived/keepalived.conf配置文件
```
# 全局配置
global_defs {
    #keepalived在发生诸如切换操作时需要发送email通知，需要开启sendmail服务。
    #notification_email {
    #    jaesonchen@163.com     #设置报警邮件地址，可以设置多个，每行一个。
    #}
    #notification_email_from sns-lvs@163.com
    #smtp_server smtp.163.com           #设置SMTP Server地址
    #smtp_connection_timeout 30         #设置SMTP Server的超时时间
    
    router_id nginx_master              #Keepalived服务器的唯一标识，通常可设为hostname。故障发生时，邮件通知会用到
}
#检测脚本，告诉 keepalived 在什么情况下切换
#script一般有2种写法：
#a. 通过脚本执行的返回结果，改变优先级，keepalived继续发送通告消息，backup比较优先级再决定是否抢占ip
#b. 脚本里面检测到异常，直接关闭keepalived进程，backup机器接收不到advertisement会抢占IP
vrrp_script chk_nginx {
    #运行脚本，检测nginx宕机以后，自动开启服务
    script "/usr/local/workspace/keepalived/check_nginx_pid.sh"
    interval 2      #检测脚本执行的间隔，单位是秒
    weight -5       #检测失败（脚本返回非0）则优先级 -5
    fall 2          #检测连续 2 次失败才算确定是真失败。会用weight减少优先级（1-255之间）
    rise 1          #检测 1 次成功就算成功。但不修改优先级
}
#vrrp 定义虚拟路由，VI_1 为虚拟路由的标示符，自己定义名称
vrrp_instance VI_1 {
    state MASTER            # 指定keepalived的初始状态，MASTER为主，BACKUP为备用，但这里指定的不算，还是要竞选通过优先级来确定。
    interface eth0          # 绑定的网卡(当前centos的网卡) 用ifconfig/ip a查看你具体的网卡
    virtual_router_id 66    # 虚拟路由编号，主从要一致，这里非常重要，相同的VRID为一个组，他将决定多播的MAC地址，范围是0-255
    #发送多播数据包时的源IP地址，这里实际上就是在哪个地址上发送VRRP通告，这个非常重要，一定要选择稳定的网卡端口来发送，
    #这里相当于 heartbeat的心跳端口，如果没有设置那么就用默认的绑定的网卡的IP，也就是interface指定的IP地址。
    mcast_src_ip 192.168.0.102
    priority 100            # 节点优先级, 优先级高的为master，范围1-254
    advert_int 1            # 检查间隔，默认为1s，VRRP的定时器，MASTER每隔这样一个时间间隔，就会发送一个advertisement报文以通知组内其他路由器自己工作正常
    #nopreempt              # 定义工作模式为非抢占模式，在MASTER节点中 配置 nopreempt ，当它异常恢复后，即使priority更高也不会抢占
    #认证方式和密码，主从必须一样
    authentication {
        auth_type PASS
        auth_pass 1234
    }
    #引用VRRP脚本，定期运行它们来改变优先级，并最终引发主备切换。
    track_script {
        #执行 Nginx 监控的服务
        chk_nginx
    }
    #虚拟ip(VIP)，多个ip每行一个
    virtual_ipaddress {
        192.168.0.1     
    }
}
```

## 修改keepalived从节点192.168.0.103的/etc/keepalived/keepalived.conf配置文件
```
global_defs {
    router_id nginx_backup
}
vrrp_instance VI_1 {
    state BACKUP
    interface eth0
    virtual_router_id 66
    priority 50
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 1234
    }
    track_script {
        chk_nginx
    }
    virtual_ipaddress {
        192.168.0.1
    }
}
```

## 创建检查nginx是否启动的shell脚本
`vi /usr/local/workspace/keepalived/check_nginx_pid.sh`
    
```
#!/bin/bash
A=`ps -C nginx --no-header |wc -l`
#如果nginx没有启动就启动nginx
if [ $A -eq 0 ];then
    #重启nginx
    /usr/local/workspace/nginx/sbin/nginx
    #nginx重启失败，则停掉keepalived服务，进行VIP转移
    if [ `ps -C nginx --no-header |wc -l` -eq 0 ];then
        killall keepalived                    
    fi
fi
```

## 启动/停止命令
```
systemctl start keepalived
systemctl stop keepalived
```
    

## Keepalived健康检查
在Keepalived的实际运用中需要对后端服务（比如Nginx）做一个健康检查来判断服务是否正常，否则Keepalived检查的对象是Keepalived自身服务而不是其他应用服务。
    
Keepalived健康检查功能支持TCP_CHECK、HTTP_GET、MISC_CHECK、VRRP_SCRIPT方式。
    
```
TCP_CHECK {
    connect_port 8080
    connect_timeout 6
    nb_get_retry 3
    delay_before_retry 3
}

HTTP_GET {
    url {
        path /index.html
        status_code 200
    }
    connect_timeout 6
    nb_get_retry 3
    delay_before_retry 3
}
```

vrrp_script脚本检查，该选项配合track_script使用。使用自定义脚本来判断服务是否异常或网关是否无法ping通等，如果出现异常就应该移除掉VIP，也就是主动来停止Keepalived服务。
    
```
vrrp_script check_nginx {
    script "/scripts/check_nginx.sh"
    interval 5
}
```

