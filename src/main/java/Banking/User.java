package Banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

   private Connection connection;
   private Scanner scanner;

    User(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }



    public void register(){
        scanner.nextLine();
        System.out.println("Full Name");
        String full_name=scanner.nextLine();
        System.out.println("Email");
        String email=scanner.nextLine();
        System.out.println("Password");
        String password=scanner.nextLine();
        if(user_exits(email)){
            System.out.println("User already exists for this email address");
            System.out.println();
            return;
        }
        String register_query="INSERT INTO user(full_name,email,password)VALUES(?,?,?)";

        try {
            PreparedStatement preparedStatement=connection.prepareStatement(register_query);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int rowaffected= preparedStatement.executeUpdate();
            if (rowaffected>0){
                System.out.println("Register succesfull");
            }else System.out.println("Register failed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String login(){
        scanner.nextLine();
        System.out.println("Email");
        String email= scanner.nextLine();
        System.out.println("Password");
        String password= scanner.nextLine();
        String login_query="SELECT * FROM user WHERE email=? AND password=?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(login_query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet st=preparedStatement.executeQuery();
            if(st.next()){
                return email;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean user_exits(String email){
        String query="SELECT * FROM user WHERE email=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet st=preparedStatement.executeQuery();
            if (st.next()){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
