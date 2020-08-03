package Joyming;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class FileWatcher {
    private String path;

    private Timer timer;
    private WatchService watchService;

    private boolean isWatching = false;

    public FileWatcher() {
        timer = new Timer();
    }

    public void startWatcher() {
        isWatching = true;

        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                WatchKey key;
                try {
                    watchService = FileSystems.getDefault().newWatchService();
                    Paths.get(path).register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
                    while (true) {
                        File file = new File(path);//path为监听文件夹
                        File[] files = file.listFiles();
                        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：")
                                .format(System.currentTimeMillis()) + "-------------------等待图片加载中-------------------");
                        key = watchService.take();//没有文件增加时，阻塞在这里
                        for (WatchEvent<?> event : key.pollEvents()) {
                            String fileName = path + "\\" + event.context();
//                            System.out.println("增加文件的文件夹路径" + fileName);
                            File file1 = files[files.length - 1];
                            //获取最新文件
                            if (mOnWatchFileListener != null) {
                                System.out.println("----------------检测到新图片执行打印：" + file1.getName() + "----------------");//根据后缀判断
                                mOnWatchFileListener.setOnWatchFileListener(isWatching, fileName, file1.getName());
                            }
                        }
                        if (!key.reset()) {
                            //中断循环
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：")
                            .format(System.currentTimeMillis()) + "！！！" + e.getMessage());
                }
            }
        }, 2000, 3000);//第一个数字2000表示，2000ms以后开启定时器,第二个数字3000，表示3000ms后运行一次run
    }

    public void cancelWatcher() {
        try {
            isWatching = false;
            timer.cancel();
            timer = null;
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：")
                    .format(System.currentTimeMillis()) + "----------------取消文件夹监听成功----------------");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：")
                    .format(System.currentTimeMillis()) + "！！！" + e.getMessage());
        }
    }

    public void setPath(String path) {
        this.path = path;
    }

    public interface OnWatchFileListener {
        void setOnWatchFileListener(boolean isWatching, String filePath, String fileName);
    }

    private OnWatchFileListener mOnWatchFileListener = null;

    public void setOnWatchFileListener(OnWatchFileListener mOnWatchFileListener) {
        this.mOnWatchFileListener = mOnWatchFileListener;
    }

}
