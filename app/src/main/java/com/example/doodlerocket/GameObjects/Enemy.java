package com.example.doodlerocket.GameObjects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.print.PrinterId;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doodlerocket.R;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends Mob {

    private int x,y,speed,health;
    private int width, height;
    private int canvasW;
    private Bitmap enemyBitmap;
    private boolean movingLeft = true;

    //creating death effect
    private List<Bitmap> deathEffectList = new ArrayList<>();
    private int i = 0;

    public Enemy(int x, int y, int speed, int health, int canvasW, int enemySkinID, Resources resources) {
        super(x, y, speed,health); //base of mob

        //from father
        this.x = getObjectX();
        this.y = getObjectY();
        this.speed = getSpeed();
        //this.health = getHealth();

        //unique son
        this.canvasW  = canvasW;

        //assign enemy bitmap picture
        enemyBitmap = BitmapFactory.decodeResource(resources,enemySkinID);
        this.width = enemyBitmap.getWidth();
        this.height = enemyBitmap.getHeight();
        enemyBitmap = Bitmap.createScaledBitmap(enemyBitmap,width,height,false);

        //death effect - unique to enemy
        deathEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom1));
        deathEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom2));
        deathEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom3));
        deathEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom4));
        deathEffectList.add(BitmapFactory.decodeResource(resources,R.drawable.boom5));
    }

    @Override
    public void drawObject(Canvas canvas) {
        if(!this.isDead()) {
            canvas.drawBitmap(enemyBitmap,getObjectX(),getObjectY(),null);
        }
        else {
            canvas.drawBitmap(deathEffectList.get(i/5),(this.getObjectX()+enemyBitmap.getWidth()/2),(this.getObjectY()+enemyBitmap.getHeight()/2),null);
            i++;
            i=i%25;
        }
    }

    @Override
    public void updateLocation() {

        if(!this.isDead()) {

            moveVertical(speed/2);

            if(movingLeft) {
                moveHorizontal(x -= speed);
                if(getObjectX() + enemyBitmap.getWidth() < canvasW/4) {
                    movingLeft = false;
                }
            }
            else {
                moveHorizontal(x += speed);
                if(getObjectX() + enemyBitmap.getWidth() > canvasW - 100) {
                    movingLeft = true;
                }
            }
        }
        else {
            moveVertical(speed/4);
        }
    }

    @Override
    public int getObjectX() {
        return super.getObjectX();
    }

    @Override
    public int getObjectY() {
        return super.getObjectY();
    }

    @Override
    public int getHealth() {
        return super.getHealth();
    }

    @Override
    public void takeDamage(int dmg) {
        super.takeDamage(dmg); //take dmg
        moveVertical(-90); // bounce - unique to enemy
    }

    //to resize in GameView
    public int getEnemyHeight() {return height;}
    public int getEnemyWidth() {return width;}

    @Override
    public boolean isDead() {
        return super.isDead();
    }

    @Override
    public void setDead(boolean dead) {
        super.setDead(dead);
    }

    @Override
    public boolean collisionDetection(int playerX, int playerY, Bitmap playerBitmap) {

        if     (playerX < getObjectX() + (enemyBitmap.getWidth())
                && getObjectX() < (playerX + playerBitmap.getWidth()) //rocket between obstacle width
                && playerY < getObjectY() + (enemyBitmap.getHeight())
                && getObjectY() < (playerY + playerBitmap.getHeight())) { //rocket between obstacle height
            return true;
        }
        return false;
    }

    public Rect getCollisionShape() {
        return new Rect(getObjectX(),getObjectY(),getObjectX()+enemyBitmap.getWidth(),getObjectY()+enemyBitmap.getHeight());
    }
}
