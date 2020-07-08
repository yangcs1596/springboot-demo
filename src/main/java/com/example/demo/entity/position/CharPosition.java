package com.example.demo.entity.position;

/**
 * @Author: 杨春生
 * @Date: 2019/11/29 10:21
 * @Description:
 */
public class CharPosition {
    private int pageNum = 0;
    private float x = 0;
    private float y = 0;


    public CharPosition(int pageNum, float x, float y) {
        this.pageNum = pageNum;
        this.x = x;
        this.y = y;
    }


    public int getPageNum() {
        return pageNum;
    }


    public float getX() {
        return x;
    }


    public float getY() {
        return y;
    }


    @Override
    public String toString() {
        return "[pageNum=" + this.pageNum + ",x=" + this.x + ",y=" + this.y + "]";
    }
}
