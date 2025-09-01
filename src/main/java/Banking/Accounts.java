package Banking;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner scanner;

    Accounts(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    public long open_account(String email){
        if(!account_exist(email)){
            String account_query="INSERT INTO accounts(account_number,full_name,email,balance,security_pin) VALUES(?,?,?,?,?)";
            scanner.nextLine();
            System.out.println("Enter your full name");
            String full_name=scanner.nextLine();
            System.out.println("Enter Intial amount");
            double balance=scanner.nextDouble();
            scanner.nextLine();
            System.out.println("enter Security pin");
            String pin =scanner.nextLine();
            try{
                long account_number=generateAccountNumber();
                PreparedStatement preparedStatement=connection.prepareStatement(account_query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,full_name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,pin);
                int RowsAffected=preparedStatement.executeUpdate();
                if (RowsAffected>0){
                    return account_number;
                }else throw new RuntimeException("Account creation failed");
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account already exist");
    }

    public long getAccountNumber(String email){
        String query="SELECT account_number FROM accounts WHERE email=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet st=preparedStatement.executeQuery();
            if (st.next()){
                return st.getLong("account_number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account number does not exists");
    }

    public long generateAccountNumber(){
        try{
            Statement statement=connection.createStatement();
            String query="SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1";
            ResultSet resultSet=statement.executeQuery(query);
            if (resultSet.next()){
                long last_account_number=resultSet.getLong("account_number");
                return last_account_number+1;
            }else return 10000100;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }

    public boolean account_exist(String email){
        String query="SELECT account_number FROM accounts WHERE email=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet st=preparedStatement.executeQuery();
            if (st.next()){
                return true;
            }else return false;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
