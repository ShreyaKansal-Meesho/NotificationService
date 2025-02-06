package org.shreya.notificationservicenew.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.shreya.notificationservicenew.util.Constants;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blacklists")
public class Blacklist {

    @NotBlank(message = Constants.ERROR_EMPTY_PHONE_MESSAGE)
    @Pattern(
            regexp = Constants.REGEX_INDIAN_PHONE,
            message = Constants.ERROR_INVALID_PHONE_MESSAGE)
    @Id
    private String phoneNumber;
}
