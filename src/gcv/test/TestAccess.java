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
import gcv.business.IAccess;
import gcv.business.IUser;
import gcv.dao.ISpecificDao;
import gcv.dao.SpecificDao;

public class TestAccess {
	
    ISpecificDao dao;
	
	@EJB(beanName="access")
    IAccess access;
	
	Person person;
	Activity activity;
    
    public TestAccess() throws Exception {
        // Nous injectons le test dans le container pour que 
        // l'annotation @EJB soit traitée
        EJBContainer.createEJBContainer().getContext().bind("inject", this);
        
        // La DAO est factice ici pour isoler les tests.
        dao = Mockito.mock(ISpecificDao.class);
        
        assertNotNull(dao);
        assertNotNull(access);
        
        person = new Person("nicolas", "DESNOUST", "desnoust.nicolas@gmail.com",
    			"www.nicolas-desnoust.com", new Date(), "pwd");
        
        activity = new Activity(2018, Nature.EDUCATION, 
        		"Master 1 ILD", "M1 ILD à SJ.", "mail@gmail.com");
        
        person.addActivity(activity);
    }
    //TODO: A TESTER
    /*
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
        Collection<Person> result = access.findAllPersonsByFirstName(toFind);

        // THEN
        for (Person p : result)
        	assertTrue(p.getFirstName().contains(toFind));
    }
    
    @Test
    public void testFindAllPersonsByLastName() {
    	Collection<Person> expected = new ArrayList<Person>(Arrays.asList(person));
    	String toFind = "SNOU";
 
    	// GIVEN
        Mockito.when(dao.findByStringProperty(Person.class, "lastName", "%" + toFind + "%"))
        	.thenReturn(expected);

        // WHEN 
        Collection<Person> result = access.findAllPersonsByLastName(toFind);

        // THEN
        for (Person p : result)
        	assertTrue(p.getLastName().contains(toFind));
    }
    
    @Test
    public void testFindAllPersonsByActivity() {
    	Collection<Person> expected = new ArrayList<Person>(Arrays.asList(person));
    	String toFind = "ILD";
 
    	// GIVEN
        Mockito.when(dao.findAllPersonsWithActivityEntitled("%" + toFind + "%"))
        	.thenReturn(expected);

        // WHEN 
        Collection<Person> result = access.findAllPersonsByActivity(toFind);

        // THEN
        for (Person p : result) {
        	boolean have = false;
			for(Activity a : p.getCv()) {
				if (a.getTitle().contains("ILD"))
					have = true;
	    	}
			assertTrue(have);
		}
    }
}