package gui;

import java.util.Locale;

import brain.MccabeThiele;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import thermodynamicsModel.GammaModel;
import thermodynamicsModel.VaporPressureModel;
import thermodynamicsModel.impl.Antoine;
import thermodynamicsModel.impl.MargulesGammaModel;

public class ResultController {
	//Project Buttoms Controls
	@FXML
	private TextArea result;
	
	public void textAreaUpdate(String text) {
		result.setText(text);
	}
}
