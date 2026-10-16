package com.example.data.model

data class HuggingFaceModel(
    val id: String,
    val name: String,
    val repoName: String,
    val fileName: String,
    val downloadUrl: String,
    val sizeFormatted: String,
    val sizeBytes: Long,
    val ramRequirementMb: Int,
    val quant: String,
    val architecture: String,
    val contextLength: Int,
    val description: String,
    val isRecommendedForLowRam: Boolean
)

object HuggingFaceCatalog {
    val curatedModels: List<HuggingFaceModel> = listOf(
        HuggingFaceModel(
            id = "qwen2.5-0.5b-instruct",
            name = "Qwen 2.5 0.5B Instruct",
            repoName = "Qwen/Qwen2.5-0.5B-Instruct-GGUF",
            fileName = "qwen2.5-0.5b-instruct-q4_k_m.gguf",
            downloadUrl = "https://huggingface.co/Qwen/Qwen2.5-0.5B-Instruct-GGUF/resolve/main/qwen2.5-0.5b-instruct-q4_k_m.gguf",
            sizeFormatted = "398 MB",
            sizeBytes = 398L * 1024 * 1024,
            ramRequirementMb = 480,
            quant = "Q4_K_M",
            architecture = "Qwen2",
            contextLength = 2048,
            description = "Alibaba's ultra-compact, high-speed conversational model. Optimized for smooth execution on mobile CPUs.",
            isRecommendedForLowRam = true
        ),
        HuggingFaceModel(
            id = "smollm2-360m-instruct",
            name = "SmolLM2 360M Instruct",
            repoName = "HuggingFaceTB/SmolLM2-360M-Instruct-GGUF",
            fileName = "smollm2-360m-instruct-q4_k_m.gguf",
            downloadUrl = "https://huggingface.co/HuggingFaceTB/SmolLM2-360M-Instruct-GGUF/resolve/main/smollm2-360m-instruct-q4_k_m.gguf",
            sizeFormatted = "229 MB",
            sizeBytes = 229L * 1024 * 1024,
            ramRequirementMb = 340,
            quant = "Q4_K_M",
            architecture = "Llama",
            contextLength = 2048,
            description = "Lightweight mobile LLM crafted by Hugging Face TB. Extremely low RAM overhead and lightning-fast responses.",
            isRecommendedForLowRam = true
        ),
        HuggingFaceModel(
            id = "llama-3.2-1b-instruct",
            name = "Llama 3.2 1B Instruct",
            repoName = "bartowski/Llama-3.2-1B-Instruct-GGUF",
            fileName = "Llama-3.2-1B-Instruct-Q4_K_M.gguf",
            downloadUrl = "https://huggingface.co/bartowski/Llama-3.2-1B-Instruct-GGUF/resolve/main/Llama-3.2-1B-Instruct-Q4_K_M.gguf",
            sizeFormatted = "808 MB",
            sizeBytes = 808L * 1024 * 1024,
            ramRequirementMb = 920,
            quant = "Q4_K_M",
            architecture = "Llama3",
            contextLength = 2048,
            description = "Meta's official lightweight edge model designed for reasoning, summarization, and multilingual dialogue.",
            isRecommendedForLowRam = true
        ),
        HuggingFaceModel(
            id = "deepseek-r1-distill-qwen-1.5b",
            name = "DeepSeek R1 Distill Qwen 1.5B",
            repoName = "bartowski/DeepSeek-R1-Distill-Qwen-1.5B-GGUF",
            fileName = "DeepSeek-R1-Distill-Qwen-1.5B-Q4_K_M.gguf",
            downloadUrl = "https://huggingface.co/bartowski/DeepSeek-R1-Distill-Qwen-1.5B-GGUF/resolve/main/DeepSeek-R1-Distill-Qwen-1.5B-Q4_K_M.gguf",
            sizeFormatted = "1.1 GB",
            sizeBytes = 1120L * 1024 * 1024,
            ramRequirementMb = 1450,
            quant = "Q4_K_M",
            architecture = "Qwen2",
            contextLength = 2048,
            description = "DeepSeek reasoning model with internal thought process. Explains its cognitive chain before answering.",
            isRecommendedForLowRam = false
        ),
        HuggingFaceModel(
            id = "gemma-2-2b-it",
            name = "Gemma 2 2B Instruct",
            repoName = "bartowski/gemma-2-2b-it-GGUF",
            fileName = "gemma-2-2b-it-Q4_K_M.gguf",
            downloadUrl = "https://huggingface.co/bartowski/gemma-2-2b-it-GGUF/resolve/main/gemma-2-2b-it-Q4_K_M.gguf",
            sizeFormatted = "1.6 GB",
            sizeBytes = 1630L * 1024 * 1024,
            ramRequirementMb = 1950,
            quant = "Q4_K_M",
            architecture = "Gemma2",
            contextLength = 2048,
            description = "Google DeepMind's state-of-the-art open model with outstanding coding and general knowledge benchmarks.",
            isRecommendedForLowRam = false
        )
    )
}
