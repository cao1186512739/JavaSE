package com.zjzy;

import sun.rmi.runtime.Log;

import java.util.logging.Logger;

/**
 * @author ：admin
 * @date ：Created in 2019/9/27 11:04
 * @description：
 * @modified By：
 * @version: $
 */

public class RealImage implements Image {

    private static final String TAG = "RealImage";

    public void display() {
        System.out.println(TAG + "display target...");
    }
}
