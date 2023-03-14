/*
 * SE2030
 * Spring 2019
 * General Transit Feed Specification Tool
 * Name: Stephen Linn
 * Created: 05/08/2019
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

import static org.junit.jupiter.api.Assertions.*;

class TimeTest {

    private Time time;
    private final String stringTime = "12:00:00";
    private final int hours = 12;
    private final int minutes = 0;
    private final int seconds = 0;

    @BeforeEach
    void init(){
        time = new Time(stringTime);
    }

//    @Test
//    void time(){
//        assertThrows(IllegalArgumentException.class, ()->
//        {time = new Time("garbage time");});
//    }

    @Test
    void getHours() {
        assertEquals(hours, time.getHours());
    }

    @Test
    void getMinutes() {
        assertEquals(minutes, time.getMinutes());
    }

    @Test
    void getSeconds() {
        assertEquals(seconds, time.getSeconds());
    }

    @Test
    void setHours() {
        // set hours
        final int newHours = 6;
        time.setHours(newHours);

        // verify change

        // assert equal to new
        assertEquals(newHours, time.getHours());

        // assert different from old
        assertNotEquals(hours, time.getHours());

        // verify update string
        assertEquals(newHours + ":" + time.getMinutes() + ":" + time.getSeconds(), time.toString());
    }

    @Test
    void setMinutes() {
        // set minutes
        final int newMinutes = 30;
        time.setMinutes(newMinutes);

        // verify change

        // assert equal to new
        assertEquals(newMinutes, time.getMinutes());

        // assert different from old
        assertNotEquals(minutes, time.getMinutes());

        // verify update string
        assertEquals(time.getHours() + ":" + newMinutes + ":" + time.getSeconds(), time.toString());
    }

    @Test
    void setSeconds() {
        // set seconds
        final int newSeconds = 30;
        time.setSeconds(newSeconds);

        // verify changes

        // assert equal to new
        assertEquals(newSeconds, time.getSeconds());

        // assert different from old
        assertNotEquals(seconds, time.getSeconds());

        // verify update string
        assertEquals(time.getHours() + ":" + time.getMinutes() + ":" + newSeconds, time.toString());
    }

    @Test
    void timeBetween() {

        final int hoursOffset = 1;
        final int minutesOffset = 20;

        Time offsetTime = new Time(time.toString());
        offsetTime.setHours(offsetTime.getHours() + hoursOffset);
        offsetTime.setMinutes(offsetTime.getMinutes() + minutesOffset);

        Time timeBetween = time.timeBetween(offsetTime);
        assertEquals(hoursOffset, timeBetween.getHours());
        assertEquals(minutesOffset, timeBetween.getMinutes());


    }

    @Test
    void isBefore() {
        Time beforeTime = new Time("00:00:00");
        assertTrue(beforeTime.isBefore(time));
        assertFalse(time.isBefore(beforeTime));
    }

    @Test
    void isAfter() {
        Time afterTime = new Time("23:59:59");
        assertTrue(afterTime.isAfter(time));
        assertFalse(time.isAfter(afterTime));
    }

    @Test
    void isSameTime() {
        Time newTime1 = new Time(time.toString());
        newTime1.setMinutes(newTime1.getMinutes() + 1);
        Time newTime2 = new Time(time.toString());
        newTime2.setHours(newTime2.getHours() + 1);

        Time sameTime = new Time(time.toString());

        assertFalse(time.isSameTime(newTime1));
        assertFalse(time.isSameTime(newTime2));
        assertTrue(time.isSameTime(sameTime));
    }

    @Test
    void updateTime() {

        // verify positive
        int hoursOffset = 2;
        int minutesOffset = 30;
        time.updateTime(hoursOffset, minutesOffset);

        assertEquals(hoursOffset + hours, time.getHours());
        assertEquals(minutesOffset + minutes, time.getMinutes());

        // verify negative
        time.updateTime(-hoursOffset, -minutesOffset);

        assertEquals(hours, time.getHours());
        assertEquals(minutes, time.getMinutes());

    }

    @Test
    void getTimeInMinutes() {
        Time testTime = new Time("04:30:00");
        assertEquals(testTime.getHours() * 60.0 + testTime.getMinutes(), Time.getTimeInMinutes(testTime));
    }

}