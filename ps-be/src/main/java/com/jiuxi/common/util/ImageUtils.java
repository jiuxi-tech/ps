package com.jiuxi.common.util;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.StrUtil;
import com.jiuxi.common.bean.ImageDraw;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

/**
 * @Description: base64，image相关操作
 * @ClassName: ImageUtils
 * @Author: pdd
 * @Date: 2021-08-20 10:19
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class ImageUtils {


    /**
     * 判断图片类型是否合法
     *
     * @param imageType 图片类型，支持：png,bmp,gif,jpeg,jpg,psd
     * @return java.lang.String
     * @author pand
     * @date 2021-08-18 11:28
     */
    public static String checkImageType(String imageType) {

        if (StrUtil.equals(imageType, ImgUtil.IMAGE_TYPE_PNG)) {
            return ImgUtil.IMAGE_TYPE_PNG;
        } else if (StrUtil.equals(imageType, ImgUtil.IMAGE_TYPE_BMP)) {
            return ImgUtil.IMAGE_TYPE_BMP;
        } else if (StrUtil.equals(imageType, ImgUtil.IMAGE_TYPE_GIF)) {
            return ImgUtil.IMAGE_TYPE_GIF;
        } else if (StrUtil.equals(imageType, ImgUtil.IMAGE_TYPE_JPEG)) {
            return ImgUtil.IMAGE_TYPE_JPEG;
        } else if (StrUtil.equals(imageType, ImgUtil.IMAGE_TYPE_JPG)) {
            return ImgUtil.IMAGE_TYPE_JPG;
        } else if (StrUtil.equals(imageType, ImgUtil.IMAGE_TYPE_PSD)) {
            return ImgUtil.IMAGE_TYPE_PSD;
        } else {
            throw new RuntimeException("不支持的图片类型！");
        }
    }

    /**
     * base64图片截取数据内容
     *
     * @param base64
     * @return int
     * @author pand
     * @date 2021-08-20 10:28
     */
    public static String subBase64(String base64) {
        // 1. 去掉base64头部，如data:image/png;base64,；data:image/jpeg;base64,
        int index = base64.indexOf(";base64,");
        if (index >= 1) {
            index = index + 8;
            base64 = base64.substring(index);
        }
        //2.找到等号，把等号也去掉
        Integer equalIndex = base64.indexOf("=");
        if (base64.indexOf("=") >= 1) {
            base64 = base64.substring(0, equalIndex);
        }
        return base64;
    }


    /**
     * 获取base64图片大小
     *
     * @param base64
     * @return int 图片大小，单位：字节（b）
     * @author pand
     * @date 2021-08-20 10:28
     */
    public static int getSize(String base64) {
        base64 = subBase64(base64);
        int baseLength = base64.length();
        int size = baseLength - (baseLength / 8) * 2;
        return size;
    }


    /**
     * 图片添加文字
     * @author Ypp
     * @date 2022/3/18 20:26 
     * @param srcPath 源图片路径，如： d:/xxxx.jpg
     * @param targetPath 目标图片地址, 如：d:/zzz.jpg
     * @param drawList 需要添加的文字及样式列表，一个坐标一个ImageDraw
     * @return void
     *
     *
     *  String srcPath = "D:\\upload\\zhens.png";
     *  String targetPath = "D:\\upload\\xxx.png";
     *  List< ImageDraw > drawList = new ArrayList<>();
     *
     *  ImageDraw draw = new ImageDraw();
     *  draw.setText("图讯科技");
     *  draw.setColor(new Color(102,102,102,100));
     *  draw.setFont(new Font("微软雅黑", Font.PLAIN, 24));
     *  draw.setX(259);  // 跟蓝湖比对，减10
     *  draw.setY(438);  // 跟蓝湖比对，加18
     *
     *  drawList.add(draw);
     *
     *  ImageUtils.drawImage(srcPath, targetPath, drawList);
     *
     */
    public static void drawImage(String srcPath, String targetPath, List<ImageDraw> drawList){

        File srcImgFile = new File(srcPath);

        Graphics2D graphics = null;
        BufferedImage srcImgBuff = null;
        BufferedImage targetImgBuff = null;
        try {

            // 源图
            srcImgBuff = ImageIO.read(srcImgFile);
            // 创建图片对象
            targetImgBuff = new BufferedImage(srcImgBuff.getWidth(), srcImgBuff.getHeight(), BufferedImage.TYPE_INT_RGB);
            // 基于图片对象打开绘图
            graphics = targetImgBuff.createGraphics();
            //绘制
            graphics.drawImage(srcImgBuff, null, 0, 0);

            for (ImageDraw item : drawList) {
                // 根据图片的背景设置水印颜色
                graphics.setColor(item.getColor());
                // 设置字体
                graphics.setFont(item.getFont());
                // 画出水印
                graphics.drawString(item.getText(), item.getX(), item.getY());
            }

            graphics.dispose();

            File outFile = new File(targetPath);
            ImageIO.write(targetImgBuff, "jpg", outFile);

            graphics = null;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            targetImgBuff = null;
            srcImgBuff = null;
            
            if(null != graphics){
                graphics.dispose();
            }
        }
    }


    /**
     * 图片添加文字
     * @author Ypp
     * @date 2022/3/18 20:26
     * @param srcPath 源图片路径，如： d:/xxxx.jpg
     * @param drawList 需要添加的文字及样式列表，一个坐标一个ImageDraw
     * @return void
     *
     *
     *  String srcPath = "D:\\upload\\zhens.png";
     *  String targetPath = "D:\\upload\\xxx.png";
     *  List< ImageDraw > drawList = new ArrayList<>();
     *
     *  ImageDraw draw = new ImageDraw();
     *  draw.setText("图讯科技");
     *  draw.setColor(new Color(102,102,102,100));
     *  draw.setFont(new Font("微软雅黑", Font.PLAIN, 24));
     *  draw.setX(259);  // 跟蓝湖比对，减10
     *  draw.setY(438);  // 跟蓝湖比对，加18
     *
     *  drawList.add(draw);
     *
     *  ImageUtils.drawImage(srcPath, targetPath, drawList);
     *
     */
    public static InputStream drawImage(String srcPath, List<ImageDraw> drawList){

        File srcImgFile = new File(srcPath);

        Graphics2D graphics = null;
        BufferedImage srcImgBuff = null;
        BufferedImage targetImgBuff = null;

        InputStream inputStream = null;

        try {

            // 源图
            srcImgBuff = ImageIO.read(srcImgFile);
            // 创建图片对象
            targetImgBuff = new BufferedImage(srcImgBuff.getWidth(), srcImgBuff.getHeight(), BufferedImage.TYPE_INT_RGB);
            // 基于图片对象打开绘图
            graphics = targetImgBuff.createGraphics();
            //绘制
            graphics.drawImage(srcImgBuff, null, 0, 0);

            for (ImageDraw item : drawList) {
                // 根据图片的背景设置水印颜色
                graphics.setColor(item.getColor());
                // 设置字体
                graphics.setFont(item.getFont());
                // 画出水印
                graphics.drawString(item.getText(), item.getX(), item.getY());
            }

            graphics.dispose();
            graphics = null;
            inputStream =  bufferedImageToInputStream(targetImgBuff);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            targetImgBuff = null;
            srcImgBuff = null;

            if(null != graphics){
                graphics.dispose();
            }
        }

        return inputStream;
    }

    public static InputStream bufferedImageToInputStream(BufferedImage image) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        return new ByteArrayInputStream(os.toByteArray());
    }
}
