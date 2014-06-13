package net.shopin.concurrency;
/**
 * FutureRenderer
 * <p/>
 * Waiting for image download with \Future
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 说明:并行的执行图像下载和渲染文本 任务
 *     渲染文本是耗Cpu资源的任务
 *     下载图像是耗io的任务
 *
 * User: kongming
 * Date: 14-6-13
 * Time: 下午3:17
 */
public abstract class FutureRenderer {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    void renderPage(CharSequence source){
        final List<SingleThreadRenderer.ImageInfo> imageInfos =   scanForImageInfo(source);
        Callable<List<SingleThreadRenderer.ImageData>> task =
                new Callable<List<SingleThreadRenderer.ImageData>>() {
                    @Override
                    public List<SingleThreadRenderer.ImageData> call() throws Exception {
                        List<SingleThreadRenderer.ImageData> result = new ArrayList<SingleThreadRenderer.ImageData>();
                        for(SingleThreadRenderer.ImageInfo imageInfo : imageInfos)
                            result.add(imageInfo.downloadImage());
                        return result;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                };
        Future<List<SingleThreadRenderer.ImageData>>  future = executor.submit(task);
        renderText(source);

        try {
            List<SingleThreadRenderer.ImageData> imageData= future.get();
            for(SingleThreadRenderer.ImageData data : imageData)
                renderImage(data);
        } catch (InterruptedException e) {
            //re assert threads status
            Thread.currentThread().interrupt();
            future.cancel(true);
        } catch (ExecutionException e) {
            throw LaunderThrowable.launderThrowable(e.getCause());
        }


    }

    abstract void renderImage(SingleThreadRenderer.ImageData i);

    abstract void renderText(CharSequence s);

    abstract List<SingleThreadRenderer.ImageInfo> scanForImageInfo(CharSequence s);
}
