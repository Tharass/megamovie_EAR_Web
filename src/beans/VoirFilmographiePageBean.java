package beans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.view.ViewScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import model.entities.Film;
import model.entities.Realisateur;
import session.FacadeFilm;
import session.FacadeRealisateur;



@SuppressWarnings("serial")
@Named
@SessionScoped
@NoArgsConstructor
@Log
public class VoirFilmographiePageBean implements Serializable{



	@Inject
	private FacadeFilm facadeFilm;

	@Inject
	private FacadeRealisateur facadeRealisateur;

	@Getter @Setter
	private List<Film> listFilms = new ArrayList<>();

	@Getter @Setter
	private Map<UUID,Realisateur> listRealisateur = new HashMap<>();

	@Getter @Setter
	private Realisateur reaSelect;

	@Getter @Setter
	private Map<UUID, byte[]> images = new HashMap();
	
	@Setter
	private StreamedContent productImage;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		listRealisateur = facadeRealisateur.mapperLesRealisateurs();
	}

	public boolean afficherTableau() {
		if ( listFilms == null || listFilms.isEmpty()) {
			return false;
		}
		return true;
	}
	
	public Realisateur getRealisateur(UUID id) {
		return listRealisateur.get(id);
	}
	
	
	public void searchFilmByRealisateur() {
		listFilms = new ArrayList<>();
		if ( reaSelect != null) {
			listFilms.addAll(facadeFilm.listerLesFilmsParRealisateur(reaSelect));
		}
		for (int i=0; i<listFilms.size(); i++ ){
            images.put(listFilms.get(i).getId(), listFilms.get(i).getJacket());}
		
	}


	public StreamedContent getProductImage() throws IOException, SQLException {

		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		}

		else {
			for (UUID id : images.keySet()) {
				System.out.println("map id"+id);
			}
			System.out.println("map"+images.size());
			UUID id = UUID.fromString(context.getExternalContext().getRequestParameterMap().get("pid"));
			
			System.out.println("id film "+id);
			byte[] image =  images.get(id);
			System.out.println("bite img "+images.get(id));
			return new DefaultStreamedContent(new ByteArrayInputStream(image));

		}
	}
	
}
