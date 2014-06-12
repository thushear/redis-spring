package net.shopin.concurrency;
/**
 * Preloader
 *
 * Using FutureTask to preload data that is needed later
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-12
 * Time: 下午2:26
 */
public class Preloader {
    interface ProductInfo{}

    ProductInfo loadProductInfo()throws DataLoadException{
        return null;
    }

    private final FutureTask<ProductInfo> future = new FutureTask<ProductInfo>(new Callable<ProductInfo>() {
        @Override
        public ProductInfo call() throws Exception {
            return loadProductInfo();  //To change body of implemented methods use File | Settings | File Templates.
        }
    });

    private final Thread thread = new Thread(future);

    public void start(){
        thread.start();
    }

    public ProductInfo get() throws DataLoadException, InterruptedException {

        try {
            return future.get();

        } catch (ExecutionException e) {
             Throwable cause = e.getCause();
            if(cause instanceof DataLoadException)
                throw (DataLoadException)cause;
            else
                throw LaunderThrowable.launderThrowable(cause);
        }
    }


    public static void main(String[] args) throws InterruptedException, DataLoadException {
        Preloader loader = new Preloader();
        loader.start();
        System.out.println(loader.get());
    }


}

class DataLoadException extends Exception{

}