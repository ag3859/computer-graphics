package hW8;

public class RayTracing1 extends MISApplet {

	double dotpdt = 0.0;
	double[] s = new double[4];
	double[] vs = new double[4];
	double A, B, C;

	double[][] allSphere = { { 1, 1, 0, 1 }, { -1, 1, 1, 0.5 } };

	int FL = 10;

	double[] v = new double[3];
	double[] w = new double[3];
	double[] nn = new double[3];
	double[] t = new double[2];

	int[] rgb = new int[3];

	Material m1, m2;

	double min;
	int index;

	double Argb[] = new double[3];
	double Drgb[] = new double[3];
	double Srgb[] = new double[3];

	double[] eye = { 0, 0, 1 };
	double ref2Eye = 0.0;
	double p;
	double specular[] = new double[3];
	double[] CoeffLcolor = new double[3];
	double[] BigRGB = new double[3];
	double projection;
	double[] diffused = new double[3];
	double[] reflection = new double[3];
	double[] tempreflection = new double[3];

	public double dot(double[] a, double[] b) {
		dotpdt = 0.0;
		for (int i = 0; i < a.length && i < b.length; i++) {
			dotpdt += a[i] * b[i];
		}
		return dotpdt;
	}

	public void setPixel(int x, int y, int rgb) {
		if ((x + y * W) > 0 && (x + y * W) < pix.length) {
			// System.out.println("x "+x+" y "+y+" rgb "+rgb);
			pix[x + y * W] = rgb;
		}
	}

	double[][][] lights = { { { -10, 10, 10 }, { 1.0, 1.0, 1.0 } },
			{ { -20, 10, 5 }, { 1.0, 1.0, 1.0 } }

	};

	public void normalize(double V[]) {
		double length = Math.sqrt(V[0] * V[0] + V[1] * V[1] + V[2] * V[2]);
		if (length != 0) {
			for (int i = 0; i < 3; i++) {
				V[i] = V[i] / length;
			}
		}
	}

	public void GammaCorrection(double src[], int dest[]) {
		for (int i = 0; i < 3; i++)
			dest[i] = (int) (255 * Math.pow(src[i], 0.45));
	}

	public boolean raytrace(double[] v, double[] w, double[] t, double[] sphere) {
		subtractV(v, sphere, vs);
		A = 1;
		B = 2 * dot(w, vs);
		C = dot(vs, vs) - sphere[3] * sphere[3];
		return solveQuadEqn(A, B, C, t);
	}

	public void subtractV(double v[], double sphere[], double vs[]) {
		vs[0] = v[0] - sphere[0];
		vs[1] = v[1] - sphere[1];
		vs[2] = v[2] - sphere[2];
	}

	public boolean solveQuadEqn(double A, double B, double C, double[] t) {
		double D = B * B - 4 * A * C;
		if (D >= 0) {
			D = Math.sqrt(D);
			t[0] = (-B - D) / (2 * A);
			t[1] = (-B + D) / (2 * A);
			return true;
		}
		return false;
	}

	public void initialize() {
		m1 = new Material();
		m2 = new Material();
		m1.setSpecularPower(10);
		m2.setSpecularPower(10);
		m1.setAmbient(new double[] { 0.6, 0.2, 0.3 });
		m1.setDiffuse(new double[] { 0.0, 0.0, 0.4 });
		m1.setSpecular(new double[] { 0.4, 0.6, 0.1 });
		m2.setAmbient(new double[] { 0.3, 0.0, 0.0 });
		m2.setDiffuse(new double[] { 0.0, 0.0, 0.4 });
		m2.setSpecular(new double[] { 0.1, 0.1, 0.1 });
		for (int i = 0; i < lights.length; i++) {
			normalize(lights[i][0]);
			normalize(lights[i][1]);
		}
	}

	public void computeImage(double time) {
		allSphere[0][1] = Math.cos(time);
		allSphere[1][1] = Math.sin(time);
		TraceMyRay();
	}

