package com.util;

import com.enums.Avatar;
import com.google.gson.Gson;
import com.models.APIModels.JSONCostume;
import com.models.APIModels.JSONCostumes;
import com.models.APIModels.RequestMethod;
import com.models.APIModels.Response;
import com.models.APIModels.ResponseStatus;

import javax.net.ssl.HttpsURLConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class API {
    //// Constants ////
    private static final String apiPath = "https://kemukupu.com/api/v1";
    // private static final String apiPath = "http://localhost:8000/api/v1";

    //// Properties ////

    private Map<Avatar, Integer> costAvatars = Map.ofEntries(
            new AbstractMap.SimpleEntry<Avatar, Integer>(Avatar.DEFAULT, 0),
            new AbstractMap.SimpleEntry<Avatar, Integer>(Avatar.SAILOR, 5),
            new AbstractMap.SimpleEntry<Avatar, Integer>(Avatar.MAGICIAN, 30),
            new AbstractMap.SimpleEntry<Avatar, Integer>(Avatar.WIZARD, 5),
            new AbstractMap.SimpleEntry<Avatar, Integer>(Avatar.NINJA, 100),
            new AbstractMap.SimpleEntry<Avatar, Integer>(Avatar.QUEEN, 30),
            new AbstractMap.SimpleEntry<Avatar, Integer>(Avatar.FAIRY, 80),
            new AbstractMap.SimpleEntry<Avatar, Integer>(Avatar.PROFESSOR, 200),
            new AbstractMap.SimpleEntry<Avatar, Integer>(Avatar.ALIEN, 50),
            new AbstractMap.SimpleEntry<Avatar, Integer>(Avatar.CHEF, 20));

    //// Private Methods ////
    /**
     * Update the prices internally from the api
     * 
     * @throws IOException thrown when the service is unable to contact the server.
     */
    private void __updatePrices() throws IOException {
        Response res = API.makeRequest(RequestMethod.Get, "/costume", null, null);
        JSONCostumes costumes = new Gson().fromJson(res.getBody(), JSONCostumes.class);
        // Load and process price data
        HashMap<Avatar, Integer> priceUpdate = new HashMap<>();
        for (JSONCostume avatarName : costumes.data) {
            Avatar newAvatar = Avatar.fromString(avatarName.name);
            if (!priceUpdate.containsKey(newAvatar))
                priceUpdate.put(newAvatar, avatarName.price);
        }
        this.costAvatars = priceUpdate;
    }

    //// Public Methods ////

    public API() {

    }

    /**
     * A helper method for making http requests to the kemukupu api Docs:
     * https://kemukupu.com/api/docs
     * 
     * @param method   the http method to use
     * @param path     the subpath to request to
     * @param data     the body of the request
     * @param JWTToken The token for this user. This may be null if you wish to send
     *                 the request without authorisation.
     * @return a response instance.
     * @throws IllegalArgumentException thrown when one of the input paramaters is
     *                                  incorrect.
     * @throws IOException              thrown when the service is unable to contact
     *                                  the server.
     */
    public static Response makeRequest(RequestMethod method, String path, String data, String JWTToken)
            throws IllegalArgumentException, IOException {
        if (path == null)
            throw new IllegalArgumentException("Path must be set for request");
        if (method == null)
            throw new IllegalArgumentException("Method must be set");
        if (data == null && method == RequestMethod.Post)
            throw new IllegalArgumentException("When posting data cannot be null");
        try {
            // Establish connection
            String url = apiPath + path;
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(method.toString());
            connection.setDoOutput(true);

            // If we have an auth header, set it
            if (JWTToken != null) {
                connection.addRequestProperty("Authorisation", JWTToken);
            }

            // Add body if post request
            if (method == RequestMethod.Post) {
                byte[] out = data.getBytes(StandardCharsets.UTF_8);
                connection.setFixedLengthStreamingMode(out.length);
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.connect();
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(out);
                    os.close();
                }
            } else {
                connection.connect();
            }

            // Process Response
            int responseCode = connection.getResponseCode();
            InputStream inputStream;
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder response = new StringBuilder();
            String currentLine;

            while ((currentLine = in.readLine()) != null)
                response.append(currentLine);
            in.close();

            return new Response(response.toString(),
                    (200 <= responseCode && responseCode <= 299) ? ResponseStatus.Success : ResponseStatus.Failure, path);
        } catch (java.net.UnknownHostException e) {
            throw new IOException(e.toString());
        } catch (Exception e) {
            throw new IOException(e.toString());
        }
    }

    /**
     * Collects the price of an avatar from the MainApp.getAPI(). Note that if the
     * user hasn't logged in, this will be the default values.
     */
    public Integer getPrice(Avatar avatar) {
        try {
            this.__updatePrices();
        } catch (Exception e) {
            /* Burn Failures */ }
        return this.costAvatars.get(avatar);
    }
}
