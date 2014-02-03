package hW4;

import java.awt.Color;
import java.awt.Graphics;

public class StringOfGeometry extends BufferedApplet{

	int w, h;
	double FL = 10.0;
	
	Geometry world, head, body, leftUpperArm, rightUpperArm, leftelbow, rightelbow, leftLowerArm, rightLowerArm;
	Geometry leftLeg, rightLeg, leftFeet, rightFeet;
	Matrix m;
	
	double time1=System.currentTimeMillis()/1000.0;
	
	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
	
		if(w==0)
		{
			w=getWidth();
			h=getHeight();
			
			world=new Geometry(h,w);
			
			head=new Geometry(h,w/*10, 10*/);
			head.Makesphere(10,10);
			//head.MakeCylinder(50, 50, 1);
			//head.MakeCylinder(5);
			head.local.translate(0, -2, 0);
			head.local.scale(0.75, 0.75, 0.75);
			//head.local.rotateY(Math.PI/2);
			world.add(head);
			
			
			body=new Geometry(h,w);
			body.MakeCube();
			body.local.translate(0, 3, 0);
			body.local.scale(2, 2, 2);
			head.add(body);
			
			
			leftUpperArm=new Geometry(h,w);
			leftUpperArm.Makesphere(25,25);
			leftUpperArm.local.translate(-1.8, -0.5, 0);
			leftUpperArm.local.rotateZ(Math.PI/8);			
			leftUpperArm.local.scale(1,0.2, 0.2);
			body.add(leftUpperArm);
			
			leftelbow=new Geometry(h,w);
			leftelbow.Makesphere(50,50);
			leftelbow.local.scale(0.1, 0.5, 0.5);
			leftelbow.local.translate(-9, 0, 0);
			leftelbow.local.rotateZ(Math.PI/2);
			leftUpperArm.add(leftelbow);
			
			leftLowerArm=new Geometry(h,w);
			leftLowerArm.Makesphere(10,10);
			leftLowerArm.local.scale(5, 1, 1);
			leftLowerArm.local.translate(-1, 0, 0);			
			leftelbow.add(leftLowerArm);
			
			rightUpperArm=new Geometry(h,w);
			rightUpperArm.Makesphere(25,25);
			rightUpperArm.local.translate(1.8, -0.5, 0);
			rightUpperArm.local.rotateZ(-Math.PI/8);
			rightUpperArm.local.scale(1, 0.2, 0.2);
			body.add(rightUpperArm);
			
			rightelbow=new Geometry(h,w);
			rightelbow.Makesphere(50, 50);
			rightelbow.local.scale(0.1, 0.5, 0.5);
			rightelbow.local.translate(9, 0, 0);
			rightelbow.local.rotateZ(-Math.PI/2);
			rightUpperArm.add(rightelbow);
			
			rightLowerArm=new Geometry(h,w);
			rightLowerArm.Makesphere(10,10);
			rightLowerArm.local.scale(5,1,1);
			rightLowerArm.local.translate(1, 0, 0);
			rightelbow.add(rightLowerArm);
			
			leftLeg=new Geometry(h,w);
			leftLeg.MakeCylinder(10,10);
			leftLeg.local.rotateX(-Math.PI/2);
			leftLeg.local.scale(0.2, 0.2, 0.8);
			leftLeg.local.translate(-2, 0, 2.2);
			body.add(leftLeg);
			
			leftFeet=new Geometry(h,w);
			leftFeet.Makesphere(20, 20);
			leftFeet.local.scale(1, 2, 0.1);
			leftFeet.local.translate(0, 0.5, 10);
			leftLeg.add(leftFeet);
			
			rightLeg=new Geometry(h,w);
			rightLeg.MakeCylinder(10,10);
			rightLeg.local.rotateX(-Math.PI/2);
			rightLeg.local.scale(0.2, 0.2, 0.8);
			rightLeg.local.translate(2, 0, 2.2);
			body.add(rightLeg);
			
			rightFeet=new Geometry(h,w);
			rightFeet.Makesphere(20, 20);
			rightFeet.local.scale(1, 2, 0.1);
			rightFeet.local.translate(0, 0.5, 10);
			rightLeg.add(rightFeet);
		}
		
