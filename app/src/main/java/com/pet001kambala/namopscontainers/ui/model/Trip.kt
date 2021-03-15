package com.pet001kambala.namopscontainers.ui.model

import androidx.databinding.Bindable
import java.time.LocalDateTime

data class Trip(
    val tripId: Long,
    var driverId: Long? = null
) : AbstractModel() {

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
    var trailer1Reg: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.trailer1Reg)
            }
        }

    @Bindable
    var trailer2Reg: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.trailer2Reg)
            }
        }
    /** truck and trailer info end **/

    var dateToBePickedUp: LocalDateTime? = null /**date container is to be picked up*/

    /** trip info start **/
    @Bindable
    var startOdo: Long? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.startOdo)
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
    /** trip info end **/

    /** container weighing info start **/
    var useBison: Boolean = false /*whether or not merchandise will be weigh by bison*/
    var useWeighBridge: Boolean = false /*whether or not merch will be weigh at the weighbridge*/
    var dateWeightBridgeEmpty: LocalDateTime? = null
    var dateWeightBridgeFull: LocalDateTime? = null
    var loadWeight: Long? = null

    @Bindable
    var emptyTruckWeight: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.emptyTruckWeight)
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

    var container1JobCardId: String? = null
    var container2JobCardId: String? = null
    var container3JobCardId: String? = null
    var unknownContainerJobCardId: String? = null


    @Bindable
    var memNotes: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.memNotes)
            }
        }
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
    var dropOffLocationName: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.dropOffLocationName)
            }
        }
    /** drop off location info end  **/
}