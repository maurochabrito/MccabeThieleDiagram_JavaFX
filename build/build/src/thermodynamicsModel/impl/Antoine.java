package thermodynamicsModel.impl;

import thermodynamicsModel.VaporPressureModel;

public class Antoine implements VaporPressureModel {
	Double A;
	Double B;
	Double C;
	public Antoine(Double a, Double b, Double c) {
		A = a;
		B = b;
		C = c;
	}
	@Override
	public Double temperature(Double pressure) {
		Double temperature = B/(A-Math.log(760.0*pressure))-C;//input pressure in atm
		return temperature;
	}
	@Override
	public Double pressure(Double temperature) {
		Double pressure = Math.exp(A-B/(temperature+C));
		return pressure/1.0;//return pressure in atm
	}
	
	
}
