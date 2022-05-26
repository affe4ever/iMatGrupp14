import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;
import se.chalmers.cse.dat216.project.*;

import java.io.*;
import java.net.URL;
import java.security.Key;
import java.util.*;

public class iMatController implements Initializable {

    IMatDataHandler dataHandler = IMatDataHandler.getInstance();
    private final ArrayList<String> categories = new ArrayList<String>(Arrays.asList("Visa alla produkter", "Pasta, Potatis & Ris",
            "Skafferi & Kryddor","Frukt & Grönt", "Nötter & Frön", "Kött & Fisk", "Dryck", "Mejeri"));

    @FXML
    private ScrollPane startPage;
    @FXML
    private ImageView kontoLogo;
    @FXML
    private Label konto;
    @FXML
    private ImageView kundvagnLogo;

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
    private FlowPane categoryProductList;
    @FXML
    private ScrollPane categoryScroll;
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
    private AnchorPane accountPage;
    @FXML
    private Pane cartNotification;
    @FXML
    private Pane emptyPane;
    @FXML
    private Label resultText;
    @FXML
    private Button clearCart;
    @FXML
    private Button toCheckout;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField mailInput;
    @FXML
    private TextField numberInput;
    @FXML
    private TextField streetInput;
    @FXML
    private TextField postInput;
    @FXML
    private TextField ortInput;

    @FXML
    private TextField nameInput1;
    @FXML
    private TextField mailInput1;
    @FXML
    private TextField numberInput1;
    @FXML
    private TextField streetInput1;
    @FXML
    private TextField postInput1;
    @FXML
    private TextField ortInput1;
    @FXML
    private TextArea shoppingList;
    @FXML
    private FlowPane previousOrders;
    @FXML
    private Button saveAccount;
    @FXML
    private FlowPane checkoutCartList;
    @FXML
    private Label checkoutCartTotal;
    @FXML
    private ComboBox dayPicker;
    @FXML
    private ComboBox timePicker;
    @FXML
    private Label mailText;
    @FXML
    private AnchorPane thankYouPage;
    @FXML
    private Label nameError;

    @FXML
    private TextField cardInput;
    @FXML
    private TextField nameOnCard;
    @FXML
    private TextField cardDate;
    @FXML
    private TextField cvcCode;
    @FXML
    private AnchorPane erbjudande;
    @FXML
    private Button shoppingListButton;
    @FXML
    private AnchorPane whiteBlock;
    @FXML
    private Label errorMessage;
    @FXML
    private Label errorMessage1;
    @FXML
    private Label noChosen;

    public ArrayList<ProductCard> products = new ArrayList<>();
    public ArrayList<ProductCard> pasta_potatis_ris = new ArrayList<>();
    public ArrayList<ProductCard> frukt_gront = new ArrayList<>();
    public ArrayList<ProductCard> kott_fisk = new ArrayList<>();
    public ArrayList<ProductCard> dryck = new ArrayList<>();
    public ArrayList<ProductCard> skafferi = new ArrayList<>();
    public ArrayList<ProductCard> mejeri = new ArrayList<>();
    public ArrayList<ProductCard> notter_fron = new ArrayList<>();
    public ArrayList<ShoppingItem> cart_item = new ArrayList<>();


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

        for (Product favorite : dataHandler.favorites()){
            favoriteCarousel.getChildren().add(new ProductCard(dataHandler, this, favorite));

        }
        updateOrderHistory();

        Tooltip tooltip = new Tooltip("Gå till dryck");
        tooltip.setFont(Font.font("Lexend Deca Bold", 24));
        Tooltip.install(erbjudande, tooltip);
        dayPicker.getItems().addAll("Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag");
        timePicker.getItems().addAll("08:00 - 09:00", "10:00 - 11:00", "12:00 - 13:00", "14:00 - 15:00", "16:00 - 17:00");


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


        fillCategories(dataHandler.getProducts(ProductCategory.SWEET), 6);
        fillCategories(dataHandler.getProducts(ProductCategory.FLOUR_SUGAR_SALT), 6);
        fillCategories(dataHandler.getProducts(ProductCategory.BREAD), 6);
        fillCategories(dataHandler.getProducts(ProductCategory.HERB), 6);

        fillCategories(dataHandler.getProducts(ProductCategory.DAIRIES), 7);


