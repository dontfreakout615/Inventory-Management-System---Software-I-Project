package main;

import controller.AddProductForm;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import controller.AddPartsForm;

/** This is the Main Class. This class starts the program.
 FUTURE ENHANCEMENT - A future enhancement for an update of this application is an alert for the user when the inventory is critically low, and automatically orders
 more inventory from the supplier. Another enhancement would be to total the value of the inventory.

 Javadoc folder located in C482 Project_Floyd_Fall2021
 */
public class Main extends Application {

    /** This is the Start method. This method loads the Main Screen.
     @param primaryStage The Main Screen stage*/
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        primaryStage.setTitle("Main Screen");
        primaryStage.setScene(new Scene(root, 970, 488));
        primaryStage.show();

        // PARTS - Test Data
        Inventory.addPart(new OutsourcedPart((AddPartsForm.createNewId()), "Jazzy Hub Cap", 209.99, 8, 2, 20, "Cool Guy Moto"));
        Inventory.addPart(new OutsourcedPart((AddPartsForm.createNewId()), "Sparkly Gas Tank", 315.33, 2, 1, 4, "WeCycle"));
        Inventory.addPart(new OutsourcedPart((AddPartsForm.createNewId()), "Zippy Handle Bars", 149.99, 3, 1, 5, "The Friendly Biker, Inc."));
        Inventory.addPart(new OutsourcedPart((AddPartsForm.createNewId()), "Rad Head Light", 62.87, 15, 5, 45, "Vegan Leather Motorcycles"));
        Inventory.addPart(new OutsourcedPart((AddPartsForm.createNewId()), "Spooky Spoked Wheel", 359.65, 2, 2, 6, "Cool Guy Moto"));

        Inventory.addPart(new InHousePart((AddPartsForm.createNewId()), "Tasseled Seat Cover", 64.49, 15, 5, 20, 437));
        Inventory.addPart(new InHousePart((AddPartsForm.createNewId()), "Hot Pink Fender", 195.06, 6, 1, 10, 824));
        Inventory.addPart(new InHousePart((AddPartsForm.createNewId()), "LED Mood Lighting", 37.98, 40, 15, 65, 298));
        Inventory.addPart(new InHousePart((AddPartsForm.createNewId()), "Flame Thrower Muffler", 142.78, 3, 1, 15, 364));

        // PRODUCTS - Test Data
        Inventory.addProduct(new Product((AddProductForm.createNewId()), "Capt. Sparkles Road King", 25099.99, 4, 2, 10));
        Inventory.addProduct(new Product((AddProductForm.createNewId()), "Black Beard's Revenge Chopper", 35412.03, 6, 1, 15));
        Inventory.addProduct(new Product((AddProductForm.createNewId()), "SoCal Sport Bike", 17459.15, 15, 5, 20));
        Inventory.addProduct(new Product((AddProductForm.createNewId()), "Rad Dual Cruiser", 20479.36, 12, 4, 15));
        Inventory.addProduct(new Product((AddProductForm.createNewId()), "Zippy Cafe Racer", 9826.55, 18, 5, 25));
    }

    /** This is hte main method. This method launches the program.
     @param args arguments for the application */
    public static void main(String[] args) {
        launch(args);
    }
}
