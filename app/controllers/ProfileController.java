package controllers;

import models.Profile;
//import org.hibernate.validator.constraints.Email;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;

import javax.inject.Inject;
import models.User;
import play.mvc.Result;
import play.mvc.Results;

import java.util.*;
import play.data.validation.ValidationError;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

import javax.ws.rs.core.MediaType;
import play.mvc.Http.Session;
import play.mvc.Http;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

import java.util.Random;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import models.*;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import play.mvc.Http.Session;

import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import play.mvc.*;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

/**
 * Created by sxh on 17/3/26.
 */
public class ProfileController extends Controller{
    @Inject WSClient ws;

    private FormFactory formFactory;

    @Inject
    public ProfileController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /**
     * This result directly redirect to application home.
     */
    /*public Result GO_HOME = Results.redirect(
            routes.HomeController.list(0, "name", "asc", "")
    );*/
    public Result GO_HOME = Results.redirect(
            routes.ShowPaperController.showMyPaper()
    );

    public Result enterProfile(){
//        Session session = Http.Context.current().session();
//        Long userid = Long.parseLong(session.get("userid"));
//        System.out.println("Enter profile page user id is "+userid.toString());
//        Profile profile = Profile.find.byId(userid);
        Form<Profile> profileForm = formFactory.form(Profile.class);

        return ok(
                views.html.profile.render(profileForm, null)
        );
    }


    /**
     * Handle the 'edit form' submission
     *
     */
    public CompletionStage<Result> edit() throws PersistenceException {
        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();


        Session session = Http.Context.current().session();
        Long userid = Long.parseLong(session.get("userid"));

        Profile newProfileData = profileForm.get();

        JsonNode json = Json.newObject()
                .put("title", newProfileData.title)
                .put("research", newProfileData.research)
                .put("firstname",newProfileData.firstname)
                .put("lastname", newProfileData.lastname)
                .put("position", newProfileData.position)
                .put("affiliation", newProfileData.affiliation)
                .put("email", newProfileData.email)
                .put("phone", newProfileData.phone)
                .put("fax", newProfileData.fax)
                .put("address",newProfileData.address)
                .put("city", newProfileData.city)
                .put("country", newProfileData.country)
                .put("region", newProfileData.region)
                .put("zipcode", newProfileData.zipcode)
                .put("comment", newProfileData.comment)
                .put("userid", userid);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/profile/edit").post(json);
        return res.thenApply(response -> {
            String ret = response.getBody();
            System.out.println("here is "+ret);
            if(profileForm.hasErrors()) {
                return ok("failure!");
            }
            else if ("insert successfully".equals(ret)) {
                return ok(ret);
            }
            else if ("update successfully".equals(ret)) {
                return ok(ret);
            }
            else{
                return ok("failure!");
            }
        });

        /*
        Transaction txn = Ebean.beginTransaction();
        try {
            Profile savedProfile = Profile.find.byId(userid);
            Profile newProfileData = profileForm.get();

            if (savedProfile != null) {
                savedProfile.title = newProfileData.title;
                savedProfile.research = newProfileData.research;
                savedProfile.firstname = newProfileData.firstname;
                savedProfile.lastname = newProfileData.lastname;
                savedProfile.position = newProfileData.position;
                savedProfile.affiliation = newProfileData.affiliation;
                savedProfile.email = newProfileData.email;
                savedProfile.phone = newProfileData.phone;
                savedProfile.fax = newProfileData.fax;
                savedProfile.address = newProfileData.address;
                savedProfile.city = newProfileData.city;
                savedProfile.country = newProfileData.country;
                savedProfile.region = newProfileData.region;
                savedProfile.zipcode = newProfileData.zipcode;
                savedProfile.comment = newProfileData.comment;

                savedProfile.userid = userid;

                savedProfile.update();
                flash("success", "Profile " + userid + " has been updated");
                txn.commit();
            }
            else{
                Profile newProfile = new Profile();
                newProfile.title = newProfileData.title;
                newProfile.research = newProfileData.research;
                newProfile.firstname = newProfileData.firstname;
                newProfile.lastname = newProfileData.lastname;
                newProfile.position = newProfileData.position;
                newProfile.affiliation = newProfileData.affiliation;
                newProfile.email = newProfileData.email;
                newProfile.phone = newProfileData.phone;
                newProfile.fax = newProfileData.fax;
                newProfile.address = newProfileData.address;
                newProfile.city = newProfileData.city;
                newProfile.country = newProfileData.country;
                newProfile.region = newProfileData.region;
                newProfile.zipcode = newProfileData.zipcode;
                newProfile.comment = newProfileData.comment;

                newProfile.userid = userid;

                newProfile.insert();
                flash("success", "Profile " + userid + " has been inserted");
                txn.commit();
            }
        } finally {
            txn.end();
        }

*/
    }

    /**
     * Handle the 'new profile form' submission
     */
    public Result save() {
        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();
        profileForm.get().save();
        flash("success", "Profile " + profileForm.get().title + profileForm.get().lastname + " has been created");
        return GO_HOME;
    }

    /**
     * Handle profile deletion
     */
    public CompletionStage<Result> delete() {
        Session session = Http.Context.current().session();
        Long userid = Long.parseLong(session.get("userid"));

        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();
        Profile newProfileData = profileForm.get();

        JsonNode json = Json.newObject()
                .put("title", newProfileData.title)
                .put("research", newProfileData.research)
                .put("firstname",newProfileData.firstname)
                .put("lastname", newProfileData.lastname)
                .put("position", newProfileData.position)
                .put("affiliation", newProfileData.affiliation)
                .put("email", newProfileData.email)
                .put("phone", newProfileData.phone)
                .put("fax", newProfileData.fax)
                .put("address",newProfileData.address)
                .put("city", newProfileData.city)
                .put("country", newProfileData.country)
                .put("region", newProfileData.region)
                .put("zipcode", newProfileData.zipcode)
                .put("comment", newProfileData.comment)
                .put("userid", userid);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/profile/delete").post(json);
        return res.thenApply(response -> {
            String ret = response.getBody();
            System.out.println("here is "+ret);
            return ok(ret);
        });
    }

}
