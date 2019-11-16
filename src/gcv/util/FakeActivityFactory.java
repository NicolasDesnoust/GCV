package gcv.util;

import java.util.Random;

import javax.enterprise.context.Dependent;

import gcv.beans.Nature;

@Dependent
public class FakeActivityFactory {

	public FakeActivityFactory() {
	}

	/*
	 * Produit un CV. Procède en créant un nombre aléatoire d'activités pour chaque
	 * nature possible (sauf OTHER).
	 */
	/*
	 * private Set<Activity> produceFakeCV(Date birthDate) { Set<Activity> CV = new
	 * HashSet<Activity>(); int birthYear = birthDate.getYear();
	 */
	/*
	 * Année d'obtention du BAC générée aléatoirement à partir de la date de
	 * naissance.
	 */
	// int baccalaureateYear = birthYear + getRandomNumberInRange(16, 18);

	/*
	 * int workExpNbr = getRandomNumberInRange(1, 4), educationNbr =
	 * getRandomNumberInRange(3, 4), projectsNbr = getRandomNumberInRange(3, 5);
	 * 
	 * for (int i = 0; i < educationNbr; i++) produceFakeActivity(Nature.EDUCATION,
	 * birthYear + 17 + i);
	 * 
	 * for (int i = 0; i < projectsNbr; i++) produceFakeActivity(Nature.PROJECT,
	 * birthYear + 18 + getRandomNumberInRange(0, educationNbr));
	 * 
	 * for (int i = 0; i < workExpNbr; i++) produceFakeActivity(Nature.WORK_EXP,
	 * birthYear);
	 */

	/*
	 * Set<Activity> fakeEducation = produceFakeEducation(baccalaureateYear);
	 * CV.addAll(fakeEducation);
	 */
	/*
	 * On suppose que les projets sont réalisés entre l'année qui suit l'obtention
	 * du BAC et la durée du parcours effectué ensuite. Attention l'année du BAC
	 * fait partie de fakeEducation donc il faut la retirer.
	 */
	/*
	 * int durationEducationPostBac = -1;
	 * CV.addAll(produceFakeProjectsInRange(baccalaureateYear + 1, baccalaureateYear
	 * + 1 + durationEducationPostBac - 1));
	 */
	/* L'expérience professionnelle débute après les études. */
	/* CV.addAll(produceFakeWorkExp(baccalaureateYear + durationEducation + 1)); */

	/*
	 * return CV; }
	 */

	/*
	 * Produit une activité d'une certaine nature. L'identifiant est généré
	 * automatiquement dans la base de données et le propriétaire est renseigné lors
	 * de l'ajout au CV d'une personne via la méthode addActivity(Activity
	 * activity).
	 */
	/*
	 * private Activity produceFakeActivity(Nature nature, int year) { Activity
	 * activity = new Activity();
	 * 
	 * activity.setDescription(produceRandomDescription()); activity.setYear(year);
	 * activity.setNature(nature);
	 * activity.setTitle(produceRandomTitle(activity.getNature()));
	 * activity.setMail(produceRandomMail(activity.getTitle()));
	 * 
	 * return activity; }
	 */

	private String produceRandomTitle(Nature nature) {
		// TODO Auto-generated method stub
		return null;
	}

	private String produceRandomDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	private String produceRandomMail(String title) {
		// TODO Auto-generated method stub
		return null;
	}

	/* min et max sont inclus. */
	public static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}
