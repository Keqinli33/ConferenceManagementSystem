package controllers;

import play.data.format.Formats;
import play.mvc.Controller;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import models.*;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Date;

/**
 * Created by shuang on 3/29/17.
 */
public class PaperController extends Controller {
    private FormFactory formFactory;

    @Inject
    public PaperController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result GO_HOME = Results.redirect(
            routes.HomeController.list(0, "name", "asc", "")
    );
    /**
     * Handle default path requests, redirect to computers list
     */
    public Result index() {
        return GO_HOME;
    }

    public Result edit(Long id) {
        Form<Paper> paperForm = formFactory.form(Paper.class).fill(
                Paper.find.byId(id)
        );
        return ok(
                views.html.editPaper.render(id, paperForm)
        );
    }
    public Result update(Long id) throws PersistenceException {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        if(paperForm.hasErrors()) {
            return badRequest(views.html.editPaper.render(id, paperForm));
        }

        Transaction txn = Ebean.beginTransaction();
        try {
            Paper savedPaper = Paper.find.byId(id);
            if (savedPaper != null) {
                Paper newPaperData = paperForm.get();
                savedPaper.title = newPaperData.title;
                savedPaper.contactemail = newPaperData.contactemail;
                savedPaper.firstname1 = newPaperData.firstname1;
                savedPaper.lastname1 = newPaperData.lastname1;
                savedPaper.email1 = newPaperData.email1;
                savedPaper.affilation1 = newPaperData.affilation1;
                savedPaper.firstname2 = newPaperData.firstname2;
                savedPaper.lastname2 = newPaperData.lastname2;
                savedPaper.email2 = newPaperData.email2;
                savedPaper.affilation2 = newPaperData.affilation2;
                savedPaper.firstname3 = newPaperData.firstname3;
                savedPaper.lastname3 = newPaperData.lastname3;
                savedPaper.email3 = newPaperData.email3;
                savedPaper.affilation3 = newPaperData.affilation3;
                savedPaper.firstname4 = newPaperData.firstname4;
                savedPaper.lastname4 = newPaperData.lastname4;
                savedPaper.email4 = newPaperData.email4;
                savedPaper.affilation4 = newPaperData.affilation4;
                savedPaper.firstname5 = newPaperData.firstname5;
                savedPaper.lastname5 = newPaperData.lastname5;
                savedPaper.email5 = newPaperData.email5;
                savedPaper.affilation5 = newPaperData.affilation5;
                savedPaper.firstname6 = newPaperData.firstname6;
                savedPaper.lastname6 = newPaperData.lastname6;
                savedPaper.email6 = newPaperData.email6;
                savedPaper.affilation6 = newPaperData.affilation6;
                savedPaper.firstname7 = newPaperData.firstname7;
                savedPaper.lastname7 = newPaperData.lastname7;
                savedPaper.email7 = newPaperData.email7;
                savedPaper.affilation7 = newPaperData.affilation7;
                savedPaper.otherauthor = newPaperData.otherauthor;
                savedPaper.candidate = newPaperData.candidate;
                savedPaper.volunteer = newPaperData.volunteer;
                savedPaper.paperabstract = newPaperData.paperabstract;
                savedPaper.ifsubmit = newPaperData.ifsubmit;
                savedPaper.format = newPaperData.format;
                savedPaper.papersize = newPaperData.papersize;
                savedPaper.conference = newPaperData.conference;
                savedPaper.topic = newPaperData.topic;
                savedPaper.status = newPaperData.status;
                savedPaper.date = newPaperData.date;
                

                savedPaper.update();
                flash("success", "Paper " + paperForm.get().title + " has been updated");
                txn.commit();
            }
        } finally {
            txn.end();
        }

        return GO_HOME;
    }
    public Result create() {
        Form<Paper> paperForm = formFactory.form(Paper.class);
        return ok(
                views.html.createPaper.render(paperForm)
        );
    }
    public Result save() {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        if(paperForm.hasErrors()) {
            return badRequest(views.html.createForm.render(paperForm));
        }
        paperForm.get().save();
        flash("success", "Paper " + paperForm.get().title + " has been created");
        return GO_HOME;
    }
}
