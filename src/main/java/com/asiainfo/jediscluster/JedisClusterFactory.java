package com.asiainfo.jediscluster;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
  * @Description: JedisCluster工厂<br>
  * @author zuowg <br>
  * @date 2015-9-4 下午06:09:28 <br>
  * Copyright: (C) Copyright 1993-2010 AsiaInfo Holdings, Inc<br>
  * Company: 北京亚信智慧数据科技有限公司
 */
public class JedisClusterFactory {
	
	/**JedisCluster单例*/
	private static JedisCluster jedisCluster;

	/**
	 * 获取JedisCluster单一实例
	 * @return
	 */
	public synchronized static JedisCluster getJedisCluster() {
		if (jedisCluster == null)
			jedisCluster = genJedisCluster();
		return jedisCluster;
	}

	/**
	 * 从连接池中获取JedisCluster实例
	 * @return
	 */
	private static JedisCluster genJedisCluster() {
		JedisCluster jc = null;
		try {
			Set<HostAndPort> haps = parseHostAndPort();
			if (haps != null) {
				GenericObjectPoolConfig config = new GenericObjectPoolConfig();
				config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
				config.setSoftMinEvictableIdleTimeMillis(900000);//15分钟
				config.setMaxWaitMillis(5000);
				//最大连接数, 默认8个
				config.setMaxTotal(100);
				//最大空闲连接数, 默认8个
				config.setMaxIdle(80);
				//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
				config.setTimeBetweenEvictionRunsMillis(5000);
				jc = new JedisCluster(haps, 10000, 3, config);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if(jc != null) {
					jc.close();
				}
			} catch (Exception e2) {}
		}
		return jc;
	}

	/**
	 * 将集群结点字符串转换为Set对象
	 * @param clusterNodes
	 * 		字符串型的Redis集群结点
	 * @return
	 * 		Set<HostAndPort> 对象
	 */
	private static Set<HostAndPort> parseHostAndPort() {
        try {
            Set<HostAndPort> haps = new HashSet<HostAndPort>();
            String clusterNodes = "10.1.235.50:6010,10.1.235.50:6011,10.1.235.50:6020,10.1.235.50:6021,10.1.235.50:6030,10.1.235.50:6031";
            String[] ipPorts = clusterNodes.split(",");
            for (String ipPort : ipPorts) {
                String[] ipAndPort = ipPort.split(":");
                HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                haps.add(hap);
            }
            return haps;
        } catch (Exception ex) {
        	ex.printStackTrace();
        	return null;
        }
    }
}