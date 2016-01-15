package io.pivotal.view;

import io.pivotal.domain.User;
import lombok.Data;

/**
 * Created by pivotal on 1/14/16.
 */
@Data
public class UserMessagePresenter extends JsonPresenter {
    private String message;
    private UserPresenter user;

    public UserMessagePresenter(User user) {
        message = "User created";
        this.user = new UserPresenter(user);
    }
}
