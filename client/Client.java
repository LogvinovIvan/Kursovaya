/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
public class Client {

        private static JTextArea textArea;
	private static JTextField textField;
	private static BufferedReader reader; //получает от сервера
	private static PrintWriter writer; //отправляет на сервер
	private static String login;
	
	public static void main(String[] args) {
		go();
	}

	private static void go() {
		
		//ImageIcon img = new ImageIcon("src/logo.png");
		//JOptionPane.showMessageDialog(null, img, "DayMessenger", -1);
		
		login = JOptionPane.showInputDialog("Введите логин");
		
		JFrame frame = new JFrame("Messenger 1.0");
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		JPanel p = new JPanel();
		textArea = new JTextArea(15, 30);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPanel = new JScrollPane(textArea);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textField = new JTextField(20);
		
                JButton buttonSend = new JButton("Отправить");
		
		
		
		
		buttonSend.addActionListener(new Send());
		
		p.add(scrollPanel);
		p.add(buttonSend);
		p.add(textField);
		setNet();
		
		Thread thread = new Thread(new Listener());
		thread.start();
		
		
		frame.getContentPane().add(BorderLayout.CENTER, p);
		
                frame.setSize(400, 340);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	private static class Listener implements Runnable{

		@Override
		public void run() {
			String msg;
			try{
				while((msg=reader.readLine())!=null){
					textArea.append(msg+"\n");
				}
			}catch(Exception ex){}
		}
		
	}
	
	private static class Send implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
                    
			String msg = login+": "+textField.getText();
			writer.println(msg);
			writer.print("");
                        writer.flush();
			
			textField.setText("");
			textField.requestFocus();
			
		}
		
	}

	private static void setNet() {
		try {
			Socket sock = new Socket("127.0.0.1", 5000);
			InputStreamReader is = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(is);
			writer = new PrintWriter(sock.getOutputStream());
                        writer.println("Get user "+login);
                    writer.flush();
                } catch (Exception ex){}
		
	}
    
}
