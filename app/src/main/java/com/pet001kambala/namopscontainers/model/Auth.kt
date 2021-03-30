package com.pet001kambala.namopscontainers.model

import androidx.databinding.Bindable
import com.pet001kambala.namopscontainers.BR

class Auth(

): AbstractModel(){
    @Bindable
    var surname: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.surname)
            }
        }

    @Bindable
    var passcode: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.passcode)
            }
        }
}