# Ollama Setup Guide - Completely Free AI Solution

This guide will help you set up Ollama to run AI models locally on your machine at **zero cost**.

## 🎯 What is Ollama?

Ollama is a free, open-source tool that lets you run large language models (LLMs) locally on your computer. This means:
- ✅ **100% Free** - No API costs, no subscriptions
- ✅ **Privacy** - All data stays on your machine
- ✅ **No Internet Required** - Works offline after model download
- ✅ **Multiple Models** - Llama, Mistral, and more

---

## 📥 Step 1: Install Ollama

### For macOS (Your System)

1. Download Ollama from: https://ollama.com/download
2. Open the downloaded `.dmg` file
3. Drag Ollama to your Applications folder
4. Launch Ollama from Applications

### For Windows
1. Download from: https://ollama.com/download
2. Run the installer
3. Follow the installation wizard

### For Linux
```bash
curl -fsSL https://ollama.com/install.sh | sh
```

---

## 🤖 Step 2: Download AI Model

After installing Ollama, open your terminal and run:

```bash
ollama pull llama2
```

This will download the Llama 2 model (about 4GB). This is a powerful, free model that works great for most tasks.

**Alternative Models (Choose one):**
- `ollama pull mistral` - Faster, smaller model (~4GB)
- `ollama pull llama3` - Latest Llama model (~4GB)
- `ollama pull phi` - Very small model (~2GB)

**Recommendation:** Use `llama2` or `mistral` for best results.

---

## 🚀 Step 3: Start Ollama Server

Ollama automatically starts when you launch the application. To verify it's running:

```bash
curl http://localhost:11434/api/tags
```

You should see a JSON response with available models.

---

## ⚙️ Step 4: Configure Your Application

Your `application.properties` is already configured:

```properties
ollama.base-url=http://localhost:11434
ollama.model=llama2
```

If you downloaded a different model (e.g., mistral), update the model name:

```properties
ollama.model=mistral
```

---

## 🧪 Step 5: Test Ollama

Test Ollama directly from terminal:

```bash
ollama run llama2 "Hello, how are you?"
```

You should get a response from the AI.

---

## 🏃 Step 6: Run Your Application

### Backend (Spring Boot)
```bash
cd UniversityManagementSystem
./mvnw spring-boot:run
```

### Frontend (React)
```bash
cd client
npm start
```

---

## 🔍 Troubleshooting

### Issue: "Connection refused" error
**Solution:** Make sure Ollama is running. Check the Ollama application is open.

### Issue: Slow responses
**Solution:** This is normal for local AI. Llama 2 takes 2-5 seconds per response on average computers.

### Issue: Out of memory
**Solution:** Use a smaller model:
```bash
ollama pull phi
```
Then update `application.properties`:
```properties
ollama.model=phi
```

### Issue: Ollama not starting
**Solution:**
- macOS: Check Applications folder, launch Ollama
- Check if port 11434 is already in use: `lsof -i :11434`

---

## 💡 Tips for Best Performance

1. **Use a Fast Computer**: Local AI works best with:
   - 8GB+ RAM
   - SSD storage
   - Multi-core processor

2. **Choose the Right Model**:
   - **llama2** - Best quality, slower
   - **mistral** - Good balance of speed/quality
   - **phi** - Fastest, lower quality

3. **Keep Ollama Running**: Responses are faster when Ollama is already running

---

## 📊 Model Comparison

| Model | Size | Speed | Quality | Recommended For |
|-------|------|-------|---------|-----------------|
| llama2 | 4GB | Medium | High | General use, best quality |
| mistral | 4GB | Fast | High | Fast responses, good quality |
| phi | 2GB | Very Fast | Medium | Low-end computers |
| llama3 | 4GB | Medium | Very High | Latest model, best results |

---

## 🎉 You're All Set!

Your University Management System now uses **completely free AI** powered by Ollama. No API keys, no costs, no limits!

### What You Can Do:
- ✅ AI Chatbot - Answer student queries
- ✅ Performance Prediction - ML-based analysis
- ✅ Exam Generator - Create questions
- ✅ Course Recommendations - Personalized suggestions

All running locally on your machine at **zero cost**!

---

## 🆘 Need Help?

- Ollama Documentation: https://github.com/ollama/ollama
- Model Library: https://ollama.com/library
- Issues: Check terminal logs for error messages
