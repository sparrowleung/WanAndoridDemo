package com.learning.common.weight

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import com.learning.common.R

class MToast {

    private var mToast: Toast? = null
    private var mContext: Context

    constructor(@NonNull context: Context, @NonNull charSequence: CharSequence, duration: Int) {
        mContext = context.applicationContext

        var view = LayoutInflater.from(mContext).inflate(R.layout.layout_mtoast, null)
        view.findViewById<TextView>(R.id.toast_txt).text = charSequence

        mToast = Toast(mContext)
        mToast?.duration = duration
        mToast?.view = view
    }

    companion object {
        fun makeText(@NonNull context: Context, @NonNull charSequence: CharSequence, duration: Int): MToast {
            return MToast(
                    context,
                    charSequence,
                    duration
            )
        }

        fun makeText(@NonNull context: Context, @NonNull charSequence: CharSequence): MToast {
            return MToast(
                    context,
                    charSequence,
                    Toast.LENGTH_SHORT
            )
        }

        fun makeText(@NonNull context: Context, @StringRes value: Int, duration: Int): MToast {
            return MToast(
                    context,
                    context.getString(value),
                    duration
            )
        }

        fun makeText(@NonNull context: Context, @StringRes value: Int): MToast {
            return MToast(
                    context,
                    context.getString(value),
                    Toast.LENGTH_SHORT
            )
        }
    }

    public fun show() {
        mToast?.show()
    }
}