package com.asiainfo.datastructure;

import java.util.SortedMap;

/**   
 *  一致性hash:
 *  先构造一个长度为232的整数环（这个环被称为一致性Hash环），根据节点名称的Hash值（其分布为[0, 232-1]）将服务器节点放置在这个Hash环上，
 *  然后根据数据的Key值计算得到其Hash值（其分布也为[0, 232-1]），接着在Hash环上顺时针查找距离这个Key值的Hash值最近的服务器节点，完成Key到服务器的映射查找。
 * 
 * 数据结构的选取：
 * 1、遍历 + List
 *   算出所有待加入数据结构的节点名称的Hash值放入一个数组中，然后使用排序算法将其从小到大进行排序，最后将排序后的数据放入List中。
 *   待路由的结点，只需要在List中遍历找到第一个Hash值比它大的服务器节点就可以了。
 *   
 * 2、二叉查找树
 * TreeMap本身提供了一个tailMap(K fromKey)方法，支持从红黑树中查找比fromKey大的值的集合，但并不需要遍历整个数据结构。
 *  缺点：为了维护红黑树，数据插入效率TreeMap在三种数据结构里面是最差的，且插入要慢上5~10倍。
 *  
 *  虚拟节点：
 *  一致性哈希算法在服务节点太少时，容易因为节点分部不均匀而造成数据倾斜问题。
 *  为了解决这种数据倾斜问题，一致性哈希算法引入了虚拟节点机制，即对每一个服务节点计算多个哈希，每个计算结果位置都放置一个此服务节点，称为虚拟节点。
 *  具体做法可以在服务器ip或主机名的后面增加编号来实现。
 * 
 * @author chenzq  
 * @date 2019年3月28日 下午4:39:51
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class ConsistentHash {

    public static void main(String[] args) {
        String[] keys = {"key1", "key2", "key3", "key4", "key5"};
        for (int i = 0; i < keys.length; i++) {
            System.out.print("[" + keys[i] + "]的hash值为" + ConsistentHashingWithoutVirtualNode.getHash(keys[i]));
            System.out.println("，被路由到结点[" + ConsistentHashingWithoutVirtualNode.getServer(keys[i]) + "]");
        }
        
        System.out.println("======================================");
        for (int i = 0; i < keys.length; i++) {
            System.out.print("[" + keys[i] + "]的hash值为" + ConsistentHashingWithVirtualNode.getHash(keys[i]));
            System.out.println("，被路由到结点[" + ConsistentHashingWithVirtualNode.getServer(keys[i]) + "]");
        }
    }
}

// 不使用虚拟节点
class ConsistentHashingWithoutVirtualNode {
    
    private static String[] servers = {"192.168.0.0:111", "192.168.0.1:111", "192.168.0.2:111", "192.168.0.3:111", "192.168.0.4:111"};
    // key表示服务器的hash值，value表示服务器的名称
    private static SortedMap<Integer, String> sortedMap =  new java.util.TreeMap<Integer, String>();
    static {
        for (String server : servers) {
            int hash = getHash(server);
            System.out.println("[" + server + "]加入集合中, 其Hash值为" + hash);
            sortedMap.put(hash, server);
        }
        System.out.println();
    }
    // 使用FNV1_32_HASH算法计算服务器的Hash值
    public static int getHash(String str) {
        final int p = 16777619;
        int hash = (int)2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        // 如果算出来的值为负数则取其绝对值
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }
    // 得到应当路由到的结点
    public static String getServer(String key) {
        // 得到带路由的结点的Hash值
        int hash = getHash(key);
        // 得到大于该Hash值的所有Map
        SortedMap<Integer, String> subMap = sortedMap.tailMap(hash);
        // 第一个Key就是顺时针过去离node最近的那个结点
        if (subMap.isEmpty()) {
            subMap = sortedMap.headMap(hash);
        }
        Integer i = subMap.firstKey();
        // 返回对应的服务器名称
        return subMap.get(i);
    }
}
// 使用虚拟节点
class ConsistentHashingWithVirtualNode {
    
    private static final int VIRTUAL_NODES = 5;
    private static String[] servers = {"192.168.0.0:111", "192.168.0.1:111", "192.168.0.2:111", "192.168.0.3:111", "192.168.0.4:111"};
    // key表示服务器的hash值，value表示服务器的名称
    private static SortedMap<Integer, String> sortedMap =  new java.util.TreeMap<Integer, String>();
    static {
        for (String server : servers) {
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                String virtualNode = server+ "&&VN" + String.valueOf(i);
                int hash = getHash(virtualNode);
                System.out.println("[" + virtualNode + "]加入集合中, 其Hash值为" + hash);
                sortedMap.put(hash, server);
            }
        }
        System.out.println();
    }
    // 使用FNV1_32_HASH算法计算服务器的Hash值
    public static int getHash(String str) {
        final int p = 16777619;
        int hash = (int)2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        // 如果算出来的值为负数则取其绝对值
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }
    // 得到应当路由到的结点
    public static String getServer(String key) {
        // 得到带路由的结点的Hash值
        int hash = getHash(key);
        // 得到大于该Hash值的所有Map
        SortedMap<Integer, String> subMap = sortedMap.tailMap(hash);
        // 第一个Key就是顺时针过去离node最近的那个结点
        if (subMap.isEmpty()) {
            subMap = sortedMap.headMap(hash);
        }
        Integer i = subMap.firstKey();
        // 返回对应的服务器名称
        return subMap.get(i);
    }
}
