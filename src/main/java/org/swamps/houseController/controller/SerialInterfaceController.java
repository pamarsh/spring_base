package org.swamps.houseController.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swamps.houseController.dto.SerialInterfaceDto;
import org.swamps.houseController.service.SerialInterfaceService;


@RestController
@RequestMapping(value = "/transport/serial")
public class SerialInterfaceController {

    @Autowired
    private SerialInterfaceService serialInterfaceService;

    @RequestMapping(value="{id}")
    public SerialInterfaceDto getSerialInterface (@PathVariable String id) {
        return serialInterfaceService.getSerialInterface(id);
    }

}
