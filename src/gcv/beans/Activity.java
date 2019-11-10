package gcv.beans;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Activity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id()
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer ActivityID;
	
	@Basic(optional = false)
	@Column(name = "year", length = 4, nullable = false)
	private Integer year;
	
    @Enumerated(EnumType.STRING)
	private Nature nature;
	
	@Basic(optional = false)
	@Column(name = "title", length = 50,
	      nullable = false)
	private String title;
	
	@Basic(optional = true)
	@Column(name = "description", length = 255)
	private String description;
	
	// On peut récupérer le mail de la personne 
	@Basic(optional = true)
	@Column(name = "mail", length = 200)
	private String mail;
	
	/* Le propriétaire n'est pas optionnel. */
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "owner")
	private Person owner;

	public Activity() {
		super();
	}

	public Activity(Integer year, Nature nature, String title, String description, String mail) {
		super();
		this.year = year;
		this.nature = nature;
		this.title = title;
		this.description = description;
		this.mail = mail;
	}

	public Integer getActivityID() {
		return ActivityID;
	}

	public void setActivityID(Integer activityID) {
		ActivityID = activityID;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Nature getNature() {
		return nature;
	}

	public void setNature(Nature nature) {
		this.nature = nature;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "Activity [ActivityID = " + ActivityID + ", year = " + year + ", nature = " + nature + ", title = " + title
				+ ", description = " + description + ", mail = " + mail + "]";
	}
}
