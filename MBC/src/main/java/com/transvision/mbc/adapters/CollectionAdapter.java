package com.transvision.mbc.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transvision.mbc.CollectionLocation;
import com.transvision.mbc.R;
import com.transvision.mbc.values.GetSetValues;

import java.util.ArrayList;
/**
 * Created by Sourav
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.TicketHolder> {
    private ArrayList<GetSetValues> arrayList = new ArrayList<>();
    private ArrayList<GetSetValues> filteredlist;
    private GetSetValues getSetValues;
    private Context context;
    private static int currentPosition = 0;
    String latitude = "", longitude = "", MRname = "";

    public CollectionAdapter(Context context, ArrayList<GetSetValues> arrayList, GetSetValues getSetValues) {
        this.arrayList = arrayList;
        this.filteredlist = arrayList;
        this.context = context;
        this.getSetValues = getSetValues;
    }

    @Override
    public CollectionAdapter.TicketHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item2, null);
        return new TicketHolder(view);
    }

    @Override
    public void onBindViewHolder(final TicketHolder holder, final int position) {
        GetSetValues getsetvalues = arrayList.get(position);
        holder.counter.setText(getsetvalues.getCounter());
         holder.date.setText(getsetvalues.getDate());
        if (getsetvalues.getApproved_flag().equals("Approved")) {
            holder.submit.setTextColor(Color.parseColor("#4CAF50"));
            holder.submit.setText(getsetvalues.getApproved_flag());
        } else {
            holder.submit.setTextColor(Color.RED);
            holder.submit.setText(getsetvalues.getApproved_flag());
        }

        holder.count.setText(getsetvalues.getReceiptcount());
        holder.amount.setText(getsetvalues.getReceipt_amount());
        holder.mode.setText(getsetvalues.getMode());
        //Getting current position here means in which down arrow button user will click that will be current position
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
        return arrayList.size();
    }

    public Filter getFilter()
    {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if (search.isEmpty())
                {
                    arrayList = filteredlist;
                }
                else
                {
                    ArrayList<GetSetValues> filterlist = new ArrayList<>();
                    for (int i=0; i<filteredlist.size(); i++)
                    {
                        GetSetValues getSetValues = filteredlist.get(i);
                        if (getSetValues.getCounter().contains(search))
                        {
                            filterlist.add(getSetValues);
                        }
                    }
                    arrayList = filterlist;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return  filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList = (ArrayList<GetSetValues>)results.values;
                notifyDataSetChanged();
            }
        };
    }
    public class TicketHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView counter, date, submit, count, amount, mode;
        LinearLayout show_hide;
        ImageView expand;
        Button call, location;

        public TicketHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            counter = (TextView) itemView.findViewById(R.id.txt_counter);
            date = (TextView) itemView.findViewById(R.id.txt_date);
            submit = (TextView) itemView.findViewById(R.id.txt_submit);
            count = (TextView) itemView.findViewById(R.id.txt_receiptcount);
            amount = (TextView) itemView.findViewById(R.id.txt_amount);
            mode = (TextView) itemView.findViewById(R.id.txt_mode);
            show_hide = (LinearLayout) itemView.findViewById(R.id.lin_hide);
            expand = (ImageView) itemView.findViewById(R.id.img_expand);
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
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", no, null));
                    context.startActivity(intent);
                    break;

                case R.id.btn_location:
                    latitude = getsetvalues.getLatitude();
                    longitude = getsetvalues.getLongitude();
                    MRname = getsetvalues.getMrname();

                    if (!latitude.equals("NA") || !longitude.equals("NA") || !MRname.equals("NA")) {
                        Intent intent1 = new Intent(context, CollectionLocation.class);
                        intent1.putExtra("LAT", latitude);
                        intent1.putExtra("LONG", longitude);
                        intent1.putExtra("MRNAME", MRname);
                        context.startActivity(intent1);
                    } else {
                        Toast.makeText(context, "Location Not available!!!", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}
