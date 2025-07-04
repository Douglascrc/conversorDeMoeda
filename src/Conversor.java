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
        int opcao = 0;

        while (opcao != 7){
           System.out.println(
                   "\nSeja bem-vindo/a ao Conversor de Moeda +\n" +
                           "1) Dólar -> Peso argentino\n" +
                           "2) Peso argentino -> Dólar\n" +
                           "3) Dolar -> Real brasileiro\n" +
                           "4) Real brasileiro -> Dólar\n" +
                           "5) Dólar -> Peso colombiano\n" +
                           "6) Peso colombiano -> Dólar\n" +
                           "7) Sair\n" +
                           "Escolha uma opção válida: "
           );
           try {
               opcao = Integer.parseInt(leitor.nextLine().trim());

               if (opcao == 7) {
                   System.out.println("Saindo do programa...");
                   break;
               }

               if (opcao < 1 || opcao > 7) {
                   System.out.println("Opção inválida! Por favor, escolha uma opção entre 1 e 7.");
                   continue;
               }

               String fromCurrency = "";
               String toCurrency = "";

               switch (opcao) {
                   case 1:
                       fromCurrency = "USD";
                       toCurrency = "ARS";
                       break;
                   case 2:
                       fromCurrency = "ARS";
                       toCurrency = "USD";
                       break;
                   case 3:
                       fromCurrency = "USD";
                       toCurrency = "BRL";
                       break;
                   case 4:
                       fromCurrency = "BRL";
                       toCurrency = "USD";
                       break;
                   case 5:
                       fromCurrency = "USD";
                       toCurrency = "COP";
                       break;
                   case 6:
                       fromCurrency = "COP";
                       toCurrency = "USD";
                       break;
               }

               System.out.print("Digite o valor a ser convertido: ");
               double amount = Double.parseDouble(leitor.nextLine().trim());

               String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + fromCurrency + "/" + toCurrency + "/" + amount;

               HttpClient client = HttpClient.newHttpClient();

               HttpRequest request = HttpRequest.newBuilder()
                       .uri(java.net.URI.create(url))
                       .build();
               HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

               String responseBody = response.body();

               if (responseBody.contains("conversion_result")) {
                   int startIndex = responseBody.indexOf("conversion_result") + "conversion_result".length() + 1;
                   int endIndex = responseBody.indexOf("}", startIndex);
                   String resultValue = responseBody.substring(startIndex, endIndex);

                   resultValue = resultValue.startsWith(":") ? resultValue.substring(1).trim() : resultValue.trim();

                   System.out.println("\nO valor convertido é: " + resultValue + " " + toCurrency);
               } else {
                     System.out.println("Erro ao obter a taxa de câmbio. Verifique a chave da API e tente novamente.");
               }
           } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Por favor, insira um número válido.");
              } catch (Exception e) {
                System.out.println("Ocorreu um erro: " + e.getMessage());
           }
        }

        leitor.close();
    }
}