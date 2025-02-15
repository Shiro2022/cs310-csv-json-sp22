package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {

    /*

        Consider the following CSV data:

        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"

        The corresponding JSON data would be similar to the following:

        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }

        (Tabs and other whitespace have been added here for clarity.)  Note the
        curly braces, square brackets, and double-quotes!  These indicate which
        values should be encoded as strings, and which values should be encoded
        as integers!  The data files which contain this CSV and JSON data are
        given in the "resources" package of this project.

        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity and readability.

        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.

    */

    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {

        String results = "";

        try {
                JSONObject obj = new JSONObject();

            /* Create CSVReader and iterator */
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();

            // INSERT YOUR CODE HERE
            JSONArray columnHeader = new JSONArray();
            JSONArray rowHeader = new JSONArray();
            JSONArray data = new JSONArray();
            JSONArray parsedData;
            /* Get String[] array from csv */
                         String[] rows = iterator.next();

            for (int i = 0; i < rows.length; i++){
                           columnHeader.add(rows[i]);
                }               

            /* Iterate through left over csv rows */
            while (iterator.hasNext()){
                parsedData = new JSONArray();                
                rows = iterator.next();
                rowHeader.add(rows[0]);
                for (int i = 1; i < rows.length; i++){
                    int stringParseddata = Integer.parseInt(rows[i]);
                                parsedData.add(stringParseddata);
                }
                data.add(parsedData);
            }

           obj.put("rowHeaders", rowHeader);
               obj.put("colHeaders", columnHeader);
                obj.put("data", data);
                 results = JSONValue.toJSONString(obj);

        }

         catch (IOException e) {e.printStackTrace();}
        catch (CsvException e) {e.printStackTrace();}

        return results.trim();

    }

    public static String jsonToCsv(String jsonString) {

        String results = "";
        /* Create StringWriter for CSV data */

        try {
        StringWriter writer = new StringWriter();

            JSONParser parser = new JSONParser();
            /* Parse JSON Data */
            JSONObject jobject = (JSONObject) parser.parse(jsonString);
            JSONArray colum = (JSONArray) jobject.get("colHeaders");
            JSONArray row = (JSONArray) jobject.get("rowHeaders");
            JSONArray data = (JSONArray) jobject.get("data");

            /* Create String[] arrays for OpenCSV */
            String[] csvcolumn = new String[colum.size()];
            
            String[] csvdata = new String[data.size()];
            JSONArray rowdata;

            /* Copy column headers */

            for (int i = 0; i < colum.size(); i++) {
                csvcolumn[i] = colum.get(i) + "";
            }
            /* Copy row headers & data*/

           
            /* Create OpenCSV Writer */
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\\', "\n");
            /* Write column headers */
            csvWriter.writeNext(csvcolumn);
            /* Write row headers and row data */

            for (int i = 0; i < csvdata.length; i++) {
                /* Create String[] container for row data */
                rowdata = (JSONArray) data.get(i);
                /* Copy row header into first element of "rowdata" */
                 csvcolumn = new String[rowdata.size() + 1];
                csvcolumn[0] = (String) row.get(i); 
                for (int j = 0 ; j < rowdata.size(); j++)
                {
                    csvcolumn[j+1]=Long.toString((long)rowdata.get(j)); 
                }

                /* Write new row */
                csvWriter.writeNext(csvcolumn);
            }
            results = writer.toString(); 

        }
        catch(ParseException e) {
            System.err.println(e.toString());
        }
        /* Return CSV string */
        return results.trim();

    }

}
