package com.github.dee1024.wechatgame.tools;

/**
 * @Description
 * @Author Dee1024 <coolcooldee@gmail.com>
 * @Version 1.0
 * @Since 1.0
 * @Date 2018/1/3
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 封装ADB功能，通过执行ADB命令行实现
 * 需要先下载 Android Debug Bridge 到本地，参考地址：https://developer.android.com/studio/command-line/adb.html#forwardports
 *
 */
public abstract class AdbToolKit {

    //请使用本地的ABD工具路径替换，该值需要在启动时，按照引导配置
    static String adbPath = "/Users/root/Downloads/platform-tools/adb";

    final static String SCRIPT_SCREEN_CAP_METHOD1 = "${adbpath} exec-out screencap -p > ${imagename}";
    final static String SCRIPT_SCREEN_CAP_METHOD2_1 = "${adbpath} shell screencap -p /sdcard/${imagename}";
    final static String SCRIPT_SCREEN_CAP_METHOD2_2 = "${adbpath} pull /sdcard/${imagename} > .";
    final static String SCRIPT_SCREEN_TOUCH = "${adbpath} shell input swipe ${x1} ${y1} ${x2} ${y2} ${time}";
    final static String SCRIPT_DEVICES = "${adbpath} devices";
    static boolean isSetting = false;

    public static boolean init(){
        LogToolKit.println("正在启动应用, 请稍等...");
        if(!isADBToolOk()){
            LogToolKit.println("应用启动失败.");
            return false;
        }
        screencap();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        screencap();
        LogToolKit.println("应用启动成功.");
        return true;
    }

    private static String[] genBaseSysParams(){
        String[] args = new String[3];
        String os = System.getProperty("os.name");
        if (os.toLowerCase().trim().startsWith("win")) {
            //LogToolKit.println("系统检测 win.");
            args[0] = "cmd.exe";
            args[1] = "/c";
        }else{
            //LogToolKit.println("系统检测 Linux / Mac os.");
            args[0] = "bash";
            args[1] = "-c";
        }
        return args;
    }

