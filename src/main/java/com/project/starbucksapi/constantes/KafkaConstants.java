package com.project.starbucksapi.constantes;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaConstants {

    public static final String CORRELATION_ID = "correlation_id";
    public static final String TENTATIVA = "tentativa";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String DELIVERY_ID = "delivery_id";
    public static final String INSTALLMENT_NUMBER = "installment_number";
}