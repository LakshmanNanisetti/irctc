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
    public static void util(){
    }
    public void log(){
        Statement stmt=null;
        ResultSet rs=null;
        Scanner sc = new Scanner(System.in);
        User user = authenticate();
        System.out.println(user+"select\n1.book ticket\n2.view booked ticket history"
                    + "\n3.cancel ticket");
        int option;
        try{
            Class.forName("com.mysql.jdbc.Driver");
                    Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/irctc","root","root");
                    stmt = myConn.createStatement();
        }catch(Exception e){
            System.out.println("conn error!");
        }
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
                bookTicket(rs,stmt,user,sc);
                
                break;
            case 2:
                System.out.println("your tickets!");
                bookedTickets(rs,stmt,user,sc);
                break;
            case 3:
                cancelTicket(rs,stmt,user,sc);
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

    private int bookedTickets(ResultSet rs, Statement stmt, User user,Scanner sc) {
        int tick_option=-1;
        System.out.println("bookedTickets!");
        try{
            
            rs = stmt.executeQuery("select * from ticket where mobile='"+user.mobile+"';");
            System.out.println("bookedTickets try!");
            rs.beforeFirst();

            while(!rs.isLast()){
                rs.next();
                System.out.println(rs.getInt("id")+"\t"+rs.getString("train_name")+"\t"+rs.getString("f")+"\t"+rs.getString("t"));
            }
            System.out.println("ticket id:");
            tick_option=sc.nextInt();
            rs = stmt.executeQuery("select * from passenger where ticket_id="+tick_option);
            rs.beforeFirst();
            while(!rs.isLast()){
                rs.next();
                String res = (rs.getInt("id")+"\t"+rs.getString("name")+"\t"+rs.getInt("age")+"\t"+rs.getInt("bearth_no")
                +"\t"+rs.getString("category"));
                if(rs.getInt("wl")!=0){
                    res+="waiting list";
                }
                System.out.println(res);
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return tick_option;
    }

    private void bookTicket(ResultSet rs, Statement stmt, User user, Scanner sc) {
        System.out.println("enter the from station");
        String from = sc.next();
        System.out.println("enter the to station");
        String to = sc.next();
        System.out.println("enter the day(1 to 7):");
        int day = sc.nextInt();

        ArrayList<Integer> trainNos = new ArrayList<>();
        try{

            rs = stmt.executeQuery("select * from train where f='"+from+"' and t='"+to+"' and day="+day+";");
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
            rs = stmt.executeQuery("select * from train where no='"+train_option+"' and day="+day+";");
            rs.first();
            String train_name = rs.getString("name");
            System.out.println("train_name = "+train_name);
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
//                            System.out.println(category+i);
                    avl.add(i);
                }
            }
            System.out.println("No. of seats available="+avl.size()+"\n"
                    + "Enter the no. of seats to book:");
            int no = sc.nextInt();
            int ticket_id=0;
            if(no>0){
                int total_fare = no*100;

                try{
                    stmt.execute("insert into ticket (category,total_fare,f,t,mobile,train_no,wl,cancelled,day,train_name) "
                            + "values('"+category+"',"+total_fare+",'"+from+
                            "','"+to+"','"+user.mobile+"',"+train_option+",0,0,"+day+",'"+train_name+"');");
                    System.out.println("TICKET INSERTED");
                    rs = stmt.executeQuery("select * from ticket");
                    rs.last();
                    ticket_id = rs.getInt("id");
                    System.out.println("ticke id is:"+ticket_id);
                }catch(Exception e){
                    System.out.println("ticket insertion:"+e);
                }
            }
            int wl=0;
            int bNo;
            System.out.println("avl is:"+avl);
            for(int i=0;i<no;i++){

                if(avl.isEmpty()){
                    wl++;
                    bNo=wl;
                }
                else{
                    
                    bNo = avl.remove(0);
                    System.out.println("assigned-bNo="+bNo+"i="+i);
                    rs = stmt.executeQuery("select avl"+category+" from train where no="+train_option+" and day="+day+";");
                    rs.first();
                    
                    stmt.execute("update train set "+category+bNo+"=1,avl"+category+"="+(rs.getInt("avl"+category)-1)+" where no="+train_option+" and day="+day+";");
                }

                System.out.println("enter the details of the passenger no-"+(i+1));
                System.out.println("name:");
                String name = sc.next();
                System.out.println("age:");
                int age = sc.nextInt();
                System.out.println("gender:1 if male 2 if female 3 others");
                int gen=sc.nextInt();
                try{
                    stmt.execute("insert into passenger (name,age,bearth_no,ticket_id,gender,category,wl) values('"+name+"',"+age+","+bNo+","+ticket_id+","+gen+",'"+category+"',"+wl+");");
                }catch(Exception e){
                    System.out.println("passenger insertion:"+e);
                }
            }
//                    int seatNo=-1;
//                    do{ 
//                        System.out.println("enter seat nos:or -1 to exit");
//                        seatNo = sc.nextInt();
//                        if(seatNo!=-1&avl.contains(seatNo)){
//                            avl.remove(new Integer(seatNo));
//                            if(!book.contains(seatNo))
//                                {book.add(seatNo);} 
//                            stmt.execute("update train set "+category+seatNo+"=1 where no="+train_option+" and day="+day+";");
//                        }
//                    }while(seatNo!=-1);
//                    if(!book.isEmpty()){
//                        
//                        int total_fare = no*100;
//                        int ticket_id=0;
//                        try{
//                            stmt.execute("insert into ticket (category,total_fare,f,t,mobile,train_no,wl,cancelled,day) "
//                                    + "values('"+category+"',"+total_fare+",'"+from+
//                                    "','"+to+"','"+user.mobile+"',"+train_option+",0,0,"+day+");");
//                            rs = stmt.executeQuery("select * from ticket");
//                            rs.last();
//                            ticket_id = rs.getInt("id");
//                            System.out.println("ticke id is:"+ticket_id);
//                        }catch(Exception e){
//                            System.out.println("ticket insertion:"+e);
//                        }
//                        for(int i:book){
//                            System.out.println("enter the details of the passenger no-"+i);
//                            System.out.println("name:");
//                            String name = sc.next();
//                            System.out.println("age:");
//                            int age = sc.nextInt();
//                            System.out.println("gender:1 if male 2 if female 3 others");
//                            int gen=sc.nextInt();
//                            
//                            try{
//                                stmt.execute("insert into passenger (name,age,bearth_no,ticket_id,gender,category) values('"+name+"',"+age+","+i+","+ticket_id+","+gen+",'"+category+"');");
//                            }catch(Exception e){
//                                System.out.println("passenger insertion:"+e);
//                            }
//                        }
//                    }
                }catch(Exception e){
                    System.out.println("getting the train details!:"+e);
                }
    }

    private void cancelTicket(ResultSet rs, Statement stmt, User user, Scanner sc) {
        int ticket = bookedTickets(rs,stmt,user,sc);
        int train_no,day,passenger,bNo,wlNo;
        String category;
        System.out.println("enter passenger no to cancel:");
        passenger = sc.nextInt();
        try{
            rs = stmt.executeQuery("select * from ticket where id="+ticket);
            rs.first();
            train_no = rs.getInt("train_no");
            category = rs.getString("category").trim();
            day = rs.getInt("day");
            rs = stmt.executeQuery("select bearth_no,wl from passenger where ticket_id="+ticket+";");
            bNo  = rs.getInt("bearth_no");
            wlNo = rs.getInt("wl");
            if(wlNo==0){
                stmt.execute("update train set "+category+bNo+" = 0 where no="+train_no+" and day="+day+";");
            }
            rs = stmt.executeQuery("select wl from passenger where wl<>0 and train_no="+train_no+" and day="+day+";");
            rs.beforeFirst();
            boolean flag=true;
            while(!rs.last()){
                rs.next();
                if(flag){
                    stmt.execute("update train set "+category+bNo+" = 1 where no="+train_no+" and day="+day+";");
                    flag=false;
                }
                stmt.execute("update passenger set wl="+(rs.getInt("wl")-1)+" where wl<>0 and train_no="+train_no+" and day="+day+";");
            }
//            rs = stmt.executeQuery("select * from passenger where ticket_id="+ticket);
//            rs.beforeFirst();
//            ArrayList<Integer> bearthNos = new ArrayList<>();
//            while(!rs.isLast()){
//                rs.next();
////                passenger = rs.getInt("id");
//                bearthNos.add(rs.getInt("bearth_no"));
//            }
//            for(int i:bearthNos){
//                stmt.execute("update train set "+category+i+" = 0 where no="+train_no+" and day="+day+";");
//            }
//            rs = stmt.executeQuery("select bearth_no from passenger where ticket_id="+ticket+";");
//            ArrayList<Integer> can = new ArrayList<>();
//            rs.beforeFirst();
//            while(!rs.last()){
//                rs.next();
//                can.add(rs.getInt(bearth_no));
//            }
//            stmt.execute("delete from passenger where ticket_id="+ticket+";");
//            stmt.execute("update ticket set cancelled=1 where id="+ticket+";");
            
        }catch(Exception e){
            System.out.println("cancel ticket fault: "+e);
        }
    }
}
