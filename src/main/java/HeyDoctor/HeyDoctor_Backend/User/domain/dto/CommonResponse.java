package com.example.signup.dto;

public class CommonResponse<T> {
    private boolean error;
    private T data;

    public CommonResponse() {}

    public CommonResponse(boolean error, T data) {
        this.error = error;
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
