# AmoAI Chatbot

AmoAI Chatbot (v1) sebuah aplikasi Android berbasis Java yang mengintegrasikan API dari GEMINI untuk generate text dan image regonation. Proyek ini menyimpan riwayat jawaban secara lokal menggunakan Room (DAO), dengan memanfaatkan SQLite yang sudah ada di Android.

## Fitur

- **Generate Text**  
- **Image Recognition**  
- **History**  
- **Local Database**  

## Struktur Proyek

- **Activities**
  - `SplashActivity`: Menampilkan splash screen dengan animasi logo AmoAI.
  - `MainActivity`: Halaman utama yang menampilkan fitur melalui tab (Generate Text, Image Recognition, History).

- **Fragments**
  - `FragmentGenerateText`: Menangani fitur generate text.
  - `FragmentImageRecognition`: Menangani fitur pengenalan gambar.
  - `FragmentHistory`: Menampilkan riwayat jawaban dengan fitur expand/collapse dan delete.

- **Adapters**
  - `HistoryAdapter`: Adapter untuk menampilkan data history pada RecyclerView.
  - `ViewPagerAdapter`: Adapter untuk mengatur fragment pada ViewPager.

- **Database**
  - `History`: Entity untuk menyimpan data riwayat.
  - `HistoryDao`: Interface DAO untuk operasi CRUD pada tabel history.
  - `AppDatabase`: Kelas database Room untuk menginisialisasi dan mengakses database.

- **Utilities**
  - `CustomLoadingDialog`: Dialog kustom untuk menampilkan loading/progress.

- **Resources**
  - Layout XML, drawable assets, dan file konfigurasi seperti colors.xml, styles.xml, dll.

## Requirement  

Sebelum menginstal, pastikan lingkungan pengembangan Anda memenuhi persyaratan berikut:

- **Android Studio:** versi Jellyfish atau yang lebih baru.
- **Gradle:** Gunakan Gradle Plugin versi 4.2+ dan Gradle Wrapper yang sesuai (misalnya, Gradle 6.7+).
- **Java:** JDK 11 atau versi yang kompatibel.
- **SDK Android:** Pastikan SDK untuk API level 24 (Android 7.0) atau lebih tinggi sudah terinstall.
- **Internet Connection:** Untuk mengunduh dependensi dan library saat pertama kali membangun proyek.
- **Dependencies:** Pastikan semua dependensi (AndroidX, Material Components, Room, Guava, Dexter) ter-update seperti yang didefinisikan di file `build.gradle`.

## Instalasi & Setup

1. **Clone Repository**

   ```bash
   git clone https://github.com/RayanKhairullah/AmoAI.git
2. **Buka Proyek di Android Studio**
    - Buka Android Studio.
    - Pilih folder proyek yang telah di-clone.
    - Tunggu Build Gradle sampai selesai

3. **Konfigurasi API Key**  
    - Buka [google studio](https://aistudio.google.com/app/apikey) jangan lupa login terlebih dahulu
    - Pilih Create API Key, setelah dicreate Copy API Key, contoh: AIxxxxnJ26exxxxxxx57a9MGJE
    - Buka kembali proyek AmoAI, Ganti nilai `YOUR_API_KEY` pada file FragmentGenerateText.java dan FragmentImageRecognition.java dengan API key yang dicopy

4. **Build dan Jalankan Aplikasi**  
    - Pastikan Build Gradle tidak ada yang error/gagal
    - Hubungkan perangkat Android atau gunakan emulator.
    - Klik Run untuk menjalankan aplikasi.

## Message
    AmoAI masih dalam tahap pengembangan v1, jadi ada kemungkinan munculnya bug pada fitur.
    
    Semoga Membantu
    -rayan