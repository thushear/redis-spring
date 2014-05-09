/**
 * 
 */
package net.shopin.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author kongming
 *
 */
public class ServerMonitor implements Watcher, Runnable {
	
	private ZooKeeper zooKeeper;
	
	private String connectString;
	
	private int sessionTimeout;
	
	private String hadoopHome;
	
	private String mapredJobTrachker;
	
	
	public ServerMonitor() throws Exception {
		SchedulingServer schedulingServer=new SchedulingServer();
		schedulingServer.initConf();
		schedulingServer.initServer();
	}


	//初始化文件加载，并用其内容配置ZooKeeper服务器的连接
	public void initConf() throws  Exception{
		InitConfReader reader=new InitConfReader("/init.properties");
		List<String> keys=new ArrayList<String>();
		keys.add("connectString");
		keys.add("sessionTimeout");
		Map<String , String>confs=reader.getConfs(keys);
		this.mapredJobTrachker=confs.get("mapred.job.tracker");
		zooKeeper=new ZooKeeper(connectString,sessionTimeout,this);
		
		
	}
	
	public void monitorNode() throws  Exception{
		List<String>  waits=zooKeeper.getChildren("/root/clien/wait" ,false);
		
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		ServerMonitor serverWaitMonitor;
		try {
			serverWaitMonitor = new  ServerMonitor();
			serverWaitMonitor.monitorNode(); Thread.sleep(5000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/* (non-Javadoc)
	 * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
	 */
	@Override
	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub

	}
	
	public static void main(String[] args) throws Exception {
		Thread thread =new  Thread(new  ServerMonitor());
	}
	

}
