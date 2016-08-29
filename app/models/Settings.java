package models;

import javax.persistence.*;

/**
 * Created by Interax on 11/02/2016.
 */
@Entity
public final class Settings extends BaseEntity{


    public static final String EMAIL_ALIAS = "mail.alias";
    public static final String EMAIL_MOCK = "mail.mock";
    @Id
    private String id;

    @Column
    private String value;

    @Column (name = "GROUP_TYPE")
    @Enumerated(EnumType.STRING)
    private Group group;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type = Type.STRING;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Add types whereever you need to classify the settings
     */
    public enum Group {
        EMAIL,
        GENERAL,
    }

    /**
     * Add types whereever you need to classify the settings
     */
    public enum Type {
        BOOLEAN,
        STRING,
        NUMBER
    }

    public static final Finder<String, Settings> find = new Finder(String.class,Settings.class);


}
