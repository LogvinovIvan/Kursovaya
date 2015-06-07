/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Иван
 */
public class Connection {

    private static BufferedReader reader; //получает от сервера
    private static PrintWriter writer; //отправляет на сервер
    private static Controller controller;

    public Connection(String inf, Controller controller) {
        setNet(inf);
        this.controller = controller;
        Thread thread = new Thread(new Connection.Listener1());
        thread.start();
    }

    private static class Listener1 implements Runnable {

        @Override
        public void run() {
            String msg;
            String typeResponse;
            String buf;
            
            try {
                while (true) {
                    typeResponse = reader.readLine();
                    if (typeResponse == null) {
                        break;
                    }
                controller.processingResponse(typeResponse, reader);
                }
                
            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
           
    }
        

    

    public void sendInf(String inf) {
        writer.print(inf);
        writer.flush();
    }

    private static void setNet(String inf) {
        try {
            Socket sock = new Socket("127.0.0.1", 5000);
            InputStreamReader is = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(is);
            writer = new PrintWriter(sock.getOutputStream());
            writer.println(inf);
            writer.flush();
        } catch (Exception ex) {
        }

    }
}
