package com.example.pingpong;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Random;

public class HelloApplication extends Application {
    private static final int width = 800;//width of the screen
    private static final int height = 600;//height of the screen
    private static final int PLAYER_HEIGHT = 100;//height of the bar
    private static final int PLAYER_WIDTH = 15;//width of the bar
    private static final double BALL_R = 15;//Radius of the ball
    private int ballYSpeed = 1;//ball speed on y axis
    private int ballXSpeed = 1;//ball speed on x axis
    private double playerOneYPos = height / 2;//Player position on Y axis
    private double playerTwoYPos = height / 2;//Computer position on Y axis
    private double ballXPs = width / 2;//ball position on X axis
    private double ballYPos = width / 2;//ball position on Y axis
    private int scoreP1 = 0;//Player 1's score
    private int scoreP2 = 0;//Player 2's score
    private boolean gameStarted;//Whether the game has started or not
    private int playerOneXPos = 0;//Player's position on X axis
    private double playerTwoXPos = width - PLAYER_WIDTH;//Computer's position on X axis
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("PONG");//Set the title
        Canvas canvas = new Canvas(width, height);//Create a new background
        GraphicsContext gc = canvas.getGraphicsContext2D();//Create a new graphic object to draw the game
        Timeline t1 = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        t1.setCycleCount(Timeline.INDEFINITE);//Number of cycles in the game. In this case it will repeat endlessly
        //mouseControl
        canvas.setOnMouseMoved(e -> playerOneYPos = e.getY());
        canvas.setOnMouseClicked(e-> gameStarted = true);
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        stage.show();
        t1.play();
    }
    private void run(GraphicsContext gc) {
        //set background color
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,width, height);
        //set text colour
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(25));
        if(gameStarted){
            //set ball movement
            ballXPs+=ballXSpeed;
            ballYPos+=ballYSpeed;
            //In any dimension the speed of the ball changes by it's speed in unit time.

            //computer
            if(ballXPs <width - width/4){
                playerTwoYPos = ballYPos - PLAYER_HEIGHT/2;
            }else{
                playerTwoYPos = ballYPos > playerTwoYPos + PLAYER_HEIGHT / 2 ?playerTwoYPos += 1: playerTwoYPos - 1;
            }
            //It is a simple computer opponent which will simply follow the ball.
            //If Statement: If the ball is in the left three-fourth of the screen. The y position of the computer will be less than the y position of the ball by a distance of half the height of the hitting object.
            //Else Statement: If the ball is in the right quarter of the screen check if the y position of the ball is greater than the y position of the computer by half it's height if this holds then increase computer's height by 1 otherwise decrease it by 1.
            //This arrangement makes sure the computer always chases the ball but never reaches it.

            //draw ball
            gc.fillOval(ballXPs, ballYPos,2* BALL_R, BALL_R);
        }//The game has started
        else{
            //set the start text
            gc.setStroke(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("on Click", width /2, height / 2);

            //reset the ball start position
            ballXPs = width / 2;
            ballYPos = height /2;

            //reset speed and direction
            ballXSpeed = new Random().nextInt(2) == 0 ? 1: -1;
            ballYSpeed = new Random().nextInt(2) == 0 ? 1: -1;
         }//The game has not started


        //ball stays in canvas
        if(ballYPos > height || ballYPos <0) ballYSpeed *=-1;

        //computer gets a point
        if(ballXPs < playerOneXPos - PLAYER_WIDTH){
            scoreP2++;
            gameStarted = false;
        }

        //if you get a point
        if(ballXPs > playerTwoXPos + PLAYER_WIDTH){
            scoreP1++;
            gameStarted = false;
        }

        //increase the ball speed
        if( (ballXPs + BALL_R > playerTwoXPos) && ballYPos >= playerTwoYPos && ballYPos <= playerTwoYPos + PLAYER_HEIGHT || ((ballXPs < playerOneXPos + PLAYER_WIDTH) && ballYPos >= playerOneYPos && ballYPos <= playerOneYPos + PLAYER_HEIGHT)){
            ballYSpeed+=1 * Math.signum(ballYSpeed);
            ballXSpeed +=1 * Math.signum(ballXSpeed);
            ballXSpeed *= -1;
            ballYSpeed *= -1;
        }
        //if ball's horizontal position is greater than computer's x position AND
        //ball's vertical position is greater than or equal to computer's y position AND
        //ball's vertical position is less than or equal to computer's vertical position with it's height OR
        //balls' horizontal position is less than player one's horizontal position added with it's width AND
        //ball's vertical position is greater than player's vertical position AND
        //ball's vertical position is less than or equal to player's vertical position added with it's height

        //draw score
        gc.fillText(scoreP1 + "\t\t\t\t\t\t\t\t" + scoreP2, width /2, 100);
        //draw player 1 & 2
        gc.fillRect(playerTwoXPos,playerTwoYPos, PLAYER_WIDTH,PLAYER_HEIGHT);
        gc.fillRect(playerOneXPos,playerOneYPos,PLAYER_WIDTH,PLAYER_HEIGHT);
    }

    public static void main(String[] args) {
        launch();
    }
}