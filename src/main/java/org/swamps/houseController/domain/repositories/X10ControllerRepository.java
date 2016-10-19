package org.swamps.houseController.domain.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.swamps.houseController.domain.X10ControllerDao;

@Repository
public interface X10ControllerRepository extends CrudRepository<X10ControllerDao,Integer> {

    X10ControllerDao findByName(String controllerName);
}
