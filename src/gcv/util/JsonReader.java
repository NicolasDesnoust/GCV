package gcv.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import javax.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Bean CDI de portée application convertissant des fichiers JSON en Objets JAVA.
 * 
 * Ce bean est principalement utilisé pour la génération aléatoire de personnes et lors de
 * tests unitaires.
 * 
 * @author Nicolas DESNOUST
 * @author Serigne Bassirou Mbacké LY
 * 
 * @see gcv.util.FakePersonFactory
 */
@ApplicationScoped
@Named("jsonReader")
public class JsonReader {

	/**Producteur de parsers de JSON.
	 */
	private JsonFactory factory;

	private final int DOMAINS = 0, LASTNAMES = 1, FIRSTNAMES = 2;

	/** ArrayList contenant tous les jeux de données convertis à partir de fichiers JSON.
	 *  Les jeux de données sont des listes de chaînes de caractères.
	 */
	private ArrayList<List<String>> datasets = new ArrayList<List<String>>();
	
	/** Chemins des différents fichiers JSON à parser.
	 */
	private String[] datasetsPaths = { "resources/domains.json", "resources/betterLN.json",
			"resources/betterFN.json" };

	/**Constructeur permettant de parser les fichiers JSON.
	 * 
	 * @throws InterruptedException
	 */
	public JsonReader() throws InterruptedException {
		factory = new ObjectMapper().getFactory();

		for (int i = 0; i < datasetsPaths.length; i++) {

			try (JsonParser parser = factory.createParser(new File(datasetsPaths[i]))) {

				while (parser.nextToken() != null) {
					datasets.add(Arrays.asList(parser.readValueAs(String[].class)));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** Retourne le jeu de données contenant divers noms de domaine pour des adresses mail.
	 * @return Le jeu de données contenant divers noms de domaine pour des adresses mail.
	 */
	public List<String> getDomains() {
		return datasets.get(DOMAINS);
	}

	/** Retourne le jeu de données contenant divers prénoms recensés en France.
	 * @return Le jeu de données contenant divers prénoms recensés en France.
	 */
	public List<String> getFirstNames() {
		return datasets.get(FIRSTNAMES);
	}

	/** Retourne le jeu de données contenant divers noms de famille recensés en France.
	 * @return Le jeu de données contenant divers noms de famille recensés en France.
	 */
	public List<String> getLastNames() {
		return datasets.get(LASTNAMES);
	}
}
