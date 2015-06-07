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
import java.util.HashMap;
import java.util.Map;
import views.Views;

/**
 *
 * @author Иван
 */
public  class Controller {
private Connection connection;
private Views views;
private Registration registration;
private Authorization authorization;
private ClientPage clientPage;
private Map<String,Integer> userOnline;

    public String createRequest(String typeRequest, String data) {
        String request = typeRequest + "\n";
        request += data + "\n";
        return request;
    }

    public void createMainPage() {
        //формирую запрос на обновление истории  общий сообщений на свои данные и кто онлайн 
    }

    public void processingResponse(String typeResponse, BufferedReader reader) throws IOException {
        switch (typeResponse) {
            
            case "good":{
                startClientPage();
                break;
            }
            case "updateUser":{
                updateUsersOnline(reader);
                break;
            }
            case "message":{
                addGeneralMessage(reader);
                break;
            }
            case "Hystory": {
                updateHystory(reader);
                break;
            }
            case "updateUsers": {
                updateUsers(reader);
                break;
            }
            case "deleteUser":{
                deleteOnlineUser(reader);
                break;
            }
            case "downloadFile": {
                downoloadFile(reader);
                break;
            }
        }
    }

    
    public  void startClientPage()
    {
        authorization.setVisible(false);
        registration.setVisible(false);
        clientPage = new ClientPage(this);
        clientPage.setVisible(true);
        
    }
    
    
    private void addGeneralMessage(BufferedReader reader) throws IOException
    {
        String message = "";
        String buf = "";
        while(true)
        {
            buf = reader.readLine();
            if("".equals(buf)) break;;
            message+=buf+"\n";
        }
        clientPage.addText(message);
    }
    
    private void deleteOnlineUser(BufferedReader reader) throws IOException
    {
        String login;
        login = reader.readLine();
        userOnline.remove(login);
        clientPage.addUserList(userOnline);
    }
    
    
    public void updateUsersOnline(BufferedReader reader) throws IOException
    {
        String login;
        Integer id;
        
        while(true)
        {
            login = reader.readLine();
            if("".equals(login)) break;
            id = Integer.parseInt(reader.readLine());
            userOnline.put(login,id);
        }
        clientPage.addUserList(userOnline);
    }
    
    
    public void updateHystory(BufferedReader reader) throws IOException {
        
        String hystory = "";
        String buf;
        while(true)
        {
            buf = reader.readLine();
            if("".equals(buf)) break;
            hystory+=buf+"\n";
        }
        clientPage.setTextArea(hystory);
    }

    public void updateUsers(BufferedReader reader) {

    }

    public void downoloadFile(BufferedReader reader) {

    }

    public void sendRequest(String typeRequest, String data) {
        String request = createRequest(typeRequest, data);
        
    }
    
    public void sendMessage(String message)
    {
        connection.sendInf(message);
    }
    
    public void avtorization(String inf)
    {
         connection = new Connection(inf,this);
         
    }
    
    
    public void start(){
    authorization = new Authorization(this);
    registration = new Registration(this);
    new Input(registration,authorization).setVisible(true);
    userOnline = new HashMap<>();
    
    
    }
    
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.start();
       
    }


    


}
