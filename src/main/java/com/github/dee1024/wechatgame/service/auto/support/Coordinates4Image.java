package com.github.dee1024.wechatgame.service.auto.support;

/**
 * @author Dee1024 <coolcooldee@gmail.com>
 * @version 1.0
 * @description
 * @date 2018/1/3
 * @since 1.0
 */

public class Coordinates4Image {

    private int x;
    private int y;

    public Coordinates4Image(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(x=" + x + ", y=" + y + ")";
    }
}
