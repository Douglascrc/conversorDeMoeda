import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import io.github.cdimascio.dotenv.Dotenv;


public class Conversor {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("EXCHANGE_RATE_API_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("API key not found in .env file");
            return;
        }

        Scanner leitor = new Scanner(System.in);
        String moeda = "";

        while (!moeda.equalsIgnoreCase("sair")){
            System.out.println("Digite que moeda deseja consultar (ex: USD, EUR, BRL etc...): ");
            moeda = leitor.nextLine().trim().toUpperCase();

            if(moeda.equalsIgnoreCase("sair")) {
                System.out.println("Saindo do programa...");
                break;
            }

            String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + moeda;
            try {
                HttpClient client = HttpClient.newHttpClient();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(java.net.URI.create(url))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println("Response status code: " + response.statusCode());
                System.out.println("Response body: " + response.body());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}