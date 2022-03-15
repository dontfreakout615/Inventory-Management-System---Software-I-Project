package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHousePart;
import model.Inventory;
import model.OutsourcedPart;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** This is the controller for the Add Parts Form. This class contains the functionality for the Add Parts Form. */
public class AddPartsForm implements Initializable {
    public Label companyNameMachineIdLabel;
    public RadioButton inHouseRadioAdd;
    public RadioButton outsourcedRadioAdd;
    public Button SaveButtonAdd;
    public TextField IdTextFieldAdd;
    public TextField NameInput;
    public TextField InventoryInput;
    public TextField PriceInput;
    public TextField MaxInPut;
    public TextField MinInput;
    public TextField CompanyName_MachineIdInput;



    /** This is the method for the cancel button. When the Cancel button is clicked, the Add Part form is closed and the user is returned to the Main Screen.
     @param actionEvent - the Add Part form is closed and returns to the Main Screen on user clicking the Cancel button.
     @throws IOException Throws exception */
    public void toMainScreen (ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene mainScreenScene = new Scene(root, 970, 488);
        primaryStage.setTitle("Main Screen");
        primaryStage.setScene(mainScreenScene);
        primaryStage.show();
    }

    /** This is the method for the In-House radio button. When this radio button is selected, the label is set to "Machine ID".
     @param actionEvent - the label is changed when the user selects In-House. */
    public void onInHouseAdd (ActionEvent actionEvent) {

        companyNameMachineIdLabel.setText("        Machine ID");

    }

    /** This is the method for the Outsourced radio button. When this radio button is selected, the label is set to "Company Name".
     @param actionEvent - the label is changed when the user selects Outsourced. */
    public void onOutsourcedAdd (ActionEvent actionEvent) {

        companyNameMachineIdLabel.setText("Company Name");

    }

    /** This is the method for the Save button. This method contains the functionality that checks to ensure the inventory, min , and max values all follow the rules.
     This method also populates a part instance in the Parts table on the Main Screen with the values entered for the new part on the Add Part screen. An error message is thrown
     if the user enters input that does not match the variable type for that field.
     LOGICAL ERROR - The first error message was thrown every time I clicked Save. I realized that I had switched the text fields for the minPart/maxPart variables.
     The minPart variable was paired with the MaxInput text field, which was causing the error message to be thrown even though the user input was entered correctly.
     @param actionEvent - the new part is added to the Parts table on the Main Screen when Save is clicked. The user is returned to the Main Screen. */
    public void savePartAdd(ActionEvent actionEvent)  {

        try {
            int partInventoryLevel = Integer.parseInt(InventoryInput.getText());
            int minPart = Integer.parseInt(MinInput.getText());
            int maxPart = Integer.parseInt(MaxInPut.getText());

            if (minPart > maxPart) {
                errorMessage("ERROR", "Max cannot exceed min. Please check values entered.");
                return;
            }

            else if (partInventoryLevel < minPart || partInventoryLevel > maxPart) {

               errorMessage("ERROR", "Inventory input must be between designated min and max.");
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
            if (CompanyName_MachineIdInput.getText().isEmpty()) {
                emptyTextErrorMessage();
                return;
            }

            else {

                int autoID = createNewId();
                String name = NameInput.getText().trim();
                int inv = partInventoryLevel;
                double cost = Double.parseDouble(PriceInput.getText());

                if (outsourcedRadioAdd.isSelected()) {
                    String compName = CompanyName_MachineIdInput.getText();

                    OutsourcedPart newPart = new OutsourcedPart(autoID, name, cost, inv, minPart, maxPart, compName);
                    model.Inventory.addPart(newPart);
                } else {
                    int machID = Integer.parseInt(CompanyName_MachineIdInput.getText());

                    InHousePart newPart = new InHousePart(autoID, name, cost, inv, minPart, maxPart, machID);
                    model.Inventory.addPart(newPart);

                }

            }

            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene firstScreenScene = new Scene(root, 970, 488);
            primaryStage.setTitle("Main Screen Control");
            primaryStage.setScene(firstScreenScene);
            primaryStage.show();

        } catch (Exception e) {
            errorMessage("Error", "Invalid Input");

        }

    }

    /** This method generated an ID number for a new part or product. This method automatically assigns an ID number when a new part or product is added.
     @return - returns autoId. */
    public static int createNewId(){
        int autoId = 1;

        for(int i = 0; i < Inventory.getAllParts().size(); i++) {
            autoId+=1;
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

    }
}
