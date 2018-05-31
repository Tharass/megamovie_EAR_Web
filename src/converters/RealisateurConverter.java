package converters;
import java.util.UUID;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import beans.EvaluerFilmPageBean;
import beans.GestionFilmsPageBean;
import model.entities.Realisateur;
 
@FacesConverter("realisateurConverter")
public class RealisateurConverter implements Converter{

	
	@Override
    public Object getAsObject(FacesContext ctx, UIComponent uiComponent, String reaId) {
        ValueExpression vex =
                ctx.getApplication().getExpressionFactory()
                        .createValueExpression(ctx.getELContext(),
                                "#{evaluerFilmPageBean}", EvaluerFilmPageBean.class);

        EvaluerFilmPageBean eva = (EvaluerFilmPageBean)vex.getValue(ctx.getELContext());
        return eva.getRealisateur(UUID.fromString(reaId));
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object rea) {
        return ((Realisateur)rea).getId().toString();
    }
}