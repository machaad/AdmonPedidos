package models;

import com.fasterxml.jackson.annotation.JsonView;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import utils.json.View;

import javax.persistence.*;
import java.util.List;

/**
 * Created by JoseLuis on 01/03/2016.
 */
@Entity
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Public.class)
    private Long id;

    @Constraints.Required
    @Column(length = 256, nullable = false)
    @JsonView(View.Public.class)
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



//    public Channel getChannel(){
//        return channel;
//    }

//    public void setChannel(Channel channel){
//        this.channel = channel;
//    }

    public static final Model.Finder<Long, Product> find = new Model.Finder<Long, Product>(Long.class, Product.class);

    public static List<Product> findAll(){
        return find.all();
    }

    public static Product findbyId(Long id){
        return find.where().eq("id",id).findUnique();
    }

}
