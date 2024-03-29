package dst.abc_bg.areas.email.receive.models.view;

import dst.abc_bg.areas.user.models.view.UserViewModel;
import dst.abc_bg.areas.email.base.models.BaseEmailModel;

public class ReceiveEmailViewModel extends BaseEmailModel {
    private String sender;

    private UserViewModel recipient;

    private Boolean isNew;

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

    public Boolean getNew() {
        return this.isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }
}
