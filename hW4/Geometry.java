package hW4;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Geometry implements IGeometry{
	
	Matrix global, local;
	
	
	double vertices[][];
	int faces[][];
	
	double[] point0 = new double[3];			//empty, in which coordinates after matrix transformations will come
    double[] point1 = new double[3];
//    double[] point2 = new double[3];

    int[] a = new int[2];						//empty, in which final coordinates will come which will then be drawn
    int[] b = new int[2];						//after using project point function
//    int[] c = new int[2];
  
    double FL=10.0;
		
	int w, h;
	int m, n;
	
	ArrayList<Geometry> kids = new ArrayList<Geometry>();
	ArrayList<int[]> triangles = new ArrayList<int[]>();
	ArrayList<int[][]> trapazoids = new ArrayList<int[][]>();
	
	int[] selfColor;
	
	/*public Geometry(int m1, int n1, int h, int w)
	{
		m=m1;
		n=n1;
		vertices=new double[(m+1)*(n+1) + 2][3];
		faces = new int[m*n][4];
		global=new Matrix2();
		global.identity();
		local=new Matrix2();
		local.identity();
		this.h=h;
		this.w=w;
	}*/
	
	public Geometry(int h, int w)
	{
		global=new Matrix();
		global.identity();
		local=new Matrix();
		local.identity();
		this.h=h;
		this.w=w;
	}
	
	public void makeTriangles()
	{
		for(int i=0;i<faces.length;i++)
		{			
			triangles.add(new int[]{faces[i][0],faces[i][1],faces[i][2]});
			triangles.add(new int[]{faces[i][0],faces[i][2],faces[i][3]});
		}
		
	}
	
	public void drawMyFigure(Graphics g, int w, int h, Matrix m)
    {
		this.w=w;this.h=h;
		
		for (int f = 0 ; f < faces.length ; f++) 
		{
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
	
	public void setFaces()
	{
		for(int i=0;i<m;i++)
			for(int j=0;j<n;j++)
			{
				faces[i+(m*j)][0]=i+(m+1)*j;
				faces[i+(m*j)][1]=(i+1)+(m+1)*j;
				faces[i+(m*j)][2]=(i+1)+(m+1)*(j+1);
				faces[i+(m*j)][3]=i+(m+1)*(j+1);
			}
	}
	
	
	
	public void MakeDiamond()
	{
		double vertices[][]={
				{0,0,-1},{0,-1,0},{-1,0,0},
				{1,0,0},{0,-1,0},{0,0,-1},
				{-1,0,0},{0,1,0},{0,0,-1},
				{0,0,-1},{0,1,0},{1,0,0},
				{-1,0,0},{0,-1,0},{0,0,1},
				{0,0,1},{0,-1,0},{1,0,0},
				{0,0,1},{0,1,0},{-1,0,0},
				{1,0,0},{0,1,0},{0,0,1},
		};
		int faces[][]={
				{0,1,2},{3,4,5},{6,7,8},{9,10,11},
				{12,13,14},{15,16,17},{18,19,20},{21,22,23},
		};
		this.vertices=vertices;
		this.faces=faces;
	}
	
	public void MakeCube()
	{
		double a[]={-1,1,1};
		double b[]={1,1,1};
		double c[]={1,-1,1};
		double d[]={-1,-1,1};
		
		double e[]={-1,1,-1};
		double f[]={1,1,-1};
		double g[]={1,-1,-1};
		double h[]={-1,-1,-1};
		
		double vertices[][]={
				d,c,b,a,
				e,f,g,h,
				a,b,f,e,
				h,g,c,d,
				f,b,c,g,
				e,a,d,h,
		};
		int faces[][]={
				{0,1,2,3},{4,5,6,7},{8,9,10,11},{12,13,14,15},{16,17,18,19},{20,21,22,23}
		};
		this.vertices=vertices;
		this.faces=faces;
	}
	
	public void Makesphere (int m1, int n1)
	{
		m=m1;
		n=n1;
		vertices=new double[(m+1)*(n+1)][3];
		faces = new int[m*n][4];
		
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
		
		setFaces();
	}
	
	public void MakeTorus (int m1, int n1, double r)
	{
		m=m1;
		n=n1;
		vertices=new double[(m+1)*(n+1)][3];
		faces = new int[m*n][4];
		
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
			
		setFaces();	
	}
		
	public void MakeCylinder (int m1, int n1)
	{		
		m=m1;
		n=n1;
		vertices=new double[(m+1)*(n+1)][3];
		faces = new int[m*n][4];
		
		double theta, z;
					
		for(int i=0;i<=m;i++)
			for(int j=0;j<=n;j++)
			{
				theta=2*Math.PI*((double)i/n);
				z=((double)j/n)<0.5? -1:1;
									
				double rv=((((double)j/n==0)||((double)j/n==1))?0:1);
	
				vertices[i+(m+1)*j][0]=Math.cos(theta)*rv;
				vertices[i+(m+1)*j][1]=Math.sin(theta)*rv;
				vertices[i+(m+1)*j][2]=z;		
			}
			
		setFaces();
	}

	@Override
	public void add(Geometry child) {
		// TODO Auto-generated method stub
		kids.add(child);
	}

	@Override
	public Geometry getChild(int i) {
		// TODO Auto-generated method stub
		return kids.get(i);
	}

	@Override
	public Matrix getMatrix() {
		// TODO Auto-generated method stub
		return local;
	}

	@Override
	public int getNumChildren() {
		// TODO Auto-generated method stub
		return kids.size();
	}

	@Override
	public void remove(Geometry child) {
		// TODO Auto-generated method stub
		kids.remove(child);
	}
}
