package myPack;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.MalformedURLException;

public class Controller {

    private static File file;
    private MPlayer m ;

    @FXML
    MenuItem open = new MenuItem();
    @FXML
    AnchorPane pContainer =new AnchorPane();

    public void openFile() throws MalformedURLException {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Video files (*.mp4)", "*.mp4"),new FileChooser.ExtensionFilter("Audio files (*.mp3)", "*.mp3"));
            file = fileChooser.showOpenDialog(new Stage());
            m.getMediaPlayer().stop();
            m = new MPlayer(file,pContainer);
        }catch (NullPointerException | MalformedURLException e){
            m = new MPlayer(file,pContainer);
        }
    }



}
