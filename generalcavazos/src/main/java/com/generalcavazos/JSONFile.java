package com.generalcavazos;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONFile {

    // read a json file and return an array
    public static JSONArray readArray(String fileName) {

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        JSONArray data = null;

        try (InputStream inputStream = JSONFile.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                return null;
            }

            try (Reader reader = new InputStreamReader(inputStream)) {
                Object obj = jsonParser.parse(reader);

                data = (JSONArray) obj;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return data;
    }
}
