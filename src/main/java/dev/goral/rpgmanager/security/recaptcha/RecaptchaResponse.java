package dev.goral.rpgmanager.security.recaptcha;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecaptchaResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("score")
    private float score;

    @JsonProperty("action")
    private String action;

    @JsonProperty("error-codes")
    private List<String> errorCodes;

}
