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

public class ConfigSysController extends Controller {

    private FormFactory formFactory;

    @Inject
    public ConfigSysController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result GetConfInfo(String conf_title_url)
    {
        Conference conf = new Conference();

        String conf_title = conf_title_url.replace("%20", " ");

        Long old_conf_id = conf.GetConferenceInfo(conf_title);

        Conference old_conf = Conference.find.byId(old_conf_id);

        JsonNode json;

        if(old_conf_id == -1){
            json = Json.newObject().put("return_status", "error");
        }
        else {
            json = Json.newObject()
                    .put("return_status","ok")
                    .put("name", old_conf.name)
                    .put("title", old_conf.title)
                    .put("url", old_conf.url)
                    .put("conference_email", old_conf.conference_email)
                    .put("chair_email", old_conf.chair_email)
                    .put("tag_title", old_conf.tag_title)
                    .put("config_content", old_conf.config_content)
                    .put("canPDF", old_conf.canPDF)
                    .put("canPostscript", old_conf.canPostscript)
                    .put("canWord", old_conf.canWord)
                    .put("canZip", old_conf.canZip)
                    .put("canMultitopics", old_conf.canMultitopics)
                    .put("isOpenAbstract", old_conf.isOpenAbstract)
                    .put("isOpenPaper", old_conf.isOpenPaper)
                    .put("isOpenCamera", old_conf.isOpenCamera)
                    .put("isBlindReview", old_conf.isBlindReview)
                    .put("discussMode", old_conf.discussMode)
                    .put("ballotMode", old_conf.ballotMode)
                    .put("reviewer_number", old_conf.reviewer_number)
                    .put("isMailAbstract", old_conf.isMailAbstract)
                    .put("isMailUpload", old_conf.isMailUpload)
                    .put("isMailReviewSubmission", old_conf.isMailReviewSubmission);
            System.out.println("in backend multi topic: "+old_conf.canMultitopics+" NAME "+old_conf.name);
        }
        return ok(json);
    }

    public Result edit() throws PersistenceException {
        Form<Conference> conferenceForm = formFactory.form(Conference.class).bindFromRequest();
        if(conferenceForm.hasErrors()) {
            return ok();
        }

        Transaction txn = Ebean.beginTransaction();
        try {
            Conference newconferenceData = conferenceForm.get();
            //Conference savedconference = Conference.find.byId(newconferenceData.userid);

            if (newconferenceData.ifInfoExist(newconferenceData.keyword)) {
                String new_title = newconferenceData.updateInfo(newconferenceData);
                //flash("success", "conference " + userid + " has been updated");
                txn.commit();
                return ok(new_title);
            }
            else{
//                Conference newconference = new Conference();
//                newconference.title = newconferenceData.title;
//                newconference.research = newconferenceData.research;
//                newconference.firstname = newconferenceData.firstname;
//                newconference.lastname = newconferenceData.lastname;
//                newconference.position = newconferenceData.position;
//                newconference.affiliation = newconferenceData.affiliation;
//                newconference.email = newconferenceData.email;
//                newconference.phone = newconferenceData.phone;
//                newconference.fax = newconferenceData.fax;
//                newconference.address = newconferenceData.address;
//                newconference.city = newconferenceData.city;
//                newconference.country = newconferenceData.country;
//                newconference.region = newconferenceData.region;
//                newconference.zipcode = newconferenceData.zipcode;
//                newconference.comment = newconferenceData.comment;
//
//                newconference.userid = newconferenceData.userid;
//
//                newconference.insert();
                //flash("success", "conference " + userid + " has been inserted");
                txn.commit();
                return ok("666notexist");
            }
        } finally {
            txn.end();
        }
    }
}