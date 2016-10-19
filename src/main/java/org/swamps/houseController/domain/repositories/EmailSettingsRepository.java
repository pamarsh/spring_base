package org.swamps.houseController.domain.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.swamps.houseController.domain.EmailServer;


@Repository
public interface EmailSettingsRepository extends CrudRepository<EmailServer, Integer> {

    public EmailServer findByServerName(String serverName);

    public EmailServer save(EmailServer emailServer) ;
}
