package gcv.util;

import java.util.Comparator;

import gcv.beans.Activity;

public class SortActivityByYearDesc implements Comparator<Activity> {

	public int compare(Activity a, Activity b) {

		return b.getYear() - a.getYear();

	}
}
