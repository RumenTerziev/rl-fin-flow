package bg.lrsoft.rlfinflow.service;

import org.springframework.http.ResponseEntity;

public interface IRestService {

    <T> ResponseEntity<T> getForEntity(String url, Class<T> entityType);
}
