package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import brain.MccabeThiele;
import brain.Plate;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import thermodynamicsModel.GammaModel;
import thermodynamicsModel.RaoultLaw;
import thermodynamicsModel.VaporPressureModel;
import thermodynamicsModel.impl.Antoine;
import thermodynamicsModel.impl.IdealLiquidGammaModel;
import thermodynamicsModel.impl.MargulesGammaModel;
import thermodynamicsModel.impl.NRTLGammaModel;
import thermodynamicsModel.impl.VanLaarGammaModel;

public class ViewController {
	// Project Buttoms Controls
	@FXML
	private Button btRun;
	@FXML
	private Button btCondition1;
	@FXML
	private Button btCondition2;
	@FXML
	private Button btCondition3;
	@FXML
	private Button btCondition4;
	@FXML
	private Button btCondition5;
	// Project Textfield Controls
	@FXML
	private MenuButton feedConditionMenu;
	@FXML
	private TextField txtZ;
	@FXML
	private TextField txtPressure;
	@FXML
	private TextField txtXb;
	@FXML
	private TextField txtXd;
	@FXML
	private TextField txtQ;
	@FXML
	private TextField txtR;
	// Project Labels
	@FXML
	private Label labelQ;
	// Thermodinamic TextField Controls
	@FXML
	private TextField txtAntoineC1;
	@FXML
	private TextField txtAntoineC2;
	@FXML
	private TextField txtAntoineB1;
	@FXML
	private TextField txtAntoineA1;
	@FXML
	private TextField txtAntoineB2;
	@FXML
	private TextField txtAntoineA2;
	// Correspondent project attributes
	Double externalPressure = null;
	Double xb = null;
	Double xd = null;
	Double z = null;
	Double q = null;
	Double r = null;
	Double condition = 1.0;
	@FXML
	private MenuButton menuSubstance1;
	@FXML
	private MenuItem substance1Option1;
	@FXML
	private MenuItem substance1Option2;
	@FXML
	private MenuItem substance1OptionOtherwise;
	boolean substance1OtherWasSelected = false;
	@FXML
	private TextField substance1Name;
	@FXML
	private MenuButton menuSubstance2;
	@FXML
	private MenuItem substance2Option1;
	@FXML
	private MenuItem substance2Option2;
	@FXML
	private MenuItem substance2OptionOtherwise;
	boolean substance2OtherWasSelected = false;
	@FXML
	private TextField substance2Name;
	// Result Controls
	@FXML
	private TextArea txtResult;
	@FXML
	private NumberAxis x;
	@FXML
	private NumberAxis y;
	@FXML
	private LineChart<?, ?> lineChart;
	@FXML
	private MenuButton gammaModelMenu;
	@FXML
	private MenuItem gammaModelMenuItem1;	
	@FXML
	private MenuItem gammaModelMenuItem2;
	@FXML
	private MenuItem gammaModelMenuItem3;
	@FXML
	private TextField txtGamma1;
	@FXML
	private TextField txtGamma2;
	@FXML
	private TextField txtGamma3;
	@FXML
	private TextField txtGamma4;
	@FXML
	private Label GammaLabel1;
	@FXML
	private Label GammaLabel2;
	@FXML
	private Label GammaLabel3;
	@FXML
	private Label GammaLabel4;
	String gammaModelOption = "Ideal liquid";
	String substance1 = "Ethanol";
	String substance2 = "Water";

	public void onBtRunAction() {
		this.auxiliar();
		this.lineChart.applyCss();
	}

	public void onBtCondition1Action() {
		this.condition = 1.0;
		feedConditionMenu.setText("Subcooled liquid");
		labelQ.setText("      q factor");
		txtQ.setDisable(false);
		txtQ.setText("1.5");
		;
	}

	public void onBtCondition2Action() {
		this.condition = 2.0;
		this.q = 1.0;
		feedConditionMenu.setText("Saturated liquid");
		labelQ.setText("      q factor");
		txtQ.setDisable(true);
		txtQ.setText("1.0");
	}

