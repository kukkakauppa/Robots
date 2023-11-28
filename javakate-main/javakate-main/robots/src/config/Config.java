package config;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// Работа с подсловарями (строится по исходному словарю и префиксу ключей).
// То есть реализуем класс на основе шаблона "Фильтр", за основу берем
// классы AbstractMap и AbstractSet
// (просто смотрим, какие методы нужно доопределить или переопределить).
// Конфигурация состояния для всех компонент
public class Config extends AbstractMap<String, String> {
    // Общее состояние для всех компонентов
    Map<String, String> data = new HashMap<>();
    // Все окна: имя_окна => состояние_окна
    Map<String, State> windows = new HashMap<>();

    // Регистрация компонента в конфигурации
    // Чтобы дальше он участвовал в сохранении и загрузке
    public void register(State state, String name) {
        windows.put(name, state);
    }

    // Загрузить все компоненты
    public void loadAllWindows() {
        // Пробегаем по словарю всех компонент
        for (Map.Entry<String, State> x : windows.entrySet()) {
            String windowName = x.getKey(); // Название компонента, например "log"
            State window = x.getValue(); // Ссылка на сам компонент (окно)
            try {
                window.setState(loadState(windowName));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    // Получаем состояние компонента по префиксу
    public Map<String, String> loadState(String windowName) {
        String prefix = windowName + "."; // Префикс = Название окна и точка '.' в конце
        // Создаём новый HashMap для этого компонента
        Map<String, String> r = new HashMap<>();
        // Пробегаем по общему словарю
        for (Map.Entry<String, String> x : data.entrySet()) {
            // Если ключ начинается с префикса
            String key = x.getKey();
            if (key.startsWith(prefix)) { // Если ключ начинается с префикса и дальше точка "."
                // Отрезаем префикс и используем как название ключа
                r.put(key.substring(prefix.length()), x.getValue());
            }
        }
        return r;
    }

    // Сохраняем состояние всех окон
    public void saveAllWindows() {
        for (Map.Entry<String, State> x : windows.entrySet()) {
            saveState(x.getKey(), x.getValue().getState());
        }
    }

    // Сохраняем состояние компонента с заданным префиксом
    public void saveState(String windowName, Map<String, String> state) {
        String prefix = windowName + "."; // Префикс = Название компонента и точка '.' в конце
        // Пробегаем все пары ключ-значение для переданного компонента
        for (Map.Entry<String, String> x : state.entrySet())
            // Добавляем их в общий словарь
            data.put(prefix + x.getKey(), x.getValue());
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return data.entrySet();
    }
}
