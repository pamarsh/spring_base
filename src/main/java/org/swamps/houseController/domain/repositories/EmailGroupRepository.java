package org.swamps.houseController.domain.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.swamps.houseController.domain.EmailGroup;

@Repository
public interface EmailGroupRepository extends CrudRepository<EmailGroup,Integer> {

    EmailGroup getByName(String emailGroup);


}
