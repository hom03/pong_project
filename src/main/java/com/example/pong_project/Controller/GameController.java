package com.example.pong_project.Controller;

import com.example.pong_project.Model.*;
import com.example.pong_project.View.*;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GameController class controls the game logic and the interaction between game components.
 */
public class GameController {
    //----------------------------------------------
    // Fields
    //----------------------------------------------
    private Canvas gameCanvas;
    private Player player1;
    private Player player2;
    private int ballSpeed = 5;
    private int racketDims;
    private int speedFreqInt;
    private MediaPlayer mediaPlayer;
    private Ball ball;
    private String player1Name;
    private String player2Name;
    private MediaPlayer gameTheme;
    private Button exitButton;
    private ImageView imageView;
    private Text player1NameText;
    private Text player2NameText;
    private Text player1ScoreText;
    private Text player2ScoreText;
    private RacketThreadHandler racketThreadHandler;
    private BallThreadHandler ballThreadHandler;
    private int finalScore;
    private AtomicInteger p1Score;
    private AtomicInteger p2Score;
    private Racket p1Racket;
    private Racket p2Racket;
    private Button pauseButton;
    private boolean isPaused = false;
    private Button resetButton;
    private StackPane goalPane;
    private Button dbCommit;
    private Button loadFromDB;

    /**
     * setGameCanvas method
     * Sets up the game canvas and the exit button
     * @param gameCanvas - the game canvas
     * @param exitButton - the exit button
     * @param gameStage - the game stage
     * @param gameScene - the game scene
     * @param player1Name - the player 1 name
     * @param player2Name - the player 2 name
     */
    public void setGameCanvas(Canvas gameCanvas, Button exitButton, Stage gameStage, Scene gameScene, String player1Name, String player2Name) {
        this.gameCanvas = gameCanvas;
        this.exitButton = exitButton;
        this.resetButton = new Button("🔁");
        this.pauseButton = new Button("⏸️");
        this.dbCommit = new Button("Save to DB");
        this.loadFromDB = new Button("Load from DB");

        imageView = new ImageView(new Image("/fd.png"));
        imageView.setPreserveRatio(true);
        player1NameText = new Text("Player 1: ");
        player1ScoreText = new Text();
        player2NameText = new Text("Player 2: ");
        player2ScoreText = new Text();
        p1Score = new AtomicInteger(0);
        p2Score = new AtomicInteger(0);

        player1NameText.setFill(Color.WHITE);
        player1ScoreText.setFill(Color.WHITE);
        player2NameText.setFill(Color.WHITE);
        player2ScoreText.setFill(Color.WHITE);
        player1ScoreText.setText(Integer.toString(p1Score.get()));
        player2ScoreText.setText(Integer.toString(p2Score.get()));


        p1Racket = new Racket(0, 0, 10, 50*racketDims, Color.BLUE);
        System.out.println("p1 Racket created");

        p2Racket = new Racket((int)gameCanvas.getWidth()/2,(int)(gameCanvas.getHeight()/2) , 10, 50*racketDims, Color.RED);
        System.out.println("p2 Racket created");

        racketThreadHandler = new RacketThreadHandler();
        racketThreadHandler.moveRacket(gameScene, p1Racket, p2Racket);

        if (ballThreadHandler != null){
            ballThreadHandler.stopBallThread();
        }
        ball = new Ball(0, 0, 10, 2, 2);
        System.out.println("Ball created");
        StackPane goalPane = new StackPane();
        ballThreadHandler = new BallThreadHandler();
        ballThreadHandler.moveBall(ball,gameScene.getWidth(),gameScene.getHeight(),p1Racket,p2Racket,
                p1Score,p2Score,finalScore,player1ScoreText,player2ScoreText,goalPane);

        GameLoop gameLoop = new GameLoop();
        gameLoop.start();

        HBox p1Hbox = new HBox();
        p1Hbox.setAlignment(Pos.TOP_CENTER);
        p1Hbox.setSpacing(10);
        p1Hbox.getChildren().addAll(player1NameText, player1ScoreText);

        HBox p2Hbox = new HBox();
        p2Hbox.setAlignment(Pos.TOP_CENTER);
        p2Hbox.setSpacing(10);
        p2Hbox.getChildren().addAll(player2NameText, player2ScoreText);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(p1Hbox, p2Hbox);

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonBox.setSpacing(10);
        buttonBox.getChildren().addAll(pauseButton, exitButton, resetButton, dbCommit, loadFromDB);


        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(imageView, vbox, ball.getBall(), p1Racket.getRacket(), p2Racket.getRacket(), buttonBox);
        gameStage.setResizable(true);
        StackPane.setAlignment(imageView, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(vbox, Pos.TOP_CENTER);
        StackPane.setMargin(p1Racket.getRacket(), new Insets(0, 0, 0, 100));
        StackPane.setAlignment(p1Racket.getRacket(), Pos.CENTER_LEFT);
        StackPane.setMargin(p2Racket.getRacket(), new Insets(0, 100, 0, 0));
        StackPane.setAlignment(p2Racket.getRacket(), Pos.CENTER_RIGHT);
        StackPane.setAlignment(buttonBox, Pos.BOTTOM_CENTER);

        setPauseButton();
        setExitButton();
        setResetButton(gameStage,gameScene);
        setDbCommit();
        setLoadFromDB();
        gameScene.setRoot(stackPane);
        gameStage.setScene(gameScene);
        gameStage.getIcons().add(new Image(getClass().getResourceAsStream("/pong_ball.png")));
        gameStage.show();

        gameStage.widthProperty().addListener((observable, oldVal, newVal) -> {
            double newWidth = newVal.doubleValue();
            updateLayout(newWidth, gameCanvas.getHeight());
        });
        gameStage.heightProperty().addListener((observable, oldVal, newVal) -> {
            double newHeight = newVal.doubleValue();
            updateLayout(gameCanvas.getWidth(), newHeight);
        });
        initializeGame(player1Name, player2Name);
    }

    /**
     * initializeGame method is used to set up the game and play the game music.
     * initializeGame method also sets up the player names.
     * @param player1Name - name of player 1
     * @param player2Name - name of player 2
     * @music - game music
     */
    public void initializeGame(String player1Name, String player2Name) {
        System.out.println("Player 1 Name: " + player1Name);
        System.out.println("Player 2 Name: " + player2Name);

        this.player1Name = player1Name;
        this.player2Name = player2Name;

        if (player1Name != null) {
            player1 = new Player(player1Name);
        } else {
            System.out.println("Player 1 still null");
        }
        if (player2Name != null) {
            player2 = new Player(player2Name);
        } else {
            System.out.println("Player 2 still null");
        }

        String gameThemePath = "/bwe4.mp3";
        Media gameThemeMedia = new Media(getClass().getResource(gameThemePath).toString());
        gameTheme = new MediaPlayer(gameThemeMedia);
        gameTheme.play();
        gameTheme.setOnEndOfMedia(() -> gameTheme.seek(Duration.ZERO));

        updatePlayerData();
    }

    /**
     * updateLayout method is used to update the layout of the game canvas when its adjusted
     * it sets the new size of the game canvas.
     * @param newWidth - new width of the game canvas
     * @param newHeight - new height of the game canvas
     */
    private void updateLayout(double newWidth, double newHeight) {
        System.out.println("old width " + gameCanvas.getWidth() + ", old height" + gameCanvas.getHeight());
        gameCanvas.setWidth(newWidth);
        gameCanvas.setHeight(newHeight);
        System.out.println("New width " + newWidth + ", New height" + newHeight);
        setPauseButtonLayout(newWidth, newHeight);
        setExitButtonLayout(newWidth, newHeight);
        setResetButtonLayout(newWidth, newHeight);
    }

    /**
     * setExitButtonLayout method is used to set the layout of the exit button when the game canvas is adjusted
     * @param newWidth - new width of the exit button
     * @param newHeight - new height of the exit button
     */
    private void setExitButtonLayout(double newWidth, double newHeight) {
        double exitButtonWidth = exitButton.getWidth();
        double exitButtonHeight = exitButton.getHeight();
        exitButton.setLayoutX((newWidth - exitButtonWidth) / 2);
        exitButton.setLayoutY(newHeight - exitButtonHeight);
    }

    /**
     * setPauseButtonLayout method is used to set the layout of the pause button when the game canvas is adjusted
     * @param newWidth - new width of the pause button
     * @param newHeight - new height of the pause button
     */
    private void setPauseButtonLayout(double newWidth, double newHeight) {
        double pauseButtonWidth = pauseButton.getWidth();
        double pauseButtonHeight = pauseButton.getHeight();
        pauseButton.setLayoutX((newWidth - pauseButtonWidth) / 2);
        pauseButton.setLayoutY(newHeight - pauseButtonHeight);
    }

    /**
     * setResetButtonLayout method is used to set the layout of the reset button when the game canvas is adjusted
     * @param newWidth - new width of the reset button
     * @param newHeight - new height of the reset button
     */
    private void setResetButtonLayout(double newWidth, double newHeight) {
        double resetButtonWidth = resetButton.getWidth();
        double resetButtonHeight = resetButton.getHeight();
        resetButton.setLayoutX((newWidth - resetButtonWidth) / 2);
        resetButton.setLayoutY(newHeight - resetButtonHeight);
    }

    /**
     * This method is used to set the player names
     * @param player1NameText - player 1 name text
     * @param player2NameText - player 2 name text
     */
    public void setPlayerNames(Text player1NameText, Text player2NameText) {
        this.player1NameText = player1NameText;
        this.player2NameText = player2NameText;
    }

    /**
     * This method is used to set the ball speed
     * @param ballSpeed - ball speed
     */
    public void setBallSpeed(int ballSpeed) {
        System.out.println("Ball Speed: " + ballSpeed);
        if (ball != null) {
            ball.setBallSpeed(ballSpeed, ballSpeed);
        }
    }

    /**
     * The updatePlayerData method keeps track of the player scores and gets it to display on the screen
     * during the game.
     */
    private void updatePlayerData() {
        if (player1 != null || player2 != null) {
            player1NameText.setStyle("-fx-font-size: 30");
            player2NameText.setStyle("-fx-font-size: 30");
            player1ScoreText.setStyle("-fx-font-size: 30");
            player2ScoreText.setStyle("-fx-font-size: 30");
            player1NameText.setText("Player 1: " + player1Name + "\t" + "Score: ");
            player2NameText.setText("Player 2: " + player2Name + "\t" + "Score: ");
        } else {
            System.out.println("Player 1 or player 2 is null");
        }
    }

    /**
     * setRacketDims method is used to set the racket dimensions
     * @param racketSize - the size of the racket
     */
    public void setRacketDims(int racketSize) {
        this.racketDims = racketSize;
    }

    /**
     * setFinalScore method is used to set the winning score that'll cause the game to end
     * @param finalScore - the winning score
     */
    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    /**
     * GameLoop class is used to render the game animation
     */
    private class GameLoop{
        private AnimationTimer timer;
        private long lastFrameTime = 0;
        private final long frameInterval = 20_000_000;
        public void start(){
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (now - lastFrameTime >= frameInterval) {
                        render();
                        lastFrameTime = now;
                    }
                }
            };
            timer.start();
        }
        public void stop(){
            timer.stop();
        }

