import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class iMatController implements Initializable {

    IMatDataHandler dataHandler = IMatDataHandler.getInstance();
    private final ArrayList<String> categories = new ArrayList<String>(Arrays.asList("Visa alla produkter", "Pasta, Potatis & Ris",
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
    private Label favorites;
    @FXML
    private Label help;
    private String inFront;
    private String previous;
    @FXML
    private AnchorPane categoryPage;
    @FXML
    private FlowPane productList;
    @FXML
    private FlowPane favoriteList;
    public ArrayList<String> stringFavoriteList = new ArrayList<>();
    @FXML
    private AnchorPane favoritePage;
    @FXML
    private AnchorPane noResult;
    @FXML
    private Label noResultText;
    @FXML
    private AnchorPane helpPage;
    @FXML
    private AnchorPane cartPage;
    @FXML
    private ScrollPane allProducts;
    @FXML
    private FlowPane categoryProductList;
    @FXML
    private TextField searchField;
    @FXML
    private TextField searchField1;
    @FXML
    private Button searchButton;
    @FXML
    private Label kundvagn;
    @FXML
    private AnchorPane stepOne;
    @FXML
    private AnchorPane stepTwo;
    @FXML
    private AnchorPane stepThree;
    @FXML
    private FlowPane shoppingCartItems;
    @FXML
    private AnchorPane checkoutPage;
    @FXML
    private Label iMatLogo;
    @FXML
    private Label cartTotal;
    @FXML
    private Pane confirmDialog;
    @FXML
    private FlowPane favoriteCarousel;
    @FXML
    private ScrollPane favoritePane;
    @FXML
    private FlowPane resultList;
    @FXML
    private AnchorPane resultPage;
    @FXML
    private Label resultText;
    public ArrayList<ProductCard> products = new ArrayList<>();
    public ArrayList<ProductCard> pasta_potatis_ris = new ArrayList<>();
    public ArrayList<ProductCard> frukt_gront = new ArrayList<>();
    public ArrayList<ProductCard> kott_fisk = new ArrayList<>();
    public ArrayList<ProductCard> dryck = new ArrayList<>();
    public ArrayList<ProductCard> skafferi = new ArrayList<>();
    public ArrayList<ProductCard> mejeri = new ArrayList<>();
    public ArrayList<ProductCard> notter_fron = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                dataHandler.shutDown();
            }
        }));
        inFront = "start";
        setBackground();
        updateCart();
        for (String category : categories) {
            categoryList.getChildren().add(new CategoryListItem(category, this));
        }
        for (Product product : dataHandler.getProducts()) {
            products.add(new ProductCard(dataHandler, this, product));
        }

        for (Product favorite : dataHandler.favorites()) {
            favoriteCarousel.getChildren().add(new ProductCard(dataHandler, this, favorite));

        }

        fillCategories(dataHandler.getProducts(ProductCategory.PASTA), 1);
        fillCategories(dataHandler.getProducts(ProductCategory.POTATO_RICE), 1);

        fillCategories(dataHandler.getProducts(ProductCategory.BERRY), 2);
        fillCategories(dataHandler.getProducts(ProductCategory.CABBAGE), 2);
        fillCategories(dataHandler.getProducts(ProductCategory.CITRUS_FRUIT), 2);
        fillCategories(dataHandler.getProducts(ProductCategory.EXOTIC_FRUIT), 2);
        fillCategories(dataHandler.getProducts(ProductCategory.FRUIT), 2);
        fillCategories(dataHandler.getProducts(ProductCategory.MELONS), 2);
        fillCategories(dataHandler.getProducts(ProductCategory.ROOT_VEGETABLE), 2);
        fillCategories(dataHandler.getProducts(ProductCategory.VEGETABLE_FRUIT), 2);
        fillCategories(dataHandler.getProducts(ProductCategory.POD), 2);

        fillCategories(dataHandler.getProducts(ProductCategory.COLD_DRINKS), 3);
        fillCategories(dataHandler.getProducts(ProductCategory.HOT_DRINKS), 3);

        fillCategories(dataHandler.getProducts(ProductCategory.MEAT), 4);
        fillCategories(dataHandler.getProducts(ProductCategory.FISH), 4);

        fillCategories(dataHandler.getProducts(ProductCategory.NUTS_AND_SEEDS), 5);

        fillCategories(dataHandler.getProducts(ProductCategory.HERB), 6);
        fillCategories(dataHandler.getProducts(ProductCategory.SWEET), 6);
        fillCategories(dataHandler.getProducts(ProductCategory.FLOUR_SUGAR_SALT), 6);
        fillCategories(dataHandler.getProducts(ProductCategory.BREAD), 6);

        fillCategories(dataHandler.getProducts(ProductCategory.DAIRIES), 7);

    }

    public void acceptClear() {
        confirmDialog.setDisable(true);
        confirmDialog.setVisible(false);
        dataHandler.getShoppingCart().clear();
        updateCartTotal();
        updateCart();
        populateCart();
    }

    public void cancelClear() {
        confirmDialog.setDisable(true);
        confirmDialog.setVisible(false);
    }

    public void confirmClear() {
        confirmDialog.setDisable(false);
        confirmDialog.setVisible(true);
    }

    public void addToFavorites(Product product) {
        boolean alreadyPresent = false;
        for (String name : stringFavoriteList) {
            if (product.getName() == name) {
                alreadyPresent = true;
                break; //an optimisation but not strictly necessary //absolut inte tagit från stack overflow
            }
        }
        if (!alreadyPresent) favoriteList.getChildren().add(new ProductCard(dataHandler, this, product));
    }

    public void updateFavorites() {
        favoriteList.getChildren().clear();
        for (Product favorite : dataHandler.favorites()) {
            favoriteList.getChildren().add(new ProductCard(dataHandler, this, favorite));
        }
    }

    private void clearCategory() {

        for (Node item : categoryList.getChildren()) {
            ((CategoryListItem) item).setActiveCategory("none");
        }

    }

    public void updateCartTotal() {
        cartTotal.setText(Math.round(dataHandler.getShoppingCart().getTotal()) + ":-");
    }

    private void setBackground() {

        clearCategory();
        switch (inFront) {
            case "start":
                home.getStyleClass().add("sideItem-pressed");
                favorites.getStyleClass().remove("sideItem-pressed");
                help.getStyleClass().remove("sideItem-pressed");
                break;
            case "favorites":
                favorites.getStyleClass().add("sideItem-pressed");
                home.getStyleClass().remove("sideItem-pressed");
                help.getStyleClass().remove("sideItem-pressed");
                break;
            case "help":
                help.getStyleClass().add("sideItem-pressed");
                home.getStyleClass().remove("sideItem-pressed");
                favorites.getStyleClass().remove("sideItem-pressed");
                break;
            case "category":
                home.getStyleClass().remove("sideItem-pressed");
                favorites.getStyleClass().remove("sideItem-pressed");
                help.getStyleClass().remove("sideItem-pressed");
                break;
            case "cart":
                home.getStyleClass().remove("sideItem-pressed");
                favorites.getStyleClass().remove("sideItem-pressed");
                help.getStyleClass().remove("sideItem-pressed");
                break;
        }
    }

    public void updateCart() {
        kundvagn.setText("Kundvagn " + Math.round(dataHandler.getShoppingCart().getTotal()) + ":-");
    }

    private void populateCart() {
        //System.out.println("Cart is populated :D");
        shoppingCartItems.getChildren().clear();
        for (ShoppingItem product : dataHandler.getShoppingCart().getItems()) {
            shoppingCartItems.getChildren().add(new CartItem(dataHandler, this, product));
        }
    }


    @FXML
    private void toStartPage() {
        refreshProductList();
        favoriteCarousel.getChildren().clear();
        for (Product favorite : dataHandler.favorites()) {
            favoriteCarousel.getChildren().add(new ProductCard(dataHandler, this, favorite));

        }
        startPage.toFront();
        cartPage.toBack();
        checkoutPage.toBack();
        inFront = "start";
        setBackground();
    }

    @FXML
    private void toFavorites() {
        updateFavorites();
        favoritePage.toFront();
        inFront = "favorites";
        setBackground();
    }

    @FXML
    private void toHelp() {
        updateFavorites();
        helpPage.toFront();
        inFront = "help";
        setBackground();
    }

    @FXML
    private void toCart() {
        inFront = "cart";
        populateCart();
        updateCartTotal();
        checkoutPage.toBack();
        cartPage.toFront();
    }

    @FXML
    private void toCheckout() {
        checkoutPage.toFront();
    }

    @FXML
    private void backFromCart() {
        cartPage.toBack();
    }

    @FXML
    private void shopStepOne() {
        stepOne.toFront();
    }

    @FXML
    private void shopStepTwo() {
        stepTwo.toFront();
    }

    @FXML
    private void shopStepThree() {
        stepThree.toFront();
    }

    public void search(Event event) {
        List<Product> result = dataHandler.findProducts(event.getSource().toString());
    }


    private void refreshProductList() {
        for (Node product : productList.getChildren()) {
            ((ProductCard) product).updateFavoriteIcon();
        }
    }

    public void setCategory(String category) {
        categoryName.setText(category.toUpperCase());
        categoryName.setStyle("-fx-font-family: 'Lexend Deca Bold'");
        categoryProductList.getChildren().clear();
        populateCategoryList(category.toLowerCase());
        inFront = "category";
        setBackground();
        categoryPage.toFront();
        for (Node item : categoryList.getChildren()) {
            ((CategoryListItem) item).setActiveCategory(category.toLowerCase());
        }
    }

    @FXML
    public void clickedCategoryImage(Event event){
        setCategory(((Label) event.getSource()).getText());

    }

    public void populateCategoryList(String category) {

        switch (category) {
            case "alla produkter":
                showProducts(0);
                break;
            case "pasta, potatis & ris":
                showProducts(1);
                break;
            case "frukt & grönt":
                showProducts(2);
                break;
            case "dryck":
                showProducts(3);
                break;
            case "kött & fisk":
                showProducts(4);
                break;
            case "nötter & frön":
                showProducts(5);
                break;
            case "skafferi":
                showProducts(6);
                break;
            case "mejeri":
                showProducts(7);
                break;

        }

    }

    private void showProducts(int category) {
        ArrayList<ProductCard> items;
        switch (category) {
            case 1:
                items = pasta_potatis_ris;
                break;
            case 2:
                items = frukt_gront;
                break;
            case 3:
                items = dryck;
                break;
            case 4:
                items = kott_fisk;
                break;
            case 5:
                items = notter_fron;
                break;
            case 6:
                items = skafferi;
                break;
            case 7:
                items = mejeri;
                break;
            default:
                items = products;
                break;
        }
        for (ProductCard card : items) {
            categoryProductList.getChildren().add(card);
        }
    }

    public void fillCategories(List<Product> products, int category) {
        for (Product product : products) {
            switch (category) {
                case 1:
                    pasta_potatis_ris.add(new ProductCard(dataHandler, this, product));
                    break;
                case 2:
                    frukt_gront.add(new ProductCard(dataHandler, this, product));
                    break;
                case 3:
                    dryck.add(new ProductCard(dataHandler, this, product));
                    break;
                case 4:
                    kott_fisk.add(new ProductCard(dataHandler, this, product));
                    break;
                case 5:
                    notter_fron.add(new ProductCard(dataHandler, this, product));
                    break;
                case 6:
                    skafferi.add(new ProductCard(dataHandler, this, product));
                    break;
                case 7:
                    mejeri.add(new ProductCard(dataHandler, this, product));
                    break;
            }

        }

    }

    @FXML
    public void checkSearch(KeyEvent event) {

        if (event.getCode().equals(KeyCode.ENTER)) {
            performSearch();
        }
        if (searchField.getText().isEmpty()) {
            searchButton.setStyle("-fx-opacity: 0");
        } else {
            searchButton.setStyle("-fx-opacity: 1");
        }
    }

    public void performSearch() {
        List<Product> matches = dataHandler.findProducts(searchField.getText());

        if (matches.isEmpty()) {
            noResultText.setText("Inga resultat för: " + '"' + searchField.getText() + '"');
            noResult.toFront();
        } else {
            resultList.getChildren().clear();
            for (Product product : matches) {
                resultList.getChildren().add(new ProductCard(dataHandler, this, product));
            }
            resultText.setText("Resultat för: " + '"' + searchField.getText() + '"');
            resultPage.toFront();
        }

    }

    public void performSearch1() {
        List<Product> matches = dataHandler.findProducts(searchField1.getText());

        if (matches.isEmpty()) {
            noResult.toFront();
        } else {
            resultList.getChildren().clear();
            for (Product product : matches) {
                resultList.getChildren().add(new ProductCard(dataHandler, this, product));
            }
            resultText.setText("Resultat för: " + '"' + searchField1.getText() + '"');
            resultPage.toFront();
        }

    }

    static void scrollToLeft(ScrollPane scrollPane) {
        Animation animation = new Timeline(
                new KeyFrame(Duration.millis(25),
                        new KeyValue(scrollPane.hvalueProperty(), 0)));
        animation.play();
    }

    static void scrollToRight(ScrollPane scrollPane) {
        Animation animation = new Timeline(
                new KeyFrame(Duration.millis(25),
                        new KeyValue(scrollPane.hvalueProperty(), 1)));
        animation.play();
    }

    @FXML
    public void swipeRight(){
        scrollToRight(favoritePane);

    }
    @FXML
    public void swipeLeft(){
        scrollToLeft(favoritePane);
    }

}
