package models;

import com.fasterxml.jackson.annotation.JsonView;
import utils.json.View;

import javax.persistence.*;
import java.util.List;

/**
 *Created by Pmendoza on 13/01/2016.
 */
@Entity
@Table(name = "s_permission",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"role_id","module_id"})
)

public final class SecurityPermission extends BaseEntity {
    @Id
    @JsonView(View.RoleForm.class)
    private long id;

    @ManyToOne
    private SecurityRole role;

    @ManyToOne
    @JsonView(View.RoleForm.class)
    private SecurityModule module;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonView(View.RoleForm.class)
    private List<SecurityAction> excludedActions;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SecurityRole getRole() {
        return role;
    }

    public void setRole(SecurityRole role) {
        this.role = role;
    }

    public SecurityModule getModule() {
        return module;
    }

    public void setModule(SecurityModule module) {
        this.module = module;
    }

    public List<SecurityAction> getExcludedActions() {
        return excludedActions;
    }

    public void setExcludedActions(List<SecurityAction> excludedActions) {
        this.excludedActions = excludedActions;
    }

    public static final Finder<Long, SecurityPermission> find = new Finder(Long.class,SecurityPermission.class);

}
