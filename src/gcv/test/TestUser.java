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
import gcv.business.IUser;
import gcv.dao.ISpecificDao;
import gcv.dao.SpecificDao;

public class TestUser {
	
    ISpecificDao dao;
	
	@EJB(beanName="user")
    IUser user;
	
	Person person;
	Activity activity;
    
    public TestUser() throws Exception {
        // Nous injectons le test dans le container pour que 
        // l'annotation @EJB soit traitée
        EJBContainer.createEJBContainer().getContext().bind("inject", this);
        
        // La DAO est factice ici pour isoler les tests.
        dao = Mockito.mock(ISpecificDao.class);
        
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
	*/
}