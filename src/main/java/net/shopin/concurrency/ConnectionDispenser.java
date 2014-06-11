package net.shopin.concurrency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 说明:     Using ThreadLocal to ensure thread confinement
 *        使用ThreadLocal来做线程限制
 * User: kongming
 * Date: 14-6-11
 * Time: 上午11:37
 */
public class ConnectionDispenser {

    static String DB_URL = "jdbc:mysql://";

    private ThreadLocal<Connection> connectionHolder =
            new ThreadLocal<Connection>(){
                public Connection initValue(){
                    try {
                        return DriverManager.getConnection(DB_URL);
                    } catch (SQLException e) {
                        throw new RuntimeException("unable to acquire connection",e);
                    }
                }
            };



    public Connection getConnection(){
        return connectionHolder.get();
    }



}
