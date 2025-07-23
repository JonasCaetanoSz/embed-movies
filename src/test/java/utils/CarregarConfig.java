package utils;

import java.io.File;
import java.io.IOException;

import org.ini4j.Ini;

public class CarregarConfig {
    public static String carregar(String config_file_name, String session, String session_key) {

        try {

            Ini ini = new Ini(new File(config_file_name));
            String key_value = ini.get(session, session_key);
            return key_value;

        } catch (IOException e) {
            String message = String.format("ERRO TEST AO CARREGAR %s:", config_file_name);
            System.err.println(message + e.getMessage());
        }
    return null;
    }
}
