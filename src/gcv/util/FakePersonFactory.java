package gcv.util;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.embeddable.EJBContainer;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;

import gcv.beans.Person;

@Dependent
@Named("fakePersonFactory")
public class FakePersonFactory {

	@Inject @Named("jsonReader")
	private JsonReader jsonReader;
	   
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

		int randomIndex = getRandomNumberInRange(0, jsonReader.getDomains().size() - 1);
		String domain = jsonReader.getDomains().get(randomIndex);

		return new String(firstName.replaceAll(" ", "_") + "." + lastName.replaceAll(" ", "_") + "@" + domain);
	}

	private String produceRandomLastName() {

		int randomIndex = getRandomNumberInRange(0, jsonReader.getLastNames().size() - 1);
		String choice = jsonReader.getLastNames().get(randomIndex);

		return choice.toLowerCase();
	}

	private String produceRandomFirstName() {

		int randomIndex = getRandomNumberInRange(0, jsonReader.getFirstNames().size() - 1);
		String choice = jsonReader.getFirstNames().get(randomIndex);

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
