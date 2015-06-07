/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 *
 * @author Иван
 */
public class Server {

/**
     * @param args the command line arguments
     */
   private static ArrayList<PrintWriter> streams;
   private static ArrayList<Integer> idUsers;
   private static Map<Integer,UsersData> dataUsers;
   private static  DataBase dataBase;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		go();
	}
        
       
        
        
	   private static void go() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        streams = new ArrayList<PrintWriter>();
        idUsers = new ArrayList<>();
        dataUsers = new HashMap<>();
         dataBase = new DataBase();
        
        try {
            ServerSocket ss = new ServerSocket(5000);
            while (true) {
                Socket sock = ss.accept();
                System.out.println("Got user!");
               
                Avtorization avtorization = new Avtorization(sock.getInputStream(),dataBase);
                int idUser = avtorization.getIdUsers();
                if (idUser > 0) {
                    PrintWriter writer = new PrintWriter(sock.getOutputStream());
                    dataUsers.put(idUser, new UsersData(dataBase.getPersonalData(idUser),writer));
                    streams.add(writer);
                    idUsers.add(idUser);
                    writer.write("good\n\n");
                    writer.flush();
                    updateGeneralHystory(writer);
                    writer.flush();
                    String data = updateUser();
                    tellEveryoneUser(data);
                    InputStreamReader is = new InputStreamReader(sock.getInputStream());
                    Thread t = new Thread(new Listener(sock,idUser,writer));
                    t.start();
                } else {
                }
            }
        } catch (Exception ex) {
        }

	}

	private static void tellEveryone(String msg, String login){
		msg = login+":"+msg;
		java.util.Iterator<PrintWriter> it = streams.iterator();
                msg = "message\n" + msg+ "\n\n";
                
                while(it.hasNext()){
			try{
				PrintWriter writer = it.next();
				writer.println(msg);
				writer.flush();
			}catch(Exception ex){}
		}
		
	}
	
        
        private static void tellEveryoneDeleteUser(String msg){
		
		java.util.Iterator<PrintWriter> it = streams.iterator();
                
                
                while(it.hasNext()){
			try{
				PrintWriter writer = it.next();
				writer.println(msg);
				writer.flush();
			}catch(Exception ex){}
		}
		
	}
        
        private static void tellEveryoneUser(String data){

		java.util.Iterator<PrintWriter> it = streams.iterator();

                
                while(it.hasNext()){
			try{
				PrintWriter writer = it.next();
				writer.println(data);
				writer.flush();
			}catch(Exception ex){}
		}
		
	}
        
        
        
        private static String updateUser()
        {
            String data="";
            for(Integer userEntries:idUsers)
            {
                 data+=dataUsers.get(userEntries).getLogin() + "\n";
                data+=userEntries.toString()+"\n";
               
            }
            data+="\n";
            data="updateUser\n"+data;
           return data;
        }
 
        
        
      
    
    
    
    
    
    
    
    
    
    private static void updateGeneralHystory(PrintWriter writer)
    {
        String hystory;
        hystory = dataBase.getHystoryMessage();
        String request = "Hystory\n";
        request+=hystory+"\n\n";
        writer.write(request);
    }
    
    
    
    
    private static  void conectToUser(Integer id1, Integer id2,BufferedReader reader) throws IOException
    {
        String message="";
        String response1 = "conect\n"+dataUsers.get(id2).getLogin()+"\n";
         String response2 = "conect\n"+dataUsers.get(id1).getLogin()+"\n";
        String buf ="";
        while(true)
        {
            buf = reader.readLine();
            if(buf=="") break;
            message+=buf;
        }
        
        
        message = dataUsers.get(id1).getLogin() + ":" +message;
        response1+=message+"\n\n";
        response2+=message+"\n\n";
        dataBase.saveConnectHystory(dataUsers.get(id1).getLogin(), dataUsers.get(id2).getLogin(), message);
        dataBase.saveConnectHystory(dataUsers.get(id2).getLogin(), dataUsers.get(id1).getLogin(), message);
        dataUsers.get(id1).getWriter().write(response1);
        dataUsers.get(id2).getWriter().write(response2);
        
    }
    
    
    
    
    
    
    
    
    
    
    
       private static class Listener implements Runnable {

        BufferedReader reader;
        Integer idUser;
        PrintWriter writer;

        Listener(Socket sock, int id, PrintWriter writer) {
            try {
                idUser = id;
                InputStreamReader is = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(is);
                this.writer = writer;
            } catch (Exception e) {
            }
        }

        @Override
           public void run() {
               String typeRequest;
               String data = "";
               String buf = "";
               try {
                   while (true) {
                       typeRequest = reader.readLine();
                       if (typeRequest == null) {
                           break;
                       }
                       System.out.println(typeRequest);
                       switch (typeRequest) {
                           case "tell_everyone": {
                               while (true) {
                                   buf = reader.readLine();
                                   if("".equals(buf)) break;
                                   data+=buf;
                               }
                               tellEveryone(data,dataUsers.get(idUser).getLogin());
                               String login = dataUsers.get(idUser).getLogin();
                               dataBase.setHystoryMessage(login, data);
                               buf="";
                               data="";
                               break;
                           }
                           case "conect": {
                               Integer id2 = Integer.parseInt(reader.readLine());
                               conectToUser(idUser,id2,reader);
                               break;
                           }
                           
                           case "updateHystory": {
                               
                           }
                           case "updateGeneralHystory": {
                               updateGeneralHystory(writer);
                               break;
                           }
                           case "downloadFile": {
                           }
                           case "loadFile": {
                           }
                       }
                   }
               
               } catch (Exception ex) {
            }
               String response = "deleteUser\n" + dataUsers.get(idUser).getLogin()+"\n\n";
               tellEveryoneDeleteUser(response);
               dataUsers.remove(idUser);
               idUsers.remove(idUser);
               //tellEveryoneUser(updateUser());
          
        }

    }

}
