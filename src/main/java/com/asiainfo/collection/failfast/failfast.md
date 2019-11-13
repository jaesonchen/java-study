
## modCount为什么没有用volatile修饰？
List，Map这些集合结构中常能看到modCount属性，在对集合进行新增或移除（LinkedHashMap在get时也会使双向链表结构发生变化）操作时会使modCount++，用于记录集合结构上变化的次数。作用是在使用迭代器Iterator对集合进行遍历时，用modCount来判断集合结构是否发生变化，如果是，则抛出ConcurrentModificationExceptions异常。
    
这里存在一个过度设计问题，首先，modcount的存在就只是为了提醒用户，此次迭代失败，因为别的线程对这个list做出了结构性的改变，并且抛出异常，作为所谓的fast-fail机制，它并没有提供任何保证，所以，为什么要付出高昂的代价(volatile虽然是轻量级锁)用于这样一个非线程异常的类？换句话说，一个ArrayList对象，能够被两个线程同时访问到。那这个设计本身就是不合理的。
    
modCount本身就不是为多线程准备的，再多线程情况下诸如ArrayList之类的集合类连本身线程安全都保证不了，又有什么必要去设计一个线程安全的modCount呢？
    

## fail-fast
fail-fast机制：当遍历一个集合对象时，如果集合对象的结构被修改了，就会抛出ConcurrentModificationExcetion异常。
    
fail-fast机制，是一种错误检测机制。它只能被用来检测错误，因为JDK并不保证fail-fast机制一定会发生。
    
注意，迭代器的快速失败行为无法得到保证。快速失败迭代器会尽最大努力抛出 ConcurrentModificationException。只有在迭代过程中修改了集合的结构，再调用next()时才会抛出该异常。因此，为提高这类迭代器的正确性而编写一个依赖于此异常的程序是错误的做法，迭代器的快速失败行为应该仅用于检测 bug。
    
当Iterator、ListIterator执行next、remove / previous、set、add操作时，如果modcount值不一致，表示集合遇到非预期的修改，会直接丢出ConcurrentModificationException异常。
    
```
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    elementData[size++] = e;
    return true;
}
private void ensureExplicitCapacity(int minCapacity) {
    modCount++;
    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}

public E remove(int index) {
    rangeCheck(index);

    modCount++;
    E oldValue = elementData(index);

    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index, numMoved);
    elementData[--size] = null; // clear to let GC do its work
    return oldValue;
}

public void clear() {
    modCount++;

    // clear to let GC do its work
    for (int i = 0; i < size; i++)
        elementData[i] = null;

    size = 0;
}

public Iterator<E> iterator() {
    return new Itr();
}

private class Itr implements Iterator<E> {
    int cursor;       // index of next element to return
    int lastRet = -1; // index of last element returned; -1 if no such
    int expectedModCount = modCount;

    public boolean hasNext() {
        return cursor != size;
    }

    public E next() {
        checkForComodification(); // 迭代时检测modCount值
        int i = cursor;
        if (i >= size)
            throw new NoSuchElementException();
        Object[] elementData = ArrayList.this.elementData;
        if (i >= elementData.length)    // 迭代时检测数组是否发生结构变化
            throw new ConcurrentModificationException();
        cursor = i + 1;
        return (E) elementData[lastRet = i];
    }

    public void remove() {
        if (lastRet < 0)
            throw new IllegalStateException();
        checkForComodification();

        try {
            ArrayList.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
            expectedModCount = modCount;    // 使用迭代器remove时，会同时更新expectedModCount，使其与modCount一致
        } catch (IndexOutOfBoundsException ex) {
            throw new ConcurrentModificationException();
        }
    }
    
    final void checkForComodification() {
    if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
    }
}

```
    
- expectedModCount的初值为modCount
- 调用add / addAll / remove / clear 时 modCount++，如果同时在进行迭代会导致fail-fast
- next和remove操作之前都会先调用checkForComodification来检查expectedModCount和modCount是否相等，不相等抛出ConcurrentModificationException
- Iterator可以使用remove方法变更集合结构，ListIterator可以使用add / set 变更集合结构，这种操作会同时更新expectedModCount，确保expectedModCount和modCount在迭代时相等。
    

