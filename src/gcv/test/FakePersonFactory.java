package gcv.test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import gcv.beans.Activity;
import gcv.beans.Nature;
import gcv.beans.Person;
import gcv.util.PasswordHash;

@Dependent
public class FakePersonFactory {

	private JsonFactory factory;

	private final int DOMAINS = 0, LASTNAMES = 1, FIRSTNAMES = 2;

	private static List<String>[] datasets = new List[3];
	private static String[] datasetsPaths = { "src/gcv/util/domains.json", "src/gcv/util/betterLN.json",
			"src/gcv/util/betterFN.json" };

	public FakePersonFactory() throws InterruptedException {
		factory = new ObjectMapper().getFactory();

		for (int i = 0; i < datasets.length; i++) {

			if (datasets[i] == null) {
				final long debut = System.nanoTime();

				System.out.println("remplissage du dataset " + i); 
				try (JsonParser parser = factory.createParser(new File(datasetsPaths[i]))) {

					while (parser.nextToken() != null) {
						datasets[i] = Arrays.asList(parser.readValueAs(String[].class));
					}
					System.out.println(i + ": " +datasets[i].size());
				} catch (IOException e) {
					e.printStackTrace();
				}
			
				final long fin = System.nanoTime();
				final long durée = (fin - debut) / 1_000_000;
				System.out.println("Durée de "+i+" = " + (double) durée / 1000 + " s.");
			}
		}
	}

	@Produces
	@Named("fakePerson")
	public Person produceFakePerson() {
		Person person = new Person();
		
		person.setFirstName(produceRandomFirstName());
		person.setLastName(produceRandomLastName());
		person.setMail(produceRandomMail(person.getFirstName(), person.getLastName()));
		person.setBirthDate(produceRandomBirthDate());
		person.setWebSite(produceRandomWebsite(person.getFirstName(), person.getLastName()));
		person.setPassword(produceRandomPassword(8));

		// System.err.println("produces ... " + person);
		return person;
	}

	/*
	 * @Produces
	 * 
	 * @Named("fakePersonWithCV") public Person produceFakePersonWithCV() { Person
	 * person = produceFakePerson();
	 * 
	 * person.setCv(produceFakeCV(person.getBirthDate()));
	 * 
	 * System.err.println("adding cv to " + person); return person; }
	 */

	private Date produceRandomBirthDate() {
		int year = getRandomNumberInRange(1991, 2001);
		int day = getRandomNumberInRange(1, 27);
		int month = getRandomNumberInRange(0, 11);

		return new Date(year - 1900, month, day);
	}

	private String produceRandomWebsite(String firstName, String lastName) {
		return "www." + firstName.replaceAll(" ", "_") + "-" + lastName.replaceAll(" ", "_") + ".com";
	}

	private String produceRandomMail(String firstName, String lastName) {

		int randomIndex = getRandomNumberInRange(0, datasets[DOMAINS].size() - 1);
		String domain = datasets[DOMAINS].get(randomIndex);

		return new String(firstName.replaceAll(" ", "_") + "." + lastName.replaceAll(" ", "_") + "@" + domain);
	}

	private String produceRandomLastName() {

		int randomIndex = getRandomNumberInRange(0, datasets[LASTNAMES].size() - 1);
		String choice = datasets[LASTNAMES].get(randomIndex);

		return choice.toLowerCase();
	}

	private String produceRandomFirstName() {

		int randomIndex = getRandomNumberInRange(0, datasets[FIRSTNAMES].size() - 1);
		String choice = datasets[FIRSTNAMES].get(randomIndex);

		return choice.toLowerCase();
	}

	private String produceRandomPassword(int length) {
		/*try {
			return PasswordHash.createHash("placeholder");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		return "placeholder";
	}

	/* min et max sont inclus. */
	public static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		return ThreadLocalRandom.current().nextInt(min, max+1);
	}
}
