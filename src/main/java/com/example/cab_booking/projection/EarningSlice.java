// src/main/java/com/example/cab_booking/projection/EarningSlice.java
package com.example.cab_booking.projection;

import java.time.LocalDate;

public interface EarningSlice {
    LocalDate getDay();     // column alias “day”
    Double    getTotal();   // SUM(amount)
}
