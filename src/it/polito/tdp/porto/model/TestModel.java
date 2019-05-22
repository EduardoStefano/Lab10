package it.polito.tdp.porto.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		model.creaGrafo();
		System.out.println("Creato grafo con "+model.grafo().vertexSet().size()+" vertici e "+model.grafo().edgeSet().size()+" archi");
		System.out.println("Vicini di 719");
		for(Author atemp:model.dammiCoautori(719)) {
			System.out.println(atemp.getId()+" "+atemp.getFirstname());
		}
		System.out.println(model.camminoMinimo(719, 2185));
	}

}
