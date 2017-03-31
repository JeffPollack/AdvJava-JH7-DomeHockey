package my_networked_game;


import java.io.Serializable;

public class MyGameOutput implements Serializable{
    MyGame myGame = null;
    MyGameOutput(MyGame g)
    {
        myGame =g;
    }
}
