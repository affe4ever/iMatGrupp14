import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    @FXML private ScrollPane startPage;
    @FXML private FlowPane categoryList;
    @FXML private Label categoryName;
    @FXML private Label home;
    @FXML private Label favorites;
    @FXML private Label help;
    private String inFront;
    @FXML private AnchorPane categoryPage;
    @FXML private FlowPane productList;
    @FXML private FlowPane favoriteList;
    public ArrayList<String> stringFavoriteList = new ArrayList<>();
    @FXML private AnchorPane favoritePage;
    @FXML private AnchorPane noResult;
    @FXML private AnchorPane helpPage;
    @FXML private ScrollPane allProducts;
    @FXML private FlowPane categoryProductList;
    @FXML private TextField searchField;
    @FXML private Button searchButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                dataHandler.shutDown();
            }
        }));
        inFront = "start";
        setBackground();
        for (String category : categories){
            categoryList.getChildren().add(new CategoryListItem(category, this));
        }

        for (Product product : dataHandler.getProducts()){
            productList.getChildren().add(new ProductCard(dataHandler,this, product));
        }

        for (Product favorite : dataHandler.favorites()){
            favoriteList.getChildren().add(new ProductCard(dataHandler,this, favorite));
            stringFavoriteList.add(favorite.getName());
        }
    }

    public void addToFavorites(Product product){
        boolean alreadyPresent = false;
        for(String name : stringFavoriteList){
            if(product.getName() == name) {
                alreadyPresent = true;
                break; //an optimisation but not strictly necessary //abolsut inte tagit från stack overflow
            }
        }
        if (!alreadyPresent) favoriteList.getChildren().add(new ProductCard(dataHandler, this, product));
    }

    public void updateFavorites(){
        favoriteList.getChildren().clear();
        for (Product favorite : dataHandler.favorites()){
            favoriteList.getChildren().add(new ProductCard(dataHandler,this, favorite));
        }
    }

    private void setBackground(){
        switch (inFront){
            case "start":
                home.setStyle("-fx-background-color: rgba(0,128,0, 1);");
                favorites.setStyle("-fx-background-color: rgba(0,128,0, 0.5)");
                help.setStyle("-fx-background-color: rgba(0,128,0, 0.5)");
                break;
            case "favorites":
                favorites.setStyle("-fx-background-color: rgba(0,128,0, 1);");
                home.setStyle("-fx-background-color: rgba(0,128,0, 0.5)");
                help.setStyle("-fx-background-color: rgba(0,128,0, 0.5)");
                break;
            case "help":
                help.setStyle("-fx-background-color: rgba(0,128,0, 1);");
                home.setStyle("-fx-background-color: rgba(0,128,0, 0.5)");
                favorites.setStyle("-fx-background-color: rgba(0,128,0, 0.5)");
                break;
            case "category":
                home.setStyle("-fx-background-color: rgba(0,128,0, 0.5)");
                favorites.setStyle("-fx-background-color: rgba(0,128,0, 0.5)");
                help.setStyle("-fx-background-color: rgba(0,128,0, 0.5)");
                break;
        }
    }

    @FXML
    public void toStartPage(){
        refreshProductList();
        startPage.toFront();
        inFront = "start";
        setBackground();
    }

    @FXML
    public void toFavorites(){
        updateFavorites();
        favoritePage.toFront();
        inFront = "favorites";
        setBackground();
    }

    @FXML
    public void toHelp(){
        updateFavorites();
        helpPage.toFront();
        inFront = "help";
        setBackground();
    }

    public void search(Event event){
       List<Product> result = dataHandler.findProducts(event.getSource().toString());
    }


    private void refreshProductList(){
        for (Node product : productList.getChildren()){
            ((ProductCard) product).updateFavoriteIcon();
        }
    }

    public void setCategory(String category){
        categoryName.setText(category.toUpperCase());
        categoryName.setStyle("-fx-font-family: 'Lexend Deca Bold'");
        categoryProductList.getChildren().clear();
        populateCategoryList(category);
        inFront = "category";
        setBackground();
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
                updateCategoryList(dataHandler.getProducts(ProductCategory.POD));break;
            case "Dryck":
                updateCategoryList(dataHandler.getProducts(ProductCategory.COLD_DRINKS));
                updateCategoryList(dataHandler.getProducts(ProductCategory.HOT_DRINKS));break;
            case "Kött & Fisk":
                updateCategoryList(dataHandler.getProducts(ProductCategory.MEAT));
                updateCategoryList(dataHandler.getProducts(ProductCategory.FISH));break;
            case "Nötter & Frön":
                updateCategoryList(dataHandler.getProducts(ProductCategory.NUTS_AND_SEEDS));break;
            case "Skafferi":
                updateCategoryList(dataHandler.getProducts(ProductCategory.HERB));
                updateCategoryList(dataHandler.getProducts(ProductCategory.SWEET));
                updateCategoryList(dataHandler.getProducts(ProductCategory.FLOUR_SUGAR_SALT));
                updateCategoryList(dataHandler.getProducts(ProductCategory.BREAD));break;
            case "Mejeri":
                updateCategoryList(dataHandler.getProducts(ProductCategory.DAIRIES));break;

        }

    }

    public void updateCategoryList(List<Product> products){
        for (Product product : products){
            categoryProductList.getChildren().add(new ProductCard(dataHandler, this, product));
        }

    }

    @FXML
    public void checkSearch(KeyEvent event){

        if (event.getCode().equals(KeyCode.ENTER))
        {
        performSearch();
        }
        if (searchField.getText().isEmpty()){
            searchButton.setStyle("-fx-opacity: 0");
        }else{
            searchButton.setStyle("-fx-opacity: 1");
        }
    }

    public void performSearch(){
        List<Product> matches = dataHandler.findProducts(searchField.getText());

        if (matches.isEmpty()){
            noResult.toFront();
        }else{
            favoriteList.getChildren().clear();
            for (Product product : matches){
                favoriteList.getChildren().add(new ProductCard(dataHandler, this, product));
            }
            favoritePage.toFront();
        }



    }


}
