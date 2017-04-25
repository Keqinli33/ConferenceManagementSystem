package models;
import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import com.avaje.ebean.Model;

@Entity
public class EmailTemplate extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public Long id;

    public String pcchair_name;

    public String template;

    public static Find<Long,EmailTemplate> find = new Find<Long,EmailTemplate>(){};

    public static String getEmailTemplateByUsername(String username)
    {
        List<EmailTemplate> results =
                find.where()
                        .eq("pcchair_name", username)
                        .findList();
        if(results.size()==0)return "";
        else
        return results.get(0).template;
    }

    public static boolean IfUserExist(String username)
    {
        List<EmailTemplate> results =
                find.where()
                        .eq("pcchair_name", username)
                        .findList();
        if(results.size() == 0)
            return false;
        else
            return true;
    }

    public static EmailTemplate updateByUsername(String username, String template)
    {
        List<EmailTemplate> results =
                find.where()
                        .eq("pcchair_name", username)
                        .findList();
        Long id =  results.get(0).id;
        EmailTemplate new_email = EmailTemplate.find.byId(id);
        new_email.template = template;
        new_email.update();
        return new_email;
    }
}