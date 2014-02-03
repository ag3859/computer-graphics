package hW7;

import java.util.ArrayList;

public class Geometry implements IGeometry {

	Matrix global, local;

	double vertices[][];
	int faces[][];

	double[] point0 = new double[6]; // empty, in which coordinates after matrix
										// transformations will come
	double[] point1 = new double[6];

	int[] a = new int[6]; // empty, in which final coordinates will come which
							// will then be drawn
	int[] b = new int[6]; // after using project point function

	double FL = 10.0;

	int w, h;
	int m, n;

	ArrayList<Geometry> kids = new ArrayList<Geometry>();
	ArrayList<int[]> triangles = new ArrayList<int[]>();
	ArrayList<double[][]> trapazoids;

	int[] selfColor;

	Material material = new Material();

	public Geometry(int h, int w) {
		global = new Matrix();
		global.identity();
		local = new Matrix();
		local.identity();
		this.h = h;
		this.w = w;
	}

	public void makeTriangles() {
		for (int i = 0; i < faces.length; i++) {
			triangles.add(new int[] { faces[i][0], faces[i][1], faces[i][2] });
			triangles.add(new int[] { faces[i][0], faces[i][2], faces[i][3] });
		}

	}

	public void normalize(double pt[]) {
		/*
		 * double length = Math .sqrt(pt[0] * pt[0] + pt[1] * pt[1] + pt[2] *
		 * pt[2]);
		 */
		double length = 0;
		for (int i = 0; i < 3; i++)
			length = length + pt[i] * pt[i];
		length = Math.sqrt(length);
		if (length != 0)
			for (int i = 0; i < 3; i++) {
				pt[i] = pt[i] / length;
			}
	}

	public void subtractV(double a[], double b[], double result[]) {
		result[0] = a[0] - b[0];
		result[1] = a[1] - b[1];
		result[2] = a[2] - b[2];
	}

	public void addV(double a[], double b[], double result[]) {
		result[0] = a[0] + b[0];
		result[1] = a[1] + b[1];
		result[2] = a[2] + b[2];
	}

	public void multVV(double v1[], double v2[], double dst[]) {
		for (int i = 0; i < v1.length; i++)
			dst[i] = v1[i] * v2[i];
	}

	public void multVS(double v[], double c, double dst[]) {
		for (int i = 0; i < v.length; i++)
			dst[i] = v[i] * c;
	}

	double[] Argb = { 0, 0, 0 };
	double[] Drgb = new double[3];
	double[] Srgb = new double[3];
	double[] RGB = new double[3];
	double[] nVector = new double[3];
	double ref2Eye = 0.0;
	double projection = 0.0;
	double[] eye = { 0, 0, 1 };
	double reflection[] = new double[3];
	double tempreflection[] = new double[3];
	double diffused[] = new double[3];
	double specular[] = new double[3];
	
	double Ldiffused[] = new double[3];
	double Lspecular[] = new double[3];	
	double[] CoeffLcolor=new double[3];
	double[] BigRHS=new double[3];
	double p;

	public void vertexColor(double xyz[], double[][][] lights) {

		Argb = material.getAmbient();
		Drgb = material.getDiffuse();
		Srgb = material.getSpecular();
		p = material.getSpecularPower();

		nVector[0] = xyz[3];
		nVector[1] = xyz[4];
		nVector[2] = xyz[5];

		normalize(nVector);

		for (int i = 0; i < lights.length; i++) {
			uselessrgb[0] = 0;
			uselessrgb[1] = 0;
			uselessrgb[2] = 0;
			
			normalize(lights[i][0]);
			normalize(lights[i][1]);

			// 1) projection = max(0, Ldir.N)
			projection = Math.max(0, dotProduct(lights[i][0], nVector));

			// 2) diffused = Drgb * max(0, Ldir.N)
			multVS(Drgb, projection, diffused);

			// 3) reflection = 2 * (Ldir.N) * N - Ldir;
			multVS(nVector, 2 * projection, tempreflection);
			subtractV(tempreflection, lights[i][0], reflection);
			normalize(reflection);

			// 4) ref2Eye = max(0, reflection.eye)
			ref2Eye = Math.max(0, dotProduct(reflection, eye));

			// 5) specular = Srgb * (max(0, reflection.eye)^p)
			multVS(Srgb, Math.pow(ref2Eye, p), specular);

			// 6) Lcolor*(Drgb*max(0, projection) + Srgb*max(0, reflection.eye)^p )
			addV(diffused, specular, CoeffLcolor);
			multVV(CoeffLcolor, lights[i][1], BigRHS);
	
			// 7) RGB = Argb + Lcolor*(Drgb*max(0, projection) + Srgb*max(0, reflection.eye)^p )
			addV(uselessrgb, BigRHS, uselessrgb);
		}
		addV(Argb, uselessrgb, RGB);
	}
	double []uselessrgb=new double[3];
	public void projectPoint(double[] xyz, double[] pxy, double[][][] lights) {

		// INPUT: YOUR POINT IN 3D

		double x = xyz[0];
		double y = xyz[1];
		double z = xyz[2];

		// OUTPUT: PIXEL COORDINATES TO DISPLAY YOUR POINT

		pxy[0] = w / 2 + (int) (h * x / (FL - z));
		pxy[1] = h / 2 - (int) (h * y / (FL - z));

		// putting colors to normals.
		vertexColor(xyz, lights);
		//(int) (255 * Math.pow(n, 0.45))
		pxy[2] = (255*Math.pow(RGB[0],0.45));
		pxy[3] = (255*Math.pow(RGB[1],0.45));
		pxy[4] = (255*Math.pow(RGB[2],0.45));

		pxy[5] = (int) (FL * z / (FL - z));
	}

