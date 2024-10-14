package Logical.LogicalRegistrations

import android.webkit.JavascriptInterface
import com.example.diplom1.navigations.NavigationsMainActivity

class JavascriptInterface(val mainActivity: NavigationsMainActivity) {
    @JavascriptInterface
    fun onPeerConnected() {
        mainActivity.onPeerConnected()
    }
}

