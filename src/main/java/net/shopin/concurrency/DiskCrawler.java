package net.shopin.concurrency;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * ProducerConsumer
 * <p/>
 * Producer and consumer tasks in a desktop search application
 *
 * @author Brian Goetz and Tim Peierls
 */
/**
 * 说明:
 * User: kongming
 * Date: 14-6-12
 * Time: 上午10:44
 */
public class DiskCrawler {

    /**
     * 桌面搜索 消息生产者
     */
    static class FileCrawler implements Runnable{

        private final BlockingQueue<File> fileQueue;
        private final FileFilter fileFilter;
        private final File root;

        FileCrawler(BlockingQueue<File> fileQueue, final FileFilter fileFilter, File root) {
            this.fileQueue = fileQueue;
            this.root = root;
            this.fileFilter = new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory() || fileFilter.accept(pathname);
                }
            };
        }

        private boolean alreadyIndexed(File f){
            return false;
        }

        private void crawl(File root)throws InterruptedException{
            File[] entries = root.listFiles(fileFilter);
            if(entries!=null){
                for(File entry:entries)
                    if(entry.isDirectory())
                        crawl(entry);
                else if (!alreadyIndexed(entry))
                        fileQueue.put(entry);
            }
        }

        @Override
        public void run() {
            try {
                crawl(root);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 消息消费者
     */
    static class Indexer implements Runnable{
        private final BlockingQueue<File> queue;

        Indexer(BlockingQueue<File> queue) {
            this.queue = queue;
        }
        public void indexFile(File f){

           //....todo
        }

        @Override
        public void run() {

            try {
                while (true)
                    indexFile(queue.take());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static final int BOUND = 10;
    private static final int N_CONSUMERS  = Runtime.getRuntime().availableProcessors();

    public static void startIndexing(File[] roots){
        BlockingQueue<File> queue = new LinkedBlockingQueue<File>(BOUND);
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        //java 参数传递 传引用
        for(File root : roots)
            new Thread(new FileCrawler(queue,filter,root)).start();

        //利用多线程并发处理 生成索引
        for (int i = 0;i<N_CONSUMERS;i++)
            new Thread(new Indexer(queue)).start();

    }


    public static void main(String[] args) {
        File[] files = new File[]{new File("D:\\")};
        startIndexing(files);
    }


}
