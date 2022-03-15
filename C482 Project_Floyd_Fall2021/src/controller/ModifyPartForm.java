package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHousePart;
import model.Inventory;
import model.Part;
import model.OutsourcedPart;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** This class contains the functionality for the Modify Part screen. */
public class ModifyPartForm implements Initializable {

    //Radio Button and Label
    public RadioButton inHouseRadioModify;
    public RadioButton outsourcedRadioModify;
    public Label companyNameMachineIdLabel;

    //Text fields where user enters data
    public TextField partID;
    public TextField partName;
    public TextField partPricePerUnit;
    public TextField partInventoryLevel;
    public TextField partMin;
    public TextField partMax;
    public TextField machID_compName;

    //Variables used to hold part data in the methods
    private Part selectedPart;
    private Part modifiedPart;
    private int selectedPartId;

    public Object scene;
    public Stage stage;


    /** This is the method for the cancel button. When the Cancel button is clicked, the Add Part form is closed and the user is returned to the Main Screen.
     * @param actionEvent - the Modify Part form is closed and returns to the Main Screen on user clicking the Cancel button.
     @throws IOException loads specified screen */
   public void toMainScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene mainScreenScene = new Scene(root, 970, 488);
        primaryStage.setTitle("Main Screen");
        primaryStage.setScene(mainScreenScene);
        primaryStage.show();
   }

    /** This is the method for the In-House radio button. When this button is selected, the last label switch to "Machine ID".
     @param actionEvent - the label is changed when the radio button is selected. */
    public void onInHouseModify (ActionEvent actionEvent) {

        companyNameMachineIdLabel.setText("        Machine ID");
    }

    /** This is the method for the In-House radio button. When this button is selected, the last label switch to "Machine ID".
     @param actionEvent - the label is changed when the radio button is selected. */
    public void onOutsourcedModify (ActionEvent actionEvent) {

        companyNameMachineIdLabel.setText("Company Name");
    }

    /** This method get the data from the part selected to modify. This method sets the data in the text fields on this page to the
     part selected from the Parts Table on the Main Screen. This method also assess if the chosen part is In-House or Outsourced and change
     the label for that text field accordingly.
     RUNTIME ERROR - When attempting to access this form via the Modify button on the Main Screen, it produced a Null Pointer Exception. The exception
     pointed to the partID when debugged. I had not set the partID to the text field in the FXML for this form. Once the fxid was set, the problem was fixed.
     @param selectedPart - this variable is used to hold the data from the part selected from the Parts Table. */
    public void getPartToModify(Part selectedPart) {

        this.selectedPart = selectedPart;
        selectedPartId = Inventory.getAllParts().indexOf(selectedPart);
        partID.setText(Integer.toString(selectedPart.getId()));
        partName.setText(selectedPart.getName());
        partInventoryLevel.setText(Integer.toString(selectedPart.getStock()));
        partPricePerUnit.setText(Double.toString(selectedPart.getPrice()));
        partMax.setText(Integer.toString(selectedPart.getMax()));
        partMin.setText(Integer.toString(selectedPart.getMin()));

        if(selectedPart instanceof InHousePart) {
            InHousePart ihp = (InHousePart) selectedPart;
            inHouseRadioModify.setSelected(true);
            this.companyNameMachineIdLabel.setText("        Machine ID");
            machID_compName.setText(Integer.toString(ihp.getMachineId()));
        } else{
            OutsourcedPart osp = (OutsourcedPart) selectedPart;
            outsourcedRadioModify.setSelected(true);
            this.companyNameMachineIdLabel.setText("Company Name");
            machID_compName.setText(osp.getCompanyName());
        }
    }

    /** This is the method for the Save button. This method first check that the data entered into the Inventory, Min, and Max fields meet the required specifications.
     This method then saves the modified part data to the Parts Table on the Main Screen. If the In-House radio button is selected the Machine ID is saved. If the
     Outsourced radio button is selected, the Company Name is saved.
     @param actionEvent - this method is executed when the Save button is selected on this pane. */
    public void onSaveModify(ActionEvent actionEvent) {

        try {
       Integer inventory = Integer.parseInt(this.partInventoryLevel.getText());
       Integer invMin = Integer.parseInt(this.partMin.getText());
       Integer invMax = Integer.parseInt(this.partMax.getText());

        if (invMin > invMax) {

            errorMessage("Error", "Minimum inventory cannot exceed designated maximum.");
            return;
        }

        if (inventory < invMin || inventory > invMax) {

            errorMessage("Error", "Inventory cannot be less than designated minimum or exceed designated maximum");
            return;

        }
        if (partName.getText().isEmpty()) {
            emptyTextErrorMessage();
            return;
        }
        if (partPricePerUnit.getText().isEmpty()) {
            emptyTextErrorMessage();
            return;
        }
        if (machID_compName.getText().isEmpty()) {
            emptyTextErrorMessage();
            return;
        }

        else {

            this.modifiedPart = selectedPart;
            selectedPart.setId(Integer.parseInt(partID.getText()));
            selectedPart.setName(partName.getText());
            selectedPart.setStock(Integer.parseInt(partInventoryLevel.getText()));
            selectedPart.setPrice(Double.parseDouble(partPricePerUnit.getText()));
            selectedPart.setMax(Integer.parseInt(partMax.getText()));
            selectedPart.setMin(Integer.parseInt(partMin.getText()));

            if(inHouseRadioModify.isSelected()) {

                 InHousePart ihp = (InHousePart) selectedPart;
                 ((InHousePart) selectedPart).setMachineId(Integer.parseInt(machID_compName.getText()));
                 Inventory.updatePart(selectedPartId, selectedPart);

                 Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                 Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                 Scene mainScreenScene = new Scene(root, 970, 488);
                 primaryStage.setTitle("Main Screen");
                 primaryStage.setScene(mainScreenScene);
                 primaryStage.show();

            } else if(outsourcedRadioModify.isSelected()){

                 OutsourcedPart ihp = (OutsourcedPart) selectedPart;
                 ((OutsourcedPart) selectedPart).setCompanyName(machID_compName.getText());
                 Inventory.updatePart(selectedPartId, selectedPart);

                 Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                 Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                 Scene mainScreenScene = new Scene(root, 970, 488);
                 primaryStage.setTitle("Main Screen");
                 primaryStage.setScene(mainScreenScene);
                 primaryStage.show();
             }
        }

    } catch (Exception e) {
        errorMessage("Error", "Invalid input");}
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

    }


}
