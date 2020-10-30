package myPack;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
//Declaring buttons for video player app
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


    public void openFile() {
//Main function that reads the file
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Video files (*.mp4)", "*.mp4"), new FileChooser.ExtensionFilter("Audio files (*.mp3)", "*.mp3"));
            file = fileChooser.showOpenDialog(new Stage());
            addToRecently(file);
            m.getMediaPlayer().stop();
            m = new MPlayer(file, pContainer, time, startTime, endTime);
            Main.pstage.setTitle(file.getName());
        } catch (NullPointerException e) {
            try{
            m = new MPlayer(file, pContainer, time, startTime, endTime);
            Main.pstage.setTitle(file.getName() + " - VLC fel good");}catch(NullPointerException ignored){}
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
                if (e.getCode() == KeyCode.SPACE) pausePlay();
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
    void about() {
        JOptionPane.showMessageDialog(null, "Made with love and Java \n          by RaniAdam");
    }

    @FXML
    void speed() {
        m.getMediaPlayer().setRate(1.5);
    }

    @FXML
    void normal() {
        m.getMediaPlayer().setRate(1.0);
    }

    @FXML
    void slow() {
        m.getMediaPlayer().setRate(0.75);
    }

    @FXML
    void slower() {
        m.getMediaPlayer().setRate(0.5);
    }

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pContainer.setOnDragOver(event -> {
            if (event.getGestureSource() != pContainer && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        pContainer.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            File f = db.getFiles().get(0);
            try {
                m.getMediaPlayer().stop();
                addToRecently(f);
                m = new MPlayer(f, pContainer, time, startTime, endTime);
                e.setDropCompleted(true);
                e.consume();
            } catch (NullPointerException ex) {
                addToRecently(f);
                m = new MPlayer(f, pContainer, time, startTime, endTime);
                e.setDropCompleted(true);
                e.consume();
            }
        });
    }

    private void addToRecently(File file) {
        MenuItem item = new MenuItem();
        item.setText(file.getPath());
        item.setOnAction(event -> {
            m.getMediaPlayer().stop();
            m = new MPlayer(file, pContainer, time, startTime, endTime);
            Main.pstage.setTitle(file.getName() + " - VLC fel good");
        });
        openRecently.getItems().add(item);
        Main.pstage.setTitle(file.getName() + " - VLC fel good");

    }
}
