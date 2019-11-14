package gcv.test;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import gcv.beans.Person;

public class Generator implements Runnable {

	FakePersonFactory fakePersonFactory;
	public Person[] persons;
	
	public Generator(int count) throws InterruptedException {
		persons = new Person[count];
		fakePersonFactory = new FakePersonFactory();
	}
	
	@Override
	public void run() {
		for (int i = 0; i < persons.length; i++)
			persons[i] = fakePersonFactory.produceFakePerson();
	}

}
