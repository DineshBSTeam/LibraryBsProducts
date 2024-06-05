package com.lib.bslibrary.internal.products

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Outline
import android.os.Bundle
import android.os.Environment
import android.view.*
import androidx.core.content.FileProvider
import com.lib.bslibrary.internal.helper.Util
import com.lib.bslibrary.internal.products.productList.models.ShareDataModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lib.bslibrary.databinding.ImagePreviewSheetBinding
import com.lib.bslibrary.internal.helper.Constants
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImagePreviewSheet(
    private val shareData: ShareDataModel?,
    private val imagePath: String?,
) : BottomSheetDialogFragment() {
    private var _binding: ImagePreviewSheetBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ImagePreviewSheetBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val curveRadius = 40F

        _binding!!.shareImage.outlineProvider = object : ViewOutlineProvider() {

            override fun getOutline(view: View?, outline: Outline?) {
                outline?.setRoundRect(
                    0,
                    0,
                    view!!.width,
                    (view.height + curveRadius).toInt(),
                    curveRadius
                )
            }
        }

        _binding!!.shareImage.clipToOutline = true


            Util().loadNetworkImage(requireActivity(), imagePath ?: "", _binding!!.shareImage)

            //Hide for Now // use SDK Init Response for Show this
            _binding!!.agentNameFirst.text = Constants.agentName.substring(0)
            _binding!!.agentName.text = Constants.agentName
            _binding!!.agentMob.text = Constants.agentMobile

        _binding!!.shareButtonView.setOnClickListener {
            onShare(context = requireActivity(), _binding!!.captureView)
        }
    }

    private fun onShare(context: Context, view: View) {
        return try {
            val message = createMessageText()
            val bitmap = getBitmapFromView(view)
            val directory =
                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    context.getExternalFilesDir(null)
                } else {
                    context.filesDir
                }
            val fileName = "temp_share_card_${System.currentTimeMillis()}.png"
            val file = File(directory, fileName)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, message)
                type = "image/png"
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun createMessageText(): String {
        var content = ""
        if (shareData?.shareContent != null && shareData.shareContent.isNotEmpty()) {
            content += "${shareData.shareContent} "
        }
        if (shareData?.shareLink?.isNotEmpty() == true) {
            content += shareData.shareLink
        }
        return content
    }

}