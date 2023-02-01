package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import brain.Factory;
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
	private MenuItem substance1Option3;
	@FXML
	private MenuItem substance1Option4;
	@FXML
	private MenuItem substance1Option5;
	@FXML
	private MenuItem substance1Option6;
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
	private MenuItem substance2Option3;
	@FXML
	private MenuItem substance2Option4;
	@FXML
	private MenuItem substance2Option5;
	@FXML
	private MenuItem substance2Option6;
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
		txtQ.setDisable(true);
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
		txtQ.setDisable(true);
	}
	public synchronized void auxiliar() {
		Locale.setDefault(Locale.US);
		Factory fc = new Factory();
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
		List<Double> gamma = new ArrayList<>();
		List<Double> antoine1 = new ArrayList<>();
		List<Double> antoine2 = new ArrayList<>();
		gamma = fc.createDoubleList(txtGamma1, txtGamma2, txtGamma3, txtGamma4);
		antoine1 = fc.createDoubleList(txtAntoineA1, txtAntoineB1, txtAntoineC1);
		antoine2 = fc.createDoubleList(txtAntoineA2, txtAntoineB2, txtAntoineC2);
		VaporPressureModel vpm1 = fc.createVapourPressureModel("Antoine", antoine1);
		VaporPressureModel vpm2 = fc.createVapourPressureModel("Antoine", antoine2);
		if(this.substance1OtherWasSelected == true) {
			this.substance1 = substance1Name.getText();
		}
		if(this.substance2OtherWasSelected == true) {
			this.substance2 = substance2Name.getText();
		}
		GammaModel gm;
		gm = fc.createGammaModel(gammaModelOption, gamma);
		//Defensive: exchange components
		if(RaoultLaw.mostVolatileComponent(z, vpm1, vpm2, gm, externalPressure) == 2) {
			gamma = fc.createDoubleList(txtGamma2, txtGamma1, txtGamma4, txtGamma3);
			txtGamma1.setText(gamma.get(0).toString());
			txtGamma2.setText(gamma.get(1).toString());
			txtGamma3.setText(gamma.get(2).toString());
			txtGamma4.setText(gamma.get(3).toString());
			txtGamma2.setText(txtGamma1.getText());
			txtGamma4.setText(txtGamma3.getText());
			gm = fc.createGammaModel(gammaModelOption, gamma);
			
			antoine1 = fc.createDoubleList(txtAntoineA2, txtAntoineB2, txtAntoineC2);
			txtAntoineA2.setText(txtAntoineA1.getText());
			txtAntoineB2.setText(txtAntoineB1.getText());
			txtAntoineC2.setText(txtAntoineC1.getText());
			antoine2 = fc.createDoubleList(txtAntoineA2, txtAntoineB2, txtAntoineC2);			
			txtAntoineA1.setText(antoine1.get(0).toString());
			txtAntoineB1.setText(antoine1.get(1).toString());
			txtAntoineC1.setText(antoine1.get(2).toString());
			vpm1 = fc.createVapourPressureModel("Antoine", antoine1);//delete
			vpm2 = fc.createVapourPressureModel("Antoine", antoine2);//delete
			
			String aux = substance1.toString();
			substance1 = substance2.toString();
			substance2 = aux.toString();
			menuSubstance1.setText(substance1);
			menuSubstance2.setText(substance2);
			z = 1-z;
			txtZ.setText(z.toString());
			xd = 1-xd;
			txtXd.setText(xd.toString());
			xb = 1-xb;
			txtXb.setText(xb.toString());
		}
		List<Double> operationalConditions = new ArrayList<>();
		operationalConditions = fc.createDoubleList(externalPressure, xd, xb, z, q, r);
		MccabeThiele mt = fc.createMccabeThiele(operationalConditions, antoine1, antoine2, gamma, gammaModelOption);
		//DEFENSIVE: Xd, Xb, Z AGAINST Xazeotrope
		//If substance 1 is most volatile, then Xaz > Xd > z > Xb > 0
		//Otherwise, Xb > z > Xd > Xaz > 0
			//xb
			if(xb <= 0.) {
				xb = 0.01;
				txtXb.setText(xb.toString());
				operationalConditions = fc.createDoubleList(externalPressure, xd, xb, z, q, r);
				mt = fc.createMccabeThiele(operationalConditions, antoine1, antoine2, gamma, gammaModelOption);
			}
			if(xb >= z-0.2) {
				xb = z-0.2;
				txtXb.setText(xb.toString());
				operationalConditions = fc.createDoubleList(externalPressure, xd, xb, z, q, r);
				mt = fc.createMccabeThiele(operationalConditions, antoine1, antoine2, gamma, gammaModelOption);
			}
			//Xd
			if(xd >= 1.0) {
				xd = 0.99;
				txtXd.setText(xd.toString());
				operationalConditions = fc.createDoubleList(externalPressure, xd, xb, z, q, r);
				mt = fc.createMccabeThiele(operationalConditions, antoine1, antoine2, gamma, gammaModelOption);
			}
			if(z+0.2 >= xd) {
				xd = z+0.2;
				txtXd.setText(xd.toString());
				operationalConditions = fc.createDoubleList(externalPressure, xd, xb, z, q, r);
				mt = fc.createMccabeThiele(operationalConditions, antoine1, antoine2, gamma, gammaModelOption);
			}
			if(mt.azeotropicPoint()-0.1 <= xd && !(gm instanceof IdealLiquidGammaModel)) {
				xd = xd-0.1;
				txtXd.setText(xd.toString());
				operationalConditions = fc.createDoubleList(externalPressure, xd, xb, z, q, r);
				mt = fc.createMccabeThiele(operationalConditions, antoine1, antoine2, gamma, gammaModelOption);
			}
		//DEFENSIVE: REFLUX RATIO
		Boolean test = mt.testR();
		while(mt.testR() && r < 50.0) {
			r = r+0.1;
		    mt.setR(r);
			txtR.setText(r.toString());
			test = mt.testR();
		}
		this.txtResult.setText(mt.toString(menuSubstance1.getText()));
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
	public void onMenuItemSubstance1Option3() {
		this.substance1 = "chloroform";
		this.menuSubstance1.setText("chloroform");
		this.substance1OtherWasSelected = false;
		this.substance1Name.setOpacity(0.0);
		this.substance1Name.setDisable(true);
		this.txtAntoineA1.setText("15.9732");
		this.txtAntoineB1.setText("2696.8");
		this.txtAntoineC1.setText("-46.16");
	}
	public void onMenuItemSubstance1Option4() {
		this.substance1 = "acetone";
		this.menuSubstance1.setText("acetone");
		this.substance1OtherWasSelected = false;
		this.substance1Name.setOpacity(0.0);
		this.substance1Name.setDisable(true);
		this.txtAntoineA1.setText("16.6513");
		this.txtAntoineB1.setText("2940.46");
		this.txtAntoineC1.setText("-35.93");
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
	public void onMenuItemSubstance2Option3() {
		this.substance2 = "chloroform";
		this.menuSubstance2.setText("chloroform");
		this.substance2OtherWasSelected = false;
		this.substance2Name.setOpacity(0.0);
		this.substance2Name.setDisable(true);
		this.txtAntoineA2.setText("15.9732");
		this.txtAntoineB2.setText("2696.8");
		this.txtAntoineC2.setText("-46.16");
	}
	public void onMenuItemSubstance2Option4() {
		this.substance2 = "acetone";
		this.menuSubstance2.setText("acetone");
		this.substance2OtherWasSelected = false;
		this.substance2Name.setOpacity(0.0);
		this.substance2Name.setDisable(true);
		this.txtAntoineA2.setText("16.6513");
		this.txtAntoineB2.setText("2940.46");
		this.txtAntoineC2.setText("-35.93");
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
		this.gammaAreaDisable(true, true, true, true);
	}
	public void onMenuItemGammaOption2() {
		this.gammaModelOption = "Margules Gamma Model";
		this.gammaModelMenu.setText(gammaModelOption);
		this.gammaAreaDisable(false, false, true, true);
		this.gammaAreaTextLabel("         A12", "         A21");
	}
	public void onMenuItemGammaOption3() {
		this.gammaModelOption = "Van Laar Gamma Model";
		this.gammaModelMenu.setText(gammaModelOption);
		this.gammaAreaDisable(false, false, true, true);
		this.gammaAreaTextLabel("         A12", "         A21");
	}
	public void onMenuItemGammaOption4() {
		this.gammaModelOption = "NRTL Gamma Model";
		this.gammaModelMenu.setText(gammaModelOption);
		this.gammaAreaDisable(false,false,false,false);
		this.gammaAreaTextLabel("         α12", "         α21", "       Δg12", "       Δg21");
	}
	protected void gammaAreaDisable(boolean... disable) {
		List<Double> opacity = new ArrayList<>();
		for(boolean d: disable) {
			if(d == true) {
				opacity.add(0.0);
			} else {
				opacity.add(1.0);
			}
		}
		this.txtGamma1.setDisable(disable[0]);
		this.txtGamma1.setOpacity(opacity.get(0));
		this.txtGamma2.setDisable(disable[1]);
		this.txtGamma2.setOpacity(opacity.get(1));
		this.txtGamma3.setDisable(disable[2]);
		this.txtGamma3.setOpacity(opacity.get(2));
		this.txtGamma4.setDisable(disable[3]);
		this.txtGamma4.setOpacity(opacity.get(3));
		
		this.GammaLabel1.setDisable(disable[0]);
		this.GammaLabel1.setOpacity(opacity.get(0));
		this.GammaLabel2.setDisable(disable[1]);
		this.GammaLabel2.setOpacity(opacity.get(1));
		this.GammaLabel3.setDisable(disable[2]);
		this.GammaLabel3.setOpacity(opacity.get(2));
		this.GammaLabel4.setDisable(disable[3]);
		this.GammaLabel4.setOpacity(opacity.get(3));
	}
	protected void gammaAreaTextLabel(String... names) {
		if(!this.GammaLabel1.isDisable()) {
			this.GammaLabel1.setText(names[0]);

		}
		if(!this.GammaLabel2.isDisable()) {
			this.GammaLabel2.setText(names[1]);

		}
		if(!this.GammaLabel3.isDisable()) {
			this.GammaLabel3.setText(names[2]);

		}
		if(!this.GammaLabel4.isDisable()) {
			this.GammaLabel4.setText(names[3]);

		}
	}
}
