package exemples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class Test {

	public static void main(String[] args) throws UnsupportedEncodingException{

		String fichier = "2003-01-02-100.txt";

		File file = new File("src/main/resources/" + fichier);
		
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
		
		byte[] bytes = texte.getBytes("ISO-8859-1");
		String doc2 = new String(bytes, "ISO-8859-1");
		
		System.out.println(texte);
		System.out.println(doc2);
	}
}
