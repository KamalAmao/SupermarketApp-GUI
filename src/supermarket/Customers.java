package supermarket;


import java.util.Objects;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Thatblackbwoy
 */
public class Customers {
   private String name;
   private int c_id;
   public Customers(String name, int c_id){
       this.name = name;
       this.c_id = c_id;
   }
   public String getName(){
       return name;
   }
   public int getc_id(){
       return c_id;
   }
   @Override
   public String toString(){
       return "Customers{"+
               "name='" + name+ '\''+
               "c_id='" +c_id+ 
               '}';
   }
   @Override
   public boolean equals(Object o){
       if(this == o) return true;
       if(o == null || getClass() != o.getClass()) return false;
       Customers customers = (Customers) o;
       return Objects.equals(name, customers.name)&& c_id == customers.c_id;
   }
   @Override
   public int hashCode(){
       return Objects.hash(name, c_id);
   }
}
