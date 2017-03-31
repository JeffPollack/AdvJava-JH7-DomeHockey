package my_networked_game;


import gameNet.GameNet_UserInterface;
import gameNet.GamePlayer;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MyUserInterface extends JFrame
implements GameNet_UserInterface
{

	Box box = null;
	Image offScreenImage=null;
	Dimension previousSize=null;
	
	JPanel domeHockeyPanel = new JPanel();
	JPanel gameStats = new JPanel();

	GamePlayer myGamePlayer;
	String myName;
	MyGameInput myGameInput = new MyGameInput();
	int game_top, game_bottom, game_left, game_right;
	Color[] paddleColors = {Color.green, Color.red};
	BoardDimensions boardDimensions = new BoardDimensions();

	@Override
	public void receivedMessage(Object ob) {
		MyGameOutput myGameOutput = (MyGameOutput)ob;
		// Check to see we were accepted and connected
		if (myGamePlayer != null)
		{
			if (myGameOutput.myGame.getMyIndex(myName) < 0)
			{
				System.out.println("Not allowed to connect to the game");
				exitProgram();
			}
			else
			{		
				box = myGameOutput.myGame.box;
				repaint();
			}
		}
		else
			System.out.println("Getting outputs before we are ready");
	}


	@Override
	public void startUserInterface(GamePlayer player) {
		myGamePlayer = player;
		myName = myGamePlayer.getPlayerName();
		myGameInput.setName( myName);
		myGameInput.setCmd(MyGameInput.CONNECTING);
		myGamePlayer.sendMessage(myGameInput);
	}

	private Image loadImage(String fileName) {
	       Image image=null;
	       try {
	           image = ImageIO.read(new File(fileName));
	       } catch (IOException ex) {

	           System.out.println("Error reading file:"+fileName + " err="+ex);
	       }
	       return image;
	}


	public MyUserInterface()
	{
		super("My Pong Game");
		setSize(800, 400);
		// setResizable(false);
		addWindowListener(new Termination());

		Mouser m = new Mouser();
		addMouseMotionListener(m);
		addMouseListener(m);
		setVisible(true); 
	}


	public void paint(Graphics theScreen)
	{  


		Image greenGolie = loadImage("GreenGolie.png");
		Image greenForward = loadImage("GreenForward.png");
		Image redGolie = loadImage("RedGolie.png");
		Image redForward = loadImage("RedForward.png");

		Dimension d = getSize();
		if (offScreenImage==null || !d.equals(previousSize))
		{
			offScreenImage = createImage(d.width, d.height);
			previousSize = d;
		}
		Graphics g = offScreenImage.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.white);
		g.fillRect(0,0, d.width, d.height);
		g.setColor(Color.black);

		Insets insets = getInsets();
		int pad=10;
		boardDimensions.setParms(insets.top+pad, insets.left+pad, 
				d.width-insets.left-insets.right -2*pad, 
				d.height-insets.top-insets.bottom -2*pad);
		if (box == null)
		{
			g.drawString("Click Mouse to start", 50,100);

		}
		else
		{

			if (!box.isRunning())
			{
				String str ="Team Red Score= "+
						box.teamRedScore+ " Team Green Score= "+ box.teamGreenScore+" Click Mouse to restart";
				g.drawString(str, 100, 100); 
			}

			Point bur = boardDimensions.toPixels(box.boxUpperRight);
			Point bul = boardDimensions.toPixels(box.boxUpperLeft);
			Point blr = boardDimensions.toPixels(box.boxLowerRight);
			Point bll = boardDimensions.toPixels(box.boxLowerLeft);
			Point hu  = boardDimensions.toPixels(box.rightHoleUpper);
			Point hl  = boardDimensions.toPixels(box.rightHoleLower);
			Point hlu = boardDimensions.toPixels(box.leftHoleUpper);
			Point hll = boardDimensions.toPixels(box.leftHoleLower);


			
			
			
			g2.setStroke(new BasicStroke(3));

			g.setColor(Color.RED);
			g.drawLine(bur.x / 2, bul.y, blr.x / 2, bul.y*4);  // center line top
			g.drawLine(blr.x / 2, bll.y, blr.x /2, (int) (bul.y*6.4)); //center line bottom
			g.drawLine(bul.x * 5, bul.y, bll.x * 5, bll.y);  // left red line
			g.drawLine(bul.x * 40, bul.y, bll.x * 40, bll.y);  // right red line
			g.setColor(Color.BLUE);
			g.drawOval(bur.x / 2 - bur.x /16, bul.y *4, bur.x/8 - bul.x/8, bur.x/8 - bul.x/8); // center ice
			g.drawLine(bur.x / 2 + bur.x /7, bul.y, blr.x / 2 + blr.x /7, bll.y); // right blue line 
			g.drawLine(bur.x / 2 - bur.x /7, bul.y, blr.x / 2 - blr.x /7, bll.y); // left blue line


			g.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(3));

			
			g.drawLine(bll.x, bll.y, blr.x, blr.y); // lower line
			g.drawLine(bll.x, bll.y, hll.x, hll.y); // below hole on left
			g.drawLine(bul.x, bul.y, hlu.x, hlu.y); // above hole on left
			g.drawLine(bul.x,bul.y, bur.x, bur.y);  // top side
			g.drawLine(bur.x, bur.y, hu.x, hu.y);   // above hole on right
			g.drawLine(blr.x, blr.y, hl.x, hl.y);	// below hole on right
			
			Point pball = boardDimensions.toPixels(box.ballLoc);
			int r = boardDimensions.toPixels(box.ballRadius);
			g.fillOval(pball.x-r, pball.y-r, r, r); // ball size


			int paddleWidth = boardDimensions.toPixels(box.paddleWidth);  // draw paddle
			
			Point pPaddle = boardDimensions.toPixels(box.paddleLoc[0]);
			g.drawImage(greenGolie, pPaddle.x-10, pPaddle.y-paddleWidth/2,
					15, 50, null);
			
			Point pPaddle2 = boardDimensions.toPixels(box.paddleLoc[1]);
			g.drawImage(redGolie, pPaddle2.x, pPaddle2.y-paddleWidth/2,
					15, 50, null);
			
			Point pPaddle3 = boardDimensions.toPixels(box.paddleLoc[2]);
			g.drawImage(greenForward, pPaddle3.x, pPaddle3.y-paddleWidth/2,
					15, 50, null);
			
			Point pPaddle4 = boardDimensions.toPixels(box.paddleLoc[3]);
			g.drawImage(redForward, pPaddle4.x, pPaddle4.y-paddleWidth/2,
					15, 50, null);
			
			g2.setStroke(new BasicStroke(1));

		}

		theScreen.drawImage(offScreenImage, 0,0, this);
	}    

	private void exitProgram()
	{
		if (myGamePlayer != null)
		{
			myGameInput.setCmd(MyGameInput.DISCONNECTING);
			myGamePlayer.sendMessage(myGameInput); // Let the game know that we are leaving

			myGamePlayer.doneWithGame(); // clean up sockets
		}
		System.exit(0);
	}

	//*******************************************
	// An Inner class 
	//*******************************************
	class Mouser extends MouseAdapter
	{
		public void mouseMoved(MouseEvent e)
		{
			int y= e.getY();

			if (box != null)
			{
				myGameInput.setLocation(boardDimensions.toGenericY(y));
				if (myGamePlayer != null)
					myGamePlayer.sendMessage(myGameInput);

			}

		}
		public void mousePressed(MouseEvent e)
		{
			myGameInput.setCmd(MyGameInput.MOUSE_PRESSED);
			if (myGamePlayer != null)
				myGamePlayer.sendMessage(myGameInput);

		}  

	}
	//*******************************************
	// Another Inner class 
	//*******************************************
	class Termination extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			System.out.println("Client is exitting game");
			exitProgram();
		}
	}

	//****** Done with Inner Classes ***************
}
