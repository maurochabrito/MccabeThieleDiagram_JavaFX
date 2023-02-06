package brain;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TextInputControl;
import thermodynamics.GammaModel;
import thermodynamics.VaporPressureModel;
import thermodynamics.impl.Antoine;
import thermodynamics.impl.IdealLiquidGammaModel;
import thermodynamics.impl.MargulesGammaModel;
import thermodynamics.impl.NRTLGammaModel;
import thermodynamics.impl.VanLaarGammaModel;

public class Factory {
	
	public Factory() {}
	//Methods
	public List<Double> createDoubleList(Double... value){
		List<Double> list = new ArrayList<>();
		for(Double v: value) {
			list.add(v);
		}
		return list;
	}
	//overload1
	public List<Double> createDoubleList(String... value){
		List<Double> list = new ArrayList<>();
		for(String v: value) {
			list.add(Double.parseDouble(v));
		}
		return list;
	}
	//overload2
	public List<Double> createDoubleList(TextInputControl... value){
		List<Double> list = new ArrayList<>();
		for(TextInputControl v: value) {
			list.add(Double.parseDouble( v.getText()));
		}
		return list;
	}
	public VaporPressureModel createVapourPressureModel(String model, List<Double> parameters) {
		VaporPressureModel vpm;
		switch(model){
		case "Antoine":
			vpm = new Antoine(parameters.get(0),parameters.get(1),parameters.get(2));
		break;
		default:
			vpm = new Antoine(parameters.get(0),parameters.get(1),parameters.get(2));
		break;
		}
		return vpm;
	}
	
	public GammaModel createGammaModel(String model, List<Double> parameters) {
		GammaModel gm;
		switch(model) {
		case "Ideal Liquid":
			gm = new IdealLiquidGammaModel();
		break;
		case "Margules Gamma Model":
			gm = new MargulesGammaModel(parameters.get(0), parameters.get(1));
		break;
		case "Van Laar Gamma Model":
			gm = new VanLaarGammaModel(parameters.get(0), parameters.get(1));
		break;
		case "NRTL Gamma Model":
			gm = new NRTLGammaModel(parameters.get(0), parameters.get(1), parameters.get(2), parameters.get(3));
		break;
		default:
			//gm = new IdealLiquidGammaModel();
			gm = new MargulesGammaModel(parameters.get(0), parameters.get(1));
		break;
		}
		return gm;
	}
	public MccabeThiele createMccabeThiele(List<Double> conditions, List<Double> vp1, List<Double> vp2, List<Double> gamma, String gammaModel) {
		VaporPressureModel vpm1 = this.createVapourPressureModel("Antoine", vp1);
		VaporPressureModel vpm2 = this.createVapourPressureModel("Antoine", vp2);
		GammaModel gm = this.createGammaModel(gammaModel, gamma);
		MccabeThiele mt;
		mt = new MccabeThiele(conditions.get(0), conditions.get(1), conditions.get(2), conditions.get(3), conditions.get(4), conditions.get(5), vpm1, vpm2, gm);
		return mt;
	}
}
