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
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.view.ViewScoped;
import javax.enterprise.context.RequestScoped;
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
@RequestScoped
@NoArgsConstructor
@Log
public class ConsulterTopPageBean implements Serializable{



	@Inject
	private FacadeFilm facadeFilm;


	@Getter @Setter
	private List<Film> listFilms = new ArrayList<>();


	@Getter @Setter
	private Map<UUID, byte[]> images = new HashMap();


	@Setter
	private StreamedContent productImage;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		listFilms = facadeFilm.listerLesFilms();
		for (int i=0; i<listFilms.size(); i++ ){
			images.put(listFilms.get(i).getId(), listFilms.get(i).getJacket());}
	}



	public StreamedContent getProductImage()  {
		System.out.println("appel methode getimage");
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			System.out.println("Re,nder response");
			return new DefaultStreamedContent();
		}

		else {
			for (UUID id : images.keySet()) {
				System.out.println("map id"+id);
			}
			System.out.println("map"+images.size());
			try {
				UUID id = UUID.fromString(context.getExternalContext().getRequestParameterMap().get("pid"));

				System.out.println("id film "+id);
				byte[] image =  images.get(id);
				System.out.println("image byte  "+image);
				System.out.println("bite img "+new DefaultStreamedContent(new ByteArrayInputStream(image), "image/png"));
				return new DefaultStreamedContent(new ByteArrayInputStream(image), "image/png");

			}catch (Exception e) {
				System.out.println("exception "+e);
			}
			return null;


		}
	}

	//	public StreamedContent getImage(Util ut) throws IOException {
	//	       //Util is the pojo
	//
	//	          String image_id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("image_id");
	//	          System.out.println("image_id: " + image_id);
	//
	//	          if (image_id == null) {
	//
	//	                defaultImage=new DefaultStreamedContent(FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/Capture.PNG"), "image/png");
	//
	//	            return defaultImage; 
	//	        }
	//
	//
	//	         image= new DefaultStreamedContent(new ByteArrayInputStream(images.get(Integer.valueOf(image_id))), "image/png");
	//
	//	        return image;
	//	    }

}
