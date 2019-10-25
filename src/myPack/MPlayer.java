package myPack;

import javafx.application.Platform;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class MPlayer {
    private MediaPlayer mediaPlayer;
    private MediaView mv;

    MPlayer(File file, AnchorPane pContainer, Slider time, Label start, Label end) throws MalformedURLException {
        try {
            Media media = new Media(file.toURI().toURL().toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mv = new MediaView(mediaPlayer);
            mv.setPreserveRatio(false);
            mv.fitWidthProperty().bind(pContainer.widthProperty());
            mv.fitHeightProperty().bind(pContainer.heightProperty());
            pContainer.getChildren().removeAll();
            pContainer.getChildren().add(mv);
            mediaPlayer.setAutoPlay(true);
        } catch (NullPointerException ignored) {
        }
        mediaPlayer.currentTimeProperty().addListener(ov -> {
            try {
                int ints = (int) mediaPlayer.getCurrentTime().toSeconds();
                String s = String.format("%06d", ints);
                DateFormat sdf = new SimpleDateFormat("hhmmss");
                Date parsedate = sdf.parse(s);
                String format = new SimpleDateFormat("HH:mm:ss").format(parsedate);
                start.textProperty().bind(Bindings.createStringBinding(() -> format));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            updatesValues(time);
        });
        time.valueProperty().addListener(ov -> {
            if (time.isPressed()) {
                mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(time.getValue() / 100));
            }
        });

    }

    protected void updatesValues(Slider time) {
        Platform.runLater(() -> time.setValue(mediaPlayer.getCurrentTime().toMillis() /
                mediaPlayer.getTotalDuration().toMillis() * 100));
    }

    public MediaView getMv() {
        return mv;
    }

    MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
