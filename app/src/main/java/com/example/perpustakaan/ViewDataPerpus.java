package com.example.perpustakaan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewDataPerpus extends AppCompatActivity {
    /*Deklarasi variable yang di butuhkan sesuai di layout viewDataPerpus*/
    private TextView nama,judul,tglPinjam,tglKembali;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_perpus);

        /*Inisialisasi variable sesuai dengan id yang ada di layout ViewData*/
        nama = findViewById(R.id.tvNama);
        judul = findViewById(R.id.tvJudulBuku);
        tglPinjam = findViewById(R.id.tvTglPinjam);
        tglKembali = findViewById(R.id.tvTglKembali);

        progressDialog = new ProgressDialog(ViewDataPerpus.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sedang Mengambil Data...");

        /*Mengambil data dari class MainActivity sesuai dengan getStringExtra*/
        Intent intent = getIntent();
        if (intent != null){
            nama.setText(intent.getStringExtra("Nama_Anggota"));
            judul.setText(intent.getStringExtra("Judul_Buku"));
            tglPinjam.setText(intent.getStringExtra("TglPinjam"));
            tglKembali.setText(intent.getStringExtra("TglKembali"));
        }
    }
}