package es.pmdm.filmoteca;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MoreActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private TextView txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        setTitle(R.string.more_activity_title);

        surfaceView = findViewById(R.id.surfaceView);
        txtStatus = findViewById(R.id.txtStatus);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        Button btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(v -> play());

        Button btnPause = findViewById(R.id.btnPause);
        btnPause.setOnClickListener(v -> pause());

        Button btnStop = findViewById(R.id.btnStop);
        btnStop.setOnClickListener(v -> stop());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initializeMediaPlayer();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseMediaPlayer();
    }

    private void initializeMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.video_info);

            if (mediaPlayer != null) {
                mediaPlayer.setDisplay(surfaceHolder);
                mediaPlayer.setScreenOnWhilePlaying(true);

                mediaPlayer.setOnCompletionListener(mp -> {
                    txtStatus.setText(R.string.video_finished);
                });
            }
        }
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void play() {
        if (mediaPlayer == null) {
            initializeMediaPlayer();
        }

        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            txtStatus.setText(R.string.video_playing);
        } else if (mediaPlayer != null) {
            txtStatus.setText(R.string.video_already_playing);
        }
    }

    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            txtStatus.setText(R.string.video_paused);
        } else {
            txtStatus.setText(R.string.video_not_playing);
        }
    }

    private void stop() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }

            mediaPlayer.release();
            mediaPlayer = null;

            txtStatus.setText(R.string.video_stopped);
        } else {
            txtStatus.setText(R.string.video_not_playing);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }
}