package config;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

// Код записи словаря в файл и чтения словаря из файла
// Сохранение и загрузка конфигураций в файл
public class ConfigFile {
    private final Path file;

    // При старте указываем файл из которого читать и в который писать конфигурацию
    public ConfigFile(Path file) {
        this.file = file;
    }

    // Сохранить конфигурацию
    public void save(Config config) throws IOException {
        // Открываем файл на запись
        try (BufferedWriter w = Files.newBufferedWriter(file)) {
            // Получаем все ключи из конфигурации в виде массива строк
            String[] keys = config.data.keySet().toArray(new String[0]);
            Arrays.sort(keys); // Сортируем ключи в алфавитном порядке
            // Записываем Ключ = Значение в файл, каждый ключ в своей строке
            for (String key : keys)
                w.write(key + " = " + config.data.get(key) + "\n");
        }
        System.out.println("Конфигурация записана: " + file.toAbsolutePath());
    }

    public Config tryLoad() {
        try { // Пробуем загрузить конфигурацию из файла
            return load(); // Вызываем чтение
        } catch (IOException e) { // Если чтение не получилось
            return new Config(); // Создаём новую конфигурацию
        }
    }

    public Config load() throws IOException { // Загрузка конфигурации из файла
        Config config = new Config(); // Создаём новую конфигурацию
        // Открываем файл на чтение
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line; // Очередная строчка файла
            // Читаем файл построчно
            while ((line = reader.readLine()) != null) { // Пока удаётся прочитать строчку
                if (line.isEmpty()) continue; // Пропускаем пустые строчки
                if (line.startsWith("#")) continue; // Пропускаем строчки начинающиеся с '#'
                // Удаляем начальные и конечные пробелы, делим строчки по знаку равно ('=')
                String[] s = line.trim().split("=");
                if (s.length != 2) { // Если не 2 кусочка получилось => пропускаем строку
                    System.out.println("Не могу прочитать строку: " + line + " файла " + file);
                    continue;
                }
                // Если получилось ровно 2 кусочка => добавляем в общий словарь
                config.data.put(s[0].trim(), s[1].trim());
            }
        }
        // Сообщаем что прочитали конфигурацию из файла
        System.out.println("Конфигурация прочитана: " + file.toAbsolutePath());
        return config;
    }
}
