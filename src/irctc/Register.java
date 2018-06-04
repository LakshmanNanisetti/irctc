/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irctc;
import java.sql.*;
import java.util.*;
/**
 *
 * @author Administrator
 */
public class Register {
    Scanner sc;
    public Register(){
        sc = new Scanner(System.in);
    }
    public void registerUser(){
        System.out.println("________User Registration________");
        User new_user = new User();
        System.out.println("enter your name:");
        new_user.name = sc.next();
        System.out.println("enter your age:");
        new_user.age = sc.nextInt();
        System.out.println("enter your mobile number:");
        new_user.mobile = sc.next();
        System.out.println("enter your password:");
        new_user.password = sc.next();
        if(!storeUser(new_user)){
            System.out.println("Registration failed!");
        }
    }
    public boolean storeUser(User user){
        boolean flag=true;
        Statement stmt = null;
        try{
            //creating a connection
            Class.forName("com.mysql.jdbc.Driver");
            
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/irctc","root","root");
            stmt = myConn.createStatement();
            stmt.execute("insert into user "+
                    "values('"+user.name+"','"+user.mobile+"',"+user.age+",'"+user.password+"');");
//            stmt.execute("insert into user values('9444761040','lakshman','password',20);");
        }catch(SQLException e){
            flag=false;
            System.out.println("sql failure:"+e);
        }
        catch(Exception e){
            System.out.println("exception");
        }
        return flag;
    }
}
