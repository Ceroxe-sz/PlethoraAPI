package asia.ceroxe.chatgpt;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Gpt3Util {
    private String apiKey;

    public Gpt3Util(String apiKey) {
        this.apiKey = apiKey;
    }

    public String generateText(String prompt) {
        return generateText(apiKey, prompt, 0, true);
    }

    public String generateText(String prompt, int maxTokens) {
        return generateText(apiKey, prompt, maxTokens, false);
    }

    public String generateText(String prompt, boolean unlimitedLength) {
        return generateText(apiKey, prompt, 0, unlimitedLength);
    }

    public static String generateText(String apiKey, String prompt, int maxTokens, boolean unlimitedLength) {
        // Ensure maxTokens is not negative
        if (maxTokens < 0) {
            maxTokens = 0;
        }

        try {
            URL url = new URL("https://api.openai.com/v1/engines/davinci/completions");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            // Configure max_tokens based on the provided parameters
            int actualMaxTokens = unlimitedLength ? 3000 : maxTokens;
            String data = "{\"prompt\":\"" + prompt + "\",\"max_tokens\":" + actualMaxTokens + "}";
            byte[] postData = data.getBytes(StandardCharsets.UTF_8);

            connection.setDoOutput(true);
            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.write(postData);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            } else {
                System.out.println("HTTP Request Failed with error code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
