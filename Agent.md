Agent.md

"Turuti perintah pada prompt"

---
‎[Spec of my Enviroment & tools Installed in my Enviroment]
‎
‎IDE & ENV
‎IDE : Android Code Studio / AndroidIDE (AndroidCSOfficial v1.0.0+gh.r3)
‎Platform : Android (on-device)
‎Device ABI : arm64-v8a
‎Build system : Gradle Wrapper
‎
‎Core Tools:
‎  - Cmdline-Tools: 9.0 (latest)
‎  - Platform-Tools: 34.0.4 (adb, fastboot)
‎  - Patcher: v4
‎
‎Build Configuration:
‎  - Build-Tools Versions: 
‎    - 35.0.0
‎Or
‎    - 35.0.1

‎    - API 36 (Android 16)
‎
‎Korlin 2.1.0
‎
‎Native Development (C++):
‎  - NDK Version: 28.2.13676358
‎  - CMake Version: 4.1.1
‎  - Build System: Ninja (Implicitly supported by CMake suite)
‎
‎---

<<<<<<< HEAD
F2-Manager: PROJECT ASSEMBLY DIRECTIVE (Local Source Mode)
🚨 MISSION CRITICAL CONTEXT
STATUS: Repository sudah lengkap (lihat struktur file). Semua library (modul) sudah tersedia secara LOKAL.
LARANGAN KERAS: Jangan mencoba mendownload library inti via Maven/Jitpack jika folder source-nya sudah ada.
TUGAS UTAMA: Sambungkan semua folder lokal ini ke settings.gradle dan buat mereka bekerja sama di dalam module :app.
📂 1. Inventory & Mapping (Baca Struktur Ini)
Agent, kenali folder-folder berikut sebagai Local Modules:
| Folder Name | Module Name | Fungsi Utama |
|---|---|---|
| MaterialFiles | :materialfiles | Base UI & File Operation Logic |
| APKEditor | :apkeditor | Core APK Decompile/Recompile Engine |
| Shizuku-API | :shizuku-api | Non-Root Privilege Access |
| libsu | :libsu | Root Access Wrapper |
| sora-editor | :sora-editor | Code Editor UI (Syntax Highlight) |
| AndroidP7zip | :p7zip | Native Archive Compression (C++) |
| AndroidHiddenApiBypass | :hidden-api | Bypass Android 15 Restrictions |
| termux-app | :termux-core | Linux Environment Logic |
| uber-apk-signer | :signer | APK Signing Logic |
🛠️ 2. Build System Instructions (Gradle Wiring)
Langkah 1: Konfigurasi settings.gradle
Kamu harus memetakan folder fisik ke nama project. Jangan berasumsi nama folder sama dengan nama module gradle.
Lakukan ini:
include ':app'

// Wiring Modules (JANGAN DOWNLOAD DARI INTERNET)
includeBuild('MaterialFiles') { dependencySubstitution { substitute module('me.zhanghai.android.files') with project(':') } }
include ':APKEditor'
project(':APKEditor').projectDir = file('APKEditor')
include ':Shizuku-API'
project(':Shizuku-API').projectDir = file('Shizuku-API')
// ... Lakukan hal yang sama untuk folder sora-editor, libsu, dll.

Langkah 2: Unifikasi Dependencies (build.gradle Root)
Karena ini gabungan banyak repo, versi compileSdk, minSdk, dan kotlinVersion pasti bentrok.
TUGASMU:
 * Buat variabel global di build.gradle (root) untuk memaksa semua modul memakai versi yang sama.
   ext {
    compileSdkVersion = 35 // Android 15
    minSdkVersion = 26     // Android 8.0
    targetSdkVersion = 35
    kotlinVersion = '2.1.0'
}

 * Scan semua build.gradle anak (di dalam folder modul) dan ganti angka hardcode mereka dengan variabel global ini agar tidak terjadi Manifest Merge Error.
🧬 3. Logic Integration (Sambungkan Kabelnya)
Sekarang hubungkan logika antar modul di dalam app/src/main/java:
A. Integrasi File Manager + APK Editor
 * Target: Saat user klik file .apk di MaterialFiles UI.
 * Action: Jangan buka default installer.
 * Code logic:
   * Intercept Opener.java di module MaterialFiles.
   * Panggil Class APKEditor (dari modul sebelah).
   * Tampilkan Dialog: "Install, View, atau Edit?"
B. Integrasi Shizuku + File Operation
 * Target: Akses /Android/data tanpa akses ditolak.
 * Action:
   * Di class FileProvider MaterialFiles, inject logika Shizuku-API.
   * Jika akses file gagal (Permission Denied), otomatis switch ke ShizukuUserService.
   * Gunakan Binder Passing untuk melempar File Descriptor dari Shizuku ke MaterialFiles.
