/*
 * SE2030
 * Spring 2019
 * General Transit Feed Specification Tool
 * Name: Stephen Linn, Sam Libert, Christopher Millan, Brycen Hakes
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

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.zip.DataFormatException;

public class GTFSController implements Initializable, Observer {
    private static DataSet dataSet;
    private File importDirectory;


    @FXML
    private Tab ioTab;
    @FXML
    private TextArea tripList;
    @FXML
    private Button importAllButton;
    @FXML
    private Button exportAllButton;


    @FXML
    private Tab findTripsTab;
    @FXML
    private TextField tripSearchRouteId;
    @FXML
    private Button tripSearchButton;
    @FXML
    private TextArea tripSearchList;


    @FXML
    private Tab findStopsTab;
    @FXML
    private Button stopSearchButton;
    @FXML
    private TextField stopSearchRouteId;
    @FXML
    private TextArea stopSearchList;


    @FXML
    private Tab tripStatsTab;
    @FXML
    private TextArea tripSpeedList;


    private static FileIO fileIO;
    private String tripListText = "";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tripList.setFont(new javafx.scene.text.Font("Consolas", 12));
        tripSpeedList.setFont(new javafx.scene.text.Font("Consolas", 12));
        tripSearchList.setFont(new javafx.scene.text.Font("Consolas", 12));
        stopSearchList.setFont(new javafx.scene.text.Font("Consolas", 12));
        exportAllButton.setDisable(true);
        findStopsTab.setDisable(true);
        findTripsTab.setDisable(true);
        tripStatsTab.setDisable(true);



    }

    @FXML
    private void onImportAll() {

        // Establish Arrays
        File[] gtfsFiles = new File[4];
        final String[] types = {"stops.txt", "routes.txt", "trips.txt", "stop_times.txt"};

        // Create DirectoryChooser
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Open GTFS Folder");
        File selectedDirectory = chooser.showDialog(null);
        if (!(selectedDirectory == null)) {
            boolean validFiles = true;
            String directoryPath = selectedDirectory.getAbsolutePath();
            for (int i = 0; i < 4; i++) {
                File temp = new File(directoryPath +"\\" +types[i]);
                if (temp.exists()) {
                    gtfsFiles[i] = temp;
                } else validFiles = false;
            }
            if (validFiles) {
                // Open Files
                tripList.setText("Loading... This may take a couple minutes.");
                // Call fileIO importAll()
                try {
                    dataSet = FileIO.importAll(gtfsFiles);
                } catch (IOException | DataFormatException e) {
                    tripList.setText("ERROR LOADING");

                    //ERROR reading or invalid format
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Error Reading File");
                    alert.setContentText("Check your file selection and format:\n" + e.getMessage());
                    alert.showAndWait();
                }
                dataSet.addObserver(this);
                showTrips();
                showTripSpeeds();
                findTripsTab.setDisable(false);
                findStopsTab.setDisable(false);
                tripStatsTab.setDisable(false);
                exportAllButton.setDisable(false);
            } else System.out.println("Invalid GTFS File");
        } else {
            System.out.println("Nothing Selected");
        }
    }

    @FXML
    private void onTripSearchButton(){
        String routeId = tripSearchRouteId.getText();
        ArrayList<Trip> futureTrips = dataSet.findFutureTrips(routeId);
        String header = "Matching Trips:\n";
        String matchingTrips = "";
        for(Trip t : futureTrips){
            matchingTrips += t.getTripId()+"\n";
        }
        if(matchingTrips.equals("")){
            tripSearchList.setText("No future trips matching that Route ID were found");
        } else {
            tripSearchList.setText(header + matchingTrips);
        }
    }

    @FXML
    private void onStopSearchButton(){
        String routeId = stopSearchRouteId.getText();
        ArrayList<StopOnTrip> stopsOnRoute = dataSet.findStopsOnRoute(routeId);
        String header = "Stops on Route:\n";
        String stopsOnRouteText = "";
        for(StopOnTrip s : stopsOnRoute){
            stopsOnRouteText += s.getPhysicalStop().getStopName()+"\n";
        }
        if(stopsOnRouteText.equals("")){
            stopSearchList.setText("No stops found");
        } else {
            stopSearchList.setText(header + stopsOnRouteText);
        }
    }

    @FXML
    private void showTripSpeeds(){
        dataSet.showTripSpeeds();
    }

    private void showTrips(){
        dataSet.showTrips();
    }

    @Override
    public void update(Object arg, String specification) {
        tripListText = (String)arg;
        if(specification.equals("import")) {
            tripList.setText(tripListText);
        } else if(specification.equals("speed")){
            tripSpeedList.setText(tripListText);
        }

    }

    public void onExportAll(){
        DirectoryChooser exportChooser = new DirectoryChooser();
        exportChooser.setInitialDirectory(importDirectory);
        exportChooser.setTitle("Choose a location to save your files");

        File saveLocation;
        boolean valid = false;
        saveLocation = exportChooser.showDialog(null);
        if(saveLocation != null){
            valid = saveLocation.isDirectory();
            do {

                if(!valid){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Error Writing File");
                    alert.setContentText("Selection must be a directory");
                    alert.showAndWait();
                }
            }while(!valid);

            File stopFileLocation = new File(saveLocation,"stops.txt");
            File routeFileLocation = new File(saveLocation,"routes.txt");
            File tripFileLocation = new File(saveLocation,"trips.txt");
            File stopTimeFileLocation = new File(saveLocation,"stop_times.txt");

            try{
                exportFile(stopFileLocation,dataSet.getStopMap(),"stop_id,stop_name,stop_desc,stop_lat,stop_lon");
                exportFile(routeFileLocation,dataSet.getRouteMap(),"route_id,agency_id,route_short_name," +
                        "route_long_name,route_desc,route_type,route_url,route_color,route_text_color");
                exportFile(tripFileLocation, dataSet.getTripMap(),"route_id,service_id,trip_id," +
                        "trip_headsign,direction_id,block_id,shape_id");

                ArrayList<StopOnTrip> stopTimeList = new ArrayList<>();
                dataSet.getTripMap().forEach((k, v) -> {
                    for(StopOnTrip st: v.getStopsOnTrip()){
                        stopTimeList.add(st);
                    }
                });
                exportFile(stopTimeFileLocation, stopTimeList, "trip_id,arrival_time,departure_time,stop_id," +
                        "stop_sequence,stop_headsign,pickup_type,drop_off_type");
            }catch(IOException ex){
                //TODO
                ex.printStackTrace();
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Export Complete");
            alert.setHeaderText("Files Exported Sucessfully");
            alert.setContentText("Your files are avaliable at:\n"+saveLocation.getPath());
            alert.showAndWait();
        }
    }

    private void exportFile(File path, Map map, String header) throws IOException{
        try(BufferedWriter fo = new BufferedWriter(new FileWriter(path))){
            fo.write(header);
            fo.newLine();
            map.forEach((k, v) -> {
                try {
                    fo.write(v.toString());
                    fo.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    private void exportFile(File path, ArrayList list, String header) throws IOException{
        try(BufferedWriter fo = new BufferedWriter(new FileWriter(path))){
            fo.write(header);
            fo.newLine();
            for(Object st: list){
                fo.write(st.toString());
                fo.newLine();
            }
        }

    }

}
