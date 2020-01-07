package watki;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Klasa tworzy scenę oraz zawiera metodę main().
 */

public class Main extends Application {

    private Playground playground;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("watki.fxml"));
        Parent root=loader.load();

        //Parent root = FXMLLoader.load(getClass().getResource("watki.fxml"));
        //primaryStage.setTitle("Hello World");

        primaryStage.setTitle("Bardzo dobry program");

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        playground=loader.getController();  //pobieram kontroler z loadera
        playground.init();  //inicjalizuję kontroler
    }


    public static void main(String[] args) {
        launch(args);
    }
}
