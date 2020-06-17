package client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;

public class ClientUi extends JFrame implements ActionListener {
    JFrame jFrame=new JFrame();
    public Client client;
    public PrintWriter printWriter;
    public JPanel jPanel1=new JPanel();
    public JPanel jPanel2=new JPanel();
    public JPanel jPanel3=new JPanel();
    public JPanel jPanel4=new JPanel();
    public JPanel jPanel5=new JPanel();
    public JPanel jPanel6=new JPanel();
    public JPanel jPanel7=new JPanel();
    public static JTextArea jTextArea1=new JTextArea(12,42);
    public static JTextArea jTextArea2 = new JTextArea(12,42);
    public JLabel jLabel=new JLabel("dei");
    public static JComboBox jComboBox=new JComboBox();
    public JCheckBox jCheckBox=new JCheckBox("私聊");
    public JTextField jTextField=new JTextField(36);
    public JButton jButton1=new JButton("发送");
    public JButton jButton2=new JButton("刷新");
    public static DefaultListModel defaultListModel1; //列表格式
    public static JList jList1;//列表

    public String name,message;
    public void getmenu(String name){
        jFrame = new JFrame("【"+name+"】的客户端");
        this.name=name;
        jComboBox.addItem("所有人");
        jTextArea1.setEditable(false);
        jTextArea2.setEditable(false);
        defaultListModel1=new DefaultListModel();
        jList1=new JList(defaultListModel1);
        jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList1.setVisibleRowCount(10);
        jList1.setFixedCellHeight(28);
        jList1.setFixedCellWidth(100);
        JScrollPane jScrollPane1=new JScrollPane(jTextArea1);
        JScrollPane jScrollPane2=new JScrollPane(jTextArea2);
        JScrollPane jScrollPane3=new JScrollPane(jList1);
        jScrollPane1.setBorder(new TitledBorder("公共聊天室"));
        jScrollPane2.setBorder(new TitledBorder("个人频道"));
        jScrollPane3.setBorder(new TitledBorder("好友列表"));
        jPanel1.setLayout(new GridLayout(2,1));//网格布局
        jPanel1.add(jScrollPane1);
        jPanel1.add(jScrollPane2);
        jPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanel2.add(jLabel);
        jPanel2.add(jComboBox);
        jPanel2.add(jCheckBox);
        jPanel3.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanel3.add(jTextField);
        jPanel3.add(jButton1);
        jPanel4.setLayout(new GridLayout(2,1));
        jPanel4.add(jPanel2);
        jPanel4.add(jPanel3);
        jPanel5.setLayout(new BorderLayout());
        jPanel5.add(jPanel1,BorderLayout.NORTH);
        jPanel5.add(jPanel4,BorderLayout.SOUTH);
        jPanel6.setLayout(new BorderLayout());
        jPanel6.add(jScrollPane3,BorderLayout.NORTH);
        jPanel6.add(jButton2,BorderLayout.SOUTH);
        jPanel7.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanel7.add(jPanel6);
        jPanel7.add(jPanel5);
        jFrame.add(jPanel7);
        jFrame.setLocation(200,200);//初始在我电脑上的位置坐标
        jFrame.setSize(650,600);//聊天框大小
        jFrame.setResizable(true);//用户是否可以调整大小
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jButton1.addActionListener(this);//监听点击图标动作
        jButton2.addActionListener(this);//监听点击图标动作
        jTextArea1.setLineWrap(true);//自动换行
        jTextArea2.setLineWrap(true);//自动换行
        jScrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);//垂直滚动条
        jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);//never 横向滚动条
        jScrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jFrame.pack();//自动适应窗口大小
        jFrame.setVisible(true);//是否可见

    }

    public void socket(){
        try{
            String user=name; //讲用户信息以字符串的形式保存
            client=new Client(user);//创建一个客户端对象
            printWriter=new PrintWriter(Client.socket.getOutputStream());//创建输出流
            printWriter.println("100");//发送创建好友列表标识
            printWriter.println(name);//发送用户信息
            printWriter.flush();
            printWriter.println("400");//发送进入聊天室的标识
            printWriter.println("欢迎【"+user+"】来到聊天室≥Ö‿Ö≤");
            printWriter.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ClientUi(){
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try{
                    printWriter=new PrintWriter(Client.socket.getOutputStream());
                    printWriter.println("500");//发送下线标识
                    printWriter.println(name+"下线了");
                    printWriter.flush();
                    jFrame.dispose();//软件关闭窗口
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });
    }
    @Override
    public void actionPerformed(ActionEvent event){
        jButton1.setText("发送");
        jButton2.setText("刷新");
        try{
            printWriter=new PrintWriter(Client.socket.getOutputStream());
            if ("发送".equals(event.getActionCommand())){
                if (!"".equals(jTextField.getText())){
                    if (jCheckBox.isSelected()){
                        String name1=(String)jComboBox.getSelectedItem();
                        message="【私聊消息】 "+name+"对"+name1+"说："+jTextField.getText();
                        printWriter.println("300");//发送私聊标识
                        printWriter.println(name+":"+name1+"911"+message);

                    }
                    else {
                        printWriter.println("200");//发送群聊标识
                        printWriter.println(name+"说"+jTextField.getText());
                    }
                    printWriter.flush();
                }
            }
            else if ("刷新".equals(event.getActionCommand())){
                printWriter=new PrintWriter(Client.socket.getOutputStream());
                printWriter.println("600");//发送刷新的表示
                printWriter.flush();
            }
        }catch (Exception e2){
            e2.printStackTrace();
        }
        jTextField.setText("");//发完聊天栏 清空聊天缓冲区
        jTextField.requestFocus();//指针指向输入框
    }
}
