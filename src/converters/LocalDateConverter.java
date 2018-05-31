package converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Converter JSF perso String <=> LocalDate
 * 
 * @author nicolas.magniez
 *
 */
@FacesConverter("localDateConverter")
public class LocalDateConverter implements Converter{

	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	// Convertir une chaine en LocalDate
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String chaine) {
		System.out.println(String.format("Conversion demandée par : %s", arg1));

		LocalDate date = null;
		if (chaine != null) {
			// Je transforme chaine en LocalDate
			// methode parse( methode statique de la classe LocalDate
			date = LocalDate.parse(chaine, dtf);
		}
		return date;
	}

	// Convertir une LocalDate en chaine
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object objet) {
		System.out.println(String.format("Conversion en String demandée par : %s", arg1));

		String chaine = "";
		// Je caste objet en LocalDate ( instanceof)
		if (objet instanceof LocalDate) {
			LocalDate laDate = (LocalDate) objet;
			// Je transforme objet en String
			chaine = laDate.format(dtf);
		}
		System.out.println(chaine);
		return chaine;
	}

}
