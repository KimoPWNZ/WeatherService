import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService {

    private static final String API_KEY = "865b4ea1-dfca-4e13-bada-01e08d82643a";
    private static final String BASE_URL = "https://api.weather.yandex.ru/v2/forecast?lat=55.75&lon=37.62";

    public static void main(String[] args) {

        try {
            String jsonResponse = sendGetRequest();
            System.out.println("Ответ от сервиса: " + jsonResponse);
            int currentTemperature = extractCurrentTemperature(jsonResponse);
            System.out.println("Текущая температура: " + currentTemperature + "°C");
            int[] temperatures = {7,8,8,4,14,13,11,9,7};
            double averageTemp = calculateAverageTemperature(temperatures);
            System.out.println("Средняя температура: " + averageTemp + "°C");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String sendGetRequest() throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-Yandex-Weather-Key", API_KEY);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    private static int extractCurrentTemperature(String jsonResponse) {
        String tempKey = "\"temp\":";
        int startIndex = jsonResponse.indexOf(tempKey) + tempKey.length();
        int endIndex = jsonResponse.indexOf(",", startIndex);
        return Integer.parseInt(jsonResponse.substring(startIndex, endIndex));
    }

    private static double calculateAverageTemperature(int[] temperatures) {
        int sum = 0;
        for (int temp : temperatures) {
            sum += temp;
        }
        return (double) sum / temperatures.length;
    }
}