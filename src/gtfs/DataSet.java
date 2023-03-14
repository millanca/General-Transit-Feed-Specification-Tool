/*
 * SE2030
 * Spring 2019
 * General Transit Feed Specification Tool
 * Name: Stephen Linn
 * Created: 04/05/2019
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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * The DataSet for holding all the Trips in the System
 */
public class DataSet extends Observable {

    private Map<String, Trip> tripMap;
    private Map<String, Route> routeMap;
    private Map<String, Stop> stopMap;

    /**
     * Creates a new DataSet object
     * @param tripMap list of imported trips
     * @param routeMap list of imported routes
     */
    public DataSet(Map<String, Trip> tripMap, Map<String, Route> routeMap, Map<String, Stop> stopMap){
        this.tripMap = tripMap;
        this.routeMap = routeMap;
        this.stopMap = stopMap;
    }

    public Map<String, Route> getRouteMap(){
        return routeMap;
    }

    public void setRouteMap(Map<String, Route> routeMap){
        this.routeMap = routeMap;
    }

    public Map<String, Trip> getTripMap() {
        return tripMap;
    }

    public Map<String, Stop> getStopMap(){
        return stopMap;
    }

    public void setTripMap(Map<String, Trip> tripMap) {
        this.tripMap = tripMap;
    }

    /**
     *
     * @param routeId
     * @return
     */
    public Map<String, Stop> getStops(String routeId){
        //TODO
        return null;
    }

    /**
     * Iterates through the TripList and counts the number of trips containing the stop.
     * @param stopId - the ID for the stop
     * @return the number of trips containing the specified stop. 0 if no trips contain the stop.
     */
    int numTripsWithStop(String stopId){

        final int[] count = {0};
        tripMap.forEach((k, v) -> {
            if(v.hasStop(stopId)){
                count[0]++;
            }
        });
        return count[0];
    }

    /**
     *
     * TODO Verify that function does not add duplicate routes
     * @param stopId
     * @return
     */
    ArrayList<Route> routesWithStop(String stopId) {

        ArrayList<Route> routes = new ArrayList<>();
        tripMap.forEach((k, v) -> {
            if(v.hasStop(stopId)){
                routes.add(v.getParentRoute());
            }
        });
        return routes;
    }

    /**
     * Sorts the list of trips by time.
     */
    public void sortByTime(){
        //Collections.sort(tripMap); //TODO
    }

    /**
     *
     * @param routeId
     * @return
     */
    public Map<String, Stop> stopsOnRoute(String routeId){
        //TODO
        return null;
    }


    /**
     * Per the requirements of feature 12, this method updates the parent route of a group of similar trips.
     * Any two trips are similar if their starting and ending stops are the same.
     * @param tripId - a valid tripId for identifying the trip group
     * @param routeId - the route to be updated to
     * @throws IllegalArgumentException if either the specified trip or route doesn't exist
     */
    void tripUpdateParentRoute(String tripId, String routeId) throws IllegalArgumentException{

        // Find trip based on ID
        Trip initialTrip = getTripFromId(tripId);

        // Get similar trips and update parent routes
        if(initialTrip != null){

            Route newRoute = getRouteFromId(routeId);

            if(newRoute != null){

                // Find similar trips and update : this method should also update the initial trip
                tripMap.forEach((k, v) -> {
                    if(v.compareTo(initialTrip) == 0){
                        v.setParentRoute(newRoute);
                    }
                });
            } else{
                throw new IllegalArgumentException("The specified route does not exist.");
            }
        } else{
            throw new IllegalArgumentException("The specified trip does not exist.");
        }


    }

    /**
     * Per the requirements of feature 12, this method updates the stop times of a group of similar trips.
     * Any two trips are similar if their starting and ending stops are the same.
     * @param tripId - a valid tripId for identifying the trip group
     * @param minuteOffset - offset in minutes to apply
     * @param hoursOffset - offset in hours to apply
     * @throws IllegalArgumentException if the specified trip doesn't exist
     */
    public void tripUpdateStopTimes(String tripId, int hoursOffset, int minuteOffset){

        // Find trip based on ID
        Trip initialTrip = getTripFromId(tripId);

        // Get similar trips and update parent routes
        if(initialTrip != null){

            // Update similar trips first
            tripMap.forEach((k, v) -> {
                if(v.compareTo(initialTrip) == 0 && !v.equals(initialTrip)){

                    for(StopOnTrip s: v.getStopsOnTrip()){
                        s.getArrivalTime().updateTime(hoursOffset, minuteOffset);
                        s.getDepartureTime().updateTime(hoursOffset, minuteOffset);
                    }
                }
            });

            // Update initial trip
            for(StopOnTrip s: initialTrip.getStopsOnTrip()){
                s.getArrivalTime().updateTime(hoursOffset, minuteOffset);
                s.getDepartureTime().updateTime(hoursOffset, minuteOffset);
            }

        } else{
            throw new IllegalArgumentException("The specified trip does not exist.");
        }
    }

