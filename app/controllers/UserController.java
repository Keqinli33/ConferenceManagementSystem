package controllers;

import models.Profile;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;

import javax.inject.Inject;
import models.User;
import play.mvc.Result;
import play.mvc.Results;

import java.util.*;
import play.data.validation.ValidationError;
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

    public Result login() {
        Form<User> userForm = formFactory.form(User.class);
        return ok(
                views.html.login.render(userForm)
        );
    }

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
            return badRequest(views.html.register.render(userForm));
        }

    }

    public Result addUser() {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        System.out.println("Start inserting");
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
            return badRequest(views.html.register.render(userForm));
        }
        userForm.get().save();
        System.out.println("User " + userForm.get().username + userForm.get().email + " has been created");
        return GO_HOME;
    }
}
