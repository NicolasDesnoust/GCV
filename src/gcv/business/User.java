package gcv.business;

import java.util.Collection;

import javax.ejb.Local;

import gcv.beans.Activity;
import gcv.beans.Person;

/**Représente un utilisateur (authentifié ou pas).
 * 
 * @author Nicolas DESNOUST
 * @author Serigne Bassirou Mbacké LY
 * 
 */
public interface User {
	
	/**
	 * Retourne la valeur du booléen connected.
	 * @return vrai si l'utilisateur est connecté, faux sinon.
	 */
	public boolean isConnected();
	
	/**Permet à un utilisateur de se connecter à son compte.
	 * 
	 * @param login L'indentifiant avec lequel un utilisateur souhaite se connecter.
	 * @param pwd Le mot de passe non-haché fourni par l'utilisateur.
	 * @return true si l'authentification a réussi, false sinon.
	 */
	boolean login(String login, String pwd);
	
	/**Permet à un utilisateur de se déconnecter de son compte.
	 */
	void logout();
	
	/**Retourne l'identifiant de l'utilisateur connecté.
	 * Cette méthode est facultative et est principalement utilisée lors des tests. 
	 * 
	 * @return L'identifiant de l'utilisateur connecté.
	 */
	String getLogin();

	/*********************************************************
	******   Opérations avec authentification requise	******
	*********************************************************/

	/** Fait persister une activité dans une base de données. Peut déléguer
	 *  la tâche à la DAO.
	 *  
	 * @param person La personne propriétaire de l'activité.
	 * @param activity L'activité à faire persister.
	 * @return true si la persistence a réussi, false sinon.
	 */
	boolean createActivity(Person person, Activity activity);

	/** Fait persister une personne dans une base de données. Peut déléguer
	 *  la tâche à la DAO.
	 *  
	 * @param person La personne à faire persister.
	 * @return true si la persistence a réussi, false sinon.
	 */
	boolean createPerson(Person person);

	/** Met à jour une activité dans une base de données. Peut déléguer
	 *  la tâche à la DAO.
	 *  
	 * @param person La personne propriétaire de l'activité.
	 * @param activity L'activité à mettre à jour.
	 * @return L'activité résultante de la mise à jour.
	 */
	Activity updateActivity(Person person, Activity activity);
	
	/** Met à jour une personne dans une base de données. Peut déléguer
	 *  la tâche à la DAO.
	 *  
	 * @param person La personne à mettre à jour.
	 * @return La personne résultante de la mise à jour.
	 */
	Person updatePerson(Person person);
	
	/**Supprime une activité d'une base de données. Peut déléguer
	 *  la tâche à la DAO.
	 *  
	 * @param person La personne propriétaire de l'activité.
	 * @param activity L'activité à supprimer.
	 */
	void removeActivity(Person person, Activity activity);
	
	/**Supprime une personne d'une base de données. Peut déléguer
	 *  la tâche à la DAO.
	 *  
	 * @param person La personne à supprimer.
	 */
	void removePerson(Person person);
	
	/*********************************************************
	******   Opérations sans authentification requise	******
	*********************************************************/
	
	/**Recherche un ensemble de personnes dans une base de données en fonction de leur prénom.
	 * Retourne toutes les personnes ayant un prénom similaire au paramètre.
	 * 
	 * @param input La chaîne de caractères à retrouver au sein du prénom.
	 * @return UNe collection de personnes.
	 */
	Collection<Person> findAllPersonsByFirstName(String input);
	
	/**Recherche un ensemble de personnes dans une base de données en fonction de leur nom de famille.
	 * Retourne toutes les personnes ayant un nom similaire au paramètre.
	 * 
	 * @param input La chaîne de caractères à retrouver au sein du nom de famille.
	 * @return UNe collection de personnes.
	 */
	Collection<Person> findAllPersonsByLastName(String input);
	
	/**Recherche un ensemble de personnes dans une base de données en fonction de leurs activités.
	 * Retourne toutes les personnes ayant au moins une activité avec un titre similaire au paramètre.
	 * 
	 * @param input La chaîne de caractères à retrouver au sein du titre d'une activité.
	 * @return UNe collection de personnes.
	 */
	Collection<Person> findAllPersonsByActivity(String input);
	
	/** Recherche une personne dans la base de données.
	 * 
	 * @param person La personne à trouver dans la base de données.
	 * @return La personne trouvée ou null sinon.
	 */
	Person readPerson(Person person);
	
	/**Recherche une activité dans la base de données.
	 * 
	 * @param activity L'activité à trouver dans la base de données.
	 * @return L'activité trouvée ou null sinon.
	 */
	Activity readActivity(Activity activity);
	
	/**Retourne toutes les personnes présentes dans la base de données.
	 * 
	 * @return Une collection de personnes issues de la base de données.
	 */
	Collection<Person> readAllPersons();
	
	/**Retourne toutes les activités présentes dans la base de données.
	 * 
	 * @return Une collection d'activités issues de la base de données.
	 */
	Collection<Activity> readAllActivities();
}