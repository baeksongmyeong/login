package hello.login.web.login;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class LoginForm {
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
}
