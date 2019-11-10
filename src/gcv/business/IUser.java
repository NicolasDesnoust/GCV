package gcv.business;

import javax.ejb.Local;

import gcv.beans.Activity;
import gcv.beans.Person;

@Local
/* Interface d'un EJB stateful qui représente un utilisateur (authentifié ou pas). */
public interface IUser {
	
	boolean login(String login, String pwd);
	void logout();
	String getLogin();

	/*********************************************************
	******   Opérations avec authentification requise	******
	*********************************************************/

	/* Créations (retournent TRUE si l'entité a bien été créée.
	 * FALSE sinon.*/
	boolean createActivity(Person person, Activity activity);
	boolean createPerson(Person person);

	/* Mises à jour */
	Activity updateActivity(Person person, Activity activity);
	Person updatePerson(Person person);

	/* Suppression */
	void removeActivity(Person person, Activity activity);
	void removePerson(Person person);
}