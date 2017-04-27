package controllers;

import play.data.format.Formats;
import play.mvc.Controller;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.mvc.Result;
import play.mvc.Http;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import models.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import javax.ws.rs.core.MediaType;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Date;
import java.io.File;
import org.apache.commons.mail.*;
import org.apache.commons.io.FileUtils;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;

/**
 * Created by shuang on 3/29/17.
 */
public class TopicController extends Controller {
    @Inject WSClient ws;
    private FormFactory formFactory;

    @Inject
    public TopicController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }


    public Result GO_HOME = Results.redirect(
            routes.TopicController.showMyTopic()
    );
    /**
     * Handle default path requests, redirect to computers list
     */
    public Result index() {
        return GO_HOME;
    }

    public CompletionStage<Result> edit(Long id) {

        Form<Paper> paperForm = formFactory.form(Paper.class);
        System.out.println("here is " + id);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/papers/"+id).get();
        return res.thenApply(response -> {

            JsonNode ret = response.asJson();
            System.out.println("response from edit" + ret);
            Paper savedPaper = new Paper();
            savedPaper.title = ret.get("title").asText();
            savedPaper.contactemail = ret.get("contactemail").asText();
            savedPaper.firstname1 = ret.get("firstname1").asText();
            savedPaper.lastname1 = ret.get("lastname1").asText();
            savedPaper.email1 = ret.get("email1").asText();
            savedPaper.affilation1 = ret.get("affilation1").asText();
            savedPaper.firstname2 = ret.get("firstname2").asText();
            savedPaper.lastname2 = ret.get("lastname2").asText();
            savedPaper.email2 = ret.get("email2").asText();
            savedPaper.affilation2 = ret.get("affilation2").asText();
            savedPaper.firstname3 = ret.get("firstname3").asText();
            savedPaper.lastname3 = ret.get("lastname3").asText();
            savedPaper.email3 = ret.get("email3").asText();
            savedPaper.affilation3 = ret.get("affilation3").asText();
            savedPaper.firstname4 = ret.get("firstname4").asText();
            savedPaper.lastname4 = ret.get("lastname4").asText();
            savedPaper.email4 = ret.get("email4").asText();
            savedPaper.affilation4 = ret.get("affilation4").asText();
            savedPaper.firstname5 = ret.get("firstname5").asText();
            savedPaper.lastname5 = ret.get("lastname5").asText();
            savedPaper.email5 = ret.get("email5").asText();
            savedPaper.affilation5 = ret.get("affilation5").asText();
            savedPaper.firstname6 = ret.get("firstname6").asText();
            savedPaper.lastname6 = ret.get("lastname6").asText();
            savedPaper.email6 = ret.get("email6").asText();
            savedPaper.affilation6 = ret.get("affilation6").asText();
            savedPaper.firstname7 = ret.get("firstname7").asText();
            savedPaper.lastname7 = ret.get("lastname7").asText();
            savedPaper.email7 = ret.get("email7").asText();
            savedPaper.affilation7 = ret.get("affilation7").asText();
            savedPaper.otherauthor = ret.get("otherauthor").asText();
            savedPaper.candidate = ret.get("candidate").asText();
            savedPaper.volunteer = ret.get("volunteer").asText();
            savedPaper.paperabstract = ret.get("paperabstract").asText();
            savedPaper.topic = ret.get("topic").asText();

            return ok(
                    views.html.editPaper.render(id, paperForm,savedPaper)
            );
        });

    }
    public CompletionStage<Result> update(Long id) throws PersistenceException {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();

        Paper newPaper = paperForm.get();

        JsonNode json = Json.newObject()
                .put("title", newPaper.title)
                .put("contactemail",newPaper.contactemail)
                .put("firstname1",newPaper.firstname1)
                .put("lastname1",newPaper.lastname1)
                .put("email1",newPaper.email1)
                .put("affilation1",newPaper.affilation1)
                .put("firstname2",newPaper.firstname2)
                .put("lastname2",newPaper.lastname2)
                .put("email2",newPaper.email2)
                .put("affilation2",newPaper.affilation2)
                .put("firstname3",newPaper.firstname3)
                .put("lastname3",newPaper.lastname3)
                .put("email3",newPaper.email3)
                .put("affilation3",newPaper.affilation3)
                .put("firstname4",newPaper.firstname4)
                .put("lastname4",newPaper.lastname4)
                .put("email4",newPaper.email4)
                .put("affilation4",newPaper.affilation4)
                .put("firstname5",newPaper.firstname5)
                .put("lastname5",newPaper.lastname5)
                .put("email5",newPaper.email5)
                .put("affilation5",newPaper.affilation5)
                .put("firstname6",newPaper.firstname6)
                .put("lastname6",newPaper.lastname6)
                .put("email6",newPaper.email6)
                .put("affilation6",newPaper.affilation6)
                .put("firstname7",newPaper.firstname7)
                .put("lastname7",newPaper.lastname7)
                .put("email7",newPaper.email7)
                .put("affilation7",newPaper.affilation7)
                .put("otherauthor", newPaper.otherauthor)
                .put("candidate", newPaper.candidate)
                .put("volunteer", newPaper.volunteer)
                .put("paperabstract", newPaper.paperabstract)
                .put("topic", newPaper.topic);
                

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/papers/"+id).post(json);
        return res.thenApplyAsync(response -> {
            String ret = response.getBody();
            System.out.println("response from update "+ret);

                if ("update successfully".equals(ret)) {


                return GO_HOME;
            }else{
                    return GO_HOME;
                }

        });


    }
    public Result create() {
//        Form<Paper> paperForm = formFactory.form(Paper.class);
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        Paper newPaper = paperForm.get();
        String conf = newPaper.conference;
        if(conf.equals("All My Conference")){
            return GO_HOME;
        }else {
        return ok(
                views.html.createPaper.render(paperForm, conf)
        );
        }
    }
    public CompletionStage<Result> save(String conf) {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();


        Paper newPaper = paperForm.get();


        Http.Session session = Http.Context.current().session();
//        String username = session.get("username");
        newPaper.username= session.get("username");
        newPaper.ifsubmit = "N";

        Date date = new Date();
        newPaper.date = date.toString();
        JsonNode json = Json.newObject()
                .put("username", newPaper.username)
                .put("title", newPaper.title)
                .put("authors", newPaper.authors)
                .put("confirmemail", newPaper.confirmemail)
                .put("contactemail",newPaper.contactemail)
                .put("firstname1",newPaper.firstname1)
                .put("lastname1",newPaper.lastname1)
                .put("email1",newPaper.email1)
                .put("affilation1",newPaper.affilation1)
                .put("firstname2",newPaper.firstname2)
                .put("lastname2",newPaper.lastname2)
                .put("email2",newPaper.email2)
                .put("affilation2",newPaper.affilation2)
                .put("firstname3",newPaper.firstname3)
                .put("lastname3",newPaper.lastname3)
                .put("email3",newPaper.email3)
                .put("affilation3",newPaper.affilation3)
                .put("firstname4",newPaper.firstname4)
                .put("lastname4",newPaper.lastname4)
                .put("email4",newPaper.email4)
                .put("affilation4",newPaper.affilation4)
                .put("firstname5",newPaper.firstname5)
                .put("lastname5",newPaper.lastname5)
                .put("email5",newPaper.email5)
                .put("affilation5",newPaper.affilation5)
                .put("firstname6",newPaper.firstname6)
                .put("lastname6",newPaper.lastname6)
                .put("email6",newPaper.email6)
                .put("affilation6",newPaper.affilation6)
                .put("firstname7",newPaper.firstname7)
                .put("lastname7",newPaper.lastname7)
                .put("email7",newPaper.email7)
                .put("affilation7",newPaper.affilation7)
                .put("otherauthor", newPaper.otherauthor)
                .put("candidate", newPaper.candidate)
                .put("volunteer", newPaper.volunteer)
                .put("paperabstract", newPaper.paperabstract)
                .put("topic", newPaper.topic)
                .put("ifsubmit", newPaper.ifsubmit)
                .put("format", newPaper.format)
                .put("papersize", newPaper.papersize)
                .put("date", newPaper.date)
                .put("conference", conf)
                .put("file", newPaper.file)
                .put("reviewerid",session.get("userid"))
                .put("reviewstatus", "assigned");

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/papers").post(json);
        return res.thenApply(response -> {
            String ret = response.getBody();
            System.out.println("here is "+ret);
            if ("save successfully".equals(ret)) {
                session.put("Submitted","ok");
                session.put("paperid",Long.toString(paperForm.get().id));

                return GO_HOME;
            }else{
                return GO_HOME;
            }

        });

    }

    public CompletionStage<Result> showMyTopic(String conferencename) {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        //Paper paperInfo = paperForm.get();
        Paper paperInfo = new Paper();
        Http.Session session = Http.Context.current().session();
        String username = session.get("username");

        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        String[] conferences= session.get("conferences").split("#");
        for(String s : conferences) {
            options.put("All My Conference","All My Conference");
            options.put(s, s);
        }
        JsonNode json = Json.newObject()
                .put("username", username);
        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/paper/" + username).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        return resofrest.thenApplyAsync(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
            List<Paper> res = new ArrayList<Paper>();
            for(JsonNode res1 : ret){
                Paper savedPaper = new Paper();
                if(res1.get("conference").asText().equals(conferencename)){
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
                    savedPaper.date = res1.get("date").asText();
                    res.add(savedPaper);
                }
            }
            return ok(
                    views.html.showmypaper.render(paperForm,res,session,options,conferencename));

        });

    }

}