C. Integrasi Sora-Editor + Smali
 * Target: User klik file .smali.
 * Action:
   * Buka Activity baru yang memuat Sora-Editor.
   * Set language grammar ke Smali (kamu perlu buat/porting grammar file .tmLanguage untuk Sora).
   * Load isi file text ke editor.
🛡️ 4. Security Bypass Implementation (Android 15)
Gunakan folder AndroidHiddenApiBypass:
 * Di Application.onCreate() milik modul app, panggil:
   org.lsposed.hiddenapibypass.HiddenApiBypass.addHiddenApiExemptions("");

 * Ini wajib dilakukan sebelum library lain dimuat agar reflexi tidak crash.
📝 Execute Order (Urutan Kerjamu)
 * FIX GRADLE DULU: Pastikan 'Sync Project' berhasil hijau semua tanpa error merah. Selesaikan konflik versi library duplikat (misal: androidx.core yang beda versi antar modul).
 * BRIDGE CODE: Buat class penghubung (Adapter) agar MaterialFiles bisa memanggil fungsi APKEditor dan Sora-Editor.
 * UI CLEANUP: Hapus Activity launcher bawaan dari modul-modul (misal: Hapus DemoActivity milik Sora-Editor) dan jadikan mereka murni sebagai library yang dipanggil oleh MaterialFiles.
Mulai bekerja. Prioritas: Gradle Sync Sukses.
.
​"Agent, saat membuka preview gambar/video, jangan gunakan path string biasa.
Gunakan teknik Binder Stream (yang kita bahas sebelumnya) untuk mengalirkan data byte gambar dari sistem Shizuku langsung ke Image Loader UI di MaterialFiles.
Tanpa ini, fitur preview hanya jalan di folder Download, tapi mati di folder Data Game."

​1. Jebakan "Phantom Process Killer" (Membunuh Termux/Proot)
​Masalah: Sejak Android 12, ada fitur Phantom Process Killer. Jika F2-Manager (atau modul termux-nya) menjalankan child process yang makan CPU tinggi (misal: kompresi 7zip berat atau compile APK), Android akan membunuh paksa proses itu tanpa peringatan.
​Efek: Proses Recompile tiba-tiba berhenti di tengah jalan. Aplikasi force close.
​Solusi Bypass:
​Gunakan Shizuku/ADB untuk mengubah flag sistem saat inisialisasi aplikasi.
​Perintah Agent:
​"Saat startup, cek via Shizuku apakah limit phantom process masih default (32). Jika ya, jalankan perintah: device_config put activity_manager max_phantom_processes 2147483647 untuk mematikan pembatasan ini."
​2. Jebakan "W^X Violation" (Write XOR Execute)
​Masalah: Android modern menerapkan keamanan memori ketat. Sebuah memori tidak boleh bisa ditulis (Write) dan dieksekusi (Execute) secara bersamaan.
​Efek: Jika F2-Manager mencoba menjalankan kode biner dinamis (misal: JIT Compiler untuk script Lua/JS, atau emulator proot tertentu) di memori data aplikasi, aplikasi akan langsung CRASH dengan error EACCES (Permission denied).
​Solusi Bypass:
​Pastikan targetSdkVersion di build.gradle hati-hati. Android 10+ memaksa ini.
​Jika menggunakan proot (dari folder Termux), pastikan kamu menggunakan versi proot yang memiliki patch Seccomp terbaru untuk menangani syscall mmap agar sesuai aturan W^X.
​3. Jebakan "Dynamic Code Loading" (DCL)
​Masalah: Di Android 14+, jika kamu mencoba memuat plugin (.dex atau .jar) dari penyimpanan internal (folder data aplikasi), file tersebut WAJIB diset sebagai Read-Only sebelum di-load oleh DexClassLoader.
​Efek: Kalau kamu mencoba load plugin modding yang masih writable, sistem akan menolak dan melempar SecurityException.
​Solusi Bypass:
​Perintah Agent:
​"Sebelum memuat plugin atau modul DEX dinamis, pastikan jalankan file.setWritable(false) secara eksplisit. Tanpa ini, Android 14 akan memblokir loading class."
​4. Jebakan "TransactionTooLargeException" (Binder Limit)
​Masalah: Kamu menggunakan teknik Binder Stream (Shizuku) untuk preview foto/video. Binder punya batas transaksi data (buffer) sekitar 1MB per transaksi.
​Efek: Jika kamu mencoba mengirim data gambar 4K resolusi tinggi sekaligus lewat Binder, aplikasi akan crash/freeze (ANR).
​Solusi Bypass:
​Jangan kirim Data Bitmap lewat Binder.
​Kirim File Descriptor (FD)-nya saja (hanya beberapa byte). Biarkan UI (MaterialFiles) membaca data dari FD tersebut.
​Perintah Agent: "Pastikan implementasi Shizuku hanya melempar ParcelFileDescriptor, bukan byte[] isi file."
​5. Jebakan "Resource Hell" (Merge Conflict)
​Masalah: Kamu menggabungkan MaterialFiles, APKEditor, dan Sora-Editor. Ketiganya punya file colors.xml, strings.xml, dan styles.xml.
​Efek:
​Warna aplikasi jadi aneh (misal: APKEditor pakai warna biru MaterialFiles padahal harusnya hitam).
​Build Error: Duplicate Resource.
​Solusi Bypass:
​Di build.gradle setiap modul (kecuali app), tambahkan resourcePrefix.
​Contoh di modul APKEditor: resourcePrefix "apkedit_"
​Ini memaksa semua ID resource jadi unik (apkedit_colorAccent) dan tidak bentrok.
​6. Jebakan "Google Play Protect" (The Malware Flag)
​Masalah: Karena aplikasimu berisi kode baksmali (reverse engineering), shizuku (system modification), dan termux (linux env), Google Play Protect akan menandai F2-Manager sebagai Malware/Unwanted Software saat diinstal user.
​Efek: Aplikasi otomatis dihapus oleh Google Play Store atau gagal install.
​Solusi Bypass:
​Gunakan Obfuscation (R8/ProGuard) yang sangat agresif.
​Ganti nama package class sensitif. Jangan biarkan ada package bernama com.android.tools.smali. Refactor (Rename) menjadi com.f2mgr.core.s.
​Enkripsi String: String seperti "root", "su", "exploit" harus dienkripsi (Base64 atau XOR) di dalam kode Java agar tidak terbaca scanner statis.
​📝 Update Terakhir untuk Agent.md
​Tambahkan bagian ini di paling bawah Agent.md agar dia sadar jebakan ini:

