package net.shopin.concurrency;

/**
 * Renderer
 * <p/>
 * Using CompletionService to render page elements as they become available
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.util.List;
import java.util.concurrent.*;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-16
 * Time: 上午9:49
 */
public abstract class Renderer {

    private final ExecutorService executor;


    protected Renderer(ExecutorService executor) {
        this.executor = executor;
    }


    void renderPage(CharSequence source){
        final List<ImageInfo> info = scanForImageInfo(source);
        CompletionService<ImageData> completionService = new ExecutorCompletionService<ImageData>(executor);
        for (final ImageInfo imageInfo : info){
            completionService.submit(new Callable<ImageData>() {
                @Override
                public ImageData call() throws Exception {

                    return imageInfo.downloadImage();  //To change body of implemented methods use File | Settings | File Templates.
                }
            });
        }
        renderText(source);
        try {
        for (int i = 0,n = info.size();i<n;i++){
                Future<ImageData> f = completionService.take();
                ImageData imageData = f.get();
                renderImage(imageData);
        }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw LaunderThrowable.launderThrowable(e.getCause());
        }
    }

    abstract void renderImage(ImageData i);

    abstract void renderText(CharSequence s);

    abstract List<ImageInfo> scanForImageInfo(CharSequence s);

    interface ImageData {
    }

    interface ImageInfo {
        ImageData downloadImage();
    }
}
