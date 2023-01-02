package com.vanmove.passesger.activities


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.*
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.vanmove.passesger.R
import com.vanmove.passesger.adapters.AddPickDropViewListAdapter
import com.vanmove.passesger.fragments.MoveFlow.RegularRideJobSheet
import com.vanmove.passesger.interfaces.DirectionFinderListener
import com.vanmove.passesger.interfaces.OnItemClickRecycler
import com.vanmove.passesger.model.AddPickupDropData
import com.vanmove.passesger.model.Route
import com.vanmove.passesger.utils.CONSTANTS
import com.vanmove.passesger.utils.DirectionFinder
import com.vanmove.passesger.utils.Utils
import com.vanmove.passesger.utils.Utils.getPreferences
import kotlinx.android.synthetic.main.activity_additional_pickup_drop_off.*
import kotlinx.android.synthetic.main.activity_choose_pick_up.*
import kotlinx.android.synthetic.main.fragment_job_sheet.*
import kotlinx.android.synthetic.main.fragment_previous_booked.*
import kotlinx.android.synthetic.main.fragment_previous_booked.view.*
import kotlinx.android.synthetic.main.fragment_signin.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class AdditionalDropOffPickUp : AppCompatActivity(R.layout.activity_additional_pickup_drop_off), View.OnClickListener, OnItemClickRecycler ,
    DirectionFinderListener {
    var additionalPickupList: ArrayList<AddPickupDropData> ? = null
    var select_image_position = 0
    var addPicklistAdapter: AddPickDropViewListAdapter? = null
    private var polylinePaths: MutableList<Polyline>? = ArrayList()
    var total_estimated_payment: Double = 0.0
    var estimated_payment: String? = ""
    var estimated_duartion: String? = ""
    var total_addiionali_estimated_payment: Double = 0.0
    var lastPosition=0

    var context: Context? = null
    var add_pickup_lat: String? = null
    var add_pickup_lon: String? = null

    var isUpdating=false
    var isDeleteUpdating=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this@AdditionalDropOffPickUp
        additionalPickupList = ArrayList()
        estimated_payment=intent.getStringExtra("estimated_payment").toString()
        estimated_duartion = getPreferences(CONSTANTS.estimated_duartion, context!!)
        total_estimated_payment=(estimated_payment!!.replace(CONSTANTS.CURRENCY,""))!!.toDouble()
        EstimatedPrice.setText("$estimated_payment")
        additionalPickupList=  intent?.getParcelableArrayListExtra<AddPickupDropData>(CONSTANTS.additional_pick_data) ?: throw IllegalStateException("array list is null")
        linkViews()
        setPickupAdapterData()
    }


    private fun linkViews() {
        iv_back.setOnClickListener(this)
        add_additional_pickup.setOnClickListener(this)
        okButton.setOnClickListener(this)

        tvTitle.text="Additional Address"

        if (additionalPickupList.isNullOrEmpty()) {
            add_pickup_lat = getPreferences(
                CONSTANTS.PREFERENCE_PICK_UP_LATITUDE_EXTRA,
                context!!
            )
            add_pickup_lon = getPreferences(
                CONSTANTS.PREFERENCE_PICK_UP_LONGITUDE_EXTRA,
                context!!
            )
            Utils.savePreferences(CONSTANTS.PREFERENCE_ADD_PICK_UP_LATITUDE_EXTRA, add_pickup_lat + "", context!!)
            Utils.savePreferences(CONSTANTS.PREFERENCE_ADD_PICK_UP_LONGITUDE_EXTRA, add_pickup_lon + "", context!!)
        }
        else{
            val length=additionalPickupList!!.size-1
            add_pickup_lat = additionalPickupList!![length].latitude
            add_pickup_lon =  additionalPickupList!![length].longitude


        }


    }

    public override fun onResume() {
        super.onResume()
        try {
            Utils.closeKeyboard(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    public override fun onPause() {
        super.onPause()
    }

    public override fun onStop() {
        super.onStop()
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.add_additional_pickup -> {

                try {
                   if (additionalPickupList!!.isEmpty()){
                       var jsonObject=AddPickupDropData()
                       jsonObject!!.title= "Additional Address"
                       jsonObject!!.address_type= ""
                       jsonObject!!.address= ""
                       jsonObject!!.address_order=  ""


                       jsonObject!!.lift= ""
                       jsonObject!!.floor_no=  ""
                       jsonObject!!.floor_charge= "0.0"
                       jsonObject!!.distance_mile=  "0.0"
                       jsonObject!!.latitude= "0.0"
                       jsonObject!!.longitude=  "0.0"
                       jsonObject!!.additional_charge=  "0.0"


                       additionalPickupList!!.add(jsonObject)
                       setPickupAdapterData()
                       add_pickup_lat = getPreferences(
                           CONSTANTS.PREFERENCE_PICK_UP_LATITUDE_EXTRA,
                           context!!
                       )
                       add_pickup_lon = getPreferences(
                           CONSTANTS.PREFERENCE_PICK_UP_LONGITUDE_EXTRA,
                           context!!
                       )
                       Utils.savePreferences(CONSTANTS.PREFERENCE_ADD_PICK_UP_LATITUDE_EXTRA, add_pickup_lat + "", context!!)
                       Utils.savePreferences(CONSTANTS.PREFERENCE_ADD_PICK_UP_LONGITUDE_EXTRA, add_pickup_lon + "", context!!)

                   }
                    else{
                        var msg=""
                        var isValid=false
                       for(i in 0 until additionalPickupList!!.size){
                           if(additionalPickupList!![i].address_type!!.isEmpty()){
                               msg="Please select address type";
                               showAllertDialog("Alert",msg)
                               return
                           }

                           if (additionalPickupList!![i].address!!.isEmpty()) {
                               msg="Please select address";

                               showAllertDialog("Alert",msg)

                               return
                           }

                       }
                       add_pickup_lat =additionalPickupList!![additionalPickupList!!.size-1].latitude
                       add_pickup_lon = additionalPickupList!![additionalPickupList!!.size-1].longitude

                       var jsonObject=AddPickupDropData()
                       jsonObject!!.title= "Additional Address"
                       jsonObject!!.address_type= ""
                       jsonObject!!.address= ""
                       jsonObject!!.address_order=  ""


                       jsonObject!!.lift= ""
                       jsonObject!!.floor_no=  ""
                       jsonObject!!.floor_charge= "0.0"
                       jsonObject!!.distance_mile=  "0.0"
                       jsonObject!!.latitude= "0.0"
                       jsonObject!!.longitude=  "0.0"
                       jsonObject!!.additional_charge=  "0.0"


                       additionalPickupList!!.add(jsonObject)
                       setPickupAdapterData()
                       Utils.savePreferences(CONSTANTS.PREFERENCE_ADD_PICK_UP_LATITUDE_EXTRA, add_pickup_lat + "", context!!)
                       Utils.savePreferences(CONSTANTS.PREFERENCE_ADD_PICK_UP_LONGITUDE_EXTRA, add_pickup_lon + "", context!!)

                   }
                }
                catch (e:Exception){
                    e.printStackTrace()
                }
            }
            R.id.iv_back -> submitData()
            R.id.okButton -> {
                submitData()
            }

        }
    }


    public override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        // ShowProgressDialog.showDialog2(context)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 401) {
                try {
                    val placeName=data!!.getStringExtra("pickup").toString()
                    val destination_lat = data!!.getStringExtra("latitude")!!.toDouble()
                    val destination_lon = data!!.getStringExtra("longitude")!!.toDouble()

                    if (isUpdating && additionalPickupList!!.size>1){
                        updateNextIndexLocationData(
                            select_image_position,
                            placeName,
                            destination_lat,
                            destination_lon)
                    }
                    else{
                        updatateLocationData(
                            select_image_position,
                            placeName,
                            destination_lat,
                            destination_lon,
                            false
                            ,false)
                    }



                }
                catch (e:Exception){
                    e.printStackTrace()
                }


            }
        }
    }
    var isNextIndexDataUpdate=false
    fun updateDeleteLocationData(position: Int,placeName:String,destination_lat:Double,destination_lon:Double){


        isDeleteUpdating=true
        System.out.println("placeName::"+placeName)
        System.out.println("destination_lat::"+destination_lat)
        System.out.println("destination_lon::"+destination_lon)
        isNextIndexDataUpdate=false
        isUpdating=true

        System.out.println("add_pickup_lat::"+add_pickup_lat)
        System.out.println("add_pickup_lat::"+add_pickup_lon)

        System.out.println("pick placeName::"+additionalPickupList!![position].address!!)
        System.out.println("destination_lat::"+additionalPickupList!![position].latitude!!.toDouble())
        System.out.println("destination_lon::"+additionalPickupList!![position].longitude!!.toDouble())

        val directionFinder = DirectionFinder(this,
            LatLng(destination_lat, destination_lon),
            LatLng(additionalPickupList!![position].latitude!!.toDouble(),
                additionalPickupList!![position].longitude!!.toDouble()), "path")
        directionFinder.showDirection()

    }
    fun updatateLocationData(position: Int,placeName:String,destination_lat:Double,destination_lon:Double,isUpdate:Boolean,isNextIndesUpdate:Boolean){

    isDeleteUpdating=false

    System.out.println("placeName::"+placeName)
    System.out.println("destination_lat::"+destination_lat)
    System.out.println("destination_lon::"+destination_lon)
    if (!isUpdate &&  !isNextIndesUpdate ) {
        additionalPickupList!![position].address=placeName
        isUpdating=false
        isNextIndexDataUpdate=false

        if (position==0){
            add_pickup_lat = getPreferences(CONSTANTS.PREFERENCE_PICK_UP_LATITUDE_EXTRA, context!!)!!
            add_pickup_lon = getPreferences(CONSTANTS.PREFERENCE_PICK_UP_LONGITUDE_EXTRA, context!!)!!

        }
        else{
            add_pickup_lat = additionalPickupList!![position-1].latitude
            add_pickup_lon = additionalPickupList!![position-1].longitude
        }



        val directionFinder = DirectionFinder(this, LatLng(add_pickup_lat!!.toDouble(), add_pickup_lon!!.toDouble()),
            LatLng(destination_lat, destination_lon), "path")
        directionFinder.showDirection()
        add_pickup_lat = destination_lat.toString()
        add_pickup_lon = destination_lon.toString()
        Utils.savePreferences(
            CONSTANTS.PREFERENCE_ADD_PICK_UP_LATITUDE_EXTRA,
            add_pickup_lat + "",
            context!!
        )
        Utils.savePreferences(
            CONSTANTS.PREFERENCE_ADD_PICK_UP_LONGITUDE_EXTRA,
            add_pickup_lat + "",
            context!!
        )
        return
    }


/*
    if (isUpdate &&  isNextIndesUpdate ) {
        if (additionalPickupList!!.size==1){
            isUpdating=true
            isNextIndexDataUpdate=false
            additionalPickupList!![position].address=placeName

            add_pickup_lat = getPreferences(CONSTANTS.PREFERENCE_PICK_UP_LATITUDE_EXTRA, context!!)!!
            add_pickup_lon = getPreferences(CONSTANTS.PREFERENCE_PICK_UP_LONGITUDE_EXTRA, context!!)!!
            val directionFinder = DirectionFinder(this, LatLng(add_pickup_lat!!.toDouble(), add_pickup_lon!!.toDouble()),
                LatLng(destination_lat, destination_lon), "path")
            directionFinder.showDirection()
            add_pickup_lat = destination_lat.toString()
            add_pickup_lon = destination_lon.toString()

            Utils.savePreferences(
                CONSTANTS.PREFERENCE_ADD_PICK_UP_LATITUDE_EXTRA,
                add_pickup_lat + "",
                context!!
            )
            Utils.savePreferences(
                CONSTANTS.PREFERENCE_ADD_PICK_UP_LONGITUDE_EXTRA,
                add_pickup_lat + "",
                context!!
            )
            return
        }//single items updation
        if (position==0) {//update in 0 index value
            isUpdating=true
            isNextIndexDataUpdate=true
            additionalPickupList!![position].address=placeName
            add_pickup_lat = getPreferences(CONSTANTS.PREFERENCE_PICK_UP_LATITUDE_EXTRA, context!!)!!
            add_pickup_lon = getPreferences(CONSTANTS.PREFERENCE_PICK_UP_LONGITUDE_EXTRA, context!!)!!
            val directionFinder = DirectionFinder(this, LatLng(add_pickup_lat!!.toDouble(), add_pickup_lon!!.toDouble()),
                LatLng(destination_lat, destination_lon), "path")
            directionFinder.showDirection()
            return
        }
        if (position==additionalPickupList!!.size-1) {//update in end index value
            isNextIndexDataUpdate=false
            additionalPickupList!![position].address=placeName
            add_pickup_lat = additionalPickupList!![position-1].latitude!!.toString()
            add_pickup_lon = additionalPickupList!![position-1].longitude!!.toString()
            val directionFinder = DirectionFinder(this, LatLng(add_pickup_lat!!.toDouble(), add_pickup_lon!!.toDouble()),
                LatLng(destination_lat, destination_lon), "path")
            directionFinder.showDirection()
            add_pickup_lat = destination_lat.toString()
            add_pickup_lon = destination_lon.toString()
            Utils.savePreferences(
                CONSTANTS.PREFERENCE_ADD_PICK_UP_LATITUDE_EXTRA,
                add_pickup_lat + "",
                context!!
            )
            Utils.savePreferences(
                CONSTANTS.PREFERENCE_ADD_PICK_UP_LONGITUDE_EXTRA,
                add_pickup_lat + "",
                context!!)
            return
        }

        isUpdating=true
        isNextIndexDataUpdate=true
        additionalPickupList!![position].address=placeName
        val directionFinder = DirectionFinder(this, LatLng( additionalPickupList!![position-1].latitude!!.toDouble(), additionalPickupList!![position-1].longitude!!.toDouble()),
            LatLng(destination_lat, destination_lon), "path")
        directionFinder.showDirection()
        return
    }
*/

}


    fun updateNextIndexLocationData(position: Int,placeName:String,destination_lat:Double,destination_lon:Double){


        if(position==0){
            additionalPickupList!![position].address=placeName
            isDeleteUpdating=false
            isUpdating=true
            isNextIndexDataUpdate=true

            add_pickup_lat = getPreferences(
                CONSTANTS.PREFERENCE_PICK_UP_LATITUDE_EXTRA,
                context!!
            )
            add_pickup_lon = getPreferences(
                CONSTANTS.PREFERENCE_PICK_UP_LONGITUDE_EXTRA,
                context!!
            )




            System.out.println("add_pickup_lat::"+additionalPickupList!![position].latitude!!.toDouble())
            System.out.println("add_pickup_lon::"+additionalPickupList!![position].longitude!!.toDouble())
            System.out.println("pick placeName::"+placeName)

            System.out.println("destination_lat::"+destination_lat)
            System.out.println("destination_lon::"+destination_lon)
            System.out.println("destination placeName::"+additionalPickupList!![position+1].address!!)

            val directionFinder = DirectionFinder(
                this,
                LatLng(
                    add_pickup_lat!!.toDouble(),
                    add_pickup_lon!!.toDouble()
                ),
                LatLng(destination_lat, destination_lon),
                "path"
            )
            directionFinder.showDirection()


            return
        }

        if(position==additionalPickupList!!.size-1){
            additionalPickupList!![position].address=placeName
            isDeleteUpdating=false
            System.out.println("add_pickup_lat::"+additionalPickupList!![position-1].latitude!!.toDouble())
            System.out.println("add_pickup_lon::"+additionalPickupList!![position-1].longitude!!.toDouble())
            System.out.println("pick placeName::"+additionalPickupList!![position-1].address!!)

            System.out.println("destination_lat::"+destination_lat)
            System.out.println("destination_lon::"+destination_lon)
            System.out.println("destination placeName::"+placeName)


            isUpdating=false
            isNextIndexDataUpdate=false

            val directionFinder = DirectionFinder(
                this,
                LatLng(
                    additionalPickupList!![position-1].latitude!!.toDouble(),
                    additionalPickupList!![position-1].longitude!!.toDouble()
                ),
                LatLng(destination_lat, destination_lon),
                "path"
            )
            directionFinder.showDirection()

            return
        }

        additionalPickupList!![position].address=placeName
        isDeleteUpdating=false
    /*    System.out.println("add_pickup_lat::"+additionalPickupList!![position-1].latitude!!.toDouble())
        System.out.println("add_pickup_lon::"+additionalPickupList!![position-1].longitude!!.toDouble())
        System.out.println("pick placeName::"+additionalPickupList!![position-1].address!!)

        System.out.println("destination_lat::"+destination_lat)
        System.out.println("destination_lon::"+destination_lon)
        System.out.println("destination placeName::"+placeName)*
        add 157 ph 10 sec 64
        dis 1.25

        503
        dis 4.14





        /
     */

        isUpdating=true
        isNextIndexDataUpdate=true
        add_pickup_lat = destination_lat.toString()
        add_pickup_lon = destination_lon.toString()


        val directionFinder = DirectionFinder(
            this,
            LatLng(
                additionalPickupList!![position-1].latitude!!.toDouble(),
                additionalPickupList!![position-1].longitude!!.toDouble()
            ),
            LatLng(destination_lat, destination_lon),
            "path"
        )
        directionFinder.showDirection()

    }



    fun updatateLocationData(position: Int){

        isDeleteUpdating=false
        isUpdating=true
        isNextIndexDataUpdate=false
        val directionFinder = DirectionFinder(this, LatLng(additionalPickupList!![position].latitude!!.toDouble(), additionalPickupList!![position].longitude!!.toDouble()),
            LatLng(additionalPickupList!![position+1].latitude!!.toDouble(),  additionalPickupList!![position+1].longitude!!.toDouble()), "path")
        directionFinder.showDirection()

    }

    private fun setPickupAdapterData(){

        addPicklistAdapter = AddPickDropViewListAdapter(additionalPickupList!!, context!!,this)
        val llm = LinearLayoutManager(context)
        additional_pickup_rv.setAdapter(addPicklistAdapter!!)
        additional_pickup_rv.setLayoutManager(llm)
        addAdditionalEstimatedPrice()
    }
    override fun onClickRecycler(view: View?, position: Int) {
        select_image_position = position
        try {
            if (view!!.id==R.id.tv_pickup){

                if(additionalPickupList!![position].address_type!!.isEmpty()){
                   // showAllertDialog("Alert","Please select address type $position.")
                    showAllertDialog("Alert","Please select address type.")

                    return
                }
                else {
                   if (additionalPickupList!![position].address!!.isEmpty()){
                       isUpdating=false
                   }
                    else{
                        isUpdating=true
                   }
                    startActivityForResult(
                        Intent(context!!, AddPlaces::class.java).putExtra(
                            CONSTANTS.callfrom,
                            CONSTANTS.additional_pickup_loc
                        ), 401
                    )
                }
            }
            if (view!!.id==R.id.pickup_address_type){
                AddressType(view,position)
            }
            if (view!!.id==R.id.pickup_lift_spinner){
                AddPickupLift(view,position)
            }
            if (view!!.id==R.id.pickup_floor_spinner){
                AddPickupFloor(view,position)
            }
            else if (view!!.id==R.id.removePickupDropoffIcon){
                showConfirmDeleteAdditionalPickupDropOffDialog(position)
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }


    }
    override fun onDirectionFinderStart(operationName: String?) {
        if (operationName == "path") {
            if (polylinePaths != null) {
                for (polyline in polylinePaths!!) {
                    polyline.remove()
                }
            }
        }
    }

    private fun AddressType(v: View,position: Int) {
        val popup =
            PopupMenu(context!!, v)
        for (category in CONSTANTS.address_type_category) {
            popup.menu.add(category)
        }
        popup.setOnMenuItemClickListener { item ->
            val item_ = item.title.toString()
            additionalPickupList!![position].address_type=item_
            addPicklistAdapter!!.notifyDataSetChanged()
           // additionalFloorEstimationPrice(position)
            true
        }
        popup.show()
    }


    private fun AddPickupLift(v: View,position: Int) {
        val popup =
            PopupMenu(context!!, v)
        for (category in CONSTANTS.categorylift) {
            popup.menu.add(category)
        }
        popup.setOnMenuItemClickListener { item ->
            val item_ = item.title.toString()
            var floor_no=""
            additionalPickupList!![position].lift=item_
          //  addPicklistAdapter!!.notifyDataSetChangedAdapter(position)
            addPicklistAdapter!!.notifyDataSetChanged()
            additionalFloorEstimationPrice(position)
            true
        }
        popup.show()
    }
    private fun showAllertDialog(title:String,msg:String) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton("Ok") { dialogInterface, which ->



        }
        //performing cancel action
      /*  builder.setNeutralButton("No") { dialogInterface, which ->

        }*/
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(true)
        alertDialog.show()
    }
    private fun showConfirmDeleteAdditionalPickupDropOffDialog(position: Int) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Alert")
        builder.setMessage("Are you sure you want to delete?")
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            var placeName=""
            var add_pickup_lat=0.0
            var add_pickup_lon=0.0
           if (additionalPickupList!!.size==1) {
               addPicklistAdapter!!.removeItem(position)
               addAdditionalEstimatedPrice()
               return@setPositiveButton
           }
            if (position == 0) {
                add_pickup_lat = getPreferences(
                    CONSTANTS.PREFERENCE_PICK_UP_LATITUDE_EXTRA,
                    context!!
                )!!.toDouble()
                add_pickup_lon = getPreferences(
                    CONSTANTS.PREFERENCE_PICK_UP_LONGITUDE_EXTRA,
                    context!!
                )!!.toDouble()

                placeName = additionalPickupList!![position+1].address!!
                if (placeName!!.isNotEmpty()) {
                    addPicklistAdapter!!.removeItem(position)
                    updateDeleteLocationData(
                        position,
                        placeName!!,
                        add_pickup_lat,
                        add_pickup_lon
                    )
                }
                else{
                    addPicklistAdapter!!.removeItem(position)
                }
                return@setPositiveButton
            }
            if (position == additionalPickupList!!.size - 1) {
                addPicklistAdapter!!.removeItem(position)
                addAdditionalEstimatedPrice()
                return@setPositiveButton
            }
            placeName = additionalPickupList!![position+1].address!!
            add_pickup_lat = additionalPickupList!![position - 1].latitude!!.toDouble()
            add_pickup_lon = additionalPickupList!![position - 1].longitude!!.toDouble()

            addPicklistAdapter!!.removeItem(position)


            if (placeName!!.isNotEmpty()) {
                updateDeleteLocationData(
                    position,
                    placeName!!,
                    add_pickup_lat,
                    add_pickup_lon
                )
            }

         }
        //performing cancel action
        builder.setNeutralButton("No") { dialogInterface, which ->

        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun AddPickupFloor(v: View,position: Int) {
        val popup =
            PopupMenu(context!!, v)
        for (category in CONSTANTS.category) {
            popup.menu.add(category)
        }
        popup.setOnMenuItemClickListener { item ->
            val item_ = item.title.toString()
            additionalPickupList!![position].floor_no=item_
           // addPicklistAdapter!!.notifyDataSetChangedAdapter(position)
            addPicklistAdapter!!.notifyDataSetChanged()
            additionalFloorEstimationPrice(position)
            true
        }
        popup.show()
    }
    private fun additionalFloorEstimationPrice(position: Int){

        val  helpers = getPreferences(CONSTANTS.helpers, context!!)
        val helpers_count_ = helpers!!.toDouble()
        System.out.println("helpers_count_:"+helpers_count_)
        var total_drop_floore_estimated_payment=0.0
        val address= additionalPickupList!![position].address
        val dropoff_floor= additionalPickupList!![position].floor_no
        val lift_value=additionalPickupList!![position].lift
        System.out.println("lift_value:"+lift_value)
        System.out.println("dropoff_floor:"+dropoff_floor)
        if (lift_value.equals("No")&&address!!.isNotEmpty()){
            if (dropoff_floor == CONSTANTS.First_Floor_) {

                val  floor_charge = getPreferences(
                    CONSTANTS.first_floor_charge,
                    context!!
                )
                System.out.println("floor_charge:"+floor_charge)
                total_drop_floore_estimated_payment=helpers_count_*floor_charge!!.toDouble()
            }
            if (dropoff_floor == CONSTANTS.Second_Floor_) {

                val  floor_charge = getPreferences(
                    CONSTANTS.second_floor_charge,
                    context!!
                )
                System.out.println("floor_charge:"+floor_charge)
                total_drop_floore_estimated_payment=helpers_count_*floor_charge!!.toDouble()
            }
            if (dropoff_floor == CONSTANTS.Third_Floor_) {

                val  floor_charge = getPreferences(
                    CONSTANTS.third_floor_charge,
                    context!!
                )
                System.out.println("floor_charge:"+floor_charge)
                total_drop_floore_estimated_payment=helpers_count_*floor_charge!!.toDouble()
            }
            if (dropoff_floor == CONSTANTS.Fourth_Floor_) {

                val  floor_charge = getPreferences(
                    CONSTANTS.fourth_above_charge,
                    context!!
                )
                System.out.println("floor_charge:"+floor_charge)
                total_drop_floore_estimated_payment=helpers_count_*floor_charge!!.toDouble()
            }
        }
        else{

            total_drop_floore_estimated_payment=0.0
        }
        System.out.println("dropoff_floor:"+total_drop_floore_estimated_payment)
        additionalPickupList!![position].floor_charge=total_drop_floore_estimated_payment.toString()
        //addPicklistAdapter!!.notifyDataSetChangedAdapter(position)
        addPicklistAdapter!!.notifyDataSetChanged()
        addAdditionalEstimatedPrice()

    }

    override fun onDirectionFinderSuccess(
        routes: List<Route?>?,
        operationName: String?
    ) {
        if (operationName == "path") {
            //polylinePaths = ArrayList()
            for (route in routes!!) {
                val distance_meter = route!!.distance!!.value
                val distance_in_miles = Utils.KmToMile("" + distance_meter)
                val endLocation_lat = route!!.endLocation!!.latitude
                val endLocation_long = route!!.endLocation!!.longitude
                val starLocation_lat = route!!.startLocation!!.latitude
                val starLocation_long = route!!.startLocation!!.longitude
                System.out.println("add distance_in_miles:: "+distance_in_miles)
                val additional_charge=Utils.calculateFareUpto(CONSTANTS.vehicles!!,distance_in_miles,context!!)

                if (!isUpdating && !isNextIndexDataUpdate&& !isDeleteUpdating){
                    additionalPickupList!![select_image_position].distance_mile= distance_in_miles
                    additionalPickupList!![select_image_position].latitude=endLocation_lat.toString()
                    additionalPickupList!![select_image_position].longitude=endLocation_long.toString()
                    additionalPickupList!![select_image_position].additional_charge=additional_charge
                    addPicklistAdapter!!.notifyDataSetChanged()
                    add_pickup_lat=endLocation_lat.toString()
                    add_pickup_lon=endLocation_long.toString()

                    Utils.savePreferences(
                        CONSTANTS.PREFERENCE_ADD_PICK_UP_LATITUDE_EXTRA,
                        add_pickup_lat + "",
                        context!!
                    )
                    Utils.savePreferences(
                        CONSTANTS.PREFERENCE_ADD_PICK_UP_LONGITUDE_EXTRA,
                        add_pickup_lat + "",
                        context!!
                    )
                }//updating
                else if (isUpdating && !isNextIndexDataUpdate && isDeleteUpdating){//deleting and updating
                    additionalPickupList!![select_image_position].distance_mile= distance_in_miles
                    additionalPickupList!![select_image_position].latitude=endLocation_lat.toString()
                    additionalPickupList!![select_image_position].longitude=endLocation_long.toString()
                    additionalPickupList!![select_image_position].additional_charge=additional_charge
                    addPicklistAdapter!!.notifyDataSetChanged()

                }
                else if (isUpdating && isNextIndexDataUpdate && !isDeleteUpdating) {
                    additionalPickupList!![select_image_position].distance_mile= distance_in_miles
                    additionalPickupList!![select_image_position].latitude=endLocation_lat.toString()
                    additionalPickupList!![select_image_position].longitude=endLocation_long.toString()
                    additionalPickupList!![select_image_position].additional_charge=additional_charge
                    addPicklistAdapter!!.notifyDataSetChanged()
                    updatateLocationData(select_image_position)

                }//current index value updating
                else if (isUpdating && !isNextIndexDataUpdate && !isDeleteUpdating){//next index value updating

                    val endLocation_lat = route!!.endLocation!!.latitude
                    val endLocation_long = route!!.endLocation!!.longitude
                    val starLocation_lat = route!!.startLocation!!.latitude
                    val starLocation_long = route!!.startLocation!!.longitude

                    System.out.println("endLocation_lat::"+endLocation_lat)
                    System.out.println("endLocation_long::"+endLocation_long)
                    System.out.println("enndLocation_address::"+route!!.endAddress)


                    System.out.println("starLocation_lat::"+starLocation_lat)
                    System.out.println("starLocation_long::"+starLocation_long)
                    System.out.println("starLocation_address::"+route!!.startAddress)


                    System.out.println("add distance_in_miles:: "+distance_in_miles)

                    System.out.println("add_pickup_lat::"+additionalPickupList!![select_image_position].latitude!!.toDouble())
                    System.out.println("add_pickup_lon::"+additionalPickupList!![select_image_position].longitude!!.toDouble())
                    System.out.println("pick placeName::"+additionalPickupList!![select_image_position].address!!)

                    System.out.println("destination_lat::"+additionalPickupList!![select_image_position+1].latitude!!.toDouble())
                    System.out.println("destination_lon::"+additionalPickupList!![select_image_position+1].longitude!!.toDouble())
                    System.out.println("destination placeName::"+additionalPickupList!![select_image_position+1].address!!)

                    additionalPickupList!![select_image_position+1].distance_mile= distance_in_miles
                    additionalPickupList!![select_image_position+1].latitude=endLocation_lat.toString()
                    additionalPickupList!![select_image_position+1].longitude=endLocation_long.toString()
                    additionalPickupList!![select_image_position+1].additional_charge=additional_charge
                    addPicklistAdapter!!.notifyDataSetChanged()

                }



               // addPicklistAdapter!!.notifyDataSetChangedAdapter(select_image_position)
               additionalFloorEstimationPrice(select_image_position)
               // addAdditionalEstimatedPrice()
            }

        }
    }

    private fun addAdditionalEstimatedPrice(){
        try {
            var totalPayment=0.0
            for (i in 0 until additionalPickupList!!.size){
                val jsonObject=additionalPickupList!![i]
                val lift=jsonObject.lift
                val floor_charge=jsonObject.floor_charge!!.toDouble()
                val additional_charge=jsonObject.additional_charge!!.toDouble()
                System.out.println("lift $i:"+lift)
                System.out.println("latitude $i:"+jsonObject.latitude!!)
                System.out.println("longitude $i:"+jsonObject.longitude!!)
                System.out.println("jsonObject.latitude!! $i:"+jsonObject.distance_mile!!)
                System.out.println("floor_charge $i:"+floor_charge)
                System.out.println("additional_charge $i:"+additional_charge)
                totalPayment+=additional_charge+ floor_charge

            }
            System.out.println("total_estimated_payment:"+total_estimated_payment)

/*
            if (intent.getStringExtra(CONSTANTS.callfrom).toString().equals(CONSTANTS.pickup)) {
                RegularRideJobSheet.total_addiionali_pickup_estimated_payment=totalPayment
            }
            else{
                RegularRideJobSheet.total_addiionali_dropoff_estimated_payment=totalPayment
            }*/
            RegularRideJobSheet.total_addiionali_dropoff_estimated_payment=totalPayment
            updateEstimatedPrice()
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun submitData(){
        var msg=""
        var isValid=false
        for(i in 0 until additionalPickupList!!.size){
            if(additionalPickupList!![i].address_type!!.isEmpty()){
                msg="Please add or delete empty field";
                showAllertDialog("Alert",msg)
                return
            }

            if (additionalPickupList!![i].address!!.isEmpty()) {
                msg="Please select address";

                showAllertDialog("Alert",msg)

                return
            }

            if (additionalPickupList!![i].lift!!.isEmpty()) {

                msg="Please select lift";

                showAllertDialog("Alert",msg)
                return
            }
            if (additionalPickupList!![i].lift!!.equals("No")&&additionalPickupList!![i].floor_no!!.isEmpty()) {
                msg="Please select floor";
                showAllertDialog("Alert",msg)
                return
            }

        }
        val intent = Intent()
        intent.putExtra(CONSTANTS.additional_pick_data,additionalPickupList)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    private fun updateEstimatedPrice(){
        val totalEstimatedPrice= RegularRideJobSheet.total_estimated_payment +
                RegularRideJobSheet.total_pickup_floor_estimated_payment +
                RegularRideJobSheet.total_drop_floore_estimated_payment +
                RegularRideJobSheet.total_ulez_stimated_payment +
                RegularRideJobSheet.total_congnigetion_stimated_payment +
                RegularRideJobSheet.total_addiionali_pickup_estimated_payment+
                RegularRideJobSheet. total_addiionali_dropoff_estimated_payment

        System.out.println("RegularRideJobSheet.total_estimated_payment:"+RegularRideJobSheet.total_estimated_payment)
        System.out.println("RegularRideJobSheet.total_pickup_floor_estimated_payment:"+RegularRideJobSheet.total_pickup_floor_estimated_payment)
        System.out.println("RegularRideJobSheet.total_drop_floore_estimated_payment:"+RegularRideJobSheet.total_drop_floore_estimated_payment)
        System.out.println("RegularRideJobSheet.total_ulez_stimated_payment:"+RegularRideJobSheet.total_ulez_stimated_payment)
        System.out.println("RegularRideJobSheet.total_congnigetion_stimated_payment:"+RegularRideJobSheet.total_congnigetion_stimated_payment)
        System.out.println("RegularRideJobSheet.total_addiionali_pickup_estimated_payment:"+RegularRideJobSheet.total_addiionali_pickup_estimated_payment)
        System.out.println("RegularRideJobSheet.total_addiionali_dropoff_estimated_payment:"+RegularRideJobSheet.total_addiionali_dropoff_estimated_payment)


        estimated_payment=CONSTANTS.CURRENCY+  String.format("%.2f", totalEstimatedPrice)
        EstimatedPrice.setText("$estimated_payment")
    }

}