package beans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import model.entities.Evaluation;
import model.entities.Film;
import model.entities.Realisateur;
import session.FacadeFilm;
import session.FacadeRealisateur;



@SuppressWarnings("serial")
@Named
@SessionScoped
@NoArgsConstructor
@Log
public class VoirEvaluationsPageBean implements Serializable{

	
	@Inject
	private FacadeFilm facadeFilm;
	
	@Inject
	private FacadeRealisateur facadeRealisateur;

	@Getter @Setter
	private List<Film> listFilms = new ArrayList<>();

	@Getter @Setter
	private List<Evaluation> listEvaluation = new ArrayList<>();
	
	@Getter @Setter
	private Map<UUID, byte[]> images = new HashMap();
	
	@Setter
	private StreamedContent productImage;
	
	@Getter @Setter
	private Map<UUID,Realisateur> listRealisateur = new HashMap<>();

	@Getter @Setter
	private Realisateur reaSelect;

	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		listRealisateur = facadeRealisateur.mapperLesRealisateurs();
	}

	public StreamedContent getProductImage() throws IOException, SQLException {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		}else {
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
		System.out.println("appel search film by rea"+reaSelect.getNom());
		listFilms = new ArrayList<>();
		if ( reaSelect != null) {
			
			listFilms.addAll(facadeFilm.listerLesFilmsParRealisateur(reaSelect));
			log.info("liste de fillms debut"+listFilms.size());
		}
		for (int i=0; i<listFilms.size(); i++ ){
            images.put(listFilms.get(i).getId(), listFilms.get(i).getJacket());
        }
		
	}
}
