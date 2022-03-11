package model;




import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;


/** This is the Inventory Class. This class contains methods for manipulating Part and Product data from the Controller classes. */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /** This the is getter for the allParts List. This getter returns the data stored in the allParts List.
     @return allParts list */
    public static ObservableList<Part> getAllParts() {return allParts;}

    /** This is the getter for the allProducts List. This getter return the data stored in the allProducts List.
     * @return allProducts list */
    public static ObservableList<Product> getAllProducts() {return allProducts;}


    //PARTS METHODS - add, lookup, update, delete

    /** This is the addPart method. This method adds a specified part to the allParts List.
     @param newPart The variable holding the data for the new part */
    public static void addPart(Part newPart) {allParts.add(newPart);
    }

    /** This method finds a part by ID. This method searches the allParts list for a specific ID number. It returns the parts found.
     @param partId The part's ID number
     @return foundPartId - The part associated with any found ID numbers */
    public static Part lookUpPartById(int partId) {
        Part foundPartId = null;
        for (Part part : allParts) {
            if (partId == part.getId()) {
                foundPartId = part;
            }
        }
        return foundPartId;
    }

    /** This method finds a part associated with a search entry. When a user enters string input in the search bar, this list is populated with the parts whose
     names include the input from the search text field. This list allows for searches to be partial in upper and lower case.
     LOGICAL ERROR - When searching a single lower case letter, any parts identified were being duplicated in the table when returned. I figured out that the if statement with the
     toLowerCase method was not called when the table came back empty, it was returning those parts with that letter twice, once for line 60 and once for line 63. When I added the
     if statement that checked if the table was empty after the first run through of the search, the issue was fixed.
     @param grabPart the input entered in the search text field.
     @return foundParts - any parts that match the search input */
    public static ObservableList<Part> lookUpByPartName(String grabPart) {

        ObservableList<Part> foundParts = FXCollections.observableArrayList();

        if (grabPart.length() == 0) {
            foundParts = allParts;
        } else {
            for(int i = 0; i < allParts.size(); i++) {
                if(allParts.get(i).getName().contains(grabPart)) {
                    foundParts.add(allParts.get(i));
                } else if(foundParts.isEmpty()) {
                    if(allParts.get(i).getName().toLowerCase().contains(grabPart)){
                        foundParts.add(allParts.get(i));
                    }
                }
            }
        }
        return foundParts;
    }

    /** This method updates part data. When this method is called, the specified part in the allParts list is updated with new or edited input from the user.
     @param index The index of the part to be updated
     @param selectedPart the selected part's data */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /** This method deletes a part. The user selects a part and this method removes that part from the allParts List.
     @param selectedPart The part to be deleted
     @return The allParts list is returned without the deleted data */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    //PRODUCT Methods - add, lookup, update, delete

    /** This is the addProduct method. This method adds a specified product to the allProducts List.
    @param newProduct The variable holding the data for the new product */
    public static void addProduct(Product newProduct) {allProducts.add(newProduct);}

    /** This method finds a product by ID. This method searches the allProducts list for a specific ID number. It returns the products found.
     @param productId The product's ID number
     @return foundPartId - The product associated with any found ID numbers */
    public static Product lookUpProductById(int productId) {
        Product foundProductId = null;
        for (Product product : allProducts) {
            if (productId == product.getId()) {
                foundProductId = product;
            }
        }
        return foundProductId;
    }

    /** This method finds a product associated with a search entry. When a user enters string input in the search bar, this list is populated with the products whose
     names include the input from the search text field. This list allows for searches to be partial and lower case.
     @param grabProduct The input entered in the search text field.
     @return foundProducts - Any products that match the search input */
    public static ObservableList<Product> lookUpByProductName(String grabProduct) {

        ObservableList<Product> foundProducts = FXCollections.observableArrayList();

        if (grabProduct.length() == 0) {
            foundProducts = allProducts;
        } else {
            for(int i = 0; i < allProducts.size(); i++) {
                if(allProducts.get(i).getName().contains(grabProduct)) {
                    foundProducts.add(allProducts.get(i));
                } else if(foundProducts.isEmpty()) {
                    if (allProducts.get(i).getName().toLowerCase().contains(grabProduct)) {
                        foundProducts.add(allProducts.get(i));
                    }
                }
            }
        }
        return foundProducts;
    }

    /** This method updates product data. When this method is called, the specified product in the allProducts list is updated with new or edited input from the user.
     @param index The index of the product to be updated
     @param selectedProduct the selected product's data */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /** This method deletes a product. The user selects a product and this method removes that product from the allProducts List.
     @param selectedProduct The product to be deleted
     @return The allProducts list is returned without the deleted data */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
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

}