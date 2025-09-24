/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author artthiramesh
 */
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Books {


    private final double price;
    private final String title;
    private final String author;
    private BooleanProperty selected = new SimpleBooleanProperty(false);
    
   public Books(String bookTitle, String authorOfBook, double priceOfBooks){
       
       this.price = priceOfBooks;
       this.title = bookTitle;
       this.author = authorOfBook;
     //  this.selectedBooks = new CheckBox();
   }
   
   public String getTitle(){
       return title;
   }
   
   public String getAuthor(){
       return author;
   }
   
   public double getPrice(){
       return price;
   }
   
   public BooleanProperty selectedProperty(){
       return selected;
   }
   
   public boolean isSelectedBooks(){
       return selected.get();
   }
   
   public void setSelectedBooks(boolean selected){
       this.selected.set(selected);
   }

}
