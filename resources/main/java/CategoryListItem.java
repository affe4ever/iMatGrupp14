import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CategoryListItem extends AnchorPane {

    @FXML
    private Label categoryLink;

    private iMatController controller;

    public CategoryListItem(String category, iMatController controller){

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("categoryListItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.controller = controller;
        categoryLink.setText(category);
        categoryLink.setAccessibleText(category.toLowerCase().replace("visa ", ""));

    }
    @FXML
    public void toCategoryPage(){
        controller.setCategory(categoryLink.getText().replace("Visa ", ""));
    }

    public void setActiveCategory(String category) {
        if (categoryLink.getAccessibleText().equals(category)) {
            categoryLink.getStyleClass().add("category-used");
        }else{
            categoryLink.getStyleClass().remove("category-used");
        }
    }

}
