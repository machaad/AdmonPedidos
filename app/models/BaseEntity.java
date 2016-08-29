package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import usecase.authentication.GetCurrentUser;
import utils.EntityUtils;

import javax.annotation.PreDestroy;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by Pmendoza on 23/06/2015.
 */
@MappedSuperclass
public class BaseEntity extends Model {

    public BaseEntity(){

    }

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "datetime")
    private Date created;
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "datetime")
    private Date updated;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("updatedBy", updatedBy)
                .append("createdBy", createdBy)
                .append("created", created)
                .append("updated", updated)
                .toString();
    }

    @PrePersist
    public void prePersist(){
        created = new Date();
        updated = new Date();
        if(createdBy == null) {
            User user = GetCurrentUser.execute();
            updatedBy = user;
            createdBy = user;
        }else{
            updatedBy = createdBy;
        }
    }

    @PreUpdate
    public void preUpdate(){
        updated = new Date();
        updatedBy = GetCurrentUser.execute();
    }

    @PreRemove
    @PreDestroy
    public void nullifyOnDelete(){
        EntityUtils.startNullifying(this);
    }
}
