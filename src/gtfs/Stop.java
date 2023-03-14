/*
 * Course: SE2030
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
 * Represents a Stop that is independent of any Trip or Route.
 */
public class Stop {

    private String stopId;
    private String stopName;
    private String stopDesc;
    private double stopLat;
    private double stopLon;

    /**
     * Creates a stop from its name, id, and location
     * @param stopId an ID that uniquely identifies a stop, station, or station entrance
     * @param stopName
     * @param stopDesc
     * @param stopLat
     * @param stopLon
     */
    public Stop(String stopId, String stopName, String stopDesc, String stopLat, String stopLon) throws DataFormatException{
        if(stopId.equals("")||stopId.equals(null)){
            throw new DataFormatException("stopId is a required field");
        }
        if(stopName.equals("")||stopName.equals(null)){
            throw new DataFormatException("stopName is a required field");
        }

        this.stopId = stopId;
        this.stopName = stopName;
        this.stopDesc = stopDesc.equals(null)?"":stopDesc;

        try {
            this.stopLat = Double.parseDouble(stopLat);
            this.stopLon = Double.parseDouble(stopLon);
        } catch(NumberFormatException e){
            throw new DataFormatException("A coordinate was incorrectly formatted");
        }
        if(Math.abs(this.stopLat)>90||Math.abs(this.stopLon)>180){
            throw new DataFormatException("The global coordinates must be in range");
        }
    }
    public Stop(String stopId, String stopName, String stopLat, String stopLon) throws DataFormatException{
        this(stopId, stopName, "", stopLat, stopLon);
    }

    public String getStopId() {
        return stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public String getStopDesc() {
        return stopDesc;
    }

    public double getStopLat() {
        return stopLat;
    }

    public double getStopLon() {
        return stopLon;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public void setStopLat(double stopLat) {
        this.stopLat = stopLat;
    }

    public void setStopLon(double stopLon) {
        this.stopLon = stopLon;
    }

    @Override
    public String toString(){
        return String.format("%s,%s,%s,%f,%f", FileIO.addQuotes(stopId), FileIO.addQuotes(stopName),
                FileIO.addQuotes(stopDesc), stopLat, stopLon);
    }
}