import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{

    static final int Game_Width = 1000;
    static final int Game_Length = 555;
    static final Dimension Screen_Size = new Dimension(Game_Width, Game_Length);
    static final int Ball_Diameter = 20;
    static final int Paddle_Width = 25;
    static final int Paddle_Height = 100;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddles paddle1;
    Paddles paddle2;
    Ball ball;
    Score score;

    GamePanel(){
        newPaddles();
        newBall();
        score = new Score(Game_Width, Game_Length);
        this.setFocusable(true);
        this.addKeyListener(new ActionListener());
        this.setPreferredSize(Screen_Size);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall(){
        random = new Random();
        ball = new Ball((Game_Width/2)-(Ball_Diameter/2), (Game_Length/2)-(Ball_Diameter/2), Ball_Diameter, Ball_Diameter);
    }

    public void newPaddles(){
        paddle1 = new Paddles(0, (Game_Length/2)-(Paddle_Height/2), Paddle_Width, Paddle_Height, 1);
        paddle2 = new Paddles(Game_Width - Paddle_Width, (Game_Length/2)-(Paddle_Height/2), Paddle_Width, Paddle_Height, 2);
    }
    public void paint(Graphics g){
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }
    public void draw(Graphics g){
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
    }
    public void move(){
        //makes the paddle's movement smoother
        paddle1.move();
        paddle2.move();
        ball.move();
    }
    public void checkCollision(){
        //Stop ball at window border
        if (ball.y <= 0){
            ball.setYDirection(-ball.yVelocity);
        }
        if (ball.y >= Game_Length - Ball_Diameter){
            ball.setYDirection(-ball.yVelocity);
        }
        //Bounces ball off paddles
        if (ball.intersects(paddle1)){
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity+= 1; //Makes the ball faster on hit
            if (ball.yVelocity > 0){
                ball.yVelocity += 1;
            } else {
                ball.yVelocity -= 1;
            }
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if (ball.intersects(paddle2)){
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity+= 1; //Makes the ball faster on hit
            if (ball.yVelocity > 0){
                ball.yVelocity += 1;
            } else {
                ball.yVelocity -= 1;
            }
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        //Stop paddles at window border
        if (paddle1.y <= 0){
            paddle1.y = 0;
        }
        if (paddle1.y >= (Game_Length - Paddle_Height)){
            paddle1.y = Game_Length - Paddle_Height;
        }
        if (paddle2.y <= 0){
            paddle2.y = 0;
        }
        if (paddle2.y >= (Game_Length - Paddle_Height)){
            paddle2.y = Game_Length - Paddle_Height;
        }
        //sets score and resets the ball
        if (ball.x <= 0){
            score.player2++;
            newPaddles();
            newBall();
        }
        if (ball.x >= Game_Width - Ball_Diameter){
            score.player1++;
            newPaddles();
            newBall();
        }
    }
    public void run(){
        //to repaint
        //updates every second to check for collisions
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while(true){
            long now = System.nanoTime();
            delta += (now - lastTime)/ns;
            lastTime = now;
            if (delta >= 1){
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }
    public class ActionListener extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }
        public void keyReleased(KeyEvent e){
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }
}