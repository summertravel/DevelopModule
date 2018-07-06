package com.summer.developmodule

import android.app.Application
import com.summer.developmodule.greendaodemo.db.DaoMaster
import com.summer.developmodule.greendaodemo.db.DaoSession

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val db = object : DaoMaster.DevOpenHelper(this, "dm_db") {}.writableDb
        newSession = DaoMaster(db).newSession()
    }

    companion object {
        var newSession: DaoSession? = null
        fun getDaosession(): DaoSession? {
            return newSession
        }
    }

}