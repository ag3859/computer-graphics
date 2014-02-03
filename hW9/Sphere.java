package hW9;

public class Sphere {

	  public double center[];
	  public double radius;
	  public Material mat;

	  public Material getMat() {
	    return mat;
	  }

	  public void setMat(Material mat) {
	    this.mat = mat;
	  }

	  public double[] getCenter() {
	    return center;
	  }

	  public double getRadius() {
	    return radius;
	  }

	  public void setRadius(double radius) {
	    this.radius = radius;
	  }

	  public void setCenter(double a, double b, double c) {
	    center = new double[3];
	    this.center[0] = a;
	    this.center[1] = b;
	    this.center[2] = c;	    
	  }
}
