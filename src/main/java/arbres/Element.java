package arbres;

import java.io.Serializable;

public abstract class Element implements Serializable {
	private Noeud noeudParent;

	public Element(Noeud noeudParent) {
		this.noeudParent = noeudParent;
	}
		
}
