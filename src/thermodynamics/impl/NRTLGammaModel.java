package thermodynamics.impl;

import thermodynamics.GammaModel;

public class NRTLGammaModel implements GammaModel {
	Double A1;
	Double A2;
	Double dG1;
	Double dG2;

	public NRTLGammaModel(Double a1, Double a2, Double dG1, Double dG2) {
		super();
		A1 = a1;
		A2 = a2;
		this.dG1 = dG1;
		this.dG2 = dG2;
	}

	@Override
	public Double gamma(Integer index, Double X, Double T) {
		Double X2 = 1-X;
		Double X1 = X;
		Double tau1 = -0.81+dG1/(T);
		Double tau2 = 3.46+dG2/(T);
		Double G1 = Math.exp(-A1*tau1);
		Double G2 = Math.exp(-A2*tau2);
		if(index == 1) {
			Double f1 = tau2*(G2/(X1+X2*G2))*(G2/(X1+X2*G2));
			Double f2 = tau1*G1/Math.pow((X2+X1*G1), 2);
			Double gamma = Math.exp(X2*X2*(f1+f2));
			return gamma;
		}else {
			Double f1 = tau1*(G1/(X2+X1*G1))*(G1/(X2+X1*G1));
			Double f2 = tau2*G2/Math.pow((X1+X2*G2), 2);
			Double gamma = Math.exp(X1*X1*(f1+f2));
			return gamma;
		}
	}

}
