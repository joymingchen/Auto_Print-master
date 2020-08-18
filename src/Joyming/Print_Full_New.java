package Joyming;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.print.*;
import javax.print.attribute.Attribute;
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
import java.awt.image.Raster;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Stack;

import static javax.imageio.ImageIO.read;

public class Print_Full_New {


    static int i = 0;

    //    static Stack<File> aStack = new Stack<File>();
    static String path1 = "";

    private String time;

    /**
     * 自定义文字内容
     */
    private String content = "";

    private boolean isRotate = false;

    public Print_Full_New() {
        long timeMillis = System.currentTimeMillis();
        time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：").format(timeMillis);
    }

    public void doPrintfJob(String filePath) throws IOException {
        File aFile = new File(filePath);
        if (aFile.exists() && aFile.isDirectory()) {
            Stack<File> aStack = new Stack<File>();
            Find_Picture.getAllFile(aFile, aStack);

            int len = aStack.size();
            if (len == 0) {
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：").format(System.currentTimeMillis()) + "！！！该文件夹没有图片文件！！！请重新选择文件夹");
            } else {
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：").format(System.currentTimeMillis()) + "*****一共有" + len + "张图片，现在开始打印：*****");
                for (i = 1; i <= len; i++) {
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：").format(System.currentTimeMillis()) + "一张为1页：第" + i + "页开始打印");
                    try {
                        path1 = aStack.pop().toString();
                        printf(path1);
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


    public void printf(String path) {

        //获取选择的文件
//        File file = new File("C:\\Users\\Joy Chen\\Desktop\\test\\DSC_2820.JPG");
        //构建打印请求属性集
        HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        //A5横向打印
        pras.add(MediaSizeName.ISO_A5);
        pras.add(PrintQuality.HIGH);
        pras.add(OrientationRequested.LANDSCAPE);

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
                //创建打印作业
                DocPrintJob job = service.createPrintJob();
                //构造待打印的文件流
//                FileInputStream fis = new FileInputStream(file);
                DocAttributeSet das = new HashDocAttributeSet();
                //（文件的）横向打印格式
                das.add(OrientationRequested.LANDSCAPE);
                das.add(MediaSizeName.ISO_A5);
                das.add(PrintQuality.HIGH);

                MediaPrintableArea area = new MediaPrintableArea(1f, 10f, 210, 148 + 40, MediaPrintableArea.MM);
                das.add(area);

//                ByteArrayInputStream inputStream = getinputstream(path);
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：")
                        .format(System.currentTimeMillis()) + path + "  开始打印咯\n");

                if (isRotate) {
                    Doc doc = new SimpleDoc(getinputstream2(path), flavor, das);
                    job.print(doc, pras);
                } else {
                    Doc doc = new SimpleDoc(getinputstream(path), flavor, das);
                    job.print(doc, pras);
                }

//                inputStream.close();

                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：")
                        .format(System.currentTimeMillis()) + path + "  打印结束咯\n");

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

    /**
     * 旋转 90 度
     *
     * @param pathFileName
     * @return
     * @throws Exception
     */
    public ByteArrayInputStream getinputstream2(String pathFileName) throws Exception {

        float scale = 1f;
        float a5Width = 210 * scale;
        float a5Height = 148 * scale;

        // 在内存中创建图象
        int width = (int) (a5Width * 18), height = (int) (a5Height * 16);


        BufferedImage src = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        // 获取图形上下文
        Graphics2D g = (Graphics2D) src.getGraphics();

        // 设定背景色
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, width, height);

        g.translate(0, 0);


        // 设定字体
        g.setColor(Color.RED);
        //旋转字体
        Font font = new Font("宋体", Font.BOLD, 100);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(-90), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        g.setFont(rotatedFont);

        //第一行文字
        String first = new SimpleDateFormat("yyyy.MM.dd   HH:mm").format(System.currentTimeMillis());
        FontMetrics fm = g.getFontMetrics(font);
        int fontWidth = fm.stringWidth(first);
        int fontHeight = fm.getHeight();
        float fontX = width - fontHeight;
        float fontY = (height + fontWidth) / 2;
//        g.drawString(first, fontHeight + 100, fontX);
        g.drawString(first, fontX, fontY);

        //第二行文字
        //间距
        int paddingTop = 20;
        String second = content;
        FontMetrics fm2 = g.getFontMetrics(font);
        int fontWidth2 = fm2.stringWidth(second);
        int fontHeight2 = fm2.getHeight();
        float fontX2 = width - fontHeight - paddingTop - fontHeight2;
        float fontY2 = (height + fontWidth2) / 2;
        g.drawString(second, fontX2, fontY2);

        //画照片
        BufferedImage image = read(new File(pathFileName));
//        g.rotate(Math.toRadians(180), width / 2, height / 2);

        float x = fontX2 - fontHeight2;
        //画图
        g.drawImage(image, 0, 0, (int) x, height, null);

        g.drawString(first, fontX, fontY);

        g.drawString(second, fontX2, fontY2);


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

    /**
     * 旋转 90 度
     *
     * @param pathFileName
     * @return
     * @throws Exception
     */
    public ByteArrayInputStream getinputstream(String pathFileName) throws Exception {

        float scale = 1f;
        float a5Width = 210 * scale;
        float a5Height = 148 * scale;

        // 在内存中创建图象
        int width = (int) (a5Width * 18), height = (int) (a5Height * 16);

        BufferedImage src = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        // 获取图形上下文
        Graphics2D g = (Graphics2D) src.getGraphics();

        // 设定背景色
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, width, height);

        g.translate(0, 0);


        // 设定字体
        g.setColor(Color.RED);
        //旋转字体
        Font font = new Font("宋体", Font.BOLD, 100);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(90), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        g.setFont(rotatedFont);

        //第一行文字
        String first = new SimpleDateFormat("yyyy.MM.dd   HH:mm").format(System.currentTimeMillis());
        FontMetrics fm = g.getFontMetrics(font);
        int fontWidth = fm.stringWidth(first);
        int fontHeight = fm.getHeight();

        //第二行文字
        //间距
        int paddingTop = 20;
        String second = content;
        FontMetrics fm2 = g.getFontMetrics(font);
        int fontWidth2 = fm2.stringWidth(second);
        int fontHeight2 = fm2.getHeight();


        float fontX = fontHeight + paddingTop + fontHeight2;
        float fontY = (height - fontWidth) / 2;

        float fontX2 = paddingTop + fontHeight2;
        float fontY2 = (height - fontWidth2) / 2;


        //画第一行文字
        g.drawString(first, fontX, fontY);

        //画第二行文字
        g.drawString(second, fontX2, fontY2);


        //画照片
        BufferedImage image = read(new File(pathFileName));
//        g.rotate(Math.toRadians(180), width / 2, height / 2);

        int angle = 0;
        Metadata metadata;
        metadata = ImageMetadataReader.readMetadata(new File(pathFileName));
        Directory directory = metadata.getDirectory(ExifDirectory.class);
        if (directory.containsTag(ExifDirectory.TAG_ORIENTATION)) {

            // Exif信息中方向　　
            int orientation = directory.getInt(ExifDirectory.TAG_ORIENTATION);

            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：")
                    .format(System.currentTimeMillis()) + " 照片的方向信息：" + orientation);

            // 原图片的方向信息
            if (6 == orientation) {
                //6旋转90
                angle = 180;
            } else if (3 == orientation) {
                //3旋转180
                angle = 180;
            }
        }

        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);

        Rectangle rect_des = new Rectangle(new Dimension(src_width, src_height));

        g.translate((rect_des.width - src_width) / 2,
                (rect_des.height - src_height) / 2);
        g.rotate(Math.toRadians(angle), src_width / 2, src_height / 2);

        float x = fontX + fontHeight;

        g.translate(0, 0);

        if (angle == 180) {
            //画图
            g.drawImage(image, 0, 0, (int) (width - x), height, null);
        } else {
            //画图
            g.drawImage(image, (int) x, 0, width, height, null);
        }

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

    /**
     * 获取图片正确显示需要旋转的角度（顺时针）
     *
     * @return
     */
    public static int getRotateAngleForPhoto(String filePath) throws ImageProcessingException, MetadataException {
        File file = new File(filePath);
        int angle = 0;
        Metadata metadata;
        metadata = ImageMetadataReader.readMetadata(file);
        Directory directory = metadata.getDirectory(ExifDirectory.class);
        if (directory.containsTag(ExifDirectory.TAG_ORIENTATION)) {

            // Exif信息中方向　　
            int orientation = directory.getInt(ExifDirectory.TAG_ORIENTATION);

            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ：")
                    .format(System.currentTimeMillis()) + " 照片的方向信息：" + orientation);

            // 原图片的方向信息
            if (6 == orientation) {
                //6旋转90
                angle = 90;
            } else if (3 == orientation) {
                //3旋转180
                angle = 180;
            } else if (8 == orientation) {
                //8旋转90
                angle = 270;
            }
        }

        return angle;
    }

    /**
     * 旋转照片
     *
     * @return
     */
    public static String rotatePhonePhoto(String fullPath, int angel) {

        BufferedImage src;
        try {
            src = ImageIO.read(new File(fullPath));
            int src_width = src.getWidth(null);
            int src_height = src.getHeight(null);

            int swidth = src_width;
            int sheight = src_height;

            if (angel == 90 || angel == 270) {
                swidth = src_height;
                sheight = src_width;
            }

            Rectangle rect_des = new Rectangle(new Dimension(swidth, sheight));

            BufferedImage res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = res.createGraphics();

            g2.translate((rect_des.width - src_width) / 2,
                    (rect_des.height - src_height) / 2);
            g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

            g2.drawImage(src, null, null);

            ImageIO.write(res, "jpg", new File(fullPath));

        } catch (IOException e) {

            e.printStackTrace();
        }

        return fullPath;

    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRotate() {
        return isRotate;
    }

    public void setRotate(boolean rotate) {
        isRotate = rotate;
    }
}


