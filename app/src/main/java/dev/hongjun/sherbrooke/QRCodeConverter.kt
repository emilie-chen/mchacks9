package dev.hongjun.sherbrooke

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.google.zxing.*
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions


object QRCodeConverter {
    //https://github.com/journeyapps/zxing-android-embedded
    fun generateQRCodeFromString(input: String): Bitmap? {
        try {
            val barcodeEncoder = BarcodeEncoder()

            return barcodeEncoder.encodeBitmap(input, BarcodeFormat.QR_CODE, 400, 400)
        } catch (e: Exception) {
        }
        return null
    }





}
