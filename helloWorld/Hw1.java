package helloWorld;

import java.awt.*;

public class Hw1 extends BufferedApplet{

	int w,h;
	
	boolean isMyMouseDown=false;
	int myX, myY;
	
	int busLength, busHeight, ballDia, ballRadius, ballX, ballY;
	
	int difficultyLevel=1;
	int winCounter=0, lossCounter=0;
	
	Font winLoss = new Font("Verdana", Font.BOLD, 20);
	
	Image img;
	
	   public boolean mouseMove(Event e, int x, int y) {
//		      System.err.println(x + " " + y);
		      return true;
		   }

		   public boolean mouseDown(Event e, int x, int y) {
		      isMyMouseDown = true;
		      myX = x;
		      myY = y;
		      return true;
		   }

		   public boolean mouseDrag(Event e, int x, int y) {
		      myX = x;
		      myY = y;
		      return true;
		   }

		   public boolean mouseUp(Event e, int x, int y) {
		      isMyMouseDown = false;
		      return true;
		   }
	
	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
				
		if(w==0)
		{
			w=getWidth();
			h=getHeight();
			myX=w/2;
			myY=h-10;
			busLength=100;
			busHeight=20;
			ballDia=busLength/2;
			ballRadius=ballDia/2;
			ballY=0;
		}
		
		
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, w, h);
		
		
		//Code to reset
		g.setColor(Color.GRAY);
		g.fill3DRect(w-100, 0, 100, 50, !isMyMouseDown);
		g.setColor(Color.WHITE);
		g.drawString("RESET", w-80, 20);		
		if((myX>w-100)&&(myX<w)&&(myY>0)&&(myY<50))
		{
			winCounter=0;
			lossCounter=0;
		}
		
		/*
		if(lossCounter-winCounter>=2)
		{
			boolean lost=true;
						
			g.setColor(Color.BLACK);
			g.setFont(winLoss);
			g.drawString("too many losses", w/2-50, h/2);
			g.drawString("Press Reset", w/2-50, h/2+20);
			
			g.setColor(Color.RED);
			g.fill3DRect(w/2-25, h/2+50, 50, 50, !isMyMouseDown);
			
			while(lost)
			{
				if((myX>w/2-25)&&(myX<w/2+25)&&(myY>h/2+50)&&(myY<h/2+100))
				{
					lost=false;
					winCounter=0;
					lossCounter=0;
				}
			}
		}
		*/
		
		
		
		g.setColor(Color.BLUE);
		g.setFont(winLoss);
		g.drawString("Wins "+winCounter, 0, 20);
		g.drawString("Losses "+lossCounter, 0, 60);
		
		
		g.setColor(Color.RED);
		
		//g.draw3DRect(myX, myY, 300, 100, !isMyMouseDown);

		//Move the bus
		g.fill3DRect(myX-(busLength/2), h-(busHeight/2), busLength, busHeight, !isMyMouseDown);
		
		//decide ball height and make it fall
		ballY=ballY+(2*(winCounter+1));
		
		//if bus cannot capture the call
		if(ballY>h)
		{
			ballY=0;
			lossCounter++;
		}
		//if bus captures the ball
		if((ballY>=h-ballDia)&&((ballX+ballRadius>myX-(busLength/2))&&(ballX+ballRadius<myX+(busLength/2))))
		{
			ballY=0;
			winCounter++;
		}
		
		
		
//		System.err.println(ballY);
		
		//Decide New position of ball drop
		if(ballY==0)
		{			
			ballX=(int)(Math.random()*w);
			if(ballX>=(w-ballDia))
			{
				ballX=ballX-ballDia;			
			}		
		}
		
		//Finally draw the ball
		g.setColor(Color.ORANGE);
		g.fillOval(ballX, ballY, ballDia, ballDia);
		
	}
}
