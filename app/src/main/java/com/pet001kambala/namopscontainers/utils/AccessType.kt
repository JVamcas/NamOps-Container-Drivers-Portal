package com.pet001kambala.remotefiletransfer.utils

enum class AccessType(var value: String) {

    SNAP_CONTAINERS("Allow this user to snap containers?"),
    SNAP_DRIVER("Allow this user to snap drivers?"),
    ADMIN("Make this user an admin?");

    override fun toString(): String {
        return value
    }
}