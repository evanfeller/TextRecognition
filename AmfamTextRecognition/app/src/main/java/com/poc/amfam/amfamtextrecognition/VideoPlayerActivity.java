package com.poc.amfam.amfamtextrecognition;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.URLUtil;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class VideoPlayerActivity extends AppCompatActivity {

    public VideoView vvMovieScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

//        String url = "https://www.youtube.com/watch?v=FpdKo6JnQJA";
//        getAssets().
//        String url = "android.resource://" + getPackageName() + "/" + R.raw.test_video;
        vvMovieScreen = (VideoView) findViewById(R.id.videoView);
        try {
            vvMovieScreen.setVideoURI(Uri.parse(getDataSource("file:///android_asset/test_video" +
                    ".mp4")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        vvMovieScreen.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                vvMovieScreen.start();
            }
        });
    }

    public static String getDataSource(String path) throws IOException {
        if (!URLUtil.isNetworkUrl(path)) {
            return path;
        } else {
            URL url = new URL(path);
            URLConnection cn = url.openConnection();
            cn.connect();
            InputStream stream = cn.getInputStream();
            if (stream == null)
                throw new RuntimeException("stream is null");
            File temp = File.createTempFile("mediaplayertmp", "dat");
            temp.deleteOnExit();
            String tempPath = temp.getAbsolutePath();
            FileOutputStream out = new FileOutputStream(temp);
            byte buf[] = new byte[128];
            do {
                int numread = stream.read(buf);
                if (numread <= 0)
                    break;
                out.write(buf, 0, numread);
            } while (true);
            try {
                stream.close();
                out.close();
            } catch (IOException ex) {
                //  Log.e(TAG, "error: " + ex.getMessage(), ex);
            }
            return tempPath;
        }
    }
}
