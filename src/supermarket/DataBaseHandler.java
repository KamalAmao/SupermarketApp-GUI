package supermarket;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Thatblackbwoy
 */
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataBaseHandler {
    PreparedStatement pst;
    Connection con;
    public void loadDriver(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("load driver successfully");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public void connect(){
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket", "root", "");
            System.out.println("connected");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