    /**
     * ADB手机屏幕截图
     */
    private static boolean isSettingScreencapMethod = false;
    private static int screencapMethod = 0;
    public static BufferedImage screencap() {
        String[] args = genBaseSysParams();
        try {
            if(!isSettingScreencapMethod){
                LogToolKit.println("尝试使用方式一获取屏幕信息。");
                args[2] = SCRIPT_SCREEN_CAP_METHOD1.replace("${adbpath}", adbPath).replace("${imagename}", SettingToolkit.getScreencapFilePathName());
                Runtime.getRuntime().exec(args).waitFor();
                if(isImageOk()){
                    screencapMethod = 1;
                    isSettingScreencapMethod = true;
                    LogToolKit.println("成功设置方式一。");
                }else{
                    LogToolKit.println("警告：方式一获取屏幕信息失败。");
                    LogToolKit.println("尝试使用方式二获取屏幕信息。");
                    String imageName = "jumpgame.png";
                    String command = SCRIPT_SCREEN_CAP_METHOD2_1.replace("${adbpath}", adbPath).replace("${imagename}", imageName);
                    Runtime.getRuntime().exec(command).waitFor();
                    command = SCRIPT_SCREEN_CAP_METHOD2_2.replace("${adbpath}", adbPath).replace("${imagename}", imageName);
                    Runtime.getRuntime().exec(command).waitFor();
                    if(isImageOk()){
                        screencapMethod = 2;
                        isSettingScreencapMethod = true;
                        LogToolKit.println("成功设置方式二。");
                    }else{
                        LogToolKit.println("警告：方式二获取屏幕信息失败。");
                    }
                }
            }else{
                if(screencapMethod==2){
                    String imageName = "jumpgame.png";
                    String command = SCRIPT_SCREEN_CAP_METHOD2_1.replace("${adbpath}", adbPath).replace("${imagename}", imageName);
                    Runtime.getRuntime().exec(command).waitFor();
                    command = SCRIPT_SCREEN_CAP_METHOD2_2.replace("${adbpath}", adbPath).replace("${imagename}", imageName);
                    Runtime.getRuntime().exec(command).waitFor();
                }else{
                    args[2] = SCRIPT_SCREEN_CAP_METHOD1.replace("${adbpath}", adbPath).replace("${imagename}", SettingToolkit.getScreencapFilePathName());
                    Runtime.getRuntime().exec(args).waitFor();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File rFile = new File(SettingToolkit.getScreencapFilePathName());
        BufferedImage bufferedImage = null;
        if(rFile.exists()) {
            try {
                bufferedImage = ImageIO.read(rFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bufferedImage;
    }

    /**
     * ADB手机屏幕长按，长按时间
     * @param time  长按时间，精确到毫秒
     * Andorid 版本需要高于 4.4
     */
    public static void screentouch(double time) {
        String times = (int)time + "";
        String x1 = ((new Random()).nextInt(100) + (new Random()).nextInt(100) + 1) + "";
        String y1 = ((new Random()).nextInt(100) + (new Random()).nextInt(100) + 1) + "";
        String x2 = ((new Random()).nextInt(100) + (new Random()).nextInt(100) + 1) + "";
        String y2 = ((new Random()).nextInt(100) + (new Random()).nextInt(100) + 1) + "";

        String args = SCRIPT_SCREEN_TOUCH.replace("${adbpath}", adbPath).replace("${time}", times)
                        .replace("${x1}", x1).replace("${y1}", y1).replace("${x2}", x2).replace("${y2}", y2);
        try {
            Runtime.getRuntime().exec(args).waitFor();
            LogToolKit.println("长按"+time+"毫秒");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测ADB工具以及 Android 设备
     *
     * 返回值说明：
     *  1：成功；
     * -1：ADB工具找不到；
     * -2：设备连接异常
     *
     * @param path
     * @return
     */
    public static int checkAdbAndDevice(String path){
        if(path==null || "".equals(path)){
            LogToolKit.println("ADB工具路径未设置.");
            return -1;
        }
        String[] args = genBaseSysParams();
        args[2] =SCRIPT_DEVICES.replace("${adbpath}", path);

        int stauts = 0;

        try {
            Process process = Runtime.getRuntime().exec(args);
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            List<String> lineList = new ArrayList<String>();
            LogToolKit.println("check result:");
            while ((line = reader.readLine()) != null) {
                lineList.add(line);
                LogToolKit.println(line);
            }
            if(!lineList.isEmpty()) {
                if (lineList.get(0).indexOf("List of devices attached") > -1) {
                    if (lineList.size() > 1 && lineList.get(1).length() > 0) {
                        stauts = 1;
                        LogToolKit.println("ADB检测 和 Android设备检测正常.");
                    } else {
                        stauts = -2;
                        LogToolKit.println("设备检测异常，请确认是否连接正常.");
                    }
                } else {
                    stauts = -1;
                    LogToolKit.println("ADB工具检测异常，未找到.");
                }
            }else{
                stauts = -1;
                LogToolKit.println("ADB工具检测异常，未找到.");
            }
            process.waitFor();
            is.close();
            reader.close();
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return stauts;
    }

    private static boolean isImageOk() throws IOException {
        BufferedImage image = ImageIO.read(new File(SettingToolkit.getScreencapFilePathName()));
        if (image == null) {
            return false;
        }else{
            return true;
        }
    }


    public static boolean isADBToolOk(){
        String tempADBPath = SettingToolkit.getADBPath();
        int checkR = AdbToolKit.checkAdbAndDevice(tempADBPath);
        if(checkR==1) {
            setAdbPath(tempADBPath);
            LogToolKit.println("ADB工具地址设置成功：" + adbPath);
            return true;
        }else if(checkR == -1){
            if(isSetting) {
                JOptionPane.showMessageDialog(null, "未找到ADB工具，请重新配置ADB工具地址！", "提示", JOptionPane.ERROR_MESSAGE);
            }
            isSetting = true;
            Object adbpathObject = JOptionPane.showInputDialog(null,"请输入ADB工具地址：\n","系统参数设置",JOptionPane.PLAIN_MESSAGE,null,null,"例如：/Users/dee/Downloads/platform-tools/adb");
            if(adbpathObject!=null){
                tempADBPath = adbpathObject.toString();
                SettingToolkit.setADBPath(tempADBPath);
                isADBToolOk();
                return true;
            }else{
                System.exit(0);
            }
        }else if(checkR == -2){
            JOptionPane.showMessageDialog(null, "未找接入的 Android 设备，请检测设备连接情况，确认后再点击确认！", "提示",JOptionPane.ERROR_MESSAGE);
            isADBToolOk();
            return true;
        }
        SettingToolkit.setADBPath(tempADBPath);
        setAdbPath(tempADBPath);
        LogToolKit.println("ADB工具地址设置成功：" + adbPath);
        return true;
    }

    //获取手机屏幕大小 adb shell wm size
    //查看机型 adb shell getprop ro.product.model

    public static void setAdbPath(String adbPath) {
        AdbToolKit.adbPath = adbPath;
    }



    public static void main(String[] args) throws IOException {
        System.out.println(isImageOk());;
    }

}
