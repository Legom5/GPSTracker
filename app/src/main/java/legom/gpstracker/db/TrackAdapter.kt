package legom.gpstracker.db

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import legom.gpstracker.R
import legom.gpstracker.databinding.TrackItemBinding

class TrackAdapter(private val listener: Listener) : ListAdapter<TrackItem, TrackAdapter.Holder>(Comparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_item, parent, false)
        return Holder(view, listener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(view: View, private val listener: Listener) : RecyclerView.ViewHolder(view), OnClickListener {

        private val binding = TrackItemBinding.bind(view)
        private var trackTemp: TrackItem? = null

        init {
            binding.ibDelete.setOnClickListener(this)
            binding.item.setOnClickListener(this)
        }

        fun bind(track: TrackItem) = with(binding) {
            trackTemp = track
            tvDate.text = track.date
            tvSpeed.text = track.velocity
            tvTime.text = track.time
            tvDistance.text = track.distance

        }

        override fun onClick(v: View) {
            val type = when(v.id){
                R.id.ibDelete -> ClickType.DELETE
                R.id.item -> ClickType.OPEN
                else -> ClickType.OPEN
            }
            trackTemp?.let {
                listener.onClick(it, type)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<TrackItem>() {
        override fun areItemsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener{
        fun onClick(track: TrackItem, type:ClickType)
    }

    enum class ClickType{
        DELETE,
        OPEN
    }
}