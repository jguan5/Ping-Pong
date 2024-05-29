import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Score extends Rectangle{
    static int Game_Width;
    static int Game_Length;
    int player1 = 0;
    int player2 = 0;

    Score(int Game_Width, int Game_Length){
        Score.Game_Width = Game_Width;
        Score.Game_Length = Game_Length;
    }
    public void draw(Graphics g){
        g.setColor(Color.white);
        g.setFont(new Font("New Courier", Font.PLAIN, 60));

        g.drawLine(Game_Width/2, 0, Game_Width/2, Game_Length);
        g.drawString(String.valueOf(player1), (Game_Width/2)-85, 50);
        g.drawString(String.valueOf(player2), (Game_Width/2)+20, 50);
    }
}