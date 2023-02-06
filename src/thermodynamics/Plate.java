package thermodynamics;

import java.io.Serializable;

public class Plate implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Double X;
	private Double Y;
	private Double temperature;
	
	public Plate(Integer id, Double x, Double y, Double temperature) {
		this.id = id;
		X = x;
		Y = y;
		this.temperature = temperature;
	}
	public Plate(Double x, Double y, Double temperature) {
		X = x;
		Y = y;
		this.temperature = temperature;
	}
	public Double getX() {
		return X;
	}
	public Double getY() {
		return Y;
	}
	public Double getTemperature() {
		return temperature;
	}
	@Override
	public String toString() {
		String toString = "Plate Number "+id+":"
				+ " X = "+String.format("%.3f",X)+" Y = "+String.format("%.3f", Y)+" T = "+String.format("%.2f", temperature)+"K";
		return toString;
	}
	
}
