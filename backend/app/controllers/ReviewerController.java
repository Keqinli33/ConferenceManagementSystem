package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import models.*;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import play.mvc.Http.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import play.libs.Json;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by sxh on 17/4/12.
 */
public class ReviewerController extends Controller{

    private FormFactory formFactory;

    @Inject
    public ReviewerController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /**
     * Handle get conf info
     */
    public Result getconf(Long id) {

        List<Paper> paperList = Paper.ReviewPapers(id);


        Map<String, Integer> reviewcount = new HashMap<String, Integer>();
        Map<String, Integer> leftcount = new HashMap<String, Integer>();

        for(Paper paper : paperList){
            System.out.println(paper.reviewstatus);
            String conf = paper.conference;
            if(paper.reviewstatus.equals("assigned")){
                System.out.println("I am here");
                leftcount.put(conf, leftcount.getOrDefault(conf, 0) + 1);
                if(!reviewcount.containsKey(conf)){
                    reviewcount.put(conf, 0);
                }
            }
            else if(paper.reviewstatus.equals("reviewed")){
                reviewcount.put(conf, reviewcount.getOrDefault(conf, 0) + 1);
                if(!leftcount.containsKey(conf)){
                    leftcount.put(conf, 0);
                }
            }
        }



        int i = 0;
        //ObjectNode node = Json.newObject();

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode arr = new ArrayNode(factory);

        for(Map.Entry<String, Integer> entry : leftcount.entrySet()){
            String key = entry.getKey();
            JsonNode json = Json.newObject()
                    .put("conf", key)
                    .put("assigned", (entry.getValue() + reviewcount.get(key)))
                    .put("reviewed", reviewcount.get(key))
                    .put("left", entry.getValue());
            //node.put(Integer.toString(i++), json);
            arr.add(json);
        }
        System.out.println(arr);
        JsonNode temp = (JsonNode)arr;

        return ok(temp);
    }

    /**
     * Handle get conf info
     */
    public Result getpapers(Long id, String papername) {
        System.out.println("============"+ papername);
        String name = papername.replaceAll("\\+", " ");
        System.out.println("============"+ name);
        List<Paper> paperList = Paper.ConfPapers(name);

        int i = 0;
        //ObjectNode node = Json.newObject();

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode arr = new ArrayNode(factory);

        for(Paper newPaper : paperList){
            if(Review.find.where().eq("paperid", newPaper.id).eq("reviewerid", id).findList().size() > 0) {
                JsonNode json = Json.newObject()
                        .put("id", newPaper.id)
                        .put("title", newPaper.title)
                        .put("contactemail", newPaper.contactemail)
                        .put("authors", newPaper.authors)
                        .put("firstname1", newPaper.firstname1)
                        .put("lastname1", newPaper.lastname1)
                        .put("email1", newPaper.email1)
                        .put("affilation1", newPaper.affilation1)
                        .put("firstname2", newPaper.firstname2)
                        .put("lastname2", newPaper.lastname2)
                        .put("email2", newPaper.email2)
                        .put("affilation2", newPaper.affilation2)
                        .put("firstname3", newPaper.firstname3)
                        .put("lastname3", newPaper.lastname3)
                        .put("email3", newPaper.email3)
                        .put("affilation3", newPaper.affilation3)
                        .put("firstname4", newPaper.firstname4)
                        .put("lastname4", newPaper.lastname4)
                        .put("email4", newPaper.email4)
                        .put("affilation4", newPaper.affilation4)
                        .put("firstname5", newPaper.firstname5)
                        .put("lastname5", newPaper.lastname5)
                        .put("email5", newPaper.email5)
                        .put("affilation5", newPaper.affilation5)
                        .put("firstname6", newPaper.firstname6)
                        .put("lastname6", newPaper.lastname6)
                        .put("email6", newPaper.email6)
                        .put("affilation6", newPaper.affilation6)
                        .put("firstname7", newPaper.firstname7)
                        .put("lastname7", newPaper.lastname7)
                        .put("email7", newPaper.email7)
                        .put("affilation7", newPaper.affilation7)
                        .put("otherauthor", newPaper.otherauthor)
                        .put("candidate", newPaper.candidate)
                        .put("volunteer", newPaper.volunteer)
                        .put("paperabstract", newPaper.paperabstract)
                        .put("topic", newPaper.topic)
                        .put("reviewstatus", newPaper.reviewstatus)
                        .put("reviewerid", newPaper.reviewerid)
                        .put("review", newPaper.review);
                //node.put(Integer.toString(i++), json);
                arr.add(json);
            }
        }
//        System.out.println(arr);
        JsonNode temp = (JsonNode)arr;

        return ok(temp);
    }


//    /**
//     * Handle the 'new profile form' submission
//     */
//    public Result save() {
//        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();
//        profileForm.get().save();
//        flash("success", "Profile " + profileForm.get().title + profileForm.get().lastname + " has been created");
//        return ok("delete successfully");
//    }
//
//    /**
//     * Handle profile deletion
//     */
//    public Result delete() {
//        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();
//        Profile deletedProfile = Profile.find.byId(profileForm.get().userid);
//        if(deletedProfile != null){
//            deletedProfile.delete();
//            return ok("delete successfully");
//        }
//        else{
//            return ok("you haven't create your profile yet");
//        }
//    }

