package com.github.dee1024.wechatgame.ui;

import com.github.dee1024.wechatgame.service.auto.AutoJumpService;
import com.github.dee1024.wechatgame.service.manual.JumpService;
import com.github.dee1024.wechatgame.tools.AdbToolKit;
import com.github.dee1024.wechatgame.tools.LogToolKit;
import com.github.dee1024.wechatgame.tools.SettingToolkit;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 显示界面、鼠标事件
 *
 * @Description
 * @Author Dee1024 <coolcooldee@gmail.com>
 * @Version 1.0
 * @Since 1.0
 * @Date 2018/1/3
 */

public abstract class WechatGameUI {

    static int fwidth = 600;
    static int fheight = 1000;
    static int width = 600; //default
    static int height = 1000; //default
    static int fx = 0;
    static int fy = 0;

    static JFrame mainFrame ;
    static JPanel corePanel;
    static JPanel settingPanel;
    static JLabel beginLable = new JLabel();
    static JLabel endLable = new JLabel();
    static BufferedImage bufferedImage; //当前截屏图片

    static boolean isAutoJumpStop = true;


    public static void init() {
        mainFrame = new JFrame();
        LogToolKit.println("UI 启动中");
        initWidthHeight();
        mainFrame.setBounds(fx, fy, fwidth, fheight);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        //主界面板
        corePanel = new CoreJPanel();
        //操作面板
        settingPanel = new SettingJPanel();
        mainFrame.add(corePanel, BorderLayout.CENTER);
        mainFrame.add(settingPanel, BorderLayout.SOUTH);
        refreshCorePanelUI();
    }

    private static void initWidthHeight(){
        //获取截图大小，并初始化窗体大小和位置
        int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int screenheight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        fx = (screenWidth-fwidth)/2;
        fy = (screenheight-fheight)/2;
        Image image = getImage4WidthAndHeight();
        if (image != null) {
            int tempWidth = image.getWidth(null);
            int tempHeight = image.getHeight(null);
            StringBuffer resulotionStr = new StringBuffer();
            if (tempHeight > tempWidth) {
                resulotionStr.append(tempWidth).append("*").append(tempHeight);
            } else {
                resulotionStr.append(tempHeight).append("*").append(tempWidth);
            }
            LogToolKit.println("当前手机屏幕分辨率：" + resulotionStr);
            if(SettingToolkit.getUiRate()==null) {
                Double tempUIRate = SettingToolkit.getDefaultUIRatioByResolution(resulotionStr.toString());
                if (tempUIRate != null) {
                    SettingToolkit.setUiRate(tempUIRate);
                    LogToolKit.println("使用系统自适配的UI适配率："+SettingToolkit.getUiRate());
                }else{
                    SettingToolkit.setDefaultTempUiRate();
                    LogToolKit.println("目前系统没有该分辨率的UI适配率，使用固定默认值("+SettingToolkit.getUiRate()+")或者请到【setting.properties】文件内手动配置【ui_rate】参数，参数值可以参考【README.TXT】");
                }
            }else{
                LogToolKit.println("使用设置文件中的UI适配率："+SettingToolkit.getUiRate());
            }
            LogToolKit.println("设置窗体大小比率为："+ SettingToolkit.getUiRate());

            if(SettingToolkit.getJumpRate()==null) {
                Double jumpRate = SettingToolkit.getDefaultJumpRateByResolution(resulotionStr.toString());
                if (jumpRate != null) {
                    SettingToolkit.setJumpRate(jumpRate);
                    LogToolKit.println("使用系统自适配的UI适配率："+SettingToolkit.getJumpRate());
                }else{
                    SettingToolkit.setDefaultTempJumpRate();
                    LogToolKit.println("目前系统没有该分辨率的跳跃系数，使用固定默认值("+SettingToolkit.getJumpRate()+")或者请到【setting.properties】文件内手动配置【jump_rate】参数，参数值可以参考【README.TXT】");
                }
            }else{
                LogToolKit.println("使用设置文件中的跳跃系数："+SettingToolkit.getJumpRate());
            }
            LogToolKit.println("设置跳跃系数为："+SettingToolkit.getJumpRate());
            fwidth = (int) (tempWidth * SettingToolkit.getUiRate());
            fheight = (int) ((tempHeight+200) * SettingToolkit.getUiRate());
            width = (int) (tempWidth * SettingToolkit.getUiRate());
            height = (int) (tempHeight * SettingToolkit.getUiRate());
        }else{
            LogToolKit.println("未找到截图，请确认ADB是否连接正常。");
        }
    }

