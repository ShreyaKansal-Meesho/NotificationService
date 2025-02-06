package org.shreya.notificationservicenew.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.shreya.notificationservicenew.util.Constants;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    @NotBlank(message = Constants.ERROR_EMPTY_PHONE_MESSAGE)
    @Pattern(
            regexp = Constants.REGEX_INDIAN_PHONE,
            message = Constants.ERROR_INVALID_PHONE_MESSAGE)
    private String phoneNumber;

    @NotBlank(message = Constants.ERROR_EMPTY_MESSAGE)
    @Size(max = 500, message = Constants.ERROR_LIMIT_MESSAGE)
    private String message;
}