	public double dotProduct(double[] src1, double[] src2) {
		double dotProductResult = 0.0;
		for (int i = 0; i < src1.length && i < src2.length; i++) {
			dotProductResult += src1[i] * src2[i];
		}
		return dotProductResult;
	}

	public void setFaces() {
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++) {
				faces[i + (m * j)][0] = i + (m + 1) * j;
				faces[i + (m * j)][1] = (i + 1) + (m + 1) * j;
				faces[i + (m * j)][2] = (i + 1) + (m + 1) * (j + 1);
				faces[i + (m * j)][3] = i + (m + 1) * (j + 1);
			}
	}

	public void MakeCube() {
		double vertices[][] = {
				{ 1, 1, 1, 0, 0, 1 },
				{ -1, 1, 1, 0, 0, 1 },
				{ -1, -1, 1, 0, 0, 1 },
				{ 1, -1, 1, 0, 0, 1 }, // front
				{ 1, 1, -1, 0, 1, 0 },
				{ -1, 1, -1, 0, 1, 0 },
				{ -1, 1, 1, 0, 1, 0 },
				{ 1, 1, 1, 0, 1, 0 },// top
				{ -1, 1, -1, 0, 0, -1 },
				{ 1, 1, -1, 0, 0, -1 },
				{ 1, -1, -1, 0, 0, -1 },
				{ -1, -1, -1, 0, 0, -1 }, // back
				{ 1, 1, -1, 1, 0, 0 },
				{ 1, 1, 1, 1, 0, 0 },
				{ 1, -1, 1, 1, 0, 0 },
				{ 1, -1, -1, 1, 0, 0 }, // right
				{ -1, -1, -1, 0, -1, 0 }, { 1, -1, -1, 0, -1, 0 },
				{ 1, -1, 1, 0, -1, 0 },
				{ -1, -1, 1, 0, -1, 0 },// bottom
				{ -1, 1, 1, -1, 0, 0 }, { -1, 1, -1, -1, 0, 0 },
				{ -1, -1, -1, -1, 0, 0 }, { -1, -1, 1, -1, 0, 0 } // left
		};
		int faces[][] = { { 0, 1, 2, 3 }, { 4, 5, 6, 7 }, { 8, 9, 10, 11 },
				{ 12, 13, 14, 15 }, { 16, 17, 18, 19 }, { 20, 21, 22, 23 } };
		this.vertices = vertices;
		this.faces = faces;
	}

	public void MakeTriangles() {
		double vertices[][] = { 
				{ 1, 0, 0, 0, 0, -1 }, { 0, 1, 0, 0, 0, -1 }, { -1, 0, 0, 0, 0, -1 }, 
				{ 1, 0, 1, 0, 0, -1 }, { 0, -1, 1, 0, 0, -1 }, { -1, 0, 1, 0, 0, -1 } 
				};
		int faces[][] = { { 0, 1, 2 } };
		this.vertices = vertices;
		this.faces = faces;
	}

	public void MakeSquare() {
		double vertices[][] = { { 1, 1, 1, 0, 0, 1 }, { -1, 1, 1, 0, 0, 1 },
				{ -1, -1, 1, 0, 0, 1 },
				{ 1, -1, 1, 0, 0, 1 }, // front
				{ -1, 1, -1, 0, 0, -1 }, { 1, 1, -1, 0, 0, -1 },
				{ 1, -1, -1, 0, 0, -1 }, { -1, -1, -1, 0, 0, -1 }, // back
		};
		int faces[][] = { { 0, 1, 2, 3 }, { 4, 5, 6, 7 } };
		this.vertices = vertices;
		this.faces = faces;
	}

	public void Makesphere(int m1, int n1) {
		m = m1;
		n = n1;
		vertices = new double[(m + 1) * (n + 1)][6];
		faces = new int[m * n][4];

		double theta, phi;

		for (int i = 0; i <= m; i++)
			for (int j = 0; j <= n; j++) {
				theta = 2 * Math.PI * i / m;
				phi = -(Math.PI / 2) + (j * Math.PI / n);

				vertices[i + (m + 1) * j][0] = Math.cos(theta) * Math.cos(phi);
				vertices[i + (m + 1) * j][1] = Math.cos(phi) * Math.sin(theta);
				vertices[i + (m + 1) * j][2] = Math.sin(phi);
				vertices[i + (m + 1) * j][3] = vertices[i + (m + 1) * j][0];
				vertices[i + (m + 1) * j][4] = vertices[i + (m + 1) * j][1];
				vertices[i + (m + 1) * j][5] = vertices[i + (m + 1) * j][2];
			}

		setFaces();
	}

	public void MakeCylinder(int m1, int n1) {
		m = m1;
		n = n1;
		vertices = new double[(m + 1) * (n + 1)][6];
		faces = new int[m * n][4];

		double theta, z;

		for (int i = 0; i <= m; i++)
			for (int j = 0; j <= n; j++) {
				theta = 2 * Math.PI * ((double) i / n);
				z = ((double) j / n) < 0.5 ? -1 : 1;

				double rv = ((((double) j / n == 0) || ((double) j / n == 1)) ? 0
						: 1);

				vertices[i + (m + 1) * j][0] = Math.cos(theta) * rv;
				vertices[i + (m + 1) * j][1] = Math.sin(theta) * rv;
				vertices[i + (m + 1) * j][2] = z;
				vertices[i + (m + 1) * j][3] = Math.cos(theta) * rv;
				vertices[i + (m + 1) * j][4] = Math.sin(theta) * rv;
				vertices[i + (m + 1) * j][5] = z;// (rv==0?1:0);

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
