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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Иван
 */
public class Avtorization {
private final InputStream inputStream;
private final DataBase db;
private final List<String> data;

    public Avtorization(InputStream inputStream, DataBase db) {
        this.inputStream = inputStream;
        this.db = db;
        data = new ArrayList<>();
    }
    

     private Integer autefication()
        {
            String login = data.get(0);
            String password = data.get(1);
            Integer id = db.getIdUsers(login, password);
            return id;
        }
        
        
        private  Integer registration()
        {
            String login = data.get(2);
            String password  = data.get(3);
            Integer id1 = db.checkRegistration(login);
            if(id1>0) return -1;
            db.setPersonalInformation(data);
            Integer id  = db.getIdUsers(login, password);
            return id;
        }
    
    
    
    
    public int getIdUsers()
    {
    try {
        String typeRequest;
        String bufData;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        typeRequest = reader.readLine();
        while(true)
        {
            bufData = reader.readLine();
            if(bufData.isEmpty()) break;
            data.add(bufData);
        }
        
        switch(typeRequest){
            case "registration": {
                return registration();
            }
            
            case "autefication":{
                return autefication();
            }
            default: return 0;
        }
    } catch (IOException ex) {
        Logger.getLogger(Avtorization.class.getName()).log(Level.SEVERE, null, ex);
    }
    return 0;
    }




}
