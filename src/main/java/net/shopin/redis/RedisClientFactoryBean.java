///**
// * 
// */
//package net.shopin.redis;
//
//import java.util.List;
//
//import org.springframework.beans.factory.FactoryBean;
//
//
//
///**
// * @author kongming
// *
// */
//public class RedisClientFactoryBean implements FactoryBean {
//	private ConnectionFactoryBuilder connectionFactoryBuilder = new ConnectionFactoryBuilder();
//	private List<String> masterConfList = null;
//    private List<String> slaveConfList = null;
//    
//    
//
//	@Override
//	public Object getObject() throws Exception {
//		  //优先zookeeper配置，先检查
//		if(connectionFactoryBuilder.getZookeeperServers()!=null && connectionFactoryBuilder.getZookeeperServers().trim().length()>0
//                && connectionFactoryBuilder.getZookeeperConfigRedisNodeName()!=null && connectionFactoryBuilder.getZookeeperConfigRedisNodeName().trim().length()>0){
//            return new RedisUtils(connectionFactoryBuilder);
//        }
//		return null;
//	}
//
//	@Override
//	public Class getObjectType() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean isSingleton() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//}
