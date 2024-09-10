package com.example.pong_project.View;


import com.example.pong_project.Model.*;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.List;

/**
 * The RacketThreadHandler class handles the movement of the rackets and the thread that moves them.
 */
public class RacketThreadHandler implements EventHandler<KeyEvent> {
    boolean racket1Up = false;
    boolean racket1Down = false;
    boolean racket2Up = false;
    boolean racket2Down = false;
    boolean endThread = false;
    public double moveRacket1 = 0;
    public double moveRacket2 = 0;
    private final int speed = 2;

    /**
     * the moveRacket method handles the movement of the rackets and threading.
     * @param scene - the scene
     * @param p1Racket - the player 1 racket
     * @param p2Racket - the player 2 racket
     */
    public void moveRacket(Scene scene, Racket p1Racket, Racket p2Racket) {
        Thread racketThread = new Thread(() -> {
            scene.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.W){
                    System.out.println("W Pressed");
                    racket1Up = true;
                }
                else if (keyEvent.getCode() == KeyCode.S){
                    System.out.println("S Pressed");
                    racket1Down = true;
                }
                else if (keyEvent.getCode() == KeyCode.UP){
                    System.out.println("UP Pressed");
                    racket2Up = true;
                }
                else if (keyEvent.getCode() == KeyCode.DOWN){
                    System.out.println("DOWN Pressed");
                    racket2Down = true;
                }
            });

            scene.setOnKeyReleased(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.W){
                    racket1Up = false;
                }
                else if (keyEvent.getCode() == KeyCode.S){
                    racket1Down = false;
                }
                else if (keyEvent.getCode() == KeyCode.UP){
                    racket2Up = false;
                }
                else if (keyEvent.getCode() == KeyCode.DOWN){
                    racket2Down = false;
                }
                moveRacket1 = 0;
                moveRacket2 = 0;
            });

            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now){
                    if(racket1Up){
                        moveRacket1 = -speed;
                    }
                    else if(racket1Down){
                        moveRacket1 = speed;
                    }
                    if(racket2Up){
                        moveRacket2 = -speed;
                    }
                    else if(racket2Down){
                        moveRacket2 = speed;
                    }
                }
            };
            timer.start();

            while (!endThread){

                Platform.runLater(() -> {
                    p1Racket.setY((int) (p1Racket.getRacket().getY() + moveRacket1));
                    p2Racket.setY((int) (p2Racket.getRacket().getY() + moveRacket2));
                    if (p1Racket.getRacket().getY() <0){
                        p1Racket.getRacket().setY(0);
                    }
                    if (p1Racket.getRacket().getY() > scene.getHeight() - p1Racket.getRacket().getHeight()){
                        p1Racket.getRacket().setY(scene.getHeight() - p1Racket.getRacket().getHeight());
                    }
                    if (p2Racket.getRacket().getY() <0){
                        p2Racket.getRacket().setY(0);
                    }
                    if (p2Racket.getRacket().getY() > scene.getHeight() - p2Racket.getRacket().getHeight()){
                        p2Racket.getRacket().setY(scene.getHeight() - p2Racket.getRacket().getHeight());
                    }
                });

                try{
                    Thread.sleep(10);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        racketThread.start();
        System.out.println("Racket Thread Started");
    }

    /**
     * the handle method handles the key events
     * @param event - the key event
     */
    @Override
    public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.W){
            racket1Up = true;
        }
        else if (event.getCode() == KeyCode.S){
            racket1Down = true;
        }
        else if (event.getCode() == KeyCode.UP){
            racket2Up = true;
        }
        else if (event.getCode() == KeyCode.DOWN){
            racket2Down = true;
        }
    }

    /**
     * the stopRacketThread method stops the racket thread
     */
    public void stopRacketThread(){
        endThread = true;
    }
}

