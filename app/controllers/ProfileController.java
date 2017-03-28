package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import models.*;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

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
    public Result GO_HOME = Results.redirect(
            routes.HomeController.list(0, "name", "asc", "")
    );

    /**
     * Handle default path requests, redirect to computers list
     */
    public Result index() {
        return GO_HOME;
    }

    /**
     * Display the paginated list of computers.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on computer names
     */
    public Result list(int page, String sortBy, String order, String filter) {
        return ok(
                views.html.list.render(
                        Computer.page(page, 10, sortBy, order, filter),
                        sortBy, order, filter
                )
        );
    }


    /**
     * Handle the 'edit form' submission
     *
     * @param userid Id of the user to edit
     */
    public Result edit(Long userid) throws PersistenceException {
        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();
        if(profileForm.hasErrors()) {
            return badRequest(views.html.profile.render(userid, profileForm));
        }

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

                savedProfile.id = userid;

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

                newProfile.id = userid;

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
     * Edit the 'profile form'.
     */
    public Result update(Long id) {
        Form<Profile> profileForm = formFactory.form(Profile.class);
        return ok(
                views.html.profile.render(id, profileForm)
        );
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
    public Result delete(Long id) {
        Profile deletedProfile = Profile.find.byId(id);
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
