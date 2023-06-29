package org.itstep.lessonsecuritylib.command;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record PublisherCommand(
        @NotBlank @Length(min = 3) String name
) {
}

