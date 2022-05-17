import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.sql.Date;
import java.time.Month;

public class PlacedOrder extends TitledPane {
    @FXML
    private Label orderDate;
    @FXML
    private Label accordionDetail;
    @FXML
    private Label orderTotal;

    @FXML
    private FlowPane orderItemList;

    private double orderPrice;

    private IMatDataHandler dataHandler;
    private iMatController controller;
    private Order order;

    public PlacedOrder(IMatDataHandler dataHandler, iMatController controller, Order order){

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("order.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.dataHandler = dataHandler;
        this.controller = controller;
        this.order = order;

        String curTime = String.format("%02d:%02d", order.getDate().getHours(), order.getDate().getMinutes());
        String detailString = "# " + Month.of(order.getDate().getMonth() + 1) + " " +
                order.getDate().getDate() + " "
                +curTime;
        this.orderDate.setText(detailString);

        for (ShoppingItem item : order.getItems()){
            this.orderPrice += item.getTotal();
        }
        orderTotal.setText(orderPrice + "kr");

        for (ShoppingItem item : order.getItems()){
            orderItemList.getChildren().add(new OrderListItem(dataHandler, controller, item, "normal"));
        }

    }

    @FXML
    public void buyAgain(){
        dataHandler.getShoppingCart().clear();
        for (ShoppingItem item : order.getItems()){
            dataHandler.getShoppingCart().addItem(item);
        }
        controller.updateCart();
        controller.toCart();

    }

    @FXML
    public void changeDetails(){
        if (accordionDetail.getText().equals("Visa Detaljer")){
            accordionDetail.setText("Minska Detaljer");
        }else{
            accordionDetail.setText("Visa Detaljer");
        }
    }

}
