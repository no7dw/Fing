package com.wade.fing.rtsp;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class RTSPClient extends Thread implements IEvent {

    private static final String VERSION = " RTSP/1.0/r/n";
    private static final String RTSP_OK = "RTSP/1.0 200 OK";

    /** */
    /**
     * 远程地址
     */
    private final InetSocketAddress remoteAddress;

    /** */
    /**
     * 本地地址
     */
    private final InetSocketAddress localAddress;

    /** */
    /**
     * 连接通道
     */
    private SocketChannel socketChannel;
    /** */
    /**
     * 发送缓冲区
     */
    private final ByteBuffer sendBuf;

    /** */
    /**
     * 接收缓冲区
     */
    private final ByteBuffer receiveBuf;
    private static final int BUFFER_SIZE = 8192;

    /** */
    /**
     * 端口选择器
     */
    private Selector selector;

    private String address;

    private Status sysStatus;

    private String sessionid;

    /** */
    /**
     * 线程是否结束的标志
     */
    private AtomicBoolean shutdown;

    private int seq = 1;

    private boolean isSended;

    private String trackInfo;


    private enum Status {
        init, options, describe, setup, play, pause, teardown
    }

    public RTSPClient(InetSocketAddress remoteAddress,
                      InetSocketAddress localAddress, String address) {
        this.remoteAddress = remoteAddress;
        this.localAddress = localAddress;
        this.address = address;

        // 初始化缓冲区
        sendBuf = ByteBuffer.allocateDirect(BUFFER_SIZE);
        receiveBuf = ByteBuffer.allocateDirect(BUFFER_SIZE);
        if (selector == null) {
            // 创建新的Selector
            try {
                selector = Selector.open();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        startup();
        sysStatus = Status.init;
        shutdown = new AtomicBoolean(false);
        isSended = false;
    }

    public void startup() {
        try {
            // 打开通道
            socketChannel = SocketChannel.open();
            // 绑定到本地端口
            socketChannel.socket().setSoTimeout(30000);
            socketChannel.configureBlocking(false);
            socketChannel.socket().bind(localAddress);
            if (socketChannel.connect(remoteAddress)) {
                System.out.println("开始建立连接:" + remoteAddress);
            }
            socketChannel.register(selector, SelectionKey.OP_CONNECT
                    | SelectionKey.OP_READ | SelectionKey.OP_WRITE, this);
            System.out.println("端口打开成功");

        } catch (final IOException e1) {
            e1.printStackTrace();
        }
    }


    public boolean isConnected() {
        return socketChannel != null && socketChannel.isConnected();
    }

    public void shutdown() {
        if (isConnected()) {
            try {
                socketChannel.close();
                System.out.println("端口关闭成功");
            } catch (final IOException e) {
                System.out.println("端口关闭错误:");
            } finally {
                socketChannel = null;
            }
        } else {
            System.out.println("通道为空或者没有连接");
        }
    }

    @Override
    public void run() {
        // 启动主循环流程
        while (!shutdown.get()) {
            try {
                if (isConnected() && (!isSended)) {
                    switch (sysStatus) {
                        case init:
                            doOption();
                            isSended = true;
                            break;
                        default:
                            break;
                    }

                }

            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        shutdown();
    }

    public void send(byte[] out) {
        if (out == null || out.length < 1) {
            return;
        }
        synchronized (sendBuf) {
            sendBuf.clear();
            sendBuf.put(out);
            sendBuf.flip();
        }

        // 发送出去
        try {
            write();
            isSended = true;
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void read(SelectionKey key) throws IOException {
    }

    public void write() throws IOException {
        if (isConnected()) {
            try {
                socketChannel.write(sendBuf);
            } catch (final IOException e) {
            }
        } else {
            System.out.println("通道为空或者没有连接上");
        }
    }

    private void doOption() {
        StringBuilder sb = new StringBuilder();
        sb.append("OPTIONS ");
        sb.append(this.address.substring(0, address.lastIndexOf("/")));
        sb.append(VERSION);
        sb.append("Cseq: ");
        sb.append(seq++);
        sb.append("/r/n");
        sb.append("/r/n");
        System.out.println(sb.toString());
        send(sb.toString().getBytes());
    }

    public void connect(SelectionKey key) throws IOException {
        if (isConnected()) {
            return;
        }
        // 完成SocketChannel的连接
        socketChannel.finishConnect();
        while (!socketChannel.isConnected()) {
            try {
                Thread.sleep(300);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
            socketChannel.finishConnect();
        }

    }

    public static void main(String[] args) {
        try {
            RTSPClient client = new RTSPClient(
                    new InetSocketAddress("218.207.101.236", 554),
                    new InetSocketAddress("192.168.2.28", 0),
                    "rtsp://218.207.101.236:554/mobile/3/67A451E937422331/8jH5QPU5GWS07Ugn.sdp");
            client.start();
            System.out.println(client.isSended);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
//https://blog.csdn.net/xyz_lmn/article/details/6055179