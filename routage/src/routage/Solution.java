package routage;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Solution {
	private Set<Camion> camions;
	private List<Client> monde;
	
	public Solution(List<Client> monde){
		this.monde = monde;
	}
	
	public Solution(List<Client> monde, Set<Camion> camions){
		this.monde = monde;
		this.camions = camions;
	}

	public void random(){
		Random rand = new Random();
		//création d'une liste des clients en ordre aléatoire
		List<Client> clientsAleatoire = new LinkedList<>();
		for (Client client : monde) {
			clientsAleatoire.add(rand.nextInt(),client);
		}
		//attribution d'un nombre aléatoire de cette liste un camion tant qu'on n'a pas fini la liste
	}
	
	public double tempsTotalDeParcours(){
		double total = 0;
		for (Camion camion : camions) {
			total+=camion.getDureeTrajet(monde);
		}
		return total;
	}
	
	public boolean valide(){
		Set<Client> clientsDesservis = new HashSet<>();
		for (Camion camion : camions) {
			for (Client client : camion.getCircuit()) {
				if(clientsDesservis.contains(client)){
					return false;
				}
				clientsDesservis.add(client);
			}
		}
		clientsDesservis.add(monde.get(0)); //Ajout du dépôt
		return clientsDesservis.containsAll(monde) && monde.containsAll(clientsDesservis);
	}
}
