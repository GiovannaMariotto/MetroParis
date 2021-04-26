package it.polito.tdp.metroparis.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	//Grafo: semplice, orientato, non pesato
	//Vertici: obj. Fermata
	
	Graph <Fermata,DefaultEdge> grafo;
	
	public void creaGrafo() {
		this.grafo = new SimpleGraph<Fermata,DefaultEdge>(DefaultEdge.class);
		MetroDAO dao = new MetroDAO();
		
		List<Fermata> fermate = dao.getAllFermate();
		
	//	for(Fermata f : fermate) { // .addAllVertices()
	//		this.grafo.addVertex(f);
	//	} //Classe aggiuntiva: GRAPHS --> Methodi statici
		
	Graphs.addAllVertices(this.grafo, fermate);	
	
	System.out.println(this.grafo);
	
	//Aggiungiamo gli archi
	//Stiamo aggiungendo troppe connessioni
	
//	for(Fermata f1 : this.grafo.vertexSet()) {
//		for(Fermata f2 : this.grafo.vertexSet()) {
			//Potrei sapere se il DAO me dice se possono essere collegate
//			if(!f1.equals(f2) && dao.femateCollegate(f1,f2)) {
//				this.grafo.addEdge(f1, f2);
//			}
//		}
//	}
	
	List<Connessione> connessioni = dao.getAllConnessioni(fermate);
	for(Connessione c : connessioni) {
		this.grafo.addEdge(c.getStazP(), c.getStazA());
	}
		
	
	}
	
}
