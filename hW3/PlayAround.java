package hW3;

import java.awt.Color;
import java.awt.Graphics;

import hW2.BufferedApplet;

public class PlayAround extends BufferedApplet{

int w,h;
	
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
	
	
	Geometry cube=new Geometry();
	Geometry sphere=new Geometry(10,10);
    Geometry torus = new Geometry(50,50);
    Geometry cylinder = new Geometry(50,50);
    
    double time1=System.currentTimeMillis()/1000.0;
    
	public PlayAround() {
		// TODO Auto-generated constructor stub
	}

	Matrix m;

	
	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
		if(w==0)
		{
			w=getWidth();
			h=getWidth();
			cube.vertices=vertices;
			cube.faces=faces;
			m=new Matrix();
			sphere.Makesphere();
			torus.MakeTorus(0.5);
			cylinder.MakeCylinder(1);
		}
		
		
   	 	g.setColor(Color.white);
   	 	g.fillRect(0, 0, w, h);
   	 	
   	    double time=System.currentTimeMillis()/1000.0-time1;

   	 	g.setColor(Color.black);   	 	
   	 	m.identity();
   	 	m.translate(2, 2, 0);
   	 	m.rotateY(Math.sin(time));
   	 	cube.drawMyFigure(g, w, h, m);
   	 	
   	 	g.setColor(Color.blue);
   	 	m.identity();
   	 	m.translate(-2, -2, 0);
   	 	m.rotateX(Math.sin(time));   	 	
   	 	sphere.drawMyFigure(g, w, h, m);
   	 	
   	 	g.setColor(Color.green);
   	 	m.identity();
   	 	m.translate(-2, 2, 0);
   	 	m.scale(1, 1, 5);
   	 	m.rotateZ(Math.sin(time));   	 	
   	 	torus.drawMyFigure(g, w, h, m);

   	 	
   	 	g.setColor(Color.green);
	 	m.identity();
	 	m.translate(2, -2, 0);
	  	m.rotateZ(Math.sin(time));	 	
	 	cylinder.drawMyFigure(g, w, h, m);
	}
}
