package brain;

import thermodynamicsModel.GammaModel;
import thermodynamicsModel.RaoultLaw;
import thermodynamicsModel.VaporPressureModel;

public class Line {
	protected final Double h = 0.00001;
	protected Double alpha;
	protected Double betha;
	
	public Line(Double alpha, Double betha) {
		this.alpha = alpha;
		this.betha = betha;
	}
	
	public Double getAlpha() {
		return alpha;
	}

	public void setAlpha(Double alpha) {
		this.alpha = alpha;
	}

	public Double getBetha() {
		return betha;
	}

	public void setBetha(Double betha) {
		this.betha = betha;
	}

	public Double y(Double X) {
		Double y = alpha*X+betha;
		return y;
	}
	public Double x(Double Y) {
		Double x = (Y-betha)/alpha;
		return x;
	}
	public Boolean compareTo(Line otherLine, Double X) {
		return this.y(X) > otherLine.y(X);
	}
	public Double intersection(Line otherLine) {
		Double X = 0.0; //initial X
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
	}
	public Double nonElementarIntersection(GammaModel gm, VaporPressureModel vpm1, VaporPressureModel vpm2, Double pressure) {
		Double X = 0.5;
		Double Xa = 0.0;
		Double Xb = 1.0;
		for(int i = 0; i< 40; i++) {
			Boolean comparation = this.y(X) > RaoultLaw.iterativeY(X, vpm1, vpm2, gm, pressure);
			Boolean newComparation = this.y(Xb) > RaoultLaw.iterativeY(Xb, vpm1, vpm2, gm, pressure);
			if (newComparation == comparation) {
				Xb = X;
				X = (Xa+X)/2;
			}else {
				Xa = X;
				X = (Xb+X)/2;
			}
		}
		return X;
	}
}
