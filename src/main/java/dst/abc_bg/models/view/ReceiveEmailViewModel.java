package dst.abc_bg.models.view;

import dst.abc_bg.models.base.BaseEmailModel;

public class ReceiveEmailViewModel extends BaseEmailModel {
    private String sender;

    private UserViewModel recipient;

    public ReceiveEmailViewModel() {
        super();
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public UserViewModel getRecipient() {
        return this.recipient;
    }

    public void setRecipient(UserViewModel recipient) {
        this.recipient = recipient;
    }
}
