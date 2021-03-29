package com.pet001kambala.namopscontainers.model

import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pet001kambala.namopscontainers.BR

@Entity
class Truck(
    @PrimaryKey(autoGenerate = true)
    override var id: Int? = null
) : AbstractModel(){


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

    @Transient
    var odoMeter: String? = null

}