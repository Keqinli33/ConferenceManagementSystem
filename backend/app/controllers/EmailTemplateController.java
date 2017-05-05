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

    public Result getEmailTemplate(String email_type, String chair_name, String conf_url){
        System.out.println("In get email template ");
        Form<EmailTemplate> EmailTemplateForm = formFactory.form(EmailTemplate.class);
        String conf = conf_url.replace("%20"," ");
        //find if chair member's email template is created,if not, create it
        EmailTemplate tmp = new EmailTemplate();
        if(!tmp.IfExist(chair_name, conf))
        {
            System.out.println("===2In get email template create template");
            tmp.createChairTemplate(chair_name, conf);
        }

        String newEmailTemplateData = EmailTemplate.getEmailTemplateByType(email_type, chair_name, conf);
        String newEmailSubjectData = EmailTemplate.getEmailSubjectByType(email_type, chair_name, conf);

        JsonNode json;

        if(newEmailTemplateData == null){
            json = Json.newObject().put("status", "unsuccessful");
        }
        else {
            json = Json.newObject()
                    .put("chair_name", chair_name)
                    .put("conference", conf)
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
            if(new_EmailTemplate.IfExist(new_EmailTemplate.chair_name, new_EmailTemplate.conference)) {
                System.out.println("ready to update pcchair name "+new_EmailTemplate.chair_name+" template" + new_EmailTemplate.template);
                new_EmailTemplate.updateEmailTemplate(new_EmailTemplate);
                txn.commit();
            }
            else {
                System.out.println("ready to save pcchair name "+new_EmailTemplate.chair_name+" template" + new_EmailTemplate.template);
                new_EmailTemplate.createChairTemplate(new_EmailTemplate.chair_name, new_EmailTemplate.conference);
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

    public Result SendReviewerReminder()
    {
        Form<EmailTemplate> EmailTemplateForm = formFactory.form(EmailTemplate.class).bindFromRequest();
        EmailTemplate new_EmailTemplate = EmailTemplateForm.get();

        String chair_name = new_EmailTemplate.chair_name;
        String conference = new_EmailTemplate.conference;

        String template = new_EmailTemplate.getEmailTemplateByType("REVIEWER_REMINDER", chair_name, conference);
        String subject = new_EmailTemplate.getEmailSubjectByType("REVIEWER_REMINDER", chair_name, conference);

        String template_email = template.replace("\\n","\n");

        PCmember members = new PCmember();
        List<PCmember> reviewers = members.GetAllReviewer(conference);
        User util_user = new User();

        for(int i = 0 ; i < reviewers.size() ; ++i)
        {
            PCmember reviewer = reviewers.get(i);
            String username = util_user.GetUsernameByEmail(reviewer.email);
            if(util_user.IfUserExist(username))
            //if user exist, send email
            {
                //get unreviewed paper count of this username and conference

            }
        }
    }

    private static void SendEmail(String emailto, String subject, String template){
        try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("socandrew2017@gmail.com", "ling0915"));
            email.setSSLOnConnect(true);
            email.setFrom("socandrew2017@gmail.com");
            email.setSubject(subject);
            email.setMsg(template);
            email.addTo(emailto);
            email.send();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}