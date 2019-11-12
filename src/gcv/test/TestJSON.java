package gcv.test;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;

import gcv.beans.Person;
import gcv.dao.Dao;
import gcv.util.PasswordHash;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;

public class TestJSON {

	@EJB(beanName="jpadao")
	Dao dao;
	
	@Inject @Named("fakePerson")
	Person person;
	
	@Inject
	FakeFactory fakeFactory;
	
	private final int PERSONS_TO_CREATE = 1000;

	public TestJSON() throws Exception {
		// Nous injectons le test dans le container pour que
		// l'annotation @EJB soit traitée
		EJBContainer.createEJBContainer().getContext().bind("inject", this);
		
		/* Teste le bon fonctionnement des injections EJB / CDI. */
		assertNotNull(person);
		assertNotNull(dao);
		assertNotNull(fakeFactory);
	}

	@Test
	public void fillDataBase() throws Exception {
		Person temp;
		
		System.err.println("Filling database ...");
		
		for(int i = 0; i < PERSONS_TO_CREATE; i++) {
			temp = fakeFactory.produceFakePerson();
			dao.create(temp);
		}
		
		System.err.println("Database filled.");
		
	}

}
