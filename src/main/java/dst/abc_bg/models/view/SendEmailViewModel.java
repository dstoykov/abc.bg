package dst.abc_bg.models.view;

import dst.abc_bg.models.base.BaseEmailModel;

public class SendEmailViewModel extends BaseEmailModel {
    private UserViewModel sender;

    private String recipient;

    public SendEmailViewModel() {
        super();
    }

    public UserViewModel getSender() {
        return this.sender;
    }

    public void setSender(UserViewModel sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
