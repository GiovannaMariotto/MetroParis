package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	//Grafo: semplice, orientato, non pesato
	//Vertici: obj. Fermata
	
	Graph <Fermata,DefaultEdge> grafo;
	
	Map<Fermata,Fermata> predecessore ;
	
	public void creaGrafo() {
		this.grafo = new SimpleGraph<Fermata,DefaultEdge>(DefaultEdge.class);
		MetroDAO dao = new MetroDAO();
		
		List<Fermata> fermate = dao.getAllFermate();
		
	//	for(Fermata f : fermate) { // .addAllVertices()
	//		this.grafo.addVertex(f);
	//	} //Classe aggiuntiva: GRAPHS --> Methodi statici
		
	Graphs.addAllVertices(this.grafo, fermate);	
	
	//System.out.println(this.grafo);
	
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
	if(!connessioni.isEmpty()) {
		for(Connessione c : connessioni) {
			if(c!=null)
				this.grafo.addEdge(c.getStazP(), c.getStazA());
		}
	}
		System.out.format("Grafo creato con %d vertici e %d archi\n",
			this.grafo.vertexSet().size(), this.grafo.edgeSet().size()) ;
		
		//System.out.println(this.grafo);
	/*Fermata f = null;
	Set<DefaultEdge>archi=this.grafo.edgesOf(f); //archi adiacenti
	for(DefaultEdge e : archi) {
		
		Fermata f1 = this.grafo.getEdgeSource(e);//Sorgente
		//oppure
		Fermata f2 = this.grafo.getEdgeTarget(e);//Alvo
		if(f1.equals(f)) {
			//f2 è quello che mi serve
		}else {
			//f1 è quello che mi serve
		}
		Fermata f1=Graphs.getOppositeVertex(this.grafo,e, f);
	}*/
	
	//List<Fermata> fermateAdj = Graphs.successorListOf(this.grafo,f);
	//Dato una fermata, prende le fermate adiacenti
	//Graphs.predecessorListOf(this.grafo, f);
	}
	
	public	List<Fermata> fermateRaggiungibili(Fermata partenza) {
		BreadthFirstIterator<Fermata,DefaultEdge> bfv = new BreadthFirstIterator(this.grafo,partenza);
		List<Fermata> result = new ArrayList();
		
		predecessore = new HashMap<>();
		predecessore.put(partenza, null);
		
		bfv.addTraversalListener(new TraversalListener<Fermata,DefaultEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
			
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
				DefaultEdge arco = e.getEdge();
				Fermata a = grafo.getEdgeSource(arco);
				Fermata b =grafo.getEdgeTarget(arco);
				//ho scoperto 'a' arrivando da 'b' ( se 'b' lo conoscevo già)
				if(predecessore.containsKey(b) && !predecessore.containsKey(a)) {
					predecessore.put(a, b);
					//System.out.println(a+" è scoperto da "+b);
			}else if(predecessore.containsKey(a) && !predecessore.containsKey(b)) {
				//di sicuro conosceva 'a' e ho scoperto 'b'
				predecessore.put(b, a);
				//System.out.println(b+" è scoperto da "+a);
			}
				}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Fermata> e) {
		//			Fermata nuova = e.getVertex();
		//			Fermata precedente; //vertice adiacente a 'nuova' che è gia aggiunta
		//			predecessore.put(nuova, precedente);
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Fermata> e) {
				// TODO Auto-generated method stub
				
			}
	
		});
		//DepthFirstIterator dfv = new DepthFirstIterator(this.grafo,partenza);
		
		while(bfv.hasNext()) {
			Fermata f = (Fermata) bfv.next();
			result.add(f);
		}
		
		return result;
	}
	
	public Fermata trovaFermata(String nome ) {
		for(Fermata f : this.grafo.vertexSet()) {
			if(f.getNome().equals(nome)) {
				return f;
			}
		}
		return null;
	}
	
	public List<Fermata> trovaCaminno(Fermata partenza, Fermata arrivo) {
		fermateRaggiungibili(partenza); //creata la mapa dei predecessori
		List<Fermata> result = new LinkedList();
		result.add(arrivo);
		Fermata f = arrivo;
		while(predecessore.get(f)!=null) {
			f=predecessore.get(f);
			result.add(0,f);
			
		}
		return result; //arrivo ---> partenza (albero camini minimi)
	}
	
}
  