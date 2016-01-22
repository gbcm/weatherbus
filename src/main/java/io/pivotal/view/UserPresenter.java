package io.pivotal.view;

import io.pivotal.domain.User;
import lombok.Data;

@Data
public class UserPresenter extends JsonPresenter {
    private String username;

    public UserPresenter(User user) {
        this.username = user.getUsername();
    }
}
