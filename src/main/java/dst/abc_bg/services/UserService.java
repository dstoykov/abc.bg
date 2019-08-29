package dst.abc_bg.services;

import dst.abc_bg.exceptions.PasswordsMismatchException;
import dst.abc_bg.exceptions.UserAlreadyExistsException;
import dst.abc_bg.models.binding.UserRegisterBindingModel;
import dst.abc_bg.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserServiceModel register(UserRegisterBindingModel userBindingModel) throws PasswordsMismatchException, UserAlreadyExistsException;
}
