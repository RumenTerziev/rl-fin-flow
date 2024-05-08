package bg.lrsoft.rlfinflow.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PageResult<T> {
    List<T> items;
    long totalRecords;
}
