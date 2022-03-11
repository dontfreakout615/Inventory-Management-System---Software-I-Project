package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class contains the functionality for the Add Parts screen. */
public class AddProductForm implements Initializable {
    public TextField AutoGenProductId;
    public TextField NameInput;
    public TextField InventoryInput;
    public TextField PriceInput;
    public TextField MaxInPut;
    public TextField MinInput;


    public TableView<Part> APFAccessToPartsTable;
    public TableColumn<Part, Integer> partAddToProductID;
    public TableColumn<Part, String> partToAddToProductName;
    public TableColumn<Part, Integer> partToAddToProductInv;
    public TableColumn<Part, Double> partToAddToProductPrice;

    public TableView<Part> APFAccessToAssociatedPartsTable;
    public TableColumn<Part, Integer> apfAssociatedPartID;
    public TableColumn<Part, String> apfAssociatedPartName;
    public TableColumn<Part, Integer> apfAssociatedPartInv;
    public TableColumn<Part,Double> apfAssociatedPartPrice;
    public Button saveNewProduct;
    public Button addPartToNewProduct;
    public Button removeAssociatedPart;
    public TextField APFPartsSearchField;

    private ObservableList<Part> partsInInventory = FXCollections.observableArrayList();
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /** This is the method for the cancel button. When the Cancel button is clicked, the Add Product form is closed and the user is returned to the Main Screen.
     @param actionEvent - the Add Product form is closed and returns to the Main Screen on user clicking the Cancel button.
     @throws IOException loads specified screen */
    public void toMainScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene mainScreenScene = new Scene(root, 970, 488);
        primaryStage.setTitle("Main Screen");
        primaryStage.setScene(mainScreenScene);
        primaryStage.show();
    }

    /** This is the method for the Save button. This method contains the functionality that checks to ensure the inventory, min , and max values all follow the rules.
     This method also populates a product instance in the Products table on the Main Screen with the values entered for the new product on the Add Product screen. An error message is thrown
     if the user enters input that does not match the variable type for that field.
     @param actionEvent - the new product is added to the Product table on the Main Screen when Save is clicked. The user is returned to the Main Screen.
     @throws IOException Input Invalid */
    public void saveNewProduct(ActionEvent actionEvent) throws IOException {

        try {
        Integer inventory = Integer.parseInt(this.InventoryInput.getText());
        Integer invMin = Integer.parseInt(this.MinInput.getText());
        Integer invMax = Integer.parseInt(this.MaxInPut.getText());

        if (invMin > invMax) {

            errorMessage("Error", "Minimum inventory cannot exceed designated maximum.");
            return;
        }

        if (inventory < invMin || inventory > invMax) {

            errorMessage("Error", "Inventory cannot be less than designated minimum or exceed designated maximum");
            return;
        }
        if (NameInput.getText().isEmpty()) {
            emptyTextErrorMessage();
            return;
        }
        if (PriceInput.getText().isEmpty()) {
            emptyTextErrorMessage();
            return;
        }

        int autoID = createNewId();
        String name = NameInput.getText().trim();
        double price = Double.parseDouble(PriceInput.getText().trim());
        int stock = Integer.parseInt(InventoryInput.getText().trim());

        Product newProduct = new Product(autoID, name, price, stock, invMin, invMax);
        newProduct.setId(createNewId());
        newProduct.setName(this.NameInput.getText().trim());
        newProduct.setPrice(Double.parseDouble(this.PriceInput.getText()));
        newProduct.setStock(Integer.parseInt(this.InventoryInput.getText()));
        newProduct.setMax(Integer.parseInt(this.MaxInPut.getText()));
        newProduct.setMin(Integer.parseInt(this.MinInput.getText()));
        newProduct.addAssociatedPart(associatedParts);
        Inventory.addProduct(newProduct);

        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene firstScreenScene = new Scene(root, 970, 488);
        primaryStage.setTitle("Main Screen Control");
        primaryStage.setScene(firstScreenScene);
        primaryStage.show();


    } catch (Exception e){

        errorMessage("Error", "Invalid Input");

        }
    }

    /** This is the method for the Add button. This method grabs the data for the part highlighted in the Parts table on the Add Product screen and adds it
     to the Associated Parts table. This method associates a chosen part with a new product.If no part is selected before Add is clicked, and error message is thrown.
     @param actionEvent - when the Add button is clicked, the Associated Parts table populates with the part chosen. */
    public void addPartToNewProduct(ActionEvent actionEvent) {
        Part selectedPart = APFAccessToPartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            associatedParts.add(selectedPart);
            APFUpdatePartTable();
            APFUpdateAssociatedPartTable();
        } else {
            errorMessage("Error", "Please select a part to add.");
        }
    }

    /** This is the method to search the Parts table. This method grabs the input from the search field, trims it, and calls the method to find the search input from the
     Inventory class. If the search input is a number, the method for finding a part by ID number is called from the Inventory class. If the search field contains input that
     is not found in the part name or ID fields on the part table, an error message is thrown. If the search field is left empty when this method is called, it returns populates
     the table with all the current parts in the allParts list in Inventory.
     LOGICAL ERROR - A runtime error occurred when enter was pressed for the search field. Nothing was being returned. I had not tied the keyEvent to a specific key on the keyboard.
     Once I used the KeyCode method, the problem was resolved.
     @param keyEvent - this method is called when the user clicks the Search field and presses enter. */
    public void APFPartsSearchOnEnter(KeyEvent keyEvent) {

        if (keyEvent.getCode() == KeyCode.ENTER) {
            String selectedPart = APFPartsSearchField.getText().trim();
            ObservableList<Part> partsFound = Inventory.lookUpByPartName(selectedPart);

            if(partsFound.size() ==0) {
                try {
                    int partID = Integer.parseInt(selectedPart);
                    Part part = Inventory.lookUpPartById(partID);
                    if(part!= null) {
                        partsFound.add(part);
                    }

                } catch (NumberFormatException e){
                }

            }
            if (partsFound.isEmpty()) {
               errorMessage("Error", "No part found");

            } else {
                APFAccessToPartsTable.setItems(partsFound);
            }
        }
    }

    /** This method disassociates a part from a new part and removes the associates part from the Associated Parts table. When the user presses the Remove
     Associated Part button, the selected part is deleted from the Associated Parts table and not saved with the new part. A confirmation error is generated
     when an associated part is selected and the Remove Associated Part button is selected. An error message is thrown if no part is selected and button pressed.
     @param actionEvent - this method is called when the Remove Associated Part button is selected by the user.*/
    public void removeAssociatedPart(ActionEvent actionEvent) {

        Part selectedPart = APFAccessToAssociatedPartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Confirmation Needed");
            alert.setContentText("Are you sure you want to delete " + selectedPart.getName() + " from this product?");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK) {
                associatedParts.remove(selectedPart);
                APFUpdatePartTable();
                APFUpdateAssociatedPartTable();
            }

        } else {
            errorMessage("Error", "No part selected. Please choose a part to delete.");
        }
    }

    /** This method sets the Part Table. This method populates the Part table on the Add Part screen with the same data that populates
     the Part table on the Main Screen. */
    public void APFUpdatePartTable() {
        APFAccessToPartsTable.setItems(Inventory.getAllParts());
    }

    /** This method sets the Associated Parts Table. This method populates the Associated Parts Table with the data stored in the associatedParts list.*/
    public void APFUpdateAssociatedPartTable() {
        APFAccessToAssociatedPartsTable.setItems(associatedParts);
    }

    /** This method automatically creates a unique ID number. When this method is called, it assigns a unique ID number to a new part.
     @return autoId - the variable that contains the ID. */
    public static int createNewId() {
        int autoId = 1;

        for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
            autoId += 1;
        }
        return autoId;
    }

    /** This method displays an error message. This method is called when an error message is needed in another method of this class.
     @param header - This is the header for the error message.
     @param content - This is where the content of the error message is defined. */
    public void errorMessage(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /** This is the error message method for blank fields. This method is called when fields are left blank by the user. */
    public void emptyTextErrorMessage() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText("One or more fields have been left blank. Please provide input for blank field(s).");
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partsInInventory = Inventory.getAllParts();
        partAddToProductID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partToAddToProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partToAddToProductInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partToAddToProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        APFAccessToPartsTable.setItems(partsInInventory);

        apfAssociatedPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        apfAssociatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        apfAssociatedPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        apfAssociatedPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        APFAccessToAssociatedPartsTable.setItems(associatedParts);


    }



}
