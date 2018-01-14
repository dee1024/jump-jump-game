package com.github.coolcooldee.wechatgame.tools;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件工具类
 *
 * @author Dee1024 <coolcooldee@gmail.com>
 * @version 1.0
 * @description
 * @date 2018/1/3
 * @since 1.0
 */

public abstract class SettingToolkit {

    private static Double jumpRate;
    private static Double uiRate;

    private final static String SCREENCAP_FILE_NAME = "jumpgame.png";
    private final static String README_FILE_NAME = "readme.txt";
    private final static String SETTING_FILE_NAME = "setting.properties";
    private final static String PRO_ADB_PATH = "adb_path"; //adb工具路径配置项
    private final static String PRO_UI_RATE = "ui_rate"; //界面的缩放比率配置项
    private final static String PRO_JUMP_RATE = "jump_rate"; //跳跃系数配置项

    //分辨率与按压时长的比率关系，其他分辨率或者不兼容的通过settting.properties文件配置
    private static final Map<String, Double> defaultResolutionMapJumpRate = new HashMap<String, Double>();
    static {
        defaultResolutionMapJumpRate.put("1600*2560",0.92);
        defaultResolutionMapJumpRate.put("1440*2960",1.02);
        defaultResolutionMapJumpRate.put("1440*2560",1.475); //ok
        defaultResolutionMapJumpRate.put("1080*2220",1.392);
        defaultResolutionMapJumpRate.put("1080*2160",1.372);
        defaultResolutionMapJumpRate.put("1080*1920",1.392);//ok
        defaultResolutionMapJumpRate.put("720*1440",2.078);//ok
        defaultResolutionMapJumpRate.put("720*1280",2.099);//ok
        defaultResolutionMapJumpRate.put("540*960",2.732);//ok
    }

    //分辨率与显示界面的实际长宽的比率
    private static final Map<String, Double> defaultResolutionMapUIRate = new HashMap<String, Double>();
    static {
        defaultResolutionMapUIRate.put("1600*2560",0.25);
        defaultResolutionMapUIRate.put("1440*2960",0.25);
        defaultResolutionMapUIRate.put("1440*2560",0.4);
        defaultResolutionMapUIRate.put("1080*2220",0.4);
        defaultResolutionMapUIRate.put("1080*2160",0.4);
        defaultResolutionMapUIRate.put("1080*1920",0.4); //ok
        defaultResolutionMapUIRate.put("720*1440",0.5);  //ok
        defaultResolutionMapUIRate.put("720*1280",0.5);  //ok
        defaultResolutionMapUIRate.put("540*960",0.5);  //ok
    }

