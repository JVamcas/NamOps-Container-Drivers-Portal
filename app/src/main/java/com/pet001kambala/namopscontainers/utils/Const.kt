package com.pet001kambala.remotefiletransfer.utils

internal interface Const {
    companion object {
        const val SNAP_CONTAINER: String = "Snap Container"
        const val SNAP_DRIVER: String = "Snap Driver"
        const val TOOLBAR_TITLE: String = "toolbar title"
        const val DRIVER_DIR: String = "Driver Images"
        const val PHONE_NUMBER: String = "phone"
        const val Default_tfp_protocol : String = "TLS"
        const val defaultConnectionTimeout: Int = 10_000
        const val CONNECTION: String = "connection"
        const val Default_Cont_Image_Dir: String = "Container Images"
        const val ICON_PATH: String = "icon path"

        const val SQUARE: String = "square"
        const val ACCOUNT: String = "account"

        const val CAPTURE_PICTURE = 1

        const val IMAGE_ROOT_PATH = "images"

        const val TEMP_FILE = "temp"//only used when creating a new model

        const val DISCARD_CHANGES = "Discard Changes?"

    }
}