package com.asiainfo.datastructure;

import java.util.Map;

/** 
 * 二叉查找树可能会出现不平衡的情况，极端情况下 左子树和右子树深度相差过多，时间复杂度可能有O(log(n))变为O(n)。
 * 
 * 红黑树（Red Black Tree） 是一种自平衡二叉查找树。
 * 红黑树和AVL树类似，都是在进行插入和删除操作时通过特定操作保持二叉查找树的平衡，从而获得较高的查找性能。
 * 红黑树的约束：
 * 1. 每个节点要么是红色，要么是黑色。
 * 2. 根节点必须是黑色
 * 3. 每个红节点的两个子节点都是黑色的，红色节点不能连续（也即是，红色节点的孩子和父亲都不能是红色）。
 * 4. 对于每个节点，从该点至null（叶子节点）的任何路径，都含有相同个数的黑色节点。
 * 这些约束强制了红黑树的关键性质: 从根到叶子的最长的可能路径不多于最短的可能路径的两倍长。
 * 
 * 在树的结构发生改变时（插入或者删除操作），往往会破坏上述条件3或条件4，需要通过调整使得查找树重新满足红黑树的条件。
 * 调整可以分为两类：
 * 一类是颜色调整，即改变某个节点的颜色；
 * 另一类是结构调整，集改变树的结构关系。
 * 结构调整过程包含两个基本操作：左旋（Rotate Left），右旋（Rotate Right）。
 * 
 * 左旋的过程是将x的右子树y绕x逆时针旋转，使得x的右子树y成为x的父亲，x成为y的左子树，y的左子树成为x的右子树。
 * 右旋的过程是将x的左子树绕y顺时针旋转，使得x的左子树y成为x的父亲，x成为y的右子树，y的右子树成为x的左子树。
 * 
 * jdk8中的HashMap、TreeMap、ConcurrentHashMap使用了红黑树，当链式节点超过8时，转换为TreeNode(红黑树)
 * 
 */
public class RBTree<K, V> {
    
    public static void main(String[] args) {
        
        RBTree<String, String> map = new RBTree<>();
        map.put("key1", "1001");
        map.put("key2", "1002");
        map.put("key3", "1003");
        map.put("key4", "1004");
        map.put("key3", "1005");
        System.out.println(map.size);
        System.out.println(map.get("key3"));
        System.out.println(map.get("key5"));
        System.out.println(map.containsKey("key1"));
        System.out.println(map.containsValue("1004"));
    }
    
    private static final boolean RED   = false;
    private static final boolean BLACK = true;
    
    private Entry<K,V> root;
    /**
     * The number of entries in the tree
     */
    private transient int size = 0;
    /**
     * The number of structural modifications to the tree.
     */
    private transient int modCount = 0;
    
    /**
     * Node in the Tree.  Doubles as a means to pass key-value pairs back to
     * user (see Map.Entry).
     */
    static final class Entry<K, V> implements Map.Entry<K, V> {
        
        K key;
        V value;
        Entry<K, V> left;
        Entry<K, V> right;
        Entry<K, V> parent;
        boolean color = BLACK;

        Entry(K key, V value, Entry<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        @Override public K getKey() {
            return key;
        }

        @Override public V getValue() {
            return value;
        }

        @Override public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override public boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
            return valEquals(key, e.getKey()) && valEquals(value, e.getValue());
        }

        @Override public int hashCode() {
            int keyHash = (key == null ? 0 : key.hashCode());
            int valueHash = (value == null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }

        @Override public String toString() {
            return key + "=" + value;
        }
    }
    
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }
    
    public boolean containsValue(Object value) {
        for (Entry<K, V> e = getFirstEntry(); e != null; e = successor(e))
            if (valEquals(value, e.value))
                return true;
        return false;
    }
    
    public V get(Object key) {
        Entry<K,V> p = getEntry(key);
        return (p == null ? null : p.value);
    }
    
