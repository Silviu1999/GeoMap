import com.fasterxml.jackson.databind.util.JSONPObject;
import netscape.javascript.JSObject;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Read {
    public static void main(String[] args) {
        String file = "pimeo.csv";
        HashMap<String, ArrayList<String>> a = new HashMap<>();
        BufferedReader bufferedReader;
        String line = "";
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String[] row = new String[]{};
            ArrayList<String> s = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                row = line.split("\n");
                s.add(row[0]);
                //  System.out.println(s.size());

            }
//            for (String i:s){
//                System.out.println(i) ;
//        }
            //  HashMap<String,ArrayList<String>> a=new HashMap<>();
            a.put("longitude", new ArrayList<String>());
            a.put("latitude", new ArrayList<String>());
            a.put("metric", new ArrayList<String>());
            for (int i = 1; i < s.size(); i++) {
                String[] val = s.get(i).split(",");
                a.get("longitude").add(val[2]);
                a.get("latitude").add(val[1]);
                a.get("metric").add(val[9]);

            }

            //      System.out.println(a);


        } catch (Exception e) {
            e.printStackTrace();

        }
        try {
            MyMqtt app = new MyMqtt();
            for (int i = 0; i < a.get("metric").size(); i++) {
                app.runClient();
                JSONObject jsObject = new JSONObject().put("longitude", Double.parseDouble(a.get("longitude").get(i)))
                        .put("latitude", Double.parseDouble(a.get("latitude").get(i)))
                        .put("metric", Double.parseDouble(a.get("metric").get(i)));
                app.sendMessage(MyMqtt.DEFAULT_TOPIC,jsObject.toString());
                Thread.sleep(300);
            }
            app.stopClient();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {

        }
    }
}
