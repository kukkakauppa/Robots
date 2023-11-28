package config;

import java.beans.PropertyVetoException;
import java.util.Map;

// Состояние компонента (например, окна с событиями, с игровым полем)
public interface State {
    // Получить состояние компонента в виде словаря:
    // width -> 100
    // height -> 200
    Map<String, String> getState(); // Получить текущее состояние компонента в виде словаря

    // Изменить состояние компонента на загруженное из файла.
    // Установить текущее состояние компонента из словаря
    void setState(Map<String, String> state) throws PropertyVetoException;
}
