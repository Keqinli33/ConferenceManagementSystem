package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import com.avaje.ebean.Model;

@Entity
public class Criteria extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public long id;
    public String label;
    public String explanations;
    public String weight;

    public static Finder<Long,Criteria> find = new Finder<Long,Criteria>(Criteria.class);

    public static List<Criteria> GetMyCriteria(Long criteriaId){
        List<Criteria> results =
                find.where()
                        .eq("id",criteriaId)
                        .findList();
        return results;
    }

}