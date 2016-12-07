package routage;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Camion {
	private List<Client> circuit;
	private int capacite;
	private Color color;

	public Camion(List<Client> circuit, int capacite) {
		super();
		this.circuit = circuit;
		this.capacite = capacite;
		this.color = randomColor();
	}

	public Camion(int capacite) {
		super();
		this.circuit = new ArrayList<>();
		this.capacite = capacite;
		this.color = randomColor();
	}

	public Camion(Camion camion) {
		capacite = camion.capacite;
		color = randomColor();
		circuit = new ArrayList<>();
		for (Client client : camion.circuit) {
			circuit.add(new Client(client));
		}
	}

	public List<Client> getCircuit() {
		return circuit;
	}

	public double getDureeTrajet(List<Client> monde) {
		Point2D.Double depot = monde.get(0).getCoordonnees();
		Point2D.Double premierClient = circuit.get(0).getCoordonnees();
		Point2D.Double dernierClient = circuit.get(circuit.size() - 1).getCoordonnees();

		double duree = depot.distance(premierClient) + dernierClient.distance(depot);

		for (int i = 0; i < circuit.size() - 1; i++) {
			Point2D.Double clientA = circuit.get(i).getCoordonnees();
			Point2D.Double clientB = circuit.get(i + 1).getCoordonnees();
			duree += clientA.distance(clientB);
		}
		return duree;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Camion camion = (Camion) o;

		if (capacite != camion.capacite) return false;
		return circuit != null ? circuit.equals(camion.circuit) : camion.circuit == null;
	}

	@Override
	public int hashCode() {
		int result = circuit != null ? circuit.hashCode() : 0;
		result = 31 * result + capacite;
		return result;
	}

	private Color randomColor() {
		Random rand = new Random();
		// Will produce only bright / light colours:
		float r = rand.nextFloat();// / 2f + 0.5f;
		float g = rand.nextFloat();// / 2f + 0.5f;
		float b = rand.nextFloat();// / 2f + 0.5f;

		return new Color(r, g, b);
	}

	public Color getColor() {
		return color;
	}
}
