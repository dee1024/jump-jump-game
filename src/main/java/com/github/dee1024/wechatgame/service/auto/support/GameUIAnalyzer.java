package com.github.dee1024.wechatgame.service.auto.support;

import com.github.dee1024.wechatgame.tools.ImageToolkit;
import com.github.dee1024.wechatgame.tools.LogToolKit;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 微信跳一跳屏幕信息解析器
 * @author Dee1024 <coolcooldee@gmail.com>
 * @version 1.0
 * @description
 * @date 2018/1/3
 * @since 1.0
 */

public abstract class GameUIAnalyzer {

    //寻找棋子底部中心点
    public static Coordinates4Image searchChessBottomCenterPoint(BufferedImage bi) throws IOException {
        Coordinates4Image topLineCenterCoordinate =  searchChessTopCenterCoordinate(bi);

        if(topLineCenterCoordinate==null){
            LogToolKit.println("未识别出棋子顶部中心点。");
            return null;
        }
        int centerX = topLineCenterCoordinate.getX();
        int beginY= topLineCenterCoordinate.getY();
        int maxY = bi.getHeight();
        int endY = 0;
        int changeCount = 0;
        boolean nowChessBodyColor = true;
        boolean found = false;
        for(int j=beginY; j<maxY; j++){
//            if(isChessBodyColorRGB(getRGB(bi, centerX, j))){
//                if(!nowChessBodyColor){
//                    changeCount++;
//                }
//                nowChessBodyColor = true;
//            }else{
//                if(nowChessBodyColor){
//                    changeCount++;
//                }
//                nowChessBodyColor = false;
//            }
//            if(changeCount==3){
//                endY = j-1;
//                break;
//            }
            if(calculateColorSimilarValue(getRGB(bi,centerX,j), getRGB(bi,centerX,j+1))<0.93){
                changeCount++;
                if(changeCount==3) {
                    endY = j;
                    found = true;
                    break;
                }
            }
        }
        Coordinates4Image c = null;
        if(found){
            c =new Coordinates4Image(centerX, endY);
            LogToolKit.println("精准模式，识别出棋子底部中心点是："+c.toString());
        }else{
            c = new Coordinates4Image(topLineCenterCoordinate.getX(), topLineCenterCoordinate.getY()+100);
            LogToolKit.println("模糊识别，识别出棋子底部中心点是："+c.toString());
        }
        return c;
    }

    //寻找棋子顶部中心点
    private static Coordinates4Image searchChessTopCenterCoordinate(BufferedImage bi){
        int width = bi.getWidth();
        int height = bi.getHeight();
        int begingheight = (int)(height*0.21);  //可以跳过分数区域
        boolean found = false;

        int count = 0;
        List<Coordinates4Image> topLinePoints = new ArrayList<Coordinates4Image>();
        for (int j = begingheight; j < height; j++) {
            if(found){
                break;
            }
            count = 0;
            topLinePoints.clear();
            for (int i = 0; i < width; i++) {
                if(found){
                    break;
                }
                if(isChessTopLineColorRGB(getRGB(bi, i, j))){
                    topLinePoints.add(new Coordinates4Image(i,j));
                    count++;
                }else{
                    if(count>5 && count<22){ //避免井盖区域干扰
                        Coordinates4Image centerCoordinates = topLinePoints.get(topLinePoints.size()/2);
                        if(isSureChessTopCenterCoordinate(bi, centerCoordinates.getX(), centerCoordinates.getY())){
                            found = true;
                        }
                    }else {
                        topLinePoints.clear();
                        count = 0;
                    }
                }
            }
        }
        if(found && !topLinePoints.isEmpty()){
            int i = topLinePoints.size()/2;
            Coordinates4Image centerCoordinates = topLinePoints.get(i);
            LogToolKit.println("精准模式，棋子顶部中心点是："+centerCoordinates.toString());
            return centerCoordinates;
        }
        return null;
    }

