package com.example.doodlerocket.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.doodlerocket.GameObjects.SoundManager;
import com.example.doodlerocket.GameView;
import com.example.doodlerocket.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private Handler handler = new Handler();
    private Timer timer; //resume or stop GameView thread
    private final static long refreshRate = 10; //refresh rate

    private SharedPreferences sp;

    private SoundManager soundManager;

    //only need 1 instance of AlertDialog and then inflate it with other layouts
    private AlertDialog gameAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        soundManager = new SoundManager(this);

        //getting info to send GameView
        int skinID;
        int backgroundID;
        int currLvl;
        sp = getSharedPreferences("storage",MODE_PRIVATE);
        skinID = sp.getInt("skin_id", R.drawable.default_ship_100);
        backgroundID = sp.getInt("lvl_bg",R.drawable.stars_pxl_png);
        currLvl = sp.getInt("curr_lvl",1);

        //game is running on thread behind the scenes
        gameView = new GameView(this,skinID,backgroundID,currLvl,soundManager);
        setContentView(gameView); //content display

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        gameView.invalidate(); //refresh screen (repaint canvas)
                    }
                });
            }
        },0, refreshRate); //delay = 0, each 10mis refresh screen

        //listens to events in GameView
        gameView.setGameViewListener(new GameView.GameViewListener() {

            @Override
            public void pauseGame() {

                //stop GameView thread
                timer.cancel();

                //for each new dialog we create a builder to show this specific dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                //first you inflate the layout and then create the builder to show it
                View pauseView = getLayoutInflater().inflate(R.layout.alert_dialog_view, null);

                //alert animation
                YoYo.with(Techniques.BounceInUp).duration(1000).playOn(pauseView);

                Button yesBtn = pauseView.findViewById(R.id.alert_yes_btn);
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //kill intent without saving score
                        finish();
                    }
                });
                Button noBtn = pauseView.findViewById(R.id.alert_no_btn);
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gameAlertDialog.dismiss();
                        resumeGame();
                    }
                });

                //building the alert dialog each time with different builder
                gameAlertDialog = builder.setView(pauseView).show();
                gameAlertDialog.setCanceledOnTouchOutside(false);
                gameAlertDialog.setCancelable(false);
            }

            @Override
            public void resumeGame() {

                //need to create new Timer after it has been stopped
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                gameView.invalidate(); //refresh screen (repaint canvas)
                            }
                        });
                    }
                },0, refreshRate); //delay = 0, each 10mis refresh screen
            }

            @Override
            public void endGame(int score) {

                //stop invalidate
                timer.cancel();

                //release sounds
                soundManager.stopSfx();

                //move to game over page
                Intent gameOverIntent = new Intent(MainActivity.this, GameOverActivity.class);
                gameOverIntent.putExtra("score", score);
                startActivity(gameOverIntent);

                //kill intent
                finish();
            }
        });
    }

    @Override //alert dialog when back pressed
    public void onBackPressed() {

        //for each new dialog we create a builder to show this specific dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        //first you inflate the layout and then create the builder to show it
        View pauseView = getLayoutInflater().inflate(R.layout.alert_dialog_view, null);

        //alert animation
        YoYo.with(Techniques.BounceInUp).duration(1000).playOn(pauseView);

        Button yesBtn = pauseView.findViewById(R.id.alert_yes_btn);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button noBtn = pauseView.findViewById(R.id.alert_no_btn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameAlertDialog.dismiss();
            }
        });

        //building the alert dialog each time with different builder
        gameAlertDialog = builder.setView(pauseView).show();
        gameAlertDialog.setCanceledOnTouchOutside(false);
        gameAlertDialog.setCancelable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
