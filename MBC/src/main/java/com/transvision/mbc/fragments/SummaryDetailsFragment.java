package com.transvision.mbc.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transvision.mbc.R;

/**
 * Created by Sourav
 */
public class SummaryDetailsFragment extends Fragment {

    TextView subdivision,totalvalidfile,totaldownload,notdownload,upload,notupload;
    String division,valid,download,downloadnot,up,uploadnot,from_date,to_date;
    TextView from, to;
    public SummaryDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_summary_details, container, false);

        Bundle bundle = getArguments();
        if (bundle!= null)
        {
            division = bundle.getString("subdivcode");
            download = bundle.getString("DWNRECORD");
            up = bundle.getString("UPLOADRECORD");
            valid = bundle.getString("INFOSYSRECORD");
            from_date = bundle.getString("FROM");
            to_date = bundle.getString("TO");
        }
        subdivision = (TextView) view.findViewById(R.id.txtsubdivision);
        totalvalidfile = (TextView) view.findViewById(R.id.txt_totalvalidfile);
        totaldownload = (TextView) view.findViewById(R.id.txt_totaldownloaded);
        notdownload = (TextView) view.findViewById(R.id.txt_notdownloaded);
        upload = (TextView) view.findViewById(R.id.txt_uploaded);
        notupload = (TextView) view.findViewById(R.id.txt_notuploaded);
        from = (TextView) view.findViewById(R.id.txt_from);
        to = (TextView) view.findViewById(R.id.txt_to);

        //Converting to integer

        int totalvalid= Integer.parseInt(valid);
        int totaldown = Integer.parseInt(download);
        int notdown = (totalvalid - totaldown);
        int uplo = Integer.parseInt(up);
        int notuplo = (totaldown - uplo);

        from.setText(from_date);
        to.setText(to_date);

        subdivision.setText(division);
        totalvalidfile.setText(valid);
        totaldownload.setText(download);
        notdownload.setText(notdown+"");
        upload.setText(up);
        notupload.setText(notuplo+"");
        return view;
    }

}
