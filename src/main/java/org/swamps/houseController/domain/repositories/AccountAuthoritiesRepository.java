package org.swamps.houseController.domain.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.swamps.houseController.domain.AccountAuthority;

import java.util.List;

@Repository
public interface AccountAuthoritiesRepository extends CrudRepository<AccountAuthority, Integer> {

    public List<AccountAuthority> findByUserId(String userId);

}
