package my_networked_game;


import gameNet.GameCreator;
import gameNet.GameNet_CoreGame;
import gameNet.GameNet_UserInterface;

public class MyMain extends GameCreator{   
 
  public GameNet_CoreGame createGame()
  {
	  return new MyGame();
  }
 
  public static void main(String[] args) 
  {   
  	MyMain myMain = new MyMain();
  	GameNet_UserInterface myUserInterface = new MyUserInterface();
   
  	myMain.enterGame(myUserInterface); 
  }// end of main
}// end of class
