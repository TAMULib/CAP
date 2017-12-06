package edu.tamu.cap.auth.model;

import edu.tamu.cap.model.User;

public class CustomUserDetails extends User {

    private static final long serialVersionUID = -7785681787331883261L;

    public CustomUserDetails(User user) {
        super(user);
    }

}
