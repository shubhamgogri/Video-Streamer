package com.example.videostreaming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private String vid8 = "https://media.w3.org/2010/05/sintel/trailer.mp4";
    private String vid9 = "https://videolinks.com/pub/media/videolinks/video/dji.osmo.action.mp4";
    private String vid10 = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4";
    private String vid11 = "https://buildappswithpaulo.com/videos/outro_music.mp4";
    private DataSource.Factory dataSourceFactory;

    @Override
    protected void onStart() {
        super.onStart();
        if (isNetworkAvailable()){
            Toast.makeText(MainActivity.this, "Connected to internet",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this, "Connect to Internet for Better Experience",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.playerview);
        listView = findViewById(R.id.listView);

        String[] videos = {
                vid8,vid9,vid10,vid11
        };

        final ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, videos);
        Log.d("List", "onCreate: "+ list);

        dataSourceFactory = new
                DefaultDataSourceFactory(MainActivity.this, Util.getUserAgent(MainActivity.this,
                getString(R.string.app_name)));

        arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_activated_1, list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (isNetworkAvailable()){
                    Log.d("List", "onItemClick: " + list.get(position));
                    if (player!=null)
                        player.stop();

                    MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(list.get(position)));

                    player = ExoPlayerFactory.newSimpleInstance(MainActivity.this,new DefaultTrackSelector());
                    player.prepare(mediaSource);

                    playerView.setPlayer(player);
                }else{
                    Toast.makeText(MainActivity.this, "Internet is Unavailable", Toast.LENGTH_SHORT).show();
                }

            }
         });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playerView.setPlayer(null);
        player.release();
        player = null;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
