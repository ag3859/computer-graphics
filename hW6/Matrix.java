package hW6;

public class Matrix implements IMatrix {

	double[][] matrix;
	double[][] inverseMatrix = new double[4][4];
	double[][] inverseMatrixTranspose = new double[4][4];
	double[][] copy = new double[4][4];
	double[] temp2 = new double[4];
	//double[][] temp = new double[4][4];
	
	static IMatrix temp = new Matrix();
	
	int r, c;

	public Matrix() {
		matrix = new double[4][4];
		r = 4;
		c = 4;
	}

	public Matrix(int r, int c) {
		matrix = new double[4][4];
		this.r = r;
		this.c = c;
	}

	public int getNrows() {
		return r;
	}

	public int getNcols() {
		return c;
	}

	public Matrix(double[][] transform) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				// System.err.println(i+" , "+j);
				// this.matrix[i][j]=transform[i][j];
				this.set(j, i, transform[i][j]);
			}
		}
	}

	@Override
	public void identity() {
		// TODO Auto-generated method stub

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				if (i == j) {
					matrix[i][j] = 1;
					this.set(j, i, 1);
				} else
					this.set(j, i, 0);

	}

	@Override
	public void set(int col, int row, double value) {
		// TODO Auto-generated method stub
		this.matrix[row][col] = value;
	}

	@Override
	public double get(int col, int row) {
		// TODO Auto-generated method stub
		return this.matrix[row][col];
	}

	@Override
	public void translate(double x, double y, double z) {
		// TODO Auto-generated method stub
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++)
				temp.set(j,i,0);
		temp.set(0,0,1);
		temp.set(1,1,1);
		temp.set(2, 2, 1);
		temp.set(3,0,x);
		temp.set(3,1,y);
		temp.set(3, 2, z);
		temp.set(3, 3, 1);

		this.rightMultiply(temp);
	}

	@Override
	public void rotateX(double radians) {
		// TODO Auto-generated method stub
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++)
				temp.set(j, i, 0);
		temp.set(0,0,1);
		temp.set(1, 1, Math.cos(radians));
		temp.set(2,2,Math.cos(radians));

		temp.set(2,1, -Math.sin(radians));
		temp.set(1, 2, Math.sin(radians));

		temp.set(3,3,1);

		this.rightMultiply(temp);
	}

	@Override
	public void rotateY(double radians) {
		// TODO Auto-generated method stub
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++)
				temp.set(j, i, 0);
		temp.set(0,0, Math.cos(radians));
		temp.set(2, 0, Math.sin(radians));
		temp.set(1,1,1);

		temp.set(0,2, -Math.sin(radians));
		temp.set(2, 2, Math.cos(radians));

		temp.set(3,3,1);

		this.rightMultiply(temp);
	}

	@Override
	public void rotateZ(double radians) {
		// TODO Auto-generated method stub
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++)
				temp.set(j, i, 0);
		temp.set(0,0,Math.cos(radians));
		temp.set(1, 0, -Math.sin(radians));
		temp.set(2,2,1);

		temp.set(0,1, Math.sin(radians));
		temp.set(1, 1, Math.cos(radians));

		temp.set(3,3,1);

		this.rightMultiply(temp);
	}

	@Override
	public void scale(double x, double y, double z) {
		// TODO Auto-generated method stub
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++)
				temp.set(j,i,0);
		temp.set(0,0,x);
		temp.set(1,1,y);
		temp.set(2,2,z);
		temp.set(3, 3, 1);

		this.rightMultiply(temp);
	}

	@Override
	public void leftMultiply(IMatrix other) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rightMultiply(IMatrix other) {
		// TODO Auto-generated method stub
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++)
				copy[i][j]=0;
				
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 4; k++) {
					copy[i][j]=copy[i][j] + this.get(k, i) * other.get(j, k);
				}
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				this.set(j, i, copy[i][j]);
			}
		}
	}

	@Override
	public void transform(double[] src, double[] dst) {
		// TODO Auto-generated method stub
		
		
		for (int i = 0; i < 3; i++)
			temp2[i] = src[i];

		temp2[3] = 1;

		for (int i = 0; i < 3; i++) {
			dst[i] = 0;
			for (int j = 0; j < 4; j++) {
				dst[i] += matrix[i][j] * temp2[j];
			}
		}

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				copy[i][j] = matrix[i][j];
			}
		}
		
		Invert.invert(copy, inverseMatrix);
		transpose(inverseMatrix, inverseMatrixTranspose);
		
		for (int i = 0; i < 3; i++)
			temp2[i] = src[i+3];
		temp2[3]=0;
		
		for (int i = 0; i < 3; i++) {
			dst[i+3] = 0;
			for (int j = 0; j < 4; j++) {
				dst[i+3] += inverseMatrixTranspose[i][j] * temp2[j];
			}
		}		
	}

	public void transpose(double src[][], double dst[][]) 
	{
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				dst[i][j] = src[j][i];
			}
		}
	}
}