    public V put(K key, V value) {
        if (key == null)
            throw new NullPointerException();
        Entry<K, V> t = root;
        if (t == null) {
            root = new Entry<>(key, value, null);
            size = 1;
            modCount++;
            return null;
        }
        
        int cmp;
        Entry<K, V> parent;
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        do {
            parent = t;
            cmp = k.compareTo(t.key);
            if (cmp < 0)
                t = t.left;
            else if (cmp > 0)
                t = t.right;
            else
                return t.setValue(value);
        } while (t != null);
        Entry<K, V> e = new Entry<>(key, value, parent);
        if (cmp < 0)
            parent.left = e;
        else
            parent.right = e;
        // rebalance rb tree
        fixAfterInsertion(e);
        size++;
        modCount++;
        return null;
    }
    
    public V remove(Object key) {
        Entry<K, V> p = getEntry(key);
        if (p == null)
            return null;

        V oldValue = p.value;
        deleteEntry(p);
        return oldValue;
    }
    
    /**
     * Removes all of the mappings from this map.
     * The map will be empty after this call returns.
     */
    public void clear() {
        modCount++;
        size = 0;
        root = null;
    }
    
    final Entry<K, V> getEntry(Object key) {
        if (key == null)
            throw new NullPointerException();
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        Entry<K, V> p = root;
        while (p != null) {
            int cmp = k.compareTo(p.key);
            if (cmp < 0)
                p = p.left;
            else if (cmp > 0)
                p = p.right;
            else
                return p;
        }
        return null;
    }
    
    /**
     * Delete node p, and then rebalance the tree.
     */
    private void deleteEntry(Entry<K, V> p) {
        modCount++;
        size--;

        // 删除的节点有2个子节点，复制后续节点的key、value到要删除的节点，删除后续节点（此时后续节点没有2个子节点）
        // If strictly internal, copy successor's element to p and then make p
        // point to successor.
        if (p.left != null && p.right != null) {
            Entry<K,V> s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        } // p has 2 children

        // 取要删除节点的左子节点或者右子节点
        // Start fixup at replacement node, if it exists.
        Entry<K,V> replacement = (p.left != null ? p.left : p.right);

        // 存在子节点（只有1个）
        if (replacement != null) {
            // Link replacement to parent
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left  = replacement;
            else
                p.parent.right = replacement;

            // Null out links so they are OK to use by fixAfterDeletion.
            p.left = p.right = p.parent = null;

            // 要删除的节点是黑色，则rebalance tree
            // Fix replacement
            if (p.color == BLACK)
                fixAfterDeletion(replacement);
        } else if (p.parent == null) { // return if we are the only node.
            root = null;
        } else { //  No children. Use self as phantom replacement and unlink.
            if (p.color == BLACK)
                fixAfterDeletion(p);

            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }
    }
    
