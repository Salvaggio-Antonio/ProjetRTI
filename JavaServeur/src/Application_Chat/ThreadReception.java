package Application_Chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import javax.swing.DefaultListModel;

public class ThreadReception extends Thread {

    private final String nom;
    private final MulticastSocket socketGroupe;
    private DefaultListModel<String> LMsgRecus;

    public ThreadReception(String n, MulticastSocket ms, DefaultListModel<String> l) {
        nom = n;
        socketGroupe = ms;
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
                byte[] buf = new byte[1000];
                DatagramPacket dtg = new DatagramPacket(buf, buf.length);
                socketGroupe.receive(dtg);
                LMsgRecus.addElement(new String(buf).trim());
            } catch (IOException e) {
                System.out.println("Erreur dans le thread :" + e.getMessage());
            }
        }
    }
}