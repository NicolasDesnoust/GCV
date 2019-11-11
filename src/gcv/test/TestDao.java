package gcv.test;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import javax.inject.Named;

import org.junit.BeforeClass;
import org.junit.Test;

import gcv.beans.Activity;
import gcv.beans.Nature;
import gcv.beans.Person;
import gcv.dao.Dao;
//TODO: Utiliser DBUnit.

public class TestDao {
	
	@EJB(beanName="jpadao")
    Dao dao;
    
    private Person person;
    
    public TestDao() throws Exception {
        // Nous injectons le test dans le container pour que 
        // l'annotation @EJB soit traitée
        EJBContainer.createEJBContainer().getContext().bind("inject", this);
        assertNotNull(dao);
        
        person = new Person("nicolas", "DESNOUST", "desnoust.nicolas@gmail.com",
    			"www.nicolas-desnoust.com", new Date(), "pwd");
    }

    @Test
    public void testCreatePerson() throws Exception {
    	Person expected = dao.create(person);
		 
		assertNotNull (expected);
    }
    
    @Test
    public void testReadPerson() throws Exception {
    	int expected = dao.create(person).getPersonID();
		 
    	int value = dao.read(Person.class, expected).getPersonID();
		 
		assertEquals (expected, value);
    }
    
    @Test
    public void testUpdatePerson() throws Exception {
    	Person toUpdate = dao.create(person);
		
    	String expected = "nico"; 
    	toUpdate.setFirstName(expected);
    	String value = dao.update(toUpdate).getFirstName();
		 
		assertEquals (expected, value);
    }
    
    @Test
    public void testRemovePerson() throws Exception {
    	int toRemove = dao.create(person).getPersonID();
		
    	assertNotNull(dao.read(Person.class, toRemove));
		
    	dao.remove(Person.class, toRemove);
    	
    	assertNull(dao.read(Person.class, toRemove));
    }
    
    @Test
    public void testFindByStringProperty() throws Exception {
    	String toFind = "NOUS";
    	
    	dao.create(person);
    	Collection<Person> persons = 
    			dao.findByStringProperty(Person.class, "lastName", "%" + toFind + "%");
    	
    	assertTrue(persons.size() > 0);
    	for(Person p : persons) {
    		assertTrue(p.getLastName().contains(toFind));
    	}
    }
    
    @Test
    public void testFindAllPersonsWithActivityEntitled() throws Exception {
    	person.addActivity(new Activity(2019,
    			Nature.EDUCATION, "Master 2 ILD", "M2 ILD à SJ.", "mail@gmail.com"));
    	person.addActivity(new Activity(2018,
    			Nature.EDUCATION, "Master 1 ILD", "M1 ILD à SJ.", "mail@gmail.com"));
    	
    	Person result = dao.create(person);
    	
    	Collection<Person> persons = 
    			dao.findAllPersonsWithActivityEntitled("%ILD%");
    	
    	assertTrue(persons.size() > 0);
    	for(Person p : persons) {
    		boolean have = false;
    		for(Activity a : p.getCv()) {
    			if (a.getTitle().contains("ILD"))
    				have = true;
        	}
    		assertTrue(have);
    	}
    }
}