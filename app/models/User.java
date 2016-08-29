package models;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Subject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import play.data.validation.Constraints;
import usecase.authentication.GetDeadboltPermissions;
import usecase.authentication.GetSha512;
import utils.json.View;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


/**
 *Created by Pmendoza on 13/01/2016.
 */
@Entity
public final class User extends BaseEntity implements Subject{

    @Id
    @JsonView(View.Public.class)
    private Long id;

    @Column(length = 256)
    @Constraints.Required
    @JsonView(View.Public.class)
    private String email;


    @Column(length = 256, nullable = false)
    @JsonView(View.Public.class)
    private String givenName;

    @Column(length = 256, nullable = false)
    @JsonView(View.Public.class)
    private String familyName;

    @ManyToMany
    @JsonView(View.UserForm.class)
    private List<SecurityRole> roles = new ArrayList<SecurityRole>();

    @Column(length = 64, nullable = false)
    @JsonIgnore
    private byte[] shaPassword;

    @Transient
    @JsonIgnore
    private String password;

    public User() {

    }

    public User(String email, String password, String name, String lasName) {
        setEmail(email);
        setPassword(password);
        this.givenName = name;
        this.familyName = lasName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        shaPassword = GetSha512.execute(password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoles(List<SecurityRole> roles) {
        this.roles = roles;
    }

    public List<? extends SecurityRole> getRoles() {
        return roles;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    //-----------------------------------------------------------------------------
    @JsonIgnore
    public List<? extends Permission> getPermissions() {
        List<Permission> permissions = new ArrayList<Permission>();
        for(SecurityRole role : roles){
            for(SecurityPermission permission : role.getPermissions()){
                permissions.addAll(GetDeadboltPermissions.execute(permission));
            }
        }
        return permissions;
    }

    @Override
    @JsonIgnore
    public String getIdentifier() {
        return String.valueOf(id);
    }

    public static final Finder<Long, User> find = new Finder<Long, User>(Long.class,User.class);


}
