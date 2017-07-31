package com.test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author YangQing
 * @version 1.0.0
 */

/*
 * 物理地址是48位，别和ipv6搞错了
 */
public class LocalmacTest {
    /**
     * @param args
     * @throws UnknownHostException
     * @throws SocketException
     */
    public static void main(String[] args) throws UnknownHostException, SocketException {
        // TODO Auto-generated method stub

        Set<String> ss=new HashSet<>();
            for (int i=0;i<100000;i++){
                ss.add(getFixLenthString(10));
            }
        System.out.println(ss.size());
//        InetAddress ia = InetAddress.getLocalHost();
//        System.out.println(ia);
//        getLocalMac(ia);
    }
    private static String getFixLenthString(int strLength) {

        Random rm = new Random();

        // 获得随机数
        int pross =  (int)(rm.nextDouble()*10000000);

        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross );

        // 返回固定的长度的随机数
        return fixLenthString;
    }
    private static void getLocalMac(InetAddress ia) throws SocketException {
        // TODO Auto-generated method stub
        //获取网卡，获取地址
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        System.out.println("mac数组长度：" + mac.length);
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i] & 0xff;
            String str = Integer.toHexString(temp);
            System.out.println("每8位:" + str);
            if (str.length() == 1) {
                sb.append("0" + str);
            } else {
                sb.append(str);
            }
        }
        System.out.println("本机MAC地址:" + sb.toString().toUpperCase());
    }
}