package com.pet001kambala.namopscontainers.model

import androidx.databinding.Bindable
import androidx.room.*
import com.pet001kambala.namopscontainers.BR
import com.pet001kambala.namopscontainers.utils.DriverConverter
import com.pet001kambala.namopscontainers.utils.LocalDateConverter
import com.pet001kambala.namopscontainers.utils.TripStatusConverter
import java.time.LocalDateTime

data class Trip(

    override var id: Int? = null

) : AbstractModel(id = id) {


    var driver: Driver? = null

    /** truck and trailer info start **/
    @Bindable
    var truckReg: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.truckReg)
            }
        }

    @Bindable
    var firstTrailerReg: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.firstTrailerReg)
            }
        }

    @Bindable
    var secondTrailerReg: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.secondTrailerReg)
            }
        }

    /** truck and trailer info end **/


    var designatePickUpDate: LocalDateTime? = null
    /**designated container pick up date*/

    /** trip start info start **/
    @Bindable
    var startODM: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.startODM)
            }
        }

    @Bindable
    var startLocationName: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.startLocationName)
            }
        }

    @Bindable
    var startLocationGPS: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.startLocationGPS)
            }
        }

    /** trip start info end **/

    /** container weighing info start **/
    var useBison: Boolean = false /*whether or not merchandise will be weigh by bison*/
    var useWeighBridge: Boolean = false /*whether or not merch will be weigh at the weighbridge*/

    var dateWeightBridgeEmpty: LocalDateTime? = null
    @Bindable
    var emptyTruckWeight: Long? = 0L
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.emptyTruckWeight)
            }
        }

    var dateWeightBridgeFull: LocalDateTime? = null

    @Bindable
    var fullTruckWeight: Long = 0L
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.fullTruckWeight)
            }
        }

    /** container weighing info end **/

    /** container scanning info start **/
    var scanContainer: Boolean = false /*whether or not the container must be thermally scanned*/

    var containerScanDate: LocalDateTime? = null
    /** container scanning info end **/

    /** container pick up info start **/

    var actualPickUpDate: LocalDateTime? = null
    var pickUpLocationGPS: String? = null

    @Bindable
    var pickUpLocationName: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.pickUpLocationName)
            }
        }

    @Bindable
    var pickUpODM: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.pickUpODM)
            }
        }

    @Bindable
    var container1: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.container1)
            }
        }

    @Bindable
    var container2: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.container2)
            }
        }

    @Bindable
    var container3: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.container3)
            }
        }

    @Bindable
    var unknownContainer: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.unknownContainer)
            }
        }

    @Bindable
    var weighBillNo: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.weighBillNo)
            }
        }

    var container1JobCardId: String? = null
    var container2JobCardId: String? = null
    var container3JobCardId: String? = null
    var unknownContainerJobCardId: String? = null

    /** container pick up info end **/

    /** drop off location info start **/
    @Bindable
    var dropOffODM: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.dropOffODM)
            }
        }

    @Bindable
    var dropOffLocationGPS: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.dropOffLocationGPS)
            }
        }

    @Bindable
    var dropOffLocationName: String? = "Unkown"
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.dropOffLocationName)
            }
        }

    @Bindable
    var memNotes: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.memNotes)
            }
        }

    /** drop off location info end  **/


    var tripStatus: TripStatus = TripStatus.START
}

enum class TripStatus {
    START, WEIGH_EMPTY, PICK_UP, WEIGH_FULL, BISON, DROP_OFF
}