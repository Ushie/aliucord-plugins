package com.github.ushie

import android.content.Context
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.after
import com.discord.models.domain.ModelMessageDelete
import com.discord.stores.StoreStream
import com.discord.widgets.chat.list.adapter.WidgetChatListAdapterItemMessage
import com.discord.widgets.chat.list.entries.MessageEntry
import com.discord.widgets.chat.list.entries.ChatListEntry

@Suppress("unused")
@AliucordPlugin
class AntiAfn : Plugin() {
    override fun start(context: Context) {
        patcher.after<WidgetChatListAdapterItemMessage>(
            "onConfigure",
            Int::class.java,
            ChatListEntry::class.java
        ) {
            val entry = it.args[1] as MessageEntry
            val author = entry.message.author.id
            val afnID = "420043923822608384".toLong()

            if (entry.message.isLoading) return@after

            if (author == afnID) {
                StoreStream.getMessages().handleMessageDelete(ModelMessageDelete(entry.message.channelId, entry.message.id))
            }
        }
    }

    override fun stop(context: Context) {
        patcher.unpatchAll()
    }
}
