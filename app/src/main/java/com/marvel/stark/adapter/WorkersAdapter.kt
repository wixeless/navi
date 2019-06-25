package com.marvel.stark.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.marvel.stark.R
import com.marvel.stark.room.Worker
import com.marvel.stark.utils.Formatter
import kotlinx.android.synthetic.main.item_worker.view.*
import java.util.concurrent.TimeUnit

/**Created by Jahongir on 6/25/2019.*/

class WorkersAdapter : RecyclerView.Adapter<WorkersAdapter.ViewHolder>() {
    private var workersList = emptyArray<Worker>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_worker, parent, false))
    }

    override fun getItemCount() = workersList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(workersList[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Worker) = with(itemView) {
            workerName.text = item.name
            hashrateCurrent.text = Formatter.hashrate(item.currentHashrate)
            hashrateReported.text = Formatter.hashrate(item.reportedHashrate)
            workerLastSeen.text = timeDiff(item.lastSeen)
            //hashrateAverage.text = item.hashrateAverage
        }

        private fun timeDiff(lastSeen: Long): String {
            val now = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
            val diff = now - lastSeen
            val min = TimeUnit.SECONDS.toMinutes(diff)
            return "$min min ago"
        }
    }

    fun update(workers: List<Worker>) {
        this.workersList = workers.toTypedArray()
        notifyDataSetChanged()
    }
}