package com.wade.fingnet;


import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by dengwei on 2019/6/1.
 */
import com.wade.util.Util;
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
                    String addrInfo = i.getHostAddress(); //+ " " + i.getHostName();
                    NetworkInterface net = NetworkInterface.getByInetAddress(i);
                    short n = net.getInterfaceAddresses().get(0).getNetworkPrefixLength();//0 IPV4 1 IPV6
                    System.out.println(addrInfo+"/"+n);
                    List<String> ipList = Util.genIp(addrInfo);
                    String[] arrayIpList = ipList.toArray(new String[ipList.size()]);
                    for (String subAddrInfo:arrayIpList) {
                        for (int port = 1; port < 1024 ; port++) {
                            boolean open =  Util.testPortOpen(subAddrInfo, port);
                            if (open) {
                                System.out.println(port + " is open" );
                            }
                        }

                    }

                }

            }

        }catch (SocketException e){
            System.out.println(e.getMessage());
        }
    }
}
//https://www.programcreek.com/java-api-examples/?class=java.net.InterfaceAddress&method=getNetworkPrefixLength