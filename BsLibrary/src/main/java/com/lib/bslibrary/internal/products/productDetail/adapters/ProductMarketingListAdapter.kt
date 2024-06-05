import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.lib.bslibrary.databinding.AdapterProductMarketingListBinding
import com.lib.bslibrary.internal.helper.Util
import com.lib.bslibrary.internal.products.ImagePreviewSheet

class ProductMarketingListAdapter : RecyclerView.Adapter<ProductMarketingViewHolder>() {

    var marketingList = mutableListOf<String?>()
    @SuppressLint("NotifyDataSetChanged")
    fun setMarketingData(data: List<String>) {
        this.marketingList = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductMarketingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterProductMarketingListBinding.inflate(inflater, parent, false)
        return ProductMarketingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductMarketingViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        Util().loadNetworkImage(context, marketingList[position] ?: "", holder.binding.imgMarketing)
        holder.binding.imgMarketing.setOnClickListener{
            val bottomSheetDialogFragment = ImagePreviewSheet(null,marketingList[position])
            bottomSheetDialogFragment.show(
                (context as AppCompatActivity).supportFragmentManager,
                "ImagePreviewSheet"
            )
        }
    }

    override fun getItemCount(): Int {
        return marketingList.size
    }
}

class ProductMarketingViewHolder(val binding: AdapterProductMarketingListBinding) : RecyclerView.ViewHolder(binding.root) {
}