    //纯色块查找中心点
    @Deprecated
    public static Coordinates4Image searchTargetAreaCenterCoordinate(BufferedImage bi){
        Coordinates4Image targetTopCoordinates =  searchTargetAreaTopCoordinate(bi); //目标顶部

        int searchHeight = (int)(bi.getHeight()*0.8);
        int bx = targetTopCoordinates.getX();
        int by = targetTopCoordinates.getY();
        for(int j=by; j<searchHeight; j++){
            if(calculateColorSimilarValue(getRGB(bi, bx, j+1), getRGB(bi, bx,j))<0.98){
                if(isCircleWhiteCenterColor(getRGB(bi, bx, j+1))){
                    continue;
                }
                Coordinates4Image temp = new Coordinates4Image(bx, j);
                LogToolKit.println("精准模式，识别到方块顶部面的下尖角点 : "+temp.toString());
                return temp;
            }
        }

        LogToolKit.println("目标区域中心点未找到.");
        return null;
    }

    public static Coordinates4Image searchTargetCenterCoordinate2(BufferedImage bi, Coordinates4Image targetAreaTopCoordinate) {
        return new Coordinates4Image(targetAreaTopCoordinate.getX(), targetAreaTopCoordinate.getY()+ new Random().nextInt(10)+30);
    }


    //识别目标中心点
    public static Coordinates4Image searchTargetCenterCoordinate(BufferedImage bi, Coordinates4Image targetAreaTopCoordinate){
        int bx = targetAreaTopCoordinate.getX();
        int by = targetAreaTopCoordinate.getY();

        //定制化
        if(isSureWuyuBoxColor(getRGB(bi, targetAreaTopCoordinate))){//识别为 "WUYUBOX"
            LogToolKit.println("模糊模式，识别为WUYU BOX");
            return new Coordinates4Image(targetAreaTopCoordinate.getX(), targetAreaTopCoordinate.getY()+ new Random().nextInt(10)+60);
        }
        //定制化
        if(isSureMagicBoxColor(getRGB(bi,targetAreaTopCoordinate.getX(), targetAreaTopCoordinate.getY()+new Random().nextInt(10)+5))){
            LogToolKit.println("模糊模式，识别为魔方");
            return new Coordinates4Image(targetAreaTopCoordinate.getX(), targetAreaTopCoordinate.getY()+new Random().nextInt(10)+50);
        }

        //定制化
        if(isSureExpressBoxColor(getRGB(bi,targetAreaTopCoordinate))){
            LogToolKit.println("模糊模式，识别为快递盒");
            return new Coordinates4Image(targetAreaTopCoordinate.getX(), targetAreaTopCoordinate.getY()+new Random().nextInt(10)+50);
        }

        //定制化
        if(isSureinkGiftBoxColor(getRGB(bi,targetAreaTopCoordinate))){
            LogToolKit.println("模糊模式，识别为粉色礼盒");
            return new Coordinates4Image(targetAreaTopCoordinate.getX(), targetAreaTopCoordinate.getY()+new Random().nextInt(10)+50);
        }

        //定制化
        if(isSureHoldWhiteSquareBoxColor(getRGB(bi,targetAreaTopCoordinate))){
            LogToolKit.println("模糊模式，识别为整体纯白带中心点的方形盒子");
            return new Coordinates4Image(targetAreaTopCoordinate.getX(), targetAreaTopCoordinate.getY()+new Random().nextInt(10)+40);
        }

        if(isSureinkHoldWhiteCircularTapColor(getRGB(bi,targetAreaTopCoordinate))){
            LogToolKit.println("模糊模式，识别为整体纯白的胶带");
            return new Coordinates4Image(targetAreaTopCoordinate.getX(), targetAreaTopCoordinate.getY()+new Random().nextInt(10)+20);
        }


        //定制化
        if(isSureHoldWGreenCircularBoxColor(getRGB(bi,targetAreaTopCoordinate))){
            LogToolKit.println("模糊模式，识别为整体纯绿色圆形柱子");
            return new Coordinates4Image(targetAreaTopCoordinate.getX(), targetAreaTopCoordinate.getY()+new Random().nextInt(10)+40);
        }

        //定制化
        if(isSureWoodTableColor(getRGB(bi,targetAreaTopCoordinate))){
            Coordinates4Image temp = new Coordinates4Image(targetAreaTopCoordinate.getX(), targetAreaTopCoordinate.getY()+new Random().nextInt(10)+40);
            LogToolKit.println("模糊模式，识别为木圆桌子中点是："+temp.toString());
            return temp;
        }

        //
        if(isSureHoldWhiteCircularBoxColor(getRGB(bi,targetAreaTopCoordinate))){

        }

        BufferedImage cleanBi = ImageToolkit.cleanImage(bi); //剩下黑白色

        boolean found = false;
        int rx = 0, ry = 0;
        for (int j = 0; j < cleanBi.getHeight()-by-1; j++) {
            rx = bx;
            ry = by+j;
            if(calculateColorSimilarValue(getRGB(cleanBi, bx, by+j), getRGB(cleanBi, bx, by+j+1))<0.50){
                found = true;
                break;
            }
        }
        if(found){
            Coordinates4Image temp = new Coordinates4Image((rx+bx)/2, (ry+by)/2);
            LogToolKit.println("精准模式，目标中间点是："+temp.toString());
            return temp;
        }else{
            Coordinates4Image temp = new Coordinates4Image(targetAreaTopCoordinate.getX(), targetAreaTopCoordinate.getY()+50);
            LogToolKit.println("模糊识别，目标中间点是："+temp.toString());
            return temp;

        }
    }


