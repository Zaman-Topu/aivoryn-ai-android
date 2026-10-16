package com.example.data.repository

import com.example.data.local.ConversationDao
import com.example.data.local.ConversationEntity
import com.example.data.local.MessageDao
import com.example.data.local.MessageEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ChatRepository(
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao
) {
    val activeConversations: Flow<List<ConversationEntity>> = conversationDao.getActiveConversations()
    val archivedConversations: Flow<List<ConversationEntity>> = conversationDao.getArchivedConversations()

    fun searchConversations(query: String): Flow<List<ConversationEntity>> =
        conversationDao.searchConversations(query)

    fun getMessages(conversationId: String): Flow<List<MessageEntity>> =
        messageDao.getMessagesForConversation(conversationId)

    suspend fun getConversation(id: String): ConversationEntity? =
        conversationDao.getConversationById(id)

    suspend fun createNewConversation(title: String, modelName: String = ""): ConversationEntity {
        val conv = ConversationEntity(
            id = UUID.randomUUID().toString(),
            title = title.ifBlank { "New Chat" },
            modelName = modelName
        )
        conversationDao.insertConversation(conv)
        return conv
    }

    suspend fun renameConversation(id: String, title: String) {
        conversationDao.renameConversation(id, title)
    }

    suspend fun togglePin(id: String) {
        conversationDao.togglePin(id)
    }

    suspend fun toggleArchive(id: String) {
        conversationDao.toggleArchive(id)
    }

    suspend fun deleteConversation(id: String) {
        conversationDao.deleteConversationById(id)
    }

    suspend fun deleteAllConversations() {
        conversationDao.deleteAllConversations()
    }

    suspend fun saveMessage(message: MessageEntity) {
        messageDao.insertMessage(message)
        // Update conversation timestamp
        conversationDao.getConversationById(message.conversationId)?.let { conv ->
            conversationDao.updateConversation(conv.copy(updatedAt = System.currentTimeMillis()))
        }
    }

    suspend fun updateMessage(message: MessageEntity) {
        messageDao.updateMessage(message)
    }

    suspend fun deleteMessage(id: String) {
        messageDao.deleteMessageById(id)
    }

    suspend fun getMessagesList(conversationId: String): List<MessageEntity> =
        messageDao.getMessagesList(conversationId)
}
