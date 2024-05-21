package viewModel

/*
import org.mozilla.javascript.Scriptable
import org.mozilla.javascript.NativeObject
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig

class VideoCallViewModel():ViewModel(){

    val userID = mutableStateOf("")

    fun generateAgoraToken(appID: String, appCertificate: String, channelName: String, uid: Int, role: String, expirationTimeInSeconds: Int): String {
        val cx: Context = Context.enter()
        cx.optimizationLevel = -1

        try {
            val scope: Scriptable = cx.initStandardObjects()

            // Считываем JavaScript файл
            cx.evaluateString(scope, """
            const Agora = require('agora-access-token');

            function generateToken(appID, appCertificate, channelName, uid, role, expirationTimeInSeconds) {
                const currentTimestamp = Math.floor(Date.now() / 1000);
                const privilegeExpiredTs = currentTimestamp + expirationTimeInSeconds;

                const token = Agora.RtcTokenBuilder.buildTokenWithUid(appID, appCertificate, channelName, uid, role, privilegeExpiredTs);
                return token;
            }
        """.trimIndent(), "generateToken.js", 1, null)

            // Вызываем функцию generateToken из JavaScript
            val generateTokenFunction = scope.get("generateToken", scope) as org.mozilla.javascript.Function
            val result = generateTokenFunction.call(cx, scope, scope, arrayOf(appID, appCertificate, channelName, uid, role, expirationTimeInSeconds))
            return result as String
        } finally {
            Context.exit()
        }
    }

*/
/*    fun videoCallServiceUser(UserID:MutableState<String>, context: Context){
        val  application =  context // Android's application context
        val appID:Long= 1256948071;   // yourAppID
        val appSign = "f01e980e182ac07d79c1e7560ee49e8e2c361daad5e79be68c1402a57410c0e7"  // yourAppSign
        String userID =; // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName =;   // yourUserName

        ZegoUIKitPrebuiltCallInvitationConfig.callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();

        ZegoUIKitPrebuiltCallService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);
    }*//*

}*/
