package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	private Graph<Author, DefaultWeightedEdge> graph;
	private Map<Integer, Author> idMap;
	
	public Model() {
		idMap = new HashMap<Integer, Author>();
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}
	
	public void creaGrafo() {
		
		PortoDAO dao = new PortoDAO();
		dao.listAutori(idMap);
		
		//aggiungo i vertici
		Graphs.addAllVertices(graph, idMap.values());
		
		//aggiungo gli archi
		for(Author atemp:this.graph.vertexSet()) {
			for(Author aatemp:dao.listaCoautori(atemp.getId())) {
				this.graph.addEdge(atemp, aatemp);
			}
		}
		
	}
	
	public List<Author> dammiCoautori(int id){
		List<Author> result = new ArrayList<Author>();
		Author autore = this.idMap.get(id);
		
		for(Author atemp:Graphs.neighborListOf(this.graph, autore)) {
			result.add(atemp);
		}
		
		return result;
	}
	
	public Graph<Author, DefaultWeightedEdge> grafo() {
		return this.graph;
	}
	
	public List<Paper> camminoMinimo(int id1, int id2){
		PortoDAO dao = new PortoDAO();
		List<Paper> paper = new LinkedList<Paper>();
		DijkstraShortestPath<Author, DefaultWeightedEdge> dijstra = new DijkstraShortestPath<>(this.graph);
		Author a1 = this.idMap.get(id1);
		Author a2 = this.idMap.get(id2);
		GraphPath<Author, DefaultWeightedEdge> path = dijstra.getPath(a1, a2);
		List<Author> pathAuthor = path.getVertexList();
		for(int i=0; i<pathAuthor.size()-1; i++) {
			Author aa1 = pathAuthor.get(i);
			Author aa2 = pathAuthor.get(i+1);
			paper.add(dao.trovaArticoliTraDueAutori(aa1.getId(), aa2.getId()));
		}
		return paper;
	}
	
	public Map<Integer, Author> ritornaAutori(){
		return this.idMap;
	}

}
