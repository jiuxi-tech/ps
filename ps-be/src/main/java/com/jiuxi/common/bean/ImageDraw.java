package com.jiuxi.common.bean;

import java.awt.*;
import java.io.Serializable;

/**
 * @ClassName: Image
 * @Description: 图片实体
 * @Author: Ypp
 * @Date: 2022/3/18 19:22
 * @Copyright: 2022 Hangzhou Jiuxi Inc. All rights reserved.
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
 *
 *
 */
public class ImageDraw implements Serializable {

    /** 添加的文字内容 */
    private String text;

    /** 字体颜色和透明度 */
    private Color color;

    /** 字体和大小 */
    private Font font;

    /** 所在图片的x坐标 */
    private int x;

    /** 所在图片的y坐标 */
    private int y;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
