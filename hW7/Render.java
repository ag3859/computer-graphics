package hW7;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Render extends MISApplet {

	Geometry world;
	Geometry ballBearing;
	Geometry bar;
	Geometry wheel;

	Matrix m = new Matrix();
	
	Material material=new Material();

	ArrayList<Geometry> allShapes = new ArrayList<Geometry>();

	double[] point0 = new double[6]; // empty, in which coordinates after matrix
										// transformations will come
	double[] point1 = new double[6];
	double[] point2 = new double[6];

	double[] a = new double[6]; // empty, in which final coordinates will come which
							// will then be drawn
	double[] b = new double[6]; // after using project point function
	double[] c = new double[6];
	double red,green,blue,zp;
	ArrayList<int[]> triangles;
	double[][] vertices;
	ArrayList<double[][]> trapazoids;
	double[]zbuffer;
	
	double[][][] lights = {
			// Ldir Lcolor
			{ { -1.0, -1.0, -1.0 }, { -1.0, -1.0, -1.0 } },
			{ { 1.0, 1.0, 1.0 }, { 1.0, 1.0, 1.0 } },
	};

	
	public void initialize() {
		world = new Geometry(H, W);
		ballBearing = new Geometry(H, W);
		bar = new Geometry(H, W);
		wheel = new Geometry(H, W);

		zbuffer=new double[pix.length];
		for(int i=0;i<pix.length;i++)
			zbuffer[i]=-10;

		material.setAmbient(new double[]{0.38,0.15,0.03});
	    material.setDiffuse(new double[]{0.5,0.4,0.0});
	    material.setSpecular(new double[]{0.8,0.8,0.0});
	    material.setSpecularPower(10);
		
		ballBearing.Makesphere(50,50);		
		/*bar.MakeCylinder(25, 25);		
		wheel.Makesphere(10, 10);*/
		
		ballBearing.material=material;
		/*bar.material=material;
		wheel.material=material;*/
		
		allShapes.add(ballBearing);
		/*allShapes.add(bar);
		allShapes.add(wheel);*/

		world.add(ballBearing);
		/*ballBearing.add(bar);
		bar.add(wheel);*/

		for (Geometry shapes : allShapes) {
			shapes.makeTriangles();
			shapes.trapazoids = new ArrayList<double[][]>(shapes.triangles.size()*2);

			for (int i = 0; i < shapes.triangles.size() * 2; i++) {
				shapes.trapazoids.add(new double[4][6]);
			}
		}
	}

	public void setVertices(Geometry shape) {
		int n = shape.getNumChildren();
		for (int i = 0; i < n; i++) {
			Geometry child = shape.getChild(i);
			child.global.identity();
			child.global.rightMultiply(shape.global);
			child.global.rightMultiply(child.local);
			setVertices(child);
		}
	}
	public void rearrange() {
		if (a[1] > b[1]) {
			if (b[1] > c[1]) {
				// a>b>c
			} else {
				if (a[1] > c[1]) {
					double[] temp = b;
					b = c;
					c = temp;

					// a>c>b
				} else {
					double[] temp = a;
					a = c;
					c = b;
					b = temp;
					// c>a>b
				}
			}
		} else {
			if (c[1] > b[1]) {
				double[] temp;
				temp = a;
				a = c;
				c = temp;
				// c>b>a
			} else {
				if (a[1] > c[1]) {
					double[] temp;
					temp = b;
					b = a;
					a = temp;
					// b>a>c
				} else {
					double[] temp = c;
					c = a;
					a = b;
					b = temp;
					// b>c>a
				}
			}
		}
	}
	
	public double interpolate(double a, double b, double ty) {
		double x = (a + ty * (b - a));
		return x;
	}

	public void maketraps() {
		for (int index=0;index<allShapes.size();index++) {
			triangles = allShapes.get(index).triangles;
			trapazoids = allShapes.get(index).trapazoids;

			int i = 0;
			vertices = allShapes.get(index).vertices;
			m = allShapes.get(index).global;

			for (int[] tri : triangles) {
				m.transform(vertices[tri[0]], point0);
				m.transform(vertices[tri[1]], point1);
				m.transform(vertices[tri[2]], point2);

				allShapes.get(index).projectPoint(point0, a, lights);
				allShapes.get(index).projectPoint(point1, b, lights);
				allShapes.get(index).projectPoint(point2, c, lights);
				
				
				double area = ((a[0] - b[0]) * (a[1] + b[1])
						+ (b[0] - c[0]) * (b[1] + c[1]) + (c[0] - a[0])
						* (c[1] + a[1]));

				if ((area < 0)) {
					rearrange();

					double t = ((double) (b[1] - a[1]) / (c[1] - a[1]));
					double[] d = new double[6];
					for (int j = 0; j < 6; j++) {
						d[j] = (int) ((double) a[j] + t * (c[j] - a[j]));
					}
					d[1] = b[1];

					if (b[0] > d[0]) {
						trapazoids.get(i)[0] = d.clone();
						trapazoids.get(i)[1] = b.clone();
						trapazoids.get(i)[2] = a.clone();
						trapazoids.get(i)[3] = a.clone();
						i++;

						trapazoids.get(i)[0] = c.clone();
						trapazoids.get(i)[1] = c.clone();
						trapazoids.get(i)[2] = b.clone();
						trapazoids.get(i)[3] = d.clone();
						i++;

					} else {
						trapazoids.get(i)[0] = b.clone();
						trapazoids.get(i)[1] = d.clone();
						trapazoids.get(i)[2] = a.clone();
						trapazoids.get(i)[3] = a.clone();
						i++;
						trapazoids.get(i)[0] = c.clone();
						trapazoids.get(i)[1] = c.clone();
						trapazoids.get(i)[2] = d.clone();
						trapazoids.get(i)[3] = b.clone();
						i++;

					}
				}
			}
		}
	}

	static double count=0;
	
	public void initFrame(double time) {
		
		for (int i = 0; i < pixInverse.length; i++) {
			pixInverse[i] = false;
			zbuffer[i] = -500;
		}

		m = ballBearing.getMatrix();
		m.identity();
//		m.scale(0.5,0.5,0.5);
		/*m.rotateX(3*Math.PI/4);
		m.rotateY(-Math.PI*time/10);*/
		m.rotateY(time*5);
//		m.rotateX(time*5);
		
//		m.translate(Math.sin(time), 0, 0);
		
		count=count+1;
		//System.out.println(count);
/*		m = bar.getMatrix();
		m.identity();		
		m.translate(4, 0, 0);
		m.rotateY(Math.PI/2);
		m.scale(0.5, 0.5, 3);
		m.rotateZ(Math.PI*time/5);
		
		m=wheel.getMatrix();
		m.identity();
		m.translate(0, 0, -1);
		m.scale(5, 5, 0.1);*/
		
		
		world.getMatrix().identity();
		setVertices(world);

		maketraps();
	}
	
	public void computeImage(double time) {
		initFrame(time);
		double XLB, XRB, XRT, XLT;
		int YT, YB, XL, XR;
		double rL, rR, gL, gR, bL, bR, zL, zR;
		for (Geometry shapes : allShapes) {
			trapazoids = shapes.trapazoids;

			for (double[][] trap : trapazoids) {

				XLB = trap[3][0];
				XRB = trap[2][0];
				XRT = trap[1][0];
				XLT = trap[0][0];
				
				YB = (int) ((trap[2][1] >= trap[3][1]) ? trap[2][1] : trap[3][1]);
				YT = (int) ((trap[0][1] <= trap[1][1]) ? trap[0][1] : trap[1][1]);

				for (int y = YT; y <= YB; y++) {
					double t;
					if (YB == YT)
						t = 0.0;
					else
						t = ((double)(y - YT) / (YB - YT));
					XL = (int) (XLT + t * (XLB - XLT));
					XR = (int) (XRT + t * (XRB - XRT));

					rL = interpolate(trap[3][2], trap[0][2], t);
					rR = interpolate(trap[2][2], trap[1][2], t);
					gL = interpolate(trap[3][3], trap[0][3], t);
					gR = interpolate(trap[2][3], trap[1][3], t);
					bL = interpolate(trap[3][4], trap[0][4], t);
					bR = interpolate(trap[2][4], trap[1][4], t);
					zL = interpolate(trap[3][5], trap[0][5], t);
					zR = interpolate(trap[2][5], trap[1][5], t);

					for (int x = XL; x <= XR; x++) {
						if (((x + y * W) < W * H) && ((x + y * W) > 0)) {
							double t1 = 0.0;
							if (XR != XL)
								 t1 = ((double)(x - XL) / (XR - XL));
							else t=0.0;
							red = interpolate(rL, rR, t1);
							green = interpolate(gL, gR, t1);
							blue = interpolate(bL, bR, t1);
							zp = interpolate(zL, zR, t1);

							if (zp >= zbuffer[x + W * y]) {
								pix[x + y * W] = pack(red, green, blue);
								zbuffer[x + y * W] = zp;
								pixInverse[x + y * W] = true;
							}
						}
					}
				}

			}
		}
		int p = 0;
		for (int i = 0; i < H; i++)
		{
			for (int j = 0; j < W; j++) {
				if (!pixInverse[p]) {
					pix[p] = pack(255, 255, 255);
				}
				p++;
			}
		}
	}
}
