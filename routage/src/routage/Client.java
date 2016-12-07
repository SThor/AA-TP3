package routage;

import java.awt.geom.Point2D;

public class Client {
	private Point2D.Double coordonnees;
	private float tMin;
	private float tMax;
	private float quantite;
	private float tDechargement;

	private String id;

	public Client(String id, Point2D.Double coordonnees, float tMin, float tMax, float quantite, float tDechargement) {
		super();
		this.id = id;
		this.coordonnees = coordonnees;
		this.tMin = tMin;
		this.tMax = tMax;
		this.quantite = quantite;
		this.tDechargement = tDechargement;
	}

	public Client(Client client) {
		coordonnees = new Point2D.Double(client.coordonnees.x, client.getCoordonnees().y);
		tMin = client.tMin;
		tMax = client.tMax;
		quantite = client.quantite;
		tDechargement = client.tDechargement;
		id = "" + client.id;
	}

	public Point2D.Double getCoordonnees() {
		return coordonnees;
	}

	public float gettMin() {
		return tMin;
	}

	public float gettMax() {
		return tMax;
	}

	public float getQuantite() {
		return quantite;
	}

	public float gettDechargement() {
		return tDechargement;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Client client = (Client) o;

		return coordonnees != null ? coordonnees.equals(client.coordonnees) : client.coordonnees == null;
	}

	@Override
	public int hashCode() {
		return coordonnees != null ? coordonnees.hashCode() : 0;
	}

	public String getId() {
		return id;
	}
}
