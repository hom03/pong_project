package com.example.pong_project.Model;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Ball class for the Pong game
 */
public class Ball extends GameObject{
    //-------------
    //Methods
    //--------------
    private double speedX;
    private double speedY;
    private int radius;
    private Circle ball;
    //-------------
    //Constructor
    //-------------

    /**
     * Ball constructor
     * @param x - x coordinate
     * @param y - y coordinate
     * @param radius - ball radius
     * @param speedX - ball x speed how fast the ball moves on x-axis
     * @param speedY - ball y speed how fast the ball moves on y-axis
     */
    public Ball(int x,int y,  int radius, double speedX,double speedY){
        super(x,y);
        this.radius = radius;
        this.ball = new Circle(radius*2, Color.ORANGE);
        this.speedX = speedX;
        this.speedY = speedY;
    }

    //-------------
    // Getters
    //-------------

    /**
     * get ball speed for x-axis
     * @return speedX
     */
    public double getSpeedX() {
        return speedX;
    }

    /**
     * get ball speed for y-axis
     * @return speedY
     */
    public double getSpeedY() {
        return speedY;
    }

    /**
     * get the radius of the ball
     * @return radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * get the ball
     * @return ball
     */
    public Circle getBall(){
        return ball;
    }
    //------------
    // Setters
    //------------

    /**
     * set ball speed
     * @param speedX - speed on x-axis
     * @param speedY - speed on y-axis
     */
    public void setBallSpeed(double speedX, double speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
    }

    /**
     * set the radius of the ball
     * @param radius - radius
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

}
