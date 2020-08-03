package Joyming;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;


public class Main_UI extends JFrame {
    private JPanel jPanelNorth;
    private JPanel jPanelCenter;
    private JPanel jPanelBottom;

    private JPanel jPaneln1;
    private JPanel jPaneln2;
    private JPanel jPanelc1;
    private JPanel jPanelc2;
    private JPanel jPanelb1;
    static PrinterJob printerJob = PrinterJob.getPrinterJob();

    private JScrollPane jScrollPane;
    private static String filePath = ".\\";

    private JLabel fullLabel;
    private JTextField fileText;
    private JButton selectFile;
    private JButton printFull;

    private JButton printHalf;

    private JButton printFour;

    private JButton clearAll;
    private JButton test;
    private JButton fileWatcherBtn;
    private JButton cancelfileWatcher;
    private JTextArea tips;

    private JLabel mAuthor;

    private FileWatcher fileWatcher;

    private Print_Full_New printer;


    public static void main(String[] args) {
        new Main_UI();
    }

    public Main_UI() {
        super("自动排版打印工具V1.0");
        this.init();
        this.initListener();
    }

    public void init() {
        {
            jPanelNorth = new JPanel();
            jPanelCenter = new JPanel();
            jPanelBottom = new JPanel();

//            jPanelSouth = new JPanel();
            jPaneln1 = new JPanel();
            jPanelc1 = new JPanel();

            jPanelc2 = new JPanel();
            jPaneln2 = new JPanel();
            jPanelb1 = new JPanel();

            jScrollPane = new JScrollPane();
        }
        {
            fullLabel = new JLabel("文件夹路径：");
            fileText = new JTextField(filePath, 25);
            selectFile = new JButton("选择文件夹");

            jPanelNorth.add(fullLabel);
            jPanelNorth.add(fileText);
            jPanelNorth.add(selectFile);
        }
        {
            printFull = new JButton("一张图片打印成一页");
            printHalf = new JButton("两张图片打印成一页");
            printFour = new JButton("四张图片打印成一页");

            jPaneln1.add(printFull);
            jPaneln1.add(printHalf);
            jPaneln1.add(printFour);

        }
        {
            clearAll = new JButton("清空记录");
            test = new JButton("Test");
            fileWatcherBtn = new JButton("文件夹监听");
            cancelfileWatcher = new JButton("取消监听");
            jPaneln2.add(clearAll);
            jPaneln2.add(test);
            jPaneln2.add(fileWatcherBtn);
            jPaneln2.add(cancelfileWatcher);
        }
        {
            tips = new JTextArea(16, 45);
            tips.setText("--------------说明----------------\n\n" +
                    "1.选定文件夹执行监听即可；\n" +
                    "\n" +
                    "2.一张图片打印成一页\n" +
                    "\n" +
//                    "3.两张图片打印成一页：适合打印发票那样大小的图片或pdf；\n" +
//                    "\n" +
//                    "4.四张图片打印成一页：适合打印手机截图那样大小的图片或pdf；\n" +
//                    "\n" +
//                    "5.打印前最好先了解一下打印排版是怎样的。\n" +
                    "--------------------------------\n\n");
            JTextAreaOutputStream out = new JTextAreaOutputStream(tips);
            System.setOut(new PrintStream(out));//设置输出重定向
            tips.setLocation(10, 300);
            tips.setLineWrap(true);
            tips.setEditable(false);
            tips.setSelectionStart(tips.getText().length());
            jScrollPane = new JScrollPane(tips);
//            jPanelSouth.add(clearAll);
            jPanelc2.add(jScrollPane);

            mAuthor = new JLabel();
            mAuthor.setText("joymingchen@foxmail.com");

            jPanelb1.add(mAuthor);
        }
        {
            this.setLayout(new BorderLayout());
            this.add(jPanelNorth, BorderLayout.NORTH);
            this.add(jPanelCenter, BorderLayout.CENTER);
            this.add(jPanelBottom, BorderLayout.SOUTH);

            jPanelCenter.setLayout(new BorderLayout());
//            jPanelCenter.add(jPaneln1, BorderLayout.NORTH);
            jPanelCenter.add(jPanelc1, BorderLayout.CENTER);

            jPanelc1.setLayout(new BorderLayout());
            jPanelc1.add(jPaneln2, BorderLayout.NORTH);
            jPanelc1.add(jPanelc2, BorderLayout.CENTER);

            jPanelBottom.setLayout(new BorderLayout());
            jPanelBottom.add(jPanelb1, BorderLayout.CENTER);

//            jPanelb1.setLayout(new BorderLayout());
//            jPanelb1.add(jPanelb1, BorderLayout.SOUTH);

//            this.add(jPanelCenter, BorderLayout.SOUTH);
            this.setVisible(true);
//            this.setContentPane(jPanel);
//            this.add(jPanel);
            this.setSize(560, 420);
            // 屏幕居中
            int windowWidth = this.getWidth(); // 获得窗口宽
            int windowHeight = this.getHeight(); // 获得窗口高
            Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
            Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
            int screenWidth = screenSize.width; // 获取屏幕的宽
            int screenHeight = screenSize.height; // 获取屏幕的高
            this.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight
                    / 2 - windowHeight / 2);

            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    public void initListener() {


        selectFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFile(fileText);
            }
        });

        printFull.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jobListener(fileText, "full");
            }
        });


        printHalf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jobListener(fileText, "half");
            }
        });


        printFour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jobListener(fileText, "four");
            }
        });

        clearAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tips.setText("");
            }
        });

        test.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
//                    boolean a = printerJob.printDialog();
                    if (true) {
                        new Print_Full_New().doPrintfJob(fileText.getText());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        fileWatcherBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (fileWatcher == null) {
                    try {
                        fileWatcher = new FileWatcher();
                    } catch (Exception e) {
                        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：")
                                .format(System.currentTimeMillis()) + "！！！" + e.getMessage());
                    }
                }

                //文件路径
                fileWatcher.setPath(fileText.getText());
                //开启监听
                fileWatcher.startWatcher();

                //有新文件进入时的回调
                fileWatcher.setOnWatchFileListener(new FileWatcher.OnWatchFileListener() {
                    @Override
                    public void setOnWatchFileListener(boolean isWatching, String filePath,String fileName) {
                        if(isWatching){
                            //执行打印
                            try {
                                if(printer == null){
                                    printer = new Print_Full_New();
                                }
                                printer.printf(filePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

        cancelfileWatcher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //取消监听
                fileWatcher.cancelWatcher();
            }
        });
    }

    public void selectFile(JTextField jTextField) {
        JFileChooser jFileChooser = new JFileChooser(".");
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = jFileChooser.showOpenDialog(null);
        if (result == jFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            String path = file.getAbsolutePath();
            jTextField.setText(path);
        } else {
        }
    }

    public void jobListener(final JTextField jTextField, final String string) {
        boolean a = printerJob.printDialog();
        if (a) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (string.equals("full")) {
                            new Print_Full().printFull(jTextField.getText(), printerJob);
                        } else if (string.equals("half")) {
                            new Print_Half().printHalf(jTextField.getText(), printerJob);
                        } else {
                            new Print_Four().printFour(jTextField.getText(), printerJob);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println(new SimpleDateFormat
                                ("yyyy-MM-dd HH:mm:ss ：").format(System.currentTimeMillis()) + "***" + ex.getMessage());
                    }
                }
            }).start();
        } else {
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：").format(System.currentTimeMillis()) + "***你取消了打印***");
        }
    }
}
