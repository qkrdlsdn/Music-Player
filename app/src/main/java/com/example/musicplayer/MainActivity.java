package com.example.musicplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;

    Button btn_play;
    ImageButton btn_next, btn_prev;
    TextView tv_song, tv_artist;
    com.airbnb.lottie.LottieAnimationView BackAnimation;
    ListView SongList;

    private int doubleClickFlag = 0;
    private final long CLICK_DELAY = 250;

    MediaPlayer mediaPlayer = new MediaPlayer();
    int pausePosition = 0;

    int play = 0;
    int[] playList = new int[]{R.raw.gongparipa_lets_fall_in_love_for_the_night, R.raw.billie_eilish_all_the_good_girls_go_to_hell};
    String[] playListName = new String[]{"Lets Fall in Love For the Night", "All the good girls go to hell"};
    String[] playListArtist = new String[]{"Gongparipa", "Billie Eilish"};

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            pausePosition = 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_next = (ImageButton) findViewById(R.id.btn_next);
        btn_prev = (ImageButton) findViewById(R.id.btn_prev);

        tv_song = (TextView) findViewById(R.id.tv_song);
        tv_artist = (TextView) findViewById(R.id.tv_artist);

        btn_play = (Button) findViewById(R.id.btn_play);

        SongList = (ListView) findViewById(R.id.SongList);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playListName);
        SongList.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        SongList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerLayout.closeDrawer(drawerView);
                play = position;
                playMusic();
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMusic();
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevMusic();

            }
        });

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            play++;
            pausePosition = 0;
            if(play < playList.length) {
                String song = "노래: " + playListName[play];
                String artist = "아티스트: " + playListArtist[play];

                BackAnimation.playAnimation();
                tv_song.setText(song);
                tv_artist.setText(artist);
            } else {
                play = 0;
                String song = "노래: " + playListName[play];
                String artist = "아티스트: " + playListArtist[play];

                BackAnimation.playAnimation();
                tv_song.setText(song);
                tv_artist.setText(artist);
            }
            mediaPlayer = MediaPlayer.create(MainActivity.this, playList[play]);
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(completionListener);
        }
    };

    public void playMusic() {
        String song = "노래: " + playListName[play];
        String artist = "아티스트: " + playListArtist[play];
        if (mediaPlayer.isPlaying()) {
            BackAnimation.cancelAnimation();
            btn_play.setText("재생");
            btn_play.setBackgroundResource(R.drawable.play_button);
            tv_song.setText(song);
            tv_artist.setText(artist);
            mediaPlayer.pause();
            pausePosition = mediaPlayer.getCurrentPosition();
        } else {
            BackAnimation.playAnimation();
            btn_play.setText("일시 정지");
            btn_play.setBackgroundResource(R.drawable.stop_button);
            tv_song.setText(song);
            tv_artist.setText(artist);
            if (pausePosition == 0) {
                mediaPlayer = MediaPlayer.create(MainActivity.this, playList[play]);
            } else {
                mediaPlayer.seekTo(pausePosition);
            }
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(completionListener);
        }
    }

    public void nextMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            pausePosition = 0;
        }

        play++;
        pausePosition = 0;

        if(play < playList.length) {
            String song = "노래: " + playListName[play];
            String artist = "아티스트: " + playListArtist[play];

            BackAnimation.playAnimation();
            tv_song.setText(song);
            tv_artist.setText(artist);
        } else {
            play = 0;
            String song = "노래: " + playListName[play];
            String artist = "아티스트: " + playListArtist[play];

            BackAnimation.playAnimation();
            tv_song.setText(song);
            tv_artist.setText(artist);
        }
        mediaPlayer = MediaPlayer.create(MainActivity.this, playList[play]);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(completionListener);
    }

    public void prevMusic() {
        doubleClickFlag++;
        Handler handler = new Handler();
        Runnable clickRunnable = new Runnable() {
            @Override
            public void run() {
                doubleClickFlag = 0;

                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                pausePosition = 0;

                String song = "노래: " + playListName[play];
                String artist = "아티스트: " + playListArtist[play];

                BackAnimation.playAnimation();
                tv_song.setText(song);
                tv_artist.setText(artist);
                mediaPlayer = MediaPlayer.create(MainActivity.this, playList[play]);
                mediaPlayer.start();

                mediaPlayer.setOnCompletionListener(completionListener);
            }
        };
        if(doubleClickFlag == 1) {
            handler.postDelayed(clickRunnable, CLICK_DELAY);
        } else if(doubleClickFlag == 2) {
            doubleClickFlag = 0;

            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }

            play--;
            pausePosition = 0;

            if(play >= 0) {
                String song = "노래: " + playListName[play];
                String artist = "아티스트: " + playListArtist[play];

                BackAnimation.playAnimation();
                tv_song.setText(song);
                tv_artist.setText(artist);
            } else {
                play = playList.length - 1;
                String song = "노래: " + playListName[play];
                String artist = "아티스트: " + playListArtist[play];

                BackAnimation.playAnimation();
                tv_song.setText(song);
                tv_artist.setText(artist);
            }
            mediaPlayer = MediaPlayer.create(MainActivity.this, playList[play]);
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(completionListener);
        }
    }
}