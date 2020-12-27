package com.example.exampleprint.utils


object UnicodeFormatter {
    fun byteToHex(b: Byte): String { // Returns hex String representation of byte b
        val hexDigit = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'
        )
        val array = charArrayOf(hexDigit[(b shl 4) and 0x0f], hexDigit[(b and 0x0f)])
        return String(array)
    }

    fun charToHex(c: Char): String { // Returns hex String representation of char c
        val hi = (c.toInt() ushr 8).toByte()
        val lo = (c.toInt() and 0xff).toByte()
        return byteToHex(hi) + byteToHex(
            lo
        )
    }

    infix fun Byte.shl(that: Int): Int = this.toInt().shl(that)
    infix fun Int.shl(that: Byte): Int = this.shl(that.toInt()) // Not necessary in this case because no there's (Int shl Byte)
    infix fun Byte.shl(that: Byte): Int = this.toInt().shl(that.toInt()) // Not necessary in this case because no there's (Byte shl Byte)

    infix fun Byte.and(that: Int): Int = this.toInt().and(that)
    infix fun Int.and(that: Byte): Int = this.and(that.toInt()) // Not necessary in this case because no there's (Int and Byte)
    infix fun Byte.and(that: Byte): Int = this.toInt().and(that.toInt()) // Not necessary in this case because no there's (Byte and Byte)
} // class

