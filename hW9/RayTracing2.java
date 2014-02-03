package hW9;

import hW5.ImprovedNoise;

public class RayTracing2 extends MISApplet {

	double dotpdt = 0.0;
	double[] s = new double[4];
	double[] vs = new double[4];
	double A, B, C;

	Sphere[] allSphere;

	int FL = 10;

	double[] v = new double[3];
	double[] w = new double[3];
	double[] nn = new double[3];
	double[] t = new double[2];

	int[] rgb = new int[3];

	Material m0, m1, m2, m3;

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

	VecOps vec = new VecOps();

	public void setPixel(int x, int y, int rgb) {
		if ((x + y * W) > 0 && (x + y * W) < pix.length) {
			// System.out.println("x "+x+" y "+y+" rgb "+rgb);
			pix[x + y * W] = rgb;
		}
	}

	Lights[] lights = { new Lights(), new Lights() };

	

	public void GammaCorrection(double src[], int dest[]) {
		for (int i = 0; i < 3; i++)
			dest[i] = (int) (255 * Math.pow(src[i], 0.45));
	}

	public boolean raytrace(double[] v, double[] w, double[] t, Sphere sphere) {
		vec.subtractV(v, sphere.center, vs);
		A = 1;
		B = 2 * vec.dot(w, vs);
		C = vec.dot(vs, vs) - sphere.radius * sphere.radius;
		return solveQuadEqn(A, B, C, t);
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
		// dir color

		lights[0].setDirection(new double[] { -1, 1, 1 });
		lights[1].setDirection(new double[] { 1, 1, 1 });
		lights[0].setColor(new double[] { 10, 10, 10 });
		lights[1].setColor(new double[] { 10, 10, 10 });
		lights[0].source=new double[]{-1,1,1};
		lights[1].source=new double[]{1,1,1};

		m0 = new Material();
		m1 = new Material();
		m2 = new Material();
		m3 = new Material();
		m0.setSpecularPower(10);
		m1.setSpecularPower(10);
		m2.setSpecularPower(10);
		m3.setSpecularPower(10);
		
		m0.setAmbient(new double[] { 0.0, 0.6, 0.6 });
		m0.setDiffuse(new double[] { 0.0, 0.0, 0.4 });
		m0.setSpecular(new double[] { 0.4, 0.6, 0.1 });
		m0.setMirrorColor(new double[] { 1, 1, 1 });
		
		m1.setAmbient(new double[] { 0.3, 0.0, 0.0 });
		m1.setDiffuse(new double[] { 0.0, 0.0, 0.4 });
		m1.setSpecular(new double[] { 0.1, 0.1, 0.1 });
		m1.setMirrorColor(new double[] { 0, 0, 0 });
		
		m2.setAmbient(new double[] { 0.3, 0.0, 0.0 });
		m2.setDiffuse(new double[] { 0.0, 0.0, 0.4 });
		m2.setSpecular(new double[] { 0.1, 0.1, 0.1 });
		m2.setMirrorColor(new double[] { 0, 0, 0 });

		m3.setAmbient(new double[] { 0.0, 0.6, 0.6 });
		m3.setDiffuse(new double[] { 0.0, 0.0, 0.4 });
		m3.setSpecular(new double[] { 0.4, 0.6, 0.1 });
		m3.setMirrorColor(new double[] { 1, 1, 1 });
		
		
		allSphere = new Sphere[4];
		for (int i = 0; i < allSphere.length; i++)
			allSphere[i] = new Sphere();
		allSphere[0].setCenter(0, 0, 0);
		allSphere[1].setCenter(-2, 0, 0);
		allSphere[2].setCenter(-2.5, 0, 0);
		allSphere[3].setCenter(0, -3, -6);
		
		allSphere[0].setRadius(1);
		allSphere[1].setRadius(0.5);
		allSphere[2].setRadius(0.5);
		allSphere[3].setRadius(1);
		
		allSphere[0].setMat(m0);
		allSphere[1].setMat(m1);
		allSphere[2].setMat(m2);
		allSphere[3].setMat(m3);
	}

	double dist=0;
	double time =0;
	public void computeImage(double time) {
		this.time=time;
		 
		 allSphere[1].center[0] = Math.sin(time)*2;
		 allSphere[1].center[1] = Math.cos(time)*2;
		 
		 allSphere[2].center[0] = Math.sin(time)*2.5;
		 allSphere[2].center[1] = Math.cos(time)*2.5;

		 allSphere[3].center[2] = Math.sin(time)*3;
//		 allSphere[0].center[2] = Math.cos(time)*4;
		

		for (int i = 0; i < H; i++)
			for (int j = 0; j < W; j++)
				setPixel(i, j, pack(0, 0, 0));
		for (int i = 0; i < W; i++)
			for (int j = 0; j < H; j++) {
				v[0] = 0;
				v[1] = 0;
				v[2] = FL;
				w[0] = (i - 0.5 * W) * FL / W;
				w[1] = (0.5 * H - j) * FL / W;
				w[2] = -1 * FL;
				vec.normalize(w);
				
				TraceMyRay(v, w, rgb, 0);
				
				this.setPixel(i, j, pack(rgb[0], rgb[1], rgb[2]));
			}

	}

	
	
	static int level = 0;;

