package models;


import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonView;
import utils.json.View;

import javax.persistence.*;

/**
 * Created by Pmendoza on 13/01/2016.
 */
@Entity
@Table(name = "s_action")
public final class SecurityAction extends BaseEntity {
    public static final String UPDATE = "update";
    public static final String CREATE = "create";
    public static final String DELETE = "delete";

    @Id
    @JsonView(View.Public.class)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static final play.db.ebean.Model.Finder<String, SecurityAction> find = new Model.Finder<String, SecurityAction>(String.class,SecurityAction.class);
}
