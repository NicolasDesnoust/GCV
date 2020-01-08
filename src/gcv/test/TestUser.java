package gcv.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import javax.inject.Named;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import gcv.beans.Activity;
import gcv.beans.Nature;
import gcv.beans.Person;
import gcv.business.User;
import gcv.dao.Dao;
import gcv.dao.JpaDao;

public class TestUser {
	
    Dao dao;
	
	@EJB(beanName="user")
    User user;
	
	Person person;
	Activity activity;
    
    public TestUser() throws Exception {
        // Nous injectons le test dans le container pour que 
        // l'annotation @EJB soit traitée
        EJBContainer.createEJBContainer().getContext().bind("inject", this);
        
        // La DAO est factice ici pour isoler les tests.
        dao = Mockito.mock(Dao.class);
        
        assertNotNull(dao);
        assertNotNull(user);
        
        person = new Person("nicolas", "DESNOUST", "desnoust.nicolas@gmail.com",
    			"www.nicolas-desnoust.com", new Date(), "pwd");
        
        activity = new Activity(2018, Nature.EDUCATION, 
        		"Master 1 ILD", "M1 ILD à SJ.", "mail@gmail.com");
        
        person.addActivity(activity);
    }
    //TODO: A TESTER
    /*
		boolean createActivity(Person person, Activity activity);
		boolean createPerson(Person person);
		Activity updateActivity(Person person, Activity activity);
		Person updatePerson(Person person);
		void removeActivity(Person person, Activity activity);
		void removePerson(Person person);
		Person readPerson(Person person);
		Activity readActivity(Activity activity);
		Collection<Person> readAllPersons();
		Collection<Activity> readAllActivities();
	*/
    
    @Test
    public void testFindAllPersonsByFirstName() {
    	Collection<Person> expected = new ArrayList<Person>(Arrays.asList(person));
    	String toFind = "cola";
 
    	// GIVEN
        Mockito.when(dao.findByStringProperty(Person.class, "firstName", "%" + toFind + "%"))
        	.thenReturn(expected);

        // WHEN 
        Collection<Person> result = user.findAllPersonsByFirstName(toFind);

        // THEN
        for (Person p : result)
        	assertTrue(p.getFirstName().toLowerCase().contains(toFind));
    }
    
    @Test
    public void testFindAllPersonsByLastName() {
    	Collection<Person> expected = new ArrayList<Person>(Arrays.asList(person));
    	String toFind = "SNOU";
 
    	// GIVEN
        Mockito.when(dao.findByStringProperty(Person.class, "lastName", "%" + toFind + "%"))
        	.thenReturn(expected);

        // WHEN 
        Collection<Person> result = user.findAllPersonsByLastName(toFind);

        // THEN
        for (Person p : result)
        	assertTrue(p.getLastName().toUpperCase().contains(toFind));
    }
    
    @Test
    public void testFindAllPersonsByActivity() {
    	Collection<Person> expected = new ArrayList<Person>(Arrays.asList(person));
    	String toFind = "ILD";
 
    	// GIVEN
        Mockito.when(dao.findAllPersonsWithActivityEntitled("%" + toFind + "%"))
        	.thenReturn(expected);

        // WHEN 
        Collection<Person> result = user.findAllPersonsByActivity(toFind);

        // THEN
        for (Person p : result) {
        	boolean have = false;
			for(Activity a : p.getCv()) {
				if (a.getTitle().toUpperCase().contains("ILD"))
					have = true;
	    	}
			assertTrue(have);
		}
    }
}