package gcv.validators;

import org.omnifaces.util.Faces;

import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("gcv.validators.EmailValidator")
public class EmailValidator implements Validator {

	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
		org.apache.commons.validator.routines.EmailValidator validator = org.apache.commons.validator.routines.EmailValidator
				.getInstance();
		String email, errorMessage;

		if (o == null) {
			return;
		}

		email = (String) o;

		boolean valid = validator.isValid(email);
		if (!valid) {

			ResourceBundle bundle = ResourceBundle.getBundle("gcv.resources.messages", Faces.getLocale());
			errorMessage = bundle.getString("emailValidator.invalid.email");

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null);
			throw new ValidatorException(msg);
		}
	}
}
