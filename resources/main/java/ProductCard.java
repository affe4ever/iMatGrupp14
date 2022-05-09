import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;

import java.io.IOException;

public class ProductCard extends AnchorPane {

    @FXML
    private ImageView productImage;
    @FXML
    private Label productName;
    @FXML
    private Label ecologic;
    @FXML
    private Label productPrice;
    @FXML
    private ImageView heart;

    private IMatDataHandler dataHandler;
    private Product product;
    private iMatController controller;


    public ProductCard(IMatDataHandler dataHandler, iMatController controller, Product product){

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productCard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.controller = controller;
        this.product = product;
        this.dataHandler = dataHandler;
        productImage.setImage(dataHandler.getFXImage(product));
        productName.setText(product.getName().toUpperCase());
        productPrice.setText(product.getPrice() + " :-/kg");

        updateFavoriteIcon();

        if (product.isEcological()){
            ecologic.setText("EKO");
        }
    }

    public void updateFavoriteIcon(){
        if (!dataHandler.isFavorite(product)){
            heart.setImage(new Image("icons/blackheart.png"));
            heart.setAccessibleText("blacked");
        }else{
            heart.setImage(new Image("icons/red.png"));
            heart.setAccessibleText("red");
        }
    }

    @FXML
    private void addFavorite(){
        if (heart.getAccessibleText().equals("blacked")){
            heart.setImage(new Image("icons/red.png"));
            heart.setAccessibleText("red");
            dataHandler.addFavorite(this.product);
            controller.addToFavorites(this.product);
        }else{
            heart.setImage(new Image("icons/blackheart.png"));
            heart.setAccessibleText("blacked");
            dataHandler.removeFavorite(this.product);

        }

    }

    public Product getProduct(){
        return this.product;
    }

}
