package bg.lrsoft.rlfinflow.service;

import org.springframework.http.ResponseEntity;

public interface RestService {

    <T> ResponseEntity<T> getForEntity(String url, Class<T> entityType);

    <T> ResponseEntity<T> asd(Object requestBody, String url, Class<T> entityType);
}
