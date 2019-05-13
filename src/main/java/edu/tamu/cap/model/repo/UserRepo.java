package edu.tamu.cap.model.repo;

import java.util.List;

import org.springframework.stereotype.Repository;

import edu.tamu.cap.model.User;
import edu.tamu.cap.model.repo.custom.UserRepoCustom;
import edu.tamu.weaver.auth.model.repo.AbstractWeaverUserRepo;

/**
 * User repository.
 *
 * @author
 *
 */
@Repository
public interface UserRepo extends AbstractWeaverUserRepo<User>, UserRepoCustom {

    public User findByEmail(String email);

    public List<User> findByRole(int roleId);

}
