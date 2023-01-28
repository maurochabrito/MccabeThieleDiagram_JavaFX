package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import application.Main;
import brain.MccabeThiele;
import brain.Plate;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import thermodynamicsModel.GammaModel;
import thermodynamicsModel.RaoultLaw;
import thermodynamicsModel.VaporPressureModel;
import thermodynamicsModel.impl.Antoine;
import thermodynamicsModel.impl.MargulesGammaModel;

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
	private Labeled txtGamma1;
	@FXML
	private Labeled txtGamma2;
	// Result Controls
	@FXML
	private TextArea txtResult;
	@FXML
	private NumberAxis x;
	@FXML
	private NumberAxis y;
	@FXML
	private LineChart<?, ?> lineChart;

	int colorState = 1;

	public void onBtRunAction() {
		this.auxiliar();
		this.lineChart.applyCss();
	}

	public void onBtCondition1Action() {
		this.condition = 1.0;
		labelQ.setText("q factor");
		txtQ.setText("1.5");
		;
	}

	public void onBtCondition2Action() {
		this.condition = 2.0;
		this.q = 1.0;
		labelQ.setText("q factor is 1.0 for inserted condition");
		txtQ.setText("1.0");
	}

	public void onBtCondition3Action() {
		this.condition = 3.0;
		labelQ.setText("Feed vaporized fraction (betha)");
		txtQ.setText("0.5");
		;
	}

	public void onBtCondition4Action() {
		this.condition = 4.0;
		this.q = 0.0;
		labelQ.setText("q factor is 0.0 for inserted condition");
		txtQ.setText("0.0");
	}

	public void onBtCondition5Action() {
		this.condition = 5.0;
		labelQ.setText("q factor");
		txtQ.setText("-0.5");
	}
	public synchronized void auxiliar() {

		Locale.setDefault(Locale.US);
		this.externalPressure = Double.parseDouble(txtPressure.getText());
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
		// VapourPressureModel vpm1 = new
		// Antoine(Double.parseDouble(txtAntoineA1.getText()),Double.parseDouble(txtAntoineB1.getText()),Double.parseDouble(txtAntoineC1.getText()));
		// VapourPressureModel vpm2 = new
		// Antoine(Double.parseDouble(txtAntoineA2.getText()),Double.parseDouble(txtAntoineB2.getText()),Double.parseDouble(txtAntoineC2.getText()));
		// GammaModel gm = new
		// MargulesGammaModel(Double.parseDouble(txtGamma1.getText()),Double.parseDouble(txtGamma2.getText()));
		VaporPressureModel vpm1 = new Antoine(18.9119, 3803.98, -41.68);
		VaporPressureModel vpm2 = new Antoine(18.3036, 3816.44, -46.13);
		GammaModel gm = new MargulesGammaModel(1.6022, 0.7947);
		MccabeThiele mt = new MccabeThiele(externalPressure, xd, xb, z, q, r, vpm1, vpm2, gm);
		this.txtResult.setText(mt.toString());
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
		while (count < 50 && count < plateList.size()) {// Make only up to 50 stages
			if (!(mt.plateList().get(count) == null)) {
				XYChart.Series horizontalPlateLine = new XYChart.Series<>();
				XYChart.Series verticalPlateLine = new XYChart.Series<>();
				Double Xf = plateList.get(count).getX();
				Double Yi = plateList.get(count).getY();
				Double Yf = null;
				if (mt.getRectifyingLine().y(Xf) < mt.getStrippingLine().y(Xf)) {
					Yf = mt.getRectifyingLine().y(Xf);
				} else {
					Yf = mt.getStrippingLine().y(Xf);
				}
				for (int i = 0; i < 10; i++) {
					if (mt.getRectifyingLine().y(Xi) < mt.getStrippingLine().y(Xi)) {
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
	}
}
