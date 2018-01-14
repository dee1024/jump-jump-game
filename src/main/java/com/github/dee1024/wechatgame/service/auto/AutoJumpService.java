package com.github.dee1024.wechatgame.service.auto;

import com.github.dee1024.wechatgame.service.auto.support.Coordinates4Image;
import com.github.dee1024.wechatgame.service.auto.support.GameUIAnalyzer;
import com.github.dee1024.wechatgame.tools.AdbToolKit;
import com.github.dee1024.wechatgame.tools.ImageToolkit;
import com.github.dee1024.wechatgame.tools.LogToolKit;
import com.github.dee1024.wechatgame.tools.SettingToolkit;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * @author Dee1024 <coolcooldee@gmail.com>
 * @version 1.0
 * @description
 * @date 2018/1/3
 * @since 1.0
 */

public abstract class AutoJumpService {

    public static boolean auto(BufferedImage bi) throws IOException {
        //step 1
        Coordinates4Image chessBottomCenterCoordinate = GameUIAnalyzer.searchChessBottomCenterPoint(bi);
        if(chessBottomCenterCoordinate!=null){
            BufferedImage nBi =  GameUIAnalyzer.markeRedCircle(bi,chessBottomCenterCoordinate.getX(), chessBottomCenterCoordinate.getY());
            ImageIO.write(nBi, "PNG", new File("z_origin.png"));
        }else{
            LogToolKit.println("未找到棋子底部中心点，结束.");
            JOptionPane.showMessageDialog(null, "游戏已结束。请再次手机上打开游戏并点击确认按钮", "提示",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //step 2
        Coordinates4Image targetAreaTopCoordinate = GameUIAnalyzer.searchTargetAreaTopCoordinate(bi);
        BufferedImage resultBi = GameUIAnalyzer.markeRedCircle(bi, targetAreaTopCoordinate.getX(), targetAreaTopCoordinate.getY());
        Coordinates4Image targetCenterCoordinate = GameUIAnalyzer.searchTargetCenterCoordinate2(bi,targetAreaTopCoordinate);
        resultBi = GameUIAnalyzer.markeRedCircle(resultBi, targetCenterCoordinate.getX(), targetCenterCoordinate.getY());
        File rfile = new File("z_target.png");
        ImageIO.write(resultBi, "PNG", rfile);

        int d = getDistance(chessBottomCenterCoordinate, targetCenterCoordinate);
        d = (int)(d*SettingToolkit.getUiRate());
        if(d!=0) {
            LogToolKit.println("识别跳跃距离 " + d);
//            if (d < 20) {
//                LogToolKit.println("距离太小，重新跳跃 " + d);
//                return false;
//            }
            AdbToolKit.screentouch(Math.floor(d * (SettingToolkit.getJumpRate() / SettingToolkit.getUiRate()) ));
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return true;
        }
        return false;
    }


    private static int getDistance(Coordinates4Image a, Coordinates4Image b) {
        double _x = Math.abs(a.getX() - b.getX());
        double _y = Math.abs(a.getY() - b.getY());
        return (int)Math.sqrt(_x*_x+_y*_y);
    }


    public static void main(String[] args) throws Exception {
//        File file = new File("/Users/dee/Documents/dev/github/wechat-jump-game/target/resolution/720*1280.png");
//        File file = new File("/Users/dee/Documents/dev/github/wechat-jump-game/target/resolution/1080*1920.png");
//        File file = new File("/Users/dee/Documents/dev/github/wechat-jump-game/target/resolution/1080*2160.png");
//        File file = new File("/Users/dee/Documents/dev/github/wechat-jump-game/target/resolution/1080*2220.png");
//        File file = new File("/Users/dee/Documents/dev/github/wechat-jump-game/target/resolution/1440*2560.png");
//        File file = new File("/Users/dee/Documents/dev/github/wechat-jump-game/target/resolution/1440*2960.png");
//        File file = new File("jumpgame.png");
        File file = new File("resolution/no/fixb.png");

        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BufferedImage zBi = ImageToolkit.cleanImage(bi);
        ImageIO.write(zBi, "PNG", new File("z_clean.png"));

        Coordinates4Image chessBottomCenterPoint = GameUIAnalyzer.searchChessBottomCenterPoint(bi);
        if(chessBottomCenterPoint!=null){
            BufferedImage nBi =  GameUIAnalyzer.markeRedCircle(bi,chessBottomCenterPoint.getX(), chessBottomCenterPoint.getY());
            ImageIO.write(nBi, "PNG", new File("z_origin.png"));
        }else{
            LogToolKit.println("未找到棋子底部中心点");
        }


//        Color bcolor = new Color(246,246,246);//背景1
//        Color ecolor = new Color(213,239,237);//井盖
//        System.out.println(GameUIAnalyzer.calculateColorSimilarValue(bcolor, ecolor));

        Coordinates4Image targetAreaTopCoordinate = GameUIAnalyzer.searchTargetAreaTopCoordinate(bi);
        BufferedImage resultBi = GameUIAnalyzer.markeRedCircle(bi, targetAreaTopCoordinate.getX(), targetAreaTopCoordinate.getY());

        //Coordinates4Image targetAreaCenterCoordinate = GameUIAnalyzer.searchTargetAreaCenterCoordinate(bi);
        //BufferedImage resultBi = GameUIAnalyzer.markeRedPoint(newbi, targetAreaCenterCoordinate.getX(), targetAreaCenterCoordinate.getY());

        Coordinates4Image targetCenterCoordinate = GameUIAnalyzer.searchTargetCenterCoordinate(bi,targetAreaTopCoordinate);
        resultBi = GameUIAnalyzer.markeRedCircle(resultBi, targetCenterCoordinate.getX(), targetCenterCoordinate.getY());



        File rfile = new File("z_target.png");
        ImageIO.write(resultBi, "PNG", rfile);





    }



}
