/*
 * SE2030
 * Spring 2019
 * General Transit Feed Specification Tool
 * Name: Christopher Millan, Sam Libert
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

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.Map;
import java.util.zip.DataFormatException;

public class FileIO {

    private static Map<String, Route> routes = new HashMap<>();
    private static Map<String, Stop> stops = new HashMap<>();
    private static Map<String, Trip> trips = new HashMap<>();

    public FileIO() {

    }


    /**
     * @param path
     * @param gtfsData
     */
    public void exportAll(Path path, DataSet gtfsData) {

    }

    /**
     * @param gtfsFiles
     */
    static DataSet importAll(File[] gtfsFiles) throws DataFormatException, IOException {
        // Create Scanner in with stop.txt
        stops = importStops(new Scanner(gtfsFiles[0])); //From stops.txt

        //Change Scanner to routes.txt
        routes = importRoutes(new Scanner(gtfsFiles[1])); //from routes.txt

        //Change Scanner to trips.txt
        trips = importTrips(new Scanner(gtfsFiles[2])); //Incomplete Trips, only trip_id, and Route. No stops

        //Change Scanner to stop_times.txt
        importStopTimes(new Scanner(gtfsFiles[3]));

        return new DataSet(trips, routes, stops);
    }

    /**
     * @param in
     */
    private static Map<String, Stop> importStops(Scanner in) throws DataFormatException {
        Map<String, Stop> possibleStops = new HashMap<>();
        String[] possibleStopFields = {"stop_id", "stop_name", "stop_desc", "stop_lat", "stop_lon"};
        List<Integer> fileFieldIndexs = getFieldIndexs(possibleStopFields, in.nextLine());

        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[] splitline = cleanLine(line);
            String stopId = splitline[fileFieldIndexs.get(0)];

            if (possibleStops.containsKey(stopId)) {
                throw new DataFormatException("A route with that ID already exists");
            } else {
                possibleStops.put(stopId, new Stop(stopId, splitline[fileFieldIndexs.get(1)], splitline[fileFieldIndexs.get(2)]
                        , splitline[fileFieldIndexs.get(3)], splitline[fileFieldIndexs.get(4)]));
            }
        }
        return possibleStops;
    }

    /**
     * @param in
     */
    private static Map<String, Route> importRoutes(Scanner in) throws DataFormatException {
        Map<String, Route> possibleRoutes = new HashMap<>();
        String[] possibleRouteFields = {"route_id", "agency_id", "route_short_name", "route_long_name"
                , "route_desc", "route_type", "route_url", "route_color", "route_text_color"};
        List<Integer> fileFieldIndexs = getFieldIndexs(possibleRouteFields, in.nextLine());

        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[] splitline = cleanLine(line);
            String routeId = splitline[fileFieldIndexs.get(0)];

            if (possibleRoutes.containsKey(routeId)) {
                throw new DataFormatException("A route with that ID already exists");
            } else {
                possibleRoutes.put(routeId, new Route(routeId, splitline[fileFieldIndexs.get(1)]
                        , splitline[fileFieldIndexs.get(2)], splitline[fileFieldIndexs.get(3)]
                        , splitline[fileFieldIndexs.get(4)], splitline[fileFieldIndexs.get(5)]
                        , splitline[fileFieldIndexs.get(6)], splitline[fileFieldIndexs.get(7)]
                        , splitline[fileFieldIndexs.get(8)]));
            }
        }
        return possibleRoutes;
    }

    /**
     * @param in
     */
    private static Map<String, Trip> importTrips(Scanner in) throws DataFormatException {
        Map<String, Trip> triplist = new HashMap<>();//Incomplete Trips, only trip_id, and Route. No stops
        String[] possibleTripFields = {"route_id", "service_id", "trip_id", "trip_headsign", "direction_id", "block_id", "shape_id"};
        List<Integer> fileFieldIndexs = getFieldIndexs(possibleTripFields, in.nextLine());

        while (in.hasNextLine()) {
            //Get Route from possibleRoute that matches route_id from Scanner
            String line = in.nextLine();
            String[] splitline = cleanLine(line);
            String route_id = splitline[fileFieldIndexs.get(0)];
            String tripId = splitline[fileFieldIndexs.get(2)];

            Route match;
            if (triplist.containsKey(tripId)) {
                throw new DataFormatException("A trip with that ID already exists");
            } else {
                match = routes.get(route_id);
                if (match == null) {
                    throw new DataFormatException("Route for trip does not exist");
                } else {
                    triplist.put(tripId, new Trip(tripId, match, splitline[fileFieldIndexs.get(1)]
                            , splitline[fileFieldIndexs.get(3)], splitline[fileFieldIndexs.get(4)]
                            , splitline[fileFieldIndexs.get(5)], splitline[fileFieldIndexs.get(6)]));
                }
            }
        }
        return triplist;
    }

    private static void importStopTimes(Scanner in) throws DataFormatException {
        String[] possibleFields = {"trip_id", "arrival_time", "departure_time", "stop_id", "stop_sequence", "stop_headsign", "pickup_type", "drop_off_type"};
        List<Integer> fileFieldIndexs = getFieldIndexs(possibleFields, in.nextLine());

        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[] splitline = cleanLine(line);
            Trip matchingTrip = trips.get(splitline[fileFieldIndexs.get(0)]);
            Stop matchingStop = stops.get(splitline[fileFieldIndexs.get(3)]);

            if (matchingStop == null || matchingTrip == null) {
                throw new DataFormatException("Stop or Trip does not exist");
            } else {
                matchingTrip.addStop(new StopOnTrip(matchingStop, new Time(splitline[1])
                        , new Time(splitline[2]), Integer.parseInt(splitline[4])
                        , splitline[fileFieldIndexs.get(0)], splitline[fileFieldIndexs.get(5)]
                        , splitline[fileFieldIndexs.get(6)], splitline[fileFieldIndexs.get(7)]));
            }
        }
    }

    /**
     * This method takes a line from a text file and splits it into an array of Strings w/out
     * the containing quotes or double quotes inside.
     *
     * @param dirty
     * @return
     */
    private static String[] cleanLine(String dirty) throws DataFormatException {

        StringBuilder field = new StringBuilder();
        ArrayList<String> fieldList = new ArrayList<>();

        boolean insideQuotes = false;
        char curr;
        char peek;

        for (int i = 0; i < dirty.length(); i++) {

            curr = dirty.charAt(i);
            if (curr == '\t' || curr == '\r' || curr == '\n') {
                throw new DataFormatException("Invalid character");
            } else if (insideQuotes) {
                if (curr == '\"') {
                    if (i < dirty.length() - 1) {
                        //Check if double or single quote
                        peek = dirty.charAt(i + 1);
                        if (peek == '\"') {
                            //replace the double quote with a single quote
                            field.append('\"');
                            i++;
                        } else {
                            insideQuotes = false;
                        }
                    }
                } else {
                    field.append(curr);
                }
            } else if (curr == '\"') {
                insideQuotes = true;
            } else if (curr == ',') {
                fieldList.add(field.toString().trim());//field is finished
                field = new StringBuilder();//new field initialized
            } else field.append(curr);
        }
        fieldList.add(field.toString().trim());
        return fieldList.toArray(new String[0]);
    }

    static String addQuotes(String exposed) {
        String surrounded;
        if(exposed.contains("\"") || exposed.contains(",")) {
            surrounded = "\"" + exposed.replace("\"", "\"\"") + "\"";
        } else {
            surrounded = exposed;
        }
        return surrounded;
    }

    private static List<Integer> getFieldIndexs(String[] possibleFields, String fileFields) {
        List<String> fields = Arrays.asList(possibleFields);
        List<String> fileFieldList = Arrays.asList(fileFields.split(","));
        List<Integer> fileFieldIndexs = new ArrayList<>();

        for (String s : fileFieldList) {
            s.trim();
        }
        int i = 0;
        while (i < fileFieldList.size()) {

            if (fields.contains(fileFieldList.get(i))) {
                fileFieldIndexs.add(fileFieldList.indexOf(fields.get(i)));
            } else {
                fileFieldIndexs.add(-1);
            }
            i++;
        }
        while (i < fields.size()) {
            fileFieldIndexs.add(-1);
        }
        return fileFieldIndexs;
    }
}