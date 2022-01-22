package dev.hongjun.sherbrooke

import android.R
import android.graphics.Bitmap
import android.widget.ImageView
import com.google.zxing.*
import com.journeyapps.barcodescanner.BarcodeEncoder


object QRCodeConverter {
    //https://github.com/journeyapps/zxing-android-embedded
    fun generateQRCodeFromString (input: String) : Bitmap?{
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(input, BarcodeFormat.QR_CODE, 400, 400)

            return bitmap
        } catch (e: Exception) {
        }
        return null
    }


}
