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
public class Login {
    public Login(){
        
    }
    public void log(){
        Scanner sc = new Scanner(System.in);
        User user = authenticate();
        System.out.println(user+"select\n1.book ticket\n2.view booked ticket history"
                    + "\n3.exit");
        int option;
        while(true){
            try{
                option = sc.nextInt();
                break;
            }catch(Exception e){
                System.out.println("Please enter a valid input");
                sc.next();
            }
        }
        switch(option){
            case 1:
                System.out.println("enter the from station");
                String from = sc.next();
                System.out.println("enter the to station");
                String to = sc.next();
                ResultSet rs;
                Statement stmt=null;
                ArrayList<Integer> trainNos = new ArrayList<>();
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/irctc","root","root");
                    stmt = myConn.createStatement();
                    rs = stmt.executeQuery("select * from train where f='"+from+"' and t='"+to+"';");
                    rs.beforeFirst();
                    while(rs.next()){
                        System.out.println(rs.getString("no")+"\t"+rs.getString("name")+"\t"+rs.getString("f")+"\t"+rs.getString("t"));
                        trainNos.add(rs.getInt("no"));
                    }
                }catch(Exception e){
                    System.out.println("exception at fetching trains:"+e);
                }
                System.out.println("enter the number of the train that you want to book:");
                int train_option;
                while(true){
                    try{
                        train_option=sc.nextInt();
                        if(!trainNos.contains(train_option)){
                            throw new Exception();
                        }
                        break;
                    }catch(Exception e){
                        System.out.println("please enter a valid input:");
                    }
                }
                try{
                    rs = stmt.executeQuery("select * from train where no='"+train_option+"';");
                    rs.first();
                    System.out.println("select an option:\n1.ac\n2.sleeper");
                    int cat = sc.nextInt();
                    String category;
                    if(cat==1){
                        category="ac";
                    }
                    else{
                        category="sl";
                    }
                    ArrayList<Integer> avl = new ArrayList<Integer>();
                    ArrayList<Integer> book = new ArrayList<Integer>();
                    for(int i=1;i<=5;i++){
                        if(rs.getString(category+i).equals("0")){
                            System.out.println(category+i);
                            avl.add(i);
                        }
                    }
                    int seatNo=-1;
                    do{
                        System.out.println("enter seat nos:or -1 to exit");
                        seatNo = sc.nextInt();
                        if(seatNo!=-1&avl.contains(seatNo)){
                            avl.remove(new Integer(seatNo));
                            book.add(seatNo);
                            stmt.execute("update train set "+category+seatNo+"=1 where no="+train_option);
                        }
                    }while(seatNo!=-1);
                    if(!book.isEmpty()){
                        int total_fare = book.size()*100;
                        int ticket_id=0;
                        try{
                            stmt.execute("insert into ticket values('"+category+","+total_fare+",'"+from+
                                    "','"+to+"','"+user.mobile+"',"+train_option+",0,0);");
                            rs = stmt.executeQuery("select * from ticket");
                            rs.last();
                            ticket_id = rs.getInt("id");
                        }catch(Exception e){
                            System.out.println("ticket insertion:"+e);
                        }
                        for(int i:book){
                            System.out.println("enter the details of the passenger in bearth no-"+i);
                            System.out.println("name:");
                            String name = sc.next();
                            System.out.println("age:");
                            int age = sc.nextInt();
                            System.out.println("gender:1 if male 2 if female 3 others");
                            int gen=sc.nextInt();
                            
                            try{
                                stmt.executeQuery("insert into passenger values('"+name+"',"+age+","+seatNo+","+ticket_id+","+gen+",'"+category+"');");
                            }catch(Exception e){
                                System.out.println("passenger insertion:"+e);
                            }
                        }
                    }
                }catch(Exception e){
                    System.out.println("getting the train details!");
                }
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                System.out.println("number must be between 1 and 3");
        }
    }
    public User authenticate(){
        User user=null;
        Statement stmt = null;
        ResultSet rs;
        String mobile,password;
        Scanner sc = new Scanner(System.in);
        while(user==null){
            System.out.println("enter your mobile:");
            mobile=sc.next();
            System.out.println("enter your password:");
            password = sc.next();
            try{
                //creating a connection
                Class.forName("com.mysql.jdbc.Driver");
                Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/irctc","root","root");
                stmt = myConn.createStatement();
                rs = stmt.executeQuery("select * from user where mobile='"+mobile+"';");
                rs.beforeFirst();
                while(!rs.isAfterLast()){
                    rs.next();
                    String mob = rs.getString("mobile");
                    mob = mob.trim();
                    String pass = rs.getString("password");
                    pass = pass.trim();
                    if(mob.equals(mobile)&&pass.equals(password)){
                        System.out.println("Successful login");
                        user = new User(rs.getInt("age"),rs.getString("name").trim(),rs.getString("password").trim(),rs.getString("mobile").trim());
                        break;
                    }
                }
             }catch(SQLException e){
                System.out.println("sql failure:"+e);
            }
            catch(Exception e){
                System.out.println("exception");
            }
        }   
        return user;
    }
}
