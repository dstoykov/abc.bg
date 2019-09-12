package dst.abc_bg.models.binding;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class SendEmailNewBindingModel {
    private static final String DEFAULT_NOT_EMPTY_MSG = "Field must not be empty. ";
    private static final String EMAIL_PATTERN_REGEXP = "^[a-zA-Z0-9._-]+@[a-z0-9-]+(\\.[a-z]{2,4}){1,3}$";
    private static final String EMAIL_PATTERN_MSG = "Invalid email format. ";
    private static final String CONTENT_MAX_LENGTH_MSG = "Message must be less tha 5000 symbols long.";

    @NotEmpty(message = DEFAULT_NOT_EMPTY_MSG)
    @Pattern(regexp = EMAIL_PATTERN_REGEXP, message = EMAIL_PATTERN_MSG)
    private String recipient;

    @NotEmpty(message = DEFAULT_NOT_EMPTY_MSG)
    private String subject;

    @NotEmpty(message = DEFAULT_NOT_EMPTY_MSG)
    @Length(max = 5000, message = CONTENT_MAX_LENGTH_MSG)
    private String content;

    public SendEmailNewBindingModel() {
    }

    public String getRecipient() {
        return this.recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
