package controller;

import javafx.beans.Observable;
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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import model.Inventory;

/** This is the class that controls the Main Screen. All the functionality for the Main Screen is written here.*/
public class MainScreenControl implements Initializable {

    public Button partDelete;
    public Button productDelete;
    public TextField productSearchTextField;

    Stage stage;
    Parent scene;

    public TableView<Part> partsTable;
    public TableColumn<Part, Integer> partID;
    public TableColumn<Part, String> partName;
    public TableColumn<Part, Integer> partInventoryLevel;
    public TableColumn<Part, Double> partPricePerUnit;


    public TableView<Product> productsTable;
    public TableColumn<Product, Integer> productID;
    public TableColumn<Product, String> productName;
    public TableColumn<Product, Integer> productInventoryLevel;
    public TableColumn<Product, Double> productPricePerUnit;
    public TextField partsTextField;


    /** This is the method for the Add button on the Parts pane. When the Add button is clicked, the user is taken to the Add Parts Form.
     @param actionEvent - action when Add button clicked.
     @throws IOException loads specified screen */
    public void toAddPartsForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPartsForm.fxml"));
        Stage addPartStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene addPartScene = new Scene(root, 600, 509);
        addPartStage.setTitle("Add Part Form");
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    /** This is the method for the Modify button. This method throws an error if no part is selected from the Parts Table. This method loads the
     Modify Part controller with the object instance from the Parts Table.
     RUNTIME ERROR - I kept getting a NullPointerException when clicking the Modify Part button. Several of the fxid fields had been left blank in
     the FXML file for the Modify Part Screen. Once those text fields were named properly, this method knew where to send the selected data.
     @param actionEvent - this method runs when the Modify button is pressed on the Parts pane. */
    public void toModifyPartForm(ActionEvent actionEvent) {

        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
            try {

                //load Modify Part Screen
                if (selectedPart == null) {

                    errorMessage("Error", "Please select part to modify.");
                } else {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyPartForm.fxml"));
                    Parent root = loader.load();

                    ModifyPartForm mpf = loader.getController();
                    mpf.getPartToModify(selectedPart);

                    Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                    stage.setTitle("Modify Part Form");
                    stage.setScene(new Scene(root, 600, 509));
                    stage.show();
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
    }

    /** This is the method for the Add button on the Product pane. When the Add button is clicked, the user is taken to the Add Product Form.
     @param actionEvent - action when Add button clicked.
     @throws IOException loads specified screen */
    public void toAddProductForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProductForm.fxml"));
        Stage addProductStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene addProductScene = new Scene(root, 1010, 650);
        addProductStage.setTitle("Add Product Form");
        addProductStage.setScene(addProductScene);
        addProductStage.show();
    }

    /** This method takes the user to the Modify Product form. When the user selected a product to modify from the Products Table,
     the Modify Product form is launched and the data from the selected instance populates the text fields on the Modify Part form.
     An error message is thrown if no product has been selected when they Modify button is clicked.
     @param actionEvent - the action for this method happens when Modify is clicked on the Products Pane. */
    public void toModifyProductForm(ActionEvent actionEvent) {

        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();

        try {
            if (selectedProduct == null) {

                errorMessage("Error", "Please select a product to modify.");
            } else {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProductForm.fxml"));
                Parent root = loader.load();

                ModifyProductForm mpf = loader.getController();
                mpf.getProductToModify(selectedProduct);

                Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                stage.setTitle("Modify Product Form");
                stage.setScene(new Scene(root, 1010, 650));
                stage.show();

            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /** This is the method for the part search text field. When a user enters a part name, partial part name, or part ID number, the parts containing
     that information are displayed on the Parts Table. If nothing is entered into the search field, the entire parts table is returned. If no parts
     are found matching the query, an error message is thrown.
     @param keyEvent - this method is executed when Enter is pressed on the keyboard. */
    public void enterKeyPressedForPart(KeyEvent keyEvent) {

        if (keyEvent.getCode() == KeyCode.ENTER) {

                String grabPart = partsTextField.getText().trim();

                ObservableList<Part> partsFound = Inventory.lookUpByPartName(grabPart);

                if(partsFound.size() ==0) {

                    try {

                    int partID = Integer.parseInt(grabPart);
                    Part part = Inventory.lookUpPartById(partID);

                    if(part!= null) {
                        partsFound.add(part);
                    }

                    } catch (NumberFormatException e){
                    }
                }

            if (partsFound.isEmpty()) {

                errorMessage("Error", "No Part Found");

            } else {
                partsTable.setItems(partsFound);
            }
        }
    }

    /** This is the method for the product search text field. When a user enters a product name, partial product name, or product ID number, the products containing
     that information are displayed on the Products Table. If nothing is entered into the search field, the entire products table is returned. If no products
     are found matching the query, an error message is thrown.
     @param keyEvent - this method is executed when Enter is pressed on the keyboard. */
    public void enterKeyPressedForProduct(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String selectedProduct = productSearchTextField.getText();
            ObservableList<Product> productsFound = Inventory.lookUpByProductName(selectedProduct);
            if(productsFound.size() ==0) {
                try {
                    int productID = Integer.parseInt(selectedProduct);
                    Product product = Inventory.lookUpProductById(productID);
                    if(product!= null) {
                        productsFound.add(product);
                    }

                } catch (NumberFormatException e){
                }

            }
            if (productsFound.isEmpty()) {
                errorMessage("Error", "No part found");

            } else {
                productsTable.setItems(productsFound);
            }
        }
    }

    /** This is the method for the Delete button on the Parts pane. This method deletes a selected part from the parts table. An error message
     is thrown when no part is selected and Delete is pressed. A confirmation message is thrown when a part is selected for deletion, but before
     it is removed from the table.
     @param actionEvent  - this method is executed when the Delete button is pressed. */
    public void deletePart(ActionEvent actionEvent) {

        if (partsTable.getSelectionModel().isEmpty()) {

            errorMessage("No part selected!", "Please select a part to delete.");
        }

        else  {
            Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
            Alert deleteConfirmation = confirmMessage("Confirmation Needed", "Are you you want to delete " + selectedPart.getName() + "?");

            if (deleteConfirmation.getResult() == ButtonType.CANCEL) {
                deleteConfirmation.close();
            } else {

                Inventory.deletePart(selectedPart);
            }
        }
    }

    /** This is the method for the Delete button on the Products pane. This method deletes a selected product from the products table. An error message
     is thrown when no product is selected and Delete is pressed. A confirmation message is thrown when a product is selected for deletion, but before
     it is removed from the table. An error message is thrown if the product selected for deletion has a part associated with it.
     @param actionEvent  - this method is executed when the Delete button is pressed. */
    public void deleteProduct(ActionEvent actionEvent) {

        if (productsTable.getSelectionModel().isEmpty()) {

            errorMessage("No product selected!", "Please select a product to delete.");
        }

        else  {
            Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
            if(selectedProduct.getAssociatedParts().isEmpty()) {

                Alert deleteConfirmation = confirmMessage("Confirmation Needed", "Are you sure want to delete " + selectedProduct.getName() + "?");

                if (deleteConfirmation.getResult() == ButtonType.CANCEL) {
                    deleteConfirmation.close();
                } else {

                    Inventory.deleteProduct(selectedProduct);
                }
            } else {
                errorMessage("Error", "This product cannot be deleted. Please remove associated parts.");
            }
        }
    }

    /** This is the method for the Exit button on the Main Screen. When pressed, a confirmation message is thrown. If the user clicks Cancel, the user is directed back to the
     Main Screen. If the user selects OK on the confirmation message, the program is closed.
     @param actionEvent - this method is executed when the user clicks the Exit button. */
    public void exitButtonPressed(ActionEvent actionEvent) {

        Alert exitConfirmation = confirmMessage("Confirmation Needed", "Are you sure you want to exit this program?");

        if(exitConfirmation.getResult() == ButtonType.CANCEL) {
            exitConfirmation.close();
        } else {

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
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

    /** This method returns a confirmation message. When called from another method in this class, this method displays a confirmation method.
     The programmer can decide on the header and content of the message.
     @param header - this is where the header is displayed.
     @param content - this is where the content of the confirmation message is displayed.
     @return alert - when this method is called, it returns an alert dialog box. */
    public Alert confirmMessage(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Optional<ButtonType> choice = alert.showAndWait();

        return alert;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Parts Table Setup
        partsTable.setItems(Inventory.getAllParts());

        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPricePerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));


        // Product Table Setup
        productsTable.setItems(Inventory.getAllProducts());

        productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPricePerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));


    }
}


