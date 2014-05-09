/**
 * 
 */
package net.shopin.zookeeper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author kongming
 *
 */
public class InitConfReader {
	
	

	public InitConfReader(String confFileUrl) {
		super();
		this.confFileUrl = confFileUrl;
	}

	private String confFileUrl;

	public Map<String, String> getConfs(List<String> keys) {
		Map<String, String> result = new HashMap<String, String>();
		Properties properties = new Properties();
		try {
			//从配置文件中读取配置信息,并用读取到的信息作为服务器启动的参数 
			properties.load(new FileReader(new File(confFileUrl)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String key : keys) {
			String value = (String) properties.get(key);
			result.put(key, value);
		}
		return result;
	}
}
