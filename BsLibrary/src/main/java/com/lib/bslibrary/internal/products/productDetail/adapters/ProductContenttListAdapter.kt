import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lib.bslibrary.databinding.AdapterContentListBinding
import com.lib.bslibrary.internal.helper.Util
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target


typealias YouTubeClickListener = (String) -> Unit

class ProductContentListAdapter(
    private val onClickListener: YouTubeClickListener
) : RecyclerView.Adapter<ProductContentViewHolder>() {
    private var contentData = mutableListOf<ProductDetailTabsContent>()

    @SuppressLint("NotifyDataSetChanged")
    fun setContentData(contents: List<ProductDetailTabsContent>?) {
        if (contents != null) {
            this.contentData = contents.toMutableList()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductContentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterContentListBinding.inflate(inflater, parent, false)
        return ProductContentViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ProductContentViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        holder.binding.titleText.text = contentData[position].title
        holder.binding.contentText.text = contentData[position].content

        val vId : String = getVideoId(contentData[position].video ?: "") ?: ""
        val url = thumbnail(vId)
        Util().loadNetworkImage(context,
                url, holder.binding.youTubeThumbnail)

        holder.binding.youTubeViewIcon.visibility = if(contentData[position].video == null || contentData[position].video.equals("")) View.GONE else View.VISIBLE
        holder.binding.youTubeView.visibility = if(contentData[position].video == null || contentData[position].video.equals("")) View.GONE else View.VISIBLE
        holder.binding.contentText.visibility = if(contentData[position].content == null || contentData[position].content.equals("")) View.GONE else View.VISIBLE

        holder.binding.youTubeViewIcon.setOnClickListener{onClickListener(contentData[position].video ?: "")}
        holder.binding.youTubeView.setOnClickListener{onClickListener(contentData[position].video ?: "")}
    }

    override fun getItemCount(): Int {
        return contentData.size
    }
}

fun thumbnail(videoId: String): String {
    return "https://img.youtube.com/vi/$videoId/mqdefault.jpg"
}

fun getVideoId(url : String): String? {
    fun isValidId(id: String?): Boolean = Regex("""^[_\-a-zA-Z\d]{11}$""").matches(id ?: "")

    return try {
        val uri = Uri.parse(url)
        when {
            !listOf("https", "http").contains(uri.scheme) -> null
            listOf("youtube.com", "www.youtube.com", "m.youtube.com").contains(uri.host) && uri.pathSegments.isNotEmpty() && uri.pathSegments.first() == "watch" && uri.getQueryParameter("v") != null -> {
                val videoId = uri.getQueryParameter("v")
                if (isValidId(videoId)) videoId else ""
            }
            uri.host == "youtu.be" && uri.pathSegments.isNotEmpty() -> {
                val videoId = uri.pathSegments.first()
                if (isValidId(videoId)) videoId else ""
            }
            else -> ""
        }
    } catch (e: Exception) {
        ""
    }
}

class ProductContentViewHolder(val binding: AdapterContentListBinding) :
    RecyclerView.ViewHolder(binding.root)