package Application_Chat;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import javax.swing.DefaultListModel;
import java.net.InetAddress;


public class ThreadReception extends Thread {
    File currentDirectory = new File(System.getProperty("user.dir"));
    public String path = currentDirectory+"\\src\\Config\\Config.config";
    
    private final MulticastSocket socketGroupe;
    private DefaultListModel<String> LMsgRecus;

    public ThreadReception(MulticastSocket s, DefaultListModel<String> l) throws IOException {
        LMsgRecus= new DefaultListModel<>();
        socketGroupe = s;
        LMsgRecus = l;
    }
    
    private volatile boolean isRunning = true;
    public void stopThread() {
            isRunning = false;
            socketGroupe.close();
        }
    
    @Override
    public void run() {
        while (isRunning) {
            try {
                System.out.println("ThreadReception is listening");
                byte[] buf = new byte[1000];
                DatagramPacket dtg = new DatagramPacket(buf, buf.length);
                
                //InetAddress groupAddress = socketGroupe.getInetAddress();
                int port = socketGroupe.getLocalPort();
                //System.out.println("ThreadReception - IP du groupe : " + groupAddress.getHostAddress());
                System.out.println(LMsgRecus);         
                
                socketGroupe.receive(dtg);
                LMsgRecus.addElement(new String(buf).trim());
                System.out.println("rcv");

            } catch (IOException e) {
                System.out.println("Erreur dans le thread :" + e.getMessage());
            }
        }
    }
}