    /**
     * Returns the trip from the passed tripId
     * @param tripId - the ID for the searched trip
     * @return the desired trip, or null if not found
     */
    Trip getTripFromId(String tripId){
        // Returns the first trip matching the tripId
        return tripMap.get(tripId);
    }

    /**
     * Returns the route from the passed routeId
     * @param routeId - the ID for the searched route
     * @return the desired route, or null if not found
     */
    private Route getRouteFromId(String routeId){
        // Returns the first route matching the routeId
        return routeMap.get(routeId);
    }

    /**
     * Appends string with trip speeds and notifies
     * observers with trip speed string
     */
    void showTripSpeeds() {
        int maxTripId = maxTripId();
        int maxRouteId = maxRouteId();
        final String[] loadedTrips = {String.format("%-" + (maxTripId) + "s     %-" + (maxRouteId) + "s     %s\n", "TripID", "RouteID", "Average Speed (km/h)")};
        this.tripMap.forEach((k, v) -> {
            if(v.getStopsOnTrip().size()!=0) {
                loadedTrips[0] += String.format("%-"+(maxTripId)+"s     %-"+(maxRouteId)+"s     %s\n", v.getTripId(), v.getParentRoute().getRouteId(),
                        v.getAverageSpeed(v));
            }
        });
        notifyObservers(loadedTrips[0],"speed");
    }

    void showTrips() {
        int maxTripId = maxTripId();
        int maxRouteId = maxRouteId();
        final String[] loadedTrips = {String.format("%-" + (maxTripId) + "s     %-" + (maxRouteId) + "s\n", "TripID    ", "RouteID")};
        this.tripMap.forEach((k, v) -> loadedTrips[0] += String.format("%-" + (maxTripId) + "s     %-" + (maxRouteId) + "s\n", v.getTripId(), v.getParentRoute().getRouteId()));
        notifyObservers(loadedTrips[0],"import");
    }

    private int maxTripId() {
        final int[] maxTripId = {0};
        this.tripMap.forEach((k, v) -> maxTripId[0] = maxTripId[0] < v.getTripId().length() ? v.getTripId().length() : maxTripId[0]);
        return maxTripId[0];
    }

    private int maxRouteId() {
        final int[] maxRouteId = {0};
        routeMap.forEach((k, v) -> maxRouteId[0] = maxRouteId[0] < v.getRouteId().length() ? v.getRouteId().length() : maxRouteId[0]);
        return maxRouteId[0];
    }

    /**
     * Finds future trips that belong to the parent
     * route of the specified routeId
     * @param routeId of the route the trips belong to
     * @return ArrayList of future trips on the
     *          specified route
     */
    public ArrayList<Trip> findFutureTrips(String routeId) {
        //ArrayList to hold future trips with matching routeId
        ArrayList<Trip> futureTrips = new ArrayList<>();

        //Getting current time for reference of which trips are in the future
        LocalTime currentTime = java.time.LocalTime.now();
        Time time = new Time(currentTime.getHour()+":"+currentTime.getMinute()+":00");

        //Searching tripMap for trips with matching parent route routeIds that
        // are also chronologically after the current time
        tripMap.forEach((k, v) -> {
            if(v.getParentRoute().getRouteId().equals(routeId)){
                if (time.isBefore(v.getStopsOnTrip().get(0).getArrivalTime())){
                    futureTrips.add(v);
                }
            }
        });
        return futureTrips;
    }

    //TODO: Change the way this method searches using Hashmap
    /**
     * Returns list of stops on the route associated with the given
     * routeId.
     * @param routeId of the route the user wants the stops from
     * @return an ArrayList of the stops (stopOnTrips)
     */
    public ArrayList<StopOnTrip> findStopsOnRoute(String routeId){
        //ArrayList to hold stops on the given route
        ArrayList<StopOnTrip> allStopsOnRoute = new ArrayList<>();
        ArrayList<StopOnTrip> uniqueStopsOnRoute = new ArrayList<>();
        Hashtable<String, String> ht = new Hashtable<>();

        //for each trip on trip list compare its parent route with given route
        tripMap.forEach((k, v) -> {
            if(v.getParentRoute().getRouteId().equals(routeId)) {
                allStopsOnRoute.addAll(v.getStopsOnTrip());
            }
        });
        for(StopOnTrip s : allStopsOnRoute){
            if(!(ht.containsKey(s.getPhysicalStop().getStopName()))) {
                ht.put(s.getPhysicalStop().getStopName(), s.getPhysicalStop().getStopId());
                uniqueStopsOnRoute.add(s);
            }
        }
        return uniqueStopsOnRoute;
    }


}

