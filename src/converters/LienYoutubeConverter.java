package converters;

import java.util.regex.Pattern;

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
@FacesConverter("lienYoutubeConverter")
public class LienYoutubeConverter implements Converter{


	// Convertir le lien en entréé pour un lien en sorti
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String chaine) {
		System.out.println(String.format("Conversion LIEN par : %s  de %s", arg1,chaine));

		Pattern p = Pattern.compile("https://www.youtube.com/watch?v=");

		if (chaine != null) {
			// séparation en sous-chaînes
			String[] items = p.split(chaine);

			if ( items.length >0 ) {
				return items[0];
			}
		}
		return "";
	}

	// Convertir un lien de sorti pour un lien en entrée
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object objet) {
		System.out.println(String.format("Conversion LIEN par : %s  de %s", arg1,(String)objet.toString()));

		String chaine = "https://www.youtube.com/v/";
		// Je caste objet en LocalDate ( instanceof)
		if (objet instanceof String) {
			chaine.concat(objet.toString());
			chaine.concat("&amp;hl=en&amp;fs=1&amp;");
		}
		System.out.println("lien youtuber genrer"+chaine);
		return chaine;
	}

}
