package com.grand.ezkorone.core.extensions

import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import java.io.Serializable

fun Bundle?.orEmpty(): Bundle = this ?: Bundle()

/**
 * puts 1 [value] with given [key] in a bundle.
 *
 * @throws IllegalArgumentException When a value is not a supported type of [Bundle].
 */
fun Bundle.addValue(key: String?, value: Any?) {
    when(value) {
        null -> putString(key, null) // Any nullable type will suffice.

        // Primitives
        is Boolean -> putBoolean(key, value)
        is Byte -> putByte(key, value)
        is Char -> putChar(key, value)
        is Double -> putDouble(key, value)
        is Float -> putFloat(key, value)
        is Int -> putInt(key, value)
        is Long -> putLong(key, value)
        is Short -> putShort(key, value)

        // Other
        is CharSequence -> putCharSequence(key, value) // contains ( String )
        is Bundle -> putBundle(key, value)
        is Parcelable -> putParcelable(key, value)

        // Primitives arrays
        is BooleanArray -> putBooleanArray(key, value)
        is ByteArray -> putByteArray(key, value)
        is CharArray -> putCharArray(key, value)
        is DoubleArray -> putDoubleArray(key, value)
        is FloatArray -> putFloatArray(key, value)
        is IntArray -> putIntArray(key, value)
        is LongArray -> putLongArray(key, value)
        is ShortArray -> putShortArray(key, value)

        // Other arrays
        is Array<*> -> {
            val componentType = value::class.java.componentType
                ?: throw IllegalArgumentException("Illegal value array type null for key \"$key\"")
            @Suppress("UNCHECKED_CAST") // Checked by reflection.
            when {
                Parcelable::class.java.isAssignableFrom(componentType) -> {
                    // contains ( SparseArray )
                    putParcelableArray(key, value as Array<Parcelable>)
                }
                String::class.java.isAssignableFrom(componentType) -> {
                    putStringArray(key, value as Array<String>)
                }
                CharSequence::class.java.isAssignableFrom(componentType) -> {
                    putCharSequenceArray(key, value as Array<CharSequence>)
                }
                Serializable::class.java.isAssignableFrom(componentType) -> {
                    putSerializable(key, value)
                }
                else -> {
                    val valueType = componentType.canonicalName
                    throw IllegalArgumentException(
                        "Illegal value array type $valueType for key \"$key\"")
                }
            }
        }

        // Serializable contains ( Enum, All Arrays )
        is Serializable -> putSerializable(key, value)

        else -> {
            @Suppress("UNCHECKED_CAST")
            if (Build.VERSION.SDK_INT >= 18 && value is IBinder) {
                putBinder(key, value)
            }else if (Build.VERSION.SDK_INT >= 21 && value is Size) {
                putSize(key, value)
            }else if (Build.VERSION.SDK_INT >= 21 && value is SizeF) {
                putSizeF(key, value)
            }else if (value is SparseArray<*> && value as? SparseArray<Parcelable> != null) {
                putSparseParcelableArray(key, value)
            }else {
                val valueType = value.javaClass.canonicalName
                throw IllegalArgumentException("Illegal value type $valueType for key \"$key\"")
            }
        }
    }
}
