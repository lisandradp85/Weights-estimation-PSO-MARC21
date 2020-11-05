/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Joker
 */
public class Pesos {

    private HashMap<String, HashMap<String, Double>> map;

    public Pesos() {
        map = new HashMap<>();
    }

    public Pesos(File f) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(f));
            map = (HashMap<String, HashMap<String, Double>>) jsonObject.get("map");

        } catch (Exception e) {

            map = new HashMap<String, HashMap<String, Double>>();
        }
    }

    public void write(File f) throws Exception {

        JSONObject obj = new JSONObject();
        obj.put("map", map);

        try (FileWriter file = new FileWriter(f)) {

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putPesos(String material, String etiqueta, double peso) {
        HashMap<String, Double> mat = map.get(material);
        if (mat == null) {
            mat = new HashMap<>();
            map.put(material, mat);
        }
        mat.put(etiqueta, peso);
    }

    public Double getPesos(String material, String tag) {
        HashMap<String, Double> mat = map.get(material);
        if (mat != null) {
            Double p = mat.get(tag);
            if (p != null) {
                return p;
            }
        }
        return 0.0;
    }

    public ArrayList<String> getTags(String material) {
        HashMap<String, Double> mat = map.get(material);
        if (mat != null) {
            return new ArrayList<>(mat.keySet());
        }
        return new ArrayList<>();

    }
    public ArrayList<String> getMaterial() {        
        if (map != null && !map.isEmpty()) {
            return new ArrayList<>(map.keySet());
        }
        return new ArrayList<>();

    }
}
