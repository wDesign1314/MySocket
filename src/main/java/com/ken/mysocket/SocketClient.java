package com.ken.mysocket;

import android.app.DownloadManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/13.
 */

public class SocketClient {
    private static SocketClient instance;
    private Socket serverSocket;

    public SocketClient() {
    }

    public static SocketClient getInstance() {
        if(instance==null) {
            instance=new SocketClient();
        }
        return instance;
    }
    public void initSocket(final String queryData){
        /* * * * * * * * * * 客户端 Socket 通过构造方法连接服务器 * * * * * * * * * */
        
        new Thread(){
            @Override
            public void run() {
                try {
                    // 客户端 Socket 可以通过指定 IP 地址或域名两种方式来连接服务器端,实际最终都是通过 IP 地址来连接服务器  
                    // 新建一个Socket，指定其IP地址及端口号  
                    Socket clientSocket = new Socket("120.77.222.86",1880);
                    // 客户端socket在接收数据时，有两种超时：1. 连接服务器超时，即连接超时；2. 连接服务器成功后，接收服务器数据超时，即接收超时  
                    // 设置 socket 读取数据流的超时时间  
                    clientSocket.setSoTimeout(5000);
                    // 发送数据包，默认为 false，即客户端发送数据采用 Nagle 算法；  
                    // 但是对于实时交互性高的程序，建议其改为 true，即关闭 Nagle 算法，客户端每发送一次数据，无论数据包大小都会将这些数据发送出去  
                    clientSocket.setTcpNoDelay(true);
                    // 设置客户端 socket 关闭时，close() 方法起作用时延迟 30 秒关闭，如果 30 秒内尽量将未发送的数据包发送出去  
                    clientSocket.setSoLinger(true, 30);
                    // 设置输出流的发送缓冲区大小，默认是4KB，即4096字节  
                    clientSocket.setSendBufferSize(4096);
                    // 设置输入流的接收缓冲区大小，默认是4KB，即4096字节  
                    clientSocket.setReceiveBufferSize(4096);
                    // 作用：每隔一段时间检查服务器是否处于活动状态，如果服务器端长时间没响应，自动关闭客户端socket  
                    // 防止服务器端无效时，客户端长时间处于连接状态  
                    clientSocket.setKeepAlive(true);
                    // 客户端向服务器端发送数据，获取客户端向服务器端输出流  
                    OutputStream osSend = clientSocket.getOutputStream();
                    OutputStreamWriter osWrite = new OutputStreamWriter(osSend);
                    BufferedWriter bufWrite = new BufferedWriter(osWrite);
                    // 代表可以立即向服务器端发送单字节数据  
                    clientSocket.setOOBInline(true);
                    // 数据不经过输出缓冲区，立即发送  
                    clientSocket.sendUrgentData(0x44);//"D"  
                    // 向服务器端写数据，写入一个缓冲区  
                    // 注：此处字符串最后必须包含“\r\n\r\n”，告诉服务器HTTP头已经结束，可以处理数据，否则会造成下面的读取数据出现阻塞  
                    // 在write() 方法中可以定义规则，与后台匹配来识别相应的功能，例如登录Login() 方法，可以写为write("Login|LeoZheng,0603 \r\n\r\n"),供后台识别;  
                    bufWrite.write(queryData);
                    // 发送缓冲区中数据 - 前面说调用 flush() 无效，可能是调用的方法不对吧！  
                    bufWrite.flush();


                    //读取发来服务器信息
                    BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    
                    String len = "";
                    
                    while ((len = br.readLine()) != null) {
                        System.out.println("queryDatases==" + len);
                    }
                   
                    
                    br.close();
                    clientSocket.close();
                    
                    
                }
                catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void connectS(){
        /* * * * * * * * * * Socket 客户端通过 connect 方法连接服务器 * * * * * * * * * */
                try {
                    serverSocket = new Socket();
                    // 使用默认的连接超时  
                    serverSocket.connect(new InetSocketAddress("172.25.103.1",12589));        // 连接超时 3 秒: serverSocket.connect(new InetSocketAddress("172.25.103.1",12589),3000);  

                    // 关闭 socket  
                    serverSocket.close();
                }
                catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
        
        
    }
    
    public void reciveData(){
        /* * * * * * * * * * Socket 客户端读取服务器端响应数据 * * * * * * * * * */
        try {
            // serverSocket.isConnected 代表是否连接成功过  
            // 判断 Socket 是否处于连接状态  
            if(true == serverSocket.isConnected() && false == serverSocket.isClosed()) {
                // 客户端接收服务器端的响应，读取服务器端向客户端的输入流  
                InputStream isRead = serverSocket.getInputStream();
                // 缓冲区  
                byte[] buffer = new byte[isRead.available()];
                // 读取缓冲区  
                isRead.read(buffer);
                // 转换为字符串  
                String responseInfo = new String(buffer);
                // 日志中输出  
                System.out.println("Socket Server"+ responseInfo);
                
            }
            // 关闭网络  
            serverSocket.close();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
