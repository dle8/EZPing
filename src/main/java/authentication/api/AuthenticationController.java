package authentication.api;

import authentication.domain.model.User;
import authentication.domain.service.UserService;
import authentication.domain.validator.NewUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private NewUserValidator newUserValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(newUserValidator);
    }

    @RequestMapping(path = "/new-account", method = RequestMethod.POST)
    public String createAccount(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "new-account";
        }
        userService.createUser(user);
        return "redirect:/";
    }
}