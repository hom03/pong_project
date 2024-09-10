package com.example.pong_project.Model;

/**
 * This is the player class to create a player
 */
public class Player{

    //-------------
    //Fields
    //--------------
    private String playerName;
    private int score;

    //-------------
    //Constructor
    //-------------

    /**
     * The player constructor
     * @param playerName - the name of the player
     */
    public Player(String playerName){
        this.playerName = playerName;
        this.score = 0;
    }

    //-------------
    // Getters
    //-------------

    /**
     * gets the player's name
     * @return the player's name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * gets the player's score
     * @return the player's score
     */
    public int getScore() {
        return score;
    }

    //------------
    // Setters
    //------------

    /**
     * sets up the player's name
     * @param playerName - the player's name
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * sets the player's score
     * @param score - the player's score
     */
    public void setScore(int score) {
        this.score = score;
    }

}
