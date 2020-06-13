package com.example.doodlerocket.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.doodlerocket.R;

public class LevelBlockThree extends AppCompatActivity {

    SharedPreferences sp;
    private int backgroundID;
    private int currLvl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_block_three_layout);

        //fixed portrait mode
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sp = getSharedPreferences("storage",MODE_PRIVATE);
        backgroundID = sp.getInt("lvl_bg",R.drawable.stars_pxl_png);


        TextView lvl5Tv = findViewById(R.id.lvl_5_tv);
        TextView lvl6Tv = findViewById(R.id.lvl_6_tv);


        //lvl 3
        final ImageView lvl5Btn = findViewById(R.id.lvl_5_btn);
        lvl5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //lvl background
                backgroundID = R.drawable.ocean_bg_1;
                currLvl = 5;

                //time entry to lvl
                Intent intent = new Intent(LevelBlockThree.this,MainActivity.class);
                startActivity(intent);

            }
        });

        //lvl 4
        final ImageView lvl6Btn = findViewById(R.id.lvl_6_btn);
        lvl6Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //put level background
                backgroundID = R.drawable.ice_bg_800;
                currLvl = 6;

                //time entry to lvl
                Intent intent = new Intent(LevelBlockThree.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //animation planets - YoYo
        YoYo.with(Techniques.ZoomInLeft).duration(1000).playOn(lvl5Btn);
        YoYo.with(Techniques.ZoomInLeft).duration(1000).playOn(lvl6Btn);

        //bounce infinite text
        ObjectAnimator bounceTextAnimator1 = new ObjectAnimator().ofFloat(lvl5Tv,"translationY",-70).setDuration(1400);
        ObjectAnimator bounceTextAnimator2 = new ObjectAnimator().ofFloat(lvl6Tv,"translationY",-70).setDuration(1400);

        bounceTextAnimator1.setRepeatMode(ValueAnimator.REVERSE);
        bounceTextAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        bounceTextAnimator2.setRepeatMode(ValueAnimator.REVERSE);
        bounceTextAnimator2.setRepeatCount(ValueAnimator.INFINITE);

        //animation planets
        ObjectAnimator animator1 = new ObjectAnimator().ofFloat(lvl5Btn,"translationY",-70).setDuration(1400);
        animator1.setRepeatMode(ValueAnimator.REVERSE);
        animator1.setRepeatCount(ValueAnimator.INFINITE);

        ObjectAnimator animator2 = new ObjectAnimator().ofFloat(lvl6Btn,"translationY",-70).setDuration(1400);
        animator2.setRepeatMode(ValueAnimator.REVERSE);
        animator2.setRepeatCount(ValueAnimator.INFINITE);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator1,animator2);
        set.playTogether(bounceTextAnimator1,bounceTextAnimator2);
        set.start();


        //next page
        ImageButton nextBlockBtn = findViewById(R.id.next_btn_block_3);
        nextBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelBlockThree.this,LevelBlockFour.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        //previous page
        ImageButton prevBlockBtn = findViewById(R.id.prev_btn_block_3);
        prevBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelBlockThree.this,LevelBlockTwo.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //put level background
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("lvl_bg",backgroundID);
        editor.putInt("curr_lvl",currLvl);
        editor.commit();
    }

    //on back pressed
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}