package com.example.bloodik.picwatcher

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.util.Pair
import java.io.IOException
import java.net.URL
import java.util.*

class Loader : IntentService("Loader") {

    private val responses = LinkedList<Pair<Int, ByteArray>>()
    private val main = Handler(Looper.getMainLooper())
    private var callback: OnLoad? = null

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            val url = intent.getStringExtra(EXTRA_URL)
            val id = intent.getStringExtra(CLIENT_ID)
            if (ACTION_LOAD == action && url != null) {
                loadPicture(id, url)
            }
        }
    }

    private fun loadPicture(id: String, urlString: String) {
        var res: ByteArray?
        try {
            val url = URL(urlString)
            val connection = url.openConnection()
            connection.connect()
            res = ByteArray(connection.contentLength)
            val istr = connection.getInputStream().use { istr ->
                var p = 0
                var r = istr.read(res!!, p, res!!.size - p)
                while (r > 0) {
                    p += r
                    r = istr.read(res!!, p, res!!.size - p)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            res = null
        }

        Log.d(LOG_TAG, "loadPicture: got: id: " + id + ", data.length = " + res?.size)
        //main.post { deliver(id, res) }
    }

    private fun deliver(id: Int, data: ByteArray?) {
        if (callback != null)
            callback!!.onLoad(Pair.create(id, data))
            responses.add(Pair.create(id, data))
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(LOG_TAG, "onBind: ")
        return MyBinder()
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.d(LOG_TAG, "onUnbind: ")
        callback = null
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent) {
        Log.d(LOG_TAG, "onRebind: ")
        super.onRebind(intent)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "onCreate: ")
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        Log.d(LOG_TAG, "onStart: ")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, "onStartCommand: ")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "onDestroy: ")
    }

    class MyBinder : Binder() {
        private val service = Loader()

        fun setCallback(callback: OnLoad) {
            Handler(Looper.getMainLooper()).post {
                service.callback = callback
                while (!service.responses.isEmpty())
                    service.callback!!.onLoad(service.responses.remove())
            }
        }
    }

    interface OnLoad {
        fun onLoad(data: Pair<Int, ByteArray>)
    }

    companion object {
        private val ACTION_LOAD = "load"
        private val EXTRA_URL = "https://api.unsplash.com/photos/random?client_id=89014e7699c564cd4cace57b2819a555b8bedef36c71c356a4825fb83f3a9806"
        private val CLIENT_ID = "?client_id=89014e7699c564cd4cace57b2819a555b8bedef36c71c356a4825fb83f3a9806"
        private val LOG_TAG = Loader::class.java.simpleName

        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        fun load(context: Context) {
            val intent = Intent(context, Loader::class.java)
            intent.action = ACTION_LOAD
            intent.putExtra(EXTRA_URL, this.EXTRA_URL)
            intent.putExtra(CLIENT_ID, this.CLIENT_ID)
            context.startService(intent)
        }
    }
}
