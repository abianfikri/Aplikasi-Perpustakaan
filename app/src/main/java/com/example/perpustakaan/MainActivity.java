package com.example.perpustakaan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.perpustakaan.adapter.PerpusAdapter;
import com.example.perpustakaan.model.PerpusModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /*Deklarasi Variable Berdasarkan isi yang ada di layout main*/
    private RecyclerView recyclerView;
    private FloatingActionButton TambahData;

    /*Penggunaan FirebaseFirestore*/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    private List<PerpusModel> list = new ArrayList<>();
    private PerpusAdapter perpusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Inisialisasi Variable Sesuai dengan Id yang ada di Layout*/
        recyclerView = findViewById(R.id.recylerlistData);
        TambahData = findViewById(R.id.btnAddFloating);

        /*Membuat Pop Loading*/
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mengambil data...");
        perpusAdapter = new PerpusAdapter(getApplicationContext(), list);

        /*Waktu Floaating Button di klik maka akan pindah ke class tambahData*/
        TambahData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TambahDataPerpus.class);
                startActivity(intent);
            }
        });

        perpusAdapter.setDialog(new PerpusAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                /*Membuat Pilihan View,Delete,Update*/
                final CharSequence[] dialogItem = {"View Data", "Update Data", "Delete Data"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /*Pilihan Sesuai Item yang akan di pilih*/
                        switch (i){
                            /*Pilihan View Data*/
                            case 0:
                                /*Pindah ke halaman class ViewDataPerpus Layout*/
                                Intent view = new Intent(getApplicationContext(),ViewDataPerpus.class);
                                view.putExtra("id",list.get(pos).getId());
                                view.putExtra("Nama_Anggota", list.get(pos).getNama());
                                view.putExtra("Judul_Buku", list.get(pos).getJudulBuku());
                                view.putExtra("TglPinjam", list.get(pos).getTglPinjam());
                                view.putExtra("TglKembali", list.get(pos).getTglKembali());
                                startActivity(view);
                                break;
                            /*Pilihan Update Data akan pindah ke halaman TambahDataPerpus untuk di Edit*/
                            case 1:
                                /*Pindah ke halaman class TambahDataPerpus*/
                                Intent Update = new Intent(getApplicationContext(), TambahDataPerpus.class);
                                Update.putExtra("id",list.get(pos).getId());
                                Update.putExtra("Nama_Anggota", list.get(pos).getNama());
                                Update.putExtra("Judul_Buku", list.get(pos).getJudulBuku());
                                Update.putExtra("TglPinjam",list.get(pos).getTglPinjam());
                                Update.putExtra("TglKembali", list.get(pos).getTglKembali());
                                startActivity(Update);
                                break;
                            /*Pilihan Untuk Delete Data*/
                            case 2:
                                deleteData(list.get(pos).getId());
                                break;

                        }
                    }
                });
                dialog.show();
            }
        });

        /*Menampilkan data di recycler view*/
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(perpusAdapter);
    }

    /*Method untuk delete data dengan mengambil id*/
    private void deleteData(String id) {
        progressDialog.show();

        db.collection("Perpustakaan").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Data Gagal di Hapus", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Data Berhasil di Hapus", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.show();
                        getData();
                    }
                });
    }

    /*Membuat data tampil di halaman MainActivity*/
    @Override
    protected void onStart(){
        super.onStart();
        getData();
    }

    /*Mengambil database dari firebasefirestore*/
    private void getData() {
        progressDialog.show();

        /*Query untuk Mengambil data di table Perpustakaan*/
        db.collection("Perpustakaan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();

                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                /*Mengambil data yang dibutuhkan sesuai yang ada di dalam PerpusModel
                                * dan FirebaseFirestore*/
                                PerpusModel perpusModel = new PerpusModel(document.getString("Nama Anggota"),document.getString("Judul Buku"),
                                       document.getString("TglPinjam").toString(),document.getString("TglKembali").toString());
                                perpusModel.setId(document.getId());
                                list.add(perpusModel);
                            }
                            perpusAdapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Data Gagal di Ambil", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
}