package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import com.avaje.ebean.Model;
import com.avaje.ebean.Expr;

/**
 * Created by shuang on 3/28/17.
 */

@Entity
public class Conference extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public long id;
    public String username;
//    @Constraints.Required
    public String title;
//    @Constraints.Required
    public String location;
    public String date;
    public String status;
    public String ifreviewer;
    public String ifadmin;
//    public String ifpaper;  //


    public static Find<Long,Conference> find = new Find<Long,Conference>(){};

    public static List<Conference> GetMyConference(String username){
        List<Conference> results =
                find.where()
                        .eq("username",username)
                        .findList();
        return results;
    }

    public static List<Conference> SearchMyConference(String username, String keysearch){
        List<Conference> results =
                find.where().
                        and(Expr.like("title", "%"+keysearch+"%"),Expr.eq("username", username))
                        .findList();
        return results;
    }
}
