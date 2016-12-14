package arbres;

import java.io.Serializable;
import java.util.LinkedList;

public class Arbre implements Serializable{

	private Noeud initNoeud;

	public Noeud getInitNoeud() {
		return initNoeud;
	}

	public Arbre(String valeur) {
		this.initNoeud = new Noeud(valeur,null);
	}
	
}
