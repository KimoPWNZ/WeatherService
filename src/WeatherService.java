import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherService {

    private static final String API_KEY = "865b4ea1-dfca-4e13-bada-01e08d82643a";
    private static final String API_URL = "https://api.weather.yandex.ru/v2/forecast";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите широту: ");
        double lat = scanner.nextDouble();
        System.out.print("Введите долготу: ");
        double lon = scanner.nextDouble();
        System.out.print("Введите количество дней: ");
        int limit = scanner.nextInt();

        try {
            String response = getWeatherData(lat, lon);
            JSONObject jsonResponse = new JSONObject(response);

            System.out.println(jsonResponse.toString());

            double currentTemp = jsonResponse.getJSONObject("fact").getDouble("temp");
            System.out.println("Текущая температура: " + currentTemp + "°C");

            double averageTemp = calculateAverageTemperature(jsonResponse, limit);
            System.out.println("Средняя температура за " + limit + " дней: " + averageTemp + "°C");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getWeatherData(Double lat, Double lon) throws Exception {
        URL url = new URL(API_URL + "?lat=" + lat + "&lon=" + lon);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-Yandex-Weather-Key", API_KEY);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private static double calculateAverageTemperature(JSONObject jsonResponse, int limit) {
        double totalTemp = 0;
        int count = 0;

        for (int i = 0; i < limit; i++) {
            if (jsonResponse.getJSONArray("forecasts").length() > i) {
                JSONObject forecast = jsonResponse.getJSONArray("forecasts").getJSONObject(i);
                double dayTemp = forecast.getJSONObject("parts").getJSONObject("day").getDouble("temp_avg");
                totalTemp += dayTemp;
                count++;
            }
        }

        return count > 0 ? totalTemp / count : 0;
    }
}