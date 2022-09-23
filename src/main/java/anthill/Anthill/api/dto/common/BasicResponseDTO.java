package anthill.Anthill.api.dto.common;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BasicResponseDTO<Type> {
    private String message;
    private Type responseData;
    private String errorMessage;

    @Builder
    public BasicResponseDTO(String message, Type responseData, String errorMessage) {
        this.message = message;
        this.responseData = responseData;
        this.errorMessage = errorMessage;
    }
}
