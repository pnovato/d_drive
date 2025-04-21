package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.util;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.util.*;

public class PartilhaManager
{
    private static final String FILE_PATH = "/home/ptrck/UFP/3_ano/SD/src/edu/ufp/inf/sd/rmi/projecto_SD/d_drive/data/partilhas.json";

    // Garante que o ficheiro existe
    private static JSONObject carregarJSON()
    {
        File file = new File(FILE_PATH);
        if (!file.exists())
        {
            try
            {
                file.getParentFile().mkdirs();
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file))
                {
                    writer.write("{}");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            StringBuilder sb = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null)
            {
                sb.append(linha);
            }
            return new JSONObject(sb.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    private static void salvarJSON(JSONObject json)
    {
        try (FileWriter writer = new FileWriter(FILE_PATH))
        {
            writer.write(json.toString(4)); // 4 = identação
            writer.flush();
            System.out.println("JSON salvo em " + FILE_PATH);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Falha ao salvar JSON em " + FILE_PATH);
        }
    }

    public static void addPartilha(String fromUser, String folderName, String toUser)
    {
        System.out.println("[DEBUG]: Chamando addPartilha com fromUser=" + fromUser + ", folderName=" + folderName + ", toUser=" + toUser);
        JSONObject json = carregarJSON();

        JSONObject userObj = json.optJSONObject(fromUser);
        if (userObj == null) {
            userObj = new JSONObject();
            System.out.println("[DEBUG] Criando novo objeto para usuário " + fromUser);
        }

        JSONArray list = userObj.optJSONArray(folderName);
        if (list == null) {
            list = new JSONArray();
            System.out.println("DEBUG] Criando nova lista de partilhas para a pasta " + folderName);
        }

        if (!list.toList().contains(toUser)) {
            list.put(toUser);
            System.out.println("[DEBUG] Adicionado " + toUser + " à lista de partilha da pasta " + folderName);
        } else {
            System.out.println("[DEBUG] " + toUser + " já existe na lista de partilhas.");
        }

        userObj.put(folderName, list);
        json.put(fromUser + "_" + folderName, userObj);

        salvarJSON(json);
        System.out.println("[DEBUG] JSON salvo com sucesso.");
    }

    public static List<String> getDestinatarios(String fromUser, String folderName)
    {
        JSONObject json = carregarJSON();
        JSONObject userObj = json.optJSONObject(fromUser);
        JSONArray list = userObj != null ? userObj.optJSONArray(folderName) : null;

        List<String> result = new ArrayList<>();
        if (list != null)
        {
            for (int i = 0; i < list.length(); i++)
            {
                result.add(list.getString(i));
            }
        }

        return result;
    }

    public static boolean isPartilhada(String fromUser, String folderName)
    {
        return !getDestinatarios(fromUser, folderName).isEmpty();
    }

    public static void removePartilha(String fromUser, String folderName, String toUser)
    {
        JSONObject json = carregarJSON();
        JSONObject userObj = json.optJSONObject(fromUser);
        JSONArray list = userObj != null ? userObj.optJSONArray(folderName) : null;

        if (list != null && list.toList().contains(toUser))
        {
            JSONArray novaLista = new JSONArray();
            for (int i = 0; i < list.length(); i++)
            {
                String item = list.getString(i);
                if (!item.equals(toUser))
                {
                    novaLista.put(item);
                }
            }
            userObj.put(folderName, novaLista);
            json.put(fromUser, userObj);
            salvarJSON(json);
        }
    }
}
