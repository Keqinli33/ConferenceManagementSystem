package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import models.*;
import java.util.*;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by keqinli on 3/29/17.
 */
public class ShowPaperController extends Controller{

    private FormFactory formFactory;

    @Inject
    public ShowPaperController(FormFactory formFactory) {
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

    public Result list(int page, String sortBy, String order, String filter) {
        return ok(
                views.html.list.render(
                        Computer.page(page, 10, sortBy, order, filter),
                        sortBy, order, filter
                )
        );
    }

    /**
     * Handle profile deletion
     */
    public Result showMyPaper() {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        //Paper paperInfo = paperForm.get();
        Paper paperInfo = new Paper();
        Http.Session session = Http.Context.current().session();
        String username = session.get("username");

        List<Paper> res = new ArrayList<Paper>();
        res = paperInfo.GetMyPaper(username);

//        Long id = res.get(0).id;
//        String title = res.get(0).title;
//        String conference = res.get(0).conference;
//
//        String authors = "";
//        authors = authors + res.get(0).firstname1 + " ";
//        authors = authors + res.get(0).lastname1 + ", ";
//        authors = authors + res.get(0).firstname2 + " ";
//        authors = authors + res.get(0).lastname2 + ", ";
//        authors = authors + res.get(0).firstname3 + " ";
//        authors = authors + res.get(0).lastname3 + ", ";
//        authors = authors + res.get(0).firstname4 + " ";
//        authors = authors + res.get(0).lastname4 + ", ";
//        authors = authors + res.get(0).firstname5 + " ";
//        authors = authors + res.get(0).lastname5 + ", ";
//        authors = authors + res.get(0).firstname6 + " ";
//        authors = authors + res.get(0).lastname6 + ", ";
//        authors = authors + res.get(0).firstname7 + " ";
//        authors = authors + res.get(0).lastname7;
//
        String email = User.GetEmailByUsername(username);
//
//        String topic = res.get(0).topic;
//        String status = res.get(0).status;
//        String format = res.get(0).format;
//        String filesize = res.get(0).papersize;
//        Date date = res.get(0).date;
//        String action = "Modify";



        return ok(
                views.html.showmypaper.render(paperForm,res, email)
        );

        //return GO_HOME;
    }

}
