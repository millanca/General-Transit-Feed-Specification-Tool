/*
 * SE2030
 * Spring 2019
 * General Transit Feed Specification Tool
 * Name: Stephen Linn
 * Created: 04/24/2019
 *
 * MIT License
 *
 * Copyright (c) 2019 Brycen Hakes, Samuel Libert, Stephen Linn, Christoper Millan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gtfs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;

class DataSetTest {

    private DataSet dataSet;
    private final static String STOP_ID = "stop101";

    @BeforeEach
    void init(){
        Map<String, Trip> trips = new HashMap<>();
        try{
            trips.put("trip230", (new Trip("trip230", new Route("route10", "0", "Green"))));
            trips.put("trip450", (new Trip("trip450", new Route("route15", "1", "Red"))));
            Route route = new Route("route20", "1", "Blue");
            trips.put("trip550", (new Trip("trip550", route)));
            trips.put("trip670", (new Trip("trip670", route)));
            Trip trip1 = new Trip("trip880" , route);
            Trip trip2 = new Trip("trip990", new Route("route25", "1", "Gold"));
            Stop sameStop = new Stop(STOP_ID, "MainSt", "50", "60");
            StopOnTrip sameStopOnTrip = new StopOnTrip(sameStop,
                    new Time("10:30:00"),
                    new Time("11:30:00"),
                    10);
            sameStopOnTrip.setPhysicalStop(sameStop);
            trip1.addStop(sameStopOnTrip);
            trip2.addStop(sameStopOnTrip);
            trips.put(trip1.getTripId(), trip1);
            trips.put(trip2.getTripId(), trip2);
        } catch (DataFormatException e){
            e.printStackTrace();
        }
        Map<String, Route> routes = new HashMap<>();
        Map<String, Stop> stops = new HashMap<>();
        trips.forEach((k, v) -> routes.put(v.getParentRoute().getRouteId(),v.getParentRoute()));
        dataSet = new DataSet(trips, routes, stops);
    }

    /**
     * Verify functionality of numTripsWithStop for both non-existent stops and existing stops
     */
    @Test
    void numTripsWithStop() {

        // Non-existent stop
        assertEquals(dataSet.numTripsWithStop("stop1"), 0);

        // Existing stop
        assertEquals(dataSet.numTripsWithStop(STOP_ID), 2);

    }

    /**
     * Verify functionality of routesWithStop for non-existent stop and one that should be on routes.
     */
    @Test
    void routesWithStop() {

        // Non-existent stop -- the array returned should be empty
        assertArrayEquals(dataSet.routesWithStop("fakeStop").toArray(new Route[0]), new Route[0]);

        // Existing stop -- the size of the route array should be greater than 0
        assertTrue(dataSet.routesWithStop(STOP_ID).size() > 0);

    }

    @Test
    void sortByTime() {
    }

    @Test
    void stopsOnRoute() {
    }

    @Test
    void tripUpdateParentRoute() {

        // Initial code
        Map<String, Trip> trips = new HashMap<>();
        try{
            Route route = new Route("route20", "1", "Blue");

            Trip trip1 = new Trip("trip880" , route);
            Trip trip2 = new Trip("trip990", new Route("route25", "1", "Gold"));
            Stop sameStop = new Stop(STOP_ID, "MainSt", "50", "60");
            StopOnTrip sameStopOnTrip = new StopOnTrip(sameStop,
                    new Time("10:30:00"),
                    new Time("11:30:00"),
                    10);
            sameStopOnTrip.setPhysicalStop(sameStop);
            trip1.addStop(sameStopOnTrip);
            trip2.addStop(sameStopOnTrip);
            trips.put(trip1.getTripId(), trip1);
            trips.put(trip2.getTripId(), trip2);
        } catch (DataFormatException e){
            e.printStackTrace();
        }

        dataSet.setTripMap(trips);

        Trip trip = dataSet.getTripFromId("trip990");
        Route oldRoute = trip.getParentRoute();

        // Verify exception for missing trip
        assertThrows(IllegalArgumentException.class,()-> dataSet.tripUpdateParentRoute("missingtrip", "route1"));

        // Verify exception for missing route
        assertThrows(IllegalArgumentException.class,()-> dataSet.tripUpdateParentRoute("trip990", "missingroute"));

        // Update routes
        dataSet.tripUpdateParentRoute("trip990", dataSet.getTripFromId("trip880").getParentRoute().getRouteId());

        // Verify correct tripId and valid route creates change
        assertNotEquals(oldRoute, dataSet.getTripFromId("trip990").getParentRoute());

        // Verify correct trips updated
        Trip compareTrip = dataSet.getTripFromId("trip880");
        assertEquals(trip.getParentRoute(), compareTrip.getParentRoute());
    }

    @Test
    void tripUpdateStopTimes() {
    }
}