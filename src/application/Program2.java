package application;

import java.util.ArrayList;
import java.util.List;

import brain.MccabeThiele;
import javafx.scene.chart.XYChart;
import thermodynamics.GammaModel;
import thermodynamics.Plate;
import thermodynamics.RaoultLaw;
import thermodynamics.VaporPressureModel;
import thermodynamics.impl.Antoine;
import thermodynamics.impl.MargulesGammaModel;

public class Program2 {

	public static void main(String[] args) {
		VaporPressureModel vpm1 = new Antoine(18.9119,3803.98,-41.68);
		VaporPressureModel vpm2 = new Antoine(18.3036,3816.44,-46.13);
		GammaModel gm = new MargulesGammaModel(1.6022,1.2);
		Double externalPressure = 760.0;
		System.out.println(RaoultLaw.iterativeY(0.99, vpm1, vpm2, gm, externalPressure));
		System.out.println(RaoultLaw.azeotropePoint(vpm1, vpm2, gm, externalPressure));
		System.out.println(RaoultLaw.mostVolatileComponent(0.95, vpm1, vpm2, gm, externalPressure));
	}

}
