/*
 * Course: SE2030 - 011
 * Spring 2019
 * General Transit Feed Specification Tool
 * Name: Brycen Hakes
 * Created: 4/24/2019
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

class TripTest {
    Trip trip1;
    Route route1;
    Route route2;


    @BeforeEach
    void setUp(){
        try {
            route1 = new Route("routeID001", "0", "Blue");
            route2 = new Route("routeID002", "1", "Red");
            trip1 = new Trip("tripID001", route1);
        }catch(DataFormatException e){
            e.printStackTrace();
        }
    }

    @Test
    void setParentRoute() {
        trip1.setParentRoute(route2);
        assertTrue(trip1.getParentRoute()==route2);
    }

    @Test
    void addStop() throws DataFormatException {
        Stop stop1 = new Stop("stopID001", "Water St Stop", "0","0");
        Time arrivalTime = new Time("5:00:00");
        Time departureTime = new Time("5:30:00");
        StopOnTrip stopOnTrip1 = new StopOnTrip(stop1, arrivalTime, departureTime, 1);
        trip1.addStop(stopOnTrip1);
        assertSame(stopOnTrip1,trip1.getStopsOnTrip().get(0));
    }
}