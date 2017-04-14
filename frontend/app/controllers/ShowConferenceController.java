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
public class ShowConferenceController extends Controller{
    @Inject WSClient ws;
//    @Inject HttpExecutionContext ec;
    private FormFactory formFactory;

    @Inject
    public ShowConferenceController(FormFactory formFactory) {
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


    /**
     * Handle profile deletion
     */
    public CompletionStage<Result> showMyConference() {
        Form<Conference> conferenceForm = formFactory.form(Conference.class).bindFromRequest();
        //Paper paperInfo = paperForm.get();
        Conference conferenceInfo = new Conference();
        Http.Session session = Http.Context.current().session();
        String username = session.get("username");

        JsonNode json = Json.newObject()
                .put("username", username);
        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/conference/" + username).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        return resofrest.thenApplyAsync(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;

                List<Conference> res = new ArrayList<Conference>();
                for(JsonNode res1 : ret){
                    Conference savedConference = new Conference();
                    savedConference.id = Long.parseLong(res1.get("id").asText());
                    savedConference.title = res1.get("title").asText();
                    savedConference.location = res1.get("location").asText();
                    savedConference.date = res1.get("date").asText();
                    savedConference.status = res1.get("status").asText();
                    savedConference.ifreviewer = res1.get("ifreviewer").asText();
                    savedConference.ifadmin = res1.get("ifadmin").asText();
                    res.add(savedPaper);
                }
                return ok(
                        views.html.showmypaper.render(conferenceForm,res,session));

        });

    }


    public CompletionStage<Result> searchConference() {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        //Paper paperInfo = paperForm.get();
        Conference conferenceInfo = new Conference();
        Http.Session session = Http.Context.current().session();
        String username = session.get("username");

        JsonNode json = Json.newObject()
                .put("username", username);
        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/conference/" + username).get();
//        List<Paper> restemp =new Arraylist<Paper>();
        return resofrest.thenApplyAsync(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;

            List<Conference> res = new ArrayList<Conference>();
            for(JsonNode res1 : ret){
                Conference savedConference = new Conference();
                savedConference.id = Long.parseLong(res1.get("id").asText());
                savedConference.title = res1.get("title").asText();
                savedConference.location = res1.get("location").asText();
                savedConference.date = res1.get("date").asText();
                savedConference.status = res1.get("status").asText();
                savedConference.ifreviewer = res1.get("ifreviewer").asText();
                savedConference.ifadmin = res1.get("ifadmin").asText();
                res.add(savedPaper);
            }
            return ok(
                    views.html.showmypaper.render(conferenceForm,res,session));

        });

    }
}
