package net.shopin.concurrency;

/**
 * SingleThreadRendere
 * <p/>
 * Rendering page elements sequentially
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:模拟浏览器顺序花渲染过程
 *      更好的利用CPU 并发性
 *      先渲染文本 占位图片
 *      并发下载图片渲染
 * User: kongming
 * Date: 14-6-13
 * Time: 下午3:03
 */
public abstract class SingleThreadRenderer {

    void renderPage(CharSequence source){
        renderText(source);
        List<ImageData> imageData = new ArrayList<ImageData>();
        for(ImageInfo imageInfo:scanForImageInfo(source))
            imageData.add(imageInfo.downloadImage());
        for (ImageData data : imageData)
            renderImage(data);
    }

    abstract void renderText(CharSequence s);
    abstract List<ImageInfo> scanForImageInfo(CharSequence s);
    abstract void renderImage(ImageData i);

    interface ImageData{}

    interface  ImageInfo{
        ImageData downloadImage();
    }

}
