package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import com.avaje.ebeaninternal.server.type.ScalarTypeYear;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;

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
import com.fasterxml.jackson.databind.node.ArrayNode;

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
        List<Paper> restemp =new Arraylist<Paper>();
        return res.thenApplyAsync(response -> {
            ArrayNode ret = response.asJson();
            if ("successful".equals(ret.get("status").asText())) {
//                ObjectMapper objectMapper = new ObjectMapper();
//                Paper[] reslist = objectMapper.readValue(ret, Paper[].class);
//                List<Paper> res = Array.asList(reslist);
                List<Paper> res = new ArrayList<Paper>();
                for(JsonNode res1 : ret){
                    Paper tem1 = new Paper();
                    tem1.id=Long.parseLong(res1.get("id"));
                    tem1.username=res.get("username").asText();
                    tem1.title=res.get("title").asText();
                    tem1.authors=res.get("authors").asText();
                    tem1.confirmemail=res.get("confirmemail").asText();
                    tem1.contactemail=res.get("contactemail").asText();
                    tem1.firstname1=res.get("firstname1").asText();
                    tem1.lastname1=res.get("lastname1").asText();
                    tem1.email1=res.get("email1").asText();
                    tem1.affilation1=res.get("affilation1").asText();
                    tem1.firstname2=res.get("firstname2").asText();
                    tem1.title=res.get("title").asText();
                    tem1.title=res.get("title").asText();
                    tem1.title=res.get("title").asText();
                    tem1.title=res.get("title").asText();
                    tem1.title=res.get("title").asText();
                    tem1.title=res.get("title").asText();
                    tem1.title=res.get("title").asText();
                    tem1.title=res.get("title").asText();
                    tem1.title=res.get("title").asText();
                    tem1.title=res.get("title").asText();
                    tem1.title=res.get("title").asText();
                    tem1.title=res.get("title").asText();

                }
                return ok(
                        views.html.showmypaper.render(paperForm,res));
            }else{
                return ok(views.html.showmypaper.render(paperForm,restemp));
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