    public Result updateReview(){
//        DynamicForm requestData = formFactory.form().bindFromRequest();
//
//        JsonNodeFactory factory = JsonNodeFactory.instance;
//        ArrayNode arr2 = new ArrayNode(factory);
//
//        for (int j = 0; j < arr2.size(); j++) {
//            JsonNode res = arr2.get(j);
//            System.out.println(res);
//        }
        Form<Review> reviewForm = formFactory.form(Review.class).bindFromRequest();
        Review review = reviewForm.get();
        List<Review> reviewList = Review.find.where().eq("paperid", review.paperid).eq("reviewerid", review.reviewerid).eq("label", review.label).eq("iscriteria", review.iscriteria).findList();
        if(reviewList.size() == 0){
            Review newreview = new Review();
            newreview.paperid = review.paperid;
            newreview.reviewerid = review.reviewerid;
            newreview.label = review.label;
            newreview.review_content = review.review_content;
            newreview.reviewstatus = "reviewed";
            newreview.iscriteria = review.iscriteria;
            newreview.insert();
        }
        else {
            for (Review newreview : reviewList) {
                System.out.println(newreview.review_content);
                newreview.review_content = review.review_content;
                newreview.update();
            }
        }

//        Paper paper = paperForm.get();
//        Long paperid = paper.id;
//        Paper update_paper = Paper.find.byId(paperid);
//        try {
//            update_paper.review = paper.review;
//            update_paper.reviewstatus = "reviewed";
//            update_paper.update();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
        return ok("successfully");
    }

    public Result changeReviewer(){
        Form<Review> reviewForm = formFactory.form(Review.class).bindFromRequest();
        Review update_paper = reviewForm.get();
        try {
            update_paper.insert();
        } catch (Exception e){
            e.printStackTrace();
        }
        return ok("successfully");
    }

    public Result deleteReviewer(){
        Form<Review> reviewForm = formFactory.form(Review.class).bindFromRequest();
        Review delete_review = reviewForm.get();
        List<Review> reviewList = Review.find.where().eq("paperid",delete_review.paperid)
                .eq("reviewerid",delete_review.reviewerid)
                .findList();
        try {
            for(Review review : reviewList){
                review.delete();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return ok("successfully");
    }
    public Result getReviewers(Long paperid){
        List<Review> reviewList = Review.find.where()
                .eq("paperid",paperid)
                .eq("iscriteria","NA")
                .findList();

        int i = 0;
        //ObjectNode node = Json.newObject();

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode arr = new ArrayNode(factory);

        for(Review review : reviewList){
            JsonNode json = Json.newObject()
                    .put("reviewerid", review.reviewerid);

            arr.add(json);
        }
//        System.out.println(arr);
        JsonNode temp = (JsonNode)arr;

        return ok(temp);
    }

    public Result getreview(Long paperid, Long reviewerid) {
        List<Review> reviewList = Review.find.where().eq("paperid", paperid).eq("reviewerid", reviewerid).findList();

        int i = 0;
        //ObjectNode node = Json.newObject();

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode arr = new ArrayNode(factory);

        for(Review reviews : reviewList){
            if(!reviews.iscriteria.equals("NA")) {
                JsonNode json = Json.newObject()
                        .put("id", reviews.id)
                        .put("paperid", reviews.paperid)
                        .put("reviewerid", reviews.reviewerid)
                        .put("iscriteria", reviews.iscriteria)
                        .put("label", reviews.label)
                        .put("review_content", reviews.review_content);
                //System.out.println(review.review);
                System.out.println(reviews.review_content);
                //node.put(Integer.toString(i++), json);
                arr.add(json);
            }
        }
//        System.out.println(arr);
        JsonNode temp = (JsonNode)arr;

        return ok(temp);
    }

}
