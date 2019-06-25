package com.marvel.stark.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marvel.stark.R
import com.marvel.stark.room.Wallet
import com.marvel.stark.utils.Formatter
import kotlinx.android.synthetic.main.item_wallet.view.*

class WalletsAdapter : RecyclerView.Adapter<WalletsAdapter.ViewHolder>()/*, ItemTouchHelperAdapter */ {

    private var wallets = ArrayList<Wallet>()

    private var swapListener: ((newOrders: ArrayList<Wallet>) -> Unit)? = null
    private var clickListener: ((Wallet) -> Unit)? = null

    //var touchHelper: ItemTouchHelper? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_wallet, parent, false))
    }

    override fun getItemCount() = wallets.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(wallets[position])

    /*override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(wallets, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }*/

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)/*, ItemTouchHelperViewHolder */ {

        fun bind(wallet: Wallet) = with(itemView) {
            wallet_name.text = wallet.name
            //wallet_name.text = cutPrefix(walletData.poolUrl)
            wallet_coin.text = wallet.coin.name
            wallet_workers.text = wallet.activeWorkers.toString()
            //wallet_unpaid.text = formatCurrency(walletData.unpaid)
            wallet_hash.text = Formatter.hashrate(wallet.reportedHashrate)
            wallet_wallet.text = wallet.address
            wallet_unpaid.text = Formatter.unpaid(wallet.unpaid)

            wallet_card.setOnClickListener {
                clickListener?.invoke(wallet)
            }

            /*if (walletData.isNotificationSupport) {
                wallet_alarm.show()
                if (walletData.isNotificationEnabled) {
                    wallet_alarm.setImageResource(R.drawable.ic_alarm)
                } else {
                    wallet_alarm.setImageResource(R.drawable.ic_alarm_off)
                }
            } else {
                wallet_alarm.hide()
            }*/

            /*wallet_drag.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    touchHelper?.startDrag(this@ViewHolder)
                }
                false
            }*/
        }

        /*override fun onItemSelected() {
            val card = itemView.findViewById<FrameLayout>(R.id.card_con)
            card.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhiteAlpha))
        }

        override fun onItemClear() {
            val card = itemView.findViewById<FrameLayout>(R.id.card_con)
            card.setBackgroundColor(Color.TRANSPARENT)
            swapListener(wallets)
        }*/

        private fun cutPrefix(pool: String): String {
            return pool.replace("^(http|https)://".toRegex(), "")
        }
    }


    internal fun setWalletClickListener(listener: ((Wallet) -> Unit)) {
        this.clickListener = listener
    }

    fun update(newWallets: List<Wallet>) {
        this.wallets = ArrayList(newWallets)
        notifyDataSetChanged()
    }
}