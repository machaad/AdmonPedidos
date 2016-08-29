package models;

import be.objectify.deadbolt.core.models.Role;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import play.data.validation.Constraints;
import utils.json.View;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Pmendoza on 13/01/2016.
 */
@Entity
@Table(name = "s_role",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"name"})
)

public final class SecurityRole extends BaseEntity implements Role {
    @Id
    @Column
    @Constraints.Required
    @JsonView(View.Public.class)
    private long id;

    @Column
    @Constraints.Required
    @JsonView(View.Public.class)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role", targetEntity = SecurityPermission.class)
    @JsonView(View.RoleForm.class)
    private List<SecurityPermission> permissions;

    public SecurityRole() {
    }


    public List<SecurityPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SecurityPermission> permissions) {
        this.permissions = permissions;
    }

    public static final Finder<Long, SecurityRole> find = new Finder(Long.class,SecurityRole.class);


    @Override
    public String getName() {
        return String.valueOf(name);
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SecurityRole that = (SecurityRole) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(name, that.name)
                .append(permissions, that.permissions)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(permissions)
                .toHashCode();
    }
}

