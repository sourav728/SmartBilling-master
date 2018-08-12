package com.transvision.mbc.fragments;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transvision.mbc.R;
import com.transvision.mbc.ViewAllLocation;
import com.transvision.mbc.adapters.MRAdapter;
import com.transvision.mbc.receiver.NetworkChangeReceiver;
import com.transvision.mbc.values.FunctionsCall;
import com.transvision.mbc.values.GetSetValues;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
/**
 * Created by Sourav
 */
public class MRTrackingFragment extends Fragment {
    /*ListView lv;*/
    RecyclerView recyclerView;
    private BroadcastReceiver mNetworkReceiver;
    GetSetValues getSetValues, getSet;
    String MRCODE, MRNAME, MOBILE_NO, DEVICE_ID,LONGITUDE,LATITUDE;
    FunctionsCall functionsCall;
    public static final String GETSET = "getset";

    ArrayList<GetSetValues> arrayList;
    private MRAdapter mrAdapter;
    String subdivisioncode="";
    //static TextView tv_check_connection;
    public MRTrackingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mrtracking, container, false);
        /*show_hide = (LinearLayout) view.findViewById(R.id.lin_showhide);
        report = (Button) view.findViewById(R.id.btn_report);
        subdivspinner = (Spinner) view.findViewById(R.id.subdiv_spin);
        role_list = new ArrayList<>();
        roleAdapter = new RoleAdapter(role_list, getActivity());*/
        mNetworkReceiver = new NetworkChangeReceiver();
        //tv_check_connection = (TextView) view.findViewById(R.id.tv_check_connection);
        Bundle bundle = getArguments();
        if (bundle!= null)
        {
            subdivisioncode = bundle.getString("subdivcode");
        }

        arrayList = new ArrayList<>();

        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.mrtrack_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mrAdapter = new MRAdapter(getActivity(), arrayList, getSetValues);

        //setting the adapter
        recyclerView.setAdapter(mrAdapter);

        //contactlist = new ArrayList<>();
        functionsCall = new FunctionsCall();

        ConnectURL connectURL = new ConnectURL();
        connectURL.execute();

       /* subdivspinner.setAdapter(roleAdapter);
        getSet = new GetSetValues();

        for (int i = 0; i < getResources().getStringArray(R.array.subdivision).length; i++) {
            getSet = new GetSetValues();
            getSet.setLogin_role(getResources().getStringArray(R.array.subdivision)[i]);
            role_list.add(getSet);
            roleAdapter.notifyDataSetChanged();
        }

        subdivspinner.setSelection(0);

        subdivspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_hide.setVisibility(View.GONE);
                ConnectURL connectURL = new ConnectURL();
                connectURL.execute();
            }
        });*/
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater mi = getActivity().getMenuInflater();
        mi.inflate(R.menu.location,menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
       // searchView.setQueryHint("Search by Mrcode..");
        //below line is for searchview hint and hint color
        searchView.setQueryHint(Html.fromHtml("<font color = #212121>" + "Search by Mrcode.." + "</font>"));
        search(searchView);
       // inflater.inflate(R.menu.location, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_location:
                //Toast.makeText(getActivity(), "Will be implemented..", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ViewAllLocation.class);
                intent.putExtra("list",arrayList);
                intent.putExtra(GETSET, getSetValues);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    public class ConnectURL extends AsyncTask<String, String, String> {
        String response = "";

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("SubDivCode", subdivisioncode);
            try {
                response = UrlPostConnection("http://bc_service.hescomtrm.com/Service.asmx/LGLTMRDETAILS", datamap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            String res = parseServerXML(s);
            JSONArray jsonarray;
            try {
                jsonarray = new JSONArray(res);
                if (jsonarray.length() > 0) {
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        getSetValues = new GetSetValues();
                        MRCODE = jsonObject.getString("MRCODE");
                        MRNAME = jsonObject.getString("MRNAME");
                        MOBILE_NO = jsonObject.getString("MOBILE_NO");
                        DEVICE_ID = jsonObject.getString("DEVICE_ID");
                        LONGITUDE = jsonObject.getString("LONGITUDE");
                        LATITUDE =jsonObject.getString("LATITUDE");

                        Log.d("Debugg", "Mrcode" + MRCODE);
                        Log.d("Debugg", "Mrname" + MRNAME);
                        Log.d("Debugg", "Phone" + MOBILE_NO);
                        Log.d("Debugg", "IEMI" + DEVICE_ID);
                        Log.d("Debugg","LONGITUDE"+LONGITUDE);
                        Log.d("Debugg", "LATITUDE"+LATITUDE);

                        if (!MRCODE.equals(""))
                            getSetValues.setMrcode(MRCODE);
                        else getSetValues.setMrcode("NA");
                        if (!MRNAME.equals(""))
                            getSetValues.setMrname(MRNAME);
                        else getSetValues.setMrname("NA");
                        if (!MOBILE_NO.equals(""))
                            getSetValues.setMobileno(MOBILE_NO);
                        else getSetValues.setMobileno("NA");
                        if (!DEVICE_ID.equals(""))
                            getSetValues.setDeviceid(DEVICE_ID);
                        else getSetValues.setDeviceid("NA");
                        if (!LONGITUDE.equals(""))
                            getSetValues.setLongitude(LONGITUDE);
                        else getSetValues.setLongitude("NA");
                        if (!LATITUDE.equals(""))
                            getSetValues.setLatitude(LATITUDE);
                        else getSetValues.setLatitude("NA");

                        arrayList.add(getSetValues);
                        mrAdapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "Success..", Toast.LENGTH_SHORT).show();

                    /*******Need to changes the following listview string values*************/

                    /*ListAdapter adapter = new SimpleAdapter(getActivity(), contactlist,
                            R.layout.list_item3, new String[]{"MRCODE", "MRNAME", "MOBILE_NO", "DEVICE_ID",},
                            new int[]{R.id.txt_mr_code, R.id.txt_mr_name, R.id.txt_phone, R.id.txt_iemi});
                    lv.setAdapter(adapter);*/
                }
            } catch (Exception e) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                Toast.makeText(getActivity(), "No Values found!!", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
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

    private void search(SearchView searchView)
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mrAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
   /* public static void dialog(boolean value){
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

        }else {
            tv_check_connection.setVisibility(View.VISIBLE);
            tv_check_connection.setText("No Internet Connection!!");
            tv_check_connection.setBackgroundColor(Color.RED);
            tv_check_connection.setTextColor(Color.WHITE);

        }
    }*/

   /* private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getActivity().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }*/
    /*protected void unregisterNetworkChanges()
    {
        try
        {
            getActivity().unregisterReceiver(mNetworkReceiver);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        unregisterNetworkChanges();
        super.onDestroy();
    }*/
}
