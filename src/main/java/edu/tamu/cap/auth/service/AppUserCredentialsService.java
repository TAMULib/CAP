package edu.tamu.cap.auth.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.tamu.cap.model.Role;
import edu.tamu.cap.model.User;
import edu.tamu.cap.model.repo.UserRepo;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.auth.service.UserCredentialsService;

@Service
public class AppUserCredentialsService extends UserCredentialsService<User, UserRepo> {
    private final static String SHIB_KEY = "shibboleth";

    @Value("#{'${authenticationStrategies}'.split(',')}")
    private List<String> authenticationStrategies;

    private boolean shibEnabled = false;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public synchronized User updateUserByCredentials(Credentials credentials) {
        User user = null;
        if (isShibEnabled()) {
            user = updateShibUser(credentials);
        } else {
            user = updateEmailUser(credentials);
        }

        credentials.setRole(user.getRole().toString());
        credentials.setUin(user.getUsername());

        return user;

	}

	protected User updateEmailUser(Credentials credentials) {
        Optional<User> optionalUser = userRepo.findByUsername(credentials.getEmail());
        User user = null;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();

            boolean changed = false;

            if (credentials.getEmail() != user.getUsername()) {
                user.setUsername(credentials.getEmail());
                changed = true;
            }

            if (credentials.getFirstName() != user.getFirstName()) {
                user.setFirstName(credentials.getFirstName());
                changed = true;
            }

            if (credentials.getLastName() != user.getLastName()) {
                user.setLastName(credentials.getLastName());
                changed = true;
            }

            if (credentials.getRole() == null) {
                user.setRole(getDefaultRole(credentials));
                changed = true;
            }

            if (changed) {
                user = userRepo.save(user);
            }
        }

	    return user;
	}

	protected User updateShibUser(Credentials credentials) {
        Optional<User> optionalUser = userRepo.findByUsername(credentials.getUin());

        User user = null;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();

            boolean changed = false;

            if (credentials.getUin() != user.getUsername()) {
                user.setUsername(credentials.getUin());
                changed = true;
            }

            if (credentials.getFirstName() != user.getFirstName()) {
                user.setFirstName(credentials.getFirstName());
                changed = true;
            }

            if (credentials.getLastName() != user.getLastName()) {
                user.setLastName(credentials.getLastName());
                changed = true;
            }

            if (credentials.getRole() == null) {
                user.setRole(getDefaultRole(credentials));
                changed = true;
            }

            if (changed) {
                user = userRepo.save(user);
            }
        } else {
            user = userRepo.create(credentials.getUin(), credentials.getFirstName(), credentials.getLastName(), getDefaultRole(credentials).toString());
        }

        return user;

	}


    public User createUserFromRegistration(String email, String firstName, String lastName, String password) {
        Role role = Role.ROLE_USER;
        for (String adminEmail : admins) {
            if (adminEmail.equals(email)) {
                role = Role.ROLE_ADMIN;
                break;
            }
        }
        return userRepo.create(email, firstName, lastName, role.toString(), password);
    }

	@Override
	public String getAnonymousRole() {
		return Role.ROLE_ANONYMOUS.toString();
	}

    private synchronized Role getDefaultRole(Credentials credentials) {
        Role role = Role.ROLE_USER;

        if (credentials.getRole() == null) {
            credentials.setRole(role.toString());
        }

        String shibUin = credentials.getUin();

        for (String uin : admins) {
            if (uin.equals(shibUin)) {
                role = Role.ROLE_ADMIN;
                credentials.setRole(role.toString());
            }
        }

        return role;
    }

    protected boolean isShibEnabled() {
        return shibEnabled;
    }

    @PostConstruct
    protected void setShibEnabled() {
        for (String strategy:authenticationStrategies) {
            if (strategy.equals(SHIB_KEY)) {
                shibEnabled = true;
                break;
            }
        }
    }

}
