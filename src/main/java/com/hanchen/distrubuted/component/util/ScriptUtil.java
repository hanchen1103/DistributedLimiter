package com.hanchen.distrubuted.component.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ScriptUtil {

    public static String loadScript(String path) {

        StringBuilder sb = new StringBuilder();

        InputStream stream = ScriptUtil.class.getClassLoader().getResourceAsStream(path);
        try {
            assert stream != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))){
                String str;
                while ((str = br.readLine()) != null) {
                    sb.append(str).append(System.lineSeparator());
                }

            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return sb.toString();
    }

}
