package com.zjzy;

/**
 * @author ：admin
 * @date ：Created in 2019/9/27 11:07
 * @description：
 * @modified By：
 * @version: $
 */

public class ProxyImage {

    private static final String TAG = "ProxyImage";

    private RealImage realImage;

    public ProxyImage(RealImage realImage) {
        this.realImage = realImage;
    }

    public void display() {
        System.out.println(TAG + "Before display...");
        realImage.display();
        System.out.println(TAG + "After display...");
    }
}
