package screen

/*import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diplom1.ui.theme.BlueBlack
import com.example.diplom1.uiComponets.IDCardComponets
import com.google.firebase.firestore.FirebaseFirestore
import firebase.FirebaseRegistrations
import org.webrtc.AudioTrack
import org.webrtc.DataChannel
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.MediaStreamTrack
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.RtpReceiver
import org.webrtc.RtpTransceiver
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import viewModel.CardVolonterViewModel
import viewModel.ChatViewModel
import viewModel.UserType

@Composable
fun ListFrendAudioCall(
    context: Context,
    userType: UserType,
    chatViewModel: ChatViewModel = viewModel(),
    cardVolonterViewModel: CardVolonterViewModel,
    navigateToAudioCall: () -> Unit,
) {
    val idCardsComponets = IDCardComponets()
    chatViewModel.auchID.value = FirebaseRegistrations().userID().toString()
    val friends = remember { mutableStateListOf<String>() } // Список друзей, нужно инициализировать

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBlack)
    ) {
        idCardsComponets.LazyColumnListO(
            context = context,
            cardVolonterViewModel = cardVolonterViewModel,
            userType = userType,
            saveId = chatViewModel.saveID,
            onClick = {

                navigateToAudioCall()
            }
        )

    }
}

@Composable
fun MainScreen(callerId: String, calleeId: String, userType: UserType) {
    var isCall = remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(calleeId) {
        listenForIncomingCalls(calleeId) { callId ->
            isCall.value = true
        }
    }

    if (isCall.value) {
        IncomingCallUI(userId = calleeId)
    } else {
        ListFrendAudioCall(
            context = context,
            userType = userType, // Замените на фактический UserType
            cardVolonterViewModel = viewModel(),
            navigateToAudioCall = {
                // Обработка навигации к экрану аудиозвонка
            }
        )
    }
}


@Composable
fun IncomingCallUI(userId: String) {
    val context = LocalContext.current
    val incomingCallId = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        listenForIncomingCalls(userId) { callId ->
            incomingCallId.value = callId
        }
    }

    incomingCallId.value?.let { callId ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            androidx.compose.material3.Text(text = "Incoming Call")
            Button(
                onClick = {
                    // Принять звонок
                    val peerConnectionFactory = initPeerConnectionFactory(context)
                    val peerConnection = createPeerConnection(peerConnectionFactory, mutableStateOf(null), callId, "callee")
                    startCall(
                        peerConnection,
                        null,
                        mutableStateOf(null),
                        peerConnectionFactory,
                        mutableStateOf(null),
                        callId,
                        "callee"
                    )
                    // Обновить статус в Firestore
                    FirebaseFirestore.getInstance().collection("calls").document(callId)
                        .update("status", "accepted")
                },
                modifier = Modifier.padding(16.dp)
            ) {
                androidx.compose.material3.Text("Принять звонок")
            }
            Button(
                onClick = {
                    // Отклонить звонок
                    FirebaseFirestore.getInstance().collection("calls").document(callId)
                        .update("status", "rejected")
                    incomingCallId.value = null
                },
                modifier = Modifier.padding(16.dp)
            ) {
                androidx.compose.material3.Text("Отклонить звонок")
            }
        }
    }
}

fun listenForIncomingCalls(userId: String, onIncomingCall: (String) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()
    firestore.collection("calls")
        .whereEqualTo("calleeId", userId)
        .whereEqualTo("status", "calling")
        .addSnapshotListener { snapshot, _ ->
            snapshot?.documents?.forEach { document ->
                val callId = document.id
                onIncomingCall(callId)
            }
        }
}

fun initPeerConnectionFactory(context: Context): PeerConnectionFactory {
    val initializationOptions = PeerConnectionFactory.InitializationOptions.builder(context)
        .createInitializationOptions()
    PeerConnectionFactory.initialize(initializationOptions)

    val options = PeerConnectionFactory.Options()
    val encoderFactory = DefaultVideoEncoderFactory(
        EglBase.create().eglBaseContext, true, true)
    val decoderFactory = DefaultVideoDecoderFactory(EglBase.create().eglBaseContext)

    return PeerConnectionFactory.builder()
        .setOptions(options)
        .setVideoEncoderFactory(encoderFactory)
        .setVideoDecoderFactory(decoderFactory)
        .createPeerConnectionFactory()
}

fun startCall(
    peerConnection: PeerConnection,
    remoteDescription: SessionDescription?,
    localAudioTrack: MutableState<AudioTrack?>,
    peerConnectionFactory: PeerConnectionFactory,
    remoteAudioTrack: MutableState<AudioTrack?>,
    callId: String,
    userType: String
) {
    val audioConstraints = MediaConstraints()

    val audioSource = peerConnectionFactory.createAudioSource(audioConstraints)
    val localTrack = peerConnectionFactory.createAudioTrack("ARDAMSa0", audioSource)
    localAudioTrack.value = localTrack

    val mediaStream = peerConnectionFactory.createLocalMediaStream("ARDAMS")
    mediaStream.addTrack(localTrack)
    peerConnection.addStream(mediaStream)

    remoteDescription?.let {
        peerConnection.setRemoteDescription(object : SdpObserver {
            override fun onCreateSuccess(p0: SessionDescription?) {}
            override fun onSetSuccess() {}
            override fun onCreateFailure(p0: String?) {}
            override fun onSetFailure(p0: String?) {}
        }, it)
    }

    peerConnection.createOffer(object : SdpObserver {
        override fun onCreateSuccess(sessionDescription: SessionDescription) {
            peerConnection.setLocalDescription(object : SdpObserver {
                override fun onCreateSuccess(p0: SessionDescription?) {}
                override fun onSetSuccess() {
                    // Отправить SDP в Firestore
                    sendSdp(callId, userType, sessionDescription)
                }
                override fun onCreateFailure(p0: String?) {}
                override fun onSetFailure(p0: String?) {}
            }, sessionDescription)
        }
        override fun onSetSuccess() {}
        override fun onCreateFailure(error: String) {}
        override fun onSetFailure(error: String) {}
    }, MediaConstraints())
}

private fun createPeerConnection(
    peerConnectionFactory: PeerConnectionFactory,
    remoteAudioTrack: MutableState<AudioTrack?>,
    callId: String,
    userType: String
): PeerConnection {
    val iceServers = listOf(
        PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
    )

    val rtcConfig = PeerConnection.RTCConfiguration(iceServers)
    val pcObserver = object : PeerConnection.Observer {
        override fun onIceCandidate(candidate: IceCandidate) {
            // Отправить ICE кандидата в Firestore
            sendIceCandidate(callId, userType, candidate)
        }

        override fun onTrack(rtpTransceiver: RtpTransceiver) {
            val track = rtpTransceiver.receiver.track()
            if (track?.kind() == MediaStreamTrack.AUDIO_TRACK_KIND) {
                remoteAudioTrack.value = track as AudioTrack
            }
        }

        override fun onIceCandidatesRemoved(candidates: Array<IceCandidate>) {}
        override fun onDataChannel(dataChannel: DataChannel) {}
        override fun onIceConnectionChange(newState: PeerConnection.IceConnectionState) {}
        override fun onIceConnectionReceivingChange(receiving: Boolean) {}
        override fun onIceGatheringChange(newState: PeerConnection.IceGatheringState) {}
        override fun onAddStream(stream: MediaStream) {}
        override fun onRemoveStream(stream: MediaStream) {}
        override fun onRenegotiationNeeded() {}
        override fun onAddTrack(receiver: RtpReceiver?, mediaStreams: Array<out MediaStream>?) {}
        override fun onSignalingChange(newState: PeerConnection.SignalingState) {}
    }

    return peerConnectionFactory.createPeerConnection(rtcConfig, pcObserver)!!
}

fun sendSdp(callId: String, userType: String, sdp: SessionDescription) {
    val callDocument = FirebaseFirestore.getInstance().collection("calls").document(callId)
    callDocument.collection(userType).document("sdp").set(mapOf(
        "type" to sdp.type.canonicalForm(),
        "description" to sdp.description
    ))
}

fun sendIceCandidate(callId: String, userType: String, candidate: IceCandidate) {
    val callDocument = FirebaseFirestore.getInstance().collection("calls").document(callId)
    val iceCandidatesCollection = callDocument.collection(userType).document("iceCandidates")
    iceCandidatesCollection.collection("candidates").document().set(mapOf(
        "candidate" to candidate.sdp,
        "sdpMid" to candidate.sdpMid,
        "sdpMLineIndex" to candidate.sdpMLineIndex
    ))
}

fun listenForSdp(callId: String, userType: String, onSdpReceived: (SessionDescription) -> Unit) {
    val callDocument = FirebaseFirestore.getInstance().collection("calls").document(callId)
    callDocument.collection(userType).document("sdp").addSnapshotListener { snapshot, _ ->
        snapshot?.data?.let { data ->
            val type = SessionDescription.Type.fromCanonicalForm(data["type"] as String)
            val description = data["description"] as String
            onSdpReceived(SessionDescription(type, description))
        }
    }
}

fun listenForIceCandidates(callId: String, userType: String, onIceCandidateReceived: (IceCandidate) -> Unit) {
    val callDocument = FirebaseFirestore.getInstance().collection("calls").document(callId)
    callDocument.collection(userType).document("iceCandidates").collection("candidates")
        .addSnapshotListener { snapshot, _ ->
            snapshot?.documents?.forEach { document ->
                val candidate = document.getString("candidate") ?: return@forEach
                val sdpMid = document.getString("sdpMid") ?: return@forEach
                val sdpMLineIndex = document.getLong("sdpMLineIndex")?.toInt() ?: return@forEach
                onIceCandidateReceived(IceCandidate(sdpMid, sdpMLineIndex, candidate))
            }
        }
}

fun endCall(localAudioTrack: AudioTrack?) {
    localAudioTrack?.let {
        it.setEnabled(false)
        it.dispose()
    }
}

 */