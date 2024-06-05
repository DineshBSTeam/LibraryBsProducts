package com.lib.bslibrary.internal.products.productDetail.tabs

import ProductContentListAdapter
import ProductDetailTabsContent
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lib.bslibrary.R
import com.lib.bslibrary.databinding.ProductDetailTabContentBinding
import com.lib.bslibrary.internal.products.productDetail.ProductDetailViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import getVideoId

class ProductDetailTabContent(content: List<ProductDetailTabsContent>?) : Fragment() {
    private val contentData: List<ProductDetailTabsContent>? = content
    private var _binding: ProductDetailTabContentBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter : ProductContentListAdapter

    private lateinit var viewModel: ProductDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductDetailTabContentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        activity?.let {
            viewModel = ViewModelProvider(it)[ProductDetailViewModel::class.java]
        }

        adapter = ProductContentListAdapter(onClickListener = this::showVideoPlayerAlertDialog)
        binding.contentList.adapter = adapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setContentData(contentData)
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showVideoPlayerAlertDialog(url: String) {
        val builder = AlertDialog.Builder(context,R.style.CustomAlertDialog)
            .create()
        val dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_video_player, null)
        val youTubePlayerView: YouTubePlayerView = dialogLayout.findViewById(R.id.youtube_player_view)
        val playerClose: AppCompatImageView = dialogLayout.findViewById(R.id.closeDialog)
        playerClose.setOnClickListener {
            builder.dismiss()
        }
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = getVideoId(url)
                youTubePlayer.loadVideo(videoId.toString(), 0f)
            }
        })

        builder.setView(dialogLayout)
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

}