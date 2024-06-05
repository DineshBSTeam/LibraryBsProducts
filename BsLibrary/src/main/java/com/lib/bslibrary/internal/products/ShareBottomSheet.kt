package com.lib.bslibrary.internal.products

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Outline
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.view.*
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import com.lib.bslibrary.databinding.ShareBottomsheetBinding
import com.lib.bslibrary.internal.helper.Util
import com.lib.bslibrary.internal.products.productList.models.ShareDataModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ShareBottomSheet(
    private val shareData: ShareDataModel?,
) : BottomSheetDialogFragment() {

    private var sheetBehavior: BottomSheetBehavior<*>? = null
    private var _binding: ShareBottomsheetBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ShareBottomsheetBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        _binding!!.scrollLayout.layoutParams.height = (height * 0.6).toInt()
        sheetBehavior = BottomSheetBehavior.from(_binding!!.bottomSheetLayout)
        (sheetBehavior as BottomSheetBehavior<*>).peekHeight = height

        return root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var sheetDismiss = true
        _binding!!.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { nestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY < oldScrollY) {
                nestedScrollView.scrollTo(0, 0)
                sheetDismiss = true
            } else {
                sheetDismiss = false
            }
        })

        binding.nestedScrollView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.parent.requestDisallowInterceptTouchEvent(false)
                    if (sheetDismiss) {
                        dismiss()
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                }

                MotionEvent.ACTION_UP -> {
                    v.parent.requestDisallowInterceptTouchEvent(false)
                }

                else -> {}
            }
            v.onTouchEvent(event)
            true
        }

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

        Util().loadNetworkImage(
            requireActivity(),
            shareData?.shareImage ?: "",
            _binding!!.shareImage
        )
        _binding!!.contentText.text = createMessageText()
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