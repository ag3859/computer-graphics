package hW5;

/*<pre>
   This is a simple example to show how to use the MISApplet
   to make your own pixel-by-pixel framebuffer.

   Two methods you can override from the MISApplet base
   class are initFrame and setPixel.
*/

public class MyMISApplet extends MISApplet {

//----- THESE TWO METHODS OVERRIDE METHODS IN THE BASE CLASS

    double t = 0;

    public void initialize() 
    {
    	System.out.println();
    }
    
    public void initFrame(double time) { // INITIALIZE ONE ANIMATION FRAME
/*
       REPLACE THIS CODE WITH YOUR OWN TIME-DEPENDENT COMPUTATIONS, IF ANY.
*/
       t = 30 * time;

    }
    public void setPixel(int x, int y, int rgb[]) { // SET ONE PIXEL'S COLOR
/*
       REPLACE THIS CODE WITH WHATEVER YOU'D LIKE, TO MAKE YOUR OWN COOL IMAGE.
*/
       double fx = ((double)x - W/2) / W;
       double fy = ((double)y - H/2) / H;
       double dx=Math.abs(x-W/2);
       double dy=Math.abs(y-H/2);
       double dist=Math.sqrt(dx*dx+dy*dy);
       for (int j = 0 ; j < 3 ; j++)
       {
    	   int temp=(int) (Math.sin((t)*ImprovedNoise.noise(4*fx, 4*fy, t))*128);
    	   double gap =Math.abs(dist-t%Math.sqrt(W*W+H*H));
    	   double gap1 =Math.abs(dist-((t+100)%Math.sqrt(W*W+H*H)));
    	   double gap2 =Math.abs(dist-((t+200)%Math.sqrt(W*W+H*H)));
    	   double gap3 =Math.abs(dist-((t+300)%Math.sqrt(W*W+H*H)));
    	   
    	   
    	   if(gap<5)
    		   rgb[j]=(int) (255-50*gap);
    	   else if (gap1<5)
    		   rgb[j]=(int) (255-50*gap1);
    	   else if (gap2<5)
    		   rgb[j]=(int) (255-50*gap2);
    	   else if (gap3<5)
    		   rgb[j]=(int) (255-50*gap3);
    	   else rgb[j]=temp;
       }
    }
}
