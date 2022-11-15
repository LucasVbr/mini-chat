package serveur;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.out.println("Could not listen on port 4444");
            System.exit(-1);
        }

        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.out.println("Accept failed on port 4444");
            System.exit(-1);
        }

        System.out.println("Client connecté");
        boolean quitte = false;
        boolean reception = true;

        DataOutputStream envoi = null;
        BufferedReader recevoir = null;

        try {
            OutputStream outputStream = clientSocket.getOutputStream();
            envoi = new DataOutputStream(outputStream);
            recevoir = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (!quitte) {
                if (reception) {
                    String message = null;
                    while (message == null){
                        message = recevoir.readLine();
                    }
                    while(message != null){
                        System.out.println(message);
                        message= recevoir.readLine();
                    }

                }
                reception = !reception;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
