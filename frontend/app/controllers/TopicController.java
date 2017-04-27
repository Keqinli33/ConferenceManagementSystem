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
        Topic topicInfo = new Topic();

        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/topic/"+id).get();
        return res.thenApply(response -> {

            JsonNode ret = response.asJson();
            System.out.println("response from edit" + ret);
            Paper savedPaper = new Paper();
            savedPaper.title = ret.get("title").asText();
            savedPaper.contactemail = ret.get("contactemail").asText();
            savedPaper.firstname1 = ret.get("firstname1").asText();
            savedPaper.lastname1 = ret.get("lastname1").asText();


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
                .put("firstname1",newPaper.firstname1);

                

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

    public CompletionStage<Result> save(String conf) {
        Form<Topic> topicForm = formFactory.form(Topic.class).bindFromRequest();


        Paper newTopic = topicForm.get();


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
                .put("confirmemail", newPaper.confirmemail);


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

    public CompletionStage<Result> showMyTopic() {
        Form<Topic> topicForm = formFactory.form(Topic.class).bindFromRequest();
        Topic topicInfo = new Topic();
        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");

        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/topic/" + conferenceinfo).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        return resofrest.thenApplyAsync(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
            List<Topic> res = new ArrayList<Topic>();
            for(JsonNode res1 : ret){
                    Topic savedTopic = new Topic();
                    savedTopic.id = Long.parseLong(res1.get("id").asText());
                    savedTopic.topic = res1.get("topic").asText();
                    res.add(savedTopic);
            }
            return ok(
                    views.html.topic.render(paperForm,res);

        });

    }

}




