package com.example.pong_project.Model;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 * the racket class is used to create the racket object.
 * It extends the GameObject class.
 */
public class Racket extends GameObject{
    //-------------
    //Fields
    //--------------
    private float width;
    private float height;
    private boolean moveUp;
    private boolean moveDown;
    private Rectangle racket;

    //-------------
    //Constructor
    //-------------

    /**
     * The racket constructor.
     * @param x - x coordinate
     * @param y - y coordinate
     * @param width - width
     * @param height - height
     * @param color - colour
     */
    public Racket(int x,int y,float width,float height,Color color){
        super(x,y);
        this.width = width;
        this.height = height;
        this.racket = new Rectangle(width,height);
        this.racket.setFill(color);
    }

    //-------------
    // Getters
    //-------------

    /**
     * gets the width of the racket
     * @return the width
     */
    public float getWidth() {
        return width;
    }

    /**
     * gets the height of the racket
     * @return the height
     */
    public float getHeight() {
        return height;
    }
    //------------
    // Setters
    //------------

    /**
     * sets the width of the racket
     * @param width - the width
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * sets the height of the racket
     * @param height - the height
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * moves the racket up
     * @param moveUp - true or false
     */
    public void setMoveUp(boolean moveUp){
        this.moveUp = moveUp;
    }

    /**
     * moves the racket down
     * @param moveDown - true or false
     */
    public void setMoveDown(boolean moveDown){
        this.moveDown = moveDown;
    }

    /**
     * We get the racket object
     * @return the racket
     */
    public Rectangle getRacket(){
        return racket;
    }

}
