/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import PSO_Algorithms.Particle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Joker
 */
public class Configuracion {

    private ArrayList<Vector<String>> llevels;
    private String material;
    private Particle p;
    private int[] cantForLevels;

    public Configuracion(String material, ArrayList<Vector<String>> dlists) {
        this.material = material;
        this.llevels = (ArrayList<Vector<String>>) dlists.clone();
        cantForLevels = new int[llevels.size()];
        for (int i = 0; i < llevels.size(); i++) {
            cantForLevels[i] = llevels.get(i).size();
        }
    }

    public Configuracion(File f) {
        llevels = new ArrayList<Vector<String>>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(f));
            JSONArray l = (JSONArray) jsonObject.get("list");
            for (Object vO : l) {
                Vector<String> n = new Vector();
                JSONArray vA = (JSONArray) vO;
                for (Object tag : vA) {
                    n.add((String) tag);
                }
                llevels.add(n);
            }
            material = (String) jsonObject.get("material");
            if (material == null) {
                material = "Desconocido";
            }
            cantForLevels = new int[llevels.size()];
            for (int i = 0; i < llevels.size(); i++) {
                cantForLevels[i] = llevels.get(i).size();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
 public Configuracion(InputStream in) {
        llevels = new ArrayList<Vector<String>>();
        JSONParser parser = new JSONParser();
        try {
            Scanner s=new Scanner(in);
            String text="";
            while (s.hasNextLine()) {
                text += s.nextLine()+"\n";                
            }            
            JSONObject jsonObject = (JSONObject) parser.parse(text);
            JSONArray l = (JSONArray) jsonObject.get("list");
            for (Object vO : l) {
                Vector<String> n = new Vector();
                JSONArray vA = (JSONArray) vO;
                for (Object tag : vA) {
                    n.add((String) tag);
                }
                llevels.add(n);
            }
            material = (String) jsonObject.get("material");
            if (material == null) {
                material = "Desconocido";
            }
            cantForLevels = new int[llevels.size()];
            for (int i = 0; i < llevels.size(); i++) {
                cantForLevels[i] = llevels.get(i).size();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void write(File f) throws Exception {

        JSONObject obj = new JSONObject();
        obj.put("list", llevels);
        obj.put("material", material);

        try (FileWriter file = new FileWriter(f)) {

            file.write(obj.toJSONString());
            file.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Vector<String>> getLlevels() {
        return (ArrayList<Vector<String>>) llevels.clone();
    }

    public String getMaterial() {
        return material;
    }

    public int[] getCantForLevels() {
        return cantForLevels;
    }
    
    public Particle getP() {
        return p;
    }

    public void setP(Particle p) {
        this.p = p;
    }

    public int getLevels() {
        return llevels.size();
    }

    public Vector getLevel(int n) {
        if (n < 0 || n >= getLevels()) {
            return null;
        }
        return (Vector) llevels.get(n).clone();
    }

}
