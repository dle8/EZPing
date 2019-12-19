package authentication.domain.validator;

import authentication.domain.model.User;
import authentication.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class NewUserValidator implements Validator { // implement Validator Spring interface

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User newUser = (User) target;
        if (userRepository.exists(newUser.getUsername())) { // UserRepository queries the database to check if the username exists
            errors.rejectValue("username", "new.account.username.already.exists"); // add new error to Errors object if username already exists
        }
    }
}