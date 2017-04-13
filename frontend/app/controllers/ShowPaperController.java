package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import com.avaje.ebeaninternal.server.type.ScalarTypeYear;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import java.text.SimpleDateFormat;
import java.text.ParseException;

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
    @Inject WSClient ws;
//    @Inject HttpExecutionContext ec;
    private FormFactory formFactory;

    @Inject
    public ShowPaperController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /**
     * This result directly redirect to application home.
     */
    public Result GO_HOME = Results.redirect(
            routes.ShowPaperController.showMyPaper()
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
        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/paper/" + username).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        return resofrest.thenApplyAsync(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
//            if ("successful".equals(ret.get("status").asText())) {
//                ObjectMapper objectMapper = new ObjectMapper();
//                Paper[] reslist = objectMapper.readValue(ret, Paper[].class);
//                List<Paper> res = Array.asList(reslist);
                List<Paper> res = new ArrayList<Paper>();
                for(JsonNode res1 : ret){
                    Paper savedPaper = new Paper();
                    savedPaper.id = Long.parseLong(res1.get("id").asText());
//                    savedPaper.username = res1.get("username").asText();
                    savedPaper.title = res1.get("title").asText();
                    savedPaper.contactemail = res1.get("contactemail").asText();
                    savedPaper.authors = res1.get("authors").asText();
                    savedPaper.firstname1 = res1.get("firstname1").asText();
                    savedPaper.lastname1 = res1.get("lastname1").asText();
                    savedPaper.email1 = res1.get("email1").asText();
                    savedPaper.affilation1 = res1.get("affilation1").asText();
                    savedPaper.firstname2 = res1.get("firstname2").asText();
                    savedPaper.lastname2 = res1.get("lastname2").asText();
                    savedPaper.email2 = res1.get("email2").asText();
                    savedPaper.affilation2 = res1.get("affilation2").asText();
                    savedPaper.firstname3 = res1.get("firstname3").asText();
                    savedPaper.lastname3 = res1.get("lastname3").asText();
                    savedPaper.email3 = res1.get("email3").asText();
                    savedPaper.affilation3 = res1.get("affilation3").asText();
                    savedPaper.firstname4 = res1.get("firstname4").asText();
                    savedPaper.lastname4 = res1.get("lastname4").asText();
                    savedPaper.email4 = res1.get("email4").asText();
                    savedPaper.affilation4 = res1.get("affilation4").asText();
                    savedPaper.firstname5 = res1.get("firstname5").asText();
                    savedPaper.lastname5 = res1.get("lastname5").asText();
                    savedPaper.email5 = res1.get("email5").asText();
                    savedPaper.affilation5 = res1.get("affilation5").asText();
                    savedPaper.firstname6 = res1.get("firstname6").asText();
                    savedPaper.lastname6 = res1.get("lastname6").asText();
                    savedPaper.email6 = res1.get("email6").asText();
                    savedPaper.affilation6 = res1.get("affilation6").asText();
                    savedPaper.firstname7 = res1.get("firstname7").asText();
                    savedPaper.lastname7 = res1.get("lastname7").asText();
                    savedPaper.email7 = res1.get("email7").asText();
                    savedPaper.affilation7 = res1.get("affilation7").asText();
                    savedPaper.otherauthor = res1.get("otherauthor").asText();
                    savedPaper.candidate = res1.get("candidate").asText();
                    savedPaper.volunteer = res1.get("volunteer").asText();
                    savedPaper.paperabstract = res1.get("paperabstract").asText();
                savedPaper.ifsubmit = res1.get("ifsubmit").asText();
                savedPaper.format = res1.get("format").asText();
                savedPaper.papersize = res1.get("papersize").asText();
                savedPaper.conference = res1.get("conference").asText();
                    savedPaper.topic = res1.get("topic").asText();
                savedPaper.status = res1.get("status").asText();
//                try{
//                savedPaper.date = new SimpleDateFormat("yyyy-MM-dd").parse(res1.get("date").asText());
                savedPaper.date = res1.get("date").asText();

//                }
//                catch (ParseException e) {
//                        e.printStackTrace();
//                    }
                    res.add(savedPaper);
                }
                return ok(
                        views.html.showmypaper.render(paperForm,res,session));
//            }
//            else{
//                return GO_HOME;
//            }
        });



        
    }

}
