package com.template.utils

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Matcher
import java.util.regex.Pattern

class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) : InputFilter {
    private var mPattern: Pattern =
        Pattern.compile(
            "-?[0-9]{0,$digitsBeforeZero}+((\\.[0-9]{0," + digitsAfterZero
                .toString() + "})?)||(\\.)?"
        )

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int,
    ): CharSequence? {

        val replacement = source.subSequence(start, end).toString()

        val newVal = dest.subSequence(0, dstart).toString() + replacement + dest.subSequence(
            dend,
            dest.length
        ).toString()

        val matcher: Matcher = mPattern.matcher(newVal)
        if (matcher.matches()) return null
        return if (source.isEmpty())
            dest.subSequence(dstart, dend)
        else
            ""
    }
}