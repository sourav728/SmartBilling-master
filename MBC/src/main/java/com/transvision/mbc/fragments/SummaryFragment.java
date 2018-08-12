package com.transvision.mbc.fragments;


import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.transvision.mbc.R;
import com.transvision.mbc.values.FunctionsCall;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
/**
 * Created by Sourav
 */
public class SummaryFragment extends Fragment {
    Button report;

   /* Spinner subdivspinner;
    ArrayList<GetSetValues> role_list;
    GetSetValues getSetValues, getSet;
    RoleAdapter roleAdapter;
    String main_role = "";
    ArrayList<GetSetValues> arrayList;*/

    TextView fromdate, todate;
    ImageView imagefrom, imageto;
    private int day, month, year;
    private Calendar mcalender;
    int daycount;
    String dd, date1, date2, subdivisioncode;
    String DWNRECORD,UPLOADRECORD,INFOSYSRECORD;
    FunctionsCall functioncall;
    FragmentTransaction fragmenttransaction;
    public SummaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        fromdate = (TextView) view.findViewById(R.id.txt_fromdate);
        todate = (TextView) view.findViewById(R.id.txt_todate);
        imagefrom = (ImageView) view.findViewById(R.id.img_fromdate);
        imageto = (ImageView) view.findViewById(R.id.img_todate);
        report = (Button) view.findViewById(R.id.btn_report);

       /* subdivspinner = (Spinner) view.findViewById(R.id.subdiv_spin);
        role_list = new ArrayList<>();
        roleAdapter = new RoleAdapter(role_list, getActivity());
        subdivspinner.setAdapter(roleAdapter);
        arrayList = new ArrayList<>();


        for (int i = 0; i < getResources().getStringArray(R.array.subdivision).length; i++) {
            getSet = new GetSetValues();
            getSet.setLogin_role(getResources().getStringArray(R.array.subdivision)[i]);
            role_list.add(getSet);
            roleAdapter.notifyDataSetChanged();
        }

        subdivspinner.setSelection(0);*/

        mcalender = Calendar.getInstance();
        day = mcalender.get(Calendar.DAY_OF_MONTH);
        daycount = day;
        year = mcalender.get(Calendar.YEAR);
        month = mcalender.get(Calendar.MONTH);
        functioncall = new FunctionsCall();
        Bundle bundle = getArguments();
        if (bundle !=null)
        {
            subdivisioncode = bundle.getString("subdivcode");
        }

        imagefrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog1();
            }
        });

        imageto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateDialog2();
            }
        });

       /* subdivspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tvrole = (TextView) view.findViewById(R.id.spinner_txt);
                String role = tvrole.getText().toString();

                if (!tvrole.equals("--SELECT--")) {
                    main_role = role;
                    if (main_role.equals("CSD1")) {
                        main_role = "540037";
                        Toast.makeText(getActivity(), "CSD1 selected.." + main_role, Toast.LENGTH_SHORT).show();

                    } else if (main_role.equals("CSD2")) {
                        main_role = "540038";
                        Toast.makeText(getActivity(), "CSD2 selected.." + main_role, Toast.LENGTH_SHORT).show();
                    } else if (main_role.equals("CSD3")) {
                        main_role = "540039";
                        Toast.makeText(getActivity(), "CSD3 selected.." + main_role, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Nothing selected..", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Select Subdivision!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (fromdate.getText().toString().equals("") || todate.getText().toString().equals(""))
                {
                    if (fromdate.getText().toString().equals(""))
                    {
                        Toast.makeText(getActivity(), "Please Select from date!!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Please Select to date!!", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    ConnectURL connecturl = new ConnectURL();
                    connecturl.execute();
                }*/
              if(!fromdate.getText().toString().equals(""))
              {
                  if (!todate.getText().toString().equals(""))
                  {
                      ConnectURL connecturl = new ConnectURL();
                      connecturl.execute();

                  }else Toast.makeText(getActivity(), "Please Select To date!!", Toast.LENGTH_SHORT).show();
              }else Toast.makeText(getActivity(), "Please select from date!!", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    public void DateDialog1() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                dd = (year + "-" + (month + 1) + "-"  + dayOfMonth);
                date1 = functioncall.Parse_Date4(dd);
                fromdate.setText(date1);
            }
        };
        DatePickerDialog dpdialog = new DatePickerDialog(getActivity(), listener, year, month, day);
        dpdialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpdialog.show();
    }

    public void DateDialog2() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dd = (year + "-" + (month + 1) + "-"  + dayOfMonth);
                date2 = functioncall.Parse_Date4(dd);
                todate.setText(date2);
            }
        };
        DatePickerDialog dpdialog = new DatePickerDialog(getActivity(), listener, year, month, day);
        dpdialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpdialog.show();
    }

    public class ConnectURL extends AsyncTask<String, String, String>
    {
        String response = "";

        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String> datamap = new HashMap<>();
            datamap.put("subdivcode", subdivisioncode);
            datamap.put("fromdate",functioncall.Parse_Date5(date1));
            datamap.put("todate",functioncall.Parse_Date5(date2) );
            try
            {
                response = UrlPostConnection("http://bc_service.hescomtrm.com/Service.asmx/FilesCount",datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            String res = parseServerXML(s);
            try
            {
                JSONObject jsonObject = new JSONObject(res);
                String message = jsonObject.getString("message");

                if (StringUtils.startsWithIgnoreCase(message, "Failed"))
                {
                    //Toast.makeText(getActivity(), "No Records Found!!", Toast.LENGTH_SHORT).show();
                    //below code is for custom toast
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,(ViewGroup) getActivity().findViewById(R.id.toast_layout));
                    ImageView imageView = (ImageView) layout.findViewById(R.id.image);
                    imageView.setImageResource(R.drawable.invalid);
                    TextView textView = (TextView) layout.findViewById(R.id.text);
                    textView.setText("No Records Found!!");
                    textView.setTextSize(20);
                    Toast toast = new Toast(getActivity());
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                    //end of custom toast code
                    fromdate.setText("");
                    todate.setText("");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                JSONArray jsonarr = new JSONArray(res);
                JSONObject jsonobject = jsonarr.getJSONObject(0);
                DWNRECORD = jsonobject.getString("DWNRECORD");
                UPLOADRECORD = jsonobject.getString("UPLOADRECORD");
                INFOSYSRECORD = jsonobject.getString("INFOSYSRECORD");

                SummaryDetailsFragment summarydetailsfragment = new SummaryDetailsFragment();

                Bundle bundle = new Bundle();
                bundle.putString("subdivcode",subdivisioncode);
                bundle.putString("DWNRECORD", DWNRECORD);
                bundle.putString("UPLOADRECORD", UPLOADRECORD);
                bundle.putString("INFOSYSRECORD", INFOSYSRECORD);
                bundle.putString("FROM",date1);
                bundle.putString("TO",date2);
                summarydetailsfragment.setArguments(bundle);

                fragmenttransaction = getFragmentManager().beginTransaction();
                fragmenttransaction.replace(R.id.container_main,summarydetailsfragment).addToBackStack(null).commit();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private String UrlPostConnection(String Post_Url, HashMap<String, String> datamap) throws IOException {
        String response = "";
        URL url = new URL(Post_Url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream outputStream = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        writer.write(getPostDataString(datamap));
        writer.flush();
        writer.close();
        outputStream.close();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }
        } else {
            response = "";
        }
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            Log.d("debug", result.toString());
        }
        return result.toString();
    }

    public String parseServerXML(String result) {
        String value = "";
        XmlPullParserFactory pullParserFactory;
        InputStream res;
        try {
            res = new ByteArrayInputStream(result.getBytes());
            pullParserFactory = XmlPullParserFactory.newInstance();
            pullParserFactory.setNamespaceAware(true);
            XmlPullParser parser = pullParserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(res, null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        switch (name) {
                            case "string":
                                value = parser.nextText();
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

}
