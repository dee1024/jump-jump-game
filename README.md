# 微信 跳一跳 wechat jump game
微信 跳一跳 辅助工具 5分钟上手 最高分已经跳出天际线超过整形最大值 智能识别跳跃 增加随机跳跃因子  ~ 👻 👻 👻  可学习可娱乐

特性
===
- __代码精简，流程简单，三步即开始游戏刷分__　
- __可以手动模式，鼠标操作简单，选中起跳点和目标点__
- __可开启全自动模式，分数无上限__　
- __增加随机因子，防止微信识别为"WAI GUA"__
- __正常手动模式和精准跳跃模式可以随时切换（通过模式混淆，更好的防止封杀）__

运行环境
====
- __Android + Windows 7+__　
- __Android + Mac OS__
- __Android + Linux__
- __iOS + Mac OS（研发中，期待与有WDA开发经验的朋友一起参与交流）__

支持手机分辨率
=======
- __1600*2560__　
- __1440*2560__
- __1080*2220__
- __1080*1920__
- __720*1280__
- __540*960__
- __其他分辨率__
- __三星 S7、S8__
- __华为 NOTE8、V8__
- __小米 Max2、5、5s 5x、6、NOTE2__
- __锤子pro2__
- __其他机型或者分辨率可以加入QQ群进行适配__



快速开始
======
- __步骤一：安装JDK__

    安装JDK7或者以上版本，具体可参考[这里](https://www.cnblogs.com/takeyblogs/p/7457913.html)参考

- __步骤二：下载 Android Debug Bridge__

    推荐下载地址，可参考：

    [ADB for Windows](https://dl.google.com/android/repository/platform-tools-latest-windows.zip)

    [ADB for Mac](https://dl.google.com/android/repository/platform-tools-latest-darwin.zip)

    [ADB for Linux](https://dl.google.com/android/repository/platform-tools-latest-linux.zip)

- __步骤三：准备 Android 手机__

    Android 手机开启 USB 调试模式，具体可参考[这里](https://jingyan.baidu.com/article/0eb457e50b99d003f0a9055f.html)

    ![](https://raw.githubusercontent.com/dee1024/jump-jump-game/master/doc/androiddebug.png)


    Android 手机连接电脑并且开启小游戏，处于可操作界面

- __步骤四：下载本应用并执行__

    点击[这里](https://github.com/dee1024/wechat-jump-game/archive/master.zip)下载本应用的 Zip 包并解压，进入目录执行命令
    ```bash
    java -jar wechat-jump-game-1.0.jar
    ```

- __步骤五：按照引导配置ADB地址__

    ![](https://raw.githubusercontent.com/dee1024/jump-jump-game/master/doc/adb-setting.png)

- __步骤六：点击鼠标开始游戏__

    1.鼠标左键点击 "黑色小人底部"

    2.鼠标左键点击 "目标块中心位置"

    3.即可看到黑色小人蓄力跳动

    4.自动刷新 UI，然后重复以上操作

    5.注意使用正常游戏模式和鼠标精准跳跃模式混搭的情况下，需要使用到 "鼠标右键" ，点击一次 "鼠标右键" 可以同步当前手机UI，然后即可进行鼠标精准跳跃

其他说明
====
- __如果出现跳跃距离过小或者偏大的情况，可以通过设置 jump_rate 跳跃系数来校正跳跃距离，改配置项在您运行的 jar 文件同目录的一个setting.properties 文件中，该值的设置值可以参考[这里](https://github.com/dee1024/wechat-jump-jump-game/blob/master/doc/readme.txt)__
- __如果电脑端显示的屏幕过小或者偏大，可以通过设置 ui_rate 显示缩放比例来适配，配置方式参考同上,参考[这里](https://github.com/dee1024/wechat-jump-jump-game/blob/master/doc/readme.txt)__

刷分效果
====
- __效果图__

    ![image](https://raw.githubusercontent.com/dee1024/jump-jump-game/master/doc/demo.gif)




贡献
===
我们期待你的 P.R !

作者
===
* __Dee Qiu__ <coolcooldee@gmail.com>

其它
===
* __QQ群( Github开源分享群 )__ 570997546





