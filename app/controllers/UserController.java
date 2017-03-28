package controllers;

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


/**
 * Created by Ling on 2017/3/27.
 */
public class UserController extends Controller {
    private FormFactory formFactory;

    @Inject
    public UserController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result GO_HOME = Results.redirect(
            routes.HomeController.list(0, "name", "asc", "")
    );

    public Result register() {
        Form<User> userForm = formFactory.form(User.class);
        return ok(
                views.html.register.render(userForm)
        );
    }

    /**
     * login when get request for login page
     * @return login html page
     */
    public Result login() {
        Form<User> userForm = formFactory.form(User.class);
        return ok(
                views.html.login.render(userForm)
        );
    }

    public Result changePwd(){
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        User new_user = userForm.get();
        String password = new_user.username;
        String password_again = new_user.password;

        Session session = Http.Context.current().session();
        String verified = session.get("ChangePwdAuthVerified");

        if(!verified.equals("true")){
            return badRequest(views.html.changePwd.render(userForm));
        }

        if(!password.equals(password_again)){
            return badRequest(views.html.changePwd.render(userForm));
        }

        String username = session.get("username");
        Long userid = Long.parseLong(session.get("userid"));
        User update_user = User.find.byId(userid);
        try {
            update_user.password = MD5(password);
            update_user.update();
        } catch (Exception e){
            e.printStackTrace();
        }
        return GO_HOME;
    }

    /**
     * Get request for verify auth for changing password
     * @return
     */
    public Result verifyAuth() {
        Form<User> userForm = formFactory.form(User.class);
        return ok(
                views.html.verifyChangePwdAuth.render(userForm)
        );
    }

    public Result sendTemporaryPwd(){
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        User new_user = userForm.get();
        //String email = new_user.email;

        //generate temporary password
        Random r = new Random();
        int max = 100000;
        int min = 0;
        String tmp_pwd =  Integer.toString(r.nextInt((max - min) + 1) + min);

        //save tmp password into User
        Session session = Http.Context.current().session();
        String username = session.get("username");
        System.out.println("Send tmp pwd Username "+username);

        //String email = new_user.GetEmailByUsername(username);
        String email = "helen14.su@hotmail.com";
        System.out.println("Send tmp pwd Email "+email);
        SendSimpleMessage(email, tmp_pwd);
        //SendEmail.SendEmail(email, tmp_pwd);
        System.out.println("Email sent");
        //TODO notify tmp pwd sent to register email

        new_user.AddTemporaryPwd(username, tmp_pwd);

        return ok(
                views.html.temporarypwd.render(userForm)
        );
    }

    public Result verifyTmpPwd(){
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        User new_user = userForm.get();
        Session session = Http.Context.current().session();
        String username = session.get("username");
        String tmp_pwd = new_user.password;
        if(new_user.IfTemporaryPwdCorrect(username, tmp_pwd)){
            session.put("ChangePwdAuthVerified", "true");
            return ok(
                    views.html.changePwd.render(userForm)
            );
        }
        return badRequest(views.html.temporarypwd.render(userForm));
    }

    public Result verifyQA(){
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        User new_user = userForm.get();

        String question1 = new_user.security_question1;
        String answer1 = new_user.security_answer1;
        String question2 = new_user.security_question2;
        String answer2 = new_user.security_answer2;

        Session session = Http.Context.current().session();
        String username = session.get("username");

        if(new_user.IfQACorrect(username, question1, answer1)){
            if(new_user.IfQACorrect(username, question2, answer2)){
                return ok(
                        views.html.temporarypwd.render(userForm)
                );
            }
        }
        //TODO notify frontend verification fail

        return badRequest(views.html.verifyChangePwdAuth.render(userForm));
    }
    /**
     * For login
     * Verify whether username and password is correct
     * @return To homepage if login success, else stay in login page
     */
    public Result verifyUser(){
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        if(userForm.hasErrors()) {
            System.out.println("ERROR");
            String errorMsg = "";
            java.util.Map<String, List<play.data.validation.ValidationError>> errorsAll = userForm.errors();
            for (String field : errorsAll.keySet()) {
                errorMsg += field + " ";
                for (ValidationError error : errorsAll.get(field)) {
                    errorMsg += error.message() + ", ";
                }
            }
            System.out.println("Please correct the following errors: " + errorMsg);
            return badRequest(views.html.login.render(userForm));
        }

            User new_user = userForm.get();
            String username = new_user.username;
            String password = new_user.password;
            if(new_user.VerifyUser(username, password)){
                System.out.println("User " + username + " login successfully!");

                Session session = Http.Context.current().session();
                Long id = new_user.GetUserID(username);
                session.put("username",username);
                session.put("userid",id.toString());
                return GO_HOME;
            }

            //TODO notify frontend error message

            return badRequest(views.html.login.render(userForm));
    }
    /**
     * Register a user
     */
    public Result addUser() {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        System.out.println("Start inserting");
        if(userForm.hasErrors()) {
            //print error msg
            System.out.println("ERROR");
            String errorMsg = "";
            java.util.Map<String, List<play.data.validation.ValidationError>> errorsAll = userForm.errors();
            for (String field : errorsAll.keySet()) {
                errorMsg += field + " ";
                for (ValidationError error : errorsAll.get(field)) {
                    errorMsg += error.message() + ", ";
                }
            }
            System.out.println("Please correct the following errors: " + errorMsg);
            return badRequest(views.html.register.render(userForm));
        }

        try {
            User new_user = userForm.get();
            String username = new_user.username;
            String password = new_user.password;

            //determine if the username exist (username needs to be unique)
            if(new_user.IfUserExist(username)){
                //if exist
                System.out.println("Username exists!");
                //flash("success", "Username " + username + " existed!");
                //TODO notify frontend with error messgae here

                return badRequest(views.html.register.render(userForm));
            }else{
                //if not exist, add user
                new_user.password = MD5(password);
                new_user.save();

                //the user has been logged in, save username and id to session
                Long id = new_user.GetUserID(username);
                Session session = Http.Context.current().session();
                session.put("username",username);
                session.put("userid",id.toString());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("User " + userForm.get().username + " has been created");

        //TODO should jump to Profile page here
        return GO_HOME;
    }

    /**
     *
     * @param password
     * @return password after encryed
     * @throws Exception
     */
    private static String MD5(String password) throws Exception{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(StandardCharsets.UTF_8.encode(password));
        return String.format("%032x", new BigInteger(1, md.digest()));
    }

    /**
     * Send temporary password for password changing
     * @param email
     * @return
     */
    private static ClientResponse SendSimpleMessage(String email, String tmp_pwd) {
        String name = "customer";
        String SendTo = name + " <" + email + ">";
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", "key-8bcaf224a0a4a59388e4dd33683d61e2"));
        WebResource webResource = client.resource("https://api.mailgun.net/v3/sandboxb3bf5434ac5e4fba8a88fa29a6bc8b74.mailgun.org/messages");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("from", "Mailgun Sandbox <postmaster@sandboxb3bf5434ac5e4fba8a88fa29a6bc8b74.mailgun.org>");
        formData.add("to", SendTo);
        formData.add("subject", "Hello customer");
        formData.add("text", "Dear Sir/Madam, your temporary password is "+tmp_pwd);
        return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                post(ClientResponse.class, formData);
    }

}
