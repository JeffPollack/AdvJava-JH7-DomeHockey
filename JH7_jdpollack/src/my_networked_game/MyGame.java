package my_networked_game;


import gameNet.GameControl;
import gameNet.GameNet_CoreGame;

import java.io.Serializable;
import java.util.ArrayList;

 
public class MyGame extends GameNet_CoreGame implements Runnable, Serializable {
   
    
    private ArrayList<String> clients = new ArrayList<String>();
    public Box box = new Box();
    
    transient GameControl gameControl;
    
    public void startGame(GameControl g)
    {
        gameControl =g;
        Thread t = new Thread(this);
        t.start();
    }
    
    public int getMyIndex(String name)  // checks if player is in game
    {
    	return clients.indexOf(name);
    }
    
    @Override
    public Object process(Object ob) {
        MyGameInput myGameInput = (MyGameInput)ob;								  // *******************************
        if (myGameInput.command == MyGameInput.CONNECTING && clients.size() < 4)  // change to increase the number of players allowed
        {
        	clients.add(myGameInput.name);
        }
        
        switch(myGameInput.command)
        {
        case MyGameInput.CONNECTING:
        	break;
        case MyGameInput.DISCONNECTING:
        	clients.remove(myGameInput.name);
        	break;
        case MyGameInput.MOUSE_PRESSED:
        	 box.setGame(true);
        	break;
        case MyGameInput.MOUSE_MOVED: 
            int clientIndex = getMyIndex(myGameInput.name);  // getting which player is moving their mouse and applying their paddle
            if (clientIndex >= 0)
            	box.setPaddleY(myGameInput.y_location, clientIndex);
        	break;
        }
        MyGameOutput myGameOutput = new MyGameOutput(this);  // sends to all clients
        return myGameOutput;
    }
    @Override
    public void run() {
        while(true)
        {
            try{
                Thread.sleep(30);  // was 60
                if (box.isRunning())
                {
                	box.update();  
                	MyGameOutput mo = new MyGameOutput(this);
    	            gameControl.putMsgs(mo);
                }
            }
            catch (InterruptedException e){}
        }
        
    }
}
