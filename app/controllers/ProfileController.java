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
     * @param id Id of the computer to edit
     */
    public Result update(Long id) throws PersistenceException {
        Form<Computer> computerForm = formFactory.form(Computer.class).bindFromRequest();
        if(computerForm.hasErrors()) {
            return badRequest(views.html.editForm.render(id, computerForm));
        }

        Transaction txn = Ebean.beginTransaction();
        try {
            Computer savedComputer = Computer.find.byId(id);
            if (savedComputer != null) {
                Computer newComputerData = computerForm.get();
                savedComputer.company = newComputerData.company;
                savedComputer.discontinued = newComputerData.discontinued;
                savedComputer.introduced = newComputerData.introduced;
                savedComputer.name = newComputerData.name;

                savedComputer.update();
                flash("success", "Computer " + computerForm.get().name + " has been updated");
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
    public Result edit(Long id) {
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
        Profile.find.ref(id).delete();
        flash("success", "Computer has been deleted");
        return GO_HOME;
    }

}
