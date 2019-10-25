package myPack;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;


class MPlayer {
    private  MediaPlayer mediaPlayer ;
    private  MediaView mv;
    DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    MPlayer(File file, AnchorPane pContainer, Slider time, Label playTime) throws MalformedURLException {try{
        Media media = new Media(file.toURI().toURL().toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mv = new MediaView(mediaPlayer);
        mv.setPreserveRatio(false);
        mv.fitWidthProperty().bind(pContainer.widthProperty());
        mv.fitHeightProperty().bind(pContainer.heightProperty());
        pContainer.getChildren().removeAll();
        pContainer.getChildren().add(mv);
        mediaPlayer.setAutoPlay(true);}catch(NullPointerException ignored){}

        mediaPlayer.currentTimeProperty().addListener(ov -> {



            playTime.textProperty().bind(Bindings.createStringBinding(() -> String.valueOf(mediaPlayer.getCurrentTime().toMillis())));
            
            updatesValues(time);});
        time.valueProperty().addListener(ov -> {
            if (time.isPressed()) {
                mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(time.getValue() / 100));
            }

        });

    }

    private void updatesValues(Slider time)
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
