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

        Form<Topic> topicForm = formFactory.form(Topic.class);
        Topic topicInfo = new Topic();

        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/topics/"+id).get();
        res.thenAccept(response -> {

            JsonNode ret = response.asJson();
            System.out.println("response from edit" + ret);
            Topic savedTopic = new Topic();
            savedTopic.topic = ret.get("topic").asText();
            savedTopic.id = Long.parseLong(ret.get("id").asText());

        });

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
            return ok(views.html.edittopic.render(id,topicForm,res,savedTopic));
        });
    }
    public CompletionStage<Result> update(Long id) throws PersistenceException {
        Form<Topic> topicForm = formFactory.form(Topic.class).bindFromRequest();

        Paper newTopic = topicForm.get();
        Topic topicInfo = new Topic();

        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");
        JsonNode json = Json.newObject()
                .put("topic", newTopic.topic);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/topics/"+id).post(json);
        return res.thenApply(response -> {
            return GO_HOME;
        });

    }

    public CompletionStage<Result> delete(Long id) throws PersistenceException {
        Form<Topic> topicForm = formFactory.form(Topic.class).bindFromRequest();

        Paper newTopic = topicForm.get();
        Topic topicInfo = new Topic();


        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/topics/delete/"+id).post();
        return res.thenApply(response -> {
            return GO_HOME;
        });

    }

    public CompletionStage<Result> save() {
        Form<Topic> topicForm = formFactory.form(Topic.class).bindFromRequest();


        Topic newTopic = topicForm.get();

        Http.Session session = Http.Context.current().session();
        String conferenceinfo = session.get("conferenceinfo");

        JsonNode json = Json.newObject()
                .put("conference", conferenceinfo)
                .put("topic", newTopic.topic);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/topics/new").post(json);
        res.thenAccept(response -> {

        });

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
                    views.html.topic.render(topicForm,res));

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
                    views.html.topic.render(topicForm,res));

        });

    }

}




