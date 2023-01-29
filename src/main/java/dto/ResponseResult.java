package dto;

import java.util.Objects;

public class ResponseResult<T> {

    /**
     * 2.	В пакете dto (Data Transfer Object) создать generic класс ResponseResult
     * с полями: result типа boolean, message типа String, object типа T.
     * Во всех API производить возврат json данного объекта, не выгружая null поля.
     * В случае успеха: result true, message null, object – тот объект, который требуется вернуть в конкретном API.
     * В случае ошибки: result false, message c указанием ошибки, object null
     */

    /**
     * 3.	Для сущностей Student и Auto реализовать сервлеты, которые задают API для:
     * •	Добавления. Принимается json объект этой сущности без id
     * •	Удаления. Принимается id сущности
     * •	Обновления. Принимается json объект этой сущности
     * •	Получения данных (по id и для всех). Либо не принимаются параметры, либо принимается id сущности
     * 	Все методы API должны возвращать json объект ResponseResult
     */
    private boolean result;
    private String message;
    private T object;

    public ResponseResult(boolean result, String message, T object) {
        this.result = result;
        this.message = message;
        this.object = object;
    }

    public ResponseResult() {
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

}