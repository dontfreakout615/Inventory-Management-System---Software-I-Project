package model;

/** This class defines the In-House Part object, which inherits from the Part class. */
public class OutsourcedPart extends Part{

    private String companyName;

    /** This is the constructor for the Outsourced parts.
     RUNTIME ERROR - I copy-pasted this constructor from the InHousePart class. I neglected to change the machineId to a String called companyName. Once I realized I
     hadn't changed that parameter, I changed it accordingly and everything worked.
     @param id The product ID
     @param name The name of the product
     @param price The price of the product
     @param stock The inventory level of the product
     @param min The inventory minimum of the product
     @param max The inventory maximum of the product
     @param companyName The name of the company of the product */
    public OutsourcedPart(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);

        this.companyName = companyName;


    }

    /** This is the setter for the company name. This method sets the value returned in the companyName to the companyName.
     @param companyName this variable stores the returned companyName. */
    public void setCompanyName(String companyName) {this.companyName = companyName;}

    /** This is the getter for the company name. When called, this method returns the value stored for the company name.
     @return companyName returns the variable that holds the companyName. */
    public String getCompanyName() {return companyName;}
}
