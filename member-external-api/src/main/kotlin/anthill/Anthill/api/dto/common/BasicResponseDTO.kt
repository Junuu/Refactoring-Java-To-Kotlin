package anthill.Anthill.api.dto.common

data class BasicResponseDTO<Type>(
    val message : String? = null,
    val responseData : Type? = null,
    val errorMessage : String? = null,
)
