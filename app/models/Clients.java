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
    private String address;

    @Column(length = 256, nullable = false)
    @JsonView(View.Public.class)
    private String email;

    public Clients(){

    }

    public Clients(String name,String lastName,String maidenName,
    String address,String email){
      this.name=name;
      this.lastName=lastName;
      this.maidenName=maidenName;
      this.address=address;
      this.email=email;
    }

    @SuppressWarnings("deprecation")
    public static final Finder<Long, Clients> find = new Finder<Long, Clients>(Long.class,Clients.class);


    public static Finder<Long, Clients> getFind() {
    		return find;
    	}

	/**
	* Returns value of id
	* @return
	*/
	public Long getId() {
		return id;
	}

	/**
	* Sets new value of id
	* @param
	*/
	public void setId(Long id) {
		this.id = id;
	}

	/**
	* Returns value of name
	* @return
	*/
	public String getName() {
		return name;
	}

	/**
	* Sets new value of name
	* @param
	*/
	public void setName(String name) {
		this.name = name;
	}

	/**
	* Returns value of lastName
	* @return
	*/
	public String getLastName() {
		return lastName;
	}

	/**
	* Sets new value of lastName
	* @param
	*/
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	* Returns value of maidenName
	* @return
	*/
	public String getMaidenName() {
		return maidenName;
	}

	/**
	* Sets new value of maidenName
	* @param
	*/
	public void setMaidenName(String maidenName) {
		this.maidenName = maidenName;
	}

	/**
	* Returns value of adress
	* @return
	*/
	public String getAddress() {
		return address;
	}

	/**
	* Sets new value of adress
	* @param
	*/
	public void setAddress(String adress) {
		this.address = address;
	}

	/**
	* Returns value of email
	* @return
	*/
	public String getEmail() {
		return email;
	}

	/**
	* Sets new value of email
	* @param
	*/
	public void setEmail(String email) {
		this.email = email;
	}

}