        fillShoppingList();


    }

    private void updateOrderHistory(){
        previousOrders.getChildren().clear();
        dataHandler.getOrders().sort(Comparator.comparing(Order::getDate).reversed());
        for (Order order : dataHandler.getOrders()){
            previousOrders.getChildren().add(new PlacedOrder(dataHandler, this, order));
        }
    }

    @FXML
    private void checkNameInput(){

    }

    private void fillShoppingList(){
        try (BufferedReader reader = new BufferedReader(new FileReader("shoppingList.txt"))) {

            String line;
            while ((line = reader.readLine()) != null)
                shoppingList.appendText(line);

        } catch (IOException e) {
            e.printStackTrace();
        }
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


    public void notifyCart(){
        cartNotification.setVisible(true);
        Animation animation = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(cartNotification.opacityProperty(), 0.0)),
                new KeyFrame(Duration.millis(50),
                        new KeyValue(cartNotification.opacityProperty(), 0.2)),
                new KeyFrame(Duration.millis(100),
                        new KeyValue(cartNotification.opacityProperty(), 0.4)),
                new KeyFrame(Duration.millis(150),
                        new KeyValue(cartNotification.opacityProperty(), 0.6)),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(cartNotification.opacityProperty(), 0.8)),
                new KeyFrame(Duration.millis(250),
                        new KeyValue(cartNotification.opacityProperty(), 1)),
                new KeyFrame(Duration.millis(2000),
                        new KeyValue(cartNotification.opacityProperty(), 0.8)),
                new KeyFrame(Duration.millis(2050),
                        new KeyValue(cartNotification.opacityProperty(), 0.6)),
                new KeyFrame(Duration.millis(2100),
                        new KeyValue(cartNotification.opacityProperty(), 0.4)),
                new KeyFrame(Duration.millis(2150),
                        new KeyValue(cartNotification.opacityProperty(), 0.2)),
                new KeyFrame(Duration.millis(2200),
                        new KeyValue(cartNotification.opacityProperty(), 0.0)));
        animation.play();


    }

    public void updateCartTotal() {
        cartTotal.setText(Math.round(dataHandler.getShoppingCart().getTotal()*100.00)/100.00 + ":-");
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
            case "search":
            case "category":
            case "account":
            case "cart":
                home.getStyleClass().remove("sideItem-pressed");
                favorites.getStyleClass().remove("sideItem-pressed");
                help.getStyleClass().remove("sideItem-pressed");
                break;
        }
    }

    public void updateCart() {
        kundvagn.setText("Kundvagn " + Math.round(dataHandler.getShoppingCart().getTotal()*100.00)/100.00 + ":-");
    }

    private void populateCart() {
        //System.out.println("Cart is populated :D");
        shoppingCartItems.getChildren().clear();
        if (dataHandler.getShoppingCart().getItems().isEmpty()){
            emptyPane.setDisable(false);
            emptyPane.setVisible(true);
            clearCart.setDisable(true);
            toCheckout.setDisable(true);

        }else {
            emptyPane.setDisable(true);
            emptyPane.setVisible(false);
            clearCart.setDisable(false);
            toCheckout.setDisable(false);
            for (ShoppingItem product : dataHandler.getShoppingCart().getItems()) {
                        shoppingCartItems.getChildren().add(new CartItem(dataHandler, this, product));
                    }
        }
    }

    @FXML
    private void saveAccountSettings(){

        Animation animation = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(saveAccount.textProperty(), "SPARAT!")),
                new KeyFrame(Duration.millis(3000),
                        new KeyValue(saveAccount.textProperty(), "SPARA")));
        animation.play();
        dataHandler.getCustomer().setFirstName(nameInput.getText());
        dataHandler.getCustomer().setLastName(nameInput.getText());
        dataHandler.getCustomer().setAddress(streetInput.getText());
        dataHandler.getCustomer().setEmail(mailInput.getText());
        dataHandler.getCustomer().setPhoneNumber(numberInput.getText());
        dataHandler.getCustomer().setPostCode(postInput.getText());
        dataHandler.getCustomer().setPostAddress(ortInput.getText());

    }

    private void saveAccount1Settings(){
        dataHandler.getCustomer().setFirstName(nameInput1.getText());
        dataHandler.getCustomer().setLastName(nameInput1.getText());
        dataHandler.getCustomer().setAddress(streetInput1.getText());
        dataHandler.getCustomer().setEmail(mailInput1.getText());
        dataHandler.getCustomer().setPhoneNumber(numberInput1.getText());
        dataHandler.getCustomer().setPostCode(postInput1.getText());
        dataHandler.getCustomer().setPostAddress(ortInput1.getText());

    }

    @FXML
    private void saveShoppingList(){

        ObservableList<CharSequence> paragraph = shoppingList.getParagraphs();
        Iterator<CharSequence> iter = paragraph.iterator();
        try
        {
            BufferedWriter bf = new BufferedWriter(new FileWriter("shoppingList.txt"));
            while(iter.hasNext())
            {
                CharSequence seq = iter.next();
                bf.append(seq);
                bf.newLine();
            }
            bf.flush();
            bf.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Animation animation = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(shoppingListButton.textProperty(), "SPARAT!")),
                new KeyFrame(Duration.millis(3000),
                        new KeyValue(shoppingListButton.textProperty(), "SPARA")));
        animation.play();
    }

    private void refreshAccount(){
        nameInput.setText(dataHandler.getCustomer().getFirstName());
        streetInput.setText(dataHandler.getCustomer().getAddress());
        mailInput.setText(dataHandler.getCustomer().getEmail());
        numberInput.setText(dataHandler.getCustomer().getPhoneNumber());
        postInput.setText(dataHandler.getCustomer().getPostCode());
        ortInput.setText(dataHandler.getCustomer().getPostAddress());
    }

    private void refreshAccount1(){
        nameInput1.setText(dataHandler.getCustomer().getFirstName());
        streetInput1.setText(dataHandler.getCustomer().getAddress());
        mailInput1.setText(dataHandler.getCustomer().getEmail());
        numberInput1.setText(dataHandler.getCustomer().getPhoneNumber());
        postInput1.setText(dataHandler.getCustomer().getPostCode());
        ortInput1.setText(dataHandler.getCustomer().getPostAddress());
    }

    private void refreshPayment(){
        cardInput.setText(dataHandler.getCreditCard().getCardNumber());
        nameOnCard.setText(dataHandler.getCreditCard().getHoldersName());
        cardDate.setText(dataHandler.getCreditCard().getValidMonth() + "");
        cvcCode.setText(dataHandler.getCreditCard().getVerificationCode() + "");
    }

    @FXML
    private void toAccount(){
        refreshAccount();
        inFront = "account";
        setBackground();
        accountPage.toFront();
        cartPage.toBack();
        checkoutPage.toBack();
        whiteBlock.toBack();

    }

    @FXML
    private void toStartPage() {
        startPage.setVvalue(0);
        favoriteCarousel.getChildren().clear();
        for (Product favorite : dataHandler.favorites()) {
            favoriteCarousel.getChildren().add(new ProductCard(dataHandler, this, favorite));

        }
        startPage.toFront();
        whiteBlock.toBack();
        cartPage.toBack();
        checkoutPage.toBack();
        thankYouPage.toBack();
        inFront = "start";
        setBackground();
        searchField.setVisible(true);
        searchButton.setVisible(true);
        kundvagnLogo.setVisible(true);
        kundvagn.setVisible(true);
        konto.setVisible(true);
        kontoLogo.setVisible(true);
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
    public void toCart() {
        inFront = "cart";
        populateCart();
        updateCartTotal();
        checkoutPage.toBack();
        whiteBlock.toFront();
        cartPage.toFront();

        setBackground();
        searchField.setVisible(true);
        searchButton.setVisible(true);
        kundvagnLogo.setVisible(true);
        kundvagn.setVisible(true);
        konto.setVisible(true);
        kontoLogo.setVisible(true);

    }

    private void updateCheckoutList(){
        checkoutCartList.getChildren().clear();
        checkoutCartTotal.setText(dataHandler.getShoppingCart().getTotal() + ":-");
        for (ShoppingItem item : dataHandler.getShoppingCart().getItems()){
            checkoutCartList.getChildren().add(new OrderListItem(dataHandler, this, item, "small"));

        }
    }

    @FXML
    private void toCheckout() {
        checkoutPage.toFront();
        updateCheckoutList();
        refreshAccount1();
        searchField.setVisible(false);
        searchButton.setVisible(false);
        kundvagnLogo.setVisible(false);
        kundvagn.setVisible(false);
        konto.setVisible(false);
        kontoLogo.setVisible(false);
        errorMessage.setVisible(false);
        errorMessage1.setVisible(false);
        noChosen.setVisible(false);
        stepOne.toFront();
        refreshPayment();


    }

    @FXML
    private void backFromCart() {
        cartPage.toBack();
        whiteBlock.toBack();
    }

    @FXML
    private void BackOne(){
        stepOne.toFront();
    }

    @FXML
    private void BackTwo(){
        stepTwo.toFront();
    }



    @FXML
    private void shopStepTwo() {
        errorMessage1.setVisible(false);
        saveAccount1Settings();
        if (dataHandler.isCustomerComplete()) {
            if (!mailInput1.getText().isEmpty()) {
                stepTwo.toFront();
                errorMessage.setVisible(false);
            }

            } else {
                errorMessage.setVisible(true);
            }

    }

    @FXML
    private void shopStepThree() {
        noChosen.setVisible(false);
        if (dayPicker.getSelectionModel().isEmpty() || timePicker.getSelectionModel().isEmpty()){
            noChosen.setVisible(true);
        }else{
            noChosen.setVisible(false);
            stepThree.toFront();
        }
    }

    public void search(Event event) {
        List<Product> result = dataHandler.findProducts(event.getSource().toString());
    }


    private void refreshProductList() {
        for (Node product : categoryProductList.getChildren()) {
            ((ProductCard) product).updateFavoriteIcon();

        }
    }

    public void setCategory(String category) {
        categoryScroll.setVvalue(0);
        categoryName.setText(category.toUpperCase());
        categoryName.setStyle("-fx-font-family: 'Lexend Deca Bold'");
        categoryProductList.getChildren().clear();
        resultList.getChildren().clear();
        populateCategoryList(category.toLowerCase());
        refreshProductList();
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
            case "skafferi & kryddor":
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
        inFront = "search";
        setBackground();
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
            cartPage.toBack();
            checkoutPage.toBack();
            thankYouPage.toBack();
        }

    }

    public void performSearch1() {
        List<Product> matches = dataHandler.findProducts(searchField1.getText());
        inFront = "search";
        setBackground();
        if (matches.isEmpty()) {
            noResult.toFront();
        } else {
            resultList.getChildren().clear();
            for (Product product : matches) {
                resultList.getChildren().add(new ProductCard(dataHandler, this, product));
            }
            resultText.setText("Resultat för: " + '"' + searchField1.getText() + '"');
            resultPage.toFront();
            cartPage.toBack();
            checkoutPage.toBack();
            thankYouPage.toBack();
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

    private boolean checkEmpty(String string, String string2, String string3, String string4){
        return (string.isEmpty() || string2.isEmpty() || string3.isEmpty() || string4.isEmpty());
    }

    @FXML
    public void completePurchase(){
        if (checkEmpty(cardInput.getText(), nameOnCard.getText(), cardDate.getText(), cvcCode.getText())) {
            errorMessage1.setVisible(true);
        }else {
            errorMessage1.setVisible(false);
            dataHandler.placeOrder();
            mailText.setText(mailInput1.getText());
            cartPage.toBack();
            checkoutPage.toBack();
            thankYouPage.toFront();
            updateCart();
            updateOrderHistory();
            dataHandler.getCreditCard().setCardNumber(cardInput.getText());
            dataHandler.getCreditCard().setHoldersName(nameOnCard.getText());
            dataHandler.getCreditCard().setValidMonth(Integer.valueOf(cardDate.getText()));
            dataHandler.getCreditCard().setVerificationCode(Integer.valueOf(cvcCode.getText()));
        }

    }

    public void setCartItem(CartItem item, int amount){

        item.getItem().setAmount((int) amount);
        updateCartTotal();
        updateCart();
        populateCart();
    }

    public void addCartItem(CartItem item){
        item.getItem().setAmount(item.getItem().getAmount() + 1);
        updateCartTotal();
        updateCart();
        populateCart();
    }

    public void removeCartItem(CartItem item){
        dataHandler.getShoppingCart().removeItem(item.getItem());
        updateCartTotal();
        updateCart();
        populateCart();

    }

    public void subCartItem(CartItem item){
        item.getItem().setAmount(item.getItem().getAmount() - 1);
        updateCartTotal();
        updateCart();
        populateCart();

    }

    @FXML
    public void toOffer(){
        setCategory("Dryck");
    }



}
