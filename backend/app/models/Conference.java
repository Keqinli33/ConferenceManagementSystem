package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import com.avaje.ebean.Model;

/**
 * Created by shuang on 3/28/17.
 */

@Entity
public class Conference extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public Long id;
    public String username;
//    @Constraints.Required
    public String title;//conference acronym
//    @Constraints.Required
    public String location;
    public String date;
    public String status;
    public String searchstatus;
    public String ifreviewer;
    public String ifadmin;
    public String keyword;


    public static Find<Long,Conference> find = new Find<Long,Conference>(){};

    public static List<Conference> GetMyConference(String username){
        List<Conference> results =
                find.where()
                        .eq("username",username)
                        .findList();
        return results;
    }

    public static void updateIfReviewer(String username, String conf, String ifreviewer)
    {
        List<Conference> results =
                find.where()
                        .and(Expr.eq("username", username), Expr.eq("title", conf))
                        .findList();
        if(results.size()>0) {
            Long new_id = results.get(0).id;

            Conference new_conf = Conference.find.byId(new_id);

            new_conf.ifreviewer = ifreviewer;

            new_conf.update();
        }
    }
}
