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


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Collections;
import java.util.zip.DataFormatException;


public class Trip implements Comparable<Trip> {

    private Route parentRoute;
    private ArrayList<StopOnTrip> stopsOnTrip;
    private String tripId;
    private Time arrivalTime;

    private String serviceId;
    private String tripHeadsign;
    private String directionId;
    private String blockId;
    private String shapeId;

    /**
     * Constructs new Trip object with appropriate attributes.
     * @param tripId assigned to this trip.
     * @param parentRoute is the route object that this trip belongs to.
     * @param serviceId
     * @param tripHeadsign
     * @param directionId
     * @param blockId
     * @param shapeId
     * @throws DataFormatException
     */
    public Trip(String tripId, Route parentRoute, String serviceId, String tripHeadsign, String directionId
            , String blockId, String shapeId) throws DataFormatException{
        if(tripId.equals("")||tripId.equals(null)){
            throw new DataFormatException("tripId is a required field");
        }
        if(parentRoute == null){
            throw new DataFormatException("A parent route is required");
        }
        this.parentRoute = parentRoute;
        this.stopsOnTrip = new ArrayList<StopOnTrip>();
        this.tripId = tripId;
        this.serviceId = serviceId.equals(null)?"":serviceId;
        this.tripHeadsign = tripHeadsign.equals(null)?"":tripHeadsign;
        this.directionId = directionId.equals(null)?"":directionId;
        this.blockId = blockId.equals(null)?"":blockId;
        this.shapeId = shapeId.equals(null)?"":shapeId;
    }

    public Trip(String tripId, Route parentRoute) throws DataFormatException{
        this(tripId, parentRoute, "", "", "", "", "");
    }

    public Route getParentRoute(){
        return parentRoute;
    }

    public ArrayList<StopOnTrip> getStopsOnTrip() {

        return this.stopsOnTrip;
    }

    public String getTripId() {
        return tripId;
    }

    public void setParentRoute(Route newParentRoute){
        this.parentRoute = newParentRoute;
    }

    /**
     * Adds a stop to the stopsOnTrip ArrayList.
     * @param s is the stop being added to the stopsOnTrip list.
     */
    public void addStop(StopOnTrip s){
        this.stopsOnTrip.add(s);
        this.arrivalTime = stopsOnTrip.get(0).getArrivalTime();
    }


    /**
     * Calculates distance from first stop to last stop of a trip.
     * @return distance of trip
     */
    public double calculateTripDistance(Trip t){
        double distance = 0;

        double currStopLat;
        double currStopLon;
        double nextStopLat;
        double nextStopLon;
        Collections.sort(stopsOnTrip);

        for(int i=0; i<stopsOnTrip.size()-1; i++) {
            currStopLat = t.stopsOnTrip.get(i).getPhysicalStop().getStopLat();
            currStopLon = t.stopsOnTrip.get(i).getPhysicalStop().getStopLon();
            nextStopLat = t.stopsOnTrip.get(i + 1).getPhysicalStop().getStopLat();
            nextStopLon = t.stopsOnTrip.get(i + 1).getPhysicalStop().getStopLon();

            double radius = 6371; //Radius of Earth in km
            double lat = degreeToRadian(nextStopLat - currStopLat);
            double lon = degreeToRadian(nextStopLon - currStopLon);
            double a = Math.sin(lat / 2) * Math.sin(lat / 2) +
                    Math.cos(degreeToRadian(currStopLat)) * Math.cos(degreeToRadian(nextStopLat)) *
                            Math.sin(lon / 2) * Math.sin(lon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            distance += radius * c;
        }
        return distance;
    }

    private double degreeToRadian(double degree){
        return degree*(Math.PI/180);
    }

    public String getAverageSpeed(Trip t){
        double tripDistance = calculateTripDistance(t);
        double tripDuration = calculateTripDuration(t);
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        return numberFormat.format(tripDistance/(tripDuration/60.0));
    }

    /**
     * Calculates the duration of the trip.
     * @return hours - duration of the trip in
     * 			hours (1.5 = 1 hour 30 minutes).
     */
    public double calculateTripDuration(Trip t){
        Time startTime = t.stopsOnTrip.get(0).getArrivalTime();
        Time endTime = t.stopsOnTrip.get(t.stopsOnTrip.size()-1).getArrivalTime();

        Time duration = startTime.timeBetween(endTime);
        return Time.getTimeInMinutes(duration);
    }

    /**
     * Returns boolean representing if the trip includes a
     * particular stop.
     * @param stopId of the stop being searched for
     */
    public boolean hasStop(String stopId){
        for(int i = 0; i<stopsOnTrip.size(); i++){
            if(stopsOnTrip.get(i).getPhysicalStop().getStopId().equals(stopId)){
                return true;
            }
        }
        return false;
    }

    /**
     * Compares arrival time of the first stop on the trip with
     * the arrival time of the first stop on the Trip being passed.
     * @param t being compared to
     * @return -1 if the passed trip is before the caller trip,
     * 			0 if the passed trip is at the same time as the
     * 		caller trip, and 1 if the passed trip is after the
     * 			caller trip.
     */
    @Override
    public int compareTo(Trip t) {
        int timeReturn = 0;
        if(this.arrivalTime.isBefore(t.arrivalTime)){
            timeReturn = -1;
        } else if(this.arrivalTime.isAfter(t.arrivalTime)){
            timeReturn = 1;
        } else if(this.arrivalTime.equals(t.arrivalTime)){
            timeReturn = 0;
        }
        return timeReturn;
    }

    /**
     * Checks that two trips are similar which means the first
     * and last stop are the same for both trips.
     * @param t is the trip being checked for similarity
     * @return true if trips are similar, false if
     *          trips are not similar.
     */
    public boolean checkIfSimilar(Trip t){
        boolean isSimilar = false;
        if (this.getStopsOnTrip().get(0).isSameStopOnTrip(t.getStopsOnTrip().get(0))) {
            if(this.getStopsOnTrip().get(this.getStopsOnTrip().size()-1).isSameStopOnTrip(
                    t.getStopsOnTrip().get(t.getStopsOnTrip().size()-1))){
                isSimilar = true;
            }
        }
        return isSimilar;
    }

    @Override
    public String toString(){
        return String.format("%s,%s,%s,%s,%s,%s,%s", parentRoute.getRouteId(), FileIO.addQuotes(serviceId), FileIO.addQuotes(tripId),
                FileIO.addQuotes(tripHeadsign), directionId
                , FileIO.addQuotes(blockId), FileIO.addQuotes(shapeId));
    }
}
