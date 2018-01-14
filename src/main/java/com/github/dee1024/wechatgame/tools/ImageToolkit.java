package com.github.coolcooldee.wechatgame.tools;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;

/**
 * @author Dee1024 <coolcooldee@gmail.com>
 * @version 1.0
 * @description
 * @date 2018/1/3
 * @since 1.0
 */

public class ImageToolkit {

    /**
     *
     * @param oBi 需要去噪的图像
     * @return
     * @throws IOException
     */
    public static BufferedImage cleanImage(BufferedImage oBi) {

        int h = oBi.getHeight();
        int w = oBi.getWidth();
        // 灰度化
        int[][] gray = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int argb = oBi.getRGB(x, y);
                // 图像加亮（调整亮度识别率非常高）
                int r = (int) (((argb >> 16) & 0xFF) * 1.1 + 30);
                int g = (int) (((argb >> 8) & 0xFF) * 1.1 + 30);
                int b = (int) (((argb >> 0) & 0xFF) * 1.1 + 30);
                if (r >= 255) {
                    r = 255;
                }
                if (g >= 255) {
                    g = 255;
                }
                if (b >= 255) {
                    b = 255;
                }
                gray[x][y] = (int) Math.pow((Math.pow(r, 2.2) * 0.2973 + Math.pow(g, 2.2) * 0.6274 + Math.pow(b, 2.2) * 0.0753), 1 / 2.2);
            }
        }

        // 二值化
        int threshold = ostu(gray, w, h);
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (gray[x][y] > threshold) {
                    gray[x][y] |= 0x00FFFF;
                } else {
                    gray[x][y] &= 0xFF0000;
                }
                bi.setRGB(x, y, gray[x][y]);
            }
        }

        // 矩阵打印
//        for (int y = 0; y < h; y++){
//            for (int x = 0; x < w; x++){
//                if (isBlack(binaryBufferedImage.getRGB(x, y))){
//                    System.out.print("*");
//                } else{
//                    System.out.print(" ");
//                }
//            }
//            System.out.println();
//        }
        return bi;
    }

    public static boolean isBlack(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() <= 300) {
            return true;
        }
        return false;
    }

    public static boolean isWhite(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() > 300) {
            return true;
        }
        return false;
    }

    public static int isBlackOrWhite(int colorInt) {
        if (getColorBright(colorInt) < 30 || getColorBright(colorInt) > 730) {
            return 1;
        }
        return 0;
    }

    public static int getColorBright(int colorInt) {
        Color color = new Color(colorInt);
        return color.getRed() + color.getGreen() + color.getBlue();
    }

    public static int ostu(int[][] gray, int w, int h) {
        int[] histData = new int[w * h];
        // Calculate histogram
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int red = 0xFF & gray[x][y];
                histData[red]++;
            }
        }

        // Total number of pixels
        int total = w * h;

        float sum = 0;
        for (int t = 0; t < 256; t++) {
            sum += t * histData[t];
        }

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t = 0; t < 256; t++) {
            wB += histData[t]; // Weight Background
            if (wB == 0) {
                continue;
            }

            wF = total - wB; // Weight Foreground
            if (wF == 0) {
                break;
            }

            sumB += (float) (t * histData[t]);

            float mB = sumB / wB; // Mean Background
            float mF = (sum - sumB) / wF; // Mean Foreground

            // Calculate Between Class Variance
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            // Check if new maximum found
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }

        return threshold;
    }

    //图片灰度，黑白
    public static BufferedImage gray(BufferedImage oBi) {
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp op = new ColorConvertOp(cs, null);
        return op.filter(oBi, null);
    }


    public static final BufferedImage dlur(BufferedImage oBi) {
        int imageWidth = oBi.getWidth();
        int imageHeight = oBi.getHeight();

        BufferedImage nBi = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_3BYTE_BGR);

        float[] data = { 0.0625f, 0.125f, 0.0625f, 0.125f, 0.125f, 0.125f,
                0.0625f, 0.125f, 0.0625f };

        Kernel kernel = new Kernel(3, 3, data);
        ConvolveOp co = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        co.filter(oBi, nBi);
        return nBi;
    }


    public static final BufferedImage sharp(BufferedImage originalPic){
        int imageWidth = originalPic.getWidth();
        int imageHeight = originalPic.getHeight();

        BufferedImage newPic = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_3BYTE_BGR);
        float[] data =
                { -1.0f, -1.0f, -1.0f, -1.0f, 10.0f, -1.0f, -1.0f, -1.0f, -1.0f };

        Kernel kernel = new Kernel(3, 3, data);
        ConvolveOp co = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        co.filter(originalPic, newPic);
        return newPic;
    }


}
