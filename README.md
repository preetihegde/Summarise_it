# 🧩 Summarise It — Chrome Extension with Spring Boot Backend

**Summarise It** is a lightweight Chrome extension that allows users to **summarize any selected text on a webpage** all powered by a **Spring Boot backend** that integrates with the **Gemini API**.


### 🧠 1. Summarise
- Select any portion of text on a webpage.  
- Click on the *Summarise It* icon.  
- Instantly receive a **concise, meaningful summary** powered by the Gemini API.


## 🏗️ Architecture Overview
[User selects text]
      ↓
Chrome Extension → /api/summarise
      ↓
Spring Boot Backend → Gemini (summary)
      ↓
Extension UI shows summary → triggers /related-qna → /further-readings

