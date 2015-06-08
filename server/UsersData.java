/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author Иван
 */
public class UsersData {

    private final String name;
    private final String surname;
    private final String login;
    private final String date;
    private final String hobby;
    private final PrintWriter writer;

    public UsersData(List<String> personalData, PrintWriter writer) {
        name = personalData.get(0);
        surname = personalData.get(1);
        login = personalData.get(2);
        date = personalData.get(3);
        hobby = personalData.get(4);
        this.writer = writer;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLogin() {
        return login;
    }

    public String getData() {
        return date;
    }

    public String getHobby() {
        return hobby;
    }

    public PrintWriter getWriter() {
        return writer;
    }
}
