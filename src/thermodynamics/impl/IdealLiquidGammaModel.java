package thermodynamics.impl;

import thermodynamics.GammaModel;

public class IdealLiquidGammaModel implements GammaModel {
	public IdealLiquidGammaModel() {
	}
	@Override
	public Double gamma(Integer index, Double X, Double T) {
		return 1.0;
	}

}
