package thermodynamics;

public class RaoultLaw {
	
	public static Double y(Double X, Double T, VaporPressureModel vpm1, VaporPressureModel vpm2, GammaModel gm, Double pressure) {
		Double y = gm.gamma(1, X, T)*X*vpm1.pressure(T)/pressure;
		return y;
	}
	public static Double x(Double Y, Double previousX, Double T, VaporPressureModel vpm1, VaporPressureModel vpm2, GammaModel gm, Double pressure) {
		Double x = Y*pressure/(gm.gamma(1, previousX, T)*vpm1.pressure(T));
		return x;
	}
	public static Double iterativeY(Double X, VaporPressureModel vpm1, VaporPressureModel vpm2, GammaModel gm, Double pressure) {
		Double T = vpm1.temperature(pressure)*X+vpm2.temperature(pressure)*(1-X); //First choice
		Double Y = y(X, T, vpm1, vpm2, gm, pressure);
		for (int i = 1; i < 10; i++) {
			T = T-f(X, T, vpm1, vpm2, gm, pressure)/df(X, T, vpm1, vpm2, gm, pressure);
		}
		Y = y(X, T, vpm1, vpm2, gm, pressure);
		return Y;
	}
	public static Double iterativeY_T(Double X, VaporPressureModel vpm1, VaporPressureModel vpm2, GammaModel gm, Double pressure) {
		Double T = vpm1.temperature(pressure)*X+vpm2.temperature(pressure)*(1-X); //First choice
		Double Y = y(X, T, vpm1, vpm2, gm, pressure);
		for (int i = 1; i < 10; i++) {
			T = T-f(X, T, vpm1, vpm2, gm, pressure)/df(X, T, vpm1, vpm2, gm, pressure);
		}
		return T;
	}
	public static Double iterativeX(Double Y, Double previousX, VaporPressureModel vpm1, VaporPressureModel vpm2, GammaModel gm, Double pressure) {
		Double T = vpm1.temperature(pressure)*Y+vpm2.temperature(pressure)*(1-Y); //First choice
		Double X = x(Y, previousX, T, vpm1, vpm2, gm, pressure);
		Double p2 = 0.0; //Just for initiate it
		for (int i = 1; i < 10; i++) {
			p2 = (1-Y)*pressure/(gm.gamma(1,1-X,T));
			T = T-f(X, T, vpm1, vpm2, gm, pressure)/df(X, T, vpm1, vpm2, gm, pressure);
			X = x(Y, previousX, T, vpm1, vpm2, gm, pressure);
		}
		return X;
	}
	public static Double azeotropePoint(VaporPressureModel vpm1, VaporPressureModel vpm2, GammaModel gm, Double pressure) {
		//First: testing which component is the most volatile
		Double h = 0.00001;
		Double Xa = 0.5;
		for(int i = 0; i < 6; i++) {
			Double Xb = Xa-h;
			Double Xc = Xa+h;
			Double Fa = iterativeY(Xa, vpm1, vpm2, gm, pressure)-Xa;
			Double Fb = iterativeY(Xb, vpm1, vpm2, gm, pressure)-Xb;
			Double Fc = iterativeY(Xc, vpm1, vpm2, gm, pressure)-Xc;
			Xa = Xa-Fa/((Fc-Fb)/(2*h));
		}
		return Xa;
	}
	public static Integer mostVolatileComponent(Double X, VaporPressureModel vpm1, VaporPressureModel vpm2, GammaModel gm, Double pressure) {
		Double Y = iterativeY(X, vpm1, vpm2, gm, pressure);
		if(Y >= X) {
			return 1; 
		}else {
			return 2;
		}
	}
	public static Double f(Double X, Double T, VaporPressureModel vpm1, VaporPressureModel vpm2, GammaModel gm, Double pressure) {
		Double f1 = gm.gamma(1, X, T)*X*vpm1.pressure(T);
		Double f2 = gm.gamma(2, X, T)*(1-X)*vpm2.pressure(T);
		Double f = f1+f2-pressure;
		return f;
	}
	public static Double df(Double X, Double T, VaporPressureModel vpm1, VaporPressureModel vpm2, GammaModel gm, Double pressure) {
		Double h = 0.001;
		Double fmais = f(X, T+h, vpm1, vpm2, gm, pressure);
		Double fmenos = f(X, T-h, vpm1, vpm2, gm, pressure);
		Double df = (fmais-fmenos)/(2*h);
		return df;
	}
}
