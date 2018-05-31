package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import model.entities.Film;
import model.entities.Genre;
import model.entities.Personne;
import session.FacadeFilm;
import session.FacadePersonne;



@SuppressWarnings("serial")
@Named
@ViewScoped
@NoArgsConstructor
@Log
public class LoginPageBean implements Serializable{

	
	
	@Inject
	private FacadePersonne facade;

	@Getter @Setter @Inject
	private Personne personne;


	

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		
		
	}

	public String checkAuthorized(String login, String mdp){
		System.out.println("lgoing..."+facade.testerConnection(login, mdp));
		if ( facade.testerConnection(login, mdp)) {
			return "gestionFilms";
		}else {
			return "login";
		}
	}
	


}