    /** From CLR */
    private void fixAfterInsertion(Entry<K, V> x) {
        // 新节点默认设为红色
        x.color = RED;
        // 父节点是红色
        while (x != null && x != root && x.parent.color == RED) {
            // 父节点是左节点
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                // 父节点对应的右兄弟节点
                Entry<K, V> y = rightOf(parentOf(parentOf(x)));
                // 父节点对应的右兄弟节点也是红色，则祖父节点必为黑色
                if (colorOf(y) == RED) {
                    // 设置父节点和对应的兄弟节点为黑色
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    // 设置祖父节点的颜色为红色，从祖父节点开始继续循环判断
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                // 父节点对应的右兄弟节点是黑色，colorOf为null时也返回BLACK
                } else {
                    // 当前节点是右节点，左旋父节点，x指向旋转后的左子节点（原来的父节点）
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    // 设置旋转后的父节点（就是新增的节点）为黑色，祖父节点为红色
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    // x现在是左子节点，右旋祖父节点
                    rotateRight(parentOf(parentOf(x)));
                }
            // 父节点是右节点
            } else {
                // 父节点对应的左兄弟节点
                Entry<K, V> y = leftOf(parentOf(parentOf(x)));
                // 父节点对应的左兄弟节点也是红色，则祖父节点必为黑色
                if (colorOf(y) == RED) {
                    // 设置父节点和对应的兄弟节点为黑色
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    // 设置祖父节点的颜色为红色，从祖父节点开始继续循环判断
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                 // 父节点对应的左兄弟节点是黑色，colorOf为null时也返回BLACK
                } else {
                    // 当前节点是左节点，右旋父节点，x指向旋转后的右子节点（原来的父节点）
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    // 设置旋转后的父节点（就是新增的节点）为黑色，祖父节点为红色
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    // x现在是右子节点，左旋祖父节点
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }
    
    // x 是被删除节点的子节点，没有子节点时是要删除的节点本身
    /** From CLR */
    private void fixAfterDeletion(Entry<K, V> x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Entry<K, V> sib = rightOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (colorOf(leftOf(sib))  == BLACK &&
                    colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else { // symmetric
                Entry<K, V> sib = leftOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) == BLACK &&
                    colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }

        setColor(x, BLACK);
    }
    
    /** From CLR */
    private void rotateLeft(Entry<K, V> p) {
        if (p != null) {
            Entry<K, V> r = p.right;
            p.right = r.left;
            if (r.left != null)
                r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null)
                root = r;
            else if (p.parent.left == p)
                p.parent.left = r;
            else
                p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }

    /** From CLR */
    private void rotateRight(Entry<K, V> p) {
        if (p != null) {
            Entry<K, V> l = p.left;
            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null)
                root = l;
            else if (p.parent.right == p)
                p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }
    
    /**
     * Returns the first Entry in the TreeMap (according to the TreeMap's
     * key-sort function).  Returns null if the TreeMap is empty.
     */
    final Entry<K, V> getFirstEntry() {
        Entry<K, V> p = root;
        if (p != null)
            while (p.left != null)
                p = p.left;
        return p;
    }

    /**
     * Returns the last Entry in the TreeMap (according to the TreeMap's
     * key-sort function).  Returns null if the TreeMap is empty.
     */
    final Entry<K, V> getLastEntry() {
        Entry<K, V> p = root;
        if (p != null)
            while (p.right != null)
                p = p.right;
        return p;
    }

    /**
     * Returns the successor of the specified Entry, or null if no such.
     */
    static <K, V> Entry<K, V> successor(Entry<K, V> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            Entry<K, V> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            Entry<K, V> p = t.parent;
            Entry<K, V> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    /**
     * Returns the predecessor of the specified Entry, or null if no such.
     */
    static <K, V> Entry<K, V> predecessor(Entry<K, V> t) {
        if (t == null)
            return null;
        else if (t.left != null) {
            Entry<K, V> p = t.left;
            while (p.right != null)
                p = p.right;
            return p;
        } else {
            Entry<K, V> p = t.parent;
            Entry<K, V> ch = t;
            while (p != null && ch == p.left) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }
    
    /**
     * Test two values for equality.  Differs from o1.equals(o2) only in
     * that it copes with {@code null} o1 properly.
     */
    static final boolean valEquals(Object o1, Object o2) {
        return (o1 == null ? o2 == null : o1.equals(o2));
    }
    
    private static <K, V> boolean colorOf(Entry<K, V> p) {
        return (p == null ? BLACK : p.color);
    }

    private static <K, V> Entry<K, V> parentOf(Entry<K, V> p) {
        return (p == null ? null: p.parent);
    }

    private static <K, V> void setColor(Entry<K, V> p, boolean c) {
        if (p != null)
            p.color = c;
    }

    private static <K, V> Entry<K, V> leftOf(Entry<K, V> p) {
        return (p == null) ? null: p.left;
    }

    private static <K, V> Entry<K, V> rightOf(Entry<K, V> p) {
        return (p == null) ? null: p.right;
    }
}
