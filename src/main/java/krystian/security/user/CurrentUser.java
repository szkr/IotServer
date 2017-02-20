package krystian.security.user;

import org.springframework.security.core.authority.AuthorityUtils;

import java.io.Serializable;

public class CurrentUser extends org.springframework.security.core.userdetails.User implements Serializable {

    private User user;

    public CurrentUser(User user) {
        super(user.getLogin(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();

    }

    public Role getRole() {
        return user.getRole();
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "user=" + user +
                "} " + super.toString();
    }
}
