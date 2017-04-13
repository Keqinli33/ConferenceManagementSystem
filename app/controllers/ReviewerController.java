package controllers;

import models.Profile;
//import org.hibernate.validator.constraints.Email;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;

import javax.inject.Inject;
import models.User;
import play.mvc.Result;
import play.mvc.Results;

import java.util.*;
import play.data.validation.ValidationError;

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

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

import java.util.Random;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import models.*;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import play.mvc.Http.Session;

import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import play.mvc.*;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

import com.fasterxml.jackson.databind.ObjectMapper;// in play 2.3
import com.fasterxml.jackson.core.type.TypeReference;
/**
 * Created by sxh on 17/3/26.
 */
public class ReviewerController extends Controller{
    @Inject WSClient ws;

    private FormFactory formFactory;

    @Inject
    public ReviewerController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /**
     * This result directly redirect to application home.
     */
    /*public Result GO_HOME = Results.redirect(
            routes.HomeController.list(0, "name", "asc", "")
    );*/


    public Result GO_HOME = Results.redirect(
            routes.ShowPaperController.showMyPaper()
    );


    public CompletionStage<Result> enterReview(){
        Session session = Http.Context.current().session();
        Long userid = Long.parseLong(session.get("userid"));
//        System.out.println("Enter profile page user id is "+userid.toString());
//        Profile profile = Profile.find.byId(userid);


        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/review/conf/"+userid).get();
        return res.thenApply(response -> {
//            String str = response.getBody();
//            System.out.println("there is "+str);
//            Json js = new Json();
//            JsonNode ret = js.parse(str);
            JsonNode ret = response.asJson();
            ArrayNode arr = (ArrayNode)ret;

            List<ConfCount> list = new ArrayList();

            if(ret == null){
                return ok(
                        views.html.reviewConf.render(list)
                );
            }

            for(int i = 0; i < arr.size(); i++){
                JsonNode node = arr.get(i);
                ConfCount count = new ConfCount();
                count.conf = node.get("conf").asText();
                count.assigned = node.get("assigned").asText();
                count.reviewed = node.get("reviewed").asText();
                count.left = node.get("left").asText();
                list.add(count);
            }

//            ObjectMapper objectMapper = new ObjectMapper();
//            //new TypeReference<List<ConfCount>>(){}
//            ConfCount[] reslist = objectMapper.readValue(ret, ConfCount[].class);
//            List<ConfCount> res = Array.asList(reslist);

//            for (Map.Entry<String, JsonNode> elt : ret.fields()){
//                JsonNode node = elt.getValue();
//                ConfCount count = new ConfCount();
//                count.conf = node.get("conf");
//                count.assigned = node.get("assigned");
//                count.reviewed = node.get("reviewed");
//                count.left = node.get("left");
//                list.add(count);
//            }

            return ok(
                    views.html.reviewConf.render(list)
            );
        });

    }

    /**
     * Handle get conf info
     */
//    public Result getconf() {
//        List<Paper> paperList = Paper.ReviewPapers;
//
//        Map<String, Integer> reviewcount = new HashMap<String, Integer>();
//        Map<String, Integer> leftcount = new HashMap<String, Integer>();
//
//        for(Paper paper : paperList){
//            String conf = paper.conference;
//            if(paper.reviewstatus == "assigned"){
//                leftcount.put(conf, leftcount.getOrDefault(conf, 0) + 1);
//                if(!reviewcount.containsKey(conf)){
//                    reviewcount.put(conf, 0);
//                }
//            }
//            else if(paper.reviewstatus == "reviewed"){
//                reviewcount.put(conf, reviewcount.getOrDefault(conf, 0) + 1);
//                if(!leftcount.containsKey(conf)){
//                    leftcount.put(conf, 0);
//                }
//            }
//        }
//
//        int i = 0;
//        JsonNode confJson = new JsonNode();
//        for(Map.Entry<String, Integer> entry : leftcount.entrySet()){
//            String key = entry.getKey();
//            JsonNode json = Json.newObject()
//                    .put("conf", key)
//                    .put("assigned", Integer.parseInt(entry.getValue() + reviewcount.get(key)))
//                    .put("reviewed", Integer.parseInt(reviewcount.get(key)))
//                    .put("left", Integer.parseInt(entry.getValue()));
//            confJson.put(Integer.toString(i++), json);
//        }
//
//        return ok(confJson);
//    }


    /**
     * Handle the 'edit form' submission
     *
     */
    public CompletionStage<Result> edit() throws PersistenceException {
        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();


        Session session = Http.Context.current().session();
        Long userid = Long.parseLong(session.get("userid"));

        Profile newProfileData = profileForm.get();

        JsonNode json = Json.newObject()
                .put("title", newProfileData.title)
                .put("research", newProfileData.research)
                .put("firstname",newProfileData.firstname)
                .put("lastname", newProfileData.lastname)
                .put("position", newProfileData.position)
                .put("affiliation", newProfileData.affiliation)
                .put("email", newProfileData.email)
                .put("phone", newProfileData.phone)
                .put("fax", newProfileData.fax)
                .put("address",newProfileData.address)
                .put("city", newProfileData.city)
                .put("country", newProfileData.country)
                .put("region", newProfileData.region)
                .put("zipcode", newProfileData.zipcode)
                .put("comment", newProfileData.comment)
                .put("userid", userid);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/profile/edit").post(json);
        return res.thenApply(response -> {
            String ret = response.getBody();
            System.out.println("here is "+ret);
            if(profileForm.hasErrors()) {
                return ok(
                        views.html.profile.render(profileForm, null, -1)
                );
            }
            else if ("insert successfully".equals(ret)) {
                return ok(
                        views.html.profile.render(profileForm, newProfileData, 1)
                );
            }
            else if ("update successfully".equals(ret)) {
                return ok(
                        views.html.profile.render(profileForm, newProfileData, 2)
                );
            }
            else{
                return ok(
                        views.html.profile.render(profileForm, null, -1)
                );
            }
        });
    }

    /**
     * Handle the 'new profile form' submission
     */
    public Result save() {
        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();
        profileForm.get().save();
        flash("success", "Profile " + profileForm.get().title + profileForm.get().lastname + " has been created");
        return GO_HOME;
    }

    /**
     * Handle profile deletion
     */
    public CompletionStage<Result> delete() {
        Session session = Http.Context.current().session();
        Long userid = Long.parseLong(session.get("userid"));

        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();
        Profile newProfileData = profileForm.get();

        JsonNode json = Json.newObject()
                .put("title", newProfileData.title)
                .put("research", newProfileData.research)
                .put("firstname",newProfileData.firstname)
                .put("lastname", newProfileData.lastname)
                .put("position", newProfileData.position)
                .put("affiliation", newProfileData.affiliation)
                .put("email", newProfileData.email)
                .put("phone", newProfileData.phone)
                .put("fax", newProfileData.fax)
                .put("address",newProfileData.address)
                .put("city", newProfileData.city)
                .put("country", newProfileData.country)
                .put("region", newProfileData.region)
                .put("zipcode", newProfileData.zipcode)
                .put("comment", newProfileData.comment)
                .put("userid", userid);

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/profile/delete").post(json);
        return res.thenApply(response -> {
            String ret = response.getBody();
            if ("delete successfully".equals(ret)) {
                return ok(
                        views.html.profile.render(profileForm, null, 3)
                );
            }
            else if ("you haven't create your profile yet".equals(ret)) {
                return ok(
                        views.html.profile.render(profileForm, null, 4)
                );
            }
            else{
                return ok(
                        views.html.profile.render(profileForm, null, -1)
                );
            }
        });
    }

}
