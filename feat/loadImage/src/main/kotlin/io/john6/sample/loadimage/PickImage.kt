package io.john6.sample.loadimage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class PickImage : ActivityResultContract<String, List<Uri>>() {
    override fun createIntent(context: Context, input: String): Intent {
        return Intent(Intent.ACTION_PICK).setType(input)
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
        if (intent == null) return emptyList()
        if (resultCode != Activity.RESULT_OK) return emptyList()
        val dataUri = intent.data
        if (dataUri != null) return listOf(dataUri)

        return intent.clipData?.let {
            val res = mutableListOf<Uri>()
            val count = it.itemCount
            for (i in 0 until count) {
                it.getItemAt(i)?.uri?.let { uri ->
                    res.add(uri)
                }
            }
            res
        }?: emptyList()
    }
}