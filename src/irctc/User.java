/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irctc;

/**
 *
 * @author Administrator
 */
public class User {
    int age;
    String name,password,mobile;

    public User(int age, String name, String password, String mobile) {
        this.age = age;
        this.name = name;
        this.password = password;
        this.mobile = mobile;
    }

    public User() {
    }
    public String toString(){
        return (name+"-"+mobile);
    }
}
