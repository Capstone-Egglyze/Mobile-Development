package com.dicoding.egglyze.view.animation

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.dicoding.egglyze.R

class LoadingDialog(private val context: Context) {
    private var dialog: Dialog? = null

    fun showLoading() {
        dialog = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.activity_loading_splash)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        dialog?.show()
    }

    fun hideLoading() {
        dialog?.dismiss()
    }
}