	public void TraceMyRay() {
		for (int i = 0; i < H; i++)
			for (int j = 0; j < W; j++)
				setPixel(i, j, pack(0, 0, 0));
		for (int i = 0; i < W; i++)
			for (int j = 0; j < H; j++) {
				// setPixel(i, j, pack(0,0,0));
				// set v & x
				v[0] = 0;
				v[1] = 0;
				v[2] = FL;
				w[0] = (i - 0.5 * W) * FL / H;// 0.8*((i+0.5)/W-0.5);//
				w[1] = (0.5 * W - j) * FL / H;// 0.8*((j+0.05)/W-0.5*H/W); //
				w[2] = -1 * FL;
				normalize(w);

				min = Double.MAX_VALUE;
				index = -1;

				// find the sphere which is nearest to the eye on that pixel

				for (int m = allSphere.length - 1; m >= 0; m--) {
					if (raytrace(v, w, t, allSphere[m])) {
						if (t[0] < min) {
							min = t[0];
							index = m;
						}
					}
				}

				if (index != -1) {
					for (int k = 0; k < 3; k++) {// mapping on the screen y =
													// mx+c
						nn[k] = v[k] + min * w[k] - allSphere[(int) index][k];
					}
					normalize(nn);
					if (index == 1)
						PhongLightening(m2);
					else
						PhongLightening(m1);
				} else {// there is no sphere here
					rgb[0] = rgb[1] = rgb[2] = 0;
				}

				// i and j may have to be changed
				this.setPixel(i, j, pack(rgb[0], rgb[1], rgb[2]));
			}
	}

	public void multVV(double v1[], double v2[], double dst[]) {
		for (int i = 0; i < v1.length; i++)
			dst[i] = v1[i] * v2[i];
	}

	public void multVS(double v[], double c, double dst[]) {
		for (int i = 0; i < v.length; i++)
			dst[i] = v[i] * c;
	}

	public void addV(double a[], double b[], double result[]) {
		result[0] = a[0] + b[0];
		result[1] = a[1] + b[1];
		result[2] = a[2] + b[2];
	}

	public void addV(double a[], double b[], int result[]) {
		result[0] = (int) (a[0] + b[0]);
		result[1] = (int) (a[1] + b[1]);
		result[2] = (int) (a[2] + b[2]);
	}

	public boolean isShadow(double light[]) {
		for (int i = 0; i < allSphere.length; i++) {
			if (i != index) {
				if (raytrace(light, nn, t, allSphere[i]) && t[0] > 0)
					return true;
			}
		}
		return false;
	}

	double[] uselessrgb = new double[3];

	public void PhongLightening(Material material) {
		Drgb = material.getDiffuse();
		Argb = material.getAmbient();
		Srgb = material.getSpecular();
		p = material.getSpecularPower();
		rgb[0] = 0;
		rgb[1] = 0;
		rgb[2] = 0;
		uselessrgb[0] = 0;
		uselessrgb[1] = 0;
		uselessrgb[2] = 0;
		for (int l = 0; l < lights.length; l++) {
			if (!isShadow(lights[l][0])) {
				projection = Math.max(0, dot(lights[l][0], nn)); // ////
				multVS(Drgb, projection, diffused);
				multVS(nn, 2 * projection, tempreflection); // ///
				subtractV(tempreflection, lights[l][0], reflection);// ///
				normalize(reflection); // //

				ref2Eye = Math.max(0, dot(reflection, eye)); // ///
				multVS(Srgb, Math.pow(ref2Eye, p), specular); // ////

				addV(diffused, specular, CoeffLcolor);
				multVV(CoeffLcolor, lights[l][1], BigRGB);
				addV(uselessrgb, BigRGB, uselessrgb);
			}
		}
		addV(Argb, uselessrgb, uselessrgb);
		rgb[0] += (int) (255 * Math.pow(uselessrgb[0], 0.45));
		rgb[1] += (int) (255 * Math.pow(uselessrgb[1], 0.45));
		rgb[2] += (int) (255 * Math.pow(uselessrgb[2], 0.45));
	}
}