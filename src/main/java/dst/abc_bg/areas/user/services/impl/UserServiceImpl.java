package dst.abc_bg.areas.user.services.impl;

import dst.abc_bg.areas.role.entities.Role;
import dst.abc_bg.areas.user.entities.User;
import dst.abc_bg.areas.user.exceptions.PasswordsMismatchException;
import dst.abc_bg.areas.user.exceptions.UserAlreadyBannedException;
import dst.abc_bg.areas.user.exceptions.UserAlreadyExistsException;
import dst.abc_bg.areas.user.exceptions.UserNotBannedException;
import dst.abc_bg.areas.user.models.binding.UserEditDataBindingModel;
import dst.abc_bg.areas.user.models.binding.UserEditPasswordBindingModel;
import dst.abc_bg.areas.user.models.binding.UserRegisterBindingModel;
import dst.abc_bg.areas.user.models.service.UserServiceModel;
import dst.abc_bg.areas.user.models.view.UserViewModel;
import dst.abc_bg.areas.user.repositories.UserRepository;
import dst.abc_bg.areas.role.services.RoleService;
import dst.abc_bg.areas.user.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final String PASSWORD_MISMATCH_EXCEPTION_MSG = "Passwords mismatch!";
    private static final String USER_ALREADY_EXIST_EXCEPTION_MSG = "User with the sam email already exists!";
    private static final String ADMIN_USERNAME = "admin";

    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(ModelMapper mapper, UserRepository userRepository, RoleService roleService, BCryptPasswordEncoder encoder) {
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    private boolean comparePasswords(String password, String confirmPassword) throws PasswordsMismatchException {
        if (!password.equals(confirmPassword)) {
            throw new PasswordsMismatchException(PASSWORD_MISMATCH_EXCEPTION_MSG);
        }

        return true;
    }

    private void checkUsername(UserRegisterBindingModel bindingModel) throws UserAlreadyExistsException {
        if (this.userRepository.findByUsernameEquals(bindingModel.getUsername()) != null) {
            throw new UserAlreadyExistsException(USER_ALREADY_EXIST_EXCEPTION_MSG);
        }
    }

    private void addUserToRole(User user, Role role) {
        role.getUsers().add(user);
        this.roleService.save(role);
    }

    private void checkAndSavePasswords(UserRegisterBindingModel bindingModel, User user) throws PasswordsMismatchException {
        if (comparePasswords(bindingModel.getPassword(), bindingModel.getConfirmPassword())) {
            user.setPassword(this.encoder.encode(bindingModel.getPassword()));
        }
    }

    private Set<UserViewModel> getSetOfUserViewModel(Set<User> users) {
        Set<UserViewModel> userViewModels = new LinkedHashSet<>();
        for (User user : users) {
            userViewModels.add(this.mapper.map(user, UserViewModel.class));
        }

        return userViewModels;
    }

    private boolean saveUserToDb(User user) {
        this.userRepository.save(user);
        return true;
    }

    private boolean checkIfUserBanned(User user) throws UserAlreadyBannedException {
        if (user == null) {
            throw new UserAlreadyBannedException("User is already banned");
        }
        return true;
    }

    private boolean checkIfUserNotBanned(User user) throws UserNotBannedException {
        if (user == null) {
            throw new UserNotBannedException("User is not banned");
        }
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findUserByUsernameAndDeletedOnNull(username);
    }

    @Override
    public UserServiceModel register(UserRegisterBindingModel bindingModel) throws PasswordsMismatchException, UserAlreadyExistsException {
        this.checkUsername(bindingModel);
        User user = this.mapper.map(bindingModel, User.class);
        this.checkAndSavePasswords(bindingModel, user);

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        Role role = this.roleService.getUserRole();
        user.addRole(role);
        this.saveUserToDb(user);
        this.addUserToRole(user, role);

        return this.mapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel getNonDeletedUserServiceModelByUsername(String username) throws UserAlreadyBannedException {
        User user = this.userRepository.findUserByUsernameAndDeletedOnNull(username);
        if (user == null) {
            throw new UserAlreadyBannedException("User was already banned");
        }

        return this.mapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel getDeletedUserByUsername(String username) throws UserNotBannedException {
        User user = this.userRepository.findByUsernameAndDeletedOnNotNull(username);
        if (user == null) {
            throw new UserNotBannedException("User wasn't banned");
        }
        return this.mapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel getUserServiceModelByUsername(String username) {
        UserServiceModel serviceModel = this.mapper
                .map(this.userRepository.findByUsernameEquals(username), UserServiceModel.class);

        return serviceModel;
    }

    @Override
    public UserServiceModel editUserDataByUsername(UserEditDataBindingModel userEditDataBindingModel, String username) {
        User user = this.userRepository.findByUsernameEquals(username);
        user.setFirstName(userEditDataBindingModel.getFirstName());
        user.setLastName(userEditDataBindingModel.getLastName());

        this.saveUserToDb(user);

        return this.mapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel editUserPasswordByUsername(UserEditPasswordBindingModel userEditPasswordBindingModel, String username) throws PasswordsMismatchException {
        User user = this.userRepository.findByUsernameEquals(username);
        this.comparePasswords(userEditPasswordBindingModel.getPassword(), userEditPasswordBindingModel.getConfirmPassword());
        user.setPassword(this.encoder.encode(userEditPasswordBindingModel.getPassword()));

        this.saveUserToDb(user);

        return this.mapper.map(user, UserServiceModel.class);
    }

    @Override
    public Set<UserViewModel> getListWithViewModels() {
        Set<User> users = this.userRepository.getAllByUsernameNotOrderByDeletedOn(ADMIN_USERNAME);
        Set<UserViewModel> userViewModels = this.getSetOfUserViewModel(users);

        return userViewModels;
    }

    @Override
    public UserServiceModel banUser(String username) throws UserAlreadyBannedException {
        User user = this.userRepository.findUserByUsernameAndDeletedOnNull(username);
        this.checkIfUserBanned(user);
        user.setDeletedOn(LocalDate.now());
        this.saveUserToDb(user);

        return this.mapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel restoreUser(String username) throws UserNotBannedException {
        User user = this.userRepository.findByUsernameAndDeletedOnNotNull(username);
        this.checkIfUserNotBanned(user);
        user.setDeletedOn(null);
        this.saveUserToDb(user);

        return this.mapper.map(user, UserServiceModel.class);
    }
}
