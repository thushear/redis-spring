///**
// * 
// */
//package net.shopin.redis;
//
//import java.io.IOException;
//
//import net.shopin.zookeeper.ZkClient;
//import net.shopin.zookeeper.ZkDataListener;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.StringUtils;
//
//
//
//import redis.clients.jedis.ShardedJedisPool;
//
///**
// * @author kongming
// *
// */
//public class RedisUtils implements ZkDataListener{
//	
//	private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);
//	
//	private ShardedJedisPool writePool = null;
//    private ShardedJedisPool readPool = null;
//    private ConnectionFactoryBuilder connectionFactoryBuilder = null;
//    
//    
//    private ZkClient zkClient;
//
//
//
//	public RedisUtils(ConnectionFactoryBuilder connectionFactoryBuilder) {
//		 this.connectionFactoryBuilder = connectionFactoryBuilder;
//	     //检查是否是zookeeper 配置
//		 if (StringUtils.hasLength(connectionFactoryBuilder.getZookeeperServers())
//	                && StringUtils.hasLength(connectionFactoryBuilder.getZookeeperConfigRedisNodeName())) {
//			 try {
//				zkClient = new ZkClient(connectionFactoryBuilder.getZookeeperServers(), connectionFactoryBuilder.getZookeeperTimeout());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			 zkClient.subscribeDataChanges(connectionFactoryBuilder.getZookeeperConfigRedisNodeName(), this);
//			 byte[] data = zkClient.readData(connectionFactoryBuilder.getZookeeperConfigRedisNodeName());
//			 
//		 }
//		
//	}
//
//
//
//	@Override
//	public void handleDataChange(String path, Object data) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//
//	@Override
//	public void handleDataDeleted(String path) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	
//    
//	
//	
//	
//
//}