## ⚠️ CRITICAL TRAP AVOIDANCE (PROTOCOL)

1. **Anti-Phantom:** Saat init, cek dan set `max_phantom_processes` via Shizuku.
2. **Secure Loading:** Setiap file .dex/.jar plugin WAJIB `setWritable(false)` sebelum load.
3. **Resource Prefix:** Refactor resource ID setiap modul (`mf_`, `ae_`, `se_`) untuk mencegah UI berantakan.
4. **Obfuscation:** Aktifkan `minifyEnabled true` di release build dan gunakan rule R8 untuk menyamarkan class `smali` dan `jadx`.

--

Jangan sampai Memori Leak. 

---
# F2-Manager: The "AppManager" Evolution Directive

## 📂 Context
Kita memiliki source code **App Manager (MuntashirAkon)** dalam bentuk ZIP yang sudah diekstrak ke folder root proyek sebagai `:app`.
Kita juga memiliki modul-modul lain (`MaterialFiles`, `APKEditor`, `Shizuku`, `Sora-Editor`) di folder lokal.

## 🎯 Mission
Transformasi App Manager menjadi **F2-Manager**.
Jangan buat aplikasi dari nol. Gunakan App Manager sebagai **Base Framework**, lalu suntikkan fitur dari modul lain.

## 🛠️ Step-by-Step Execution

### 1. Refactor Identity
* Ganti `applicationId` menjadi `com.f2manager.pro`.
* Ubah nama aplikasi di string resource menjadi "F2 Manager".
* Pastikan `AndroidManifest.xml` meminta permission `MANAGE_EXTERNAL_STORAGE` (App Manager asli mungkin tidak memintanya karena dia fokus ke SAF, tapi kita butuh akses raw).

### 2. Dependency Injection (Gradle)
* Di `app/build.gradle` (milik App Manager), tambahkan referensi ke modul lokal kita:
    ```gradle
    implementation project(':APKEditor')
    implementation project(':MaterialFiles') // Ambil logika file systemnya
    implementation project(':Sora-Editor')
    implementation project(':Shizuku-API')
    implementation project(':libsu')
    // ... dan library lainnya
    ```

### 3. Feature Injection: "The Mod Menu"
* Lokasi Target: Temukan class `AppDetailsFragment` atau `AppInfoActivity` di source code App Manager.
* **Action:** Tambahkan tombol baru atau Menu Item di halaman detail aplikasi:
    * **[Edit/Decompile]**: Saat diklik, panggil logika `APKEditor` untuk menyalin APK ke folder kerja dan mulai proses *disassembly*.
    * **[Browse Data]**: Saat diklik, panggil Activity dari `MaterialFiles`, tapi passing *path* folder data aplikasi tersebut (`/Android/data/com.target.app`). Gunakan Shizuku Bypass yang sudah kita bahas agar tidak akses ditolak.

