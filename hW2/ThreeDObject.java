package hW2;

import java.awt.Color;
import java.awt.Graphics;



public class ThreeDObject extends BufferedApplet{

	int w,h;
	
	int counter;
	
	
    double[][] vertices = {
            { -1,-1,-1}, {0, 0,-1}, { 1,-1,-1}, {-1, 1,-1}, {0, 2,-1}, {1, 1,-1},
            { -1,-1, 1}, {0, 0, 1}, { 1,-1, 1}, {-1, 1, 1}, {0, 2, 1}, {1, 1, 1},
         };

    double[] center ={0,0,0};
    int[] actualcenter=new int[2];
    
         int[][] edges = {
            // EDGES IN X DIRECTION
            {0,1},{1,2},{3,4},{4,5},
            {6,7},{7,8},{9,10},{10,11},
            // EDGES IN Y DIRECTION
            {0,3},{6,9},{2,5},{8,11},
            // EDGES IN Z DIRECTION
            {0,6},{3,9},{4,10},{1,7},{5,11},{2,8} 
         };

         double[] point0 = new double[3];			//empty, in which coordinates after matrix transformations will come
         double[] point1 = new double[3];

         int[] a = new int[2];						//empty, in which final coordinates will come which will then be drawn
         int[] b = new int[2];						//after using project point function

         double FL = 20.0; 
         
         Matrix matrix = new Matrix();
         
         double bit=0.1;
         double factor=4;
         public void projectPoint(double[] xyz, int[] pxy) {

             // INPUT: YOUR POINT IN 3D

             double x = xyz[0];
             double y = xyz[1];
             double z = xyz[2];

             // OUTPUT: PIXEL COORDINATES TO DISPLAY YOUR POINT

             pxy[0] = w / 2 + (int)(h * x / (FL - z));
             pxy[1] = h / 2 - (int)(h * y / (FL - z));
             
          }
         
         double time1= System.currentTimeMillis()/1000;
         
         public void drawMyFigure(Graphics g)
         {
        	 double center1=0, center0=0;
        	 
        	 for (int e = 0 ; e < edges.length ; e++) {
        		 int i = edges[e][0];
        		 int j = edges[e][1];
             
        		 matrix.transform(vertices[i], point0);
        		 matrix.transform(vertices[j], point1);

        		 projectPoint(point0, a);
        		 projectPoint(point1, b);
        		 
        		 center0+=(a[0]+b[0])/2;
        		 center1+=(a[1]+b[1])/2;
        		 
        		 g.drawLine(a[0], a[1], b[0], b[1]);
        		 
        	 }
        	 g.drawLine(actualcenter[0], actualcenter[1], (int)center0/edges.length, (int)center1/edges.length);
         }
         
         public void indi(Graphics g, double x, double y, double z)
         {
        	        	 
        	 matrix.identity();
           	
           	 matrix.rotateY(x);
           	 matrix.rotateX(y);
           	 matrix.rotateZ(z);
           	 matrix.translate(x,y,z);


           	 g.setColor(new Color(150,150,150/*, random, random*/));

           	 
        	 drawMyFigure(g);
        	 
        	 
         }
         
         /* (non-Javadoc)
         * @see hW2.BufferedApplet#render(java.awt.Graphics)
         */
        public void render(Graphics g) {

        	 double time2=System.currentTimeMillis()/1000.0;
        	 
        	 if(w==0)
        	 {
        			w=getWidth();
        			h=getHeight();
        			projectPoint(center, actualcenter);
        	 }
        	 
        	 g.setColor(Color.white);
        	 g.fillRect(0, 0, w, h);
        	 

        	 indi(g, 10*Math.sin(time2), 0, 0);
        	 
        	 indi(g, 0, 10*Math.cos(time2), 0);
        	 
        	 indi(g, 0, 0, 10*Math.sin(time2));
        	        	              
         }
}

