package models;

import com.fasterxml.jackson.annotation.JsonView;
import utils.json.View;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by Pmendoza on 13/01/2016.
 */
@Entity
@Table(name = "s_module")
public final class SecurityModule extends BaseEntity{


    @Id
    @JsonView(View.RoleForm.class)
    private String id;

    @ManyToMany
    @JsonView(View.RoleForm.class)
    private List<SecurityAction> actions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SecurityAction> getActions() {
        return actions;
    }

    public void setActions(List<SecurityAction> actions) {
        this.actions = actions;
    }

    public static final Finder<String, SecurityModule> find = new Finder<String, SecurityModule>(String.class,SecurityModule.class);
}
