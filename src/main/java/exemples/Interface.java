package exemples;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Interface {

	private LectureArbres lecture;

	public void afficherPremierEcran() throws IOException, ClassNotFoundException {
		String[] possibilites = {"Créer les arbres","Importer les arbres"};
		String messageAccueil = "Veuillez choisir votre action.";


		int choix = JOptionPane.showOptionDialog(null, 
				messageAccueil,
				"Accueil",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				possibilites,
				null);
		if (choix == 0) {
			lecture = new LectureArbres (true);
			lecture.creerArbres();
		}
		if (choix == 1) {
			lecture = new LectureArbres (false);
			lecture.importerArbres();
		}
		this.faireUneRecherche();
	}

	public void faireUneRecherche() {
		JTextField recherche = new JTextField();

		JOptionPane.showOptionDialog(null, 
				new Object[] {"Votre recherche :", recherche},
				"Recherche",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);

		Requete requete = new Requete(recherche.getText());

		HashMap<String, Double> hashmap = requete.calculCos(lecture);
		Set<String> listeDocuments = hashmap.keySet();

		this.afficherResultat(listeDocuments);
	}

	public void afficherResultat(Set<String> listeDocuments) {
		String[] listeTitreArticles = listeDocuments.toArray(new String[listeDocuments.size()]);

		String choix = (String)JOptionPane.showInputDialog(null, 
				"Articles à noter",
				"Sélection de l'article à noter",
				JOptionPane.QUESTION_MESSAGE, null, listeTitreArticles, null);

		if (choix!=null){
			File file = new File("src/main/resources/" + choix);
			String texte = lecture.lireFichier(file);
			//JOptionPane.showMessageDialog(null, texte, choix, JOptionPane.INFORMATION_MESSAGE);
			JTextArea message = new JTextArea(texte);
			message.setRows(20);
			message.setColumns(50);
			message.setLineWrap(true);
			message.setWrapStyleWord(true);

			JScrollPane scrollPane = new JScrollPane(message);

			JOptionPane.showMessageDialog(null, scrollPane);  
		}
	}

	public LectureArbres getLecture() {
		return lecture;
	}


}
