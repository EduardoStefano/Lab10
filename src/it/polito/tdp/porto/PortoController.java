package it.polito.tdp.porto;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.db.PortoDAO;
import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {
	
	Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> boxPrimo;

    @FXML
    private ComboBox<String> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {
    	txtResult.clear();
    	String autore = boxPrimo.getValue();
    	int idAutore = -1;
    	
    	try {
    		String[] autoreSplittato = autore.split("-");
    		idAutore = Integer.parseInt(autoreSplittato[0].trim());
    	}
    	catch(Exception e) {
    		
    	}
    	
    	this.model.creaGrafo();
    	for(Author atemp:this.model.dammiCoautori(idAutore)) {
    		txtResult.appendText(atemp.getFirstname()+" "+atemp.getLastname()+"\n");
    	}
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	txtResult.clear();
    	String autore1 = boxPrimo.getValue();
    	String autore2 = boxSecondo.getValue();
    	int idAutore1 = -1;
    	int idAutore2 = -1;
    	
    	try {
    		String[] autoreSplittato1 = autore1.split("-");
    		idAutore1 = Integer.parseInt(autoreSplittato1[0].trim());
    		String[] autoreSplittato2 = autore2.split("-");
    		idAutore2 = Integer.parseInt(autoreSplittato2[0].trim());
    	}
    	catch(Exception e) {
    		
    	}
    	
    	this.model.creaGrafo();
    	if(model.camminoMinimo(idAutore1, idAutore2).size()==0) {
    		txtResult.appendText("Non ci sono collegamenti tra i due autori");
    	}
    	else {
        	for(Paper ptemp:model.camminoMinimo(idAutore1, idAutore2)) {
        		txtResult.appendText(ptemp.getTitle()+"\n");
        	}	
    	}
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		PortoDAO dao = new PortoDAO();
		List<String> autori = new ArrayList<String>();
		for(String atemp: dao.ritornaAutori()) {
			autori.add(atemp);
		}
		this.boxPrimo.getItems().addAll(autori);
		this.boxSecondo.getItems().addAll(autori);
	}
}