    //寻找目标点的顶部中心点(纯白色块、纯灰色块、纯绿色块、纯灰色圆盘桌子、纯白色圆桌子偏差、纯橙色块)
    public static Coordinates4Image searchTargetAreaTopCoordinate(BufferedImage bi){
        int width = bi.getWidth();
        int height = bi.getHeight();
        //避开分数横向区域
        int begingheight = (int)(height*0.21);
        //避开棋子纵向区域
        Coordinates4Image chessTopCenterCoordinate = searchChessTopCenterCoordinate(bi);
        int tempx = chessTopCenterCoordinate.getX();
        int beingEscapeX = tempx-50;
        int endEscapeX = tempx+50;

        boolean found = false;
        Coordinates4Image beingCoordinates4Image = null;
        for(int j=begingheight; j<height; j++){
            if(found){
                break;
            }
            for (int i = 1; i < width; i++) {
                if(found){
                    break;
                }
                if(i>=beingEscapeX && i<=endEscapeX){
                    //LogToolKit.println("识别出逃离区");
                    continue;
                }
                if(calculateColorSimilarValue(getRGB(bi,i,j), getRGB(bi,(i-1),j))<0.98){
                    found = true;
                    beingCoordinates4Image = new Coordinates4Image(i,j);
                    LogToolKit.println("精准识别，目标点顶部起始点是："+beingCoordinates4Image.toString());
                }
            }
        }

        if(beingCoordinates4Image!=null){  //解决曲面弧形的误差
            //横向往后找
            int bx = beingCoordinates4Image.getX();
            int by = beingCoordinates4Image.getY();
            boolean found2 = false;
            int length = 0;
            for(int i=1; i<=50; i++){
                length ++;
                if(calculateColorSimilarValue(getRGB(bi,bx,by), getRGB(bi, bx+i, by))<0.98){
                    found2 = true;
                    break;
                }
            }
            int centerx = 0;
            int centery = 0;
            if(found2){
                centerx = bx + length/2;
                centery = by;
            }else{
                centerx = bx;
                centery = by;
            }
            Coordinates4Image coordinates4Image = new Coordinates4Image(centerx, centery);
            LogToolKit.println("精准识别，目标点顶部中间点是："+coordinates4Image.toString());
            return coordinates4Image;
        }else{
            LogToolKit.println("目标点顶部起始点未找到.");
            return null;
        }
    }



