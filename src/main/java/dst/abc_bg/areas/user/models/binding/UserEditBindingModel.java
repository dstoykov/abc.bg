package dst.abc_bg.areas.user.models.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserEditBindingModel {
    private static final String DEFAULT_NOT_EMPTY_MSG = "Field name must not be empty. ";
    private static final String NAME_PATTERN_MSG = "Incorrect name format. ";
    private static final String NAME_PATTERN_REGEXP = "^[a-zA-Z]+$";
    private static final int PASSWORD_SIZE_MIN = 8;
    private static final int PASSWORD_SIZE_MAX = 20;
    private static final String PASSWORD_SIZE_MSG = "Password length must be between 8 and 20 symbols. ";

    private String id;

    @NotEmpty(message = DEFAULT_NOT_EMPTY_MSG)
    @Pattern(regexp = NAME_PATTERN_REGEXP, message = NAME_PATTERN_MSG)
    private String firstName;

    @NotEmpty(message = DEFAULT_NOT_EMPTY_MSG)
    @Pattern(regexp = NAME_PATTERN_REGEXP, message = NAME_PATTERN_MSG)
    private String lastName;

    @NotEmpty(message = DEFAULT_NOT_EMPTY_MSG)
    @Size(min = PASSWORD_SIZE_MIN, max = PASSWORD_SIZE_MAX, message = PASSWORD_SIZE_MSG)
    private String password;

    @NotEmpty(message = DEFAULT_NOT_EMPTY_MSG)
    private String confirmPassword;

    public UserEditBindingModel() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
