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

/** This class contains the functionality of the Modify Product Form. */
public class ModifyProductForm implements Initializable {

    public Button saveModifiedProduct;
    public Button removeAssociatedPart;
    public Button addPartMPF;

    //Form Text Fields - Entered by User
    public TextField modifyProductID;
    public TextField modifyProductName;
    public TextField modifyProductInventory;
    public TextField modifyProductPrice;
    public TextField modifyProductMax;
    public TextField modifyProductMin;

    // Part Inventory Table Setup
    public TableView<Part> accessToPartTableMPF;
    public TableColumn<Part, Integer> partIDMPF;
    public TableColumn<Part, String> partNameMPF;
    public TableColumn<Part, Integer> partInventoryMPF;
    public TableColumn<Part, Double> partPriceMPF;

    // Associated Part Table Setup
    public TableView<Part> associatedPartsMPF;
    public TableColumn<Part, Integer> associatedPartIdMPF;
    public TableColumn<Part, String> associatedPartNameMPF;
    public TableColumn<Part, Integer> associatedPartInventoryMPF;
    public TableColumn<Part, Double> associatedPartPriceMPF;

    // Part Search Text Field
    public TextField MPFsearchPartTextField;

    private Product selectedProduct;
    private Product modifiedProduct;
    private int productId;

