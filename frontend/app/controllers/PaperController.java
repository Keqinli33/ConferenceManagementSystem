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
public class PaperController extends Controller {
    @Inject WSClient ws;
    private FormFactory formFactory;

    @Inject
    public PaperController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /*public Result GO_HOME = Results.redirect(
            routes.HomeController.list(0, "name", "asc", "")
    );*/
    public Result GO_HOME = Results.redirect(
            routes.ShowPaperController.showMyPaper()
    );
    /**
     * Handle default path requests, redirect to computers list
     */
    public Result index() {
        return GO_HOME;
    }

    public CompletionStage<Result> edit(Long id) {
//        Form<Paper> paperForm = formFactory.form(Paper.class).fill(
//                Paper.find.byId(id)
//        );
//        return ok(
//                views.html.editPaper.render(id, paperForm)
//        );
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
//                savedPaper.ifsubmit = ret.get("ifsubmit").asText();
//                savedPaper.format = ret.get("format").asText();
//                savedPaper.papersize = ret.get("papersize").asText();
//                savedPaper.conference = ret.get("conference").asText();
            savedPaper.topic = ret.get("topic").asText();
//                savedPaper.status = ret.get("status").asText();
//                savedPaper.date = ret.get("date").asText();
            return ok(
                    views.html.editPaper.render(id, paperForm,savedPaper)
            );
        });

    }
    public CompletionStage<Result> update(Long id) throws PersistenceException {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
//        if(paperForm.hasErrors()) {
//            return badRequest(views.html.editPaper.render(id, paperForm));
//        }

//        Transaction txn = Ebean.beginTransaction();
//        try {
//            Paper savedPaper = Paper.find.byId(id);
//            if (savedPaper != null) {
                Paper newPaper = paperForm.get();
//                if(!newPaperData.contactemail.equals(newPaperData.confirmemail)){
//                    return badRequest(views.html.editPaper.render(id, paperForm));
//                }

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
//            if(paperForm.hasErrors()) {
//                return badRequest(views.html.createPaper.render(paperForm));
//            }
//            else
                if ("update successfully".equals(ret)) {
//                session.put("Submitted","ok");
//                session.put("paperid",Long.toString(paperForm.get().id));

                return GO_HOME;
            }else{
                    return GO_HOME;
                }

        });

//                savedPaper.update();
//                flash("success", "Paper " + paperForm.get().title + " has been updated");
//                txn.commit();
//            }
//        } finally {
//            txn.end();
//        }

//        return GO_HOME;
    }
    public Result create() {
        Form<Paper> paperForm = formFactory.form(Paper.class);
        return ok(
                views.html.createPaper.render(paperForm)
        );
    }
    public CompletionStage<Result> save() {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
//        if(paperForm.hasErrors()) {
//            return badRequest(views.html.createPaper.render(paperForm));
//        }

        Paper newPaper = paperForm.get();
//        if(!newPaper.contactemail.equals(newPaper.confirmemail)){
//            return badRequest(views.html.createPaper.render(paperForm));
//        }

        Http.Session session = Http.Context.current().session();
//        String username = session.get("username");
        newPaper.username= session.get("username");
        newPaper.ifsubmit = "N";
//        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//
//        // Get the date today using Calendar object.
//        Date today = Calendar.getInstance().getTime();
//        // Using DateFormat format method we can create a string
//        // representation of a date with the defined format.
//        String reportDate = df.format(today);
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
                .put("conference", newPaper.conference)
                .put("file", newPaper.file);

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
//        String email = session.get("email");
        //SendEmail(email, "Dear Sir/Madam, your paper is successfully submitted");

        //TODO flash not working, switch to session way
        //flash("success", "Thank you. Your paper abstract has been submitted successfully. " + paperForm.get().title + " has been created" +" Please keep your paper id:" + paperForm.get().id);
//        session.put("Submitted","ok");
//        session.put("paperid",Long.toString(paperForm.get().id));
//
//        return GO_HOME;
    }
    public Result uploadFile(Long id) {
        Form<Paper> paperForm = formFactory.form(Paper.class);
        return ok(
                views.html.selectFile.render(id, paperForm)
        );
    }
    public Result selectFile(Long id) {
        Form<Paper> paperForm = formFactory.form(Paper.class).bindFromRequest();
//        if(paperForm.hasErrors()) {
//            return badRequest(views.html.editPaper.render(id, paperForm));
//        }
        Paper savedPaper = Paper.find.byId(id);
        System.out.println("begin upload file");
//        if (savedPaper != null) {
            System.out.println("upload file");
            Http.MultipartFormData body = request().body().asMultipartFormData();
            if(body == null)
            {
                return badRequest("Invalid request, required is POST with enctype=multipart/form-data.");
            }

            Http.MultipartFormData.FilePart<File> filePart = body.getFile("file");
            if(filePart == null)
            {
                return badRequest("Invalid request, no file has been sent.");
            }

            // getContentType can return null, so we check the other way around to prevent null exception
//            if(!"application/pdf".equalsIgnoreCase(filePart.getContentType()))
//            {
//                return badRequest("Invalid request, only PDFs are allowed.");
//            }
            try {
                File file = filePart.getFile();
                File destination = new File("/Users/huiliangling/uploads", file.getName());
                FileUtils.moveFile(file, destination);
                savedPaper.ifsubmit = "Y";
                savedPaper.format = filePart.getContentType();
                savedPaper.papersize = String.valueOf(destination.length());
                System.out.println("File length  " + destination.length());
                savedPaper.update();
            } catch (Exception e){
                e.printStackTrace();
            }
//                savedPaper.ifsubmit = "Y";
//                savedPaper.format = filePart.getContentType();
//                savedPaper.papersize = String.valueOf(file.length());
//                savedPaper.update();


//        }
        try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("socandrew2017@gmail.com", "ling0915"));
            email.setSSLOnConnect(true);
            email.setFrom("socandrew2017@gmail.com");
            email.setSubject("Paper submitted");
            email.setMsg("Dear Sir/Madam, your paper is successfully submitted");
            Http.Session session = Http.Context.current().session();
            String emailto = session.get("email");
            email.addTo(emailto);
            email.send();
        }catch (Exception e){
            e.printStackTrace();
        }
        flash("success", "Paper File has been submitted");
        return GO_HOME;
    }

    private static void SendEmail(String emailto, String content){
        try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("socandrew2017@gmail.com", "ling0915"));
            email.setSSLOnConnect(true);
            email.setFrom("socandrew2017@gmail.com");
            email.setSubject("Temporary password");
            email.setMsg(content);
            email.addTo(emailto);
            email.send();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
