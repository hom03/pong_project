package com.example.pong_project;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * the play application class for the pong game.
 * This extends the application class from javafx.
 * It sets up the scene for the game.
 */
public class PlayApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PlayApplication.class.getResource("menu-view.fxml"));
        Parent root = fxmlLoader.load();
        Image image = new Image(getClass().getResourceAsStream("/logo.png"));
        ImageView imageView = new ImageView(image);
        ((Pane)root).getChildren().add(imageView);

        Scene scene = new Scene(root, 320, 240);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/pong_ball.png")));
        stage.setTitle("Pong 2024");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * the main method launches the application
     * @param args - the command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}