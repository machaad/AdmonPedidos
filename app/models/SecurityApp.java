package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Interax on 22/02/2016.
 */
@Entity
@Table(name = "s_app")
public final class SecurityApp extends BaseEntity {
    @Id
    private String licenseKey;
    @Column
    private String name;
    @Column
    private String description;

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static final Finder<String, SecurityApp> find = new Finder<String, SecurityApp>(String.class,SecurityApp.class);


}
