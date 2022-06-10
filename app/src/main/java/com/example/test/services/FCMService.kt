package com.example.test.services

import com.example.test.domain.models.MainUserModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.greenrobot.eventbus.EventBus

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val id = message.data["id"]?.toInt() ?: 0
        val changesCount = message.data["changesCount"]?.toInt()?:0
        EventBus.getDefault().postSticky(MainUserModel(id, changesCount))
    }
}