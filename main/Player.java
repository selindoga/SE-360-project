package main;
import java .awt.*;

public class Player {


    public Color color;
    public int score;
    public String name;

    public Player(Color color , String name){
        this.color = color;
        score = 0;
        this.name = name;
    }

    public Color getColor() {
        return this.color;
    }

    public int getScore() {
        return this.score;
    }

    public String getName() {
        return this.name;
    }

    public void increaseScore() {
        this.score++;
    }



}
