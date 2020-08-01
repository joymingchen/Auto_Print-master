package Joyming;



import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.awt.print.PrinterException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Stack;

import static javax.imageio.ImageIO.read;

public class Print_Full {


    static int i = 0;

    //    static Stack<File> aStack = new Stack<File>();
    static String path1 = "";

    public void printFull(String filePath, PrinterJob printerJob) throws PrinterException, IOException {

        File aFile = new File(filePath);
        if (aFile.exists() && aFile.isDirectory()) {
            Stack<File> aStack = new Stack<File>();
            Find_Picture.getAllFile(aFile, aStack);
            //System.out.println(aStack.size());
            int len = aStack.size();
//            boolean a = printerJob.printDialog();
            if (len == 0) {
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：").format(System.currentTimeMillis()) + "！！！该文件夹没有图片文件！！！请重新选择文件夹");
            } else {
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：").format(System.currentTimeMillis()) + "*****一共有" + len + "张图片，现在开始打印：*****");
                for (i = 1; i <= len; i++) {
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：").format(System.currentTimeMillis()) + "一张为1页：第" + i + "页开始打印");
                    try {
                        path1 = aStack.pop().toString();
                        printf();
                    } finally {
                        path1 = "";
                    }
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：").format(System.currentTimeMillis()) + "一张为1页：第" + i + "页打印结束咯");
                }
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：").format(System.currentTimeMillis()) + "*****一张为1页已经全部打印完成，共" + len + "页！！！*****\n\n");
            }
        } else {
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：").format(System.currentTimeMillis()) + "！！！文件夹为空或不存在！！！请重新选择文件夹");
        }
    }


    public void printf() {

        //获取选择的文件
//        File file = new File("C:\\Users\\Joy Chen\\Desktop\\test\\DSC_2820.JPG");
        //构建打印请求属性集
        HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        //A5横向打印
        pras.add(MediaSizeName.ISO_A5);

        //设置打印格式，因为未确定类型，所以选择autosense
        DocFlavor flavor = DocFlavor.INPUT_STREAM.JPEG;

        //查找所有的可用的打印服务
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
        //定位默认的打印服务
        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();

//        for(PrintService service :printService){
//            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：")
//                    .format(System.currentTimeMillis()) + "!!!" + service.getName());
//        }

        //显示打印对话框
//        PrintService service = ServiceUI.printDialog(null, 200, 200, printService,
//                defaultService, flavor, pras);
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();

        if (service != null) {
            try {
                DocPrintJob job = service.createPrintJob(); //创建打印作业
//                FileInputStream fis = new FileInputStream(file); //构造待打印的文件流
                DocAttributeSet das = new HashDocAttributeSet();
                das.add(OrientationRequested.LANDSCAPE);//（文件的）横向打印格式
                das.add(MediaSizeName.ISO_A5);
                das.add(PrintQuality.HIGH);

                MediaPrintableArea area = new MediaPrintableArea(1f, 10f, 210 , 148 * 1.2f, MediaPrintableArea.MM);
                das.add(area);

                Doc doc = new SimpleDoc(getinputstream(), flavor, das);
                job.print(doc, pras);
                getinputstream().close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：")
                        .format(System.currentTimeMillis()) + "！！！" + e.getMessage());
            }
        } else {
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：")
                    .format(System.currentTimeMillis()) + "！！！找不到默认打印机服务！！！请检查设置");
        }
    }


    public static ByteArrayInputStream getinputstream() throws Exception {
        // ByteArrayInputStream inputStream;

        float scale = 1f;
        float a5Width = 210 * scale;
        float a5Height = 148 * scale;


        // 在内存中创建图象
        int width = (int) (a5Width * 15), height = (int) (a5Height * 15);

//        Image src = read(new FileInputStream(new File("C:\\Users\\Joy Chen\\Desktop\\test\\DSC_2820.JPG")));
//
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：")
//                .format(System.currentTimeMillis()) + "！！！ width:" + src.getWidth(null)
//                +" height:"+ src.getHeight(null));
        BufferedImage image = read(new File(path1));

        BufferedImage src = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        // 获取图形上下文
        Graphics2D g = (Graphics2D) src.getGraphics();

        // 设定背景色
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, width, height);

//        g.setColor(new Color(125, 125, 255));
//        g.drawLine(0, 0, width, height);
//        // 设定字体
//        g.setFont(new Font("宋体", Font.BOLD, 18));
//        // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
//        g.setColor(new Color(0, 0, 0));
//
//        // g.drawLine(0,0,50,50);

        //画图
        g.drawImage(image, 400, 0, width, height, null);

        // 设定字体
        g.setColor(Color.RED);

        //旋转字体
        Font font = new Font("宋体", Font.BOLD, 100);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(90), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        g.setFont(rotatedFont);

        String first = "终南山寨峡谷运动乐园留念";
        FontMetrics fm = g.getFontMetrics(font);
        int fontWidth = fm.stringWidth(first);
        int fontHeight = fm.getHeight();
        float fontX = (height - fontWidth) / 2;
        g.drawString(first, fontHeight + 100, fontX);

        String second = new SimpleDateFormat("yyyy.MM.dd   HH:mm").format(System.currentTimeMillis());
        FontMetrics fm2 = g.getFontMetrics(font);
        int fontWidth2 = fm2.stringWidth(second);
        float fontX2 = (height - fontWidth2) / 2;
        g.drawString(second, 20, fontX2);

        // 图象生效
        g.dispose();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageOutputStream imageOut = ImageIO.createImageOutputStream(output);
        ImageIO.write(src, "JPEG", imageOut);
        imageOut.close();
        ByteArrayInputStream input = new ByteArrayInputStream(
                output.toByteArray());
        return input;
    }

}


