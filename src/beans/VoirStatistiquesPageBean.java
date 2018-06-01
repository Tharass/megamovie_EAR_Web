package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.PieChartModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import model.entities.Film;
import model.entities.Genre;
import session.FacadeFilm;



@SuppressWarnings("serial")
@Named
@ViewScoped
@NoArgsConstructor
@Log
public class VoirStatistiquesPageBean implements Serializable{



	@Inject
	private FacadeFilm facadeFilm;

	@Getter @Setter
	private List<Film> listFilms = new ArrayList<>();
	
	@Getter @Setter
	private PieChartModel pieModel;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		createPieModel();
	}

	private void createPieModel() {
		pieModel = new PieChartModel();
        
		
		Map<Genre,Long> mapCompterFilm = facadeFilm.compterLesFilmsParGenre();
		
		for (Genre genre : mapCompterFilm.keySet()) {
			pieModel.set(genre.toString(), mapCompterFilm.get(genre));
		}
		
         
		pieModel.setTitle("Nombre de film par genre");
		pieModel.setLegendPosition("w");
    }

	
	
}
