package entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Element {
    private int code;
    private String name;
    private String quantity;
}