	public boolean TraceMyRay(double[] v, double[] w, int[] rgb, int level) {

		// setPixel(i, j, pack(0,0,0));
		// set v & x

		min = Double.MAX_VALUE;
		index = -1;

		// find the sphere which is nearest to the eye on that pixel

		for (int m = 0; m < allSphere.length; m++) {
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
				// this nn point is a point on the sphere's surface
				nn[k] = (v[k] + min * w[k] - allSphere[index].center[k]);
				normal[k] = nn[k] / Math.sqrt(allSphere[index].radius);
				
			}
			// normalize(nn);
									
			PhongLightening(index, rgb);

			reflection(index, rgb, level);
			dist=vec.distVV(v,w);			
			//if(time%10>5)
			//	applyfog(rgb, dist, nn);
			
			return true;
		} else {// there is no sphere here
			//rgb[0] = rgb[1] = rgb[2] = 0;
			//sky
			rgb[0]=rgb[1]=100;
			
			rgb[2]=255;
			
			return false;
		}

		// i and j may have to be changed

	}

	public void applyfog(int []rgb, double dist, double[] v)
	{
		double k = 0.05;
		double[] fogcolor = {150,150,150};
		double a = Math.pow(2, -k * dist);
		double fx = ((double)v[0] - W/2) / W;
	    double fy = ((double)v[1] - H/2) / H;
	    double temp=Math.sin(ImprovedNoise.noise(4*fx, 4*fy, time*1000));
	    
		rgb[0] = (int) (((1 - a) * rgb[0] + a * fogcolor[0])*temp);
		rgb[1] = (int) (((1 - a) * rgb[1] + a * fogcolor[1])*temp);
		rgb[2] = (int) (((1 - a) * rgb[2] + a * fogcolor[2])*temp);
		
		//rgb[0]=rgb[1]=rgb[2]=temp;
	}
	
	double[] ReflectedRay = new double[3];
	double epsilon = 0.001;

	public void reflection(int index, int[] rgb, int level) {
		level++;
		if (level < 2) {
			if ((allSphere[index].getMat().mirrorColor[0] != 0)
					&& (allSphere[index].getMat().mirrorColor[1] != 0)
					&& (allSphere[index].getMat().mirrorColor[2] != 0)) {
				double dist = 2.0 * vec.dot(normal, w);
				ReflectedRay[0] = w[0] - (dist * normal[0]);
				ReflectedRay[1] = w[1] - (dist * normal[1]);
				ReflectedRay[2] = w[2] - (dist * normal[2]);

				double[] vv = { 0.0, 0.0, 0.0 };
				double[] ww = { 0.0, 0.0, 0.0 };
				vv[0] = nn[0] + (epsilon * w[0]);
				vv[1] = nn[1] + (epsilon * w[1]);
				vv[2] = nn[2] + (epsilon * w[2]);
				ww[0] = ReflectedRay[0];
				ww[1] = ReflectedRay[1];
				ww[2] = ReflectedRay[2];

				int[] refColor = { 0, 0, 0 };
				if (TraceMyRay(vv, ww, refColor, level)) {
					for (int i = 0; i < 3; i++) {
						rgb[i] = (int) (rgb[i]
								* (1.0 - allSphere[index].getMat().mirrorColor[i]) + refColor[i]
								* allSphere[index].getMat().mirrorColor[i]);
					}
				}
			}
		}
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
	double[] normal = new double[3];
	double[] lightsdimmed = new double[3];
	
	public void PhongLightening(int index, int[] rgb2) {
		Drgb = allSphere[index].getMat().getDiffuse();
		Argb = allSphere[index].getMat().getAmbient();
		Srgb = allSphere[index].getMat().getSpecular();
		p = allSphere[index].getMat().getSpecularPower();
		rgb2[0] = 0;
		rgb2[1] = 0;
		rgb2[2] = 0;
		uselessrgb[0] = 0;
		uselessrgb[1] = 0;
		uselessrgb[2] = 0;
		vec.normalize(nn);

		for (int l = 0; l < lights.length; l++) {
			// dir color
			double distill=vec.distVV(lights[l].source, nn);
			double d = Math.pow(distill, 1.5);
			//double d=1;
			lightsdimmed[0]=lights[l].color[0]/d;
			lightsdimmed[1]=lights[l].color[1]/d;
			lightsdimmed[2]=lights[l].color[2]/d;
			if (!isShadow(lights[l].getDirection())) {
				// 1) projection = max(0, Ldir.N)
				projection = Math.max(0, vec.dot(lights[l].getDirection(), normal));

				// 2) diffused = Drgb * max(0, Ldir.N)
				vec.multVS(Drgb, projection, diffused);

				// 3) reflection = 2 * (Ldir.N) * N - Ldir;
				vec.multVS(normal, 2 * projection, tempreflection);
				vec.subtractV(tempreflection, lights[l].getDirection(), reflection);
				vec.normalize(reflection);

				// 4) ref2Eye = max(0, reflection.eye)
				ref2Eye = Math.max(0, vec.dot(reflection, eye));

				// 5) specular = Srgb * (max(0, reflection.eye)^p)
				vec.multVS(Srgb, Math.pow(ref2Eye, p), specular);

				// 6) Lcolor*(Drgb*max(0, projection) + Srgb*max(0,
				// reflection.eye)^p )
				vec.addV(diffused, specular, CoeffLcolor);
				vec.multVV(CoeffLcolor, lightsdimmed, BigRGB);

				// 7) RGB = Argb + Lcolor*(Drgb*max(0, projection) + Srgb*max(0,
				// reflection.eye)^p )
				vec.addV(uselessrgb, BigRGB, uselessrgb);
			}
		}
		vec.addV(Argb, uselessrgb, uselessrgb);
		rgb2[0] += (int) (255 * Math.pow(uselessrgb[0], 0.45));
		rgb2[1] += (int) (255 * Math.pow(uselessrgb[1], 0.45));
		rgb2[2] += (int) (255 * Math.pow(uselessrgb[2], 0.45));
	}
}