package com.wade.fing;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import org.pcap4j.*;
/**
 * Created by dengwei on 2019/6/1.
 */
import com.wade.fing.util.Util;
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
                    //TODO: use exists IPList instead of generate subnet ips
                    //TODO: loop it in thread
                    List<String> ipList = Util.genSubnetIp(addrInfo);
                    String[] arrayIpList = ipList.toArray(new String[ipList.size()]);
                    for (String subAddrInfo:arrayIpList) {
                        System.out.println(subAddrInfo);
                        //TODO first check well-known port , then other port
                        for (int port = 80; port < 1024 ; port++) {
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