package myPack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.MalformedURLException;

public class Controller {

    private static File file;
    private MPlayer m;
    private boolean isfullscreen = false;

    @FXML
    MenuItem open = new MenuItem();
    @FXML
    AnchorPane pContainer = new AnchorPane();

    public void openFile() throws MalformedURLException {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Video files (*.mp4)", "*.mp4"), new FileChooser.ExtensionFilter("Audio files (*.mp3)", "*.mp3"));
            file = fileChooser.showOpenDialog(new Stage());
            m.getMediaPlayer().stop();
            m = new MPlayer(file, pContainer);
        } catch (NullPointerException | MalformedURLException e) {
            m = new MPlayer(file, pContainer);
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


}
