package com.zjzy;

/**
 * @author ：admin
 * @date ：Created in 2019/9/27 11:08
 * @description：
 * @modified By：
 * @version: $
 */

public class Main {
    public static void main(String[] args) {
        RealImage realImage = new RealImage();
        ProxyImage proxyImage = new ProxyImage(realImage);
        proxyImage.display();
    }
}
