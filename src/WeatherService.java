import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherService {
    private static final String API_KEY = "ваш_ключ"; // замените на ваш уникальный ключ
    private static final String API_URL = "https://api.weather.yandex.ru/v2/forecast";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Запрос координат
        System.out.print("Введите широту (lat): ");
        double lat = scanner.nextDouble();

        System.out.print("Введите долготу (lon): ");
        double lon = scanner.nextDouble();

        // Выполнение запроса
        try {
            String jsonResponse = getWeatherData(lat, lon);
            System.out.println("Ответ сервиса: " + jsonResponse);

            // Извлечение температуры
            double temperature = extractTemperature(jsonResponse);
            System.out.println("Температура: " + temperature + "°C");

            // Напоминаем о средней температуре (замените на реальные значения)
            System.out.print("Введите количество дней для средней температуры: ");
            int limit = scanner.nextInt();
            double averageTemperature = calculateAverageTemperature(jsonResponse, limit);
            System.out.println("Средняя температура за " + limit + " дней: " + averageTemperature + "°C");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static String getWeatherData(double lat, double lon) throws Exception {
        URL url = new URL(API_URL + "?lat=" + lat + "&lon=" + lon);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Yandex-Weather-Key", API_KEY);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private static double extractTemperature(String jsonResponse) {
        // Здесь нужно вручную искать значение температуры
        // В реальной задаче лучше было бы парсить JSON, например, с помощью библиотеки GSON или Jackson
        int tempIndex = jsonResponse.indexOf("\"temp\":") + 7; // находим индекс начала температуры
        int endIndex = jsonResponse.indexOf(",", tempIndex); // находим конец значения
        String tempString = jsonResponse.substring(tempIndex, endIndex);
        return Double.parseDouble(tempString);
    }

    private static double calculateAverageTemperature(String jsonResponse, int limit) {
        // Для примера возвращаем среднее значение
        // Будет реальная реализация с парсингом JSON
        // Пример вычисления будет вычисляться с учетом будущих или исторических данных
        return extractTemperature(jsonResponse) * limit / limit; // пожалуйста, замените на свою логику
    }
}