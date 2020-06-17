package server;

import client.ClientUi;
import tools.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author lllxh
 */
public class Server {
    private static final int PORT=15555;
    ClientUi clientUi = new ClientUi();
    private ServerSocket server;
    public ArrayList<PrintWriter> list;
    public static String user;
    /**
     * 定义一个用户集合
     */
    public static ArrayList<User> list1=new ArrayList<User>();
    public User uu;

    public Server(String user) {
        this.user=user;
    }

    public void getServer() {
        list =new ArrayList<PrintWriter>();
        try{
            server=new ServerSocket(PORT);
            System.out.println("服务器启动，正在监听... (ง •̀_•́)ง ");
            while(true) {
                //开始监听客户端线程
                Socket client=server.accept();
                PrintWriter writer = new PrintWriter(client.getOutputStream());
                list.add(writer);
                Thread t = new ServerThread(client);
                t.start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server(user).getServer();
    }
    class ServerThread extends Thread {
        Socket socket;
        private BufferedReader bufferedReader;
        private String msg;
        private String mssg="";

        public ServerThread(Socket socket) {
            try{
                this.socket=socket;
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try{
                bufferedReader =new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while((msg= bufferedReader.readLine())!=null)
                {
                    //显示好友列表
                    if(msg.equals("100")) {
                        msg= bufferedReader.readLine();
                        //将用户信息跟信息分隔开
                        String[] st=msg.split(":");
                        //将用户信息添加到User对象中
                        uu=new User(st[0],socket);
                        //将对象添加到用户集合
                        list1.add(uu);
                        //遍历用户集合
                        Iterator<User> it= Server.list1.iterator();
                        while(it.hasNext()) {
                            User use=it.next();
                            msg=use.getName()+":";
                            //将所有的用户信息连接成一个字符串
                            mssg+=msg;
                        }
                        //显示好友列表匹配标识
                        sendMessage("100");
                        //群发消息
                        sendMessage(mssg);
                    }
                    //匹配群聊信息
                    else if(msg.equals("200")) {
                        msg= bufferedReader.readLine();
                        System.out.println(msg);
                        sendMessage("200");
                        sendMessage(msg);
                    }
                    //显示进入聊天室
                    else if(msg.equals("400")) {
                        msg= bufferedReader.readLine();
                        System.out.println(msg);
                        sendMessage("400");
                        sendMessage(msg);
                    }
                    //私聊
                    else if(msg.equals("300"))
                    {
                        msg= bufferedReader.readLine();
                        //把传进来的用户信息与说话内容分开
                        String[] rt=msg.split("911");
                        //在服务端显示说话内容
                        System.out.println(rt[1]);
                        //把源目用户信息分隔开
                        String[] tg=rt[0].split(":");
                        //遍历用户集合
                        Iterator<User> iu= Server.list1.iterator();
                        while(iu.hasNext()) {
                            User se=iu.next();
                            //如果传进来的用户信息跟集合中的用户信息吻合
                            if(tg[1].equals(se.getName())){
                                try{
                                    //建立用户自己的流
                                    PrintWriter pwriter=new PrintWriter(se.getSocket().getOutputStream());
                                    pwriter.println("300");
                                    //像单独用户发送消息
                                    pwriter.println(rt[1]);
                                    pwriter.flush();
                                    System.out.println(rt[1]);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                            //如果传进来的用户信息与集合中的用户信息吻合
                            else if(tg[0].equals(se.getName())) {
                                try{
                                    PrintWriter pwr=new PrintWriter(se.getSocket().getOutputStream());
                                    pwr.println("300");
                                    pwr.println(rt[1]);
                                    pwr.flush();
                                    System.out.println(rt[1]);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    //下线
                    else if(msg.equals("500")) {
                        msg= bufferedReader.readLine();
                        System.out.println(msg);
                        sendMessage("500");
                        sendMessage(msg);
                        String[] si=msg.split(":");
                        Iterator<User> at= Server.list1.iterator();
                        while(at.hasNext()) {
                            User sr=at.next();
                            if(sr.getName().equals(si[0])) {
                                list1.remove(sr);
                                sr.getSocket().close();
                            }
                        }
                        break;
                    }
                    //刷新
                    else if(msg.equals("600")) {
                        String mssge="";
                        Iterator<User> iter= Server.list1.iterator();
                        while(iter.hasNext()) {
                            User uus=iter.next();
                            msg=uus.getName()+":";
                            mssge+=msg;
                        }
                        sendMessage("600");
                        sendMessage(mssge);
                    }
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }

    /**
     * 广播消息
     * @param message
     */
    public void sendMessage(String message)
    {
        try{
            //输出流集合
            for(PrintWriter pw:list) {
                pw.println(message);
                pw.flush();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}




