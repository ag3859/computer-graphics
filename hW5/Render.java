package hW5;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Render extends MISApplet {

	Geometry world;
	Geometry ballBearing;
	Geometry bar;
	Geometry wheel;

	Matrix m = new Matrix();

	ArrayList<Geometry> allShapes = new ArrayList<Geometry>();

	double[] point0 = new double[3]; // empty, in which coordinates after matrix
										// transformations will come
	double[] point1 = new double[3];
	double[] point2 = new double[3];

	int[] a = new int[2]; // empty, in which final coordinates will come which
							// will then be drawn
	int[] b = new int[2]; // after using project point function
	int[] c = new int[2];

	ArrayList<int[]> triangles;
	double[][] vertices;
	ArrayList<int[][]> trapazoids;

	public void initialize() {
		world = new Geometry(H, W);
		ballBearing = new Geometry(H, W);
		bar = new Geometry(H, W);
		wheel = new Geometry(H, W);

		ballBearing.MakeCylinder(50, 50);
		ballBearing.selfColor = new int[] { 50, 50, 500 };

		bar.MakeCube();
		bar.selfColor = new int[] { 100, 50, 25 };

		allShapes.add(ballBearing);
		allShapes.add(bar);

		world.add(ballBearing);
		ballBearing.add(bar);

		for (Geometry shapes : allShapes)
		{
			shapes.makeTriangles();
			shapes.trapazoids= new ArrayList<int[][]>();
			for(int i=0;i<shapes.triangles.size()*2;i++)
			{
				shapes.trapazoids.add(new int[4][2]);
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
					int[] temp = b;
					b = c;
					c = temp;

					// a>c>b
				} else {
					int[] temp = a;
					a = c;
					c = b;
					b = temp;
					// c>a>b
				}
			}
		} else {
			if (c[1] > b[1]) {
				int[] temp;
				temp = a;
				a = c;
				c = temp;
				// c>b>a
			} else {
				if (a[1] > c[1]) {
					int[] temp;
					temp = b;
					b = a;
					a = temp;
					// b>a>c
				} else {
					int[] temp = c;
					c = a;
					a = b;
					b = temp;
					// b>c>a
				}
			}
		}
	}

	public void maketraps() {
		for(int index =0;index<allShapes.size();index++){
			triangles=allShapes.get(index).triangles;
			trapazoids=allShapes.get(index).trapazoids;

			int i=0;
			vertices=allShapes.get(index).vertices;
			m=allShapes.get(index).global;

			for (int[] tri : triangles) {
				m.transform(vertices[tri[0]], point0);
				m.transform(vertices[tri[1]], point1);
				m.transform(vertices[tri[2]], point2);

				allShapes.get(index).projectPoint(point0, a);
				allShapes.get(index).projectPoint(point1, b);
				allShapes.get(index).projectPoint(point2, c);

				int area = Integer.signum((a[0] - b[0]) * (a[1] + b[1])
						+ (b[0] - c[0]) * (b[1] + c[1]) + (c[0] - a[0])
						* (c[1] + a[1]));

				if ((area < 0)) {
					rearrange();

					double t = ((double) (b[1] - a[1]) / (c[1] - a[1]));
					int d = (int) (a[0] + t * (c[0] - a[0]));

					if (b[0] > d) {
						trapazoids.get(i)[0][0]=d;
						trapazoids.get(i)[0][1]=b[1];
						trapazoids.get(i)[1][0]=b[0];
						trapazoids.get(i)[1][1]=b[1];
						trapazoids.get(i)[2][0]=a[0];
						trapazoids.get(i)[2][1]=a[1];
						trapazoids.get(i)[3][0]=a[0];
						trapazoids.get(i)[3][1]=a[1];
						i++;
											
						trapazoids.get(i)[0][0]=c[0];
						trapazoids.get(i)[0][1]=c[1];
						trapazoids.get(i)[1][0]=c[0];
						trapazoids.get(i)[1][1]=c[1];
						trapazoids.get(i)[2][0]=b[0];
						trapazoids.get(i)[2][1]=b[1];
						trapazoids.get(i)[3][0]=d;
						trapazoids.get(i)[3][1]=b[1];
						i++;
						
					} else {
						trapazoids.get(i)[0][0]=b[0];
						trapazoids.get(i)[0][1]=b[1];
						trapazoids.get(i)[1][0]=d;
						trapazoids.get(i)[1][1]=b[1];
						trapazoids.get(i)[2][0]=a[0];
						trapazoids.get(i)[2][1]=a[1];
						trapazoids.get(i)[3][0]=a[0];
						trapazoids.get(i)[3][1]=a[1];
						i++;
						
						trapazoids.get(i)[0][0]=c[0];
						trapazoids.get(i)[0][1]=c[1];
						trapazoids.get(i)[1][0]=c[0];
						trapazoids.get(i)[1][1]=c[1];
						trapazoids.get(i)[2][0]=d;
						trapazoids.get(i)[2][1]=b[1];
						trapazoids.get(i)[3][0]=b[0];
						trapazoids.get(i)[3][1]=b[1];
						i++;
						
					}
				}
			}
			allShapes.get(index).trapazoids=trapazoids;
		}
	}

	public void initFrame(double time) {

		for (int i = 0; i < pixInverse.length; i++) {
			pixInverse[i] = false;
		}

		m = ballBearing.getMatrix();
		m.identity();
		m.rotateX(time);
		
		m = bar.getMatrix(); 
		m.identity(); 
		m.translate(3, 0, 0);
		
		
		world.getMatrix().identity();
		setVertices(world);
		
		maketraps();
	}

	public void computeImage(double time) {
		initFrame(time);
		double XLB, XRB, XRT, XLT;
		int YT, YB, XL, XR;

		for (Geometry shapes : allShapes) {
			trapazoids = shapes.trapazoids;

			for (int[][] trap : trapazoids) {
				
				XLB = trap[3][0];
				XRB = trap[2][0];
				XRT = trap[1][0];
				XLT = trap[0][0];


				YB = (trap[2][1] >= trap[3][1]) ? trap[2][1] : trap[3][1];
				YT = (trap[0][1] <= trap[1][1]) ? trap[0][1] : trap[1][1];
				
				for (int y = YT; y <= YB; y++) {
					double t;
					if (YB == YT)
						t = 0;
					else
						t = ((double)(y - YT) / (YB - YT));
					XL = (int) (XLT + t * (XLB - XLT));
					XR = (int) (XRT + t * (XRB - XRT));

					for (int x = XL; x <= XR; x++) {
						if (((x + y * W) < W * H) && ((x + y * W) > 0)) {
							pix[x + y * W] = pack(shapes.selfColor[0],
									shapes.selfColor[1], shapes.selfColor[2]);
							pixInverse[x + y * W] = true;
						}
					}
				}
			}
		}
		int p = 0;
		for (int i = 0; i < H; i++)
			for (int j = 0; j < W; j++) {
				if (!pixInverse[p]) {
					pix[p] = pack(0, 255, 0);
				}
				p++;
			}
	}
}
