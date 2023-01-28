package thermodynamicsModel.impl;

import thermodynamicsModel.GammaModel;

public class IdealLiquidGammaModel implements GammaModel {
	public IdealLiquidGammaModel() {
	}
	@Override
	public Double gamma(Integer index, Double X, Double T) {
		return 1.0;
	}

}
