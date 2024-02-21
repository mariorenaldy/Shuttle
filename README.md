Shuttle Bus Booking Application

Shuttle adalah perusahaan yang bergerak dibidang transportasi darat yang ingin membuat sebuah aplikasi mobile untuk mempermudah para pelanggannya memesan tempat duduk pada kendaraan yang mereka sediakan. 
Perusahaan tersebut menginginkan agar pelanggannya dapat dengan mudah melihat jalur atau rute yang disediakan, memilih tempat duduk dengan kapasitas tertentu, dan melihat history pemesanan. Maka dari itu, dibangunlah sebuah aplikasi yang dapat memenuhi spesifikasi yang telah dipaparkan serta beberapa fitur tambahan yang akan lebih mengakomodasi pelanggan untuk memesan layanan dari Shuttle tersebut

Fitur Dasar:
- Melakukan login ke sebuah akun.
- Melihat dan memilih jalur/rute, tanggal, waktu, dan ukuran kendaraan yang tersedia.
- Melihat dan memilih tempat duduk yang tersedia.
- Melakukan pemesanan tempat duduk sesuai data-data yang dimasukkan sebelumnya.
- Melihat history pemesanan.
- Mengambil seluruh data yang diperlukan dari Web Service PPPB Travel API.
- Membangun aplikasi dengan arsitektur MVP.
- Menggunakan third party library.

Fitur Tambahan:
- Mengimplementasi NavigationUI dan Fragment (dengan animasi transisi).
- Pengubahan ukuran kendaraan/tempat duduk dengan sensor gerak (shake).
- Menyimpan token pada cache.
- Mengimplementasi dialog untuk login gagal dan pemesanan berhasil.
- Mengimplementasi Date dan Time Picker.

Third Party Libraries:

Volley (Web Service): Mempermudah koneksi dengan web service.
https://developer.android.com/training/volley

Gson (JSON): Mempermudah penggunaan JSON.
https://github.com/google/gson

MaterialDateTimePicker (UI): Mempermudah penggunaan date dan time picker serta fitur untuk mengubah warna, desain, dan batasan.
https://github.com/wdullaer/MaterialDateTimePicker

SweetAlert (UI): Mempermudah penggunaan dan pengubahan desain dialog.
https://github.com/pedant/sweet-alert-dialog

Sensey (Logic): Mempermudah penggunaan sensor event/gesture.
https://github.com/nisrulz/sensey
