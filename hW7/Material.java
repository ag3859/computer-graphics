package hW7;

public class Material {

	  double ambient[] = {0.0, 0.0, 0.0};
	  double diffuse[] = {0.0, 0.0, 0.0};
	  double specular[] = {0.0, 0.0, 0.0};
	  double specularPower = 10;
	public double[] getAmbient() {
		return ambient;
	}
	public void setAmbient(double[] ambient) {
		this.ambient = ambient;
	}
	public double[] getDiffuse() {
		return diffuse;
	}
	public void setDiffuse(double[] diffuse) {
		this.diffuse = diffuse;
	}
	public double[] getSpecular() {
		return specular;
	}
	public void setSpecular(double[] specular) {
		this.specular = specular;
	}
	public double getSpecularPower() {
		return specularPower;
	}
	public void setSpecularPower(double specularPower) {
		this.specularPower = specularPower;
	}

}
