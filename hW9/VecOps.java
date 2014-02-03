package hW9;

public class VecOps {

	public double dot(double[] a, double[] b) {
		double dotpdt = 0.0;
		for (int i = 0; i < a.length && i < b.length; i++) {
			dotpdt += a[i] * b[i];
		}
		return dotpdt;
	}

	public void normalize(double V[]) {
		double length = Math.sqrt(V[0] * V[0] + V[1] * V[1] + V[2] * V[2]);
		if (length != 0) {
			for (int i = 0; i < 3; i++) {
				V[i] = V[i] / length;
			}
		}
	}

	public void subtractV(double v[], double sphere[], double vs[]) {
		vs[0] = v[0] - sphere[0];
		vs[1] = v[1] - sphere[1];
		vs[2] = v[2] - sphere[2];
	}

	public double distVV(double[] v, double[] w) {
		return Math.sqrt(Math.pow(v[0] - w[0], 2) + Math.pow(v[1] - w[1], 2)
				+ Math.pow(v[2] - w[2], 2));
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

}
