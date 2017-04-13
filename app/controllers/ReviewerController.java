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
     * Handle the 'new profile form' submission
     */
    public Result save() {
        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();
        profileForm.get().save();
        flash("success", "Profile " + profileForm.get().title + profileForm.get().lastname + " has been created");
        return ok("delete successfully");
    }

    /**
     * Handle profile deletion
     */
    public Result delete() {
        Form<Profile> profileForm = formFactory.form(Profile.class).bindFromRequest();
        Profile deletedProfile = Profile.find.byId(profileForm.get().userid);
        if(deletedProfile != null){
            deletedProfile.delete();
            return ok("delete successfully");
        }
        else{
            return ok("you haven't create your profile yet");
        }
    }

}
