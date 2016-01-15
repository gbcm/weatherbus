package io.pivotal.view;

import io.pivotal.domain.User;
import lombok.Data;

/**
 * Created by pivotal on 1/14/16.
 */
@Data
public class UserPresenter extends JsonPresenter {
    private String username;
    private long id;

    public UserPresenter(User user) {
        username = user.getUsername();
        id = user.getId();
    }
}
