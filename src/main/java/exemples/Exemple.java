package exemples;

import org.annolab.tt4j.TreeTaggerWrapper;
import static java.util.Arrays.asList;

public class Exemple {
	public static void main(String[] args) throws Exception {
		// Point TT4J to the TreeTagger installation directory. The executable is expected
		// in the "bin" subdirectory - in this example at "/opt/treetagger/bin/tree-tagger"
		System.setProperty("treetagger.home", "/home/francoise/Documents/ENSAI/WebDataMining");
		TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
		try {
			tt.setModel("/home/francoise/Documents/ENSAI/WebDataMining/lib/french-utf8.par:iso8859-1");

			tt.setHandler((token, pos, lemma) -> 
			System.out.println(token + "\t" + pos + "\t" + lemma));


			tt.process(asList(new String[] { "Ceci", "est", "un", "test", "." }));
		}
		finally {
			tt.destroy();
		}
	}

}
