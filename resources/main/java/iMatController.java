import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class iMatController implements Initializable {

    IMatDataHandler dataHandler = IMatDataHandler.getInstance();
    private final ArrayList<String> categories = new ArrayList<String>(Arrays.asList("Visa alla produkter","Pasta, Potatis & Ris",
            "Frukt & Grönt", "Nötter & Frön", "Kött & Fisk", "Skafferi", "Dryck", "Mejeri"));

    @FXML
    private ScrollPane startPage;
    @FXML
    private FlowPane categoryList;

    @FXML
    private Label categoryName;

    @FXML
    private Label home;

    @FXML
    private AnchorPane categoryPage;

    @FXML
    private FlowPane productList;

    @FXML
    private FlowPane favoriteList;
    @FXML
    private AnchorPane favoritePage;
    @FXML
    private ScrollPane allProducts;

    @FXML
    private FlowPane categoryProductList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                dataHandler.shutDown();

            }
        }));

        for (String category : categories){
            categoryList.getChildren().add(new CategoryListItem(category, this));

        }
        for (Product product : dataHandler.getProducts()){
            productList.getChildren().add(new ProductCard(dataHandler,this, product));
        }

        for (Product favorite : dataHandler.favorites()){
            favoriteList.getChildren().add(new ProductCard(dataHandler,this, favorite));
        }
    }

    public void addToFavorites(Product product){
        favoriteList.getChildren().add(new ProductCard(dataHandler, this, product));
    }

    public void updateFavorites(){
        favoriteList.getChildren().clear();
        for (Product favorite : dataHandler.favorites()){
            favoriteList.getChildren().add(new ProductCard(dataHandler,this, favorite));
        }
    }

    public void toStartPage(){
        refreshProductList();
        allProducts.toFront();
        home.setStyle("-fx-background-color: rgba(0,128,0, 1);");

    }


    private void refreshProductList(){
        for (Node product : productList.getChildren()){
            ((ProductCard) product).updateFavoriteIcon();
        }
    }

    public void toFavorites(){
        updateFavorites();
        favoritePage.toFront();
    }

    public void setCategory(String category){
        categoryName.setText(category.toUpperCase());
        categoryProductList.getChildren().clear();
        populateCategoryList(category);

        categoryPage.toFront();
        for (Node item : categoryList.getChildren()){
                ((CategoryListItem) item).setActiveCategory(category);
        }
    }

    public void populateCategoryList(String category){

        categoryProductList.getChildren().clear();
        switch(category){
            case "Visa alla produkter": updateCategoryList(dataHandler.getProducts()); break;
            case "Pasta, Potatis & Ris":
                updateCategoryList(dataHandler.getProducts(ProductCategory.PASTA));
                updateCategoryList(dataHandler.getProducts(ProductCategory.POTATO_RICE));break;
            case "Frukt & Grönt":
                updateCategoryList(dataHandler.getProducts(ProductCategory.BERRY));
                updateCategoryList(dataHandler.getProducts(ProductCategory.CABBAGE));
                updateCategoryList(dataHandler.getProducts(ProductCategory.CITRUS_FRUIT));
                updateCategoryList(dataHandler.getProducts(ProductCategory.EXOTIC_FRUIT));
                updateCategoryList(dataHandler.getProducts(ProductCategory.FRUIT));
                updateCategoryList(dataHandler.getProducts(ProductCategory.MELONS));
                updateCategoryList(dataHandler.getProducts(ProductCategory.ROOT_VEGETABLE));
                updateCategoryList(dataHandler.getProducts(ProductCategory.VEGETABLE_FRUIT));
                updateCategoryList(dataHandler.getProducts(ProductCategory.POD));


        }

    }

    public void updateCategoryList(List<Product> products){
        for (Product product : products){
            categoryProductList.getChildren().add(new ProductCard(dataHandler, this, product));
        }

    }


}
