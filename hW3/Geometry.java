package hW3;

import java.awt.Graphics;

public class Geometry {
	
	double vertices[][];
	int faces[][];
	
	double[] point0 = new double[3];			//empty, in which coordinates after matrix transformations will come
    double[] point1 = new double[3];
    double[] point2 = new double[3];

    int[] a = new int[2];						//empty, in which final coordinates will come which will then be drawn
    int[] b = new int[2];						//after using project point function
    int[] c = new int[2];
  
    double FL=10.0;
		
	int w, h;
	int m, n;
	public Geometry(int m1, int n1)
	{
		m=m1;
		n=n1;
		vertices=new double[(m+1)*(n+1) + 2][3];
		faces = new int[m*n][4];
	}
	
	public Geometry()
	{
		
	}
	
	public void drawMyFigure(Graphics g, int w, int h, Matrix m)
    {
		this.w=w;this.h=h;
		
   	 for (int f = 0 ; f < faces.length ; f++) {
   		 for(int f2=0;f2<faces[f].length;f2++)
   		 {
   		 
   		 int i = faces[f][f2];
   		 int j = faces[f][(f2+1)%faces[f].length];
   		  
         m.transform(vertices[j], point1);
   		 m.transform(vertices[i], point0);   		 

   		 projectPoint(point0, a);
   		 projectPoint(point1, b);
   		 
   		 g.drawLine(a[0], a[1], b[0], b[1]);
   		 
   		 }
   		 
   	 }

    }
	
	public void projectPoint(double[] xyz, int[] pxy) {

        // INPUT: YOUR POINT IN 3D

        double x = xyz[0];
        double y = xyz[1];
        double z = xyz[2];

        // OUTPUT: PIXEL COORDINATES TO DISPLAY YOUR POINT

        pxy[0] = w / 2 + (int)(h * x / (FL - z));
        pxy[1] = h / 2 - (int)(h * y / (FL - z));
        
     }
	
		
	public void Makesphere ()
	{
		double theta, phi;
					
		for(int i=0;i<=m;i++)
			for(int j=0;j<=n;j++)
			{
				theta=2*Math.PI*i/m;
				phi=-(Math.PI/2)+(j*Math.PI/n);
				
				vertices[i+(m+1)*j][0]=Math.cos(theta)*Math.cos(phi);
				vertices[i+(m+1)*j][1]=Math.cos(phi)*Math.sin(theta);
				vertices[i+(m+1)*j][2]=Math.sin(phi);				
			}
			
		for(int i=0;i<m;i++)
			for(int j=0;j<n;j++)
			{
				faces[i+(m*j)][0]=i+(m+1)*j;
				faces[i+(m*j)][1]=(i+1)+(m+1)*j;
				faces[i+(m*j)][2]=(i+1)+(m+1)*(j+1);
				faces[i+(m*j)][3]=i+(m+1)*(j+1);
			}		
	}
	
	public void MakeTorus (double r)
	{
		double theta, phi;
					
		for(int i=0;i<=m;i++)
			for(int j=0;j<=n;j++)
			{
				theta=2*Math.PI*i/m;
				phi=2*Math.PI*j/n;
				
				vertices[i+(m+1)*j][0]=(1+r*Math.cos(phi))*Math.cos(theta);
				vertices[i+(m+1)*j][1]=(1+r*Math.cos(phi))*Math.sin(theta);
				vertices[i+(m+1)*j][2]=r*Math.sin(phi);		
			}
			
		for(int i=0;i<m;i++)
			for(int j=0;j<n;j++)
			{
				faces[i+(m*j)][0]=i+(m+1)*j;
				faces[i+(m*j)][1]=(i+1)+(m+1)*j;
				faces[i+(m*j)][2]=(i+1)+(m+1)*(j+1);
				faces[i+(m*j)][3]=i+(m+1)*(j+1);
			}
		
	}
	
	public void MakeCylinder (double r)
	{		
		double theta, z;
					
		for(int i=0;i<=m;i++)
			for(int j=0;j<=n;j++)
			{
				theta=2*Math.PI*i/m;
				z=j/n<0.5? -1:1;
									
				//double rv=j/n==0||2/n==1?0:1;
				double rv=0.5;
				vertices[i+(m+1)*j][0]=Math.cos(theta)*rv;
				vertices[i+(m+1)*j][1]=Math.sin(theta)*rv;
				vertices[i+(m+1)*j][2]=z;		
			}
			
		for(int i=0;i<m;i++)
			for(int j=0;j<n;j++)
			{
				faces[i+(m*j)][0]=i+(m+1)*j;
				faces[i+(m*j)][1]=(i+1)+(m+1)*j;
				faces[i+(m*j)][2]=(i+1)+(m+1)*(j+1);
				faces[i+(m*j)][3]=i+(m+1)*(j+1);
			}
	}
}
