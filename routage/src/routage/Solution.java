package routage;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Solution implements Comparable<Solution> {
	private final static int CAPACITE_DEFAULT = 100;
	private List<Camion> camions;
	private List<Client> monde;
	private int capacite = CAPACITE_DEFAULT;
	private int MAX_DEFAULT_RANDOM_CIRCUIT_LENGTH = 19;

	public Solution() {
		this.camions = new ArrayList<>();
	}

	public Solution(List<Client> monde) {
		this();
		this.monde = monde;
	}

	public Solution(Solution solution) {
		monde = new ArrayList<>();
		camions = new ArrayList<>();
		for (Client client : solution.monde) {
			monde.add(new Client(client));
		}
		for (Camion camion : solution.getCamions()) {
			camions.add(new Camion(camion));
		}
	}

	public List<Client> getMonde() {
		return monde;
	}

	public void setMonde(List<Client> monde) {
		this.monde = monde;
	}

	public List<Camion> getCamions() {
		return camions;
	}

	public void setCamions(List<Camion> camions) {
		this.camions = camions;
	}

	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}

	/**
	 * Cree la liste de clients a livrer
	 *
	 * @return liste de clients a livrer
	 * @throws IOException
	 */
	public List<Client> lireMonde(File fichier) throws IOException {
		String id;
		float x;
		float y;
		float quantite;
		float tmin;
		float tmax;
		float duree;

		Scanner scanner = null;
		this.camions = new ArrayList<>();

		// Creation scanner
		try {
			scanner = new Scanner(fichier);
		} catch (FileNotFoundException e) {
			System.err.println("Echec creation scanner");
			e.printStackTrace();
		}

		// Lecture fichier et creation clients
		monde = new ArrayList<>();
		// Decalage de la chaîne "Cap"
		scanner.next();
		// Recuperation capacite
		setCapacite(scanner.nextInt());
		// Decalage de la seconde ligne
		while (!scanner.hasNextInt()) {
			scanner.next();
		}
		// Infos du depot

		// Parcours de chaque ligne
		while (scanner.hasNext()) {
			// Sauvegarde des caracteristiques du client
			id = scanner.next();
			x = Float.parseFloat(scanner.next());
			y = Float.parseFloat(scanner.next());
			quantite = Float.parseFloat(scanner.next());
			tmin = Float.parseFloat(scanner.next());
			tmax = Float.parseFloat(scanner.next());
			duree = Float.parseFloat(scanner.next());
			monde.add(new Client(id, new Point2D.Double(x, y), tmin, tmax, quantite, duree));
		}
		return monde;
	}

	public void genSolutionUnCamion() {
		this.camions = new ArrayList<>();
		List<Client> circuit = new ArrayList<>();
		for (int i = 1; i < monde.size(); i++) {
			circuit.add(monde.get(i));
		}
		Camion camion = new Camion(circuit, CAPACITE_DEFAULT);
		camions.add(camion);
	}

	public void genSolutionPleinDeCamions() {
		this.camions = new ArrayList<>();
		for (int i = 1; i < monde.size(); i++) {
			List<Client> circuit = new ArrayList<>();
			Client client = monde.get(i);
			circuit.add(client);
			Camion camion = new Camion(circuit, CAPACITE_DEFAULT);
			camions.add(camion);
		}
	}

	public void random() {
		this.camions = new ArrayList<>();

		Random rand = new Random();
		//création d'une liste des clients en ordre aléatoire
		List<Client> clientsAleatoire = new LinkedList<>();
		clientsAleatoire.addAll(monde);
		Collections.shuffle(clientsAleatoire);
		//attribution d'un nombre aléatoire de cette liste à un camion tant qu'on n'a pas fini la liste
		int i = 0;
		while (i < clientsAleatoire.size()) {
			int circuitLength = rand.nextInt(MAX_DEFAULT_RANDOM_CIRCUIT_LENGTH) + 1;
			List<Client> circuit = new ArrayList<>();
			for (int j = 0; j < circuitLength && i < clientsAleatoire.size(); j++) {
				circuit.add(clientsAleatoire.get(i));
				i++;
			}
			Camion camion = new Camion(circuit, CAPACITE_DEFAULT);
			System.err.println("camion = " + camion);
			camions.add(camion);
		}
	}

	public double tempsTotalDeParcours() {
		double total = 0;
		for (Camion camion : camions) {
			total += camion.getDureeTrajet(monde);
		}
		return total;
	}

	public boolean valide() {
		Set<Client> clientsDesservis = new HashSet<>();
		for (Camion camion : camions) {
			for (Client client : camion.getCircuit()) {
				if (clientsDesservis.contains(client)) {
					return false;
				}
				clientsDesservis.add(client);
			}
		}
		clientsDesservis.add(monde.get(0)); //Ajout du dépôt
		System.err.println("clientsDesservis.size() = " + clientsDesservis.size());
		boolean tousClientsDesservis = clientsDesservis.containsAll(monde) && monde.containsAll(clientsDesservis);
		return tousClientsDesservis;
	}

	public boolean isSet() {
		return !camions.isEmpty();
	}

	@Override
	public int compareTo(Solution s) {
		return (int) -(s.tempsTotalDeParcours() - tempsTotalDeParcours());
	}

	private void retireCamionsVides() {
		for (Iterator<Camion> it = camions.iterator(); it.hasNext(); ) {
			Camion camion = it.next();
			if (camion.getCircuit().isEmpty()) {
				it.remove();
				System.err.println("là je supprime un camion");
			}
		}
	}

	public void bougeClient(int idClient, int idCamionDestination) {
		Client client = monde.get(idClient);
		Camion ancienCamion = getCamion(client);
		Camion camionDestination;

		if (idCamionDestination < camions.size() && idCamionDestination >= 0) {
			camionDestination = camions.get(idCamionDestination);
			System.err.println("bouger client");
		} else {
			camionDestination = new Camion(CAPACITE_DEFAULT);
			camions.add(camionDestination);
			System.err.println("créer camion");
		}

		System.err.println("idClient = " + idClient);
		System.err.println("ancienCamion = " + ancienCamion);
		if (ancienCamion.getCircuit().size() == 1) { //J'ai un NullPointerException ici, ce qui veut dire que
			System.err.println("là, je devrais supprimer le camion, normalement.");
		}
		ancienCamion.getCircuit().remove(client);
		camionDestination.getCircuit().add(client);
		retireCamionsVides();
		valide();
	}

	private Camion getCamion(Client client) {
		for (Camion camion : camions) {
			if (camion.getCircuit().contains(client)) {
				return camion;
			}
		}
		System.err.println("Client non trouvé dans les circuits des camions.");
		return null;
	}

	public void imprimerFichierDot() {
		System.out.println("digraph {");
		for (Camion camion : camions) {
			List<Client> circuit = camion.getCircuit();
			String line = monde.get(0).getId() + " -> ";
			for (int i = 0; i < circuit.size() - 1; i++) {
				line += circuit.get(i).getId() + " -> ";
			}
			line += monde.get(0).getId() + ";";

			System.out.println(line);
		}
		System.out.println("}");
	}
}
