package gcv.web;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import java.io.IOException;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Cookie;
import org.omnifaces.util.Faces;

@Named("languageController")
@SessionScoped
public class LanguageController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@Cookie
	private String localeCode = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale().toString();

	private static Map<String, Object> countries;

	static {
		countries = new LinkedHashMap<String, Object>();
		countries.put("Anglais", Locale.ENGLISH);
		countries.put("Français", Locale.FRENCH);
	}

	@PostConstruct
	public void init() {
		if (localeCode == null) {
			localeCode = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale().toString();
			
		} else {
			System.out.println("localeCode is initialized based on cookie");
		}
	}

	public Map<String, Object> getCountriesInMap() {
		return countries;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
		System.out.println("locale code set to " + this.localeCode);
	}

	public String getLanguage() {
		// countries.get(localeCode);
		// return countries.get(localeCode).getLanguage();
		return null;
	}

	public void changeCountryLocaleCode() throws IOException {

		String odlLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale().toString();
		System.out.println("changing locale code... " + localeCode + " " + odlLocale);

		if (!localeCode.equals(odlLocale)) {
			// loop country map to compare the locale code
			for (Map.Entry<String, Object> entry : countries.entrySet()) {
				System.out.println(entry.getValue().toString() + " " + localeCode);
				if (entry.getValue().toString().equals(localeCode)) {
					FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
					Faces.addResponseCookie("localeCode", localeCode, 2_592_000);
				}
			}
		}

	}

}
