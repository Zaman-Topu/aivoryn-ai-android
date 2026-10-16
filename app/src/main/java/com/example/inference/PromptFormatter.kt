package com.example.inference

import com.example.data.local.MessageEntity

enum class PromptTemplateType {
    CHATML,
    LLAMA_3,
    MISTRAL,
    ALPACA
}

object PromptFormatter {
    fun formatConversation(
        architecture: String,
        messages: List<MessageEntity>,
        isThinkMode: Boolean,
        attachedContent: String?
    ): String {
        val template = when (architecture.lowercase()) {
            "llama" -> PromptTemplateType.LLAMA_3
            "qwen2", "phi3" -> PromptTemplateType.CHATML
            "mistral" -> PromptTemplateType.MISTRAL
            else -> PromptTemplateType.CHATML
        }

        val systemPrompt = buildString {
            append("You are Aivoryn AI, a helpful, precise, and concise on-device AI assistant.")
            if (isThinkMode) {
                append(" When answering, think methodically and provide deep reasoning. Format your thoughts inside <think> and </think> tags before your final response.")
            }
            if (!attachedContent.isNullOrBlank()) {
                append("\n\nContext attached by user:\n---\n$attachedContent\n---")
            }
        }

        return when (template) {
            PromptTemplateType.CHATML -> formatChatMl(systemPrompt, messages)
            PromptTemplateType.LLAMA_3 -> formatLlama3(systemPrompt, messages)
            PromptTemplateType.MISTRAL -> formatMistral(systemPrompt, messages)
            PromptTemplateType.ALPACA -> formatAlpaca(systemPrompt, messages)
        }
    }

    private fun formatChatMl(systemPrompt: String, messages: List<MessageEntity>): String {
        val sb = StringBuilder()
        sb.append("<|im_start|>system\n$systemPrompt<|im_end|>\n")
        for (msg in messages) {
            when (msg.role) {
                "user" -> {
                    sb.append("<|im_start|>user\n")
                    if (!msg.attachedContentPreview.isNullOrBlank()) {
                        sb.append("[Attachment: ${msg.attachedFileName}]\n${msg.attachedContentPreview}\n\n")
                    }
                    sb.append("${msg.content}<|im_end|>\n")
                }
                "assistant" -> {
                    sb.append("<|im_start|>assistant\n")
                    if (!msg.thinkContent.isNullOrBlank()) {
                        sb.append("<think>\n${msg.thinkContent}\n</think>\n")
                    }
                    sb.append("${msg.content}<|im_end|>\n")
                }
            }
        }
        sb.append("<|im_start|>assistant\n")
        return sb.toString()
    }

    private fun formatLlama3(systemPrompt: String, messages: List<MessageEntity>): String {
        val sb = StringBuilder()
        sb.append("<|begin_of_text|><|start_header_id|>system<|end_header_id|>\n\n$systemPrompt<|eot_id|>")
        for (msg in messages) {
            when (msg.role) {
                "user" -> {
                    sb.append("<|start_header_id|>user<|end_header_id|>\n\n")
                    if (!msg.attachedContentPreview.isNullOrBlank()) {
                        sb.append("[Attachment: ${msg.attachedFileName}]\n${msg.attachedContentPreview}\n\n")
                    }
                    sb.append("${msg.content}<|eot_id|>")
                }
                "assistant" -> {
                    sb.append("<|start_header_id|>assistant<|end_header_id|>\n\n")
                    if (!msg.thinkContent.isNullOrBlank()) {
                        sb.append("<think>\n${msg.thinkContent}\n</think>\n")
                    }
                    sb.append("${msg.content}<|eot_id|>")
                }
            }
        }
        sb.append("<|start_header_id|>assistant<|end_header_id|>\n\n")
        return sb.toString()
    }

    private fun formatMistral(systemPrompt: String, messages: List<MessageEntity>): String {
        val sb = StringBuilder()
        sb.append("<s>[INST] $systemPrompt\n\n")
        val lastIndex = messages.size - 1
        messages.forEachIndexed { index, msg ->
            if (msg.role == "user") {
                if (index > 0) sb.append("[INST] ")
                sb.append(msg.content)
                sb.append(" [/INST]")
            } else if (msg.role == "assistant") {
                sb.append(" ")
                sb.append(msg.content)
                if (index < lastIndex) sb.append("</s>")
            }
        }
        return sb.toString()
    }

    private fun formatAlpaca(systemPrompt: String, messages: List<MessageEntity>): String {
        val sb = StringBuilder()
        sb.append("$systemPrompt\n\n")
        for (msg in messages) {
            if (msg.role == "user") {
                sb.append("### Instruction:\n${msg.content}\n\n")
            } else if (msg.role == "assistant") {
                sb.append("### Response:\n${msg.content}\n\n")
            }
        }
        sb.append("### Response:\n")
        return sb.toString()
    }
}
