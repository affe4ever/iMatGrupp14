import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import javax.tools.Tool;
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
    Tooltip tooltip, tooltip2;
    Image blacked, red;


    public ProductCard(IMatDataHandler dataHandler, iMatController controller, Product product){


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productCard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        tooltip = new Tooltip("Lägg till favorit");
        tooltip.setFont(Font.font("Lexend Deca Bold", 20));
        tooltip2 = new Tooltip("Ta bort favorit");
        tooltip2.setFont(Font.font("Lexend Deca Bold", 20));

        blacked = new Image("icons/blackheart.png");
        red = new Image("icons/red.png");
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

        productPrice.setText(product.getPrice() + " " + product.getUnit());
        nmrBuy.setText("1");
        updateFavoriteIcon();

        if (product.isEcological()){
            ecologic.setText("EKO");
        }
    }

    public void updateFavoriteIcon(){
        if (!dataHandler.isFavorite(product)){
            heart.setImage(blacked);
            heart.setAccessibleText("blacked");
            Tooltip.install(heart, tooltip);
        }else{
            heart.setImage(red);
            heart.setAccessibleText("red");
            Tooltip.install(heart, tooltip2);
        }
    }

    @FXML
    private void addFavorite(){
        if (heart.getAccessibleText().equals("blacked")){
            heart.setImage(red);
            heart.setAccessibleText("red");
            dataHandler.addFavorite(this.product);
            controller.addToFavorites(this.product);
            controller.stringFavoriteList.add(this.product.getName());
            Tooltip.install(heart, tooltip2);
        }else{
            heart.setImage(blacked);
            heart.setAccessibleText("blacked");
            dataHandler.removeFavorite(this.product);
            controller.stringFavoriteList.add(this.product.getName());
            Tooltip.install(heart, tooltip);
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
        else if (clickedBuy) { //second step

            try {
                if (!(Integer.valueOf(nmrBuy.getText()) > 0)) {
                    nmrBuy.setText("1");
                } else {

                    addProducts();
                    buyButton.setPrefWidth(290);
                    addBuy.setDisable(true);
                    subBuy.setDisable(true);
                    nmrBuy.setDisable(true);

                    clickedBuy = false;

                    Animation animation = new Timeline(
                            new KeyFrame(Duration.millis(0),
                                    new KeyValue(buyButton.textProperty(), nmrBuy.getText() + " " + product.getUnit().replace("kr/", "") + " Tillagd!")),
                            new KeyFrame(Duration.millis(0),
                                    new KeyValue(buyButton.disableProperty(), true)),

                            new KeyFrame(Duration.millis(4000),
                                    new KeyValue(buyButton.textProperty(), "VÄLJ ANTAL")),
                            new KeyFrame(Duration.millis(4000),
                                    new KeyValue(buyButton.disableProperty(), false)));

                    animation.play();
                    nmrBuy.setText("1");

                }

                } catch(Exception e){
                    nmrBuy.setText("1");
                }


        } //change to 290
    }

    private void addProducts(){
            for (ShoppingItem item : dataHandler.getShoppingCart().getItems()){
                if (item.getProduct().equals(this.product)){
                    item.setAmount(item.getAmount() + Integer.valueOf(nmrBuy.getText()));
                    controller.notifyCart();
                    controller.updateCart();
                    return;
                }
            }
            dataHandler.getShoppingCart().addProduct(this.product, Integer.valueOf(nmrBuy.getText()));
            controller.notifyCart();
            controller.updateCart();
    }

    @FXML
    private void addOne(){

        try {
            if (!nmrBuy.getText().equals("99")) {
                Integer temp = (Integer.parseInt(nmrBuy.getText()) + 1);
                nmrBuy.setText(temp.toString());
            }
        } catch (Exception e){
                nmrBuy.setText("1");

            }
    }
    @FXML
    private void subOne(){
        try {
            if (!nmrBuy.getText().equals("0")) {
                Integer temp = (Integer.parseInt(nmrBuy.getText()) - 1);
                nmrBuy.setText(temp.toString());
            }
        }catch (Exception e){
            nmrBuy.setText("1");
        }
    }


    public Product getProduct(){
        return this.product;
    }

}
