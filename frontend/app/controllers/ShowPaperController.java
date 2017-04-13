package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import com.avaje.ebeaninternal.server.type.ScalarTypeYear;
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

import com.avaje.ebeaninternal.server.type.ScalarTypeYear;

import com.fasterxml.jackson.databind.ObjectMapper;// in play 2.3
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

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
    public CompletionStage<Result> showMyPaper() {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        //Paper paperInfo = paperForm.get();
        Paper paperInfo = new Paper();
        Http.Session session = Http.Context.current().session();
        String username = session.get("username");

        JsonNode json = Json.newObject()
                .put("username", username);
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/papers/" + username).get();
        return res.thenApplyAsync(response -> {
            JsonNode ret = response.asJson();
            if ("successful".equals(ret.get("status").asText())) {
                ObjectMapper objectMapper = new ObjectMapper();
                Paper[] reslist = objectMapper.readValue(json, Paper[].class);
                List<Paper> res = Array.asList(reslist);
                return ok(
                        views.html.showmypaper.render(paperForm,res);
                );
            }else{
                return ok(views.html.showmypaper.render(paperForm,1));
            }
        });

//        List<Paper> res = new ArrayList<Paper>();
        //res = paperInfo.GetMyPaper(username);

//        Long id = res.get(0).id;
//        String title = res.get(0).title;
//        String conference = res.get(0).conference;
//

//         String authors = "";
//        for(int i =0; i <res.size(); i++){
//            res.get(i).authors = "";
//            if(!res.get(i).firstname1.isEmpty()){
//                res.get(i).authors = res.get(i).authors + res.get(i).firstname1 + " ";
//                res.get(i).authors = res.get(i).authors + res.get(i).lastname1 + ", ";
//            }
//            if(!res.get(i).firstname2.isEmpty()){
//                res.get(i).authors = res.get(i).authors + res.get(i).firstname2 + " ";
//                res.get(i).authors = res.get(i).authors + res.get(i).lastname2 + ", ";
//            }
//            if(!res.get(i).firstname3.isEmpty()){
//                res.get(i).authors = res.get(i).authors + res.get(i).firstname3 + " ";
//                res.get(i).authors = res.get(i).authors + res.get(i).lastname3 + ", ";
//            }
//            if(!res.get(i).firstname4.isEmpty()){
//                res.get(i).authors = res.get(i).authors + res.get(i).firstname4 + " ";
//                res.get(i).authors = res.get(i).authors + res.get(i).lastname4 + ", ";
//            }
//            if(!res.get(i).firstname5.isEmpty()){
//                res.get(i).authors = res.get(i).authors + res.get(i).firstname5 + " ";
//                res.get(i).authors = res.get(i).authors + res.get(i).lastname5 + ", ";
//            }
//            if(!res.get(i).firstname6.isEmpty()){
//                res.get(i).authors = res.get(i).authors + res.get(i).firstname6 + " ";
//                res.get(i).authors = res.get(i).authors + res.get(i).lastname6 + ", ";
//            }
//            if(!res.get(i).firstname7.isEmpty()){
//                res.get(i).authors = res.get(i).authors + res.get(i).firstname7 + " ";
//                res.get(i).authors = res.get(i).authors + res.get(i).lastname7 + ", ";
//            }
//            if(!res.get(i).contactemail.isEmpty()){
//                res.get(i).authors = res.get(i).authors + res.get(i).contactemail + " ";
//            }
//        }


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
        //String email = User.GetEmailByUsername(username);
//        String email = session.get("email");
//        System.out.println("In show my paper username "+username);
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
