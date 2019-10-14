package myPack;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import java.io.File;
import java.net.MalformedURLException;

class MPlayer {
    private  MediaPlayer mediaPlayer ;
    private  MediaView mv;
    MPlayer(File file, AnchorPane pContainer,Slider time) throws MalformedURLException {try{
        Media media = new Media(file.toURI().toURL().toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mv = new MediaView(mediaPlayer);
        mv.setPreserveRatio(false);
        mv.fitWidthProperty().bind(pContainer.widthProperty());
        mv.fitHeightProperty().bind(pContainer.heightProperty());
        pContainer.getChildren().removeAll();
        pContainer.getChildren().add(mv);
        mediaPlayer.setAutoPlay(true);}catch(NullPointerException ignored){}
        mediaPlayer.currentTimeProperty().addListener(ov -> updatesValues(time));
        time.valueProperty().addListener(ov -> {
            if (time.isPressed()) {
                mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(time.getValue() / 100));
            }
        });
    }

    protected void updatesValues(Slider time)
    {
        Platform.runLater(() -> time.setValue(mediaPlayer.getCurrentTime().toMillis()/
                mediaPlayer.getTotalDuration().toMillis()* 100));
    }

    public MediaView getMv() {
        return mv;
    }

    MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
