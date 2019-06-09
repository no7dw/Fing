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
                    //InetAddress addr=InetAddress.getByName(mynetworkips + new Integer(i).toString());
                    //addr.isReachable(1000)
                    for (String subAddrInfo:arrayIpList) {
                        System.out.println(subAddrInfo);
                        //TODO first check well-known port , then other port
                        //https://www.jiangyu.org/port-and-rtsp-address-of-several-ipcams/
                        int[] ports = new int[]{80,443, 554, 3702, 5000, 8000, 8080,8899, 8554, 34567};
                        for (int port : ports) {
                            boolean open =  Util.testPortOpen(subAddrInfo, port);
                            if (open) {
                                System.out.println(port + " is open" );
                            }
                        }
                        //TODO check RTSP
                    }

                }

            }

        }catch (SocketException e){
            System.out.println(e.getMessage());
        }
    }
}
//https://www.programcreek.com/java-api-examples/?class=java.net.InterfaceAddress&method=getNetworkPrefixLength