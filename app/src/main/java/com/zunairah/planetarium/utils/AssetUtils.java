package com.zunairah.planetarium.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetUtils {
    /**
     * Read a text file from assets
     *
     * @param context Application context
     * @param fileName Name of the file in assets
     * @return String content of the file
     * @throws IOException If file cannot be read
     */
    public static String readAssetFile(Context context, String fileName) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = context.getAssets().open(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        bufferedReader.close();
        inputStream.close();

        return stringBuilder.toString();
    }
}