    private static void refreshCorePanelUI() {
        bufferedImage = AdbToolKit.screencap();
        mainFrame.getComponent(0).validate();
        mainFrame.getComponent(0).repaint();
        beginLable.setText("起跳点：空");
        endLable.setText("目标点：空");
        LogToolKit.println("重新绘制 UI 成功, 等待操作 ...");
        JumpService.setBeginPoint(new Point(0, 0));
        JumpService.setEndPoint(new Point(0, 0));

    }

    class MyLable extends JLabel {
        public MyLable() {

        }
    }

    static class RefreshButton extends JButton {
        public RefreshButton() {
            this.setText("刷新UI");
            this.setVisible(true);
            this.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    refreshCorePanelUI();
                }

                public void mousePressed(MouseEvent e) {

                }

                public void mouseReleased(MouseEvent e) {

                }

                public void mouseEntered(MouseEvent e) {

                }

                public void mouseExited(MouseEvent e) {

                }
            });
        }
    }

    static class AutoJumpDubugButton extends JButton {
        public AutoJumpDubugButton() {
            this.setText("开启自动模式(开发版)");
            this.setVisible(true);
            final JButton ebutton = this;
            this.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    isAutoJumpStop = !isAutoJumpStop;
                    if(isAutoJumpStop){
                        ebutton.setText("开启自动模式(开发版)");
                    }else{
                        ebutton.setText("关闭自动模式(开发版)");
                    }
                    new Thread(new Runnable() {
                        public void run() {
                            while(!isAutoJumpStop) {
                                try {
                                    Thread.sleep(1000);
                                    AutoJumpService.auto(bufferedImage);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                refreshCorePanelUI();
                            }
                        }
                    }).start();
                }

                public void mousePressed(MouseEvent e) {

                }

                public void mouseReleased(MouseEvent e) {

                }

                public void mouseEntered(MouseEvent e) {

                }

                public void mouseExited(MouseEvent e) {

                }
            });
        }
    }


    static  class CoreJPanel extends JPanel {

        public CoreJPanel() {
            this.setBorder(BorderFactory.createTitledBorder("操作"));
            this.setRequestFocusEnabled(true);
            this.addMouseListener(getMyMouseListener());
        }

        public void paint(Graphics g) {
            Image image = getImage4WidthAndHeight();
            if (image == null) {
                LogToolKit.println("未找到资源图片，请检查ADB连接是否正常。");
                return;
            }
            g.drawImage(image, 0, 0, width, height, null);
        }

        private MouseListener getMyMouseListener() {
            return new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    //左键
                    if (MouseEvent.BUTTON1 == e.getButton()) {
                        if (JumpService.getBeginPoint().getX() == 0 && JumpService.getBeginPoint().getY() == 0) {
                            beginLable.setText("起跳点：(" + e.getPoint().getX() + "," + e.getPoint().getY() + ")");
                            JumpService.setBeginPoint(e.getPoint());
                        } else {
                            endLable.setText("目标点：(" + e.getPoint().getX() + "," + e.getPoint().getY() + ")");
                            JumpService.setEndPoint(e.getPoint());
                            new Thread(new Runnable() {
                                public void run() {
                                    JumpService.jump(JumpService.getBeginPoint(), JumpService.getEndPoint());
                                    refreshCorePanelUI();
                                }
                            }).start();
                        }
                    }
                    //右键
                    if (MouseEvent.BUTTON3 == e.getButton()) {
                        refreshCorePanelUI();
                    }
                }

                public void mousePressed(MouseEvent e) {

                }

                public void mouseReleased(MouseEvent e) {

                }

                public void mouseEntered(MouseEvent e) {

                }

                public void mouseExited(MouseEvent e) {

                }

            };
        }

    }

    static class SettingJPanel extends JPanel {
        public SettingJPanel() {
            this.setBorder(BorderFactory.createTitledBorder("操作"));
            this.setLayout(new BorderLayout());
            beginLable = new JLabel("起跳点：空");
            endLable = new JLabel("目标点：空");
            this.add(new AutoJumpDubugButton(), BorderLayout.NORTH);
            this.add(beginLable, BorderLayout.WEST);
            this.add(endLable, BorderLayout.EAST);
            this.add(new RefreshButton(), BorderLayout.SOUTH);
            this.setVisible(true);
        }
    }

    private static Image getImage4WidthAndHeight() {
        File file = new File(SettingToolkit.getScreencapFilePathName());
        if (file.exists() && file.isFile() && file.length() > 0) {
            Image image = null;
            try {
                image = ImageIO.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }
        return null;
    }
}
