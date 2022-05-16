import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class OrderListItem extends AnchorPane {

    @FXML
    private ImageView orderImage;
    @FXML
    private Label orderItem;
    @FXML
    private Label orderAmount;
    @FXML
    private Label orderItemPrice;

    public OrderListItem(IMatDataHandler dataHandler, iMatController controller, ShoppingItem item) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("orderListItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.orderImage.setImage(dataHandler.getFXImage(item.getProduct()));
        this.orderItem.setText(item.getProduct().getName());
        this.orderAmount.setText((int)item.getAmount() + "st");
        this.orderItemPrice.setText(item.getTotal() +":-");
    }
}
