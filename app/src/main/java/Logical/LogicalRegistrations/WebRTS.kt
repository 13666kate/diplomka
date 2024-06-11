
import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import firebase.FirebaseRegistrations
import org.webrtc.AudioTrack
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.RtpReceiver
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import viewModel.CardVolonterViewModel

class WebRTS(cardVolonterViewModel: CardVolonterViewModel) {
    private lateinit var peerConnectionFactory: PeerConnectionFactory
    private lateinit var localAudioTrack: AudioTrack
    private var peerConnection: PeerConnection? = null
    private val firebaseFirestore = FirebaseFirestore.getInstance()
     val localUserId = FirebaseRegistrations().userID().toString()
     val remoteUserId = cardVolonterViewModel.saveID.value

    fun initializeWebRTC(context: Context) {
        // Инициализация PeerConnectionFactory
        val initializationOptions = PeerConnectionFactory.InitializationOptions.builder(context)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(initializationOptions)

        // Создание и настройка PeerConnectionFactory
        val options = PeerConnectionFactory.Options()
        peerConnectionFactory = PeerConnectionFactory.builder()
            .setOptions(options)
            .createPeerConnectionFactory()

        // Создание локальной аудиодорожки
        val audioConstraints = MediaConstraints()
        val audioSource = peerConnectionFactory.createAudioSource(audioConstraints)
        localAudioTrack = peerConnectionFactory.createAudioTrack("101", audioSource)

        // Создание PeerConnection
        val rtcConfig = PeerConnection.RTCConfiguration(emptyList())
        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, object : PeerConnection.Observer {
            // Обработчики событий PeerConnection
            override fun onSignalingChange(signalingState: PeerConnection.SignalingState) {}
            override fun onIceConnectionChange(iceConnectionState: PeerConnection.IceConnectionState) {}
            override fun onIceConnectionReceivingChange(b: Boolean) {}
            override fun onIceGatheringChange(iceGatheringState: PeerConnection.IceGatheringState) {}
            override fun onIceCandidate(iceCandidate: IceCandidate) {
                // Отправка ICE кандидата в Firestore
                firebaseFirestore.collection("calls").document(localUserId)
                    .collection("candidates").add(iceCandidate.toMap())
                Log.e("Call", "ID"+ localUserId.toString())

            }

            override fun onIceCandidatesRemoved(iceCandidates: Array<IceCandidate>) {}
            override fun onAddStream(mediaStream: MediaStream) {}
            override fun onRemoveStream(mediaStream: MediaStream) {}
            override fun onDataChannel(dataChannel: DataChannel) {}
            override fun onRenegotiationNeeded() {}
            override fun onAddTrack(rtpReceiver: RtpReceiver, mediaStreams: Array<MediaStream>) {}
        }) ?: throw IllegalStateException("Failed to create PeerConnection")
    }

    fun initiateCall() {
        // Проверяем, что PeerConnection инициализирован
        val pc = peerConnection ?: throw IllegalStateException("PeerConnection is not initialized")

        // Уточняем параметры MediaConstraints для создания предложения
        val mediaConstraints = MediaConstraints().apply {
            // Добавляем ограничения, если необходимо
        }

        // Создаем предложение для установки соединения
        pc.createOffer(object : SdpObserver {
            override fun onCreateSuccess(sessionDescription: SessionDescription) {
                // Устанавливаем локальное описание
                pc.setLocalDescription(this, sessionDescription)
                // Отправляем предложение в Firestore
                firebaseFirestore.collection("calls").document(remoteUserId)
                    .set(sessionDescription.toMap())
            }
            override fun onSetSuccess() {}
            override fun onCreateFailure(s: String) {}
            override fun onSetFailure(s: String) {}
        }, mediaConstraints)
    }

    fun answerCall() {
        val pc = peerConnection ?: throw IllegalStateException("PeerConnection is not initialized")

        firebaseFirestore.collection("calls").document(localUserId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    val offer = snapshot.toObject(SessionDescription::class.java)
                    pc.setRemoteDescription(object : SdpObserver {
                        override fun onCreateSuccess(sessionDescription: SessionDescription) {}
                        override fun onSetSuccess() {
                            val mediaConstraints = MediaConstraints()
                            pc.createAnswer(object : SdpObserver {
                                override fun onCreateSuccess(sessionDescription: SessionDescription) {
                                    pc.setLocalDescription(this, sessionDescription)
                                    firebaseFirestore.collection("calls").document(remoteUserId)
                                        .set(sessionDescription.toMap())
                                }
                                override fun onSetSuccess() {}
                                override fun onCreateFailure(s: String) {}
                                override fun onSetFailure(s: String) {}
                            }, mediaConstraints)
                        }
                        override fun onCreateFailure(s: String) {}
                        override fun onSetFailure(s: String) {}
                    }, offer)
                } else {
                    Log.e("Call", "Document not found")
                }
            }
    }

    fun rejectCall() {
        val pc = peerConnection ?: throw IllegalStateException("PeerConnection is not initialized")
        firebaseFirestore.collection("calls").document(remoteUserId).delete()
    }

    private fun SessionDescription.toMap(): Map<String, Any> {
        return mapOf("type" to type.canonicalForm(), "sdp" to description)
    }

    private fun IceCandidate.toMap(): Map<String, Any> {
        return mapOf("sdpMid" to sdpMid, "sdpMLineIndex" to sdpMLineIndex, "candidate" to sdp)
    }
}