package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.MessageEntity
import com.example.inference.GgufMetadataParser
import com.example.inference.PromptFormatter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context matches Aivoryn AI`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Aivoryn AI", appName)
    }

    @Test
    fun `prompt formatter formats chatml conversation correctly`() {
        val messages = listOf(
            MessageEntity(
                conversationId = "conv-1",
                role = "user",
                content = "Hello Aivoryn"
            ),
            MessageEntity(
                conversationId = "conv-1",
                role = "assistant",
                content = "Hello! How can I help you today?"
            )
        )

        val formatted = PromptFormatter.formatConversation(
            architecture = "qwen2",
            messages = messages,
            isThinkMode = false,
            attachedContent = null
        )

        assertTrue(formatted.contains("<|im_start|>system"))
        assertTrue(formatted.contains("<|im_start|>user\nHello Aivoryn<|im_end|>"))
        assertTrue(formatted.contains("<|im_start|>assistant\nHello! How can I help you today?<|im_end|>"))
    }

    @Test
    fun `gguf parser validates non-gguf file gracefully`() {
        val dummyFile = File.createTempFile("test_model", ".bin")
        dummyFile.writeText("This is not a real GGUF file")

        val info = GgufMetadataParser.parse(dummyFile)
        assertFalse(info.isValidGguf)
        dummyFile.delete()
    }

    @Test
    fun `gguf parser detects valid gguf magic bytes`() {
        val tempGguf = File.createTempFile("mock_model", ".gguf")
        FileOutputStream(tempGguf).use { fos ->
            val bb = ByteBuffer.allocate(32).order(ByteOrder.LITTLE_ENDIAN)
            bb.putInt(0x46554747) // GGUF magic
            bb.putInt(3)          // version 3
            bb.putLong(120L)      // tensor count
            bb.putLong(10L)       // kv count
            fos.write(bb.array())
        }

        val info = GgufMetadataParser.parse(tempGguf)
        assertTrue(info.isValidGguf)
        assertEquals(3, info.version)
        assertEquals(120L, info.tensorCount)
        assertEquals(10L, info.kvCount)

        tempGguf.delete()
    }
}
