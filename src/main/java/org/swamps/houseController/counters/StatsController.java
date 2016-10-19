package org.swamps.houseController.counters;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {

    @Autowired
    private StatisticsCounterService statsCounterService;

    @RequestMapping(value = "/statistics/all", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AllCounters[] getAllCounters() {
        return statsCounterService.getAllCounters();
    }

}
