package net.shopin.transcode;

/**
 * 说明:  Transcoder is an interface for classes that convert between byte arrays and    objects for storage in the cache.
 * User: kongming
 * Date: 14-5-26
 * Time: 下午4:46
 */
public interface Transcoder<T> {


    /**
     * ecode object to byte arrays
     * @param o
     * @return
     */
    byte[] encodeObject(Object o);



}
