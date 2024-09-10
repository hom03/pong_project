package com.example.pong_project.Model;

/**
 * GameObject superclass for all objects in the game
 * abstracts the x and y coordinates
 */
public abstract class GameObject {

    //-------------
    //Methods
    //--------------
    protected int x;
    protected int y;

    //-------------
    //Constructor
    //-------------

    /**
     * game object constructor describes the x and y coordinates.
     * @param x - x coordinate
     * @param y - y coordinate
     */
    public GameObject(int x, int y){
        this.x = x;
        this.y = y;
    }

    //-------------
    // Getters
    //-------------

    /**
     * get x coordinate
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * get y coordinate
     * @return y
     */
    public int getY() {
        return y;
    }

    //------------
    // Setters
    //------------

    /**
     * set x coordinate
     * @param x - x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * set y coordinate
     * @param y - y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }
}
