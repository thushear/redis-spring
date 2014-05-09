/**
 * 
 */
package net.shopin.zookeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/**
 * 初始化与维护原始数据节点
 * @author kongming
 *
 */
public class SchedulingServer implements Watcher {
	
	private ZooKeeper zooKeeper;
	
	private String connectString; //conectString连接字符串，包括IP地址，服务器端口号 
	
	private int  sessionTimeout; 
	
	
	public void initConf() throws Exception{
		InitConfReader reader = new InitConfReader("init.properties");
		List<String>keys=new ArrayList<String>(); 
		keys.add("connectSring");
		keys.add("sessionTimeout");
		Map<String ,String>  confs=reader.getConfs(keys); 
		this.connectString=confs.get("connectString");
		this.sessionTimeout=Integer.parseInt(confs.get("sessionTimeout")); 
		zooKeeper=new ZooKeeper(connectString, sessionTimeout ,this); 
	}
	
	
	public void initServer() throws Exception{
		
		//stat用于存储被监测节点是否存在，若不存在则对应的值为null 
		Stat stat = zooKeeper.exists("/root", false);
		if(stat == null){
			//根节点 
			zooKeeper.create("/root", null,Ids.OPEN_ACL_UNSAFE , CreateMode.PERSISTENT);
			//失败任务存储节点
			zooKeeper.create("/root/error",null,Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 
			//成功任务存储节点 
			zooKeeper.create("/root/processed",null,Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 
			//等待和正在运行任务存储节点 
			zooKeeper.create("/root/wait",null,Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
			//临时存储第一次处理失败的节点 
			zooKeeper.create("/root/temp",null,Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 
		}
		
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
	 */
	@Override
	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub

	}
}
