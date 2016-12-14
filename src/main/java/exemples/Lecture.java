package exemples;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;

public class Lecture {

	public static void main(String[] args) {
		
		//Lecture lecture = new Lecture();

		//HashMap<String, HashMap<String, Integer>> hashmap = lecture.lireTousLesDocument();

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


	public HashMap<String, Integer> wraperTT(String texte) {
		HashMap<String,Integer> hashmap = new HashMap<String,Integer>();
		texte = texte.replaceAll(",", " ,");
		texte = texte.replaceAll("'", " ' ");
		texte = texte.replaceAll("\"", " \" ");
		//System.out.println(texte);
		String[] phrases = texte.split("[.]");
		System.setProperty("treetagger.home", "/home/francoise/Documents/ENSAI/WebDataMining");
		TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
		try {
			tt.setModel("/home/francoise/Documents/ENSAI/WebDataMining/lib/french-utf8.par:iso8859-1");

			tt.setHandler((token, pos, lemma) -> {
				if (!pos.startsWith("KON") &&  !pos.startsWith("PRP") &&  !pos.startsWith("DET") &&  !pos.startsWith("PUN") &&  !pos.startsWith("PRO")){
					if (hashmap.containsKey(lemma)) {
						hashmap.put(lemma,hashmap.get(lemma)+1);
					} else {
						hashmap.put(lemma, 1);
					}
				}
			}
					);

			for (String phrase : phrases) {
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

	public HashMap<String, HashMap<String, Integer>> lireTousLesDocument() {
		File resources = new File("src/main/resources");
		String[] listeFichiers = resources.list();
		Arrays.sort(listeFichiers);

		HashMap<String,HashMap<String,Integer>> hashMapGlobale = new HashMap<String,HashMap<String,Integer>>();

		for (String fichier : listeFichiers) {
			
			File file = new File("src/main/resources/" + fichier);
			String texte = this.lireFichier(file);
			//System.out.println(texte);
			HashMap<String,Integer> hashmap = this.wraperTT(texte);
			hashMapGlobale.put(fichier, hashmap);
		}
		return hashMapGlobale;

	}
}
