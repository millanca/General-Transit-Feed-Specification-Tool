/*
 * SE2030
 * Spring 2019
 * General Transit Feed Specification Tool
 * Name: Samuel Libert
 * Created: 04/25/2019
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

public class StopTest {

    Stop st;
    Time arrivalTime;
    Time departureTime;
    int stopSequence;

    @BeforeEach
    void setUp() throws DataFormatException{
        st = new Stop("STP1","Stop One", "4.35", "-2");
        arrivalTime = new Time("01:30:00");
        departureTime = new Time("02:30:00");
        stopSequence = 5;
    }

    @Test
    void makeStop()throws DataFormatException {
        assertEquals(st.getStopId(),"STP1");
        assertEquals(st.getStopName(),"Stop One");
        assertEquals(st.getStopLat(), 4.35);
        assertEquals(st.getStopLon(), -2);
    }
    @Test
    void useInvalidCoordinates(){
        assertThrows(DataFormatException.class, ()->{st = new Stop("STP2","Stop Two", "a", "mistake");});
    }

    @Test
    void makeStopLocationOutOfRange(){
        assertThrows(DataFormatException.class, ()->{st = new Stop("STP2","Stop Two", "160", "100");});
    }

    @Test
    void makeStopOnTrip() throws DataFormatException {
        Time arrivalTime = new Time("01:30:00");
        Time departureTime = new Time("02:30:00");

        StopOnTrip stopWithTrip = new StopOnTrip(st,arrivalTime, departureTime, stopSequence);
        assertEquals(stopWithTrip.getPhysicalStop(),st);
        assertEquals(stopWithTrip.getArrivalTime(), arrivalTime);
        assertEquals(stopWithTrip.getDepartureTime(), departureTime);
        assertEquals(stopWithTrip.getStopSequence(), stopSequence);
    }

    @Test
    void reverseTimes() {
        Time arrivalTime = new Time("01:30:00");
        Time departureTime = new Time("02:30:00");
        int stopSequence = 5;

        assertThrows(DataFormatException.class, ()->{StopOnTrip stopWithTrip = new StopOnTrip(st,departureTime, arrivalTime, stopSequence);});
    }

    @Test
    void nullTime(){
        assertThrows(DataFormatException.class, ()->{StopOnTrip stopWithTrip = new StopOnTrip(st,null, departureTime, stopSequence);});
    }
    @Test
    void nullStop(){
        assertThrows(DataFormatException.class, ()->{StopOnTrip stopWithTrip = new StopOnTrip(null,arrivalTime, departureTime, stopSequence);});
    }
    @Test
    void negativeStopSequence(){
        assertThrows(DataFormatException.class, ()->{StopOnTrip stopWithTrip = new StopOnTrip(st,arrivalTime, departureTime, -1*stopSequence);});
    }
}
