package com.vanmove.passesger.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class AddPickupDropData:Parcelable{
    var title: String ? = null
    var address_type: String? = null
    var address: String? = null
    var address_order: String? = null
    var lift: String? = null
    var floor_no: String? = null
    var floor_charge: String? = null
    var distance_mile: String? = null
    var latitude: String? = null
    var longitude: String? = null
    var additional_charge: String? = null
     constructor() {}
       constructor(
         title: String?,
         address_type: String?,
         address: String?,
         address_order: String?,
         lift: String?,
         floor_no: String?,
         floor_charge: String?,
         distance_mile: String?,
         latitude: String?,
         longitude: String?,
         additional_charge: String?
     ) {
         this.title = title
         this.address_type = address_type
         this.address = address
         this.address_order = address_order
         this.lift = lift
         this.floor_no = floor_no
         this.floor_charge = floor_charge
         this.distance_mile = distance_mile
         this.latitude = latitude
         this.longitude = longitude
         this.additional_charge = additional_charge

     }

   constructor(parcel: Parcel) {
    this.title = parcel.readString()
    this.address_type = parcel.readString()
    this.address = parcel.readString()
    this.address_order = parcel.readString()
    this.lift = parcel.readString()
    this.floor_no = parcel.readString()
    this.floor_charge = parcel.readString()
    this.distance_mile = parcel.readString()
    this.latitude = parcel.readString()
    this.longitude = parcel.readString()
    this.additional_charge = parcel.readString()
   }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(address_type)
        parcel.writeString(address)

        parcel.writeString(address_order)
        parcel.writeString(lift)
        parcel.writeString(floor_no)


        parcel.writeString(floor_charge)
        parcel.writeString(distance_mile)
        parcel.writeString(latitude)

        parcel.writeString(longitude)
        parcel.writeString(additional_charge)



    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddPickupDropData> {
        override fun createFromParcel(parcel: Parcel): AddPickupDropData {
            return AddPickupDropData(parcel)
        }

        override fun newArray(size: Int): Array<AddPickupDropData?> {
            return arrayOfNulls(size)
        }
    }
}