import Modelos.Api;
import Modelos.DatosApi;
import Modelos.RespuestaApi;
import com.google.gson.*;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String result = null;
        System.out.print("Ingrese la moneda de origen (ej: USD): ");
        String monedaOrigen = sc.next().toUpperCase();

        System.out.print("Ingrese la moneda de destino (ej: EUR): ");
        String monedaCambio = sc.next().toUpperCase();

        System.out.print("Ingrese el monto a convertir: ");
        double monto = sc.nextDouble();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        //Llamo la apikey desde la clase
        Api api = new Api();
        String apiKey = api.getApi_key();
        String direccion = "https://v6.exchangerate-api.com/v6/"+apiKey+"/latest/" + monedaOrigen;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(direccion))
                    .build();
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();
            //System.out.println(json);

            DatosApi datosApi = gson.fromJson(json, DatosApi.class);
            //System.out.println(datosApi);

            RespuestaApi respuestaApi = new RespuestaApi(datosApi);
            //System.out.println(respuestaApi);

            Map<String, Double> tasa = respuestaApi.getConversionRates();
            if (tasa.containsKey(monedaCambio)) {
                double conversionTasa = tasa.get(monedaCambio);
                double conversionMonto = monto * conversionTasa;

                result = String.format("%.2f %s equivalen a %.2f %s%n", monto, monedaOrigen, conversionMonto, monedaCambio);
                System.out.println(result);
            } else {
                System.out.println("La tasa de cambio para " + monedaCambio + " no está disponible.");
            }
        }catch (Exception e){
            System.out.println("Error al leer la URL" + e.getMessage());
        }
        if (result != null) {
            try (FileWriter escritura = new FileWriter("historial_conversiones.txt")) {
                escritura.write(result);
            } catch (IOException e) {
                System.out.println("Error al escribir en el archivo: " + e.getMessage());
            }
        }
    }
}