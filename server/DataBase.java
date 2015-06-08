/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import com.mysql.jdbc.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Иван
 */
public class DataBase {
    Connection connection;
    Statement statement;
    ResultSet resultSet;

    public DataBase() {
        try {
            String URL = "jdbc:mysql://localhost:3306/messenger";
            String login = "root";
            String pass = "";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(URL, login, pass);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public int checkRegistration(String login) {
      Integer id = 0;
        try {
            String request = "SELECT * FROM `personaldata` WHERE `login` LIKE '"+login+"'";
            resultSet = (ResultSet) statement.executeQuery(request);
            resultSet.next();
            id= resultSet.getInt("id");
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
        return  id;
        }  
    }
    
    
    
    
    
    public int getIdUsers(String login, String password) {
        Integer id = -2;
        try {
            String request = "SELECT * FROM `personaldata` WHERE `login` LIKE '"+login+"' AND `password` LIKE '"+password+"'";
            resultSet = (ResultSet) statement.executeQuery(request);
            resultSet.next();
            id= resultSet.getInt("id");
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
        return  id;
        }
    }
    
    public void setPersonalInformation(List<String> personalData)
    {
        String name = personalData.get(0);
        String surname = personalData.get(1);
        String login = personalData.get(2);
        String password = personalData.get(3);
        String date = personalData.get(4);
        String hobby = personalData.get(5);
        try {
            statement.executeUpdate("INSERT INTO `messenger`.`personaldata` (`id`, `login`, `name`, `surname`, `password`, `date`, `hobby`) VALUES (NULL, '"+login+"', '"+name+"', '"+surname+"', '"+password+"', '"+date+"', '"+hobby+"')");
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setHystoryMessage(String login, String message) {
        try {
            String SQL = "INSERT INTO `messenger`.`chat1` (`numb`, `login`, `msg`) VALUES (NULL, '"+login+"', '"+message+"')";
            System.out.print(SQL);
            statement.executeUpdate(SQL);
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void saveConnectHystory(String login1, String login2, String message)
    {
        try {
            String SQL = "INSERT INTO `messenger`.`chat2` (`numb`, `login1`, `login2`, `msg`) VALUES (NULL, '"+login1+"', '"+login2+"', '"+message+"')";
            System.out.print(SQL);
            statement.executeUpdate(SQL);
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public String getHystoryMessage() {
        String data = "";
        try {
            resultSet = (ResultSet) statement.executeQuery("SELECT * FROM `chat1`");
            resultSet.next();
            while(resultSet.next()==true)
            {data += resultSet.getString("login")+":" + resultSet.getString("msg") + "\n";}
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    public String getConnetHystory(String login1, String login2) {
        String hystory = "";
        try {
            resultSet = (ResultSet) statement.executeQuery("SELECT * FROM `chat2` WHERE `login1` LIKE '"+login1+"' AND `login2` LIKE '"+login2+"'");
            resultSet.next();
            hystory += resultSet.getString("msg") + "\n";
            while(resultSet.next()==true)
            {hystory += resultSet.getString("msg") + "\n";}
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    return hystory;
    }

    public List<String> getPersonalData(Integer id) {
        try {
            List<String> data = new ArrayList<>();
            resultSet = (ResultSet) statement.executeQuery("SELECT * FROM `personaldata` WHERE `id` = "+id.toString());
            resultSet.next();
            data.add(resultSet.getString("name"));
            data.add(resultSet.getString("surname"));
            data.add(resultSet.getString("login"));
            data.add(resultSet.getString("date"));
            data.add(resultSet.getString("hobby"));
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }



}
