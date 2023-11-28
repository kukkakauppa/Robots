package gui;

import config.Config;
import config.ConfigFile;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigTest {
    @Test
    void saveAndRestoreState() throws IOException {
        //Тогда мы собираем всю эту информацию в один общий словарь:
        //log.width -> 100
        //log.height -> 200
        //model.width -> 300
        //model.height -> 400

        // Окно протокола формирует словарь:
        // width -> 100
        // height -> 200
        Map<String, String> logState = new HashMap<>();
        logState.put("width", "100");
        logState.put("height", "200");
        // Окно для моделирования движения робота формирует словарь:
        // width -> 300
        // height -> 400
        Map<String, String> modelState = new HashMap<>();
        modelState.put("width", "300");
        modelState.put("height", "400");
        // Собираем в общий конфиг
        Config config = new Config();
        config.saveState("log", logState);
        config.saveState("model", modelState);

        // Сохраняем конфигурацию в файл и восстанавливаем её из файла
        ConfigFile file = new ConfigFile(Paths.get("config.txt"));
        file.save(config); // Сохраняем в файл
        Config r = file.load(); // Восстанавливаем из файла

        // Восстанавливаем состояние
        assertEquals(logState, r.loadState("log"));
        assertEquals(modelState, r.loadState("model"));
    }
}
