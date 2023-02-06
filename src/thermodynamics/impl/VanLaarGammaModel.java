package thermodynamics.impl;

import thermodynamics.GammaModel;

public class VanLaarGammaModel implements GammaModel {
	Double A1;
	Double A2;
	
	public VanLaarGammaModel(Double a1, Double a2) {
		A1 = a1;
		A2 = a2;
	}

	@Override
	public Double gamma(Integer index, Double X, Double T) {
		if(index == 1) {
			Double gamma = (A2*(1-X)/(A1*X+A2*(1-X)));
			gamma = A1*Math.pow(gamma, 2.0);
			gamma = Math.exp(gamma);
			return gamma;
		}else {
			Double gamma = Math.exp(A2*Math.pow(((A1*X/(A1*X+A2*(1-X)))), 2.0));
			return gamma;
		}
	}

}
