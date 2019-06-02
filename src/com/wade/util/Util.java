package com.wade.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengwei on 2019/6/2.
 */
public class Util {
    public static boolean validIP (String ip){
        String[] a = ip.split("\\.");
        if( a.length == 4){
            for(int index = 0; index < a.length; index++){
                if( 255 < Integer.parseInt(a[index]) || 0 >= Integer.parseInt(a[index])){
                    return false;
                }
            }
            return true;
        }
        return false;

    }
    public static List<String> genIp(String ip){
        List<String> result = new ArrayList<String>();
        if(validIP(ip)){
            String[] a = ip.split("\\.");

            for(int i = 1; i< 255 ; i++){
                result.add(a[0]+"."+a[1]+"."+a[2]+"."+ Integer.toString(i));
            }
        }
        return result;
    }
    public static boolean testPortOpen(String host, int port){
        //verify host format
        if(!validIP(host)) return false;
        Socket socket = null;
        try {
            System.out.println("connecting "+ host + " " + port);
            socket = new Socket(host, port);
            int timeout = 100;//ms
            socket.setSoTimeout(timeout);
            socket.connect(new InetSocketAddress(host,port), timeout);
            return true;
        }catch (Exception e){
            System.out.println(host + " may timeout");
            return false;
        }finally {
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
