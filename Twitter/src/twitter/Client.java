/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


/**
 *
 * @author Dusan
 */
public class Client extends Thread {

    public static Socket clientSocket;
    static String username;
    static LogIn loginScreen;
    static Home home;
    private static DataInputStream is = null;
    private static DataOutputStream os = null;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Client.username = username;
    }

    public static Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());
        //BufferedReader is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        Client c = null;
        try {
            c = new Client(new Socket("localhost",5000));
        } catch (Exception e) {}
        
        c.start();
        


        loginScreen = new LogIn();
        loginScreen.setVisible(true);
    }
    
    public Client(Socket s) {
        clientSocket = s;
        try{
        is = new DataInputStream(s.getInputStream());
        os = new DataOutputStream(s.getOutputStream());
        }catch (Exception e) {}
        
    }

    @Override
    public void run() {
        System.out.println("q");
            while (true) {              
                   System.out.println("start");                   
                   String s = "";
            try {
                s = is.readUTF();
                obradi(s);
            } catch (IOException ex) {}                   
              
            }
    }

    public void obradi(String poruka) {
        System.out.println("poruka   '"+poruka+"'");
        if (poruka.equals("LOG.OK")) {loginScreen.logSuccesfull(true); return;}
        if (poruka.equals("LOG.ER")) {loginScreen.logSuccesfull(false); return;}
        if (poruka.equals("REG.OK")) {loginScreen.regSuccesfull(true); return;}
        if (poruka.equals("REG.ER")) {loginScreen.regSuccesfull(false); return;}
        if (poruka.equals("TWP.OK")) {home.twpSuccesfull(true); return;}
        if (poruka.equals("TWP.ER")) {home.twpSuccesfull(false); return;}
        if (poruka.equals("DMP.OK")) {home.dmpSuccesfull(true); return;}
        if (poruka.equals("DMP.ER")) {home.dmpSuccesfull(false); return;}        
        if (poruka.startsWith("TWL")) {home.displayTweet(poruka); return;}
        if (poruka.startsWith("DML")) {home.displayDM(poruka); return;}
        
    }

    public static void sendMessage(String poruka) {
        try {
            os.writeUTF(poruka);
        } catch (Exception e) {
            System.out.println("err");
        }

    }
}
