package com.imagepop.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

/**
 * Created by BernardXie on 3/20/16.
 */

@Controller
public class UsersController {
    @Autowired
    protected IUserService service;

    private final String API_PATH = "/api/users";

    //Register New User
    @RequestMapping(value = API_PATH, method = RequestMethod.POST)
    public @ResponseBody String registerUser(@RequestBody User userInfo,
                                             BindingResult result, WebRequest request, Errors errors) {
        User registered = new User();
        if(!result.hasErrors()) {
            try {
                registered = service.registerNewUser(userInfo);
            } catch (EmailExistsException e) {
                return "registrationform";
            }
        }
        if (registered == null) {
            result.rejectValue("email", "message.regError");
        }
        if (result.hasErrors()) {
            return "registrationform";
        }
        else {
            return "registrationsuccess";
        }
    }
}
