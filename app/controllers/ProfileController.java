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

/**
 * Created by sxh on 17/3/26.
 */
public class ProfileController extends Controller{

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
        Session session = Http.Context.current().session();
        Long userid = Long.parseLong(session.get("userid"));
        System.out.println("Enter profile page user id is "+userid.toString());
        Form<Profile> profileForm = formFactory.form(Profile.class);
        Profile profile = Profile.find.byId(userid);
        return ok(
                views.html.profile.render(profileForm, profile)
        );
    }


    /**
     * Handle the 'edit form' submission
     *
     */
    public Result edit() throws PersistenceException {
        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();
        if(profileForm.hasErrors()) {
            return badRequest(views.html.profile.render(profileForm, null));
        }

        Session session = Http.Context.current().session();
        Long userid = Long.parseLong(session.get("userid"));

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

        return GO_HOME;
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
    public Result delete() {
        Session session = Http.Context.current().session();
        Long userid = Long.parseLong(session.get("userid"));
        Profile deletedProfile = Profile.find.byId(userid);
        if(deletedProfile != null){
            deletedProfile.delete();
            flash("success", "Computer has been deleted");
        }
        else{
            flash("success", "You haven't created your profile yet");
        }
        return GO_HOME;
    }

}
