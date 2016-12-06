package routage;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Solution implements Comparable<Solution> {
	public static final int MEILLEUR_PARMIS_TOUS = 0;
	public static final int PREMIER_AMELIORANT = -1;
	private final static int CAPACITE_DEFAULT = 100;
	private static final int NOUVEAU_CAMION = -1;
	private List<Camion> camions;
	private List<Client> monde;
	private Map<Client, Camion> repartition;
	private int capacite = CAPACITE_DEFAULT;
	private int MAX_DEFAULT_RANDOM_CIRCUIT_LENGTH = 19;

	public Solution() {
		this.camions = new ArrayList<>();
		this.repartition = new HashMap<>();
	}

	public Solution(List<Client> monde) {
		this();
		this.monde = monde;
	}

	public Solution copie(Solution originale) {
		Solution copie = new Solution();
		copie.setMonde(new ArrayList<Client>(originale.monde));
		copie.setCamions(new ArrayList<Camion>(originale.camions));
		copie.setRepartition(new HashMap<Client, Camion>(originale.repartition));
		return copie;
	}

	public Map<Client, Camion> getRepartition() {
		return repartition;
	}

	public void setRepartition(Map<Client, Camion> repartition) {
		this.repartition = repartition;
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
		this.repartition = new HashMap<>();

		// Creation scanner
		try {
			scanner = new Scanner(fichier);
		} catch (FileNotFoundException e) {
			System.out.println("Echec creation scanner");
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
		this.repartition = new HashMap<>();
		List<Client> circuit = new ArrayList<>();
		for (int i = 1; i < monde.size(); i++) {
			circuit.add(monde.get(i));
		}
		Camion camion = new Camion(circuit, CAPACITE_DEFAULT);
		for (Client client : circuit) {
			repartition.put(client, camion);
		}
		camions.add(camion);
	}

	public void genSolutionPleinDeCamions() {
		this.camions = new ArrayList<>();
		this.repartition = new HashMap<>();
		for (int i = 1; i < monde.size(); i++) {
			List<Client> circuit = new ArrayList<>();
			Client client = monde.get(i);
			circuit.add(client);
			Camion camion = new Camion(circuit, CAPACITE_DEFAULT);
			repartition.put(client, camion);
			camions.add(camion);
		}
	}

	public void random() {
		this.camions = new ArrayList<>();
		this.repartition = new HashMap<>();

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
			System.out.println("camion = " + camion);
			camions.add(camion);
			for (Client client : camion.getCircuit()) {
				repartition.put(client, camion);
			}
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
		return clientsDesservis.containsAll(monde) &&
				monde.containsAll(clientsDesservis)/* &&
				repartition.keySet().containsAll(monde) &&
				repartition.values().containsAll(camions) &&
				camions.containsAll(repartition.values())*/;
	}

	/**
	 * @param nbRedemarrage   nombre de redemarrages de la recherche
	 * @param nbIteRechLocale nombre d'iterations sur la recherche locale
	 * @param choixVoisin     meilleur parmis tous : 0, premier ameliorant : -1, meilleur parmis echantillon : taille de l'echantillon
	 * @return
	 */
	public Solution calculerSolution(int nbRedemarrage, int nbIteRechLocale, int choixVoisin) {
		TreeSet<Solution> solutions = new TreeSet<>();
		for (int i = 0; i < nbRedemarrage; i++) {
			solutions.add(recherche(nbIteRechLocale, choixVoisin));
		}
		return solutions.first();
	}

	public Solution recherche(int nbIteRechLocale, int choixVoisin) {
		TreeSet<Solution> solutions = new TreeSet<>();
		for (int i = 0; i < nbIteRechLocale; i++) {
			solutions.add(iteration(choixVoisin, null)); //TODO
		}
		return solutions.first();
	}

	private Solution iteration(int choixVoisin, Solution actuelle) {
		Solution solution;
		switch (choixVoisin) {
			case MEILLEUR_PARMIS_TOUS:
				solution = genAllVoisins().first();
				break;
			case PREMIER_AMELIORANT:
				Solution voisin = genRandomVoisin();
				while (actuelle.compareTo(voisin) >= 0) { //tant que le voisin généré n'améliore pas la solution actuelle
					voisin = genRandomVoisin();
				}
				solution = voisin;
				break;
			default:
				solution = genRandomVoisins(choixVoisin).first();
				break;
		}
		return solution;
	}

	public Solution genVoisinChange(int camion, int client) {
		Solution voisin = copie(this);

		int i = 0; //TODO
		voisin.getCamions().get(i);

		return voisin;
	}

	public Solution genRandomVoisin() {
		Random rand = new Random();
		int n = rand.nextInt(2);
		switch (n) {
			case 0:
				return genVoisinAjouteCamion(rand.nextInt(monde.size()));
			case 1:
				return genVoisinBougeClient(rand.nextInt(monde.size()), rand.nextInt(camions.size()));
			default:
				return null;
		}
	}

	public Solution genRandomVoisinAjouteCamion() {
		Random rand = new Random();
		System.out.println("ajoute Camion");
		return genVoisinAjouteCamion(rand.nextInt(monde.size() - 1) + 1);
	}

	public Solution genRandomVoisinBougeClient() {
		Random rand = new Random();
		return genVoisinBougeClient(rand.nextInt(monde.size() - 1) + 1, rand.nextInt(camions.size()));
	}

	private Solution genVoisinAjouteCamion(int idClient) {
		Solution voisin = copie(this);
		voisin.bougerClient(idClient, NOUVEAU_CAMION);
		return voisin;
	}

	private Solution genVoisinBougeClient(int idClient, int idCamionDestination) {
		Solution voisin = copie(this);
		voisin.bougerClient(idClient, idCamionDestination);
		return voisin;
	}

	public TreeSet<Solution> genRandomVoisins(int nbVoisins) {
		TreeSet<Solution> voisins = new TreeSet<>();

		while (voisins.size() < nbVoisins) {
			Solution voisin = genRandomVoisin();
			voisins.add(voisin);
		}

		return voisins;
	}

	public TreeSet<Solution> genAllVoisins() {
		TreeSet<Solution> voisins = new TreeSet<>();

		for (int i = 1; i < monde.size(); i++) {
			voisins.add(genVoisinAjouteCamion(i));
		}
		for (int i = 0; i < camions.size(); i++) {
			for (int j = 0; j < camions.size(); j++) {
				if (i != j) {
					voisins.add(genVoisinBougeClient(i, j));
				}
			}
		}

		return voisins;
	}

	public boolean isSet() {
		return !camions.isEmpty();
	}

	@Override
	public int compareTo(Solution s) {
		return (int) (s.tempsTotalDeParcours() - tempsTotalDeParcours());
	}

	private void retirerCamionsVides() {
		for (Iterator<Camion> it = camions.iterator(); it.hasNext();) {
			Camion camion = it.next();
			if (camion.getCircuit().isEmpty()) {
				it.remove();
				System.out.println("là je supprime un camion");
			}
		}
	}

	private void bougerClient(int idClient, int idCamionDestination) {
		Client client = monde.get(idClient);
		Camion ancienCamion = repartition.get(client);
		Camion camionDestination;

		if (idCamionDestination < camions.size() && idCamionDestination >= 0) {
			camionDestination = camions.get(idCamionDestination);
			System.out.println("bouger client");
		} else {
			camionDestination = new Camion(CAPACITE_DEFAULT);
			camions.add(camionDestination);
			System.out.println("créer camion");
		}

		repartition.remove(client);
		if(ancienCamion.getCircuit().size() == 1){ //J'ai un NullPointerException ici, ce qui veut dire que
			System.out.println("là, je devrais supprimer le camion, normalement.");
		}
		ancienCamion.getCircuit().remove(client);

		camionDestination.getCircuit().add(client);
		repartition.put(client, camionDestination);

		retirerCamionsVides();
	}
}
