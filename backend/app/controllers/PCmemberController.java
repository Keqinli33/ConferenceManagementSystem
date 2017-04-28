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
import play.libs.Json;
import java.util.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class PCmemberController extends Controller {

    private FormFactory formFactory;

    @Inject
    public PCmemberController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result GetAllPCmember(String conf_title_url) {
        PCmember member = new PCmember();

        String conf_title = conf_title_url.replace("%20", " ");
        List<PCmember> all_members = member.GetAllmember(conf_title);

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode jsonarray = new ArrayNode(factory);
        for(int i=0; i< all_members.size(); i++){
            JsonNode json = Json.newObject()
                    .put("email",all_members.get(i).email)
                    .put("firstname",all_members.get(i).firstname)
                    .put("lastname",all_members.get(i).lastname)
                    .put("affiliation",all_members.get(i).affiliation)
                    .put("phone",all_members.get(i).phone)
                    .put("address",all_members.get(i).address)
                    .put("ifChair",all_members.get(i).ifChair)
                    .put("ifReviewer",all_members.get(i).ifReviewer);
            jsonarray.add(json);

        }
        //jsonarray.add({"status": "successful"});
        //System.out.println(jsonarray);
        JsonNode temp = (JsonNode) jsonarray;
        return ok(temp);
    }

    public Result EditPCmember()
    {
        Form<PCmember> PCmemberForm = formFactory.form(PCmember.class).bindFromRequest();
        if(PCmemberForm.hasErrors()) {
            return ok("error");
        }

        PCmember new_member = PCmemberForm.get();

        if(new_member.IfEXIST(new_member.email)){
            new_member.updatemember(new_member);

            //get corresponding username of this email
            User tmp = new User();
            String username = tmp.GetUsernameByEmail(new_member.email);
            //update ifreview in conference
            if(!"error".equals(username))
            {
                Conference tmp_conf = new Conference();
                tmp_conf.updateIfReviewer(username, new_member.conference, new_member.ifReviewer);
            }

        }else{
            new_member.createmember(new_member);

            //get corresponding username of this email
            User tmp = new User();
            String username = tmp.GetUsernameByEmail(new_member.email);
            //update ifreview in conference
            if(!"error".equals(username))
            {
                Conference tmp_conf = new Conference();
                tmp_conf.updateIfReviewer(username, new_member.conference, new_member.ifReviewer);
            }

        }
        return ok();
    }

    public Result GetPCmember(String email) {
        PCmember member = new PCmember();

        Long ID = member.GetmemberID(email);

        PCmember old_member = PCmember.find.byId(ID);

            JsonNode json = Json.newObject()
                    .put("email",old_member.email)
                    .put("firstname",old_member.firstname)
                    .put("lastname",old_member.lastname)
                    .put("affiliation",old_member.affiliation)
                    .put("phone",old_member.phone)
                    .put("address",old_member.address)
                    .put("ifChair",old_member.ifChair)
                    .put("ifReviewer",old_member.ifReviewer);

        return ok(json);
    }

    public Result deletePCmember(String email){
        PCmember member = new PCmember();

        Long ID = member.GetmemberID(email);

        PCmember.find.ref(ID).delete();

        return ok();
    }
}