package com.imagepop.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by BernardXie on 3/20/16.
 */

@Controller
public class UserController {
    @Autowired
    protected UserService service;

    //Register New User
    @CrossOrigin
    @RequestMapping(value = "/api/users/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void registerUser(@RequestBody User user) {
        service.registerNewUser(user);
    }
}
