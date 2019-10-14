package myPack;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller  {

    @FXML
    public MenuItem open = new MenuItem();
    public Button pause = new Button();

    private static File file;
    private MPlayer m;
    private boolean isfullscreen = false;
    private boolean playerstate =false;

    @FXML  Label startTime = new Label();
    @FXML Label endTime = new Label();

  @FXML  MenuItem speed = new MenuItem();

    @FXML
    public Slider time = new Slider();
    public Slider vol = new Slider();
    @FXML
    AnchorPane pContainer = new AnchorPane();
    java.time.Duration strtTime;


    public void openFile() throws MalformedURLException {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Video files (*.mp4)", "*.mp4"), new FileChooser.ExtensionFilter("Audio files (*.mp3)", "*.mp3"));
            file = fileChooser.showOpenDialog(new Stage());
            m.getMediaPlayer().stop();
            m = new MPlayer(file, pContainer,time);
            slide();
        } catch (NullPointerException | MalformedURLException e) {
            m = new MPlayer(file, pContainer,time);
        }
    }

    @FXML
    AnchorPane FullAnchor = new AnchorPane();
    Scene temp = new Scene(FullAnchor);

    public void screen(MouseEvent event) {
        System.out.println(isfullscreen);
        if (event.getClickCount() == 2 && !isfullscreen) {
            MediaView fullm = m.getMv();
            fullm.setPreserveRatio(false);
            fullm.fitWidthProperty().bind(FullAnchor.widthProperty());
            fullm.fitHeightProperty().bind(FullAnchor.heightProperty());
            fullm.setOnMouseClicked(this::screen);
            FullAnchor.getChildren().add(fullm);
            temp.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    backtonormal();
                    isfullscreen=false;
                }
            });
            Main.pstage.setScene(temp);
            Main.pstage.setFullScreen(true);
            isfullscreen = true;
            event.consume();
        } else {
            if (event.getClickCount() == 2 && isfullscreen) {
                System.out.println("triggered");
                backtonormal();
                isfullscreen = false;
            }
        }
    }



    public void backtonormal() {
        m.getMv().fitWidthProperty().bind(pContainer.widthProperty());
        m.getMv().fitHeightProperty().bind(pContainer.heightProperty());
        pContainer.getChildren().removeAll();
        pContainer.getChildren().add(m.getMv());
        Main.pstage.setScene(Main.pscene);
    }

    @FXML
    public void pausePlay(){
        try{
            if(playerstate) {m.getMediaPlayer().pause(); playerstate =false;}
            else { m.getMediaPlayer().play(); playerstate = true;}}catch (NullPointerException e){}
    }

    @FXML public void fst () {
        m.getMediaPlayer().setRate(1.0);
    }
    @FXML public void fster () {
        m.getMediaPlayer().setRate(1.5);
    }






    public void slide() {

          time.valueProperty().addListener(ov -> {
           if (time.isPressed()) {
             m.getMediaPlayer().seek(m.getMediaPlayer().getMedia().getDuration().multiply(time.getValue() / 100));
       }



    });}
    @FXML
    public void changevol(){
        {
            vol.valueProperty().addListener(new InvalidationListener() {
                public void invalidated(Observable ov) {
                    if (vol.isPressed()) {
                        m.getMediaPlayer().setVolume(vol.getValue() / 100);
                    }
                }
            });

        }}
}
