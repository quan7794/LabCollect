package com.wac.labcollect


const val COVID_SERVER = "https://203.254.249.240/COVIDSEHC/"
const val SCV_BASE_URL = "https://203.254.249.240/"
const val SCV_ENDPOINT_URL = "COVIDSEHC"

const val CONFIG_HOST = "https://raw.githubusercontent.com"
const val CONFIG_URL = "/quan7794/jsonDemo/main/config.json"
const val ABOUT_URL = "https://quan7794.github.io/S-Covid-Page/aboutme/"
const val GUIDE_URL = "https://quan7794.github.io/S-Covid-Page/guideline/"
const val DOWNLOAD_URL = "https://quan7794.github.io/S-Covid-Page/download/"
const val SYSTEM_CONFIG = "system_config"
const val USER_CONFIG = "user_config"
const val DARK_MODE_CONFIG = "dark_mode_config"
const val CONFIG_VERSION = "config_version"
const val UNKNOWN = ""
const val FORM_DATA = "formData"

const val SUBMIT_HOST = "submit_host"
const val COMP_TYPE = "CompType"
const val COMP_NAME = "CompName"
const val GEN_ID = "GenNo"
const val FULL_NAME = "Fullname"
const val PHONE = "Phone"
const val DUP_YN = "DupYn"
const val Q1YN = "Q1YN"
const val Q2YN = "Q2YN"
const val Q3YN = "Q3YN"
const val Q4YN = "Q4YN"
const val Q5YN = "Q5YN"
const val Q6YN = "Q6YN"
const val Q7YN = "Q7YN"
const val Q8YN = "Q8YN"
const val Q9YN = "Q9YN"
const val Q10YN = "Q10YN"
const val ACCESS_TOKEN_KEY = "__RequestVerificationToken"
const val DARK_MODE_TYPE = "dark_mode_type"

//WorkerTag
const val DAILY_DECLARATION_WORK = "DailyDeclaration"
const val DAILY_REMINDER_WORK = "DailyReminder"

//Schedule constance
const val SCHEDULE_TYPE = "scheduleType"
const val TYPE_REMINDER = "Reminder"
const val TYPE_AUTO_DECLARE = "Auto declaration"
const val TYPE_MANUAL_DECLARE = "Manual declaration"
const val TYPE_UNKNOW = "Unknow"
const val WEEK_DAYS = "weekdays"

//Declaration result
const val DECLARE_SUCCESS = "thành công"
const val DECLARE_FAIL = "Lỗi"

//DialogType
const val DIALOG_DELETE_CONFIRM = "deleteConfirm"
const val DIALOG_CREATE_UPDATE_CONFIRM = "createOrUpdateConfirm"

//About page style
const val PAGE_STYLE_KEY = "PAGE_STYLE_KEY"
const val ABOUT_PAGE_STYLE = "ABOUT_PAGE_STYLE"
const val UPDATE_PAGE_STYLE = "UPDATE_PAGE_STYLE"
const val GUIDELINE_PAGE_STYLE = "GUIDELINE_PAGE_STYLE"

//App start action
const val START_ACTION ="START_ACTION"
const val START_ACTION_SHOW_HISTORY_SCREEN ="START_ACTION_SHOW_HISTORY_SCREEN"

//
const val AGREE_AUTO_DECLARATION = "AGREE_AUTO_DECLARATION"
enum class JobType() {
    CREATE, UPDATE
}