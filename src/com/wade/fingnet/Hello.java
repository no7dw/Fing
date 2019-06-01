package com.wade.fingnet;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by dengwei on 2019/6/1.
 */
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hey Babe");
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) e.nextElement();
                Enumeration ee = networkInterface.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    String addrInfo = i.getHostAddress() + " " + i.getHostName();
                    System.out.println(addrInfo);
                }
            }
        }catch (SocketException e){
            System.out.println(e.getMessage());
        }
    }
}
