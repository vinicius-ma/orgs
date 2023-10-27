package br.com.vinma.orgs.database.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

class Converter {
    @TypeConverter
    fun fromBigDecimal(value: Double?): BigDecimal {
        return value?.let { BigDecimal(value.toString())} ?: BigDecimal.ZERO
    }

    @TypeConverter
    fun bigDecimalToDouble(value: BigDecimal?): Double? {
        return value?.let { value.toDouble() }
    }
}