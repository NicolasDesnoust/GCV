package gcv.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;

import gcv.beans.Person;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;

public class TestFillDB {
	
	@EJB(beanName = "executorBean")
	ExecutorBean executorBean;
	
	/* Modifier TASKS en fonction du nombre de processeurs disponibles. */
	private final int PERSONS_TO_CREATE = 100_000, TASKS = 6;

	public TestFillDB() throws Exception {
		// Nous injectons le test dans le container pour que
		// l'annotation @EJB soit traitée
		EJBContainer.createEJBContainer().getContext().bind("inject", this);
	}

	/*
	 * Génère un ensemble de personnes aléatoire et l'insère dans la base de
	 * données. L'ensemble est de taille PERSONS_TO_CREATE. En fonction de ce
	 * paramètre l'opération peut prendre un certain temps. Il est possible
	 * d'exporter ensuite le contenu de la base de données sous forme de requêtes
	 * d'insertion pour le faire plus rapidement. */
	@Test
	public void fillDataBaseRandomly() throws Exception {
		final long start = System.nanoTime();
		final ArrayList<Future<Integer>> results = new ArrayList<Future<Integer>>();

		System.err.println("Filling database asynchronously ...");
		
		int taskSize = (int) Math.round((double)PERSONS_TO_CREATE / TASKS);
		
		for (int i = 0; i < TASKS-1; i++)
			results.add(executorBean.generateAndPersist(taskSize));
		results.add(executorBean.generateAndPersist(PERSONS_TO_CREATE - taskSize * (TASKS-1)));
		
		for (int i = 0; i < TASKS; i++)
			results.get(i).get();

		final long end = System.nanoTime();
		final long duration = (end - start) / 1_000_000;
		System.err.println("Database filled in " + (double) duration / 1000 + " s (" + PERSONS_TO_CREATE + " new entities).");
	}

}
