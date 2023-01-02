package com.vanmove.passesger.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vanmove.passesger.R
import com.vanmove.passesger.interfaces.OnItemClickRecycler
import com.vanmove.passesger.model.AddPickupDropData


class AddPickDropViewListAdapter(var stepList: ArrayList<AddPickupDropData>, var context: Context, var onItemClickRecycler: OnItemClickRecycler) :
    RecyclerView.Adapter<AddPickDropViewListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return stepList.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.additional_pickup_dropoff_items_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val x = holder.layoutPosition
        try {
            val jsonObject=stepList[x]
            val pos=x+1
            holder.tvTitle.text = jsonObject.title+" $pos"

            if (jsonObject.address.toString().isNotEmpty())
            holder.tv_pickup.text = jsonObject.address
            else
            holder.tv_pickup.hint=  "Enter address"

            if (jsonObject.lift.toString().isNotEmpty())
                holder.pickup_lift_spinner.text = jsonObject.lift
            else
                holder.pickup_lift_spinner.hint=  "Select"

            if (jsonObject.floor_no.toString().isNotEmpty())
                holder.pickup_floor_spinner.text = jsonObject.floor_no
            else
                holder.pickup_floor_spinner.hint=  "Select"

            if (jsonObject.lift.toString().isNotEmpty())
                holder.pickup_lift_spinner.text = jsonObject.lift
            else
                holder.pickup_lift_spinner.hint=  "Select"


            if (jsonObject.address_type.toString().isNotEmpty())
                holder.pickup_address_type.text = jsonObject.address_type
            else
                holder.pickup_address_type.hint=  "Select"


            if (jsonObject.address.toString().isNotEmpty()) {
                holder.distance_tv.visibility = View.VISIBLE

            }
            else
            holder.distance_tv.visibility=View.GONE

            holder.distance_tv.text = "Distance: " + jsonObject.distance_mile + " miles"
            stepList[x].address_order=pos.toString()


        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var remove: ImageView
        var pickup_address_type:TextView
        var pickup_floor_spinner : TextView
        var pickup_lift_spinner : TextView
        var tv_proprty_type_pickup:TextView
        var distance_tv : TextView
        var tvTitle : TextView
        var tv_pickup : TextView

        init {
            pickup_address_type= itemView.findViewById(R.id.pickup_address_type)
            distance_tv= itemView.findViewById(R.id.distance_tv)
            pickup_floor_spinner = itemView.findViewById(R.id.pickup_floor_spinner)
            pickup_lift_spinner = itemView.findViewById(R.id.pickup_lift_spinner)
            tv_proprty_type_pickup = itemView.findViewById(R.id.tv_proprty_type_pickup)
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tv_pickup = itemView.findViewById(R.id.tv_pickup)
            remove = itemView.findViewById(R.id.removePickupDropoffIcon)
            remove.setOnClickListener {
                val position = adapterPosition

                try {
                    val jsonObject=stepList[position]
                    remove.tag=jsonObject
                    onItemClickRecycler.onClickRecycler(remove,position)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            pickup_address_type.setOnClickListener {
                val position = adapterPosition
                try {
                    val jsonObject=stepList[position]
                    pickup_address_type.tag=jsonObject
                    onItemClickRecycler.onClickRecycler(pickup_address_type,position)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            pickup_floor_spinner.setOnClickListener {
                val position = adapterPosition
                try {
                    val jsonObject=stepList[position]
                    pickup_floor_spinner.tag=jsonObject
                    onItemClickRecycler.onClickRecycler(pickup_floor_spinner,position)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            pickup_lift_spinner.setOnClickListener {
                val position = adapterPosition
                try {
                    val jsonObject=stepList[position]
                    pickup_lift_spinner.tag=jsonObject
                    onItemClickRecycler.onClickRecycler(pickup_lift_spinner,position)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            tv_pickup.setOnClickListener {
                val position = adapterPosition
                try {
                    val jsonObject=stepList[position]
                    tv_pickup.tag=jsonObject
                    onItemClickRecycler.onClickRecycler(tv_pickup,position)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }

    fun removeItem(position:Int){
        stepList.removeAt(position)
        notifyDataSetChanged()
    }

}
