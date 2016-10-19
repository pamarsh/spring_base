package org.swamps.houseController.counters;

/**
 * Created by paul on 8/01/13.
 */
public class AllCounters {

    private String key;
    private long oneMin;
    private long tenMin;
    private long oneHour;
    private long twentyFourHours;

    public String getKey() {
        return key;
    }

    public long getOneMin() {
        return oneMin;
    }

    public long getTenMin() {
        return tenMin;
    }

    public long getOneHour() {
        return oneHour;
    }

    public long getTwentyFourHours() {
        return twentyFourHours;
    }


    public void tick() {
        oneMin++;
    };

    public void updateOneMinut() {

    }
}
