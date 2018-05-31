package validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


@FacesValidator("NomValidator")
public class NomValidator implements Validator{

	


	
	public NomValidator(){
		
	}
	
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		
		if ( value.toString().equals("toto")) {
			System.out.println("toto decouvert ! ");
			
			FacesMessage msg = 
					new FacesMessage("TOTO decouvert !");
			
			
			throw new ValidatorException(msg);
		}

	}
	
}
