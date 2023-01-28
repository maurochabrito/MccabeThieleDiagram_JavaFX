package brain;

import thermodynamicsModel.GammaModel;
import thermodynamicsModel.VaporPressureModel;

public class AsymptoticLine extends QLine {
	Double XLimit;
	public AsymptoticLine(Double XLimit) {
		super(1.0, XLimit);
		this.XLimit = XLimit;
	}
	@Override
	public Double intersection(Line otherLine) {
		return this.XLimit;
	}
	public Double nonElementarIntersection(GammaModel gm, VaporPressureModel vpm1, VaporPressureModel vpm2, Double pressure) {
		return this.XLimit;
	}
}
