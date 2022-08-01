package com.ecotton.impex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecotton.impex.R;
import com.ecotton.impex.activities.MyContractDetailsActivity;
import com.ecotton.impex.models.ContractDetailModel;

import java.util.List;

public class DebitNoteImageAdapter extends RecyclerView.Adapter<DebitNoteImageAdapter.ViewHolder> {
    private Context context;
    private List<ContractDetailModel.DebitNote> debitNoteList;
    private OnItemClickListener onItemClickListener;

    public DebitNoteImageAdapter(MyContractDetailsActivity mcontext, List<ContractDetailModel.DebitNote> debit_note_array) {
        this.context = mcontext;
        this.debitNoteList = debit_note_array;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener myClickListener) {
        this.onItemClickListener = myClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        Button btn_upload_debit_note;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_debit_note);
            // btn_upload_debit_note = (Button) itemView.findViewById(R.id.btn_upload_debit_note);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.debit_note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(debitNoteList.get(position).getFile_name()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return debitNoteList.size();
    }
}
