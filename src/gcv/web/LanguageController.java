package gcv.web;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name = "languageController")
@SessionScoped
public class LanguageController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String localeCode;

	private static Map<String, Object> countries;

	static {
		countries = new LinkedHashMap<String, Object>();
		countries.put("Anglais", Locale.ENGLISH);
		countries.put("Français", Locale.FRENCH);
	}

	@PostConstruct
	public void init() {
		localeCode = FacesContext.getCurrentInstance().getViewRoot().getLocale().toString();
	}

	public Map<String, Object> getCountriesInMap() {
		return countries;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public void changeCountryLocaleCode() {

		String odlLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale().toString();

		if (!odlLocale.equals(localeCode)) {

			// loop country map to compare the locale code
			for (Map.Entry<String, Object> entry : countries.entrySet()) {

				if (entry.getValue().toString().equals(localeCode)) {
					FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
				}
			}
		}
	}

}
