package io.radar.flutter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import io.flutter.plugin.common.MethodChannel
import io.radar.sdk.Radar
import io.radar.sdk.RadarInAppMessageReceiver
import io.radar.sdk.RadarInAppMessageView
import io.radar.sdk.model.RadarInAppMessage
import com.google.gson.Gson
import org.json.JSONObject
import java.util.*

class RadarFlutterInAppMessageReceiver(private val channel: MethodChannel) : RadarInAppMessageReceiver {

    companion object {
        private const val TAG = "RadarFlutterInAppMessageReceiver"
        private val lock = Any()
        private val mainHandler = Handler(Looper.getMainLooper())
    }

    override fun onNewInAppMessage(inAppMessage: RadarInAppMessage) {
        try {
            val obj = JSONObject()
            obj.put("inAppMessage", inAppMessage.toJson())

            val res = Gson().fromJson(obj.toString(), HashMap::class.java) as HashMap<String, Any>
            val inAppMessageArgs = ArrayList<Any>()
            inAppMessageArgs.add(0)
            inAppMessageArgs.add(res)
            
            synchronized(lock) {
                mainHandler.post {
                    channel.invokeMethod("inAppMessage", inAppMessageArgs)
                }
            }

            Radar.showInAppMessage(inAppMessage)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    override fun onInAppMessageDismissed(inAppMessage: RadarInAppMessage) {
        try {
            val obj = JSONObject()
            obj.put("inAppMessage", inAppMessage.toJson())

            val res = Gson().fromJson(obj.toString(), HashMap::class.java) as HashMap<String, Any>
            val dismissedArgs = ArrayList<Any>()
            dismissedArgs.add(0)
            dismissedArgs.add(res)
            
            synchronized(lock) {
                mainHandler.post {
                    channel.invokeMethod("inAppMessageDismissed", dismissedArgs)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    override fun onInAppMessageButtonClicked(inAppMessage: RadarInAppMessage) {
        try {
            val obj = JSONObject()
            obj.put("inAppMessage", inAppMessage.toJson())

            val res = Gson().fromJson(obj.toString(), HashMap::class.java) as HashMap<String, Any>
            val buttonClickedArgs = ArrayList<Any>()
            buttonClickedArgs.add(0)
            buttonClickedArgs.add(res)
            
            synchronized(lock) {
                mainHandler.post {
                    channel.invokeMethod("inAppMessageButtonClicked", buttonClickedArgs)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    // override fun createInAppMessageView(
    //     context: Context,
    //     inAppMessage: RadarInAppMessage,
    //     onDismissListener: (() -> Unit)?,
    //     onInAppMessageButtonClicked: (() -> Unit)?,
    //     onViewReady: (View) -> Unit
    // ) {
    //     super.createInAppMessageView(
    //         context,
    //         inAppMessage,
    //         onDismissListener,
    //         onInAppMessageButtonClicked,
    //         onViewReady
    //     )
    // }
}
