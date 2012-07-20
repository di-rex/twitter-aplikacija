/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Dusan
 */
public class Server {

    static ServerThread[] clients = new ServerThread[50];

    public static void main(String[] args) {
        
        int port = 5000;
        Socket clientSocket = null;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                clientSocket = serverSocket.accept();
                for (int i = 0; i < clients.length; i++) {
                    if (clients[i] == null) {
                        clients[i] = new ServerThread(clientSocket);
                        clients[i].start();
                        break;
                    }                   
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }

    }
}
