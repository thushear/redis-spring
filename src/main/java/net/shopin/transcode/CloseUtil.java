package net.shopin.transcode;

/**
 * 说明:
 * User: kongming
 * Date: 14-5-26
 * Time: 下午4:57
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * CloseUtil exists to provide a safe means to close anything closeable. This
 * prevents exceptions from being thrown from within finally blocks while still
 * providing logging of exceptions that occur during close. Exceptions during
 * the close will be logged using the spy logging infrastructure, but will not
 * be propagated up the stack.
 */
public final   class CloseUtil {
    private static Logger logger = LoggerFactory.getLogger(CloseUtil.class);


    private CloseUtil() {
        // Empty
    }

    /**
     * Close a closeable.
     */
    public static void close(Closeable closeable){
        if(closeable!=null){
            try {
                closeable.close();
            } catch (IOException e) {
                logger.info("Unable to close %s",closeable,e);
            }
        }
    }





}
