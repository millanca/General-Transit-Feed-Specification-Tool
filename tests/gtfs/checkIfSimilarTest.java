/*
 * SE2030
 * Spring 2019
 * General Transit Feed Specification Tool
 * Name: Brycen Hakes
 * Created: 05/07/2019
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

class checkIfSimilarTest {
    Route route1;
    Route route2;
    Trip trip1;
    Trip trip2;
    Stop firstStop1;
    Stop firstStop2;
    Stop lastStop1;
    Stop lastStop2;
    Stop firstStop3;
    StopOnTrip firstStopOnTrip1;
    StopOnTrip firstStopOnTrip2;
    StopOnTrip lastStopOnTrip1;
    StopOnTrip lastStopOnTrip2;
    Time fATime1;       //First arrival time 1
    Time fDTime1;       //First departure time 1
    Time fATime2;
    Time fDTime2;
    Time lATime1;
    Time lDTime1;
    Time lATime2;
    Time lDTime2;


    @BeforeEach
    void setUp() {
        try {
            firstStop1 = new Stop("firstStop", "first1", "0", "0");
            firstStop2 = new Stop("firstStop", "first2", "0","0");
            lastStop1 = new Stop("lastStop", "last1", "0", "0");
            lastStop2 = new Stop("lastStop", "last2", "0", "0");
            route1 = new Route("route1", "0", "Blue");
            route2 = new Route("route2", "0", "Red");
            trip1 = new Trip("trip1", route1);
            trip2 = new Trip("trip2", route2);
            fATime1 = new Time("01:00:00");
            fDTime1 = new Time("01:30:00");
            fATime2 = new Time("01:00:00");
            fDTime2 = new Time("01:30:00");
            lATime1 = new Time("06:00:00");
            lDTime1 = new Time("06:30:00");
            lATime2 = new Time("06:00:00");
            lDTime2 = new Time("06:30:00");

            firstStopOnTrip1 = new StopOnTrip(firstStop1, fATime1, fDTime1, 1);
            lastStopOnTrip1 = new StopOnTrip(lastStop1, lATime1, lDTime1, 2);
            trip1.addStop(firstStopOnTrip1);
            trip1.addStop(lastStopOnTrip1);

            firstStopOnTrip2 = new StopOnTrip(firstStop2, fATime2, fDTime2, 1);
            lastStopOnTrip2 = new StopOnTrip(lastStop2, lATime2, lDTime2, 2);
            trip2.addStop(firstStopOnTrip2);
            trip2.addStop(lastStopOnTrip2);




        } catch (DataFormatException e){
            System.out.println("Bad data format");
        }

    }

    @Test
    void checkIfSimilar() {
        assertTrue(trip1.checkIfSimilar(trip2));
        trip1.getStopsOnTrip().get(0).getPhysicalStop().setStopId("NonSimilarID");
        assertFalse(trip1.checkIfSimilar(trip2));


    }
}