package com.transvision.mbc.fragments.billing;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.transvision.mbc.R;
import com.transvision.mbc.adapters.RoleAdapter;
import com.transvision.mbc.values.FunctionsCall;
import com.transvision.mbc.values.GetSetValues;

import java.util.ArrayList;
import java.util.Calendar;
/**
 * Created by Sourav
 */

public class DL_MNR_Report extends Fragment {
    Spinner dlmnrspinner,statusspinner;
    ArrayList<GetSetValues> roles_list1, roles_list2;
    RoleAdapter roleAdapter1,roleAdapter2;
    GetSetValues getSetValues;
    String main_role = "";
    ImageView date;
    String dd, selecteddate;
    private Calendar mcalender;
    TextView dl_mnr_date;
    FunctionsCall functionsCall;
    private int day, month, year;
    public DL_MNR_Report() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dl__mnr__report, container, false);
        dlmnrspinner = (Spinner) view.findViewById(R.id.dlmnr_spinner);
        statusspinner = (Spinner) view.findViewById(R.id.status_spinner);
        date = (ImageView) view.findViewById(R.id.img_fromdate);
        dl_mnr_date = (TextView) view.findViewById(R.id.txt_date);
        functionsCall = new FunctionsCall();

        roles_list1 = new ArrayList<>();
        roles_list2 = new ArrayList<>();
        roleAdapter1 = new RoleAdapter(roles_list1, getActivity());
        roleAdapter2 = new RoleAdapter(roles_list2, getActivity());

        dlmnrspinner.setAdapter(roleAdapter1);
        statusspinner.setAdapter(roleAdapter2);

        for (int i = 0; i < getResources().getStringArray(R.array.dl_mnr).length; i++) {
            getSetValues = new GetSetValues();
            getSetValues.setLogin_role(getResources().getStringArray(R.array.dl_mnr)[i]);
            roles_list1.add(getSetValues);
            roleAdapter1.notifyDataSetChanged();
        }

        dlmnrspinner.setSelection(0);

        for (int i = 0; i < getResources().getStringArray(R.array.dlmnr_status).length; i++) {
            getSetValues = new GetSetValues();
            getSetValues.setLogin_role(getResources().getStringArray(R.array.dlmnr_status)[i]);
            roles_list2.add(getSetValues);
            roleAdapter2.notifyDataSetChanged();
        }
        statusspinner.setSelection(0);

        dlmnrspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tvrole = (TextView) view.findViewById(R.id.spinner_txt);
                String role = tvrole.getText().toString();

                if (!tvrole.equals("--SELECT--")) {
                    main_role = role;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        statusspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tvrole = (TextView) view.findViewById(R.id.spinner_txt);
                String role = tvrole.getText().toString();

                if (!tvrole.equals("--SELECT--")) {
                    main_role = role;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "clicked..", Toast.LENGTH_SHORT).show();
                datedialog();
            }
        });

        return view;
    }

    public void datedialog()
    {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dd = (year + "-" + (month + 1) + "-" + dayOfMonth);
                selecteddate = functionsCall.Parse_Date4(dd);
                dl_mnr_date.setText(selecteddate);
            }
        };
        DatePickerDialog dpdialog = new DatePickerDialog(getActivity(), listener, year, month, day);
        dpdialog.show();
    }

}
