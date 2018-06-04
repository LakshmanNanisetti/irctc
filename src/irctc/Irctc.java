/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irctc;
import java.util.*;
/**
 *
 * @author Administrator
 */
public class Irctc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        System.out.println("1.Login\n2.Register\n3.Exit");
        int login_option;
        while(true){
            try{
                login_option = sc.nextInt();
                break;
            }
            catch(Exception e){
                System.out.println("Please enter a valid input!");
            }
        }
        switch(login_option){
            case 1:
                System.out.println("______Login_______");
                Login l = new Login();
                l.log();
                break;
            case 2:
                //user registration
                Register r = new Register();
                r.registerUser();
                break;
            case 3:
                break;
            default:
        }
    }

    
    
}
