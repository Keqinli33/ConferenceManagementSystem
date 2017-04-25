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

public class EmailTemplateController extends Controller {

    private FormFactory formFactory;

    @Inject
    public EmailTemplateController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result getEmailTemplate(String username){
        System.out.println("In get email template ");
        Form<EmailTemplate> EmailTemplateForm = formFactory.form(EmailTemplate.class);
        String newEmailTemplateData = EmailTemplate.getEmailTemplateByUsername(username);

        JsonNode json;

        if(newEmailTemplateData == null){
            json = Json.newObject().put("status", "unsuccessful");
        }
        else {
            json = Json.newObject()
                    .put("pcchair_name", username)
                    .put("template",newEmailTemplateData)
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
            if(new_EmailTemplate.IfUserExist(new_EmailTemplate.pcchair_name)) {
                System.out.println("ready to update pcchair name "+new_EmailTemplate.pcchair_name+" template" + new_EmailTemplate.template);
                EmailTemplate email_template = new_EmailTemplate.updateByUsername(new_EmailTemplate.pcchair_name, new_EmailTemplate.template);
                //email_template.pcchair_name = new_EmailTemplate.pcchair_name;
                //email_template.template = new_EmailTemplate.template;
                //email_template.template = "updated";
                //System.out.println("fk id "+email_template.id);
                //System.out.println()
                //email_template.update();
                txn.commit();
            }
            else {
                System.out.println("ready to save pcchair name "+new_EmailTemplate.pcchair_name+" template" + new_EmailTemplate.template);
                new_EmailTemplate.save();
                txn.commit();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            txn.end();
        }
        return ok("successfully");
    }
}