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

@Stateful(name = "user")
/* EJB stateful qui représente un utilisateur (authentifié ou pas). */
public class UserBean implements User {

	@EJB(beanName = "jpadao")
	Dao dao;

	private String login = "";
	private boolean connected = false;

	// TODO: Revoir le type de login (Integer ou String ?)
	@Override
	public boolean login(String login, String pwd) {
		this.login = "";
		connected = false;

		Person person = dao.read(Person.class, Integer.parseInt(login));

		if (person != null) {
			try {
				if (PasswordHash.validatePassword(pwd, person.getPasswordHash())) {
					this.login = String.valueOf(person.getPersonID());
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

	@Override
	@Remove
	public void logout() {
		this.login = "";
		connected = false;
		System.out.printf("Logout on %s\n", this);
	}

	@Override
	public String getLogin() {
		return login;
	}

	/* Une activité ne peut être créée indépendamment d'une personne. */
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

	@Override
	public boolean createPerson(Person person) {
		if (!connected)
			return false;
		
		if (dao.create(person) == null)
			return false;
		
		return true;
	}

	@Override
	public Activity updateActivity(Person person, Activity activity) {
		if (!connected || !login.equals(String.valueOf(person.getPersonID())))
			return null;
		
		return dao.update(activity);
	}

	@Override
	public Person updatePerson(Person person) {
		if (!connected || !login.equals(String.valueOf(person.getPersonID())))
			return null;
		
		return dao.update(person);
	}

	@Override
	public void removeActivity(Person person, Activity activity) {
		if (!connected || !login.equals(String.valueOf(person.getPersonID())))
			return;
		
		dao.remove(Activity.class, activity);
	}

	@Override
	public void removePerson(Person person) {
		if (!connected || !login.equals(String.valueOf(person.getPersonID())))
			return;
		
		dao.remove(Person.class, person);
	}
	
	@Override
	public Collection<Person> findAllPersonsByFirstName(String input) {
		return dao.findByStringProperty(Person.class, "firstName", "%" + input + "%");
	}

	@Override
	public Collection<Person> findAllPersonsByLastName(String input) {
		return dao.findByStringProperty(Person.class, "lastName", "%" + input + "%");
	}

	@Override
	public Collection<Person> findAllPersonsByActivity(String input) {
		return dao.findAllPersonsWithActivityEntitled("%" + input + "%");
	}

	@Override
	public Person readPerson(Person person) {
		return dao.read(Person.class, person);
	}

	@Override
	public Activity readActivity(Activity activity) {
		return dao.read(Activity.class, activity);
	}

	@Override
	public Collection<Person> readAllPersons() {
		return dao.readAll(Person.class);
	}

	@Override
	public Collection<Activity> readAllActivities() {
		return dao.readAll(Activity.class);
	}
}