package models;

import javax.persistence.*;

/**
 * Created by Interax on 22/02/2016.
 */
@Entity
@Table(name ="s_token")
public final class SecurityToken extends BaseEntity{

    @Id
    private long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private SecurityApp app;

    @Column
    private String token;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SecurityApp getApp() {
        return app;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setApp(SecurityApp app) {
        this.app = app;
    }
    public static final Finder<Long, SecurityToken> find = new Finder<Long, SecurityToken>(Long.class,SecurityToken.class);




}
