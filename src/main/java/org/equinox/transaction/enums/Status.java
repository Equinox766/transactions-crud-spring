package org.equinox.transaction.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Estado de la transacción")
public enum Status {
    @Schema(description = "01: Pendiente")
    PENDIENTE,

    @Schema(description = "02: Liquidada")
    LIQUIDADA,

    @Schema(description = "03: Rechazada")
    RECHAZADA,

    @Schema(description = "04: Cancelada")
    CANCELADA;
}