package com.example.pong_project.View;

import com.example.pong_project.Model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The ball thread handler class is responsible for moving the ball and updating the score.
 * It also handles the threading of the ball.
 */
public class BallThreadHandler {
    private Ball ball;
    private boolean endThread = false;
    private StackPane goalPane = new StackPane();
    private AtomicInteger p1Score = new AtomicInteger();
    private AtomicInteger p2Score = new AtomicInteger();

    public BallThreadHandler() {
        this.ball = new Ball(5,0,10,5,5);
    }
    /**
     * This method handles the threading of the ball and the movement.
     * @param ball - the ball
     * @param sceneWidth - the width of the scene
     * @param sceneHeight - the height of the scene
     * @param p1Racket - the player 1 racket
     * @param p2Racket - the player 2 racket
     * @param p1Score - the player 1 score
     * @param p2Score - the player 2 score
     * @param finalScore - the final score
     * @param player1ScoreText - the player 1 score text
     * @param player2ScoreText - the player 2 score text
     */
    public void moveBall(Ball ball,double sceneWidth, double sceneHeight, Racket p1Racket, Racket p2Racket,
                                AtomicInteger p1Score, AtomicInteger p2Score,int finalScore,Text player1ScoreText,
                                Text player2ScoreText,StackPane goalPane) {
        this.ball = ball;
        this.goalPane = goalPane;
        Thread ballThread = new Thread(() -> {
            double deltaX = 5.0;
            double deltaY = 5.0;
            while (!endThread) {
                double newX = ball.getX() + deltaX;
                double newY = ball.getY() + deltaY;
                ball.getBall().setCenterX(newX);
                ball.getBall().setCenterY(newY);
                if (newY <= 0 || newY >= sceneHeight - ball.getBall().getRadius()) {
                    deltaY = -deltaY;
                }

                if (newX <= p1Racket.getX() + p1Racket.getWidth() &&
                        newY >= p1Racket.getY() &&
                        newY <= p1Racket.getY() + p1Racket.getHeight()
                        || newX >= p2Racket.getX() &&
                        newX <= p2Racket.getX() + p2Racket.getWidth() &&
                        newY >= p2Racket.getY() &&
                        newY <= p2Racket.getY() + p2Racket.getHeight()) {
                    deltaX = -deltaX;
                }
                if (newX <= 0) {
                    p2Score.getAndIncrement();
                    System.out.println("P2 Score: " + p2Score.get());
                    displayGoalMessage("Player 2 scored!");
                    resetBall(sceneWidth, sceneHeight);
                    if (p2Score.get() == finalScore) {
                        System.out.println("Player 2 wins!");
                        Platform.runLater(() -> {
                            try {
                                Thread.sleep(2000);
                                Platform.exit();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                        endThread = true;
                    }
                }
                if (newX >= sceneWidth - ball.getBall().getRadius()) {
                    p1Score.getAndIncrement();
                    System.out.println("P1 Score: " + p1Score.get());
                    displayGoalMessage("Player 1 scored!");
                    resetBall(sceneWidth, sceneHeight);
                    if (p1Score.get() == finalScore) {
                        System.out.println("Player 1 wins!");
                        Platform.runLater(() -> {
                            try {
                                Thread.sleep(2000);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                        endThread = true;
                    }
                }

                Platform.runLater(() -> {
                    ball.getBall().setCenterY(newY);
                    player2ScoreText.setText("" + p2Score.get());
                    player1ScoreText.setText("" + p1Score.get());
                });
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        );
        ballThread.start();
        System.out.println("Ball Thread Started");
    }

    /**
     * stopBallThread stops the ball thread
     */
    public void stopBallThread() {
        endThread = true;
    }

    /**
     * Resets the ball position to the center when a goal is scored
     * @param sceneWidth - the width of the scene
     * @param sceneHeight - the height of the scene
     */
    public void resetBall(double sceneWidth, double sceneHeight) {
        if (ball != null) {
            ball.getBall().setCenterX(sceneWidth / 2 - ball.getBall().getRadius());
            ball.getBall().setCenterY(sceneHeight / 2 - ball.getBall().getRadius());
        }
    }

    /**
     * displayGoalMessage displays the message when a goal is scored
     * @param message - the message to display
     */
    public void displayGoalMessage(String message) {
        Label goalMessage = new Label(message);
        goalMessage.setStyle("-fx-font-size: 20;-fx-text-fill: white");
        goalPane.getChildren().add(goalMessage);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            goalPane.getChildren().remove(goalMessage);
        }));
        timeline.play();
    }

    public Ball getBall(){
        return ball;
    }
    public boolean p2Goal(){
        return ball.getBall().getCenterX() < 0;
    }

}

