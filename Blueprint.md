package com.org.comeback

## **📌 Project Name: Comeback**  
### **🛠️ Tech Stack:**  
- **Language:** Kotlin  
- **Framework:** Jetpack Compose / XML (choose one)  
- **State Management:** ViewModel  
- **API:** Multilingual Model API (Google Translate API / Gemini API)  
- **Database (Optional):** Room / SharedPreferences (to store user preferences)  
- **UI Components:** Material Design 3  
- **Animations:** Lottie (for smooth UI effects)  

---

## **🗺️ App Structure & Features**  

### **1️⃣ Splash Screen (Fun Entry Point)**
- Show app logo with a cool animation (Lottie or simple fade-in).  
- Auto-redirect to the home screen after 2-3 seconds.  

### **2️⃣ Home Screen (Main UI)**
- Display an **"Install" button** (Styled like Play Store).  
- When clicked, show a **loading animation** and then a **random funny message.**  
- The message will be fetched dynamically (Multilingual API).  
- Options:
  - **Share Button** (to share messages via WhatsApp, Instagram, etc.).  
  - **Change Language Button** (to manually switch languages).  
  - **Settings Button** (for theme customization).  

### **3️⃣ Custom Message Input Screen**
- User can enter their name or phrase.  
- App will generate a **personalized funny message** based on input.  
- Show a "Try Again" button for multiple tries.  

### **4️⃣ Sound Effects & Vibrations**
- Short vibration when clicking "Install."  
- Funny sound when showing a rejection message.  
- Use `MediaPlayer` for sound effects.  

### **5️⃣ Dark Mode & Theme Customization**
- Toggle switch to enable/disable dark mode.  
- Allow users to select a theme color from predefined options.  

### **6️⃣ Multilingual Support**
- Auto-detect device language and display messages accordingly.  
- Allow manual language selection for extra fun.  
- Use **Google Translate API / Gemini API** for real-time translation.  



detailed breakdown of each screen in your **Fun Compatibility Check App** 🎉:  

---

## **1️⃣ Splash Screen** (🔥 Entry Point)  
### **🎯 Purpose:**  
- Give a **fun first impression** with an animated logo or effect.  
- Briefly introduce users to the app.  

### **🖌️ UI Elements:**  
✅ Full-screen background (gradient or animated).  
✅ App logo (can use Lottie animation).  
✅ Funny welcome text like:  
   - *"Let's see if you can install this app 😆"*  
✅ Auto-transition to **Home Screen** after 2-3 seconds.  

### **🎬 Features & Effects:**  
✔️ **Animation:** Use Lottie for logo reveal or fade-in.  
✔️ **Auto Redirect:** Use `Handler().postDelayed()` to switch screens.  

---

## **2️⃣ Home Screen** (👑 Main UI)  
### **🎯 Purpose:**  
- The core screen where users interact.  
- Show a fake **Install button** that leads to a rejection message.  

### **🖌️ UI Elements:**  
✅ **Title Text:** *"Fun Compatibility Check!"* (Bold & centered).  
✅ **Play Store-style Install Button:**  
   - Styled like a real Play Store button.  
   - Clicking it triggers a **fun rejection message**.  
✅ **Message Box:**  
   - Displays random funny rejection messages.  
   - Example: *"Oops! You're too smart for this app. Try later! 🤣"*  
✅ **3 Action Buttons:**  
   - **🔁 Try Again** → Regenerates a new funny message.  
   - **🌍 Change Language** → Opens a language selector.  
   - **🎨 Change Theme** → Opens theme customization.  
✅ **Settings Button:** (⚙️ In the top-right corner).  

### **🎬 Features & Effects:**  
✔️ **Click Animation:** Install button should shake when clicked.  
✔️ **Vibration Effect:** Short vibration when clicking install.  
✔️ **Lottie Animation:** Add a fun rejection effect (like an error pop-up).  

---

## **3️⃣ Custom Message Input Screen** (📝 Personalized Fun)  
### **🎯 Purpose:**  
- Let users **input their name** and get a **funny message** about them.  

### **🖌️ UI Elements:**  
✅ **EditText (Input Field):**  
   - User enters their name.  
✅ **Submit Button:**  
   - On press, the app generates a **funny personalized message**.  
✅ **Generated Message Display:**  
   - Example: *"Hey, Abhishek! You are 99.99% fun-compatible with this app! But still... NO INSTALL FOR YOU! 🤪"*  
✅ **Try Again Button:**  
   - Allows users to enter a different name.  
✅ **Back Button:**  
   - Returns to **Home Screen**.  

### **🎬 Features & Effects:**  
✔️ **Random Messages:** Different responses every time.  
✔️ **Sound Effect:** A funny "Denied!" sound on rejection.  
✔️ **Emoji Animations:** Shake or bounce effect when showing results.  

---

## **4️⃣ Language Selection Screen** (🌍 Multilingual Support)  
### **🎯 Purpose:**  
- Let users **select their preferred language** for fun messages.  

### **🖌️ UI Elements:**  
✅ **Dropdown List / Radio Buttons**  
   - Show a list of supported languages.  
✅ **Preview Text in Selected Language**  
   - Example: *"Oops! You failed the compatibility test!"* displayed in Hindi, French, etc.  
✅ **Save Button:**  
   - Saves the selected language.  

### **🎬 Features & Effects:**  
✔️ **API Integration:** Translate messages dynamically using Google Translate API.  
✔️ **Language Auto-Detect:** Fetch device language as the default.  

---

## **5️⃣ Theme Customization Screen** (🎨 Personalization)  
### **🎯 Purpose:**  
- Allow users to **switch between light/dark mode** and **choose colors**.  

### **🖌️ UI Elements:**  
✅ **Dark Mode Toggle** (🌙🌞 Switch between dark and light theme).  
✅ **Color Picker** (🎨 Choose from predefined colors for UI).  
✅ **Preview Section** (📱 Show how changes will look).  
✅ **Save & Apply Button**  

### **🎬 Features & Effects:**  
✔️ **Smooth Transition:** Animate theme change.  
✔️ **Save Preferences:** Store settings using `SharedPreferences`.  

---

## **6️⃣ Fun Share Screen** (📤 Social Sharing)  
### **🎯 Purpose:**  
- Let users **share funny messages** with friends.  

### **🖌️ UI Elements:**  
✅ **Preview Box** (Shows the message in a fun format).  
✅ **"📸 Take Screenshot" Button** (Saves message as an image).  
✅ **"🔗 Share" Button** (Share via WhatsApp, Instagram, etc.).  

### **🎬 Features & Effects:**  
✔️ **One-Click Sharing:** Uses `Intent.ACTION_SEND`.  
✔️ **Custom Image Layout:** Messages appear in a fun card design.  

---


# API
API: AIzaSyBZ-_k9fpfbWh8hXNTelq-Onw46L6U42T4

curl "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=GEMINI_API_KEY" \
-H 'Content-Type: application/json' \
-X POST \
-d '{
  "contents": [{
    "parts":[{"text": "Explain how AI works"}]
    }]
   }'