## fail-safe
Fail-Safe 迭代的出现，是为了解决fail-fast抛出异常处理不方便的情况。fail-safe是针对线程安全的集合类。并发容器的iterate方法返回的iterator对象，内部都是保存了该集合对象的一个快照副本，并且没有modCount等数值做检查。这也造成了并发容器的iterator读取的数据是某个时间点的快照版本。你可以并发读取，不会抛出异常，但是不保证你遍历读取的值和当前集合对象的状态是一致的！这就是安全失败的含义。
    
Fail-Safe 迭代的缺点是：首先是iterator不能保证返回集合更新后的数据，因为其工作在集合克隆上，而非集合本身。其次，创建集合拷贝需要相应的开销，包括时间和内存。
    
在java.util.concurrent 包中集合的迭代器，如 ConcurrentHashMap, CopyOnWriteArrayList等默认为都是Fail-Safe。多线程下使用java并发包下的类来代替对应的集合，如CopyOnWriteArrayList代替ArrayList。
    
```
public boolean add(E e) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        Object[] elements = getArray();
        int len = elements.length;
        Object[] newElements = Arrays.copyOf(elements, len + 1);  // 复制
        newElements[len] = e;
        setArray(newElements);  // 使用新数组
        return true;
    } finally {
        lock.unlock();
    }
}

public E set(int index, E element) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        Object[] elements = getArray();
        E oldValue = get(elements, index);

        if (oldValue != element) {
            int len = elements.length;
            Object[] newElements = Arrays.copyOf(elements, len);    // 复制
            newElements[index] = element;
            setArray(newElements);  // 使用新数组
        } else {
            // Not quite a no-op; ensures volatile write semantics
            setArray(elements); // set的值相同时不修改结构
        }
        return oldValue;
    } finally {
        lock.unlock();
    }
}

public E remove(int index) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        Object[] elements = getArray();
        int len = elements.length;
        E oldValue = get(elements, index);
        int numMoved = len - index - 1;
        if (numMoved == 0)
            setArray(Arrays.copyOf(elements, len - 1)); // 使用新数组
        else {
            Object[] newElements = new Object[len - 1];
            System.arraycopy(elements, 0, newElements, 0, index);
            System.arraycopy(elements, index + 1, newElements, index, numMoved);
            setArray(newElements);  // 使用新数组
        }
        return oldValue;
    } finally {
        lock.unlock();
    }
}

public void clear() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        setArray(new Object[0]);
    } finally {
        lock.unlock();
    }
}


public Iterator<E> iterator() {
    return new COWIterator<E>(getArray(), 0);
}

static final class COWIterator<E> implements ListIterator<E> {
    /** Snapshot of the array */
    private final Object[] snapshot;
    /** Index of element to be returned by subsequent call to next.  */
    private int cursor;

    private COWIterator(Object[] elements, int initialCursor) {
        cursor = initialCursor;
        snapshot = elements;
    }

    public boolean hasNext() {
        return cursor < snapshot.length;
    }

    @SuppressWarnings("unchecked")
    public E next() {
        if (! hasNext())
            throw new NoSuchElementException();
        return (E) snapshot[cursor++];
    }

    /**
     * Not supported. Always throws UnsupportedOperationException.
     * @throws UnsupportedOperationException always; {@code remove}
     *         is not supported by this iterator.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported. 
     */
    public void set(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported. 
     */
    public void add(E e) {
        throw new UnsupportedOperationException();
    }
}
```
    
- 和ArrayList继承于AbstractList不同，CopyOnWriteArrayList没有继承于AbstractList，它仅仅只是实现了List接口。
- CopyOnWriteArrayList在add / set(值不一样时) / remove / clear 时会使用新数组，只保留需要使用的数组大小；ArrayList只有在扩容时才会变更数组，且每次扩容是原来的1.5倍。
- ArrayList的Iterator实现类中调用next()时，会调用checkForComodification()比较expectedModCount和modCount是否相等；CopyOnWriteArrayList的Iterator实现类中，没有所谓的checkForComodification()，更不会抛出ConcurrentModificationException异常！ 


