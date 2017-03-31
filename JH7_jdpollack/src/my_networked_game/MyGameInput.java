package my_networked_game;


import java.io.Serializable;

public class MyGameInput implements Serializable{
	static final int CONNECTING=1;
	static final int DISCONNECTING=2;
	static final int MOUSE_PRESSED=3;
	static final int MOUSE_MOVED=4;
	
    String name;
    int y_location;
    int command;
    
    void setName(String name)
    {
    	this.name=name;
    }
    void setCmd(int command)
    {
    	this.command=command;
    }
    
    void setLocation(int location)
    {
    	y_location = location;
    	command=MOUSE_MOVED;
    }
}
