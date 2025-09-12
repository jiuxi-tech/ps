package com.jiuxi.captcha.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.RandomUtil;
import com.jiuxi.common.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * @ClassName: CaptchaUtil
 * @Description: 验证码工具类
 * @Author: 杨攀
 * @Date: 2022/12/12 16:43
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class ImageCaptchaUtil {


    private static final Logger LOGGER = LoggerFactory.getLogger(ImageCaptchaUtil.class);

    public static final String TYPE_JPG = "jpg";

    public static final String TYPE_JPEG = "jpeg";

    public static final String TYPE_PNG = "png";

    /**
     * 初始化 png 的图片
     * @author 杨攀
     * @date 2022/12/13 16:40
     * @param cacheMap 缓存map
     * @param dir 目录
     * @param size 缓存图片的数量
     * @return void
     */
    public static void initImage(Map<String, BufferedImage> cacheMap, String dir, int size){

        ClassLoader classLoader = ImageCaptchaUtil.class.getClassLoader();
        for (int i = 0; i < size; i++) {
            // 文件名，用下标
            String filename = String.valueOf(i);
            // 图片路径
            String imgPath = dir.concat("/").concat(filename.concat(".png"));
            InputStream resourceAsStream = classLoader.getResourceAsStream(imgPath);
            BufferedImage bufferedImage = ImgUtil.read(resourceAsStream);
            // 原图
            cacheMap.put(filename, bufferedImage);
        }
    }


    /**
     * 拷贝图片
     *
     * @param img       img
     * @param imageType imageType
     * @return BufferedImage
     */
    public static BufferedImage copyImage(Image img, int imageType) {
        return copyImage(img, imageType, null);
    }

    /**
     * 拷贝图片
     *
     * @param img             img
     * @param imageType       imageType
     * @param backgroundColor backgroundColor
     * @return BufferedImage
     */
    public static BufferedImage copyImage(Image img, int imageType, Color backgroundColor) {
        final BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), imageType);
        final Graphics2D bGr = createGraphics(bimage, backgroundColor);
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }


    /**
     * 创建画板
     *
     * @param image image
     * @param color color
     * @return Graphics2D
     */
    public static Graphics2D createGraphics(BufferedImage image, Color color) {
        final Graphics2D g = image.createGraphics();
        if (null != color) {
            // 填充背景
            g.setColor(color);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
        }
        return g;
    }


    /**
     * 后缀是否是jpg
     *
     * @param type type
     * @return boolean
     */
    public static boolean isJpeg(String type) {
        return TYPE_JPG.equalsIgnoreCase(type) || TYPE_JPEG.equalsIgnoreCase(type);
    }

    /**
     * 后缀是否是 png
     *
     * @param type type
     * @return boolean
     */
    public static boolean isPng(String type) {
        return TYPE_PNG.equalsIgnoreCase(type);
    }

    /**
     * 随机选一个图片, 同时返回新的BufferedImage对象, 避免对原始图片修改
     * @author 杨攀
     * @date 2022/12/13 16:57
     * @param cacheMap 缓存map
     * @return java.awt.image.BufferedImage
     */
    public static BufferedImage randomImage(Map<String, BufferedImage> cacheMap) {
        int randomInt = RandomUtil.randomInt(cacheMap.size());
        BufferedImage bufferedImage =  cacheMap.get(String.valueOf(randomInt));
        return createBufferedImage(bufferedImage);
    }

    /**
     * 创建一个新的BufferedImage对象, 避免对原始图片修改
     * @author 杨攀
     * @date 2022/12/5 14:16
     * @param src
     * @return java.awt.image.BufferedImage
     */
    public static BufferedImage createBufferedImage(BufferedImage src){
        ColorModel cm = src.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = src.copyData(src.getRaster().createCompatibleWritableRaster());
        BufferedImage  image = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        return image;
    }

    /**
     * BufferedImage 转 Base64Str
     * @author 杨攀
     * @date 2022/12/13 17:39
     * @param backgroundImage
     * @param formatName 文件格式，如： jpg
     * @return java.lang.String
     */
    public static String bufferedImageToBase64Str(BufferedImage backgroundImage, String formatName) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(backgroundImage, formatName, stream);
            String base64 = Base64.encode(stream.toByteArray());
            return base64;
        } catch (IOException e) {
            LOGGER.error("BufferedImage转Base64字符串失败！原因:", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    /**
     * 分隔图片
     *
     * @param pos       分隔点
     * @param direction true为水平方向， false为垂直方向
     * @param img       待分割的图片
     * @return BufferedImage[]
     */
    public static BufferedImage[] splitImage(int pos, boolean direction, BufferedImage img) {
        int startImageWidth;
        int startImageHeight;
        int endImageWidth;
        int endImageHeight;
        int endScanX;
        int endScanY;
        if (direction) {
            startImageHeight = img.getHeight() - pos;
            startImageWidth = img.getWidth();
            endImageWidth = img.getWidth();
            endImageHeight = pos;
            endScanX = 0;
            endScanY = startImageHeight;
        } else {
            startImageWidth = pos;
            startImageHeight = img.getHeight();
            endImageWidth = img.getWidth() - startImageWidth;
            endImageHeight = img.getHeight();
            endScanX = pos;
            endScanY = 0;
        }

        // start
        int[] rgbArr = new int[startImageWidth * startImageHeight];
        img.getRGB(0, 0, startImageWidth, startImageHeight, rgbArr, 0, startImageWidth);
        int type = img.getColorModel().getTransparency();
        BufferedImage startImg = new BufferedImage(startImageWidth, startImageHeight, type);
        startImg.setRGB(0, 0, startImageWidth, startImageHeight, rgbArr, 0, startImageWidth);
        // end
        rgbArr = new int[endImageWidth * endImageHeight];
        img.getRGB(endScanX, endScanY, endImageWidth, endImageHeight, rgbArr, 0, endImageWidth);
        BufferedImage endImg = new BufferedImage(endImageWidth, endImageHeight, type);
        endImg.setRGB(0, 0, endImageWidth, endImageHeight, rgbArr, 0, endImageWidth);

        BufferedImage[] splitImageArr = new BufferedImage[2];
        splitImageArr[0] = startImg;
        splitImageArr[1] = endImg;
        return splitImageArr;
    }

    /**
     * 拼接图片
     *
     * @param direction true为水平方向， false为垂直方向
     * @param width     拼接后图片宽度
     * @param height    拼接后图片高度
     * @param imgArr    拼接的图片数组
     * @return BufferedImage
     */
    public static BufferedImage concatImage(boolean direction, int width, int height, BufferedImage... imgArr) {
        int pos = 0;
        BufferedImage newImage = new BufferedImage(width, height, imgArr[0].getColorModel().getTransparency());
        for (BufferedImage img : imgArr) {
            int[] rgbArr = new int[width * height];
            img.getRGB(0, 0, img.getWidth(), img.getHeight(), rgbArr, 0, img.getWidth());
            if (direction) {
                newImage.setRGB(pos, 0, img.getWidth(), img.getHeight(), rgbArr, 0, img.getWidth());
                pos += img.getWidth();
                // 水平方向
            } else {
                // 垂直方向
                newImage.setRGB(0, pos, img.getWidth(), img.getHeight(), rgbArr, 0, img.getWidth());
                pos += img.getHeight();
            }
        }
        return newImage;
    }


    /**
     * 图片覆盖（覆盖图压缩到width*height大小，覆盖到底图上）
     *
     * @param baseBufferedImage  底图
     * @param coverBufferedImage 覆盖图
     * @param x                  起始x轴
     * @param y                  起始y轴
     */
    public static void overlayImage(BufferedImage baseBufferedImage, BufferedImage coverBufferedImage, int x, int y) {
        // 创建Graphics2D对象，用在底图对象上绘图
        Graphics2D g2d = baseBufferedImage.createGraphics();
        // 绘制
        g2d.drawImage(coverBufferedImage, x, y, coverBufferedImage.getWidth(), coverBufferedImage.getHeight(), null);
        // 释放图形上下文使用的系统资源
        g2d.dispose();
    }


    /**
     * 居中图片覆盖且旋转覆盖的图片
     * @author 杨攀
     * @date 2022/12/13 20:27
     * @param baseBufferedImage
     * @param coverBufferedImage
     * @param degree
     * @return void
     */
    public static void centerOverlayAndRotateImage(BufferedImage baseBufferedImage, BufferedImage coverBufferedImage, final double degree) {
        coverBufferedImage = rotateImage(coverBufferedImage, degree);
        int bw = baseBufferedImage.getWidth();
        int bh = baseBufferedImage.getHeight();
        int cw = coverBufferedImage.getWidth();
        int ch = coverBufferedImage.getHeight();
        overlayImage(baseBufferedImage, coverBufferedImage, bw / 2 - cw / 2, bh / 2 - ch / 2);
    }


    /**
     * 居中覆盖
     * @author 杨攀
     * @date 2023/1/3 15:05
     * @param baseBufferedImage  底图
     * @param coverBufferedImage 覆盖图
     * @return void
     */
    public static void centerOverlay(BufferedImage baseBufferedImage, BufferedImage coverBufferedImage) {
        int bw = baseBufferedImage.getWidth();
        int bh = baseBufferedImage.getHeight();
        int cw = coverBufferedImage.getWidth();
        int ch = coverBufferedImage.getHeight();
        overlayImage(baseBufferedImage, coverBufferedImage, bw / 2 - cw / 2, bh / 2 - ch / 2);
    }

    /**
     * 旋转图片, 返回复制好的图片，原图片依然没有变，没有旋转，下次还可以使用。
     *
     * @param bufferedimage
     * @param degree
     * @return 返回复制好的图片，原图片依然没有变，没有旋转，下次还可以使用。
     */
    public static BufferedImage rotateImage(final BufferedImage bufferedimage,
                                            final double degree) {
        // 得到图片宽度。
        int w = bufferedimage.getWidth();
        // 得到图片高度。
        int h = bufferedimage.getHeight();
        // 得到图片透明度。
        int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage img;// 空的图片。
        Graphics2D graphics2d;// 空的画笔。
        (graphics2d = (img = new BufferedImage(w, h, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // 旋转，degree是整型，度数，比如垂直90度。
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
        // 从bufferedimagecopy图片至img，0,0是img的坐标。
        graphics2d.drawImage(bufferedimage, 0, 0, null);
        graphics2d.dispose();
        // 返回复制好的图片，原图片依然没有变，没有旋转，下次还可以使用。
        return img;
    }



    /**
     * 通过模板图片抠图（不透明部分）
     *
     * @param origin   源图片
     * @param template 模板图片
     * @param x        坐标轴x
     * @param y        坐标轴y
     * @return BufferedImage
     */
    public static BufferedImage cutImage(BufferedImage origin, BufferedImage template, int x, int y) throws InterruptedException {
        int bw = template.getWidth(null);
        int bh = template.getHeight(null);
        int lw = origin.getWidth(null);
        int lh = origin.getHeight(null);
        //得到透明的区域(人物轮廓)
        Shape imageShape = getImageShape(template, false);
        //合成后的图片
        BufferedImage image = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        //设置画布为透明
        image = graphics.getDeviceConfiguration().createCompatibleImage(bw, bh, Transparency.TRANSLUCENT);
        graphics.dispose();
        Graphics2D graphics2 = image.createGraphics();
        //取交集(限制可以画的范围为shape的范围)
        graphics2.clip(imageShape);
        //抗锯齿
        graphics2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        graphics2.drawImage(origin, -x, -y, lw, lh, null);
        graphics2.dispose();
        return image;
    }


    /**
     * 将Image图像中的透明/不透明部分转换为Shape图形
     *
     * @param img         图片信息
     * @param transparent 是否透明
     * @return Shape
     * @throws InterruptedException 异常
     */
    public static Shape getImageShape(Image img, boolean transparent) throws InterruptedException {
        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();
        int width = img.getWidth(null);
        int height = img.getHeight(null);

        // 首先获取图像所有的像素信息
        PixelGrabber pgr = new PixelGrabber(img, 0, 0, -1, -1, true);
        pgr.grabPixels();
        int[] pixels = (int[]) pgr.getPixels();

        // 循环像素
        for (int i = 0; i < pixels.length; i++) {
            // 筛选，将不透明的像素的坐标加入到坐标ArrayList x和y中
            int alpha = (pixels[i] >> 24) & 0xff;
            if (alpha != 0) {
                x.add(i % width > 0 ? i % width - 1 : 0);
                y.add(i % width == 0 ? (i == 0 ? 0 : i / width - 1) : i / width);
            }
        }

        // 建立图像矩阵并初始化(0为透明,1为不透明)
        int[][] matrix = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = 0;
            }
        }

        // 导入坐标ArrayList中的不透明坐标信息
        for (int c = 0; c < x.size(); c++) {
            matrix[y.get(c)][x.get(c)] = 1;
        }

        /*
         * 逐一水平"扫描"图像矩阵的每一行，将透明（这里也可以取不透明的）的像素生成为Rectangle，
         * 再将每一行的Rectangle通过Area类的rec对象进行合并， 最后形成一个完整的Shape图形
         */
        Area rec = new Area();
        int temp = 0;
        //生成Shape时是1取透明区域还是取非透明区域的flag
        int flag = transparent ? 0 : 1;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (matrix[i][j] == flag) {
                    if (temp == 0) {
                        temp = j;
                    }
                } else {
                    if (temp != 0) {
                        rec.add(new Area(new Rectangle(temp, i, j - temp, 1)));
                        temp = 0;
                    }
                }
            }
            temp = 0;
        }
        return rec;
    }
}
