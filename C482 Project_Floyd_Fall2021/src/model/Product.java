package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This is the Product class. */
public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /** This is the constructor for the Product class.
     @param id The ID for the product
     @param name The name for the product
     @param price The price for the product
     @param stock The stock for the product
     @param min The minimum stock level for the product
     @param max  The maximum stock level for the product */
    public Product(int id, String name, double price, int stock, int min, int max) {
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);

    }

    /** This is the setter for the ID. This method sets a specified ID to a returned value.
     @param id The product Id */
    public void setId(int id) {this.id = id;}
    /** This is the getter for the ID. This method returns a value for ID.
     @return the ID */
    public int getId() {return this.id;}

    /** This is the setter for the Name. This method sets a specified Name to a returned value.
     @param name The product name */
    public void setName(String name) {this.name = name;}
    /** This is the getter for the Name. This method returns a value for Name.
     @return the product name */
    public String getName() {return this.name;}

    /** This is the setter for the Price. This method sets a specified Price to a returned value.
     @param price The product price */
    public void setPrice(double price) {this.price = price;}
    /** This is the getter for the Price. This method returns a value for Price.
     @return The product price */
    public double getPrice() {return this.price;}

    /** This is the setter for the Stock. This method sets a specified Stock to a returned value.
     RUNTIME ERROR - I was using the term "inventory" instead of stock when calling this method. Once I referred back to this class and the actual method, I realized my naming
     conventions were causing the problem. Once I replaced "inventory" with "stock", the issue was fixed.
     @param stock The product stock level */
    public void setStock(int stock) {this.stock = stock;}
    /** This is the getter for the Stock. This method returns a value for Stock.
     @return The product stock */
    public int getStock() {return this.stock;}

    /** This is the setter for the minimum stock. This method sets a specified minimum stock to a returned value.
     @param min The minimum allowable stock */
    public void setMin(int min) {this.min = min;}
    /** This is the getter for the minimum stock. This method returns a value for minimum stock.
     @return The minimum allowable stock */
    public int getMin() {return this.min;}

    /** This is the setter for the maximum stock. This method sets a specified maximum stock to a returned value.
     @param max The maximum allowable stock*/
    public void setMax(int max) {this.max = max;}
    /** This is the getter for the maximum stock. This method returns a value for maximum stock.
     @return The maximum allowable stock */
    public int getMax() {return this.max;}

    /** This method add a part to the Associated Parts list. When this method is called, it adds a selected part to the Associated Parts list.
     @param parts The part to be added to the Associated Parts list*/
    public void addAssociatedPart(ObservableList<Part> parts) {this.associatedParts.addAll(parts);}

    /** This method removes a part from the Associated Parts list. When this method is called, the part selected for removal is located in this list and deleted.
     @param selectedAssociatedPart The part selected on the Associated Parts list for removal
     @return true */
    public boolean deleteAssociatedPart(int selectedAssociatedPart) {
        int i;
        for (i = 0; i < associatedParts.size(); i++) {
            if (associatedParts.get(i).getId() == selectedAssociatedPart)
                associatedParts.remove(i);
            }
        return true;
    }

    /** This is the getter for the Associated Parts list. When this is called, the data stored in the Associated Parts list is returned.
     @return the Associated Parts list */
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }
}