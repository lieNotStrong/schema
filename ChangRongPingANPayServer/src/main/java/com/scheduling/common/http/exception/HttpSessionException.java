package com.scheduling.common.http.exception;

public class HttpSessionException extends RuntimeException {
  public HttpSessionException(String message) {
    super(message);
  }

  public HttpSessionException(Throwable ex) {
    super(ex);
  }
}
