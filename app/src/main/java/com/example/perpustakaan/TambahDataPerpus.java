package com.example.perpustakaan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TambahDataPerpus extends AppCompatActivity {

    /*Deklarasi Variable yang di butuhkan sesuai layout tambahDataPerpus*/
    private EditText nama,judul,tglPinjam,tglKembali;
    private Button simpan;
    private DatePickerDialog datePickerDialog;

    /*variable FirebaseFirestore*/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    private String id = "";

    private String date, date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data_perpus);

        nama = findViewById(R.id.edNama);
        judul = findViewById(R.id.edJudul);
        tglPinjam = findViewById(R.id.edPinjam);
        tglKembali = findViewById(R.id.edKembali);

        simpan = findViewById(R.id.btnSimpan);

        /*Buat Pop Up Loading Menyimpan Data*/
        progressDialog = new ProgressDialog(TambahDataPerpus.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan Data...");

        /*Deklarasi VAriable calender*/
        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        /*Event Clik edit text masukkan tanggan Pinjam dan menampilkan calender*/
        tglPinjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(TambahDataPerpus.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        date = day + "-" + month + "-" + year;
                        tglPinjam.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        /*Event Clik edit text masukkan tanggan Kembali dan menampilkan calender*/
        tglKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(TambahDataPerpus.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        date1 = day + "-" + month + "-" + year;
                        tglKembali.setText(date1);
                    }
                },year,month,day);
                datePickerDialog1.show();
            }
        });

        /*Event Clikc waktu button simpan di klik maka data sukses ditambahkan*/
        simpan.setOnClickListener(v->{
            if (nama.getText().length() > 0 && judul.getText().length() > 0 && tglPinjam.getText().length() > 0 &&
                    tglKembali.getText().length() > 0){

                /*Method untuk menyimpan data dengan firebasefirestore*/
                saveData(nama.getText().toString(), judul.getText().toString(), tglPinjam.getText().toString(), tglKembali.getText().toString());

            }
            else{
                /*jika data ada yang masih kosong maka akan muncul pesan*/
                Toast.makeText(this, "Data Harus di isi Semua!!!", Toast.LENGTH_SHORT).show();
            }
        });

        /*Mengambil data dari class MainActivity di teruskan ke class TambahDataPerpus
        * untuk di update data nya*/
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getStringExtra("id");
            nama.setText(intent.getStringExtra("Nama_Anggota"));
            judul.setText(intent.getStringExtra("Judul_Buku"));
            tglPinjam.setText(intent.getStringExtra("TglPinjam"));
            tglKembali.setText(intent.getStringExtra("TglKembali"));
        }
    }

    /*Method untuk create database
    * Datanya sesuai dengan urutan PerpusModel Constructor*/
    private void saveData(String NamaAnggota, String JudulBuku, String TglPinjam, String TglKembali) {
        Map<String,Object> perpustakaan = new HashMap<>();

        /*Membuat Colomn object di dalam table perpustakaan*/
        perpustakaan.put("Nama Anggota", NamaAnggota);
        perpustakaan.put("Judul Buku", JudulBuku);
        perpustakaan.put("TglPinjam", TglPinjam);
        perpustakaan.put("TglKembali", TglKembali);

        progressDialog.show();

        if (id != null) {
            /**
             *  kode untuk edit data firestore dengan mengambil id
             */
            db.collection("Perpustakaan").document(id)
                    .set(perpustakaan)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(TambahDataPerpus.this, "Berhasil di Edit", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(TambahDataPerpus.this, "Gagal di Edit", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            /**
             * Code untuk menambahkan data dengan add
             */
            db.collection("Perpustakaan")
                    .add(perpustakaan)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(TambahDataPerpus.this, "Berhasil di simpan", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TambahDataPerpus.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        }
    }

}