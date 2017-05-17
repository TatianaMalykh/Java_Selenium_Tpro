package config;

import org.apache.commons.lang3.ArrayUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

public class Configuration {
    public EnvironmentConfiguration environment;
    public ParametersConfiguration params;
    public CardsConfiguration cards;

    public void load() throws IOException {
        try {
            environment = read("src/test/resources/config.environment.yml", EnvironmentConfiguration.class);
            params = read("src/test/resources/config.parameters.yml", ParametersConfiguration.class);
            cards = read("src/test/resources/config.cards.yml", CardsConfiguration.class);
        } catch (IOException e) {
            System.out.println("ERROR: cannot parse config file: " + e.getMessage());
            throw e;
        }
    }

    public String[][] getInitParams(String caseName) {
        return ArrayUtils.addAll(params.accounts.get(caseName).fields, params.initParameters);
    }

    public String[][] getChargeParams(String[][] card) {
        return ArrayUtils.addAll(params.chargeParameters, card);
    }

    public String[][] getChargeHoldParams(String caseName) {
        String[][] guid = {params.accounts.get(caseName).fields[0]}; // only GUID is required
        return ArrayUtils.addAll(guid, params.chargeHoldParameters);
    }

    private <T> T read(String path, Class<T> c) throws IOException {
        Yaml yaml = new Yaml();
        try (InputStream in = java.nio.file.Files.newInputStream(Paths.get(path))) {
            return yaml.loadAs(in, c);
        }
    }
}
