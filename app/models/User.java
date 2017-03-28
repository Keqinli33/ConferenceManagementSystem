package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;
import com.avaje.ebean.Expr;

/**
 * Created by Ling on 2017/3/27.
 */
@Entity
public class User extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public Long id;

    @Constraints.Required
    public String username;

    @Constraints.Required
    public String password;

    public String email;

    public String security_question_answer1;

    public String security_question_answer2;

    public String security_question_answer3;

    public static Find<Long,User> find = new Find<Long,User>(){};

    public static boolean VerifyUser(String username, String password) {
        List<User> results =
                find.where()
                        .and(Expr.eq("username", username), Expr.eq("password", password))
                        .findList();
        if(results.isEmpty()) return false;
        else return true;
    }
}
