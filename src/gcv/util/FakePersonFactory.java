package gcv.util;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import gcv.beans.Person;

/**Producteur de fausses personnes géré par CDI.
 * Il est essentiellement utilisé pour peupler la base de données
 * et pendant les tests unitaires.
 * 
 * @author Nicolas DESNOUST
 * @author Serigne Bassirou Mbacké LY
 * 
 * @see gcv.util.JsonReader
 */
@Dependent
@Named("fakePersonFactory")
public class FakePersonFactory {

	/** L'objet jsonReader fournit des listes de noms, prénoms et domaines utiles
	 *  à la génération aléatoire.
	 */
	@Inject @Named("jsonReader")
	private JsonReader jsonReader;
	   
	/**
	 * Produit une fausse personne.
	 * 
	 * @return Une instance de la classe personne, construite aléatoirement.
	 */
	@Produces @Named("fakePerson")
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

	/**Produit une date d'anniversaire aléatoire entre 1991 et 2001.
	 * Les jours ne dépassent jamais 27 pour éviter les incohérences
	 * (mois qui ne finissent pas en 31 par exemple).
	 * 
	 * @return Une date d'anniversaire aléatoire.
	 */
	private Date produceRandomBirthDate() {
		int year = getRandomNumberInRange(1991, 2001);
		int day = getRandomNumberInRange(1, 27);
		int month = getRandomNumberInRange(0, 11);

		return new Date(year - 1900, month, day);
	}

	/**
	 * Produit un lien vers un site web personnel.
	 * 
	 * Le lien est contruit à partir d'un nom et d'un prénom passés en paramètres.
	 * 
	 * @param firstName Le prénom de la personne possédant le site web.
	 * @param lastName Le nom de famille de la personne possédant le site web.
	 * @return Un lien vers un site web personnel.
	 */
	private String produceRandomWebsite(String firstName, String lastName) {
		return "www." + firstName.replaceAll(" ", "_") + "-" + lastName.replaceAll(" ", "_") + ".com";
	}

	/**
	 * Produit une adresse mail aléatoire basée sur le nom et le prénom du propriétaire.
	 * 
	 * L'adresse mail est contruite à partir d'un nom et d'un prénom passés en paramètres.
	 * Le domaine est choisi aléatoirement parmi une liste de domaines utilisés en France.
	 * 
	 * @param firstName Le prénom de la personne possédant l'adresse mail.
	 * @param lastName Le nom de famille de la personne possédant l'adresse mail.
	 * @return Une adresse mail aléatoire basée sur le nom et le prénom du propriétaire.
	 */
	private String produceRandomMail(String firstName, String lastName) {

		int randomIndex = getRandomNumberInRange(0, jsonReader.getDomains().size() - 1);
		String domain = jsonReader.getDomains().get(randomIndex);

		return new String(firstName.replaceAll(" ", "_") + "." + lastName.replaceAll(" ", "_") + "@" + domain);
	}

	/**
	 * Produit un nom de famille aléatoire.
	 * 
	 * Le nom de famille est choisi aléatoirement parmi une liste de noms utilisés en France
	 * en 2018 selon l'INSEE.
	 * 
	 * @return Un nom de famille aléatoire.
	 */
	private String produceRandomLastName() {

		int randomIndex = getRandomNumberInRange(0, jsonReader.getLastNames().size() - 1);
		String choice = jsonReader.getLastNames().get(randomIndex);

		return choice.toLowerCase();
	}

	/**
	 * Produit un prénom aléatoire.
	 * 
	 * Le prénom est choisi aléatoirement parmi une liste de prénoms utilisés en France
	 * en 2018 selon l'INSEE.
	 * 
	 * @return Un prénom aléatoire.
	 */
	private String produceRandomFirstName() {

		int randomIndex = getRandomNumberInRange(0, jsonReader.getFirstNames().size() - 1);
		String choice = jsonReader.getFirstNames().get(randomIndex);

		return choice.toLowerCase();
	}

	/**
	 * Produit un mot de passe aléatoire sécurisé.
	 * 
	 * @param length La longueur du mot de passe à générer.
	 * @return Le mot de passe aléatoire généré.
	 */
	private String produceRandomPassword(int length) {
		/*try {
			return PasswordHash.createHash("placeholder");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		return "placeholder";
	}

	/**
	 * Tire un nombre entier aléatoirement entre deux bornes.
	 * Les bornes sont incluses dans le tirage au sort.
	 * 
	 * @param min La borne minimale incluse dans le tirage.
	 * @param max La borne maximale incluse dans le tirage.
	 * @return Le nombre entier tiré aléatoirement entre deux bornes.
	 */
	public static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		return ThreadLocalRandom.current().nextInt(min, max+1);
	}
}
