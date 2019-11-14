package gcv.test;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;

import gcv.beans.Person;
import gcv.dao.Dao;
import gcv.util.PasswordHash;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;

public class TestJSON {

	@EJB(beanName = "jpadao")
	Dao dao;

	@Inject
	@Named("fakePerson")
	Person person;

	@Inject
	FakePersonFactory fakePersonFactory;

	private final int PERSONS_TO_CREATE = 100_000, THREADS = 12;

	public TestJSON() throws Exception {
		// Nous injectons le test dans le container pour que
		// l'annotation @EJB soit traitée
		EJBContainer.createEJBContainer().getContext().bind("inject", this);

		/* Teste le bon fonctionnement des injections EJB / CDI. */
		assertNotNull(person);
		assertNotNull(dao);
		assertNotNull(fakePersonFactory);
	}

	/*
	 * Génère un ensemble de personnes aléatoire et l'insère dans la base de
	 * données. L'ensemble est de taille PERSONS_TO_CREATE. En fonction de ce
	 * paramètre l'opération peut prendre un certain temps. Il est possible
	 * d'exporter ensuite le contenu de la base de données sous forme de requêtes
	 * d'insertion pour le faire plus rapidement.
	 * 
	 */
	@Test
	public void fillDataBaseRandomly() throws Exception {
		Person temp;
		Generator[] tasks = new Generator[THREADS];
		Thread[] generators = new Thread[THREADS];
		final long debut = System.nanoTime();
		
		for (int i = 0; i < THREADS-1; i++)
			generators[i] = new Thread(tasks[i] = new Generator(PERSONS_TO_CREATE/THREADS));
		generators[THREADS-1] = new Thread(tasks[THREADS-1] = new Generator(PERSONS_TO_CREATE - PERSONS_TO_CREATE/THREADS * (THREADS-1)));
		
		System.err.println("Filling database ...");

		for (int i = 0; i < THREADS; i++)
			generators[i].start();
		for (int i = 0; i < THREADS; i++)
			generators[i].join();
		
	/*	final long fin = System.nanoTime();
		final long durée = (fin - debut) / 1_000_000;
		System.out.println("Duréejava = " + (double) durée / 1000 + " s.");*/
		
		//final long debut2 = System.nanoTime();
		
		/* Insertion avec batch processing */
		for (int i = 0; i < THREADS; i++)
			dao.createAll(tasks[i].persons);
		/*
		for (int i = 0; i < THREADS; i++)
			for (Person p : tasks[i].persons)
				dao.create(p);*/
		
		final long fin = System.nanoTime();
		final long durée = (fin - debut) / 1_000_000;
		System.out.println("Durée totale = " + (double) durée / 1000 + " s.");
		System.err.println("Database filled in.");

	}

}
