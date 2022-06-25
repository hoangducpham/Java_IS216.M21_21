/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Hoang Duc
 */
public class User {
    private String username;
    private String password;
    private int phanQuyen;

    public User(){};
    
    public User(String username, String password, int phanQuyen) {
        this.username = username;
        this.password = password;
        this.phanQuyen = phanQuyen;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhanQuyen() {
        return phanQuyen;
    }

    public void setPhanQuyen(int phanQuyen) {
        this.phanQuyen = phanQuyen;
    }
    
}
