package gcv.business;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import gcv.beans.Activity;
import gcv.beans.Person;
import gcv.dao.Dao;
import gcv.util.PasswordHash;

/**EJB stateful qui représente un utilisateur (authentifié ou pas).
 * 
 * @author Nicolas DESNOUST
 * @author Serigne Bassirou Mbacké LY
 * 
 */
@Stateful(name = "user")
public class UserBean implements User {

	/** DAO utilisée pour déléguer l'accès aux données.
	 */
	@EJB(beanName = "jpadao")
	Dao dao;

	/** Adresse email (utilisée comme identifiant) de l'utilisateur connecté.
	 */
	private String login = "";
	
	/**Booléen indiquant si l'utilisateur est connecté ou non.
	 * 
	 */
	private boolean connected = false;

	/**Permet à un utilisateur de se connecter à son compte.
	 * Recherche l'utilisateur associé à l'adresse email spécifiée (login).
	 * S'il existe, compare son mot de passe à celui entré par la personne
	 * souhaitant se connecter.
	 * 
	 * @param login L'adresse email avec laquelle un utilisateur souhaite se connecter.
	 * @param pwd Le mot de passe non-haché fourni par l'utilisateur.
	 * @return true si l'authentification a réussi, false sinon.
	 */
	@Override
	public boolean login(String login, String pwd) {
		this.login = "";
		connected = false;

		Person person = dao.readPersonByMail(login);

		if (person != null) {
			try {
				if (PasswordHash.validatePassword(pwd, person.getPasswordHash())) {
					this.login = person.getMail();
					connected = true;
					System.out.printf("Login user %s on %s\n", login, this);
					return true;
				}
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	/**Permet à un utilisateur de se déconnecter de son compte.
	 */
	@Override
	@Remove
	public void logout() {
		this.login = "";
		connected = false;
		System.out.printf("Logout on %s\n", this);
	}
	
	/**Retourne l'identifiant de l'utilisateur connecté.
	 * Cette méthode est facultative et est principalement utilisée lors des tests. L'identifiant
	 * est ici une adresse email.
	 * 
	 * @return L'identifiant de l'utilisateur connecté.
	 */
	@Override
	public String getLogin() {
		return login;
	}

	/** Fait persister une activité dans une base de données. Vérifie que l'utilisateur a 
	 *  l'autorisation d'effectuer l'opération puis délègue
	 *  la tâche à la DAO.
	 *  
	 * @param person La personne propriétaire de l'activité.
	 * @param activity L'activité à faire persister.
	 * @return true si la persistence a réussi, false sinon.
	 */
	@Override
	public boolean createActivity(Person person, Activity activity) {
		/*
		 * Sécurité : nécessité d'authentification. Attention, il faut vérifier que
		 * l'utilisateur est connecté sur le compte qu'il essaie de modifier et non un
		 * autre.
		 */
		if (!connected || !login.equals(String.valueOf(activity.getOwner().getPersonID())))
			return false;

		person.addActivity(activity);

		if (dao.update(person) != null)
			return true;

		person.removeActivity(activity);
		return false;
	}

	/** Fait persister une personne dans une base de données. Vérifie que l'utilisateur a 
	 *  l'autorisation d'effectuer l'opération puis délègue la tâche à la DAO.
	 *  
	 * @param person La personne à faire persister.
	 * @return true si la persistence a réussi, false sinon.
	 */
	@Override
	public boolean createPerson(Person person) {
		if (!connected)
			return false;
		
		dao.create(person);
		
		return true;
	}

	/** Met à jour une activité dans une base de données. Vérifie que l'utilisateur a 
	 *  l'autorisation d'effectuer l'opération puis délègue la tâche à la DAO.
	 *  
	 * @param person La personne propriétaire de l'activité.
	 * @param activity L'activité à mettre à jour.
	 * @return L'activité résultante de la mise à jour.
	 */
	@Override
	public Activity updateActivity(Person person, Activity activity) {
		if (!connected || !login.equals(String.valueOf(person.getPersonID())))
			return null;
		
		return dao.update(activity);
	}

	/** Met à jour une personne dans une base de données. Vérifie que l'utilisateur a 
	 *  l'autorisation d'effectuer l'opération puis délègue la tâche à la DAO.
	 *  
	 * @param person La personne à mettre à jour.
	 * @return La personne résultante de la mise à jour.
	 */
	@Override
	public Person updatePerson(Person person) {
		if (!connected || !login.equals(String.valueOf(person.getPersonID())))
			return null;
		
		return dao.update(person);
	}

	/**Supprime une activité d'une base de données. Vérifie que l'utilisateur a 
	 * l'autorisation d'effectuer l'opération puis délègue la tâche à la DAO.
	 *  
	 * @param person La personne propriétaire de l'activité.
	 * @param activity L'activité à supprimer.
	 */
	@Override
	public void removeActivity(Person person, Activity activity) {
		if (!connected || !login.equals(String.valueOf(person.getPersonID())))
			return;
		
		dao.remove(Activity.class, activity);
	}

	/**Supprime une personne d'une base de données. Vérifie que l'utilisateur a 
	 * l'autorisation d'effectuer l'opération puis délègue la tâche à la DAO.
	 *  
	 * @param person La personne à supprimer.
	 */
	@Override
	public void removePerson(Person person) {
		if (!connected || !login.equals(String.valueOf(person.getPersonID())))
			return;
		
		dao.remove(Person.class, person);
	}
	
	/**Recherche un ensemble de personnes dans une base de données en fonction de leur prénom.
	 * Retourne toutes les personnes ayant un prénom similaire au paramètre.
	 * Délègue la tâche à la DAO.
	 * 
	 * @param input La chaîne de caractères à retrouver au sein du prénom.
	 * @return UNe collection de personnes.
	 */
	@Override
	public Collection<Person> findAllPersonsByFirstName(String input) {
		return dao.findByStringProperty(Person.class, "firstName", "%" + input + "%");
	}

	/**Recherche un ensemble de personnes dans une base de données en fonction de leur nom de famille.
	 * Retourne toutes les personnes ayant un nom similaire au paramètre.
	 * Délègue la tâche à la DAO.
	 * 
	 * @param input La chaîne de caractères à retrouver au sein du nom de famille.
	 * @return UNe collection de personnes.
	 */
	@Override
	public Collection<Person> findAllPersonsByLastName(String input) {
		return dao.findByStringProperty(Person.class, "lastName", "%" + input + "%");
	}

	/**Recherche un ensemble de personnes dans une base de données en fonction de leurs activités.
	 * Retourne toutes les personnes ayant au moins une activité avec un titre similaire au paramètre.
	 * Délègue la tâche à la DAO.
	 * 
	 * @param input La chaîne de caractères à retrouver au sein du titre d'une activité.
	 * @return UNe collection de personnes.
	 */
	@Override
	public Collection<Person> findAllPersonsByActivity(String input) {
		return dao.findAllPersonsWithActivityEntitled("%" + input + "%");
	}

	/** Recherche une personne dans la base de données. Délègue la tâche à la DAO.
	 * 
	 * @param person La personne à trouver dans la base de données.
	 * @return La personne trouvée ou null sinon.
	 */
	@Override
	public Person readPerson(Person person) {
		return dao.read(Person.class, person);
	}

	/**Recherche une activité dans la base de données. Délègue la tâche à la DAO.
	 * 
	 * @param activity L'activité à trouver dans la base de données.
	 * @return L'activité trouvée ou null sinon.
	 */
	@Override
	public Activity readActivity(Activity activity) {
		return dao.read(Activity.class, activity);
	}

	/**Retourne toutes les personnes présentes dans la base de données.
	 * Délègue la tâche à la DAO.
	 * 
	 * @return Une collection de personnes issues de la base de données.
	 */
	@Override
	public Collection<Person> readAllPersons() {
		return dao.readAll(Person.class);
	}

	/**Retourne toutes les activités présentes dans la base de données.
	 * Délègue la tâche à la DAO.
	 * 
	 * @return Une collection d'activités issues de la base de données.
	 */
	@Override
	public Collection<Activity> readAllActivities() {
		return dao.readAll(Activity.class);
	}
}