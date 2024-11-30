package org.kuro.cat_microservice.service.exception;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CatNotFoundException extends EntityNotFoundException {
    public CatNotFoundException(String... additionalInfo) {
        super("Cat", formatAdditionalInfo(additionalInfo));
    }

    private static String formatAdditionalInfo(String... additionalInfo) {
        if (additionalInfo.length == 0) {
            return "Not found.";
        } else {
            return Arrays.stream(additionalInfo)
                    .map(info -> String.format("%s - %s not found.", info.getClass().getName(), info))
                    .collect(Collectors.joining("\n"));
        }
    }
}