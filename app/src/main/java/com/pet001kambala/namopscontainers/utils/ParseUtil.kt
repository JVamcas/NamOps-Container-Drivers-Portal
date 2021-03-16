package com.pet001kambala.namopscontainers.utils

import android.accounts.Account
import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pet001kambala.namopscontainers.model.AbstractModel
import java.io.File
import java.util.regex.Pattern

class ParseUtil {

    companion object {
        fun path(vararg param: String): String {
            val path = StringBuilder()

            for (p in param)
                path.append(p).append("/")
            return path.toString()
        }

        inline fun <reified O : AbstractModel?> O?.copyOf(): O? {
            if (this == null)
                return null
            val json = this.toJson()
            return json.convert()
        }

        fun <T> T.toMap(): Map<String, Any> {
            return convert()
        }


        inline fun <I, reified O> I.convert(): O {
            val json = this.toJson()
            return Gson().fromJson(json, object : TypeToken<O>() {}.type)
        }

        inline fun <reified O> String.convert(): O {
            return Gson().fromJson(this, object : TypeToken<O>() {}.type)
        }

        fun <K> K.toJson(): String {
            return Gson().toJson(this)
        }

        fun findFilePath(mContext: Context, filePath: String): String? {
            val file = File(mContext.getExternalFilesDir(null), filePath)
            return if (file.exists()) "file:" + file.absolutePath else null
        }

        fun isValidEmail(email: String?): Boolean {
            if (email.isNullOrEmpty()) return false
            val email1 = email.replace("\\s+".toRegex(), "")
            val EMAIL_STRING = ("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
            return !TextUtils.isEmpty(email1) && Pattern.compile(EMAIL_STRING)
                .matcher(email1).matches()
        }

        fun isValidMobile(phone: String?): Boolean {
            if (phone.isNullOrEmpty()) return false
            val phone1 = phone.replace("\\s+".toRegex(), "")
            return phone1.isNotEmpty() && Pattern.matches(
                "^0?8[1,5]\\d{7}",
                phone1
            )
        }

        fun String?.stripCountryCode(): String? {
            return if (this == null)
                null
            else {
                val match = Regex("^(\\+264)?(\\d+)?(8[1,5]\\d+)$").find(this)
                val (_, _,cell) = match!!.destructured
                "0$cell"
            }
        }
        /***
         * Compute relative path for the view's icon
         * @param rtDir the base dir for the icon
         * @param viewId id of the view
         * @return the relative path
         */
        fun iconPath(rtDir: String?, viewId: String): String {
            return StringBuilder(rtDir ?: "").append("/_").append(viewId).append("_.jpg")
                .toString()
        }

        @JvmStatic
        fun String?.toPhone(): String {

            return "+264${this?.trimStart { it == '0' }}"
        }

        fun Account.isIncompleteAccount() =
            name.isNullOrEmpty()

        fun isValidAuthCode(code: String?): Boolean {
            return code?.length ?: 0 == 6
        }
    }
}