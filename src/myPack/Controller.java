package myPack;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;


public class Controller {

    @FXML
    public Button pause = new Button();
    public MenuItem open = new MenuItem();

    private static File file;
    static MPlayer m;
    private boolean isfullscreen = false;
     static boolean playerstate = true;

    @FXML
    Label startTime = new Label();
    @FXML
    Label endTime = new Label();


    @FXML
    public Slider time = new Slider();
    public Slider vol = new Slider();
    @FXML
    AnchorPane pContainer = new AnchorPane();


    public void openFile() throws MalformedURLException {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Video files (*.mp4)", "*.mp4"), new FileChooser.ExtensionFilter("Audio files (*.mp3)", "*.mp3"));
            file = fileChooser.showOpenDialog(new Stage());
            MenuItem item = new MenuItem();
            item.setText(file.getPath());
            item.setOnAction(event -> {
                m.getMediaPlayer().stop();
                try {
                    m = new MPlayer(file, pContainer, time, startTime, endTime);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Main.pstage.setTitle(file.getName() + " - VLC fel good");
            });
            openRecently.getItems().add(item);
            m.getMediaPlayer().stop();
            m = new MPlayer(file, pContainer, time, startTime, endTime);
            Main.pstage.setTitle(file.getName());
        } catch (NullPointerException | MalformedURLException e) {
            m = new MPlayer(file, pContainer, time, startTime, endTime);
            Main.pstage.setTitle(file.getName() + " - VLC fel good");
        }
    }

    @FXML
    AnchorPane FullAnchor = new AnchorPane();
    private Scene temp = new Scene(FullAnchor);

    @FXML
    Menu openRecently = new Menu();

    public void screen(MouseEvent event) {
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
                    isfullscreen = false;
                }
                if(e.getCode() ==KeyCode.SPACE)pausePlay();
            });
            Main.pstage.setScene(temp);
            Main.pstage.setFullScreen(true);
            isfullscreen = true;
            event.consume();
        } else {
            if (event.getClickCount() == 2) {
                backtonormal();
                isfullscreen = false;
            }
        }
    }


    private void backtonormal() {
        m.getMv().fitWidthProperty().bind(pContainer.widthProperty());
        m.getMv().fitHeightProperty().bind(pContainer.heightProperty());
        pContainer.getChildren().removeAll();
        pContainer.getChildren().add(m.getMv());
        Main.pstage.setScene(Main.pscene);
    }

    @FXML
    public void pausePlay() {
        try {
            if (playerstate) {
                m.getMediaPlayer().pause();
                playerstate = false;
            } else {
                m.getMediaPlayer().play();
                playerstate = true;
            }
        } catch (NullPointerException ignored) {
        }
    }

    @FXML
     void speed() {m.getMediaPlayer().setRate(1.5);}

    @FXML
     void normal() {m.getMediaPlayer().setRate(1.0);}
    @FXML
    void slow(){m.getMediaPlayer().setRate(0.75);}
    @FXML
    void slower(){m.getMediaPlayer().setRate(0.5);}
    @FXML
    void changevol() {
        {
            vol.valueProperty().addListener(ov -> {
                if (vol.isPressed()) {
                    m.getMediaPlayer().setVolume(vol.getValue() / 100);
                }
            });

        }
    }

    @FXML
    void quit() {
        System.exit(0);
    }
}
