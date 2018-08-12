package com.transvision.mbc.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.transvision.mbc.R;
import com.transvision.mbc.values.GetSetValues;

import java.util.ArrayList;

/**
 * Created by Sourav
 */

public class MRDetailsAdapter extends RecyclerView.Adapter<MRDetailsAdapter.DetailsHolder> {
    private ArrayList<GetSetValues> arrayList = new ArrayList<>();
    private Context context;
    private static int currentPosition = 0;
    public MRDetailsAdapter(Context context,ArrayList<GetSetValues>arrayList)
    {
        this.arrayList = arrayList;
        this.context = context;
    }
    @Override
    public MRDetailsAdapter.DetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.demo, null);
        return new DetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(MRDetailsAdapter.DetailsHolder holder, int position) {
        GetSetValues getsetvalues = arrayList.get(position);
        holder.mrcode.setText(getsetvalues.getMrcode());
        holder.download.setText(getsetvalues.getDownlod_record());
       /* if (getsetvalues.getStatus().equals("PD"))
        {
            holder.status.setTextColor(Color.parseColor("#d32f2f"));
            holder.status.setText(getsetvalues.getStatus());
        }
        else*/ holder.status.setText(getsetvalues.getStatus());
        holder.downloadtime.setText(getsetvalues.getDownload_time());
        holder.billed.setText(getsetvalues.getBilled_record());
        holder.billedtime.setText(getsetvalues.getBilled_time());
        holder.unbilled.setText(getsetvalues.getUnbilled_record());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class DetailsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mrcode,status,download,downloadtime,billed,billedtime,unbilled;
        ImageView call;
        public DetailsHolder(View itemView) {
            super(itemView);
            mrcode = (TextView) itemView.findViewById(R.id.txt_mrcode);
            download = (TextView) itemView.findViewById(R.id.txt_downloaded_record);
            status = (TextView) itemView.findViewById(R.id.txt_status);
            downloadtime = (TextView) itemView.findViewById(R.id.txt_download_time);
            billed = (TextView) itemView.findViewById(R.id.txt_billed_record);
            billedtime = (TextView) itemView.findViewById(R.id.txt_billed_time);
            unbilled = (TextView) itemView.findViewById(R.id.txt_unbilled_record);
            call = (ImageView) itemView.findViewById(R.id.img_calling);
            call.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            GetSetValues getsetvalues = arrayList.get(pos);
            switch (v.getId())
            {
                case R.id.img_calling:
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "9594563456", null));
                    context.startActivity(intent);
                    break;
            }
        }
    }

}
