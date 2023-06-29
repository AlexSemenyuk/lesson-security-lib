package org.itstep.lessonsecuritylib.command;

import java.util.List;

public record BookCommand(
        String title,
        Integer publisherId,
        List<Integer> authorsIds
) {
}

