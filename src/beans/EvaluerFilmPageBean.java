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
import javax.enterprise.inject.spi.CDI;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import model.entities.Evaluation;
import model.entities.Film;
import model.entities.Genre;
import model.entities.Realisateur;
import session.FacadeEvaluation;
import session.FacadeFilm;
import session.FacadeRealisateur;



@SuppressWarnings("serial")
@Named
@ViewScoped
@NoArgsConstructor
@Log
public class EvaluerFilmPageBean implements Serializable{



	@Inject
	private FacadeFilm facadeFilm;

	@Inject
	private FacadeRealisateur facadeRealisateur;

	@Inject
	private FacadeEvaluation facadeEvaluation;

	@Getter @Setter
	private List<Film> listFilms = new ArrayList<>();

	@Getter @Setter @Inject
	private Film film;

	@Getter @Setter
	private Map<UUID,Realisateur> listRealisateur = new HashMap<>();

	@Getter @Setter
	private Realisateur reaSelect;

	@Getter @Setter @Inject
	private Evaluation evaluation;

	@Getter @Setter
	private Genre genreSelect;

	@Getter @Setter
	private Map<UUID, byte[]> images = new HashMap();

	@Setter
	private StreamedContent productImage;

	@Getter @Setter 
	private Boolean afficherEvaluation;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
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

	public Realisateur getRealisateur(UUID id) {
		return listRealisateur.get(id);
	}


	/**
	 * Methode qui retourne une liste de film en fonction du REALISATEUR selectionn� dans la combobox
	 * Puis Ajoute dans une Map de UUID / Film les films selectionn�
	 */
	public void searchFilmByRealisateur() {
		listFilms = new ArrayList<>();
		if ( reaSelect != null) {
			listFilms.addAll(facadeFilm.listerLesFilmsParRealisateur(reaSelect));
		}
		images.clear();
		for (int i=0; i<listFilms.size(); i++ ){
			images.put(listFilms.get(i).getId(), listFilms.get(i).getJacket());}

	}


	/**
	 * Methode qui retourne une liste de film en fonction du GENRE selectionn� dans le radio button
	 */
	public void searchFilmByGenre() {
		listFilms = new ArrayList<>();
		if ( genreSelect != null) {
			listFilms.addAll(facadeFilm.listerLesFilmsParGenre(genreSelect));
		}
		images.clear();
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


	public void onRowSelect(SelectEvent event) {
		afficherEvaluation = true;
	}


	public void selectionnerFilm() {

		if ( film != null) {
			System.out.println(evaluation.getCommentaire());
			facadeEvaluation.ajouterEvaluation(evaluation);
		}
		evaluation = CDI.current().select(Evaluation.class).get();

	}


	/**
	 * Méthode qui ajouter une évaluation à un film
	 * puis reset le film et evaluation
	 */
	public void evaluer() {

		if ( evaluation != null && film != null) {

			film.ajouterEvaluation(evaluation);
			facadeFilm.modifierFilm(film);
			evaluation = CDI.current().select(Evaluation.class).get();
			film = CDI.current().select(Film.class).get();
		}
	}

}
