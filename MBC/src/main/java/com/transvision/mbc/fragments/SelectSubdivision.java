package com.transvision.mbc.fragments;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.transvision.mbc.Collection_Details;
import com.transvision.mbc.R;
import com.transvision.mbc.adapters.RoleAdapter;
import com.transvision.mbc.receiver.NetworkChangeReceiver;
import com.transvision.mbc.values.GetSetValues;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Sourav
 */

public class SelectSubdivision extends Fragment {
    private static final int PROGRESS_HOLD = 1;
    static Button submit;
    FragmentTransaction fragmentTransaction;
    Spinner subdivspinner;
    ArrayList<GetSetValues> role_list;
    GetSetValues getSetValues, getSet;
    RoleAdapter roleAdapter;
    String main_role = "";
    ArrayList<GetSetValues> arrayList;
    String dum = "", dd = "", day = "", flag = "";
    ProgressDialog progressDialog;
    private BroadcastReceiver mNetworkReceiver;
    static TextView tv_check_connection;
    static ConnectURL connectURL;
    public SelectSubdivision() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_subdivision, container, false);
        submit = (Button) view.findViewById(R.id.btn_submit);
        tv_check_connection = (TextView) view.findViewById(R.id.tv_check_connection);
        mNetworkReceiver = new NetworkChangeReceiver();
        Bundle bundle = getArguments();

        subdivspinner = (Spinner) view.findViewById(R.id.subdiv_spin);
        role_list = new ArrayList<>();
        roleAdapter = new RoleAdapter(role_list, getActivity());
        subdivspinner.setAdapter(roleAdapter);
        arrayList = new ArrayList<>();

        connectURL = new ConnectURL();
        connectURL.execute();

        /*for (int i = 0; i < getResources().getStringArray(R.array.subdivision).length; i++) {
            getSet = new GetSetValues();
            getSet.setLogin_role(getResources().getStringArray(R.array.subdivision)[i]);
            role_list.add(getSet);
            roleAdapter.notifyDataSetChanged();
        }*/

         subdivspinner.setSelection(0);

        if (bundle != null) {
            //subdivisioncode = bundle.getString("subdivcode");
            dum = bundle.getString("date");
            dd = bundle.getString("dd");
            day = bundle.getString("daycount");
            flag = bundle.getString("flag");
        }

        subdivspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tvrole2 = (TextView) view.findViewById(R.id.spinner_txt);
                String role = tvrole2.getText().toString();
                main_role = role;
                if (main_role.equals("540001 - CITY SUB-DIVISION-3 HUBLI")) {
                    main_role = "540001";
                }else if (main_role.equals("540002 - CITY SUB-DIVISION-2 HUBLI")){
                    main_role = "540002";
                }else if (main_role.equals("540004 - CITY SUB-DIVISION-1 HUBLI")){
                    main_role = "540004";
                }else if (main_role.equals("540006 - CITY SUB-DIVISION-1 DHARWAD")){
                    main_role = "540006";
                }else if (main_role.equals("540008 - CITY SUB-DIVISION-2 DHARWAD")){
                    main_role = "540008";
                }else if (main_role.equals("540011 - CITY SUB-DIVISION GADAG")){
                    main_role = "540011";
                }else if (main_role.equals("540014 - LAKSHMESHVAR SUB-DIVISION")){
                    main_role = "540014";
                }else if (main_role.equals("540015 - NARGUND SUB-DIVISION")){
                    main_role = "540015";
                }else if (main_role.equals("540018 - HAVERI SUB-DIVISION")){
                    main_role = "540018";
                }else if (main_role.equals("540020 - SAVANUR SUB-DIVISION")){
                    main_role = "540020";
                }else if (main_role.equals("540022 - RANEBENNUR-1 SUB-DIVISION")){
                    main_role = "540022";
                }else if (main_role.equals("540026 - SIRSI SUB-DIVISION")){
                    main_role = "540026";
                }else if (main_role.equals("540030 - DANDELI SUB-DIVISION")){
                    main_role = "540030";
                }else if (main_role.equals("540032 - KARWAR SUB-DIVISION")){
                    main_role = "540032";
                }else if (main_role.equals("540034 - KUMTA SUB-DIVISION")){
                    main_role = "540034";
                }else if (main_role.equals("540036 - BHATKAL SUB-DIVISION")){
                    main_role = "540036";
                } else if (main_role.equals("540037 - CITY SUB-DIVISION-1 BELGAUM")) {
                    main_role = "540037";
                } else if (main_role.equals("540038 - CITY SUB-DIVISION-2 BELGAUM")) {
                    main_role = "540038";
                } else if (main_role.equals("540039 - CITY SUB-DIVISION-3 BELGAUM")) {
                    main_role = "540039";
                } else if (main_role.equals("540042 - BAILHONGAL SUB-DIVISION")){
                    main_role = "540042";
                } else if (main_role.equals("540043 - SAUNDATTI SUB-DIVISION")){
                    main_role = "540043";
                } else if (main_role.equals("540044 - RAMDURG SUB-DIVISION")){
                    main_role = "540044";
                } else if (main_role.equals("540046 - GOKAK SUB-DIVISION")){
                    main_role = "540046";
                } else if (main_role.equals("540047 - CHIKODI SUB-DIVISION")){
                    main_role = "540047";
                } else if (main_role.equals("540048 - NIPPANI SUB-DIVISION")){
                    main_role = "540048";
                } else if (main_role.equals("540050 - ATHANI SUB-DIVISION")){
                    main_role = "540050";
                } else if (main_role.equals("540055 - CITY SUB-DIVISION-1 BIJAPUR")){
                    main_role = "540055";
                } else if (main_role.equals("540056 - CITY SUB-DIVISION-2 BIJAPUR")){
                    main_role = "540056";
                } else if (main_role.equals("540061 - INDI SUB-DIVISION")){
                    main_role = "540061";
                } else if (main_role.equals("540065 - CITY SUB-DIVISION BAGALKOT")){
                    main_role = "540065";
                } else if (main_role.equals("540067 - GULEDAGUDDA SUB-DIVISION")){
                    main_role = "540067";
                } else if (main_role.equals("540068 - ILKAL SUB-DIVISION")){
                    main_role = "540068";
                } else if (main_role.equals("540069 - JAMKHANDI SUB-DIVISION")){
                    main_role = "540069";
                } else if (main_role.equals("540070 - RABKAVI SUB-DIVISION")){
                    main_role = "540070";
                } else if (main_role.equals("540071 - MAHALINGPUR SUB-DIVISION")){
                    main_role = "540071";
                } else if (main_role.equals("540072 - MUDHOL SUB-DIVISION")) {
                    main_role = "540072";
                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag.equals("One")) {
                    ShowMrDetails showMrDetails = new ShowMrDetails();
                    Bundle bundle = new Bundle();
                    bundle.putString("subdivcode", main_role);
                    bundle.putString("date", dum);
                    bundle.putString("dd", dd);
                    bundle.putString("daycount", day);
                    showMrDetails.setArguments(bundle);
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container_main, showMrDetails).addToBackStack(null).commit();
                } else if (flag.equals("Two")) {
                    SummaryFragment summaryFragment = new SummaryFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("subdivcode", main_role);
                    summaryFragment.setArguments(bundle);
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container_main, summaryFragment).addToBackStack(null).commit();

                } else if (flag.equals("Three")) {
                    Collection_Details collection = new Collection_Details();
                    Bundle bundle = new Bundle();
                    bundle.putString("subdivcode", main_role);
                    collection.setArguments(bundle);
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container_main, collection).addToBackStack(null).commit();
                } else if (flag.equals("Four")) {
                    MRTrackingFragment mrTrackingFragment = new MRTrackingFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("subdivcode", main_role);
                    mrTrackingFragment.setArguments(bundle);
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container_main, mrTrackingFragment).addToBackStack(null).commit();
                } else {
                    Toast.makeText(getActivity(), "Please select..", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public static void dialog1(boolean value){
        if(value){
            tv_check_connection.setText("Back Online");
            tv_check_connection.setBackgroundColor(Color.parseColor("#558B2F"));
            tv_check_connection.setTextColor(Color.WHITE);
            Handler handler = new Handler();
            Runnable delayrunnable = new Runnable() {
                @Override
                public void run() {
                    tv_check_connection.setVisibility(View.GONE);
                }
            };
            handler.postDelayed(delayrunnable, 3000);
            submit.setEnabled(true);
        }else {
            tv_check_connection.setVisibility(View.VISIBLE);
            tv_check_connection.setText("No Internet Connection!!");
            tv_check_connection.setBackgroundColor(Color.RED);
            tv_check_connection.setTextColor(Color.WHITE);
            submit.setEnabled(false);
        }
    }

    public class ConnectURL extends AsyncTask<String, String, String> {
        String response = "";

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),"Loading","Loading Please wait..",true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                response = UrlPostConnection("http://bc_service.hescomtrm.com/Service.asmx/Subdivision_Details");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            String res = parseServerXML(s);
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(res);
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        getSet = new GetSetValues();
                        String subdivisionname = jsonObject.getString("subdivisionname");
                        getSet.setLogin_role(subdivisionname);
                        role_list.add(getSet);
                        roleAdapter.notifyDataSetChanged();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity(), "No Value Found!!", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
            super.onPostExecute(s);
        }
    }

    private String UrlPostConnection(String Post_Url) throws IOException {
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
