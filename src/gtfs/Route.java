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


import java.util.zip.DataFormatException;

public class Route {

    private String routeId;

    private String agencyId;
    private String routeShortName;
    private String routeLongName;
    private String routeDesc;

    private int routeType;
    private String routeUrl;
    private String routeColor;

    private String routeTextColor;


    /**
     * Constructs a new Route object with the specified routeColor,
     * routeId, and routeType.
     * @param routeId provides a unique ID used to identify the route.
     * @param routeColor provides a corresponding route color for map?
     * @param routeType describes the type of transportation used on the route.
     *                  See GTFS routes.txt page for details. (0-7)
     */
    public Route(String routeId, String agencyId, String routeShortName, String routeLongName
            , String routeDesc, String routeType, String routeUrl, String routeColor, String routeTextColor)
            throws DataFormatException{
        if(routeId.equals("")||routeId.equals(null)){
            throw new DataFormatException("routeId is a required field");
        }

        int rType = -1;
        try {
            rType = Integer.parseInt(routeType);
        } catch (NumberFormatException e) {
            throw new DataFormatException("routeType was incorrectly formatted");
        }
        if (rType < 0 || rType > 7) {
            throw new DataFormatException("routeType is not a valid integer");
        }


        if(routeColor.equals("")||routeColor==(null)){
            throw new DataFormatException("routeColor is a required field");
        }

        this.routeId = routeId;
        this.agencyId = agencyId.equals(null)?"":agencyId;
        this.routeShortName = routeShortName.equals(null)?"":routeShortName;
        this.routeLongName = routeLongName.equals(null)?"":routeLongName;
        this.routeDesc = routeDesc.equals(null)?"":routeDesc;
        this.routeType = rType;
        this.routeUrl = routeUrl.equals(null)?"":routeUrl;
        this.routeColor = routeColor;
        this.routeTextColor = routeTextColor.equals(null)?"":routeTextColor;
    }
    public Route(String routeId, String routeType, String routeColor) throws DataFormatException{
        this(routeId, "", "", "", "", routeType, "", routeColor, "");
    }

    public String getRouteId() {
        return routeId;
    }

    public int getRouteType() {
        return routeType;
    }

    public String getRouteColor() {
        return routeColor;
    }

    /**
     * Creates a String representation of the Route object with all its attributes
     * @return String representation that would appear in stops.txt
     */
    @Override
    public String toString(){
        return String.format("%s,%s,%s,%s,%s,%d,%s,%s,%s", FileIO.addQuotes(routeId), FileIO.addQuotes(agencyId),
                FileIO.addQuotes(routeShortName), FileIO.addQuotes(routeLongName),
                FileIO.addQuotes(routeDesc), routeType, routeUrl, routeColor, routeTextColor);
    }


}
