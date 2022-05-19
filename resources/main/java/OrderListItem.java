import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
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
    private Label orderItemPrice;

    public OrderListItem(IMatDataHandler dataHandler, iMatController controller, ShoppingItem item, String size) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("orderListItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.orderImage.setImage(dataHandler.getFXImage(item.getProduct()));
        this.orderItem.setText((int) item.getAmount() + item.getProduct().getUnit().replace("kr/", "") + " " + item.getProduct().getName());
        this.orderItemPrice.setText(item.getTotal() +":-");

        if (size.equals("small")){
            this.prefWidthProperty().set(400);
        }else{
            this.prefWidthProperty().set(500);
        }

    }
}
