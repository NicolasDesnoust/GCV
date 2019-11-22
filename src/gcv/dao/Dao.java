package gcv.dao;

import java.util.Collection;

import javax.ejb.Local;

import gcv.beans.Person;

/**Interface regroupant les prototypes des fonctions non-génériques de la couche DAO.
 * 
 * @author Nicolas DESNOUST
 * @author Serigne Bassirou Mbacké LY
 * 
 * @see gcv.dao.GenericDao
 */
@Local
public interface Dao extends GenericDao {
	/**
	 * Récupère une collection de personnes à partir du titre d'une de leurs
	 * activités. Cette méthode recherche dans la base de données toutes les
	 * activités ayant un titre similaire à celui passé en paramètre et retourne les
	 * personnes propriétaires de ces activités. La collection ne peut contenir deux
	 * fois la mëme personne.
	 * 
	 * @param title Le titre complet ou partiel d'activités à trouver.
	 * @return La collection des personnes correspondant à la recherche.
	 */
	public Collection<Person> findAllPersonsWithActivityEntitled(String title);

	/**
	 * Recherche d'une personne dans une base de données via son email.
	 * 
	 * @param mail L'email unique de la personne à rechercher.
	 * @return L'entité trouvée ou null.
	 */
	public Person readPersonByMail(String mail);

}