    //Lists used to store the part data for the Parts in Inventory and the Associated Parts tables
    private ObservableList<Part> partsInInventory = FXCollections.observableArrayList();
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /** This is the method for the cancel button. When the Cancel button is clicked, the Add Part form is closed and the user is returned to the Main Screen.
     @param actionEvent - the Add Part form is closed and returns to the Main Screen on user clicking the Cancel button.
     @throws IOException loads specified screen */
    public void toMainScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene mainScreenScene = new Scene(root, 970, 488);
        primaryStage.setTitle("Main Screen");
        primaryStage.setScene(mainScreenScene);
        primaryStage.show();
    }

    /** This method get the data selected from the Products Table on the Main Screen. When this mathod is called, the data from the selected product populates to the
     text fields on the Modify Product screen.
     @param selectedProduct - this variable holds the data of the product selected by the user for modification. */
    public void getProductToModify(Product selectedProduct){

        this.selectedProduct = selectedProduct;
        productId = Inventory.getAllProducts().indexOf(selectedProduct);
        modifyProductID.setText(Integer.toString(selectedProduct.getId()));
        modifyProductName.setText(selectedProduct.getName());
        modifyProductInventory.setText(Integer.toString(selectedProduct.getStock()));
        modifyProductPrice.setText(Double.toString(selectedProduct.getPrice()));
        modifyProductMax.setText(Integer.toString(selectedProduct.getMax()));
        modifyProductMin.setText((Integer.toString(selectedProduct.getMin())));
        associatedParts.addAll(selectedProduct.getAssociatedParts());
    }

    /**This is the method for the Save button. This method first check that the data entered into the Inventory, Min, and Max fields meet the required specifications.
     This method then sends the data to the Main Screen and updates the modified product in the Product Table on the Main Screen. This method also updates the
     selected product in Inventory.
     @param actionEvent - this method is executed when the user clicks Save on the Modify Product Screen.*/
    public void saveModifiedProduct(ActionEvent actionEvent) {

        try{
        Integer inventory = Integer.parseInt(this.modifyProductInventory.getText());
        Integer invMin = Integer.parseInt(this.modifyProductMin.getText());
        Integer invMax = Integer.parseInt(this.modifyProductMax.getText());

        if (invMin > invMax) {

            errorMessage("Error", "Minimum inventory cannot exceed designated maximum.");
            return;
        }

        if (inventory < invMin || inventory > invMax) {

            errorMessage("Error", "Inventory cannot be less than designated minimum or exceed designated maximum");
            return;
        }
        if (modifyProductName.getText().isEmpty()) {
            emptyTextErrorMessage();
            return;
        }
        if (modifyProductPrice.getText().isEmpty()) {
            emptyTextErrorMessage();
            return;
        }

        else{
            this.modifiedProduct = selectedProduct;
            selectedProduct.setId(Integer.parseInt(modifyProductID.getText()));
            selectedProduct.setName(modifyProductName.getText());
            selectedProduct.setStock(Integer.parseInt(modifyProductInventory.getText()));
            selectedProduct.setPrice(Double.parseDouble(modifyProductPrice.getText()));
            selectedProduct.setMax(Integer.parseInt(modifyProductMax.getText()));
            selectedProduct.setMin(Integer.parseInt(modifyProductMin.getText()));
            selectedProduct.getAssociatedParts().clear();
            selectedProduct.addAssociatedPart(associatedParts);
            Inventory.updateProduct(productId, selectedProduct);

            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene mainScreenScene = new Scene(root, 970, 488);
            primaryStage.setTitle("Main Screen");
            primaryStage.setScene(mainScreenScene);
            primaryStage.show();
        }
    } catch (Exception e) {

            errorMessage("Error", "Invalid Input");
        }
    }

    /** This is the method for removing an associated part. This method first confirms with the user that they really want to remove this part from the selected product.
     This method then removes that part from the table and from the Associated Parts List.
     LOGICAL ERROR - Nothing was happening when I pressed okay on the confirmation message. I wrote an if- statement that fixed the problem.
     @param actionEvent - this method is executed when the user clicks on the Remove Associated Part button. */
    public void removeAssociatedPartMPF(ActionEvent actionEvent) {

        Part selectedPart = associatedPartsMPF.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Confirmation Needed");
            alert.setContentText("Are you sure you want to delete " + selectedPart.getName() + " from this product?");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK) {
                associatedParts.remove(selectedPart);
                MPFUpdatePartTable();
                MPFUpdateAssociatedPartTable();
            }
        }

        else {
            errorMessage("Error", "No part selected. Please choose a part to delete.");
        }
    }

    /** This method adds a part to the Associated Parts table for the selected product. This method adds the selected part to the Associated Parts table and
     updates the Associated Parts list. If no part is selected before Add is clicked, and error message is thrown.
     @param actionEvent - this method is executed when the user clicks the Add button. */
    public void addPartMPF(ActionEvent actionEvent) {

        Part selectedPart = accessToPartTableMPF.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            associatedParts.add(selectedPart);
            MPFUpdatePartTable();
            MPFUpdateAssociatedPartTable();
        }else {
            errorMessage("Error", "Please select a part to add.");
        }
    }

    /** This is the method to search the Parts table. This method grabs the input from the search field, trims it, and calls the method to find the search input from the
     Inventory class. If the search input is a number, the method for finding a part by ID number is called from the Inventory class. If the search field contains input that
     is not found in the part name or ID fields on the part table, an error message is thrown. If the search field is left empty when this method is called, it returns populates
     the table with all the current parts in the allParts list in Inventory.
     @param keyEvent - this method is called when the user clicks the Search field and presses enter. */
    public void searchPartOnEnterMPF(KeyEvent keyEvent) {

        if (keyEvent.getCode() == KeyCode.ENTER) {
            String selectedPart = MPFsearchPartTextField.getText().trim();
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
                accessToPartTableMPF.setItems(partsFound);
            }
        }
    }

    /** This method sets the Parts table on the Modify Product screen. When this method is called, the parts table on the Modify Parts screen is
     populated with the data stored in the allParts list. This the same list that populates the Parts Table on the Main Screen. */
    public void MPFUpdatePartTable() {
        accessToPartTableMPF.setItems(Inventory.getAllParts());

    }

    /** This method populates the Associated Parts table on the Modify Product Screen. When this method is called, the Associated Parts table is
     populated with the data stored in the associatedParts list. */
    public void MPFUpdateAssociatedPartTable() {
        associatedPartsMPF.setItems(associatedParts);
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

        //Parts and Associated Parts tables initialized here and populated here.
        partsInInventory = Inventory.getAllParts();
        partIDMPF.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameMPF.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryMPF.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceMPF.setCellValueFactory(new PropertyValueFactory<>("price"));
        accessToPartTableMPF.setItems(partsInInventory);

        associatedPartIdMPF.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameMPF.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryMPF.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceMPF.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartsMPF.setItems(associatedParts);

        MPFUpdatePartTable();
        MPFUpdateAssociatedPartTable();
    }
}