    public static void init(){
        String jarPath = System.getProperty("user.dir")+File.separator;
        LogToolKit.println("当前应用目录为："+jarPath);
        File readFile = new File(README_FILE_NAME);
        if(!readFile.exists()){
            try {
                boolean r =readFile.createNewFile();
                if(r) {
                    //初始化说明信息
                    FileOutputStream newFileOutputStream = new FileOutputStream(README_FILE_NAME);
                    OutputStreamWriter osw = new OutputStreamWriter(newFileOutputStream, "UTF-8");
                    osw.write(genInitDesc());
                    osw.flush();
                    osw.close();
                }else{
                    LogToolKit.println(" README.TXT创建成功！");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File file = new File(SETTING_FILE_NAME);
        if(!file.exists()){
            try {
                boolean r =file.createNewFile();
                if(r) {
                    //初始化说明信息

                    FileOutputStream newFileOutputStream = new FileOutputStream(SETTING_FILE_NAME);
                    Properties properties = new Properties();
                    properties.setProperty(PRO_ADB_PATH,"E:\\software\\android-platform-tools\\adb");
                    properties.setProperty(PRO_UI_RATE,"");
                    properties.setProperty(PRO_JUMP_RATE,"");
                    properties.store(newFileOutputStream, "app setting");

                    newFileOutputStream.flush();
                    newFileOutputStream.close();
                    LogToolKit.println("成功创建并初始化配置文件 "+jarPath+ SETTING_FILE_NAME);
                }else{
                    LogToolKit.println(" 配置文件创建失败！");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String genInitDesc(){
        StringBuffer sb = new StringBuffer();
        sb.append("参数说明").append("\n");
        sb.append(PRO_ADB_PATH).append(" : ADB工具路径，如：\"E\\:\\\\software\\\\android-platform-tools\\\\adb\"。").append("\n");
        sb.append(PRO_UI_RATE).append(" : UI界面缩放比例，控制界面大小,，方便全屏显示。").append("\n");
        sb.append(PRO_JUMP_RATE).append(" : 跳跃系数，跳跃距离与手机屏幕的相对比值；跳跃距离偏大或者偏小是可以通过该值来校正。").append("\n");
        sb.append("\t以下为常见机型跳跃系数的参考值：").append("\n");

        sb.append("\t一般机型 480*850 系数值为 ：3.100").append("\n");
        sb.append("\t一般机型 540*960 系数值为 ：2.732").append("\n");
        sb.append("\t一般机型 720*1280 系数值为 ：2.099").append("\n");
        sb.append("\t一般机型 720*1440 系数值为 ：2.099").append("\n");
        sb.append("\t一般机型 1080*1920 系数值为 ：1.392").append("\n");
        sb.append("\t一般机型 1080*2160 系数值为 ：1.372").append("\n");
        sb.append("\t一般机型 1440*2560 系数值为 ：1.475").append("\n");
        sb.append("\t华为荣耀 NOTE8 系数值为 ：1.04").append("\n");
        sb.append("\t华为荣耀 V8 系数值为 ：1.07").append("\n");

        sb.append("\t小米Max2 系数值为 ：1.5").append("\n");
        sb.append("\t小米5 系数值为 ：1.475").append("\n");
        sb.append("\t小米5s 系数值为 ：1.475").append("\n");
        sb.append("\t小米5x 系数值为 ：1.45").append("\n");
        sb.append("\t小米6 系数值为 ：1.44").append("\n");
        sb.append("\t小米NOTE2 系数值为：1.47").append("\n");

        sb.append("\t三星S7 系数值为 ：1.0").append("\n");
        sb.append("\t三星S8 系数值为 ：1.365 （注意该机型需要在设置里【关闭曲面侧屏】）").append("\n");

        sb.append("\t锤子pro2 系数值为 ：1.392").append("\n\n\n");

        return sb.toString();
    }

    public static String getADBPath(){
        return getSettingProperties().getProperty(PRO_ADB_PATH);
    }

    public static Double getUiRate(){
        if(uiRate==null) {
            String temp = getSettingProperties().getProperty(PRO_UI_RATE);
            if (temp == null || "".endsWith(temp)) {
                //uiRate = 0.4;
                return null;
            }else {
                Double tempD = Double.parseDouble(temp.trim());
                if (tempD < 0 || tempD > 1) {
                    //uiRate = 0.4;
                    return null;
                }
                uiRate = tempD;
            }
        }
        return uiRate;
    }

    public static void setDefaultTempUiRate(){
        uiRate = 0.4;
    }

    public static void setDefaultTempJumpRate(){
        jumpRate = 1.392;
    }

    public static Double getJumpRate(){
        if(jumpRate ==null) {
            String temp = getSettingProperties().getProperty(PRO_JUMP_RATE);
            if (temp == null || "".endsWith(temp)) {
                //jumpRate = 1.392;
                return null;
            }else {
                Double tempD = Double.parseDouble(temp.trim());
                if (tempD < 0 || tempD > 5) {
                    //jumpRate = 1.392;
                    return null;
                }
                jumpRate = tempD;
            }
        }
        return jumpRate;
    }

    public static void setADBPath(String path){
        Properties pro = getSettingProperties();
        pro.setProperty(PRO_ADB_PATH, path);
        FileOutputStream oFile = null;
        try {
            oFile = new FileOutputStream(SETTING_FILE_NAME);
            pro.store(oFile, "app setting");
            oFile.close();
            LogToolKit.println("保存ADB地址配置信息到 "+ SETTING_FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Properties getSettingProperties(){
        Properties pro = new Properties();
        File file = new File(SETTING_FILE_NAME);
        if(file.exists()){
            //LogToolKit.println("读取配置文件 "+SETTING_FILE_NAME);
            try {
                FileInputStream in = new FileInputStream(SETTING_FILE_NAME);
                pro.load(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return pro;
        }else{
            try {
                boolean r =file.createNewFile();
                if(r) {
                    FileInputStream in = new FileInputStream(SETTING_FILE_NAME);
                    pro.load(in);
                    in.close();
                    LogToolKit.println("成功创建配置文件 "+ SETTING_FILE_NAME);
                }else{
                    LogToolKit.println(" 配置文件创建失败！");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return pro;
        }
    }

    private static void initDescInfo(File file, String content){
        byte bt[] = new byte[1024];
        bt = content.getBytes();
        try {
            FileOutputStream in = new FileOutputStream(file);
            try {
                in.write(bt, 0, bt.length);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getScreencapFilePathName(){
        return SettingToolkit.SCREENCAP_FILE_NAME;
    }

    public static void setJumpRate(Double jumpRate) {
        SettingToolkit.jumpRate = jumpRate;
    }

    public static void setUiRate(Double uiRate) {
        SettingToolkit.uiRate = uiRate;
    }

    public static Double getDefaultJumpRateByResolution(String resolution){
        return defaultResolutionMapJumpRate.get(resolution);
    }

    public static Double getDefaultUIRatioByResolution(String resolution){
        return defaultResolutionMapUIRate.get(resolution);
    }


}
