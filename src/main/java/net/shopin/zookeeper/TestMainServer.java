/**
 * 
 */
package net.shopin.zookeeper;

import java.io.File;
import java.io.IOException;

import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;

/**
 * @author kongming
 *
 */
public class TestMainServer extends ZooKeeperServerMain{
	
	
	public static final int CLIENT_PORT = 2181;
	
	public static class MainThread extends Thread{
		File confFile;
	    TestMainServer main;
		
		public MainThread(int clientPort) {
			super("standalone server with ports:" + clientPort);
			main = new TestMainServer();
			confFile = new File("D:\\zookeeper\\server1\\zookeeper-3.4.6\\conf\\zoo.cfg");
		}

		@Override
		public void run() {
			String args[] = new String[1];
			args[0] = confFile.toString();
			try {
				main.initializeAndRun(args);
			} catch (ConfigException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	public static void start(){
		MainThread main = null;
		
		main = new MainThread(CLIENT_PORT);
		
		main.start();
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestMainServer.start();
	}

}
