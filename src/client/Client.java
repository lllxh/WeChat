package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.BufferPoolMXBean;
import java.net.Socket;
import java.security.Principal;
import java.security.PublicKey;

public class Client {
    private static final int PORT = 15555;//定义端口数值
    public static String user;
    public static Socket socket;

    public Client(String user){
        this.user=user;
        try{
            socket=new Socket("localhost",PORT);//建立Socket连接
            System.out.println("欢迎【"+user+"】来到聊天室≥Ö‿Ö≤");//
            Thread thread=new CreatThread(socket,user);
            thread.start();


        }catch (Exception e){
            e.printStackTrace();
        }


    }


}

class CreatThread extends Thread {
    public String user;
    private Socket socket;
    private BufferedReader bufferedReader1;
    public BufferedReader bufferedReader2;
    private PrintWriter printWriter;
    private String message;
    ClientUi clientUi = new ClientUi();

    public CreatThread(Socket socket, String user) throws IOException {
        try {
            this.socket = socket;
            this.user = user;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            bufferedReader2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((message=bufferedReader2.readLine())!=null){
                String msg = message;
                // 创建好友列表
                if (msg.equals("100")){
                    clientUi.defaultListModel1.clear();
                    clientUi.jComboBox.removeAllItems();//清空复选框
                    clientUi.jComboBox.addItem("所有人");
                    msg=bufferedReader2.readLine();
                    String[] str = msg.split(":");
                    for (String ss:str){
                        clientUi.defaultListModel1.addElement(ss);//讲所有用户添加到好友列表
                        clientUi.jComboBox.addItem(ss);//将所有用户信息添加到复选框
                    }
                }
                // 显示群聊消息
                else if (msg.equals("200")){
                    msg=bufferedReader2.readLine();
                  System.out.println("收到"+msg);
                    clientUi.jTextArea1.append(msg+"\n");
                    clientUi.jTextArea2.append(msg+"\n");

                }
                //私聊
                else if (msg.equals("300")){
                    msg=bufferedReader2.readLine();
                    System.out.println("收到"+msg);
                    clientUi.jTextArea2.append(msg+"\n");
                }
                //显示用户进入聊天室
                else if (msg.equals("400")){
                    msg=bufferedReader2.readLine();
                    clientUi.jTextArea1.append(msg+"\n");
                    //clientUi.jTextArea2.append(msg+"\n");
                }
                //下线
                else if (msg.equals("500")){
                    msg=bufferedReader2.readLine();
                    clientUi.jTextArea1.append(msg+"\n");
                    clientUi.jTextArea2.append(msg+"\n");
                }
                //刷新好友列表
                else if (msg.equals("600")){
                    clientUi.defaultListModel1.clear();
                    clientUi.jComboBox.removeAllItems();//清空复选框
                    clientUi.jComboBox.addItem("所有人");
                    msg=bufferedReader2.readLine();
                    String[] str = msg.split(":");
                    for (String ssr:str){
                        clientUi.defaultListModel1.addElement(ssr);//讲所有用户添加到好友列表
                        clientUi.jComboBox.addItem(ssr);//将所有用户信息添加到复选框
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}