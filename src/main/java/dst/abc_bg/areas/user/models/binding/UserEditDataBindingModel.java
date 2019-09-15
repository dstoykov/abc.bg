package dst.abc_bg.areas.user.models.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserEditDataBindingModel {
    private static final String DEFAULT_NOT_EMPTY_MSG = "Field name must not be empty. ";
    private static final String NAME_PATTERN_MSG = "Incorrect name format. ";
    private static final String NAME_PATTERN_REGEXP = "^[a-zA-Z]+$";

    @NotEmpty(message = DEFAULT_NOT_EMPTY_MSG)
    @Pattern(regexp = NAME_PATTERN_REGEXP, message = NAME_PATTERN_MSG)
    private String firstName;

    @NotEmpty(message = DEFAULT_NOT_EMPTY_MSG)
    @Pattern(regexp = NAME_PATTERN_REGEXP, message = NAME_PATTERN_MSG)
    private String lastName;

    public UserEditDataBindingModel() {
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
}
