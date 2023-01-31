package brain;

import thermodynamicsModel.GammaModel;
import thermodynamicsModel.VaporPressureModel;

public class QLine extends Line{
	public Double q;
	public Double z;
	
	public QLine(Double q, Double z) {
		super((q)/(q-1), -z/(q-1));
		this.q = q;
		this.z = z;
	}

	public void setQ(Double q) {
		this.q = q;
	}
	public void setZ(Double z) {
		this.z = z;
	}


	public Double intersectionEquilibriumLine(VaporPressureModel vpm1, VaporPressureModel vpm2, GammaModel gm, Double pressure) {
		if(q == 1) {
			return z;
		}else {
		//Line qLine = new Line(alpha, betha);
		//double xIntersected = qLine.nonElementarIntersection(gm, vpm1, vpm2, pressure);
		double xIntersected = this.nonElementarIntersection(gm, vpm1, vpm2, pressure);
		return xIntersected;
		}
	}
	@Override
	public Double intersection(Line otherLine) {
		Double X = 0.0; //initial X
		if(!(this.q == 1.0)) {
			//h -> Sensibility
			Boolean comparation = this.compareTo(otherLine, X);
			Boolean newComparation = this.compareTo(otherLine, X+h);
			while(newComparation == comparation) {
				X = X+h;
				comparation = newComparation;
				newComparation = this.compareTo(otherLine, X+h);
			}
			Double limitX = X;
			X = (limitX+X)/2;
			return X;
		}else {
			return z;
		}
	}
}