   	 	g.setColor(Color.white);
   	 	g.fillRect(0, 0, w, h);
		
		double time=System.currentTimeMillis()/1000.0-time1;

		g.setColor(Color.black);
		
		head.global.identity();
		head.local.rotateY(Math.sin(time)/10);
		head.global.rightMultiply(world.global);
		head.global.rightMultiply(head.local);			
		head.drawMyFigure(g, w, h, head.global);
		
		body.global.identity();
		body.global.rightMultiply(head.global);
		body.global.rightMultiply(body.local);
		body.drawMyFigure(g, w, h, body.global);
				
		g.setColor(Color.red);
		leftUpperArm.local.rotateX(Math.sin(time)/10);
		leftUpperArm.global.identity();
		leftUpperArm.global.rightMultiply(body.global);
		leftUpperArm.global.rightMultiply(leftUpperArm.local);
		leftUpperArm.drawMyFigure(g, w, h, leftUpperArm.global);
		
		g.setColor(Color.green);		
		leftelbow.global.identity();
		leftelbow.global.rightMultiply(leftUpperArm.global);
		leftelbow.global.rightMultiply(leftelbow.local);
		leftelbow.drawMyFigure(g, w, h, leftelbow.global);
		
		g.setColor(Color.gray);
		leftLowerArm.global.identity();
		leftLowerArm.global.rightMultiply(leftelbow.global);
		leftLowerArm.global.rightMultiply(leftLowerArm.local);
		leftLowerArm.drawMyFigure(g, w, h, leftLowerArm.global);
		
		
		
		g.setColor(Color.red);
		rightUpperArm.local.rotateX(Math.sin(time)/10);
		rightUpperArm.global.identity();
		rightUpperArm.global.rightMultiply(body.global);
		rightUpperArm.global.rightMultiply(rightUpperArm.local);
		rightUpperArm.drawMyFigure(g, w, h, rightUpperArm.global);
		
		g.setColor(Color.green);		
		rightelbow.global.identity();
		rightelbow.global.rightMultiply(rightUpperArm.global);
		rightelbow.global.rightMultiply(rightelbow.local);
		rightelbow.drawMyFigure(g, w, h, rightelbow.global);
		
		g.setColor(Color.gray);
		rightLowerArm.global.identity();
		rightLowerArm.global.rightMultiply(rightelbow.global);
		rightLowerArm.global.rightMultiply(rightLowerArm.local);
		rightLowerArm.drawMyFigure(g, w, h, rightLowerArm.global);
		
		g.setColor(Color.orange);
		leftLeg.global.identity();
		leftLeg.global.rightMultiply(body.global);
		leftLeg.global.rightMultiply(leftLeg.local);
		leftLeg.drawMyFigure(g, w, h, leftLeg.global);
		
		g.setColor(Color.yellow.darker());
		leftFeet.global.identity();
		leftFeet.global.rightMultiply(leftLeg.global);
		leftFeet.global.rightMultiply(leftFeet.local);
		leftFeet.drawMyFigure(g, w, h, leftFeet.global);
		
		g.setColor(Color.orange);
		rightLeg.global.identity();
		rightLeg.global.rightMultiply(body.global);
		rightLeg.global.rightMultiply(rightLeg.local);
		rightLeg.drawMyFigure(g, w, h, rightLeg.global);
		
		g.setColor(Color.yellow.darker());
		rightFeet.global.identity();
		rightFeet.global.rightMultiply(rightLeg.global);
		rightFeet.global.rightMultiply(rightFeet.local);
		rightFeet.drawMyFigure(g, w, h, rightFeet.global);
	}
}
