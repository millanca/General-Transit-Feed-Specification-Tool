/*
 * SE2030
 * Spring 2019
 * General Transit Feed Specification Tool
 * Name: Sam Libert
 * Created: 04/08/2019
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

import java.util.zip.DataFormatException;

/**
 * Represents a Stop that a trip is associated with. Has arrival/departure times and a sequence number.
 */
public class StopOnTrip implements Comparable<StopOnTrip> {

    private Time arrivalTime;
    private Time departureTime;
    private Stop physicalStop;
    private int stopSequence;
    private String parentTripId;
    private String stopHeadsign;
    private String pickupType;
    private String dropoffType;

    public StopOnTrip(Stop st, Time arrivalTime, Time departureTime, int stopSequence, String parentTripId,
                      String stopHeadsign, String pickupType, String dropoffType) throws DataFormatException {
        if(st == null){
           throw new DataFormatException("The field for st cannot be null");
        } else if(arrivalTime == null || departureTime == null){
            throw new DataFormatException("Cannot pass null for times");
        } else if(departureTime.isBefore(arrivalTime)){
            throw new DataFormatException("Cannot depart from a stop before arrival");
        } else if(stopSequence<0){
            throw new DataFormatException("Cannot have a negative number for stop sequence");
        }
        physicalStop = st;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.stopSequence = stopSequence;

        this.parentTripId = parentTripId;
        this.stopHeadsign = stopHeadsign.equals(null)?"":stopHeadsign;
        this.pickupType = pickupType.equals(null)?"":pickupType;
        this.dropoffType = dropoffType.equals(null)?"":dropoffType;
    }

    public StopOnTrip(Stop st, Time arrivalTime, Time departureTime, int stopSequence) throws DataFormatException {
        this(st, arrivalTime, departureTime, stopSequence, "", "", "", "");
    }


        public Time getArrivalTime() {
        return arrivalTime;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public Stop getPhysicalStop() {
        return physicalStop;
    }

    public int getStopSequence() {
        return stopSequence;
    }

    public void setArrivalTime(Time arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    public void setPhysicalStop(Stop physicalStop) {
        this.physicalStop = physicalStop;
    }

    public void setStopSequence(int stopSequence) {
        this.stopSequence = stopSequence;
    }

    /**
     * Checks is two StopOnTrip objects are the same by
     * comparing their arrival/departure times and
     * the stopId.
     * @param s StopOnTrip being compared with
     * @return True is the StopOnTrip objects share the
     * same arrival time, departure time, and stopId
     */
    public boolean isSameStopOnTrip(StopOnTrip s){
        boolean isSame = false;
        if(this.getArrivalTime().isSameTime(s.getArrivalTime())){
            if(this.getDepartureTime().isSameTime(s.getDepartureTime())){
                if(this.getPhysicalStop().getStopId().equals(s.getPhysicalStop().getStopId())){
                    isSame = true;
                }
            }
        }
        return isSame;
    }

    @Override
    public String toString(){
        return String.format("%s,%s,%s,%s,%d,%s,%s,%s", FileIO.addQuotes(parentTripId), arrivalTime.toString(),
                departureTime.toString(), FileIO.addQuotes(physicalStop.getStopId()), stopSequence,
                FileIO.addQuotes(stopHeadsign), pickupType, dropoffType);
    }

    @Override
    public int compareTo(StopOnTrip s) {
        return (Integer.compare(this.getStopSequence(), s.getStopSequence()));
    }
}