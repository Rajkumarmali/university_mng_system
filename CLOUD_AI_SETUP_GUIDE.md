# ⚡ Fast Cloud AI Setup Guide (Groq LPU)

This guide shows how to use **Groq Cloud API** for ultra-fast (~0.3s - 0.5s) AI responses with **0% RAM and CPU usage** on your Mac.

---

## 🎯 Why Groq Cloud API?

| Feature             | Local Ollama (llama2)                                | Groq Cloud API (llama-3.1-8b)                        |
| ------------------- | ---------------------------------------------------- | ---------------------------------------------------- |
| **Response Time**   | 30 - 60+ seconds                                     | **~0.3 - 0.5 seconds (Sub-second)**                  |
| **Mac RAM Usage**   | 4.5 GB - 5 GB (causes system freeze/hang on 8GB Mac) | **0 MB (0% RAM load)**                               |
| **Mac CPU/Battery** | 100% CPU usage, battery drains, fans spin            | **0% CPU usage, no heat**                            |
| **Cost**            | Free                                                 | **100% Free** (no credit card needed)                |
| **Rate Limit**      | Unlimited                                            | 14,400 requests/day, 30 req/min (generous free tier) |

---

## 🔑 Step 1: Get Your Free Groq API Key (Takes 30 seconds)

1. Open **[https://console.groq.com/keys](https://console.groq.com/keys)** in your browser.
2. Sign in with Google or GitHub (Free, no credit card required).
3. Click **"Create API Key"**.
4. Name it anything (e.g., `UniversitySystem`) and click **"Submit"**.
5. Copy the generated key (it looks like `gsk_...`).

---

## ⚙️ Step 2: Add Key to `application.properties`

Open:
`UniversityManagementSystem/src/main/resources/application.properties`

Replace `YOUR_GROQ_API_KEY` with your actual key:

```properties
# AI Provider: 'groq' (Fast Cloud) or 'ollama' (Local)
ai.provider=groq

# Groq Cloud API Key
groq.api-key=gsk_your_actual_groq_api_key_here
groq.base-url=https://api.groq.com/openai/v1
groq.model=llama-3.1-8b-instant
```

> **Note:** Available models on Groq:
>
> - `llama-3.1-8b-instant` — Fastest (~500+ tokens/sec, response in ~0.3s)
> - `llama-3.3-70b-versatile` — High intelligence (~250 tokens/sec, response in ~0.8s)

---

## 🚀 Step 3: Run the Backend

```bash
cd UniversityManagementSystem
./mvnw spring-boot:run
```

And run your React frontend:

```bash
cd client
npm start
```

---

## 🧪 Step 4: Test It

Open the AI Chatbot in your browser:

- Ask: `"What courses are available?"` or `"Tell me about computer science"`
- You will receive a response in **less than half a second** without any laptop lag!

---

## 🔄 Switching Back to Local Ollama Anytime

If you ever want to run completely offline without internet:
Simply set in `application.properties`:

```properties
ai.provider=ollama
```

We also optimized the local Ollama service with memory bounds (`num_ctx: 2048`, `num_predict: 350`) and updated the recommended model to `llama3.2:3b` so it won't crash your 8GB Mac.
