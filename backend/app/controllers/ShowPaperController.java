package controllers;

import com.avaje.ebeaninternal.server.type.ScalarTypeYear;

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

//import play.libs.Mail;
import org.apache.commons.mail.*;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
    public Result showMyPaper(String username) {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        //Paper paperInfo = paperForm.get();
        Paper paperInfo = new Paper();


        List<Paper> res = new ArrayList<Paper>();
        res = paperInfo.GetMyPaper(username);

//        Long id = res.get(0).id;
//        String title = res.get(0).title;
//        String conference = res.get(0).conference;
//

         String authors = "";
        for(int i =0; i <res.size(); i++){
            res.get(i).authors = "";
            if(!res.get(i).firstname1.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname1 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname1 + ", ";
            }
            if(!res.get(i).firstname2.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname2 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname2 + ", ";
            }
            if(!res.get(i).firstname3.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname3 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname3 + ", ";
            }
            if(!res.get(i).firstname4.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname4 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname4 + ", ";
            }
            if(!res.get(i).firstname5.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname5 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname5 + ", ";
            }
            if(!res.get(i).firstname6.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname6 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname6 + ", ";
            }
            if(!res.get(i).firstname7.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).firstname7 + " ";
                res.get(i).authors = res.get(i).authors + res.get(i).lastname7 + ", ";
            }
            if(!res.get(i).contactemail.isEmpty()){
                res.get(i).authors = res.get(i).authors + res.get(i).contactemail + " ";
            }
        }

        ArrayNode jsonarray = new ArrayNode();
        for(int i=0; i< res.size; i++){
            JsonNode json = Json.newObject()
                    .put("id", res.get(i).id)
                    .put("username", username)
                    .put("title", res.get(i).title)
                    .put("authors", res.get(i).authors)
                    .put("confirmemail", res.get(i).confirmemail)
                    .put("contactemail",res.get(i).contactemail)
                    .put("firstname1",res.get(i).firstname1)
                    .put("lastname1",res.get(i).lastname1)
                    .put("email1",res.get(i).email1)
                    .put("affilation1",res.get(i).affilation1)
                    .put("firstname2",res.get(i).firstname2)
                    .put("lastname2",res.get(i).lastname2)
                    .put("email2",res.get(i).email2)
                    .put("affilation2",res.get(i).affilation2)
                    .put("firstname3",res.get(i).firstname3)
                    .put("lastname3",res.get(i).lastname3)
                    .put("email3",res.get(i).email3)
                    .put("affilation3",res.get(i).affilation3)
                    .put("firstname4",res.get(i).firstname4)
                    .put("lastname4",res.get(i).lastname4)
                    .put("email4",res.get(i).email4)
                    .put("affilation4",res.get(i).affilation4)
                    .put("firstname5",res.get(i).firstname5)
                    .put("lastname5",res.get(i).lastname5)
                    .put("email5",res.get(i).email5)
                    .put("affilation5",res.get(i).affilation5)
                    .put("firstname6",res.get(i).firstname6)
                    .put("lastname6",res.get(i).lastname6)
                    .put("email6",res.get(i).email6)
                    .put("affilation6",res.get(i).affilation6)
                    .put("firstname7",res.get(i).firstname7)
                    .put("lastname7",res.get(i).lastname7)
                    .put("email7",res.get(i).email7)
                    .put("affilation7",res.get(i).affilation7)
                    .put("otherauthor", res.get(i).otherauthor)
                    .put("candidate", res.get(i).candidate)
                    .put("volunteer", res.get(i).volunteer)
                    .put("paperabstract", res.get(i).paperabstract)
                    .put("topic", res.get(i).topic);
                    .put("ifsubmit", res.get(i).ifsubmit)
                    .put("format", res.get(i).format)
                    .put("papersize", res.get(i).papersize)
                    .put("date", res.get(i).date)
                    .put("conference", res.get(i).conference)
                    .put("file", res.get(i).file);
             jsonarray.add(json);

        }
        return ok(jsonarray);
//
//        String topic = res.get(0).topic;
//        String status = res.get(0).status;
//        String format = res.get(0).format;
//        String filesize = res.get(0).papersize;
//        Date date = res.get(0).date;
//        String action = "Modify";



//        return ok(
//                views.html.showmypaper.render(paperForm,res, authors)
//        );

        //return GO_HOME;
    }

}
