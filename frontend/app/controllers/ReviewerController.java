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
            routes.ShowConferenceController.showMyConference()
    );


    public CompletionStage<Result> enterReviewConf(){
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

    public CompletionStage<Result> enterReviewPaper(String confName){
        Session session = Http.Context.current().session();
        Long userid = Long.parseLong(session.get("userid"));
//        System.out.println("Enter profile page user id is "+userid.toString());
//        Profile profile = Profile.find.byId(userid);

        String temp = confName.replaceAll(" ", "+");

        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/review/paper/"+userid+"/"+temp).get();
        return res.thenApply(response -> {
//            String str = response.getBody();
//            System.out.println("there is "+str);
//            Json js = new Json();
//            JsonNode ret = js.parse(str);
            JsonNode ret = response.asJson();
            ArrayNode arr = (ArrayNode)ret;

            List<Paper> list = new ArrayList();

            if(ret == null){
                return ok(
                        views.html.reviewPaper.render(list)
                );
            }

            for(int i = 0; i < arr.size(); i++){
                ret = arr.get(i);
                Paper savedPaper = new Paper();
                savedPaper.id = Long.parseLong(ret.get("id").asText());
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
                savedPaper.reviewstatus = ret.get("reviewstatus").asText();
                savedPaper.reviewerid = Long.parseLong(ret.get("reviewerid").asText());
                savedPaper.review = ret.get("review").asText();

                String affauthors = "";
                if(savedPaper.firstname1 != null || savedPaper.lastname1 != null){
                    if(savedPaper.firstname1 != null){
                        affauthors = affauthors + savedPaper.firstname1 + " ";
                    }
                    if(savedPaper.lastname1 != null){
                        affauthors = affauthors + savedPaper.lastname1 + " ";
                    }

                    if(savedPaper.affilation1 != null){
                        affauthors = savedPaper.affilation1 + " " + affauthors;
                    }

                    if(!affauthors.equals("")){
                        affauthors = ", " + affauthors;
                    }
                }
                if(savedPaper.firstname2 != null || savedPaper.lastname2 != null){
                    if(savedPaper.firstname2 != null){
                        affauthors = affauthors + savedPaper.firstname2 + " ";
                    }
                    if(savedPaper.lastname2 != null){
                        affauthors = affauthors + savedPaper.lastname2 + " ";
                    }

                    if(savedPaper.affilation2 != null){
                        affauthors = savedPaper.affilation2 + " " + affauthors;
                    }

                    if(!affauthors.equals("")){
                        affauthors = ", " + affauthors;
                    }
                }
                if(savedPaper.firstname3 != null || savedPaper.lastname3 != null){
                    if(savedPaper.firstname3 != null){
                        affauthors = affauthors + savedPaper.firstname3 + " ";
                    }
                    if(savedPaper.lastname3 != null){
                        affauthors = affauthors + savedPaper.lastname3 + " ";
                    }

                    if(savedPaper.affilation3 != null){
                        affauthors = savedPaper.affilation3 + " " + affauthors;
                    }

                    if(!affauthors.equals("")){
                        affauthors = ", " + affauthors;
                    }
                }
                if(savedPaper.firstname4 != null || savedPaper.lastname4 != null){
                    if(savedPaper.firstname4 != null){
                        affauthors = affauthors + savedPaper.firstname4 + " ";
                    }
                    if(savedPaper.lastname4 != null){
                        affauthors = affauthors + savedPaper.lastname4 + " ";
                    }

                    if(savedPaper.affilation4 != null){
                        affauthors = savedPaper.affilation4 + " " + affauthors;
                    }

                    if(!affauthors.equals("")){
                        affauthors = ", " + affauthors;
                    }
                }
                if(savedPaper.firstname5 != null || savedPaper.lastname5 != null){
                    if(savedPaper.firstname5 != null){
                        affauthors = affauthors + savedPaper.firstname5 + " ";
                    }
                    if(savedPaper.lastname5 != null){
                        affauthors = affauthors + savedPaper.lastname5 + " ";
                    }

                    if(savedPaper.affilation5 != null){
                        affauthors = savedPaper.affilation5 + " " + affauthors;
                    }

                    if(!affauthors.equals("")){
                        affauthors = ", " + affauthors;
                    }
                }
                if(savedPaper.firstname6 != null || savedPaper.lastname6 != null){
                    if(savedPaper.firstname6 != null){
                        affauthors = affauthors + savedPaper.firstname6 + " ";
                    }
                    if(savedPaper.lastname6 != null){
                        affauthors = affauthors + savedPaper.lastname6 + " ";
                    }

                    if(savedPaper.affilation6 != null){
                        affauthors = savedPaper.affilation6 + " " + affauthors;
                    }

                    if(!affauthors.equals("")){
                        affauthors = ", " + affauthors;
                    }
                }
                if(savedPaper.firstname7 != null || savedPaper.lastname7 != null){
                    if(savedPaper.firstname7 != null){
                        affauthors = affauthors + savedPaper.firstname7 + " ";
                    }
                    if(savedPaper.lastname7 != null){
                        affauthors = affauthors + savedPaper.lastname7 + " ";
                    }

                    if(savedPaper.affilation7 != null){
                        affauthors = savedPaper.affilation7 + " " + affauthors;
                    }

                    if(!affauthors.equals("")){
                        affauthors = ", " + affauthors;
                    }
                }

                savedPaper.authors = ret.get("authors").asText();
                list.add(savedPaper);
                System.out.println("IN WWWWWW "+ret.get("review").asText());
            }


            return ok(
                    views.html.reviewPaper.render(list)
            );
        });

    }


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

    public Result download(String filename){
            System.out.println("downloading...");
            response().setContentType("application/x-download");
            String cmd = "attachment; filename="+filename;
            response().setHeader("Content-disposition",cmd);
            String path = "/Users/sxh/Desktop/"+filename;
            //return ok(new File("/User/huiliangling/uploads/test.txt"));
            return ok(new java.io.File(path));

    }

    public Result review(String review, long paperid){
        Form<Paper> paperForm = formFactory.form(Paper.class);
        Map<String,String> anyData = new HashMap();
        anyData.put("paperid", Long.toString(paperid));
        anyData.put("review", review);
        System.out.println("IN REVIEW PAGE REVIEW: "+review);

        paperForm.bind(anyData);

        return ok(views.html.editreview.render(paperForm, review, paperid));
    }

    public Result updateReview(Long paperid){
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
        Paper new_paper = paperForm.get();
        System.out.println("===IN REVIEW PAPER ID IS "+Long.toString(paperid) + "REVIEW IS "+new_paper.review);

        JsonNode json = Json.newObject()
                .put("id", Long.toString(paperid))
                .put("review", new_paper.review);
        CompletionStage<WSResponse> res = ws.url("http://localhost:9000/updatereview").post(json);

        return GO_HOME;
    }

    public Result showreview(String review){
        return ok(views.html.showreview.render(review));
    }

    public Result printreview(String review){
        return ok(views.html.printreview.render(review));
    }

}
