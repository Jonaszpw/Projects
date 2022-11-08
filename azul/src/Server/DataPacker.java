package Server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataPacker {
    /**
     * metoda pakuje dane w jsona
     * @param session
     * @return
     * @throws IOException
     */
    public static String packData(Session session) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(session);

        return json;



    }

    /**
     * metoda zapisuje obecny stan rozgrywki
     * @param session
     * @param filePath
     * @throws IOException
     */
    public static void saveSession(Session session,String filePath) throws IOException {
        FileWriter fw= new FileWriter(new File(filePath));
        fw.write(packData(session));
        fw.close();
    }
    public static Session loadSession(String filePath) throws IOException {
        Path path= Paths.get(filePath);
        String json= Files.readString(path);
        Gson gson = new Gson();
        Session session = gson.fromJson(json.substring(6), Session.class);
        return session;



    }

}