	public void onBtCondition3Action() {
		this.condition = 3.0;
		feedConditionMenu.setText("Partially vaporized");
		labelQ.setText("        Betha");
		txtQ.setText("0.5");
		txtQ.setDisable(false);
		;
	}

	public void onBtCondition4Action() {
		this.condition = 4.0;
		this.q = 0.0;
		feedConditionMenu.setText("Saturated vapour");
		labelQ.setText("      q factor");
		txtQ.setText("0.0");
		txtQ.setDisable(true);
	}

	public void onBtCondition5Action() {
		this.condition = 5.0;
		feedConditionMenu.setText("Superheated vapour");
		labelQ.setText("      q factor");
		txtQ.setText("-0.5");
		txtQ.setDisable(false);
	}
	public synchronized void auxiliar() {

		Locale.setDefault(Locale.US);
		this.externalPressure = 760.0*Double.parseDouble(txtPressure.getText());
		this.xb = Double.parseDouble(txtXb.getText());
		this.xd = Double.parseDouble(txtXd.getText());
		this.z = Double.parseDouble(txtZ.getText());
		if (condition == 3) {
			this.q = 1 - Double.parseDouble(txtQ.getText());

		} else {
			this.q = Double.parseDouble(txtQ.getText());
		}
		this.r = Double.parseDouble(txtR.getText());
		//
		Double a1 = Double.parseDouble(txtAntoineA1.getText());
		Double b1 = Double.parseDouble(txtAntoineB1.getText());
		Double c1 = Double.parseDouble(txtAntoineC1.getText());
		Double a2 = Double.parseDouble(txtAntoineA2.getText());
		Double b2 = Double.parseDouble(txtAntoineB2.getText());
		Double c2 = Double.parseDouble(txtAntoineC2.getText());
		Double g1 = Double.parseDouble(txtGamma1.getText());
		Double g2 = Double.parseDouble(txtGamma2.getText());
		Double g3 = Double.parseDouble(txtGamma3.getText());
		Double g4 = Double.parseDouble(txtGamma4.getText());	
		VaporPressureModel vpm1 = new Antoine(a1,b1,c1);
		VaporPressureModel vpm2 = new Antoine(a2,b2,c2);
		if(this.substance1OtherWasSelected == true) {
			this.substance1 = substance1Name.getText();
		}
		if(this.substance2OtherWasSelected == true) {
			this.substance2 = substance2Name.getText();
		}
		GammaModel gm;
		switch(gammaModelOption) {
		case "Ideal Liquid":
			gm = new IdealLiquidGammaModel();
		break;
		case "Margules Gamma Model":
			gm = new MargulesGammaModel(g1, g2);
		break;
		case "Van Laar Gamma Model":
			gm = new VanLaarGammaModel(g1, g2);
		break;
		case "NRTL Gamma Model":
			gm = new NRTLGammaModel(g1, g2, g3, g4);
		break;
		default:
			//gm = new IdealLiquidGammaModel();
			gm = new MargulesGammaModel(g1, g2);
		break;
		}
		//exchange components
		if(RaoultLaw.mostVolatileComponent(z, vpm1, vpm2, gm, externalPressure) == 2) {
			a1 = a2.doubleValue();
			b1 = b2.doubleValue();
			c1 = c2.doubleValue();
			g1 = g2.doubleValue();
			g3 = g4.doubleValue();
			txtAntoineA2.setText(txtAntoineA1.getText());
			txtAntoineB2.setText(txtAntoineB1.getText());
			txtAntoineC2.setText(txtAntoineC1.getText());
			txtGamma2.setText(txtGamma1.getText());
			txtGamma4.setText(txtGamma3.getText());
			a2 = Double.parseDouble(txtAntoineA2.getText());
			b2 = Double.parseDouble(txtAntoineB2.getText());
			c2 = Double.parseDouble(txtAntoineC2.getText());
			g2 = Double.parseDouble(txtGamma2.getText());
			g4 = Double.parseDouble(txtGamma4.getText());
			txtAntoineA1.setText(a1.toString());
			txtAntoineB1.setText(b1.toString());
			txtAntoineC1.setText(c1.toString());
			txtGamma1.setText(g1.toString());
			txtGamma3.setText(g3.toString());
			vpm1 = new Antoine(a1,b1,c1);//delete
			vpm2 = new Antoine(a2,b2,c2);//delete
			switch(gammaModelOption) {//reinstantiating
			case "Ideal Liquid":
				gm = new IdealLiquidGammaModel();
			break;
			case "Margules Gamma Model":
				gm = new MargulesGammaModel(g1, g2);
			break;
			case "Van Laar Gamma Model":
				gm = new VanLaarGammaModel(g1, g2);
			break;
			case "NRTL Gamma Model":
				gm = new NRTLGammaModel(g1, g2, g3, g4);
			break;
			default:
				//gm = new IdealLiquidGammaModel();
				gm = new MargulesGammaModel(g1, g2);
			break;
			}
			//
			String aux = substance1.toString();
			substance1 = substance2.toString();
			substance2 = aux.toString();
			menuSubstance1.setText(substance1);
			menuSubstance2.setText(substance2);
			//
			z = 1-z;
			txtZ.setText(z.toString());
			xd = 1-xd;
			txtXd.setText(xd.toString());
			xb = 1-xb;
			txtXb.setText(xb.toString());
		}
		MccabeThiele mt = new MccabeThiele(externalPressure, xd, xb, z, q, r, vpm1, vpm2, gm);
		//DEFENSIVE: Xd, Xb, Z AGAINST Xazeotrope
		//Change X to represents X2, if substance 2 is more volatile over the domain.
		String substance = substance1;
		this.txtResult.setText("foi");
		boolean componentsExchanged;
		if(mt.mostVolatileOnFeed() == 2) {			
			switch(gammaModelOption) {
			case "Ideal Liquid":
				gm = new IdealLiquidGammaModel();
			break;
			case "Margules Gamma Model":
				gm = new MargulesGammaModel(g2, g1);
			break;
			case "Van Laar Gamma Model":
				gm = new VanLaarGammaModel(g2, g1);
			break;
			case "NRTL Gamma Model":
				gm = new NRTLGammaModel(g2, g1, g4, g3);
			break;
			default:
				//gm = new IdealLiquidGammaModel();
				gm = new MargulesGammaModel(g2, g1);
			break;
			}
			vpm1 = new Antoine(a2,b2,c2);
			vpm2 = new Antoine(a1,b1,c1);
			mt = new MccabeThiele(externalPressure, 1-xd, 1-xb, 1-z, q, r, vpm1, vpm2, gm);
			substance = substance2;
			componentsExchanged = true;
		}else {
			componentsExchanged = false;
			mt = new MccabeThiele(externalPressure, xd, xb, z, q, r, vpm1, vpm2, gm);
		}
		//If substance 1 is most volatile, then Xaz > Xd > z > Xb > 0
		//Otherwise, Xb > z > Xd > Xaz > 0
			//xb
			if(xb >= z-0.2) {
				xb = z-0.2;
				txtXb.setText(xb.toString());
				mt = new MccabeThiele(externalPressure, xd, xb, z, q, r, vpm1, vpm2, gm);
			}
			//Xd
			if(z+0.2 >= xd) {
				xd = z+0.2;
				txtXd.setText(xd.toString());
				mt = new MccabeThiele(externalPressure, xd, xb, z, q, r, vpm1, vpm2, gm);
			}
			if(mt.azeotropicPoint()-0.1 <= xd) {
				xd = mt.azeotropicPoint()-0.1;
				txtXd.setText(xd.toString());
				mt = new MccabeThiele(externalPressure, xd, xb, z, q, r, vpm1, vpm2, gm);
			}
		//DEFENSIVE: REFLUX RATIO
		Boolean test = mt.testR();
		while(mt.testR() && r < 50.0) {
			r = r+0.1;
		    mt.setR(r);
			txtR.setText(r.toString());
			test = mt.testR();
		}
		this.txtResult.setText(mt.toString(substance));
		XYChart.Series equilibriumLine = new XYChart.Series<>();
		XYChart.Series diagonalLine = new XYChart.Series<>();
		XYChart.Series rectifyingLine = new XYChart.Series<>();
		XYChart.Series strippingLine = new XYChart.Series<>();
		XYChart.Series qLine = new XYChart.Series<>();
		int points = 1001;
		for (int i = 0; i < points; i++) {
			Double X = ((double) (i)) / (points - 1);
			Double equilibriumY = RaoultLaw.iterativeY(X, vpm1, vpm2, gm, externalPressure);
			Double rectifyingY = mt.getRectifyingLine().y(X);
			Double strippingY = mt.getStrippingLine().y(X);
			Double qY = mt.getqLine().y(X);
			if (X == 1.0) {// ensuring that domain and image of equilibrium line are from 0 to 1
				equilibriumY = 1.0;
			} else if (X == 0.0) {
				equilibriumY = 0.0;
			}
			// Equilibrium and diagonal lines adding
			equilibriumLine.getData().add(new XYChart.Data(X, equilibriumY));
			diagonalLine.getData().add(new XYChart.Data(X, X));
			// Rectifying line adding if X is minor or equal to xd (X of distillate)
			if (X <= xd && rectifyingY < strippingY) {
				rectifyingLine.getData().add(new XYChart.Data(X, rectifyingY));
			}
			// Stripping line adding if strippingY is minor than rectifyingY and greater
			// than diagonal line (X)
			// In other words, adding if stripping line is between diagonal and equilibrium
			// lines
			if (strippingY <= rectifyingY && strippingY >= X) {
				strippingLine.getData().add(new XYChart.Data(X, strippingY));
			}
			// qLine adding if qLines is between operational and diagonal Lines
			if (qY <= rectifyingY && qY <= strippingY && qY >= X) {
				qLine.getData().add(new XYChart.Data(X, qY));
			}
			if (Math.abs(q - 1) < 0.02 && X.equals(z)) {
				qLine.getData().clear();
				qY = mt.getRectifyingLine().y(X);
				System.out.println(qY);
				qLine.getData().add(new XYChart.Data(X, qY));
				if (X > 0.000001) {
					qLine.getData().add(new XYChart.Data(X - 0.000001, X - 0.000001));
				} else {
					qLine.getData().add(new XYChart.Data(X + 0.000001, X + 0.000001));
				}
			}
		}
		// MccabeLines
		List<Plate> plateList = mt.plateList();
		List<Series<Double, Double>> horizontalLines = new ArrayList();
		List<Series<Double, Double>> verticalLines = new ArrayList();
		Double Xi = xd;
		int count = 0;
		while (count < mt.maximumPlates && count < plateList.size()) {// Make only up to 70 stages
			if (!(mt.plateList().get(count) == null)) {
				XYChart.Series horizontalPlateLine = new XYChart.Series<>();
				XYChart.Series verticalPlateLine = new XYChart.Series<>();
				Double Xf = plateList.get(count).getX();
				Double Yi = plateList.get(count).getY();
				Double Yf = null;
				if (!mt.getRectifyingLine().compareTo(mt.getStrippingLine(), Xf)) {
					Yf = mt.getRectifyingLine().y(Xf);
				} else {
					Yf = mt.getStrippingLine().y(Xf);
				}
				for (int i = 0; i < 10; i++) {
					if (!mt.getRectifyingLine().compareTo(mt.getStrippingLine(), Xi)) {
						Xi = mt.getRectifyingLine().x(Yi);
					} else {
						Xi = mt.getStrippingLine().x(Yi);
						;
					}
				}
				if (count + 1 == plateList.size()) {
					Yf = plateList.get(count).getX();
				}
				horizontalPlateLine.getData().add(new XYChart.Data(Xi, Yi));
				horizontalPlateLine.getData().add(new XYChart.Data(Xf, Yi));
				verticalPlateLine.getData().add(new XYChart.Data(Xf, Yi));
				verticalPlateLine.getData().add(new XYChart.Data(Xf + 0.000000001, Yf));
				horizontalLines.add(horizontalPlateLine);
				verticalLines.add(verticalPlateLine);
			}
			count++;
		}
		// Basic Chart without Mccabe Stages
		this.lineChart.setOpacity(1.0);
		this.lineChart.setLegendVisible(false);
		x.setTickUnit(0.1);
		y.setTickUnit(0.1);
		this.lineChart.getData().clear();
		this.lineChart.getData().addAll(equilibriumLine);
		this.lineChart.getData().addAll(diagonalLine);
		this.lineChart.getData().addAll(rectifyingLine);
		this.lineChart.getData().addAll(strippingLine);
		this.lineChart.getData().addAll(qLine);
		for (int i = 0; i < count + 1; i++) {
			this.lineChart.getData().addAll((XYChart.Series) horizontalLines.get(i));
			this.lineChart.getData().addAll((XYChart.Series) verticalLines.get(i));
		}
		this.lineChart.getData().clear();
	}
	public void onMenuItemSubstance1Option1() {
		this.substance1 = "ethanol";
		this.menuSubstance1.setText("ethanol");
		this.substance1OtherWasSelected = false;
		this.substance1Name.setOpacity(0.0);
		this.substance1Name.setDisable(true);
		this.txtAntoineA1.setText("18.9119");
		this.txtAntoineB1.setText("3803.98");
		this.txtAntoineC1.setText("-41.68");
	}
	public void onMenuItemSubstance1Option2() {
		this.substance1 = "water";
		this.menuSubstance1.setText("water");
		this.substance1OtherWasSelected = false;
		this.substance1Name.setOpacity(0.0);
		this.substance1Name.setDisable(true);
		this.txtAntoineA1.setText("18.3036");
		this.txtAntoineB1.setText("3816.44");
		this.txtAntoineC1.setText("-46.13");
	}
	public void onMenuItemSubstance1OptionOtherwise() {
		this.substance1OtherWasSelected = true;
		this.menuSubstance1.setText("Other:");
		this.substance1Name.setOpacity(1.0);
		this.substance1Name.setDisable(false);
		this.txtAntoineA1.setText("");
		this.txtAntoineB1.setText("");
		this.txtAntoineC1.setText("");
	}
	public void onMenuItemSubstance2Option1() {
		this.substance2 = "ethanol";
		this.menuSubstance2.setText("ethanol");
		this.substance2OtherWasSelected = false;
		this.substance2Name.setOpacity(0.0);
		this.substance2Name.setDisable(true);
		this.txtAntoineA2.setText("18.9119");
		this.txtAntoineB2.setText("3803.98");
		this.txtAntoineC2.setText("-41.68");
	}
	public void onMenuItemSubstance2Option2() {
		this.substance2 = "water";
		this.menuSubstance2.setText("water");
		this.substance2OtherWasSelected = false;
		this.substance2Name.setOpacity(0.0);
		this.substance2Name.setDisable(true);
		this.txtAntoineA2.setText("18.3036");
		this.txtAntoineB2.setText("3816.44");
		this.txtAntoineC2.setText("-46.13");
	}
	public void onMenuItemSubstance2OptionOtherwise() {
		this.substance2OtherWasSelected = true;
		this.menuSubstance2.setText("Other:");
		this.substance2Name.setOpacity(1.0);
		this.substance2Name.setDisable(false);
		this.txtAntoineA2.setText("");
		this.txtAntoineB2.setText("");
		this.txtAntoineC2.setText("");
	}
	public void onMenuItemGammaOption1() {
		this.gammaModelOption = "Ideal Liquid";
		this.gammaModelMenu.setText(gammaModelOption);
		this.GammaLabel1.setDisable(true);
		this.GammaLabel2.setDisable(true);
		this.GammaLabel3.setDisable(true);
		this.GammaLabel4.setDisable(true);
		this.GammaLabel1.setOpacity(0.0);
		this.GammaLabel2.setOpacity(0.0);
		this.GammaLabel3.setOpacity(0.0);
		this.GammaLabel4.setOpacity(0.0);
		this.txtGamma1.setDisable(true);
		this.txtGamma2.setDisable(true);
		this.txtGamma3.setDisable(true);
		this.txtGamma4.setDisable(true);
		this.txtGamma1.setOpacity(0.0);
		this.txtGamma2.setOpacity(0.0);
		this.txtGamma3.setOpacity(0.0);
		this.txtGamma4.setOpacity(0.0);
	}
	public void onMenuItemGammaOption2() {
		this.gammaModelOption = "Margules Gamma Model";
		this.gammaModelMenu.setText(gammaModelOption);
		this.GammaLabel1.setDisable(false);
		this.GammaLabel2.setDisable(false);
		this.GammaLabel3.setDisable(true);
		this.GammaLabel4.setDisable(true);
		this.GammaLabel1.setOpacity(1.0);
		this.GammaLabel2.setOpacity(1.0);
		this.GammaLabel3.setOpacity(0.0);
		this.GammaLabel4.setOpacity(0.0);
		this.txtGamma1.setDisable(false);
		this.txtGamma2.setDisable(false);
		this.txtGamma3.setDisable(true);
		this.txtGamma4.setDisable(true);
		this.txtGamma1.setOpacity(1.0);
		this.txtGamma2.setOpacity(1.0);
		this.txtGamma3.setOpacity(0.0);
		this.txtGamma4.setOpacity(0.0);
		this.GammaLabel1.setText("         A12");
		this.GammaLabel2.setText("         A21");
	}
	public void onMenuItemGammaOption3() {
		this.gammaModelOption = "Van Laar Gamma Model";
		this.gammaModelMenu.setText(gammaModelOption);
		this.GammaLabel1.setDisable(false);
		this.GammaLabel2.setDisable(false);
		this.GammaLabel3.setDisable(true);
		this.GammaLabel4.setDisable(true);
		this.GammaLabel1.setOpacity(1.0);
		this.GammaLabel2.setOpacity(1.0);
		this.GammaLabel3.setOpacity(0.0);
		this.GammaLabel4.setOpacity(0.0);
		this.txtGamma1.setDisable(false);
		this.txtGamma2.setDisable(false);
		this.txtGamma3.setDisable(true);
		this.txtGamma4.setDisable(true);
		this.txtGamma1.setOpacity(1.0);
		this.txtGamma2.setOpacity(1.0);
		this.txtGamma3.setOpacity(0.0);
		this.txtGamma4.setOpacity(0.0);
		this.GammaLabel1.setText("         A12");
		this.GammaLabel2.setText("         A21");
	}
	public void onMenuItemGammaOption4() {
		this.gammaModelOption = "NRTL Gamma Model";
		this.gammaModelMenu.setText(gammaModelOption);
		this.GammaLabel1.setDisable(true);
		this.GammaLabel2.setDisable(true);
		this.GammaLabel3.setDisable(true);
		this.GammaLabel4.setDisable(true);
		this.GammaLabel1.setOpacity(1.0);
		this.GammaLabel2.setOpacity(1.0);
		this.GammaLabel3.setOpacity(1.0);
		this.GammaLabel4.setOpacity(1.0);
		this.txtGamma1.setDisable(false);
		this.txtGamma2.setDisable(false);
		this.txtGamma3.setDisable(false);
		this.txtGamma4.setDisable(false);
		this.txtGamma1.setOpacity(1.0);
		this.txtGamma2.setOpacity(1.0);
		this.txtGamma3.setOpacity(1.0);
		this.txtGamma4.setOpacity(1.0);
		this.GammaLabel1.setText("         α12");
		this.GammaLabel2.setText("         α21");
		this.GammaLabel3.setText("        Δg12");
		this.GammaLabel4.setText("        Δg21");
	}
}
