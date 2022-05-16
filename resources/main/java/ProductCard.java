import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ProductCard extends AnchorPane {

    @FXML private ImageView productImage;
    @FXML private Label productName;
    @FXML private Label ecologic;
    @FXML private Label productPrice;
    @FXML private ImageView heart;
    @FXML private Button buyButton;
    @FXML private Button subBuy;
    @FXML private Button addBuy;
    @FXML private TextField nmrBuy;

    private IMatDataHandler dataHandler;
    private Product product;
    private iMatController controller;
    private boolean clickedBuy = false;


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
        String temp = product.getName().toLowerCase();
        temp = temp.replace("ekologisk ", "");
        temp = temp.replace("ekologiskt ", "");
        temp = temp.replace("ekologiska ", "");
        temp = temp.substring(0,1).toUpperCase() + temp.substring(1);
        productName.setText(temp);

        productPrice.setText(product.getPrice() + ":-");
        nmrBuy.setText("1");
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
            controller.stringFavoriteList.add(this.product.getName());
        }else{
            heart.setImage(new Image("icons/blackheart.png"));
            heart.setAccessibleText("blacked");
            dataHandler.removeFavorite(this.product);
            controller.stringFavoriteList.add(this.product.getName());
        }

    }

    @FXML
    private void twoStepBuy(){
        if (!clickedBuy){ //first step, change 115
            clickedBuy = true;
            buyButton.setText("KÖP");
            buyButton.setPrefWidth(115);
            addBuy.setDisable(false);
            subBuy.setDisable(false);
            nmrBuy.setDisable(false);
        }
        else if (clickedBuy){ //second step
            addProducts();
            buyButton.setPrefWidth(290);
            buyButton.setText(nmrBuy.getText() + "st Tillagd!");
            addBuy.setDisable(true);
            subBuy.setDisable(true);
            nmrBuy.setDisable(true);
            nmrBuy.setText("1");
            clickedBuy = false;
        } //change to 290
    }

    private void addProducts(){
        dataHandler.getShoppingCart().addProduct(this.product, Integer.valueOf(nmrBuy.getText()));
        controller.notifyCart();
        controller.updateCart();
    }

    @FXML
    private void addOne(){
        if (!nmrBuy.getText().equals("99")){
            Integer temp = (Integer.parseInt(nmrBuy.getText()) + 1);
            nmrBuy.setText(temp.toString());
        }
        else{
            //todo error
        }
    }
    @FXML
    private void subOne(){
        if (!nmrBuy.getText().equals("0")){
            Integer temp = (Integer.parseInt(nmrBuy.getText()) - 1);
            nmrBuy.setText(temp.toString());
        }
        else{
            //todo error
        }
    }

    public Product getProduct(){
        return this.product;
    }

}
