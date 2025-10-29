# 🧩 Summarise It — Chrome Extension with Spring Boot Backend

**Summarise It** is a lightweight Chrome extension that allows users to **summarize any selected text on a webpage** all powered by a **Spring Boot backend** that integrates with the **Gemini API**.


### 🧠 1. Summarise
- Select any portion of text on a webpage.  
- Click on the *Summarise It* icon.  
- Instantly receive a **concise, meaningful summary** powered by the Gemini API.


## 🏗️ Architecture Overview
## 🏗️ Architecture Overview

```mermaid
flowchart TD

%% === FRONTEND ===
A[🧑‍💻 User selects text on webpage] --> B[🔹 Chrome Extension]

%% === BACKEND SUMMARIZATION ===
B --> C[📩 POST /api/summarise]
C --> D[☕ Spring Boot Backend]
D --> E[🧠 Gemini API - Summarization]
E --> F[🧾 Summary Returned]
F --> G[🪟 Extension UI displays summary]

%% === RELATED Q&A ===
G --> H[📩 POST /api/related-qna]
H --> I[☕ Backend Q&A Generator]
I --> J[🧠 Gemini API - Question Generation]
J --> K[💬 Related Questions (and short answers)]

%% === FURTHER READINGS ===
K --> L[📩 POST /api/further-readings]
L --> M[☕ Backend Article Recommender]
M --> N[🧠 Gemini API - Suggest Related Articles]
N --> O[📚 Related Reading Links Displayed in Extension]

%% === FUTURE RAG ENHANCEMENT ===
subgraph RAG[🔮 Future Enhancement: RAG Layer]
    P1[✳️ Embedding Generation (Gemini / Sentence Transformer)]
    P2[🗄️ Vector Database (FAISS / Pinecone / Weaviate)]
    P3[🔍 Context Retrieval]
    P4[⚡ Context + Query → Gemini for Grounded Answers]
end

I --> RAG
L --> RAG
RAG --> J
RAG --> N

