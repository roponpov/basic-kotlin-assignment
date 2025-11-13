package models

import utils.getValueOf
import utils.rawArrayOf

class ApiResponseModel<T> (
    var code: String,
    var status: String,
    var message: String,
    var messageDev: String,
    var data:T,
){

    private val red = "\u001B[31m"
    private val reset = "\u001B[0m"

    override fun toString(): String {
        return buildString {
            appendLine("📦 ApiResponseModel")
            appendLine("├─ Code       : ${red}$code$reset")
            appendLine("├─ Status     : ${red}$status$reset")
            appendLine("├─ Message    : ${red}$message$reset")
            appendLine("├─ MessageDev : ${red}${messageDev.ifBlank { "(none)" }}$reset")
            appendLine("└─ Data       : ${red}${(data)}$reset")
        }
    }

    companion object {
        fun <T> fromJson(json: String, dataMapper: (String) -> T): ApiResponseModel<T> {
            return ApiResponseModel(
                code = json.getValueOf("code"),
                status = json.getValueOf("status"),
                message = json.getValueOf("message"),
                messageDev = json.getValueOf("messageDev"),
                data = dataMapper(json.rawArrayOf("data")),
            )
        }
    }
}