package pl.milosz.chatip;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static BufferedReader reader;
    private static InputStreamReader inputStreamReader;
    private static String message ="";


    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    private static ServerSocket serverSend;
    private static Socket socketSend;
    private static PrintWriter writer;
    private static String ip = "192.168.0.52";
    public static void main(String[] args) {
        JFrame f=new JFrame();
        JTextArea ta= new JTextArea();
        ta.setBounds(300,300,300,300);
        ta.setText("wiadomości");
        final JTextField t=new JTextField();
        t.setBounds(100,100,300,100);
        t.setText("Wpisz wiadomość");
        JButton b=new JButton("wyślij wiadomość");
        b.setBounds(130,100,100, 40);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    //serverSend = new ServerSocket(6000);
                    socketSend = new Socket("192.168.0.80",6000);
                    writer = new PrintWriter(socketSend.getOutputStream());
                    writer.write(t.getText().trim());
                    writer.flush();
                    writer.close();
                    socketSend.close();
                    System.out.println("wyslane");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(ta);
        f.add(t);
        f.add(b);
        f.setSize(500,700);
        f.setLayout(new GridLayout(3,1));
        f.setVisible(true);
        try {
            while(true) {
                serverSocket = new ServerSocket(4200);
                socket = serverSocket.accept();
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(inputStreamReader);
                message += reader.readLine();
                message += "\n";
                System.out.println(message);
                ta.setText(message);
                inputStreamReader.close();
                reader.close();
                socket.close();
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
