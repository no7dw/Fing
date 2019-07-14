package com.wade.fing.rtsp;

/**
 * Created by dengwei on 2019/7/14.
 */

import javax.media.*;
import javax.media.format.*;
import javax.media.protocol.*;
import javax.media.rtp.*;
import javax.media.rtp.event.*;
import javax.media.rtp.rtcp.*;
import java.io.IOException;

public class RTPSocketPlayer implements ControllerListener {
    RTPSocket rtpSocket = null;

    public static void main(String[] args) {
        new RTPSocketPlayer("localhost", 554);
        System.out.println("rtsp");
    }

    public void controllerUpdate(ControllerEvent var1) {

    }

    RTPSocketPlayer(String addr, int port) {
        rtpSocket = new RTPSocket();
        rtpSocket.setContentType("rtpraw/" + "video");
        try {
            rtpSocket.connect();
        } catch (IOException e) {
        }

    }
}
