package com.marvel.stark.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marvel.stark.R
import com.marvel.stark.room.Payout
import com.marvel.stark.utils.Formatter
import kotlinx.android.synthetic.main.item_payout.view.*
import java.text.SimpleDateFormat
import java.util.*

/**Created by Jahongir on 6/25/2019.*/

class PayoutsAdapter : RecyclerView.Adapter<PayoutsAdapter.ViewHolder>() {

    private var payoutsList = emptyList<Payout>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.item_payout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = payoutsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(payoutsList[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Payout) = with(itemView) {
            val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            val date = formatter.format(Date(item.paidOn * 1000))
            payoutDate.text = date
            payoutAmount.text = Formatter.unpaid(item.amount)
            //payoutDuration.text = item.duration
        }
    }

    fun update(payouts: List<Payout>) {
        this.payoutsList = payouts
        notifyDataSetChanged()
    }
}