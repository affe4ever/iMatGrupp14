import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;


//Kommer
public class iMatApplication extends Application{
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("iMatHomePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Font.loadFont(getClass().getResourceAsStream("/resources/LexendDeca-Bold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/resources/LexendDeca-SemiBold.ttf"), 14);
        stage.setTitle("iMat");
        stage.setScene(scene);
        stage.show();


    }




    public static void main(String[] args){
        launch();


    }



}