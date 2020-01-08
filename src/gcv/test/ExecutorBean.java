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

/**
 * Bean Stateless géré par EJB. 
 * 
 * Il est utilisé comme un fournisseur de tâches lors du peuplage de la base de données
 * pour tester l'ergonomie de l'application.
 * 
 * @author Nicolas DESNOUST
 * @author Serigne Bassirou Mbacké LY
 * 
 * @see gcv.test.TestFillDB
 */
@Stateless(name="executorBean")
public class ExecutorBean {
	
	/** DAO utilisée pour faire persister les lots de personnes 
	 *  générées aléatoirement.
	 */
	@EJB(beanName = "jpadao")
	Dao dao;

	/** Usine de création de fausses personnes.
	 */
	@Inject @Named("fakePersonFactory")
	FakePersonFactory fakePersonFactory;
 
	
	/** 
	 * Tâche asynchrone qui génère un groupe de personnes et le fait persister dans la base de données.
	 * Les personnes sont générées par l'usine et sauvegardées dans la base de données par la DAO via 
	 * du batch processing.
	 * 
	 * @param size La taille du groupe de personnes.
	 * @return Un Future permettant d'attendre la fin de l'exécution de la tâche.
	 */
	@Asynchronous
	public Future<Integer> generateAndPersist(int size) {
		Person[] persons = new Person[size];
		
		for (int i = 0; i < persons.length; i++)
			persons[i] = fakePersonFactory.produceFakePerson();

		dao.createAll(persons);

		return new AsyncResult<Integer>(size);
	}
}