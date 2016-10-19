package org.swamps.houseController.x10;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swamps.houseController.domain.X10ControllerDao;
import org.swamps.houseController.domain.repositories.X10ControllerRepository;
import org.swamps.houseController.controller.InvalidChangeRequestException;
import org.swamps.houseController.controller.ResourceDoesNotExistException;
import org.swamps.houseController.dto.X10ControllerMessage;

import java.util.ArrayList;
import java.util.List;

@Service
public class X10Service {

    @Autowired
    private X10ControllerRepository x10ControllerRepository;

    private List<X10ControllerMessage> allControllers;

    public void addController(X10ControllerMessage message) {
        x10ControllerRepository.save(new X10ControllerDao(message.getName(), message.getCommunicationAdapter()));
    }

    public List<X10ControllerMessage> getAllControllers() {
        List<X10ControllerMessage> x10Controllers = new ArrayList<>();
        for (X10ControllerDao x10ControllerDao : x10ControllerRepository.findAll()) {
            x10Controllers.add(new X10ControllerMessage(x10ControllerDao.getName(),x10ControllerDao.getCommunicationsAdapter()));
        }
        return x10Controllers;
    }

    public X10ControllerMessage getByUserName(String controllerName) {
        final X10ControllerDao storedControllerDetails = x10ControllerRepository.findByName(controllerName);
        return new X10ControllerMessage(storedControllerDetails.getName(), storedControllerDetails.getCommunicationsAdapter());
    }

    public void deleteController(String controllerName) {
        x10ControllerRepository.delete(x10ControllerRepository.findByName(controllerName));
    }

    public void updateController(String controllerName, X10ControllerMessage message) throws ResourceDoesNotExistException, InvalidChangeRequestException {
        final X10ControllerDao currentController = x10ControllerRepository.findByName(controllerName);
        if (currentController == null) {
            throw new ResourceDoesNotExistException() ;
        }
        if (message.getName().equals(currentController.getName())) {
            X10ControllerDao updatedController = new X10ControllerDao(controllerName, message.getCommunicationAdapter());
            x10ControllerRepository.save(updatedController);
            return;
        }
        throw new InvalidChangeRequestException("controller name can not be changed");
    }


}
