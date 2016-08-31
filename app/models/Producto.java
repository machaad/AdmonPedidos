package models;

import com.fasterxml.jackson.annotation.JsonView;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import utils.json.View;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonView;


@Entity
public final  class Producto extends BaseEntity {


    @Id
    @JsonView(View.Public.class)
    private Long id;

    @Column(length = 256, nullable = false)
    @JsonView(View.Public.class)
    private String givenName;

    @Column(length = 256, nullable = false)
    @JsonView(View.Public.class)
    private String description;

    @Column(length = 256, nullable = false)
    @JsonView(View.Public.class)
    private String sku;

    public Producto(){

    }

    public Producto(String name,String desc, String sku){
      this.givenName=name;
      this.description=desc;
      this.sku=sku;
    }


    @SuppressWarnings("deprecation")
    public static final Finder<Long, Producto> find = new Finder<Long, Producto>(Long.class,Producto.class);


    public static Finder<Long, Producto> getFind() {
    		return find;
    	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

}
