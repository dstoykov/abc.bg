package dst.abc_bg.models.service;

import dst.abc_bg.models.base.BaseEmailModel;

public class ReceiveEmailServiceModel extends BaseEmailModel {
    private String sender;

    private UserServiceModel recipient;

    public ReceiveEmailServiceModel() {
        super();
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public UserServiceModel getRecipient() {
        return this.recipient;
    }

    public void setRecipient(UserServiceModel recipient) {
        this.recipient = recipient;
    }
}

