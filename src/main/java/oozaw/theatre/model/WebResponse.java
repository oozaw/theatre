package oozaw.theatre.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {

    private Integer code;

    private String status;

    private T data;

    private String errors;


}
