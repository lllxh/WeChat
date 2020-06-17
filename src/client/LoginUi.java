package client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 登录UI
 * @author lllxh
 */
public class LoginUi extends Frame implements ActionListener {
    JFrame jFrame =new JFrame("登录界面");
    JPanel jPanel1 =new JPanel();
    JPanel jPanel2 =new JPanel();
    JPanel jPanel3 =new JPanel();
    JPanel jPanel4 =new JPanel();
    JLabel jLabel1 =new JLabel("姓名：");
    JLabel jLabel2 =new JLabel("地址：");
    JLabel jLabel3 =new JLabel("端口：");
    public JTextField jTextField1 =new JTextField(10);
    public JTextField jTextField2 =new JTextField(10);
    public JTextField jTextField3 =new JTextField(10);
    JButton jButton1 =new JButton("登录");
    JButton jButton2 =new JButton("取消");
    TitledBorder titledBorder =new TitledBorder("");
    public LoginUi(){
        init();
    }
    /**
     * 初始化登录界面
     */
    public void init(){
        jButton1.addActionListener(this);
        jButton2.addActionListener(this);
        jPanel1.add(jLabel1);
        jPanel1.add(jTextField1);
        jPanel2.add(jLabel2);
        jPanel2.add(jTextField2);
        jPanel2.add(jLabel3);
        jPanel2.add(jTextField3);
        jPanel3.add(jButton1);
        jPanel3.add(jButton2);
        jPanel4.setLayout(new GridLayout(3,1));
        jPanel4.add(jPanel1);
        jPanel4.add(jPanel2);
        jPanel4.add(jPanel3);
        jFrame.add(jPanel4);
        jTextField2.setText("localhost");
        jTextField3.setText("15555");
        jFrame.setLocation(200, 200);
        jFrame.setSize(350, 200);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setVisible(true);
    }

    @Override
    /**
     * 按钮事件触发
     */
    public void actionPerformed(ActionEvent event){
        jButton1.setText("登录");
        jButton2.setText("取消");
        if("取消".equals(event.getActionCommand())) {
            System.exit(0);
        }

        if("登录".equals(event.getActionCommand())) {
            if("".equals(jTextField1.getText())) {
                JOptionPane.showMessageDialog(null,"用户名不能为空");
            }
            else {
                jFrame.setVisible(false);
                ClientUi clientUi=new ClientUi();
                clientUi.getmenu(jTextField1.getText());
                clientUi.socket();
            }
        }
    }
}
