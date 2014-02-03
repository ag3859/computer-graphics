package hW9;

public class Lights {
      
	  double[] color = new double[3];
	  double[] direction = new double[3];
	  double[] source = new double[3];
	  public double[] getColor() {
	    return color;
	  }

	  public void setColor(double[] color) {
	    this.color[0] = color[0];
	    this.color[1] = color[1];
	    this.color[2] = color[2];
	    normalize(this.color);
	  }

	  public double[] getDirection() {
	    return direction;
	  }

	  public void setDirection(double[] direction) {
	    this.direction[0] = direction[0];
	    this.direction[1] = direction[1];
	    this.direction[2] = direction[2];
	    
	  normalize(this.direction);
	  }
	  public void normalize(double V[]) {
			double length = Math.sqrt(V[0] * V[0] + V[1] * V[1] + V[2] * V[2]);
			if (length != 0) {
				for (int i = 0; i < 3; i++) {
					V[i] = V[i] / length;
				}
			}
		}
}