        /**
         * render method is used to render the game animation and the ball movement
         */
        private void render(){
            GraphicsContext gc = gameCanvas.getGraphicsContext2D();
            gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
            if (ball != null){
                gc.setFill(Color.ORANGE);
                gc.fillOval(ball.getBall().getCenterX(), ball.getBall().getCenterY(), ball.getBall().getRadius(), ball.getBall().getRadius());
            }
        }

    }

    /**
     * setPauseButton method is used to set the pause button.
     * KeyPressed event is used to pause the game when the escape key is pressed.
     * isPaused is set to true when the escape key is pressed
     * Pause Button pressed is printed to the console
     * gameTheme.stop is used to stop the game theme
     * racketThreadHandler.stopRacketThread is used to stop the racket thread
     * ballThreadHandler.stopBallThread is used to stop the ball thread
     */
    private void setPauseButton() {
        pauseButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                isPaused = true;
                System.out.println("Pause Button pressed");
                gameTheme.stop();
                racketThreadHandler.stopRacketThread();
                ballThreadHandler.stopBallThread();
            }
        });
    }


    private void setResetButton(Stage gameStage,Scene gameScene) {
        resetButton.setOnAction(event -> {
            System.out.println("Reset Button pressed");
            gameTheme.stop();
            racketThreadHandler.stopRacketThread();
            ballThreadHandler.stopBallThread();
            p1Score.set(0);
            p2Score.set(0);
            player1ScoreText.setText(""+p1Score.get());
            player2ScoreText.setText(""+p2Score.get());
            initializeGame(player1Name, player2Name);
            setGameCanvas(gameCanvas, exitButton,gameStage, gameScene, player1Name, player2Name);
        });
    }

    /**
     * setDbCommit method is used to set the db commit button.
     */
    private void setDbCommit() {
        dbCommit.setOnAction(event -> {
            System.out.println("DB Commit Button pressed");
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Save Game");
            dialog.setHeaderText("Enter Game Name");
            dialog.setContentText("Please Enter Game Name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                //get data to be inserted
                String player1Name = player1.getPlayerName();
                String player2Name = player2.getPlayerName();
                int player1Score = p1Score.get();
                int player2Score = p2Score.get();
                int finalScore = this.finalScore;

                //Use connectionManager to obtain a connection to the db
                ConnectionManager connectionManager = ConnectionManager.getInstance();
                try (Connection connection = connectionManager.getConnection()) {
                    String insertQuery = "INSERT INTO Game (player1Name, player2Name, player1Score, player2Score, finalScore) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                        preparedStatement.setString(1, name);
                        preparedStatement.setString(2, player1Name);
                        preparedStatement.setString(3, player2Name);
                        preparedStatement.setInt(4, player1Score);
                        preparedStatement.setInt(5, player2Score);
                        preparedStatement.setInt(6, finalScore);

                        int rowsInserted = preparedStatement.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Data inserted successfully");
                        }else{
                            System.out.println("Data insertion failed");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void setLoadFromDB() {

        loadFromDB.setOnAction(event -> {
            System.out.println("Load From DB Button pressed");

            ConnectionManager connectionManager = ConnectionManager.getInstance();
            try (Connection connection = connectionManager.getConnection()) {
                String selectQuery = "SELECT * FROM Game";
                try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        String game_name = resultSet.getString("gameName");
                        String player1_name = resultSet.getString("player1Name");
                        String player2_name = resultSet.getString("player2Name");
                        int player1_score = resultSet.getInt("player1Score");
                        int player2_score = resultSet.getInt("player2Score");
                        int final_score = resultSet.getInt("finalScore");
                        System.out.println(game_name);
                        initializeGame(player1_name, player2_name);
                        p1Score.set(player1_score);
                        p2Score.set(player2_score);
                        finalScore = final_score;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    /**
     * setExitButton method is used to set the exit button.
     * It prints exit button pressed to the console.
     * It stops the racketThreadHandler and ballThreadHandler.
     * It plays an exit sound.
     * When the sound is finished it exits the game.
     */
    private void setExitButton() {
        exitButton.setOnAction(event -> {
            System.out.println("Exit Button pressed");
            gameTheme.stop();
            racketThreadHandler.stopRacketThread();
            ballThreadHandler.stopBallThread();
            String exitSound = "/bye_bye_marioDS.mp3";
            Media sound = new Media(getClass().getResource(exitSound).toString());
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.stop();
                Platform.exit();
            });
        });
    }
}
