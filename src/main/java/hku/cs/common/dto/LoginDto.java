package hku.cs.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginDto implements Serializable {
    @NotBlank(message = "Not Empty")
    private String username;

    @NotBlank(message = "Not Empty")
    private String password;
}
