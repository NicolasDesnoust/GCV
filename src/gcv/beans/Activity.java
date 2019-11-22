package gcv.beans;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**Bean représentant une activité d'un CV. Il est donc propre à une personne.
 * Ce dernier peut ëtre persisté dans une base de données.
 * 
 * @author Nicolas DESNOUST
 * @author Serigne Bassirou Mbacké LY
 * 
 * @see gcv.beans.Person
 */
@Entity
public class Activity implements Serializable {
	
	/**Facilite la gestion des versions de cette classe (en terme de données sérialisées).
	 * A incrémenter pour une nouvelle version si jamais il est nécessaire de stocker des
	 * données sérialisées qui doivent survivent à plusieurs versions du code.
	 */
	private static final long serialVersionUID = 1L;
	
	/**Identifiant unique d'une activité. Equivalent de la clé primaire au niveau base de données.
	 * Il est généré automatiquement.
	 */
	@Id()
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer ActivityID;
	
	/** L'année de l'activité.
	 * Elle est de longueur 4 et oligatoire.
	 */
	@Basic(optional = false)
	@Column(name = "year", length = 4, nullable = false)
	private Integer year;
	
	
    /** La nature de l'activité.
     * 
     * @see gcv.beans.Nature
     */
    @Enumerated(EnumType.STRING)
	private Nature nature;
	
	/** Le titre de l'activité. 
	 * Il est obligatoire.
	 */
	@Basic(optional = false)
	@Column(name = "title", length = 50,
	      nullable = false)
	private String title;
	
	/** La description de l'activité.
	 * Elle est facultative.
	 */
	@Basic(optional = true)
	@Column(name = "description", length = 255)
	private String description;
	
	 
	/** L'adresse web de l'activité.
	 *  Elle est facultative.
	 */
	@Basic(optional = true)
	@Column(name = "web_address", length = 200)
	private String webAddress;
	
	/** Le propriétaire de l'activité. Il est chargé avec elle depuis la base de données
	 *  et n'est pas facultatif.
	 */
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "owner")
	private Person owner;

	
	/** Construit une activité vide.
	 */
	public Activity() {
		super();
	}
	
	/** Construit entièrement une activité.
	 *  Le propriétaire est ajouté par le setter d'ajout d'activité de la classe Person. 
	 *  L'identifiant est ajouté automatiquement.
	 *   
	 * @param year L'année de l'activité.
	 * @param nature La nature de l'activité.
	 * @param title Le titre de l'activité.
	 * @param description La description de l'activité (facultative).
	 * @param webAddress L'adresse web de l'activité (facultative).
	 * 
	 * @see gcv.beans.Person#addActivity(Activity)
	 */
	public Activity(Integer year, Nature nature, String title, String description, String webAddress) {
		super();
		this.year = year;
		this.nature = nature;
		this.title = title;
		this.description = description;
		this.webAddress = webAddress;
	}

	/** Retourne l'identifiant de l'activité.
	 * @return L'identifiant de l'activité.
	 */
	public Integer getActivityID() {
		return ActivityID;
	}

	/** Redéfini l'identifiant de l'activité. Ce setter est présent pour respecter les
	 * conventions sur les beans entité. Il n'est pas nécessaire puisque l'identifiant
	 * est attribué automatiquement.
	 * @param activityID Le nouvel identifiant de l'activité.
	 */
	public void setActivityID(Integer activityID) {
		ActivityID = activityID;
	}

	/** Retourne l'année de l'activité.
	 * @return L'année de l'activité.
	 */
	public Integer getYear() {
		return year;
	}

	/** Redéfini l'année de l'activité.
	 * @param year La nouvelle année de l'activité.
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/** Retourne la nature de l'activité.
	 * @return La nature de l'activité.
	 */
	public Nature getNature() {
		return nature;
	}

	/** Redéfini la nature de l'activité.
	 * @param nature La nouvelle nature de l'activité.
	 */
	public void setNature(Nature nature) {
		this.nature = nature;
	}

	/** Retourne le titre de l'activité.
	 * @return Le titre de l'activité.
	 */
	public String getTitle() {
		return title;
	}

	/** Redéfini le titre de l'activité.
	 * @param title Le nouveau titre de l'activité.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/** Retourne la description de l'activité.
	 * @return La description de l'activité.
	 */
	public String getDescription() {
		return description;
	}

	/** Redéfini la description de l'activité.
	 * @param description La nouvelle description de l'activité.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/** Retourne l'adresse web de l'activité.
	 * @return L'adresse web de l'activité.
	 */
	public String getWebAddress() {
		return webAddress;
	}

	/** Redéfini l'adresse web de l'activité.
	 * @param webAddress La nouvelle adresse web de l'activité.
	 */
	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	/** Retourne le propriétaire de l'activité.
	 * @return Le propriétaire de l'activité.
	 */
	public Person getOwner() {
		return owner;
	}

	/** Redéfini le propriétaire de l'activité. Ce setter est présent pour respecter les
	 * conventions sur les beans entité. Il n'est pas nécessaire puisque qu'une activité ne
	 * peut pas changer de CV ou de propriétaire.
	 * 
	 * @param owner Le nouveau propriétaire de l'activité.
	 */
	public void setOwner(Person owner) {
		this.owner = owner;
	}

	
	/** Redéfinition de la méthode toString() de la classe Object pour afficher une activité
	 *  de façon personnalisée.
	 *  
	 *  @see Object#toString()
	 */
	@Override
	public String toString() {
		return "Activity [ActivityID = " + ActivityID + ", year = " + year + ", nature = " + nature + ", title = " + title
				+ ", description = " + description + ", webAddress = " + webAddress + "]";
	}
}
