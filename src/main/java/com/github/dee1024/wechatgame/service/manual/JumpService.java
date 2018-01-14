package com.github.coolcooldee.wechatgame.service.manual;
import com.github.coolcooldee.wechatgame.tools.AdbToolKit;
import com.github.coolcooldee.wechatgame.tools.LogToolKit;
import com.github.coolcooldee.wechatgame.tools.SettingToolkit;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 跳跃的核心逻辑
 *
 * @Description
 * @author Dee1024 <coolcooldee@gmail.com>
 * @Version 1.0
 * @Since 1.0
 * @Date 2018/1/3
 */

public abstract class JumpService {

    //起跳点
    private static Point beginPoint = new Point(0,0);
    //目标点
    private static Point endPoint = new Point(0,0);

    /**
     * 进行跳跃，同时等待一会，等到其停止，方便下一步截屏
     * @param beginPoint
     * @param endPoint
     */
    public static boolean jump(Point beginPoint, Point endPoint){
        int d = getDistance(beginPoint, endPoint);
        if(d!=0) {
            LogToolKit.println("跳跃距离 " + d);
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


    private static int getDistance(Point a, Point b) {
        double _x = Math.abs(a.x - b.x);
        double _y = Math.abs(a.y - b.y);
        return (int)Math.sqrt(_x*_x+_y*_y);
    }

    public static void setBeginPoint(Point beginPoint) {
        if(beginPoint!=null){
            JumpService.beginPoint.setLocation(beginPoint.getX(), beginPoint.getY());
            if(beginPoint.getX()>0 && beginPoint.getY()>=0) {
                LogToolKit.println("起跳点 (" + beginPoint.getX() + ", " + beginPoint.getY() + ")");
            }
        }else{
            JumpService.beginPoint.setLocation(0,0);
        }
    }

    public static void setEndPoint(Point endPoint) {
        if(endPoint!=null){
            JumpService.endPoint.setLocation(endPoint.getX(), endPoint.getY());
            if(endPoint.getX()>0 && endPoint.getY()>=0) {
                LogToolKit.println("目标点 (" + endPoint.getX() + ", " + endPoint.getY() + ")");
            }
        }else{
            JumpService.endPoint.setLocation(0,0);
        }

    }

    public static Point getBeginPoint() {
        return beginPoint;
    }

    public static Point getEndPoint() {
        return endPoint;
    }

}
