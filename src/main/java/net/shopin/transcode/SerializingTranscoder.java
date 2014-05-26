package net.shopin.transcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 * 说明:
 * User: kongming
 * Date: 14-5-26
 * Time: 下午4:48
 */
public class SerializingTranscoder  implements Transcoder<Object> {


    private static Logger logger = LoggerFactory.getLogger(SerializingTranscoder.class);



    @Override
    public byte[] encodeObject(Object o) {
        byte[] b = null;
        b = serialize(o);
        assert b != null;
        byte[] compressed = compress(b);
        if(compressed.length < b.length){
            logger.debug("Compressed %s from %d to %d",o.getClass().getName(),b.length,compressed.length);
        }else {
            logger.debug("Compression increased the size of %s from %d to %d",o.getClass().getName(),b.length,compressed.length);

        }
        b = compressed;
        return b;
    }



    //----------------------util methods------------
    /**
     * Get the bytes representing the given serialized object.
     */
    protected byte[] serialize(Object o) {
        if(o == null)
            throw new  NullPointerException("Cannot Serialize null");
        byte[] rv = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;


        try {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(o);
            os.close();
            bos.close();
            rv = bos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("Non-serializable object",e);
        } finally {
            CloseUtil.close(os);
            CloseUtil.close(bos);
        }
        return rv;
    }



    /**
     * Compress the given array of bytes.
     */
    protected byte[] compress(byte[] in) {
        if (in == null) {
            throw new NullPointerException("Can't compress null");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gz = null;

        try {
            gz = new GZIPOutputStream(bos);
            gz.write(in);
        } catch (IOException e) {
            throw new RuntimeException("IO exception compressing data ",e);
        } finally {
            CloseUtil.close(gz);
            CloseUtil.close(bos);
        }
        byte[] rv = bos.toByteArray();
        logger.debug("compressed $d bytes to %d",in.length,rv.length);
        return rv;
    }


    //----------------main test method

    public static void main(String[] args) {
        Transcoder<Object> transcoder = new SerializingTranscoder();
        Map map = new HashMap();
        map.put("name","kongming");
        map.put("age",11);
        byte[] rv = transcoder.encodeObject(map);

        System.out.println(rv);
    }




}
