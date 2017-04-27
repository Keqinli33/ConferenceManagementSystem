package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import models.*;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import play.mvc.Http.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import play.libs.Json;

import java.lang.reflect.Array;
import java.util.*;
import javax.inject.Inject;
import org.apache.commons.mail.*;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;// in play 2.3
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class EmailTemplateController extends Controller {

    private FormFactory formFactory;

    @Inject
    public EmailTemplateController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result getEmailTemplate(String email_type, String chair_name){
        System.out.println("In get email template ");
        Form<EmailTemplate> EmailTemplateForm = formFactory.form(EmailTemplate.class);
        //find if chair member's email template is created,if not, create it
        EmailTemplate tmp = new EmailTemplate();
        if(!tmp.IfUserExist(chair_name))
        {
            System.out.println("===2In get email template create template");
            tmp.createChairTemplate(chair_name);
        }

        String newEmailTemplateData = EmailTemplate.getEmailTemplateByType(email_type, chair_name);
        String newEmailSubjectData = EmailTemplate.getEmailSubjectByType(email_type, chair_name);

        JsonNode json;

        if(newEmailTemplateData == null){
            json = Json.newObject().put("status", "unsuccessful");
        }
        else {
            json = Json.newObject()
                    .put("chair_name", chair_name)
                    .put("email_type", email_type)
                    .put("template",newEmailTemplateData)
                    .put("subject",newEmailSubjectData)
                    .put("status","successful");
        }

        return ok(json);
    }

    public Result updateEmailTemplate()
    {
        Form<EmailTemplate> EmailTemplateForm = formFactory.form(EmailTemplate.class).bindFromRequest();
        EmailTemplate new_EmailTemplate = EmailTemplateForm.get();

        Transaction txn = Ebean.beginTransaction();
        try {
            if(new_EmailTemplate.IfUserExist(new_EmailTemplate.chair_name)) {
                System.out.println("ready to update pcchair name "+new_EmailTemplate.chair_name+" template" + new_EmailTemplate.template);
                new_EmailTemplate.updateEmailTemplate(new_EmailTemplate);
                txn.commit();
            }
            else {
                System.out.println("ready to save pcchair name "+new_EmailTemplate.chair_name+" template" + new_EmailTemplate.template);
                new_EmailTemplate.createChairTemplate(new_EmailTemplate.chair_name);
                txn.commit();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            txn.end();
        }
        return ok("successfully");
    }

    /* find all reviewer who has unreviewed paper
     */
    public Result findReviewer()
    {
        List<User> all_users = User.find.all();
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode jsonarray = new ArrayNode(factory);
        for(int i = 0 ; i < all_users.size() ; ++i)
        {
            Long id = all_users.get(i).id;
            List<Paper> paperList = Paper.ReviewPapers(id);
            int assign_count=0;
            int reviewed_count=0;
            for(int j = 0 ; j < paperList.size() ; ++j)
            {
                if("assigned".equals(paperList.get(j).reviewstatus))
                    assign_count++;
//                else if("reviewed".equals(paperList.get(j).reviewstatus))
//                    reviewed_count++;
            }
            if(assign_count > 0)
            {
                Profile profile = Profile.find.byId(all_users.get(i).id);
                JsonNode json = Json.newObject()
                        .put("id", all_users.get(i).id)
                        .put("username", all_users.get(i).username)
                        .put("firstname", profile.firstname)
                        .put("lastname", profile.lastname)
                        .put("unreviewedcount", Integer.toString(assign_count-reviewed_count));

                jsonarray.add(json);
            }
        }
        JsonNode temp = (JsonNode) jsonarray;
        return ok(temp);
    }
}