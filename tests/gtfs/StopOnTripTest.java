/*
 * SE2030
 * Spring 2019
 * General Transit Feed Specification Tool
 * Name: Stephen Linn
 * Created: 05/03/2019
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

import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;

class StopOnTripTest {

    // Class instance
    private StopOnTrip stopOnTrip;

    // Class fields
    private final String arrivalTimeString = "06:00:00";
    private final Time arrivalTime = new Time(arrivalTimeString);

    private final String departureTimeString = "06:02:00";
    private final Time departureTime = new Time(departureTimeString);

    private final String stopId = "Stop1";
    private final String stopName = "33rd & King";
    private final String stopLat = "43.0";
    private final String stopLon = "-87.9";
    private Stop physicalStop;

    private final int stopSequence = 1;

    @BeforeEach
    void init(){

        try{
            physicalStop = new Stop(stopId, stopName, stopLat, stopLon);
            stopOnTrip = new StopOnTrip(physicalStop, arrivalTime, departureTime, stopSequence);
        } catch(DataFormatException e){
            e.printStackTrace();
        }

    }

    @Test
    void stopOnTrip(){

        // invalid stop
        assertThrows(DataFormatException.class, ()->
        {stopOnTrip = new StopOnTrip(null, arrivalTime, departureTime, stopSequence);});

        // invalid arrival (or departure) time
        assertThrows(DataFormatException.class, ()->
        {stopOnTrip = new StopOnTrip(new Stop(stopId, stopName, stopLat, stopLon), null, departureTime, stopSequence);});

        // depart before arrive
        assertThrows(DataFormatException.class, ()->
        {stopOnTrip = new StopOnTrip(new Stop(stopId, stopName, stopLat, stopLon), departureTime, arrivalTime, stopSequence);});

        // negative stop sequence
        assertThrows(DataFormatException.class, ()->
        {stopOnTrip = new StopOnTrip(new Stop(stopId, stopName, stopLat, stopLon), arrivalTime, departureTime, -1);});
    }

    @Test
    void getArrivalTime() {
        assertEquals(arrivalTime, stopOnTrip.getArrivalTime());
    }

    @Test
    void getDepartureTime() {
        assertEquals(departureTime, stopOnTrip.getDepartureTime());
    }

    @Test
    void getPhysicalStop() {
        assertEquals(physicalStop, stopOnTrip.getPhysicalStop());
    }

    @Test
    void getStopSequence() {
        assertEquals(stopSequence, stopOnTrip.getStopSequence());
    }

    @Test
    void setArrivalTime() {
        // set time
        final Time newArrivalTime = new Time("07:00:00");
        stopOnTrip.setArrivalTime(newArrivalTime);

        // verify change

        // assert equal to new
        assertEquals(newArrivalTime, stopOnTrip.getArrivalTime());

        //assert different from old
        assertNotEquals(arrivalTime, stopOnTrip.getArrivalTime());
    }

    @Test
    void setDepartureTime() {
        // set time
        final Time newDepartureTime = new Time("07:02:00");
        stopOnTrip.setDepartureTime(newDepartureTime);

        // verify change

        // assert equal to new
        assertEquals(newDepartureTime, stopOnTrip.getDepartureTime());

        //assert different from old
        assertNotEquals(departureTime, stopOnTrip.getDepartureTime());
    }

    @Test
    void setPhysicalStop() {

        // initial values for new Stop
        final String newStopId = "Stop2";
        final String newStopName = "Queen & Mother";
        final String newStopLat = "53.0";
        final String newStopLon = "-77.9";

        try{

            // set stop
            Stop newPhysicalStop = new Stop(newStopId, newStopName, newStopLat, newStopLon);
            stopOnTrip.setPhysicalStop(newPhysicalStop);

            // verify change

            // assert equal to new
            assertEquals(newPhysicalStop, stopOnTrip.getPhysicalStop());

            // assert different from old
            assertNotEquals(physicalStop, stopOnTrip.getPhysicalStop());

        } catch(DataFormatException e){
            e.printStackTrace();
        }

    }

    @Test
    void setStopSequence() {

        // set stop sequence
        final int newStopSequence = stopSequence + 1;
        stopOnTrip.setStopSequence(newStopSequence);

        // verify change

        //assert equal to new
        assertEquals(newStopSequence, stopOnTrip.getStopSequence());

        //assert different from old
        assertNotEquals(stopSequence, stopOnTrip.getStopSequence());
    }
}