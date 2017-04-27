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
        session.put("conferenceinfo", conferenceinfo);
        return ok(views.html.admin.render());
    }

}