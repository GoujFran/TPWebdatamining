package exemples;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;

import arbres.Arbre;
import arbres.Noeud;

public class LectureArbres {


	public static void main(String[] args) throws IOException, ClassNotFoundException {

		Interface inter = new Interface();
		inter.afficherPremierEcran(); 
		
		/*LectureArbres lecture = new LectureArbres (true);
		lecture.creerArbres();*/

		/*LectureArbres lecture = new LectureArbres (false);
		lecture.importerArbres(); */
		
		/*System.out.println("Lecture des arbres");
		for (Arbre arbre : lecture.listeArbres) {
			System.out.println(arbre.getInitNoeud().getValeur() + " : " + arbre.getInitNoeud().getListeNoeuds().size());
		}*/

		//System.out.println(lecture.longueurDocuments.size());
		/*System.out.println("Liste des documents");
		LinkedList<String> docs = lecture.chercherMot("candidat");
		for (String doc : docs) {
			System.out.println(doc);
		}*/

		/*Requete requete = new Requete("dgfgdh");
		HashMap<String, Double> hashmap = requete.calculCos(lecture);
		for (Entry<String, Double> e : hashmap.entrySet()) {
			System.out.println(e.getKey()+ " : "+e.getValue());
		}*/

		/*Date maDate = new Date();
		System.out.println(maDate);*/
	}


	private HashMap<String,Integer> longueurDocuments = new HashMap<String,Integer>();

	ArrayList<Arbre> listeArbres = new ArrayList<Arbre>();

	String[] alphabet = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

	public LectureArbres(boolean charge) {
		if (charge == true){
			for (String c : alphabet){
				Arbre arbre = new Arbre(c);
				this.listeArbres.add(arbre);
			}
		} 
	}

