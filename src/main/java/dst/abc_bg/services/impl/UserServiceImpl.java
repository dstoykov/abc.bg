package dst.abc_bg.services.impl;

import dst.abc_bg.entities.Role;
import dst.abc_bg.entities.User;
import dst.abc_bg.exceptions.PasswordsMismatchException;
import dst.abc_bg.exceptions.UserAlreadyExistsException;
import dst.abc_bg.models.binding.UserRegisterBindingModel;
import dst.abc_bg.models.service.UserServiceModel;
import dst.abc_bg.repositories.UserRepository;
import dst.abc_bg.services.RoleService;
import dst.abc_bg.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final String PASSWORD_MISMATCH_EXCEPTION_MSG = "Passwords mismatch!";
    private static final String USER_ALREADY_EXIST_EXCEPTION_MSG = "User with the sam email already exists!";

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

    private void checkPasswords(UserRegisterBindingModel bindingModel, User user) throws PasswordsMismatchException {
        if (comparePasswords(bindingModel.getPassword(), bindingModel.getConfirmPassword())) {
            user.setPassword(this.encoder.encode(bindingModel.getPassword()));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findUserByUsernameAndDeletedOnNull(username);
    }

    @Override
    public UserServiceModel register(UserRegisterBindingModel bindingModel) throws PasswordsMismatchException, UserAlreadyExistsException {
        this.checkUsername(bindingModel);
        User user = this.mapper.map(bindingModel, User.class);
        this.checkPasswords(bindingModel, user);

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        Role role = this.roleService.getUserRole();
        user.addRole(role);
        this.userRepository.save(user);
        this.addUserToRole(user, role);

        return this.mapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel getUserServiceModelByUsername(String username) {
        UserServiceModel serviceModel = this.mapper
                .map(this.userRepository.findUserByUsernameAndDeletedOnNull(username), UserServiceModel.class);

        return serviceModel;
    }
}
