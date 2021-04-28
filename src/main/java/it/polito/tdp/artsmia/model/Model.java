package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Graph<ArtObject,DefaultWeightedEdge> grafo; // specificato nel testo
	private ArtsmiaDAO dao;
	//creo una mappa per non creare più volte gli ArtObject uguali inutilmente ogni volta che viene creato nuov grafo
	private Map<Integer, ArtObject> idMap;
	
	public Model() {
		dao = new ArtsmiaDAO();
		idMap = new HashMap<Integer, ArtObject>();
	}
	
	//conviene inizializzare qua il grafo perchè nel model ce ne crea uno solo che poi scomodo da modificare
	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Aggiungere i vertici
		// 1. -> Recupero tutti gli ArtObject dal db
		// 2. -> Li inserisco come vertici
		
		// lascio a dao di dovere riempire map
		dao.listObjects(idMap); //oggetti creati una sola volta
		Graphs.addAllVertices(grafo, idMap.values());
		
		//aggiungere archi
		// APPROCCIO 1
		// + operazioni intere rispetto a database
		// --> doppio ciclo for sui vertici
		// --> dati due vertici controllo se sono collegati
		
		/*for(ArtObject a1 : this.grafo.vertexSet()) {
			for(ArtObject a2 : this.grafo.vertexSet()) {
				//controllo che oggetti siano diversi e che non esista già un arco
					//-->archi non orientati quindi controllo se esiste direzione opposta
				if(!a1.equals(a2) && !grafo.containsEdge(a1,a2)) {
					// devo collegare a1 e a2?
					int peso = dao.getPeso(a1,a2);
					if(peso > 0) {
						Graphs.addEdge(this.grafo, a1, a2, peso);
					}
				}
			}
		}
*/
		//APPROCCIO 3
		
		for(Adiacenza a : dao.getAdiacenze()) {
			if(a.getPeso()>0) {
				Graphs.addEdge(grafo, this.idMap.get(a.getId1()), this.idMap.get(a.getId2()), a.getPeso());
			}
		}
		
		
		System.out.println("GRAFO CREATO!");
		System.out.println("# VERTICI: "+grafo.vertexSet().size());
		System.out.println("# ARCHI: "+ grafo.edgeSet().size());
	}
	

}
