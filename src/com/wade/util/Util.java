package com.wade.util;

import java.io.IOException;
import java.net.Socket;

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
    public static boolean testPortOpen(String host, int port){
        //verify host format
        if(!Util.validIP(host)) return false;
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
}
