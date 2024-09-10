package com.example.pong_project.Controller;

import com.example.pong_project.Model.Player;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MenuController class for the menu scene
 */
public class MenuController {
    //--------------------------------
    //Fields
    //--------------------------------
    @FXML
    private Label welcomeText;
    @FXML
    private Button playButton;
    @FXML
    private Canvas gameCanvas;
    @FXML
    private TextField p1Name;
    @FXML
    private TextField p2Name;
    @FXML
    private Spinner<Integer> setScore;
    @FXML
    private Slider ballSpeedSlider;
    @FXML
    private Slider racketSlider;
    @FXML
    private Slider speedFreqSlider;
    @FXML
    private Label p1Label;
    @FXML
    private Label p2Label;
    private int ballSpeed;
    private int racketDims;
    private int speedFreqInt;
    private MediaPlayer mediaPlayer;
    private AtomicInteger p1Score;
    private AtomicInteger p2Score;

    Player player1 = new Player("");
    Player player2 = new Player("");
    Text player1NameText = new Text("");
    Text player2NameText = new Text("");
    String menuSound = "/sans.mp3";
    Media sound = new Media(getClass().getResource(menuSound).toString());
    MediaPlayer menuMusic;

    /**
     * onPlayButtonClick method for the play button
     * Stops the menu music.
     * Plays a button sound.
     * takes player names, racketSize, ballSpeed, and finalScore data and passes it to the game controller.
     * @throws IOException
     */
    @FXML
    protected void onPlayButtonClick() throws IOException {
        stopMenuTheme();
        String playBall = "/play_ball_wii_sports.mp3";
        Media sound = new Media(getClass().getResource(playBall).toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        String player1Name = p1Name.getText();
        String player2Name = p2Name.getText();
        int racketSize = (int) racketSlider.getValue();
        int ballSpeed = (int) ballSpeedSlider.getValue();
        int finalScore = setScore.getValue();

        saveGame(player1Name,player2Name,racketSize,ballSpeed,finalScore);

        if (player1 != null){
            player1.setPlayerName(player1Name);
        }
        if (player2 != null){
            player2.setPlayerName(player2Name);
        }

        if (player1Name.isEmpty() || player2Name.isEmpty()){
            System.out.println("Please enter names for both players.");
        } else{
            System.out.println("Starting game with players "+player1Name+" and "+player2Name+".");
            GameController gameController = new GameController();
            gameController.setPlayerNames(player1NameText,player2NameText);
            gameController.setRacketDims(racketSize);
            gameController.setBallSpeed(ballSpeed);
            gameController.setFinalScore(finalScore);
            Stage gameStage = new Stage();
            gameStage.setTitle("Playing Pong");
            Button exitButton = new Button("Exit");
            Scene gameScene = new Scene(new Group(gameCanvas));
            gameController.setGameCanvas(gameCanvas,exitButton,gameStage,gameScene,player1Name,player2Name);
            gameController.initializeGame(player1Name,player2Name);
            gameStage.setScene(gameScene);
            gameStage.show();
        }

    }

    /**
     * The initialize method for the menu scene
     * sets the size of the game canvas.
     * starts the menu music.
     */
    public void initialize(){
        gameCanvas = new Canvas(800,600);
        playMenuMusic();
    }

    /**
     * playMenuMusic method to start the menu music.
     */
    private void playMenuMusic(){
        menuMusic = new MediaPlayer(sound);
        menuMusic.play();
        menuMusic.setOnEndOfMedia(() -> menuMusic.seek(Duration.ZERO));
    }

    /**
     * stopMenuTheme method to stop the menu music.
     */
    private void stopMenuTheme(){
        if (menuMusic !=null){
            menuMusic.stop();
        }
    }

    /**
     * saveGame method to save the game data to a file using serialization.
     * @param player1Name - player 1 name
     * @param player2Name - player 2 name
     * @param racketSize - racket size
     * @param ballSpeed - ball speed
     * @param finalScore - final score
     * All this data is passed to the file when the game is started.
     */
    private void saveGame(String player1Name, String player2Name, int racketSize, int ballSpeed, int finalScore) {
        try (FileOutputStream fileOut = new FileOutputStream("savedGame.ser")) {
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(player1Name);
                out.writeObject(player2Name);
                out.writeObject(racketSize);
                out.writeObject(ballSpeed);
                out.writeObject(finalScore);
                out.close();
                System.out.println("Game saved successfully to file savedGame.ser");
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    /**
     * Load game method to deserialize the game data from a file.
     * If file is not found, returns null.
     * @return an object array containing the deserialized game data
     */
    private Object[] loadGame() {
        try (FileInputStream fileIn = new FileInputStream("savedGame.ser")) {
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object[] savedGame = {in.readObject(),in.readObject(),in.readObject(),in.readObject(),in.readObject()};
            in.close();
            return savedGame;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading game: " + e.getMessage());
            return new Object[]{null,null,null,null,null};
        }
    }
    @FXML
    private void onLoadGame() {
        Object[] savedGame = loadGame();

        if (savedGame != null) {
            String player1Name = (String) savedGame[0];
            String player2Name = (String) savedGame[1];
            int racketSize = (int) savedGame[2];
            int ballSpeed = (int) savedGame[3];
            int finalScore = (int) savedGame[4];
            p1Name.setText(player1Name);
            p2Name.setText(player2Name);
            racketSlider.setValue(racketSize);
            ballSpeedSlider.setValue(ballSpeed);
            setScore.getValueFactory().setValue(finalScore);
        }
    }
    /**
     * onExitClick method to exit the game.
     * stops the menu music.
     * plays an exit sound.
     * exits the game.
     */
    @FXML
    protected void onExitClick(){
        stopMenuTheme();
        String exitSound = "/bye_bye_marioDS.mp3";
        Media sound = new Media(getClass().getResource(exitSound).toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            Platform.exit();
        });
    }
}