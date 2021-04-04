package com.pet001kambala.namopscontainers.utils


import android.content.Context
import android.text.TextUtils
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.pet001kambala.namopscontainers.model.*
import java.io.File
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern


class ParseUtil {

    class LocalDateTimeSerializer : JsonSerializer<LocalDateTime?> {
        override fun serialize(
            localDateTime: LocalDateTime?,
            srcType: Type?,
            context: JsonSerializationContext
        ): JsonElement? {

            return if (localDateTime == null) null
            else {
                val year = localDateTime.year
                val month = localDateTime.month
                val day = localDateTime.dayOfMonth

                val hour = localDateTime.hour
                val minute = localDateTime.minute
                val seconds = localDateTime.second
                val json =
                    "{date:{year:$year,month:$month,day:$day},time:{hour:$hour,minute:$minute,second:$seconds}}"
                JsonParser.parseString(json).asJsonObject
            }

        }

        companion object {
            private val formatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT)
        }
    }

    class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime?> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext
        ): LocalDateTime? {
            return if (json != null && !json.isJsonNull) {
                val date = json.asJsonObject.get("date").asJsonObject
                val time = json.asJsonObject.get("time").asJsonObject

                val year = date.get("year").asInt
                val month = date.monthIndex()
                val day = date.get("day").asInt

                val hour = time.get("hour").asInt
                val minute = time.get("minute").asInt
                val seconds = time.get("second").asInt

                LocalDateTime.of(year, month, day, hour, minute, seconds)

            } else null
        }
    }

    object ParseGson {
        var gson: Gson? = null
        val builder = GsonBuilder()
            .apply {
                registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
                registerTypeAdapter(
                    LocalDateTime::class.java,
                    LocalDateTimeDeserializer()
                )
            }

        init {
            gson = if (gson == null) builder.setPrettyPrinting().create() else gson
        }

    }


    companion object {

        val gson: Gson by lazy { ParseGson.gson!! }

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


        fun Trip?.isCancellable() =
             this !=null && this.tripStatus in listOf(
                TripStatus.WEIGH_EMPTY, TripStatus.PICK_UP
            )
        fun Trip?.containerPickedUp():Boolean{
            return this!= null &&
                    this.tripStatus !in arrayListOf(
                        TripStatus.START, TripStatus.PICK_UP, TripStatus.WEIGH_EMPTY)
        }

        fun Driver?.isInvalid(): Boolean{
            return this == null || (TextUtils.isEmpty(this.firstName))
        }

        fun LocalTrip?.isInvalid(): Boolean{
            return this == null || this.trip == null
        }


        inline fun <I, reified O> I.convert(): O {
            val json = this.toJson()
            return gson.fromJson(json, object : TypeToken<O>() {}.type)
        }

        inline fun <reified O> String.convert(): O {
            return gson.fromJson(this, object : TypeToken<O>() {}.type)
        }

        fun <K> K.toJson(): String {
            return gson.toJson(this)
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

        fun JsonObject.monthIndex(): Int {
            val value = this.get("month")
            return try {
                value.asInt
            } catch (e: NumberFormatException) {
                val date: Date = SimpleDateFormat("MMMM").parse(value.asString)
                val cal = Calendar.getInstance()
                cal.time = date
                cal[Calendar.MONTH] + 1
            }
        }


        fun String?.isValidContainerNo() =
            !TextUtils.isEmpty(this) && this?.length ?:0 == 11

        fun JobCardItem?.preAssigned() =
            this != null && this.id != 0

        fun String?.stripCountryCode(): String? {
            return if (this == null)
                null
            else {
                val match = Regex("^(\\+264)?(\\d+)?(8[1,5]\\d+)$").find(this)
                val (_, _, cell) = match!!.destructured
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
        fun String?.isValidVehicleNo(): Boolean {
            val pattern = Pattern.compile("^[hH]\\d{2,}$")
            return !this.isNullOrEmpty() && pattern.matcher(this).matches()
        }

        @JvmStatic
        fun String?.toPhone(): String {

            return "+264${this?.trimStart { it == '0' }}"
        }

        fun String?.isValidPlateNo(): Boolean {
            val pattern = Pattern.compile("^[nN]\\d+[aA-zZ]+$")
            return !this.isNullOrEmpty() && pattern.matcher(this).matches()
        }

        fun String?.isValidOdo() =  !TextUtils.isEmpty(this) && this?.toDouble() ?: 0.0 > 0.0

        fun Long?.isValidWeight() =
            !TextUtils.isEmpty(this.toString()) && this !=null &&this >= 7000L

        fun isValidAuthCode(code: String?): Boolean {
            return code?.length ?: 0 == 6
        }
    }
}