import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


//Kommer
public class iMatApplication extends Application{
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("iMatHomePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("iMat");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch();
    }
}