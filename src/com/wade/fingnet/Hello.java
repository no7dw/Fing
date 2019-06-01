package com.wade.fingnet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by dengwei on 2019/6/1.
 */
public class Hello {
    public static boolean validIP (String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    public static boolean testPortOpen(String host, int port){
        //verify host format
        if(!validIP(host)) return false;
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            return true;
        }catch (Exception e){
            return false;
        }finally {
            try{socket.close();}
            catch (IOException e){}
        }




    }
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
                    int port = 22;
                    boolean open =  testPortOpen(addrInfo, port);
                    if (open) {
                        System.out.println(port + " is open" );
                    }
                }

            }

        }catch (SocketException e){
            System.out.println(e.getMessage());
        }
    }
}
//https://www.programcreek.com/java-api-examples/?class=java.net.InterfaceAddress&method=getNetworkPrefixLength