package dst.abc_bg.models.service;

import dst.abc_bg.models.base.BaseEmailModel;

public class SendEmailServiceModel extends BaseEmailModel {
    private UserServiceModel sender;

    private String recipient;

    public SendEmailServiceModel() {
        super();
    }

    public UserServiceModel getSender() {
        return this.sender;
    }

    public void setSender(UserServiceModel sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
