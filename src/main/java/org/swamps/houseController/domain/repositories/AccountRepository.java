package org.swamps.houseController.domain.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.swamps.houseController.domain.UserAccount;

@Repository
public interface AccountRepository extends CrudRepository<UserAccount, Integer> {

    public UserAccount findByUserId(String userId);

    public UserAccount save(UserAccount userAccount) ;

}
