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

@ApplicationScoped
@Named("jsonReader")
public class JsonReader {

	private JsonFactory factory;

	private final int DOMAINS = 0, LASTNAMES = 1, FIRSTNAMES = 2;

	private ArrayList<List<String>> datasets = new ArrayList<List<String>>();
	private String[] datasetsPaths = { "resources/domains.json", "resources/betterLN.json",
			"resources/betterFN.json" };

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

	public List<String> getDomains() {
		return datasets.get(DOMAINS);
	}

	public List<String> getFirstNames() {
		return datasets.get(FIRSTNAMES);
	}

	public List<String> getLastNames() {
		return datasets.get(LASTNAMES);
	}
}
