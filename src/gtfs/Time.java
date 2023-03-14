/*
 * Course: SE2030 - 011
 * Spring 2019
 * General Transit Feed Specification Tool
 * Name: Brycen Hakes
 * Created: 4/5/2019
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

import java.time.LocalTime;

public class Time {
    private int hours;
    private int minutes;
    private int seconds;


    /**
     * Constructs a Time object using hour, minute, and
     * second attributes.
     * @param time String with format HH:MM:SS
     * @throws IllegalArgumentException if user passes
     * a bad format
     */
    public Time(String time) throws IllegalArgumentException{
        //Split time string into hours, minutes, seconds
        String[] splitTime = time.split(":");
        //Array must have exactly 3 indexes
        if (splitTime.length == 3) {
            //parsing hours, minutes, and seconds
            int hours = Integer.parseInt(splitTime[0]);
            this.hours = hours;
            int minutes = Integer.parseInt(splitTime[1]);
            this.minutes = minutes;
            int seconds = Integer.parseInt(splitTime[2]);
            this.seconds = seconds;
        } else {
            throw new IllegalArgumentException("Time string has incorrect format." +
                    "Must include hours:minutes:seconds.");
        }
    }

    public int getHours(){
        return this.hours;
    }

    public int getMinutes(){
        return this.minutes;
    }

    public  int getSeconds(){
        return this.seconds;
    }

    public void setHours(int hours){
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(int seconds){
        this.seconds = seconds;
    }

    /**
     * Calculates the time between two time objects and
     * returns the difference with a new Time object.
     * A negative time means the calling Time is after
     * the parameter time.
     * @param t is the Time being compared to.
     * @return duration of time between calling time and
     *          parameter time.
     */
    public Time timeBetween(Time t){
        int hours = (t.getHours() - this.hours);
        int minutes = (t.getMinutes() - this.minutes);
        int seconds = (t.getSeconds() - this.seconds);
        String timeBetween = hours + ":" + minutes + ":" + seconds;
        Time duration = new Time(timeBetween);
        return duration;
    }

    /**
     * Returns whether the calling Time is before the parameter Time.
     * @param t Time being compared to.
     * @return True if the calling Time is before the parameter Time.
     */
    public boolean isBefore(Time t){
        boolean before = false;
        if(this.hours < t.getHours()){
            before = true;
        } else if(this.hours == t.getHours()){
            if(this.minutes < t.getMinutes()){
                before = true;
            }
        }
        return before;
    }

    /**
     * Returns whether the calling Time is after the parameter Time.
     * @param t Time being compared to.
     * @return True if the calling Time is after the
     *          calling Time, otherwise false.
     */
    public boolean isAfter(Time t){
        boolean after = false;
        if(this.hours > t.getHours()) {
            after = true;
        } else if(this.hours == t.getHours()){
                if(this.minutes > t.getMinutes()){
                    after = true;
                }
        }
        return after;
    }

    /**
     * Returns whether the calling time is the same Time as the
     * parameter Time.
     * @param t time being compared to
     * @return true if the times are the same,
     *         false if times are different.
     */
    public boolean isSameTime(Time t){
        boolean sameTime = false;
        if(this.hours == t.getHours()){
            if (this.minutes == t.getMinutes()) {
                sameTime = true;
            }
        }
        return sameTime;
    }

    /**
     * Allows for adjustment of a time using an
     * offset given in minutes.
     * @param hourOffset is the hours adjustment
     * @param minuteOffset is the minutes adjustment
     */
    public void updateTime(int hourOffset, int minuteOffset){
        //Adjusts hours (negative will push make time earlier)
        this.setHours(this.hours + hourOffset);
        //Adjust minutes
        if(minuteOffset >= 0){
            while(minuteOffset >= 60){
                this.setHours(this.hours +1);
                minuteOffset -= 60;
            }
            this.setMinutes(this.minutes + minuteOffset);
            if(this.minutes > 59){
                this.hours += 1;
                this.minutes -= 60;
            }
        } else {
            while(minuteOffset <= -60){
                this.setHours(this.hours -1);
                minuteOffset += 60;
            }
            if(this.minutes >= Math.abs(minuteOffset)) {
                this.setMinutes(this.minutes + minuteOffset);
            } else {
                this.hours -= 1;
                this.minutes += 60;
                this.setMinutes(this.minutes + minuteOffset);
            }
        }
    }

    public static double getTimeInMinutes(Time t){
        return t.getHours()*60 + t.getMinutes() + t.getSeconds()/60.0;
    }

    public Time getCurrentTime(){
        LocalTime currentTime = java.time.LocalTime.now();
        Time time = new Time(currentTime.getHour()+":"+currentTime.getMinute()+":"+currentTime.getSecond());
        return time;
    }

    @Override
    public String toString(){
        return String.format("%02d:%02d:%02d",hours,minutes,seconds);
    }



}
