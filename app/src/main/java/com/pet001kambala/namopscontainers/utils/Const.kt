package com.pet001kambala.namopscontainers.utils

internal interface Const {
    companion object {
        const val baseUrl: String = "http://160.242.10.200:8081/namops_driver_portal"
        const val DefaultJobCardNo: String = "JA000"
        const val JOB_CARD: String = "Job card"
        const val IS_PRE_ASSIGNED: String = "isPreAssigned"
        const val TOOLBAR_TITLE: String = "toolbar title"
        const val ICON_PATH: String = "icon path"
        const val SQUARE: String = "square"
        const val CAPTURE_PICTURE = 1
        const val IMAGE_ROOT_PATH = "images"
        const val TEMP_FILE = "temp"//only used when creating a new model
    }
}