    /**
     * 确认是否棋子的顶部中心点，该二次确认以避开井盖区域干扰
     * @return
     */
    private static boolean isSureChessTopCenterCoordinate(BufferedImage bi, int x, int y){
        for(int i=0; i<30; i++){
            if(!isChessBodyColorRGB(getRGB(bi,x,y+i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isMusicBoxTopCoordinate(BufferedImage bi, Coordinates4Image coordinates4Image){
        int x = coordinates4Image.getX();
        int y = coordinates4Image.getY();
        int[] rgb = getRGB(bi, x, y);
        if(!((rgb[0]>=218&&rgb[0]<=228)&&(rgb[1]>=218&&rgb[1]<=228)&&(rgb[2]>=228&&rgb[2]<=230))){
            return false;
        }
        //下一层颜色
        for(int j=1; j<100; j++){
            int[] trgb = getRGB(bi, x, y+j);
            if((trgb[0]>=160&&trgb[0]<=180)&&(trgb[1]>=160&&trgb[1]<=190)&&(trgb[2]>=150&&trgb[2]<=160)){
                LogToolKit.println("精准识别，识别为音乐盒顶部起点："+coordinates4Image.toString());
                return true;
            }
        }
        return false;
    }

    private static int[] getRGB(BufferedImage bi, int x, int y){
        int[] rgb = new int[3];
        int pixel = bi.getRGB(x, y);
        rgb[0] = (pixel & 0xff0000) >> 16;
        rgb[1] = (pixel & 0xff00) >> 8;
        rgb[2] = (pixel & 0xff);
        return rgb;
    }

    private static int[] getRGB(BufferedImage bi, Coordinates4Image coordinates4Image){
        return getRGB(bi, coordinates4Image.getX(), coordinates4Image.getY());
    }

    private static int getRBInt(BufferedImage bi, int x, int y){
        int[] trgb = getRGB(bi,x,y);
        return getRBInt(trgb[0],trgb[1],trgb[2]);
    }

    public static int getRBInt(int red, int green, int blue){
        int[] trgb = {red,green,blue};
        return getRBInt(trgb);
    }

    public static int getRBInt(int[] trgb){
        int rgb = (trgb[0]*256 + trgb[1])*256+trgb[2];
        if(rgb>8388608) {
            rgb = rgb - 16777216;
        }
        return rgb;
    }

    //两个点的相似度 ，如 0.9 表示相似度为 90%
    public static Double calculateColorSimilarValue(Color bColor, Color eColor){
        int r1 = bColor.getRed();
        int g1 = bColor.getGreen();
        int b1 = bColor.getBlue();
        int r2 = eColor.getRed();
        int g2 = eColor.getGreen();
        int b2 = bColor.getBlue();
        double result = (255 - Math.abs(r1 - r2) * 0.297 - Math.abs(g1 - g2) * 0.593 - Math.abs(b1 - b2) * 0.11) / 255;
        LogToolKit.println("相似度为："+ String.format("%.2f",result));
        return result;
    }

    public static Double calculateColorSimilarValue(int[] bRGB, int[] eRGB){
        int r1 = bRGB[0];
        int g1 = bRGB[1];
        int b1 = bRGB[2];
        int r2 = eRGB[0];
        int g2 = eRGB[1];
        int b2 = eRGB[2];
        double result = (255 - Math.abs(r1 - r2) * 0.297 - Math.abs(g1 - g2) * 0.593 - Math.abs(b1 - b2) * 0.11) / 255;
        //LogToolKit.println("相似度为："+result);
        return result;
    }

    public static BufferedImage markeRedPoint(BufferedImage bi, int x, int y){
        BufferedImage newBi = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
        newBi.setData(bi.getData());
        newBi.setRGB(x,y,getRBInt(255, 0, 0));
        return newBi;
    }

    public static BufferedImage markeRedCircle(BufferedImage bi, int x, int y){
        BufferedImage newBi = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
        newBi.setData(bi.getData());
        for(int i=-10; i<10; i++){
            for(int j=-10; j<10; j++){
                int sx = x+i;
                int sy = y+j;
                Double r = getDistance(new Point(sx, sy), new Point(x,y));
                if(r<10){
                    newBi.setRGB(sx,sy,getRBInt(255, 0, 0));
                }
            }
        }
        return newBi;
    }

    private static Double getDistance(Point a, Point b) {
        double _x = Math.abs(a.x - b.x);
        double _y = Math.abs(a.y - b.y);
        return Math.sqrt(_x*_x+_y*_y);
    }

    //棋子顶部
    private static boolean isChessTopLineColorRGB(int[] rgb){
        if(rgb.length==3){
            if((rgb[0]>=50&&rgb[0]<=55)&&(rgb[1]>=50&&rgb[1]<=55)&&(rgb[2]>=55&&rgb[2]<=65)){
                return true;
            }
            if((rgb[0]>=60&&rgb[0]<=65)&&(rgb[1]>=55&&rgb[1]<=65)&&(rgb[2]>=80&&rgb[2]<=85)){
                return true;
            }
        }
        return false;
    }

    //棋子整体
    private static boolean isChessBodyColorRGB(int[] rgb){
        if(rgb.length==3){
            if((rgb[0]>=45&&rgb[0]<=90)&&(rgb[1]>=45&&rgb[1]<=90)&&(rgb[2]>=40&&rgb[2]<=130)){
                return true;
            }
        }
        return false;
    }

    //if (50 < red && red < 60&& 55 < green && green < 65&& 95 < blue && blue < 105)
    private static boolean isChessBottomLineColorRGB(int[] rgb){
        if(rgb.length==3){
            if((rgb[0]>=51&&rgb[0]<=58)&&(rgb[1]>=57&&rgb[1]<=60)&&(rgb[2]>=96&&rgb[2]<=102)){
                return true;
            }
        }
        return false;
    }

    //识别是否为圆盘中心白点
    private static boolean isCircleWhiteCenterColor(int[] rgb){
        if(rgb.length==3){
            if((rgb[0]>=240&&rgb[0]<=255)&&(rgb[1]>=240&&rgb[1]<=255)&&(rgb[2]>=240&&rgb[2]<=255)){
                return true;
            }
        }
        return false;
    }

    //识别为WUYU盒子
    private static boolean isSureWuyuBoxColor(int[] rgb){
        if(rgb.length==3){
            if((rgb[0]==147)&&(rgb[1]==147)&&(rgb[2]==147)){
                return true;
            }
        }
        return false;
    }

    //识别为彩色魔方
    private static boolean isSureMagicBoxColor(int[] rgb){
        if(rgb.length==3){
            if((rgb[0]==107)&&(rgb[1]==156)&&(rgb[2]==248)){
                return true;
            }
        }
        return false;
    }

    //识别为彩色魔方
    private static boolean isSureExpressBoxColor(int[] rgb){
        int[] orgb = {227,201,142};
        if(rgb.length==3){
            if(calculateColorSimilarValue(rgb, orgb)>0.98){
                return true;
            }
        }
        return false;
    }

    //识别为粉色礼盒
    private static boolean isSureinkGiftBoxColor(int[] rgb){
        int[] orgb = {255,172,178};
        if(rgb.length==3){
            if(calculateColorSimilarValue(rgb, orgb)>0.98){
                return true;
            }
        }
        return false;
    }

    //识别为整体纯白、纯绿、纯黄带中心点的方形盒子
    private static boolean isSureHoldWhiteSquareBoxColor(int[] rgb){
        if((rgb[0]==250)&&(rgb[1]==250)&&(rgb[2]==250)){
            return true;
        }
        int[] orgb = {186,240,68};
        if(calculateColorSimilarValue(rgb, orgb)>0.98){
            return true;
        }
        int[] orgb2 = {255,238,97};
        if(calculateColorSimilarValue(rgb, orgb2)>0.98){
            return true;
        }
        return false;
    }

    //识别为整体纯白物体
    private static boolean isSureHoldWhiteCircularBoxColor(int[] rgb){
        if((rgb[0]==246)&&(rgb[1]==246)&&(rgb[2]==246)){
            return true;
        }
        return false;
    }

    //识别为整体白色的胶带
    private static boolean isSureinkHoldWhiteCircularTapColor(int[] rgb){
        if((rgb[0]==255)&&(rgb[1]==255)&&(rgb[2]==255)){
            return true;
        }
        return false;
    }

    //识别为整体纯绿色圆形柱子
    private static boolean isSureHoldWGreenCircularBoxColor(int[] rgb){
        int[] orgb = {76,163,87};
        if(calculateColorSimilarValue(rgb, orgb)>0.95){
            return true;
        }
        return false;
    }

    //识别为木圆桌子
    private static boolean isSureWoodTableColor(int[] rgb){
        int[] orgb = {220,190,158};
        if(calculateColorSimilarValue(rgb, orgb)>0.95){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        int[] a = {64,66,90};
        int[] b = {52,53,59};
        System.out.println(calculateColorSimilarValue(a,b));
     }

}
