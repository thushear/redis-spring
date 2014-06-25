package net.shopin.concurrency;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-23
 * Time: 下午6:33
 */
public class AttributeStore {

     private final Map<String,String > attributes = new HashMap<String, String>();


    public synchronized boolean userLocationMatches(String name,String regexp){
        String key = "user." + name + ".location";
        String location = attributes.get(key);
        if(location == null)
            return false;
        else
            return Pattern.matches(regexp,location);
    }


    public static void main(String[] args) {


    }




}
