import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import javax.print.attribute.standard.DateTimeAtCompleted;
import java.io.IOException;

public class CartItem extends AnchorPane {

    @FXML
    private ImageView cartImage;
    @FXML
    private Label cartText;
    @FXML
    private Label cartPrice;
    @FXML
    private TextField nmrBuy;

    private iMatController controller;
    private ShoppingItem item;
    private IMatDataHandler dataHandler;

    public CartItem(IMatDataHandler dataHandler, iMatController controller, ShoppingItem product) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cartItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.item = product;
        this.controller = controller;
        this.dataHandler = dataHandler;
        cartImage.setImage(dataHandler.getFXImage(product.getProduct()));
        cartText.setText((int) product.getAmount() + "st " + product.getProduct().getName());
        cartPrice.setText(Math.round(product.getTotal()*100.00)/100.00 + ":-");
        nmrBuy.setText((int) product.getAmount() + "");
    }

    public ShoppingItem getItem(){
        return this.item;
    }

    @FXML
    private void addCart(){
        controller.addCartItem(this);
        nmrBuy.setText((int) item.getAmount() + 1 + "");
    }

    @FXML
    private void subCart(){

        if (nmrBuy.getText().equals("1")){
            controller.removeCartItem(this);
        }else{
            controller.subCartItem(this);
        }

    }

}
