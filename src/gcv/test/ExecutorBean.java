package gcv.test;

import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import gcv.beans.Person;
import gcv.dao.Dao;
import gcv.util.FakePersonFactory;

@Stateless(name="executorBean")
public class ExecutorBean {
	
	@EJB(beanName = "jpadao")
	Dao dao;

	@Inject @Named("fakePersonFactory")
	FakePersonFactory fakePersonFactory;
 
	@Asynchronous
	public Future<Integer> generateAndPersist(int size) {
		Person[] persons = new Person[size];
		
		for (int i = 0; i < persons.length; i++)
			persons[i] = fakePersonFactory.produceFakePerson();

		dao.createAll(persons);

		return new AsyncResult<Integer>(size);
	}
}