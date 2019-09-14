package dst.abc_bg.areas.user.services;

import dst.abc_bg.areas.user.exceptions.PasswordsMismatchException;
import dst.abc_bg.areas.user.exceptions.UserAlreadyExistsException;
import dst.abc_bg.areas.user.models.binding.UserRegisterBindingModel;
import dst.abc_bg.areas.user.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserServiceModel register(UserRegisterBindingModel userBindingModel) throws PasswordsMismatchException, UserAlreadyExistsException;

    UserServiceModel getUserServiceModelByUsername(String username);
}
