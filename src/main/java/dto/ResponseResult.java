package dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    private boolean result;
    private String message;
    private T data;

    public ResponseResult(boolean result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    // Constructor for error Responses
    public ResponseResult(String errorMessage){
        this.result = false;
        this.message = errorMessage;
        this.data = null;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