	public String lireFichier(File file)  {

		String texte = "";

		FileInputStream fis = null;

		try {
			fis = new FileInputStream(file);

			int content;
			while ((content = fis.read()) != -1) {
				texte = texte + (char) content;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return texte;
	}


	public HashMap<String, Integer> wraperTT(String texte1) throws UnsupportedEncodingException {
		HashMap<String,Integer> hashmap = new HashMap<String,Integer>();

		byte[] bytes = texte1.getBytes("UTF-8");
		String texte= new String(bytes, "ISO-8859-1");
		texte = texte.replaceAll(",", " , ");
		texte = texte.replaceAll(".", " . ");
		texte = texte.replaceAll("'", " ' ");
		texte = texte.replaceAll("\"", " \" ");
		texte = texte.replaceAll("[(]", " ( ");
		texte = texte.replaceAll("[)]", " ) ");
		texte = texte.replaceAll("\\[|\\]" , "");
		texte = texte.replaceAll("[-+*/=@¤µ£]", " ");
		texte = texte.replaceAll("[«»{}_^]", "");
		texte = texte.replaceAll("[0123456789]", " ");
		texte = texte.replaceAll("[?!#$€%&'`;:/@...]", " ");
		//System.out.println(texte);
		String[] phrases = texte.split("[.]");
		//System.setProperty("treetagger.home", "/home/francoise/Documents/ENSAI/WebDataMining");
		System.setProperty("treetagger.home", "/home/theov/tree-tragger");
		TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
		try {
			//tt.setModel("/home/francoise/Documents/ENSAI/WebDataMining/lib/french-utf8.par:iso8859-1");
			tt.setModel("/home/theov/tree-tragger/lib/french-utf8.par:iso8859-1");

			tt.setHandler((token, pos, lemma) -> {
				if ( !pos.startsWith("NUM") && !pos.startsWith("KON") &&  !pos.startsWith("PRP") &&  !pos.startsWith("DET") &&  !pos.startsWith("PUN") &&  !pos.startsWith("PRO")){
					//System.out.println(token + " " + pos +" "+lemma);
					lemma = removeAccent(lemma.toLowerCase());
					if (hashmap.containsKey(lemma)) {
						hashmap.put(lemma,hashmap.get(lemma)+1);
					} else {
						hashmap.put(lemma, 1);
					}
				}
			}
					);
			for (String phrase : phrases){
				String[] mots = phrase.split(" ");
				tt.process(asList(mots));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TreeTaggerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			tt.destroy();
		}
		return hashmap;
	}

	public void lireTousLesDocument() throws UnsupportedEncodingException {
		File resources = new File("src/main/resources");
		String[] listeFichiers = resources.list();
		Arrays.sort(listeFichiers);

		for (String fichier : listeFichiers) {

			insererFichier(fichier);
		}
	}

	public void insererFichier(String fichier) throws UnsupportedEncodingException{
		File file = new File("src/main/resources/" + fichier);

		String texte = this.lireFichier(file);
		//System.out.println(texte);
		HashMap<String,Integer> hashmap = this.wraperTT(texte);

		int res=0;
		for (int i :hashmap.values()) {
			res += i;
		}

		longueurDocuments.put(fichier,res);
		remplirArbre(hashmap, fichier);
	}

	public void supprimerFichier(String fichier) throws UnsupportedEncodingException{
		File file = new File("src/main/resources/" + fichier);

		String texte = this.lireFichier(file);
		//System.out.println(texte);
		HashMap<String,Integer> hashmap = this.wraperTT(texte);

		Set<String> listeMots = hashmap.keySet();
		for (String mot : listeMots) {
			LinkedList<String> listDoc = this.chercherMot(mot);
			for (String doc : listDoc){
				if (doc.equals(fichier)){
					listDoc.remove(doc);
				}
			}
		}
	}

	public void methodeTest() throws UnsupportedEncodingException {
		File resources = new File("src/main/resources");
		String[] listeFichiers = resources.list();
		Arrays.sort(listeFichiers);
		String tousFichier = "";
		HashMap<String,Integer> hashmap = new HashMap<String,Integer>();

		for (String fichier : listeFichiers){
			File file = new File("src/main/resources/" + fichier);
			String texte1 = this.lireFichier(file);

			byte[] bytes = texte1.getBytes("UTF-8");
			String texte= new String(bytes, "ISO-8859-1");
			texte = texte.replaceAll(",", " , ");
			texte = texte.replaceAll("'", " ' ");
			texte = texte.replaceAll("\"", " \" ");
			texte = texte.replaceAll("[(]", " ( ");
			texte = texte.replaceAll("[)]", " ) ");
			texte = texte.replaceAll("\\[|\\]" , "");
			texte = texte.replaceAll("[-+*/=@¤µ£]", " ");
			texte = texte.replaceAll("[«»{}_^]", "");
			texte = texte.replaceAll("[0123456789]", " ");
			texte = texte.replaceAll("[?!#$€%&'`;:/@...]", " ");
			tousFichier = tousFichier + texte + "  Vleeschouwers  ";	
		}
		//System.setProperty("treetagger.home", "/home/francoise/Documents/ENSAI/WebDataMining");
		System.setProperty("treetagger.home", "/home/theov/tree-tragger");
		TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
		try {
			LinkedList<Integer> compteur = new LinkedList<Integer>();
			compteur.add(0);
			
			LinkedList<Integer> nbMots = new LinkedList<Integer>();
			nbMots.add(0);
			//tt.setModel("/home/francoise/Documents/ENSAI/WebDataMining/lib/french-utf8.par:iso8859-1");
			tt.setModel("/home/theov/tree-tragger/lib/french-utf8.par:iso8859-1");

			tt.setHandler((token, pos, lemma) -> {
				
				if ( !pos.startsWith("NUM") && !pos.startsWith("KON") &&  !pos.startsWith("PRP") &&  !pos.startsWith("DET") &&  !pos.startsWith("PUN") &&  !pos.startsWith("PRO")){

					if (lemma.equals("Vleeschouwers")){
						longueurDocuments.put(listeFichiers[compteur.getFirst()], nbMots.getFirst());
						nbMots.addFirst(0);
						
						int ind = compteur.getFirst();
						compteur.addFirst(ind+1);
					} else {
						//System.out.println(token + " " + pos +" "+lemma);
						int nb = nbMots.getFirst();
						nbMots.addFirst(nb+1);
						
						lemma = removeAccent(lemma.toLowerCase());
						int compt = 0;
						int index = -1;
						for (String lettre : alphabet) {
							if (lemma.startsWith(lettre)) {
								index = compt;
							}
							compt += 1;
						}
						//System.out.println(mot);
						listeArbres.get(index).getInitNoeud().insererMots(lemma, listeFichiers[compteur.get(0)]);

					}
				}
			}
					);
			String[] mots = tousFichier.split(" ");
			tt.process(asList(mots));


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TreeTaggerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			tt.destroy();
		}


	}

	public void remplirArbre(HashMap<String,Integer> hashmap, String document) {
		//System.out.println(document);
		for (String mot : hashmap.keySet()){
			int compteur = 0;
			int index = -1;
			for (String lettre : alphabet) {
				if (mot.startsWith(lettre)) {
					index = compteur;
				}
				compteur += 1;
			}
			//System.out.println(mot);
			for (int i = 0; i<hashmap.get(mot);i++) {
				listeArbres.get(index).getInitNoeud().insererMots(mot, document);
			}
		}
	}

	public String removeAccent(String source) {
		return Normalizer.normalize(source, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
	}


	public LinkedList<String> chercherMot(String mot) {
		LinkedList<String> documents = new LinkedList<>();
		int compteur = 0;
		int index = -1;

		//chercher le bon arbre
		for (String lettre : alphabet) {
			if (mot.startsWith(lettre)) {
				index = compteur;
			}
			compteur += 1;
		}
		Noeud noeud = listeArbres.get(index).getInitNoeud();

		for (int i = 1;i<mot.length();i++) {
			String lettre = mot.substring(i,i+1);
			boolean trouve = false;
			for (Noeud temp : noeud.getListeNoeuds()) {
				if (temp.getValeur().equals(lettre)) {
					noeud = temp;
					trouve = true;
					break;
				}
			}
			if (trouve == false) {
				//System.out.println("Le mot n'existe pas ! trouve false"); 
				noeud = new Noeud(null,null);
				break;
			} 
		}
		if (noeud.getFeuille() == null) {
			//System.out.println("Le mot n'existe pas ! pas de feuille"); 
		} else {
			documents = noeud.getFeuille().getDocuments();
		}
		return documents;
	}

	public HashMap<String, Integer> getLongueurDocuments() {
		return longueurDocuments;
	}

	public void setLongueurDocuments(HashMap<String, Integer> longueurDocuments) {
		this.longueurDocuments = longueurDocuments;
	}

	public void creerArbres() throws IOException {

		//this.lireTousLesDocument();
		
		this.methodeTest();

		//Pour sérialiser les arbres
		int compteur = 0;
		for (Arbre arbre : this.listeArbres) {
			FileOutputStream fos = new FileOutputStream("src/main/arbres/arbre"+ compteur +".serial");

			ObjectOutputStream oos= new ObjectOutputStream(fos);

			oos.writeObject(arbre); 
			oos.flush();

			oos.close();
			fos.close();
			compteur++;
		}
		FileOutputStream fos = new FileOutputStream("src/main/arbres/longueurDocuments.serial");
		ObjectOutputStream oos= new ObjectOutputStream(fos);

		oos.writeObject(this.getLongueurDocuments()); 
		oos.flush();

		oos.close();
		fos.close();
	}

	public void importerArbres() throws IOException, ClassNotFoundException {


		//Pour lire les arbres
		int compteur = 0;
		for (int i=0;i<26;i++) {
			FileInputStream fis = new FileInputStream("src/main/arbres/arbre"+ compteur +".serial");

			ObjectInputStream ois= new ObjectInputStream(fis);

			Arbre arbre = (Arbre) ois.readObject();
			this.listeArbres.add(arbre);
			ois.close();
			fis.close();
			compteur++;
		} 
		FileInputStream fis = new FileInputStream("src/main/arbres/longueurDocuments.serial");

		ObjectInputStream ois= new ObjectInputStream(fis);

		this.setLongueurDocuments((HashMap<String, Integer>) ois.readObject());
		ois.close();
		fis.close();
		compteur++;
	}

}