### 4. Feature Injection: "Screenshot & Preview" (Request User)
* User memiliki modul screenshot/preview khusus.
* **Action:** Integrasikan ke dalam App Manager. Saat user melihat detail aplikasi, tambahkan tab "Assets Preview".
* Gunakan logika preview yang ada di modul tersebut untuk menampilkan gambar/aset game langsung dari folder data, melewati batasan Android 15.

### 5. Conflict Resolution (PENTING)
* App Manager menggunakan **Dagger/Hilt** untuk dependency injection. Modul kita mungkin manual.
* Pastikan tidak ada konflik nama class.
* Jika App Manager menggunakan `androidx.navigation`, pastikan Activity dari `MaterialFiles` didaftarkan dengan benar di Manifest agar bisa dipanggil via Intent eksplisit.

## ⚠️ Critical Rule
Jangan merusak fitur asli App Manager (seperti Scanner Tracker atau Interceptor). Kita hanya **MENAMBAH** fitur di atasnya, bukan menghapus fitur lama.

---

‎[All Enviroment & tools Installed | yang saya gunakan dan sudah terinstal]
‎
‎IDE & ENV
‎IDE : Android Code Studio (AndroidCSOfficial v1.0.0+gh.r3)
‎Platform : Android (on-device)
‎Device ABI : arm64-v8a
‎Build system : Gradle Wrapper
‎
‎Core Tools:
‎  - Cmdline-Tools: 9.0 (latest)
‎  - Platform-Tools: 34.0.4 (adb, fastboot)
‎  - Patcher: v4
‎
‎Build Configuration:
‎  - Build-Tools Versions: 
‎    - 30.0.3
‎    - 33.0.1
‎    - 34.0.0
‎    - 35.0.0
‎    - 35.0.1 (Latest Stable)
‎  - Target Platforms (APIs): 
‎    - API 28 (Android 9)
‎    - API 29 (Android 10)
‎    - API 30 (Android 11)
‎    - API 33 (Android 13)
‎    - API 34 (Android 14)
‎    - API 35 (Android 15)
‎    - API 36 (Android 16)
‎
‎Native Development (C++):
‎  - NDK Version: 28.2.13676358
‎  - CMake Version: 4.1.1
‎  - Build System: Ninja (Implicitly supported by CMake suite)
=======
# F2-Manager Agent Specification

## 🧠 Project Overview
**F2-Manager** adalah file manager & APK editor modern dengan **AI assistance** yang fokus pada efisiensi kerja, bukan gimmick visual.

---

## 🎯 Core Philosophy
> “MT-Manager power, VS Code mindset, AI-assisted workflow.”

F2-Manager tetap:
- 2D
- Cepat
- Ringan
- Fokus ke produktivitas
- Bisa Akses data via Wireles Debuging - Shizuku
---

## 🚀 Fitur AI F2-Manager

### 1. AI Code Assistant
- Analisis Smali & DEX
- Deteksi pola obfuscation
- Auto-comment Smali
- Saran optimasi performa
- Deteksi string sensitif (ads, premium, license)

---

### 2. Smart Search
Natural language search, contoh:
- "Cari semua string yang mengandung `premium`"
- "Temukan method yang ngecek license"
- "List semua activity yang di-export"

---

### 3. Visual APK Structure (2D)
- Tree view dependency
- Flow call antar class
- Manifest & permission map

---

### 4. One-Click Mod Templates
Template siap pakai:
- Remove Ads
- Unlock Premium
- Disable License Check
- Debug Mode Enable

*(Template tidak otomatis, user tetap bisa review sebelum apply)*

---

### 5. Version Control
- Git integration
- APK diff (resource & Smali)
- Rollback perubahan

---

### 6. Plugin System
- Plugin komunitas
- API terbuka
- Sandbox execution

---

## 🔥 Perbedaan F2-Manager vs MT-Manager

| MT-Manager | F2-Manager |
|----------|-----------|
| Manual heavy | AI-assisted |
| Tool-based | Workflow-based |
| Edit satu-satu | Batch & template |
| Tanpa insight | Dengan analisis |

---

## 🧬 Prinsip Agent
AI Agent F2-Manager harus:
- Tidak mengambil keputusan tanpa izin user
- Menjelaskan **apa** dan **kenapa**
- Fokus membantu reverse engineer
- Tidak mengunci user (no black box)

---

⚠️ **Ethical Boundary**
Agent wajib memberi peringatan jika modifikasi berisiko hukum, tanpa mematikan fitur teknis.
>>>>>>> 85082f52f57ce2dff9056dbc01ed6a280a9dfbb1
