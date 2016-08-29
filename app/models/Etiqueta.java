package models;

import javax.persistence.Id;

import utils.json.View;

import com.avaje.ebean.Model.Finder;
import com.fasterxml.jackson.annotation.JsonView;

public class Etiqueta {
	

    @Id
    @JsonView(View.Public.class)
    private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	 @SuppressWarnings("deprecation")
	public static final Finder<Long, Etiqueta> find = new Finder<Long, Etiqueta>(Long.class,Etiqueta.class);

}
