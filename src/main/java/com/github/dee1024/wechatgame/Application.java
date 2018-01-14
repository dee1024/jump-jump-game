package com.github.coolcooldee.wechatgame;

/**
 * @Description
 * @Author Dee1024 <coolcooldee@gmail.com>
 * @Version 1.0
 * @Since 1.0
 * @Date 2018/1/3
 */

import com.github.coolcooldee.wechatgame.tools.LogToolKit;
import com.github.coolcooldee.wechatgame.tools.SettingToolkit;
import com.github.coolcooldee.wechatgame.ui.WechatGameUI;
import com.github.coolcooldee.wechatgame.tools.AdbToolKit;

/**
 * 应用启动
 */
public class Application {
    public static void main(String[] args) {
        LogToolKit.println("V1.0.20180114.stable");
        SettingToolkit.init();
        AdbToolKit.init();
        WechatGameUI.init();
    }

}
