package com.example.pong_project.View;

import static org.junit.Assert.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.junit.Before;
import org.junit.Test;
import com.example.pong_project.*;
import com.example.pong_project.View.*;
import com.example.pong_project.Controller.*;
import com.example.pong_project.Model.*;

import java.util.concurrent.atomic.AtomicInteger;

public class BallThreadHandlerTest {
    BallThreadHandler ballThreadHandler;
    Racket p1Racket;
    Racket p2Racket;
    Ball ball;


    @Before
    public void setUp(){
        ballThreadHandler = new BallThreadHandler();
        p1Racket = new Racket(10, 10, 10, 10, Color.RED);
        p2Racket = new Racket(100, 10, 10, 10, Color.BLUE);
        ball = ballThreadHandler.getBall();
    }
    @Test
    public void p2GoalTest(){
        ball.getBall().setCenterX(-1);
        assertTrue(ballThreadHandler.p2Goal());
        ball.getBall().setCenterX(100);
        assertFalse(ballThreadHandler.p2Goal());
    }
    @Test
    public void racketCollisionTest(){
        ball.getBall().setCenterX(10);
        assertTrue((int) ball.getBall().getCenterX() == p1Racket.getX());
        assertFalse((int) ball.getBall().getCenterX() == p2Racket.getX());
        ball.getBall().setCenterX(100);
        assertTrue((int) ball.getBall().getCenterX() == p2Racket.getX());
        assertFalse((int) ball.getBall().getCenterX() == p1Racket.getX());
    }
}
