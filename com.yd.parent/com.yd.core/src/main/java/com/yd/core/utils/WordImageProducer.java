package com.yd.core.utils;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

/**
 * @Filename: WordImageProducer.java
 * @Description: 数字校验码生成器
 * @Version: 1.0
 * @Author: peigen
 * @Email: yinsha@mbaobao.com
 * @History:<br> <li>Author: yinsha</li>
 * <li>Date: 2011-8-2</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 */
public class WordImageProducer {

    // ----- 由容器配置 -----
    private int imageWidth = 60;

    private int imageHeight = 25;

    private String bgImageName = "ground.jpg";

    /**
     * 背景图片cache
     */
    private BufferedImage bgImage;

    /**
     * 背景图片宽度
     */
    private int bgImageWidth;

    /**
     * 背景图片高度
     */
    private int bgImageHeight;
    /**
     * 选转标志，验证码旋转
     */
    private boolean ROTATE_FLAG = true;
    /**
     * 用于生成随机数
     */
    private Random rand = new Random();

    /**
     * 初始化
     */
    public void init() throws IOException {
        if (bgImage == null) {
            URL url = this.getClass().getClassLoader().getResource(bgImageName);
            //            FileSystemResource imagePath = new FileSystemResource(new File("/tmp/ground.jpg"));
            //            String imagePath = PathTool.getFullPathRelateClass("", this.getClass()) + "/ground.jpg";
            bgImage = ImageIO.read(url);

        }

        bgImageWidth = bgImage.getWidth(null);
        bgImageHeight = bgImage.getHeight(null);

        // 预加载图形系统
        produce("1234");
    }

    /**
     * 将传入的文字生成图片文件。
     *
     * @param word
     * @return
     */
    public BufferedImage produce(String word) {

        // 从背景图片中随机切出一小块

        int startX = RandomUtils.nextInt(bgImageWidth - imageWidth);
        int startY = RandomUtils.nextInt(bgImageHeight - imageHeight);
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, bgImage.getType());

        bgImage.getSubimage(startX, startY, imageWidth, imageHeight).copyData(image.getRaster());

        // 在切出的背景图片上写文字
        if (StringUtils.isEmpty(word)) {
            return image;
        }

        Graphics2D g2d = image.createGraphics();
        Font font = new Font("Batmos Regular", Font.BOLD, 20);

        font.deriveFont(20);
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);

        FontMetrics fontMetrics = g2d.getFontMetrics();
        int wordWidth = fontMetrics.stringWidth(word);

        int posX = (imageWidth - wordWidth) / 2;
        int posY = 18;
        for (int i = 0; i < 3; i++) {//画三条随机的斜线
            g2d.drawLine(rand.nextInt(imageWidth), rand.nextInt(imageHeight),
                    rand.nextInt(imageWidth), rand.nextInt(imageHeight));
        }
        g2d = this.rotateWord(posX, posY, fontMetrics, word, g2d);//画旋转的字体

        g2d.dispose();
        return image;
    }

    /**
     * 画旋转的字体
     *
     * @param posX        X坐标
     * @param posY        Y坐标
     * @param fontMetrics 字体规格化
     * @param word        要旋转的字体
     * @param g2          图像graphics
     * @return 生成好的graphics
     */
    public Graphics2D rotateWord(int posX, int posY, FontMetrics fontMetrics, String word,
                                 Graphics2D g2) {
        for (int i = 0; i < word.length(); i++) {

            //如果设置选装标志，则旋转文字
            if (ROTATE_FLAG) {
                AffineTransform at = new AffineTransform();
                at.rotate(10 * Math.PI / 180, 30, 30);
                g2.setTransform(at);
            }

            float stroke = Math.abs(rand.nextFloat() % 30);
            g2.setStroke(new BasicStroke(stroke));
            String temp = word.substring(i, i + 1);
            g2.drawString(temp, posX, ((i % 2) == 0) ? (posY + 2) : (posY - 2));
            posX += fontMetrics.stringWidth(temp);
        }
        return g2;
    }

    //--------------------------------- setters --------------------------------//

    /**
     * @param imageHeight
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    /**
     * @param imageWidth
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }
}
