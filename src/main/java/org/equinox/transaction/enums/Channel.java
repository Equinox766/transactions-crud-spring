package org.equinox.transaction.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Canal por el cual se realizó la transacción")
public enum Channel {
    @Schema(description = "Transacción vía Web")
    WEB,

    @Schema(description = "Transacción vía Cajero automático")
    CAJERO,

    @Schema(description = "Transacción en Oficina")
    OFICINA;
}
