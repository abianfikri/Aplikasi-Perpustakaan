package com.example.perpustakaan.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perpustakaan.R;
import com.example.perpustakaan.model.PerpusModel;

import java.util.List;

public class PerpusAdapter extends RecyclerView.Adapter<PerpusAdapter.PerpusViewHolder> {

    private Context context;
    private List<PerpusModel> list;
    private Dialog dialog;

    public interface Dialog{
        void onClick(int pos);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public PerpusAdapter(Context context, List<PerpusModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PerpusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_data,parent,false);
        return new PerpusViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PerpusViewHolder holder, int position) {
        /*Untuk Menampilkan Data ke dalam Row Data
        * kemudian di tampilkan ke halaman MainActvity*/

        // Mau merubah tampilan warna dan lain-lain, ubah di layout row data
        holder.judul.setTextColor(Color.BLUE);
        holder.anggota.setTextColor(Color.BLACK);
        holder.tglPinjam.setTextColor(Color.BLACK);
        holder.tglKembali.setTextColor(Color.RED);

        holder.judul.setTextSize(20);
        holder.anggota.setTextSize(18);
        holder.tglPinjam.setTextSize(16);
        holder.tglKembali.setTextSize(16);

        holder.judul.setText(list.get(position).getJudulBuku());
        holder.anggota.setText(list.get(position).getNama());
        holder.tglPinjam.setText(list.get(position).getTglPinjam());
        holder.tglKembali.setText(list.get(position).getTglKembali());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PerpusViewHolder extends RecyclerView.ViewHolder {
        /*Deklarasi Variable sesuai yang ada di layout Row Data supaya
        * data yang ada di dalam database di tampilkan ke dalam
        * layout Row Data*/
        private CardView cardView;
        private TextView judul,anggota,tglPinjam,tglKembali;

        public PerpusViewHolder(@NonNull View itemView){
            super(itemView);

            cardView = itemView.findViewById(R.id.cardPerpus);
            judul = itemView.findViewById(R.id.textJudul);
            anggota = itemView.findViewById(R.id.textAnggota);
            tglPinjam = itemView.findViewById(R.id.textTglPinjam);
            tglKembali = itemView.findViewById(R.id.textTglKembali);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog != null){
                        dialog.onClick(getLayoutPosition());
                    }
                }
            });
        }
    }
}
