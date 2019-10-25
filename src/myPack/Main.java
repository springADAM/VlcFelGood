package myPack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Main extends Application {

    static Stage pstage;
    static Scene pscene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        pstage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setTitle("VlcFelGood");
        Scene s = new Scene(root);
        pscene = s;
        s.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.SPACE)pausePlay();
        });
        primaryStage.setScene(s);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
    public void pausePlay() {
        try {
            if (Controller.playerstate) {
                Controller.m.getMediaPlayer().pause();
                Controller.playerstate = false;
            } else {
                Controller.m.getMediaPlayer().play();
                Controller.playerstate = true;
            }
        } catch (NullPointerException ignored) {
        }
    }
}
