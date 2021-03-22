package com.pet001kambala.namopscontainers.model

import androidx.databinding.Bindable
import com.pet001kambala.namopscontainers.BR

class Truck : AbstractModel(){
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

}