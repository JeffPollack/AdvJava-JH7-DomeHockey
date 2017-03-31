package my_networked_game;


import java.awt.Point;

public class BoardDimensions {

	private int top=-1, left=-1;
	private double scale_width=0.0, scale_height=0.0;
		
	public void setParms(int top, int left , int width, int height)
	{
		this.top = top;
		this.left = left;
		scale_width = Box.box_width/(double)width;
		scale_height = Box.box_height/(double)height;
	}
	// Used to translate MouseMoved locations to Paddle Locations
	int toGenericY(int y)
	{
		int yGeneric = (int)((y-top) * scale_height);
		return yGeneric;
	}
	// Used for translating Box Points to the Screen
	Point toPixels(Point p)
	{
		int x = left + (int)(p.x / scale_width);
		int y = top + (int)(p.y / scale_height);
		return new Point(x,y);
	}
	// Used to resize the Ball
	int toPixels(int y)
	{
		int yPixels = (int)(y / scale_height);
		return yPixels;
	}
	
	

}
