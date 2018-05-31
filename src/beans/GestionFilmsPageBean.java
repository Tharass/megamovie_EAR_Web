package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.spi.CDI;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import model.entities.Film;
import model.entities.Genre;
import model.entities.Realisateur;
import net.entetrs.commons.jsf.JsfUtils;
import session.FacadeFilm;
import session.FacadeRealisateur;

@SuppressWarnings("serial")
@Named
@ViewScoped
@NoArgsConstructor
@Log
public class GestionFilmsPageBean implements Serializable{



	@Inject
	private FacadeFilm facadeFilm;

	@Inject
	private FacadeRealisateur facadeRealisateur;

	@Getter @Setter @Inject
	private Film film;

	@Getter @Setter
	private List<Film> listFilms = new ArrayList<>();

	@Getter @Setter
	private Map<UUID,Realisateur> listRealisateur = new HashMap<>();

	@Getter @Setter
	private Realisateur reaSelect;



	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		listFilms = facadeFilm.listerLesFilms();
		listRealisateur = facadeRealisateur.mapperLesRealisateurs();
	}

	public Genre[] getListGenres(){
		return Genre.values();
	}

	public boolean afficherTableau() {
		if ( listFilms == null || listFilms.isEmpty()) {
			return false;
		}
		return true;
	}

	public void ajouterFilm() {
		System.out.println("bite :"+reaSelect);

		film.setRealisateur(reaSelect);

		facadeFilm.ajouterFilm(film);
		listFilms.add(film);
		film = CDI.current().select(Film.class).get();
	}

	public void deleteFilm(Film film) {
		facadeFilm.supprimerFilm(film);
		listFilms.remove(film);
	}

	public void modifierFilm(Film film) {
		facadeFilm.modifierFilm(film);
		listFilms.remove(film);
		listFilms.add(film);
	}

	// Ajax du tableau
	public void onRowEdit(RowEditEvent event) {
		modifierFilm((Film) event.getObject());
	}


	public void onRowCancel(RowEditEvent event) {
		//	        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Car) event.getObject()).getId());
		//	        FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void handleFileUpload(FileUploadEvent event) {
		System.out.println(">>>>>>>>Taille de l'image : " + event.getFile().getContents().length);
		film.setJacket(event.getFile().getContents());
		JsfUtils.sendGrowlMessage("Fichier uploadé: %s", event.getFile().getFileName());
	}

	public Realisateur getRealisateur(UUID id) {
		return listRealisateur.get(id);
	}

}
