package org.swamps.houseController.counters;

import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;


@Service
public class StatisticsCounterService {

    private Map<String, AllCounters> counters = new HashMap<String, AllCounters>();

    public AllCounters[] getAllCounters() {
        return (AllCounters[]) counters.values().toArray(new AllCounters[0]);
    }

    public void tick(String counterKey) {
        AllCounters counter = counters.get(counterKey);
        if ( counter == null ) {
            counter = new AllCounters();
            counters.put(counterKey,counter);
        }
        counter.tick();
    }
}
