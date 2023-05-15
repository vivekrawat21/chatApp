import java.awt.Font;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Client extends JFrame {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //COmponent Declaration
    private JLabel heading= new JLabel("Client Area");
    private JTextArea messageArea= new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font = new Font("Roboto",Font.ITALIC,20);
    private Font fkkt = new Font("Roboto",Font.PLAIN,25);

    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub


            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // System.out.println("YOu released enter");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }

            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub


            }



        });
    }

    private void createGUI(){

        this.setTitle("Client Message[END]");
        this.setSize(700,700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        //coding for componentss..
        heading.setFont(fkkt);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setOpaque(true);
        Color whatsappGreen = new Color(0,132,255);

        heading.setBackground(whatsappGreen);
        heading.setForeground(Color.WHITE);



        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,25,20));
        ImageIcon icon = new ImageIcon("messenger.png");
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        heading.setIcon(scaledIcon);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        messageArea.setEditable(false);
        //Setting up layout
        this.setLayout(new BorderLayout());
        //Adding the componets
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane= new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);

        this.add(messageInput,BorderLayout.SOUTH);


        this.setVisible(true);

    }
    public Client(){
        try {


            System.out.println("Sending Request to server...");
            socket=new Socket(InetAddress.getLocalHost(),7777);
            System.out.println("Connection done");
            br =new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out= new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
//              startWriting();

        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    public void startReading() {
        //thread-read krke deta rhega
        Runnable r1= ()->{
            System.out.println("Reader started...");
            try{
                while(true){
                    String msg;

                    msg = br.readLine();
                    if(msg.equals("exit"))
                    {
                        System.out.println("Server Terminated the chat..");
                        JOptionPane.showMessageDialog(this, "Server Terminated the Chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //   System.out.println("Server : "+ msg);
                    messageArea.append("Server : "+ msg+"\n");

                }
            }catch(Exception e){

                System.out.println("Connection closed..");
            }
        };
        new Thread(r1).start();
    }
    public void startWriting(){
        //data user se lega aur server ko send karega
        Runnable r2=()->{
            System.out.println("Writer Started...");
            try{
                while(!socket.isClosed()){


                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }

                }
            }catch(Exception e){
                System.out.println("Connection closed..");
            }
        };
        new Thread(r2).start();}
    public static void main(String[] args) {
        System.out.println("This is client");
        new Client();
    }
}
