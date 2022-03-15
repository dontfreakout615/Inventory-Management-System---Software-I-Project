package model;

/** This class defines the In-House Part object, which inherits from the Part class. */
public class InHousePart extends Part{

    private int machineId;

    /** This is the constructor for In-House Parts.
     LOGICAL ERROR - the machineId field was not populating on the Modify Part page. I had not set the returned machineId value to the machineId variable. Once I did that,
     the field populated correctly.
     @param id The ID value
     @param name The name string
     @param price The price of the part
     @param stock The number of parts in Inventory
     @param min The minimum stock allowed
     @param max the maximum stock allowed
     @param machineId The machine ID */
    public InHousePart( int id, String name, double price, int stock, int min, int max, int machineId){
        super(id, name, price, stock, min, max);

        this.machineId = machineId;


    }

    /** This is the getter for the Machine ID. When called, this method returns the value stored for the Machine ID.
     @return machineId returns the variable that holds the machineId. */
    public int getMachineId() {return machineId;
    }

    /** This is the setter for the Machine ID. This method sets the value returned in the machineId to the machineId.
     @param machineId this variable stores the returned machineId. */
    public  void setMachineId(int machineId) {this.machineId = machineId;
    }


}
