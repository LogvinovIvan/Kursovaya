/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import views.Views;

/**
 *
 * @author Иван
 */
public class Controller {
private Connection connection;
private Views views;


    public String createRequest(String typeRequest, String data) {
        String request = typeRequest + "\n";
        request += data + "\n";
        return request;
    }

    public void createMainPage() {
        //формирую запрос на обновление истории  общий сообщений на свои данные и кто онлайн 
    }

    public void processingResponse(String typeResponse, String data) {
        switch (typeResponse) {
            case "updateHystory": {
                updateHystory(data);
                break;
            }
            case "updateUsers": {
                updateUsers(data);
                break;
            }
            case "downloadFile": {
                downoloadFile(data);
                break;
            }
        }
    }

    public void updateHystory(String data) {

    }

    public void updateUsers(String data) {

    }

    public void downoloadFile(String data) {

    }

    public void sendRequest(String typeRequest, String data) {
        String request = createRequest(typeRequest, data);
        
    }
}
