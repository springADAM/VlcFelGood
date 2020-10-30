package myPack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Main extends Application {

    static Stage pstage;
    static Scene pscene;

    @Override
    public void start(Stage primaryStage) throws Exception{
//Made reference to the stage to use later for changing scene
        pstage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setTitle("VlcFelGood");
        Scene s = new Scene(root);
        pscene = s;
        s.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.SPACE)pausePlay();
        });
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("ico.png")));
        primaryStage.setScene(s);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
    private void pausePlay() {
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
