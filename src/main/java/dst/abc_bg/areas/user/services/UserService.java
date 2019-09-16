package dst.abc_bg.areas.user.services;

import dst.abc_bg.areas.user.exceptions.PasswordsMismatchException;
import dst.abc_bg.areas.user.exceptions.UserAlreadyBannedException;
import dst.abc_bg.areas.user.exceptions.UserAlreadyExistsException;
import dst.abc_bg.areas.user.exceptions.UserNotBannedException;
import dst.abc_bg.areas.user.models.binding.UserEditDataBindingModel;
import dst.abc_bg.areas.user.models.binding.UserEditPasswordBindingModel;
import dst.abc_bg.areas.user.models.binding.UserRegisterBindingModel;
import dst.abc_bg.areas.user.models.service.UserServiceModel;
import dst.abc_bg.areas.user.models.view.UserViewModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

public interface UserService extends UserDetailsService {
    UserServiceModel register(UserRegisterBindingModel userBindingModel) throws PasswordsMismatchException, UserAlreadyExistsException;

    UserServiceModel getNonDeletedUserServiceModelByUsername(String username) throws UserAlreadyBannedException;

    UserServiceModel getUserServiceModelByUsername(String username);

    UserServiceModel editUserDataByUsername(UserEditDataBindingModel userEditDataBindingModel, String username);

    UserServiceModel editUserPasswordByUsername(UserEditPasswordBindingModel userEditDataBindingModel, String username) throws PasswordsMismatchException;

    Set<UserViewModel> getListWithViewModels();

    UserServiceModel banUser(String username) throws UserAlreadyBannedException;

    UserServiceModel restoreUser(String username) throws UserNotBannedException;

    UserServiceModel getDeletedUserByUsername(String username) throws UserNotBannedException;
}
