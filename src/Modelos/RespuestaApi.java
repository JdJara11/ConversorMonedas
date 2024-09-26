package Modelos;

import java.util.Map;

public class RespuestaApi {
    private String result;
    private String base_code;
    private Map<String, Double> conversion_rates;

    public RespuestaApi(DatosApi datosApi) {
        this.result = datosApi.getResult();
        this.base_code = datosApi.getBase_code();
        this.conversion_rates = datosApi.getConversion_rates();
    }

    public Map<String, Double> getConversionRates() {
        return conversion_rates;
    }

    @Override
    public String toString() {
        return "RespuestaApi{" +
                "result='" + result + '\'' +
                ", base_code='" + base_code + '\'' +
                ", conversion_rates=" + conversion_rates +
                '}';
    }
}

