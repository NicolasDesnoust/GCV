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
public class FakeFactory {

	private JsonFactory factory;

	private final int DOMAINS = 0, LASTNAMES = 1, FIRSTNAMES = 2;
	
	private static List<String>[] datasets = new List[3];
	private static String[] datasetsPaths = {
												"src/gcv/util/domains.json",
												"src/gcv/util/betterLN.json",
												"src/gcv/util/betterFN.json"
											};

	public FakeFactory() {
		factory = new ObjectMapper().getFactory();
		System.out.println("conversion...");
		
		// TODO: A synchroniser
		for (int i = 0; i < datasets.length; i++) {
			
			if (datasets[i] == null) {
				try (JsonParser parser = factory.createParser(new File(datasetsPaths[i]))) {
	
					while (parser.nextToken() != null) {
						datasets[i] = Arrays.asList(parser.readValueAs(String[].class));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
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

		//System.err.println("produces ... " + person);
		return person;
	}

	/*@Produces
	@Named("fakePersonWithCV")
	public Person produceFakePersonWithCV() {
		Person person = produceFakePerson();

		person.setCv(produceFakeCV(person.getBirthDate()));

		System.err.println("adding cv to " + person);
		return person;
	}*/

	/*
	 * Produit un CV. Procède en créant un nombre aléatoire d'activités pour chaque
	 * nature possible (sauf OTHER).
	 */
	/*private Set<Activity> produceFakeCV(Date birthDate) {
		Set<Activity> CV = new HashSet<Activity>();
		int birthYear = birthDate.getYear();*/
		/*
		 * Année d'obtention du BAC générée aléatoirement à partir de la date de
		 * naissance.
		 */
		//int baccalaureateYear = birthYear + getRandomNumberInRange(16, 18);

		/*
		 * int workExpNbr = getRandomNumberInRange(1, 4), educationNbr =
		 * getRandomNumberInRange(3, 4), projectsNbr = getRandomNumberInRange(3, 5);
		 * 
		 * for (int i = 0; i < educationNbr; i++) produceFakeActivity(Nature.EDUCATION,
		 * birthYear + 17 + i);
		 * 
		 * for (int i = 0; i < projectsNbr; i++) produceFakeActivity(Nature.PROJECT,
		 * birthYear + 18 + getRandomNumberInRange(0, educationNbr));
		 * 
		 * for (int i = 0; i < workExpNbr; i++) produceFakeActivity(Nature.WORK_EXP,
		 * birthYear);
		 */

		/*
		 * Set<Activity> fakeEducation = produceFakeEducation(baccalaureateYear);
		 * CV.addAll(fakeEducation);
		 */
		/*
		 * On suppose que les projets sont réalisés entre l'année qui suit l'obtention
		 * du BAC et la durée du parcours effectué ensuite. Attention l'année du BAC
		 * fait partie de fakeEducation donc il faut la retirer.
		 */
		/*
		 * int durationEducationPostBac = -1;
		 * CV.addAll(produceFakeProjectsInRange(baccalaureateYear + 1, baccalaureateYear
		 * + 1 + durationEducationPostBac - 1));
		 */
		/* L'expérience professionnelle débute après les études. */
		/* CV.addAll(produceFakeWorkExp(baccalaureateYear + durationEducation + 1)); */

	/*	return CV;
	}*/

	/*
	 * Produit une activité d'une certaine nature. L'identifiant est généré
	 * automatiquement dans la base de données et le propriétaire est renseigné lors
	 * de l'ajout au CV d'une personne via la méthode addActivity(Activity
	 * activity).
	 */
	/*private Activity produceFakeActivity(Nature nature, int year) {
		Activity activity = new Activity();

		activity.setDescription(produceRandomDescription());
		activity.setYear(year);
		activity.setNature(nature);
		activity.setTitle(produceRandomTitle(activity.getNature()));
		activity.setMail(produceRandomMail(activity.getTitle()));

		return activity;
	}*/

	private String produceRandomTitle(Nature nature) {
		// TODO Auto-generated method stub
		return null;
	}

	private String produceRandomDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	private String produceRandomMail(String title) {
		// TODO Auto-generated method stub
		return null;
	}

	private Date produceRandomBirthDate() {
		int year = getRandomNumberInRange(1991, 2001);
		int day = getRandomNumberInRange(1, 27);
		int month = getRandomNumberInRange(0, 11);

		return new Date(year - 1900, month, day);
	}

	private String produceRandomWebsite(String firstName, String lastName) {
		return "www.placeholder.com";
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
		try {
			return PasswordHash.createHash("placeholder");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/* min et max sont inclus. */
	public static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}
