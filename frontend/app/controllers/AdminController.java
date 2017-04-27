package controllers;

import com.avaje.ebeaninternal.server.type.ScalarTypeYear;
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

//import play.libs.Mail;
import org.apache.commons.mail.*;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;
import play.mvc.*;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Ling on 2017/3/27.
 */
public class AdminController extends Controller {
    @Inject
    WSClient ws;

    private FormFactory formFactory;

    @Inject
    public AdminController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result GO_HOME = Results.redirect(
            routes.ShowConferenceController.showMyConference()
    );

    public Result adminPage(String conferenceinfo){
        Http.Session session = Http.Context.current().session();
        conferenceinfo = conferenceinfo.replaceAll(" ","+");
        session.put("conferenceinfo", conferenceinfo);
        return ok(views.html.admin.render());
    }

    public Result download(){
        Http.Session session = Http.Context.current().session();
        String username = session.get("username");
        String conferenceinfo = session.get("conferenceinfo");
        conferenceinfo = conferenceinfo.replaceAll("\\+"," ");
        CompletionStage<WSResponse> resofrest = ws.url("http://localhost:9000/paper/" + username).get();
        List<Long> res = new ArrayList<Long>();
        resofrest.thenAccept(response -> {
            System.out.println("here is "+response);
            JsonNode arr = response.asJson();
            ArrayNode ret = (ArrayNode) arr;
            for(JsonNode res1 : ret){
                Paper savedPaper = new Paper();
                if(res1.get("conference").asText().equals(conferenceinfo)){
                    savedPaper.id = Long.parseLong(res1.get("id").asText());
                    res.add(savedPaper.id);
                }
            }

        });


        try {
            FileOutputStream fos = new FileOutputStream(conferenceinfo);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for(int i =0; i<res.size(); i++){
                addToZipFile(res.get(i), zos);

            }
            zos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("downloading...");
        response().setContentType("application/x-download");
        String cmd = "attachment; filename="+conferenceinfo;
        response().setHeader("Content-disposition",cmd);
        String path = "/Users/shuang/uploads/"+Long.toString(conferenceinfo);
        //return ok(new File("/User/huiliangling/uploads/test.txt"));
        return ok(new java.io.File(path));

    }

    public static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

        System.out.println("Writing '" + fileName + "' to zip file");

        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }
}