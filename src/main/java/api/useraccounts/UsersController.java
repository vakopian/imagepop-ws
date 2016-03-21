package api.useraccounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by BernardXie on 3/20/16.
 */

@Controller
public class UsersController {
    @Autowired
    private IUserService service;

    private final String API_PATH = "/api/users";

    //Register New User
    @RequestMapping(value = API_PATH, method = RequestMethod.POST)
    public @ResponseBody ModelAndView registerUser(@ModelAttribute("user") @Valid User user,
                              BindingResult result, WebRequest request, Errors errors) {
        User registered = new User();
        if(!result.hasErrors()) {
            registered = createUserAccount(user, result);
        }
        if (registered == null) {
            result.rejectValue("email", "message.regError");
        }
        if (result.hasErrors()) {
            return new ModelAndView("registration", "user", user);
        }
        else {
            return new ModelAndView("successRegister", "user", user);
        }
    }

    private User createUserAccount(User user, BindingResult result) {
        User registered = null;
        try {
            registered = service.registerNewUser(user);
        } catch (EmailExistsException e) {
            return null;
        }
        return registered;
    }
}
