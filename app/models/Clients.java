package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import utils.json.View;

import com.fasterxml.jackson.annotation.JsonView;


@Entity
public final  class Clients extends BaseEntity {


    @Id
    @JsonView(View.Public.class)
    private Long id;

    @Column(length = 256, nullable = false)
    @JsonView(View.Public.class)
    private String name;

    @Column(length = 256, nullable = false)
    @JsonView(View.Public.class)
    private String lastName;

    @Column(length = 256, nullable = false)
    @JsonView(View.Public.class)
    private String maidenName;

    @Column(length = 256, nullable = false)
    @JsonView(View.Public.class)
    private String adress;

    @Column(length = 256, nullable = false)
    @JsonView(View.Public.class)
    private String email;

    @SuppressWarnings("deprecation")
    public static final Finder<Long, Clients> find = new Finder<Long, Clients>(Long.class,Clients.class);


}
