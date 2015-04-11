/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.text.html.HTMLDocument.Iterator;
/**
 *
 * @author Иван
 */
public class Server {

    /**
     * @param args the command line arguments
     */
   private static ArrayList<PrintWriter> streams;

	public static void main(String[] args) {
		go();
	}

	private static void go() {
		streams = new ArrayList<PrintWriter>();
		try {
			ServerSocket ss = new ServerSocket(5000);
			while (true) {
				Socket sock = ss.accept();
				System.out.println("Got user!");
				PrintWriter writer = new PrintWriter(sock.getOutputStream());
				streams.add(writer);
				
				Thread t = new Thread(new Listener(sock));
				t.start();
			}
		} catch (Exception ex) {
		}

	}

	private static void tellEveryone(String msg){
		try{
			int x = msg.indexOf(':');
			String login = msg.substring(0, x);
		}catch(Exception ex){}
		
		java.util.Iterator<PrintWriter> it = streams.iterator();
		while(it.hasNext()){
			try{
				PrintWriter writer = it.next();
				writer.println(msg);
				writer.flush();
			}catch(Exception ex){}
		}
		
	}
	
	private static class Listener implements Runnable {

		BufferedReader reader;
		Listener(Socket sock) {
			try {
				InputStreamReader is = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(is);
			} catch (Exception e) {
			}
		}

		@Override
		public void run() {
			String msg;
			try {
				while ((msg = reader.readLine()) != null) {
					System.out.println(msg);
					tellEveryone(msg);
				}
			} catch (Exception ex) {
			}
		}

	}
    
}
