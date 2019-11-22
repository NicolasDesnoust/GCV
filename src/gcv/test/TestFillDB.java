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

/**
 * Classe de Test pour le remplissage de la base de données en masse.
 * 
 * Permet d'ajouter beaucoup de personnes générées aléatoirement dans la base de données afin d'évaluer 
 * les performances de l'application.
 * 
 * @author Nicolas DESNOUST
 * @author Serigne Bassirou Mbacké LY
 * 
 * @see gcv.test.ExecutorBean
 */
public class TestFillDB {
	
	/** Fournit les tâches asynchrones exécutant la génération et la persistence de
	 * groupes de personnes.
	 */
	@EJB(beanName = "executorBean")
	ExecutorBean executorBean;
	
	/**Constante indiquant combien de personnes au total doivent être rajoutées dans 
	 * la base de données.
	 */
	private final int PERSONS_TO_CREATE = 100_000;
	
	/** Constante indiquant en combien de tâches la génération des personnes va se répartir.
	 *  Elle doit coïncider avec le nombre de processeurs disponibles.
	 */
	private final int TASKS = 3;

	/**
	 * Constructeur du test. 
	 * Le test est injecté dans le container pour que l'annotation d'EJB soit traitée.
	 * 
	 * @throws Exception
	 */
	public TestFillDB() throws Exception {
		EJBContainer.createEJBContainer().getContext().bind("inject", this);
	}

	/**
	 * Génère un ensemble de personnes aléatoires et l'insère dans la base de
	 * données. 
	 * L'ensemble est de taille PERSONS_TO_CREATE. En fonction de ce
	 * paramètre l'opération peut prendre un certain temps. 
	 * La tâche est divisée en sous-tâches pour accélérer le processus. 
	 * Il est possible d'exporter ensuite le contenu de la base de données sous forme de requêtes
	 * d'insertion pour le faire plus rapidement par la suite. 
	 *
	 * @throws Exception
	 */
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
