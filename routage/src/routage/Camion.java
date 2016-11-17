package routage;

import java.awt.geom.Point2D;
import java.util.List;

public class Camion {
	private List<Client> circuit;
	private int capacite;
	
	public Camion(List<Client> circuit, int capacite) {
		super();
		this.circuit = circuit;
		this.capacite = capacite;
	}

	public List<Client> getCircuit() {
		return circuit;
	}
	
	public double getDureeTrajet(List<Client> monde){
		Point2D.Double depot = monde.get(0).getCoordonnees();
		Point2D.Double premierClient = circuit.get(0).getCoordonnees(); 
		Point2D.Double dernierClient = circuit.get(circuit.size()-1).getCoordonnees();
		
		double duree = depot.distance(premierClient) + dernierClient.distance(depot);
		
		for (int i = 0; i < circuit.size()-1; i++) {
			Point2D.Double clientA = circuit.get(i).getCoordonnees();
			Point2D.Double clientB = circuit.get(i+1).getCoordonnees();
			duree+=clientA.distance(clientB);			
		}
		return duree;
	}
	
}
