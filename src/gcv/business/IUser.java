package gcv.business;

import java.util.Collection;

import javax.ejb.Local;

import gcv.beans.Activity;
import gcv.beans.Person;

@Local
public interface IUser {
	
	boolean login(String login, String pwd);
	void logout();
	String getLogin();

	/*********************************************************
	******   Opérations sans authentification requise	******
	*********************************************************/
	
	/* Lectures spécifiques */
	Collection<Person> findAllPersonsByFirstName(String input);
	Collection<Person> findAllPersonsByLastName(String input);
	Collection<Person> findAllPersonsByActivity(String input);
	
	/* Lectures standard */
	Person readPerson(Person person);
	Activity readActivity(Activity activity);
	Collection<Person> readAllPersons();
	Collection<Activity> readAllActivities();
	

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