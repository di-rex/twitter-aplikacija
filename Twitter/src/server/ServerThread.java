/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dusan
 */
public class ServerThread extends Thread {

    Socket socket = null;
    String username;
    DataInputStream is;
    DataOutputStream os;

    public ServerThread(Socket socket) {
        try {
            this.socket = socket;
            os = new DataOutputStream(socket.getOutputStream());
            is = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ServerThread() {
    }

    public boolean logIn(String message) {
        try {
            Scanner in = new Scanner(new File("users.txt"));

            while (in.hasNextLine()) {
                String p = in.nextLine();
                if (p.equals(message)) {
                    username = message.split(" ")[0];
                    return true;
                }

            }
            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public String tryLogin(String poruka) {
        String m = poruka.substring(3);
        if (logIn(m)) {
            return ("LOG.OK");
        } else {
            return ("LOG.ER");
        }
    }

    public String tryRegister(String poruka) throws IOException {
        String m = poruka.substring(3);
        if (ragistered(m) == false) {
            String fajl = "";
            BufferedReader r = new BufferedReader(new FileReader("users.txt"));
            String s = r.readLine();
            while (s != null) {
                fajl += s + "\n";
                s = r.readLine();
            }


            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("users.txt")));
            out.println(fajl + "\n" + m);
            out.close();

            return ("REG.OK");

        } else {
            return ("REG.ER");
        }
    }

    public String postujTweet(String poruka) throws IOException {

        String m = poruka.substring(3);
        if (m.length() <= 140) {
            String fajl = readFile("tweets.txt");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("tweets.txt")));            
            out.println(fajl + "\n" + m);
            out.close();
            return ("TWP.OK");
        } else {
            return ("TWP.ER");
        }
    }

    public String postujDM(String poruka) throws IOException {
        {
            String m = poruka.substring(3);
            if (m.length() <= 140) {
                String fajl = readFile("dm.txt");
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("dm.txt")));
                out.println(fajl + "\n" + m);
                out.close();
                return ("DMP.OK");
            } else {
                return ("DMP.ER");
            }
        }
    }

    public String obradi(String poruka) throws IOException {
        if (poruka.startsWith("LOG")) {
            return tryLogin(poruka);
        }
        if (poruka.startsWith("REG")) {
            return tryRegister(poruka);
        }
        if (poruka.startsWith("PTW")) {
            return postujTweet(poruka);
        }
        if (poruka.startsWith("PDM")) {
            return postujDM(poruka);
        }
        if (poruka.startsWith("GTW")) {
            return vratiTweet();
        }
        if (poruka.startsWith("GDM")) {
            return vratiDM();
        }
        return "ERROR!!";
    }

    @Override
    public void run() {
        String poruka;
        try {
            while (true) {

                poruka = is.readUTF();
                System.out.println("poruka " + poruka);
                String s = obradi(poruka);
                os.writeUTF(s);
                os.flush();
            }
        } catch (Exception e) {
        }

    }

    private boolean ragistered(String message) {
        try {
            Scanner in = new Scanner(new File("users.txt"));

            while (in.hasNextLine()) {
                String p = in.nextLine();
                if (p.equals(message)) {
                    in.close();
                    return true;
                }
            }
            in.close();
        } catch (FileNotFoundException ex) {
        }
        return false;

    }

    private String vratiTweet() throws FileNotFoundException {

        String p = "TWL";
        return p + readFile("tweets.txt");

    }
    
    private String readFile(String filename) throws FileNotFoundException {
        String s = "";
        Scanner in = new Scanner(new File(filename));
        while (in.hasNextLine()) {
            s += "\n" + in.nextLine();
        }
        return s.substring(1);    
    }

    private String vratiDM() throws FileNotFoundException {
        String s = "DML";
        String temp = "";
        Scanner in = new Scanner(new File("dm.txt"));
        while (in.hasNextLine()) {
            temp = in.nextLine();
            if(temp.startsWith(username)) s += temp.substring(username.length()+1) + "\n";
        }
        return s;    
    }
    
    
    
}
