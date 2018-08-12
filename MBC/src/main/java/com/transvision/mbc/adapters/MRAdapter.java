package com.transvision.mbc.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transvision.mbc.Location;
import com.transvision.mbc.R;
import com.transvision.mbc.values.GetSetValues;

import java.util.ArrayList;

/**
 * Created by Sourav
 */

public class MRAdapter extends RecyclerView.Adapter<MRAdapter.TicketHolder> implements Filterable {
    String latitude = "", longitude = "", MRname = "", MRcode = "";
    private static int currentPosition = 0;
    private ArrayList<GetSetValues> arrayList = new ArrayList<>();
    private ArrayList<GetSetValues> filteredList;
    private Context context;
    private GetSetValues getSetValues;

    public MRAdapter(Context context, ArrayList<GetSetValues> arrayList, GetSetValues getSetValues) {
        this.arrayList = arrayList;
        this.context = context;
        this.filteredList = arrayList;
        this.getSetValues = getSetValues;

    }

    @Override
    public TicketHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item3, null);
        return new TicketHolder(view);
    }

    @Override
    public void onBindViewHolder(TicketHolder holder, final int position) {
        GetSetValues getsetvalues = arrayList.get(position);
        holder.mrcode.setText(getsetvalues.getMrcode());
        holder.mrname.setText(getsetvalues.getMrname());
        holder.mrphone.setText(getsetvalues.getMobileno());
        holder.mriemi.setText(getsetvalues.getDeviceid());
        holder.show_hide.setVisibility(View.GONE);

        if (currentPosition == position) {
            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
            holder.show_hide.setVisibility(View.VISIBLE);
            holder.show_hide.startAnimation(slideDown);
            holder.expand.setVisibility(View.GONE);
        } else {
            holder.expand.setVisibility(View.VISIBLE);
        }

        holder.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        //returning the arraylist size
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if (search.isEmpty())
                    arrayList = filteredList;
                else {
                    ArrayList<GetSetValues> filterlist = new ArrayList<>();
                    for (int i = 0; i < filteredList.size(); i++) {
                        GetSetValues getSetValues = filteredList.get(i);
                        if (getSetValues.getMrcode().contains(search)) {
                            filterlist.add(getSetValues);
                        }
                    }
                    arrayList = filterlist;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList = (ArrayList<GetSetValues>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class TicketHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mrcode, mrname, mrphone, mriemi;
        // ImageView phone, location;
        ImageView expand;
        Button call, location;
        LinearLayout show_hide;

        public TicketHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mrcode = (TextView) itemView.findViewById(R.id.txt_mr_code);
            mrname = (TextView) itemView.findViewById(R.id.txt_mr_name);
            mrphone = (TextView) itemView.findViewById(R.id.txt_phone);
            mriemi = (TextView) itemView.findViewById(R.id.txt_iemi);
            show_hide = (LinearLayout) itemView.findViewById(R.id.lin_hide);
            expand = (ImageView) itemView.findViewById(R.id.img_expand);
           /* phone = (ImageView) itemView.findViewById(R.id.img_call);
            phone.setOnClickListener(this);
            location = (ImageView) itemView.findViewById(R.id.img_location);
            location.setOnClickListener(this);*/
            call = (Button) itemView.findViewById(R.id.btn_call);
            call.setOnClickListener(this);
            location = (Button) itemView.findViewById(R.id.btn_location);
            location.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            GetSetValues getsetvalues = arrayList.get(pos);
            switch (v.getId()) {
                case R.id.btn_call:
                    String no = getsetvalues.getMobileno();
                    if (!no.equals("NA")) {
                        //code for open the dialer once the dialer icon clicked..
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", no, null));
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Contact no is not available !!", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.btn_location:
                    latitude = getsetvalues.getLatitude();
                    longitude = getsetvalues.getLongitude();
                    MRname = getsetvalues.getMrname();
                    MRcode = getsetvalues.getMrcode();
                    if (!latitude.equals("NA") || !longitude.equals("NA") || !MRname.equals("NA") || !MRcode.equals("NA") || !latitude.equals("") || !longitude.equals("")) {
                        Intent intent = new Intent(context, Location.class);
                        intent.putExtra("LAT", latitude);
                        intent.putExtra("LONG", longitude);
                        intent.putExtra("MRNAME", MRname);
                        intent.putExtra("MRcode", MRcode);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Location Not available!!!", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}
