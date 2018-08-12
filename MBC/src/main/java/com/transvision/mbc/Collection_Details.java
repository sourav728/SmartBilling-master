package com.transvision.mbc;


import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.transvision.mbc.adapters.CollectionAdapter;
import com.transvision.mbc.receiver.NetworkChangeReceiver;
import com.transvision.mbc.values.FunctionsCall;
import com.transvision.mbc.values.GetSetValues;

import org.apache.commons.lang.StringUtils;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Sourav
 */
public class Collection_Details extends Fragment {

    RecyclerView recyclerView;
    private BroadcastReceiver mNetworkReceiver;
    static TextView tv_check_connection;
    ListView lv;
    LinearLayout show_hide;
    TextView fromdate, todate;
    ImageView imagefrom, imageto;
    static Button report;
    String dd, date1, date2, subdivisioncode;
    FunctionsCall functionsCall;
    private int day, month, year;
    private Calendar mcalender;
    ArrayList<GetSetValues> collection_list;
    GetSetValues getsetvalues;
    String Counter = "", ReceiptCount = "", Amount = "", Mode = "", MRNAME = "", MOBILENO = "", LATITUDE = "", LONGITUDE = "", Date = "", Approved_flag = "", Flag = "";
    ArrayList<HashMap<String, String>> contactList;
    ArrayList<GetSetValues> arrayList;
    private CollectionAdapter collectionAdapter;
    private Toolbar toolbar;
    Menu menu;
    public Collection_Details() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_collection__details2, container, false);
        setHasOptionsMenu(true);

       /* tv_check_connection = (TextView) view.findViewById(R.id.tv_check_connection);
        mNetworkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();*/

        fromdate = (TextView) view.findViewById(R.id.txt_fromdate);
        todate = (TextView) view.findViewById(R.id.txt_todate);
        imagefrom = (ImageView) view.findViewById(R.id.img_fromdate);
        imageto = (ImageView) view.findViewById(R.id.img_todate);
        report = (Button) view.findViewById(R.id.btn_report);
        show_hide = (LinearLayout) view.findViewById(R.id.lin_showhide);
        getsetvalues = new GetSetValues();
        collection_list = new ArrayList<>();

        lv = (ListView) view.findViewById(R.id.list);

        contactList = new ArrayList<>();
        mcalender = Calendar.getInstance();
        day = mcalender.get(Calendar.DAY_OF_MONTH);
        year = mcalender.get(Calendar.YEAR);
        month = mcalender.get(Calendar.MONTH);
        functionsCall = new FunctionsCall();

        arrayList = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.mrtrack_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        collectionAdapter = new CollectionAdapter(getActivity(), arrayList, getsetvalues);

        //setting the adapter
        recyclerView.setAdapter(collectionAdapter);


        Bundle bundle = getArguments();
        if (bundle != null) {
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

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromdate.getText().toString().equals("") || todate.getText().toString().equals("")) {
                    if (fromdate.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Please Select from date!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Please Select to date!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    show_hide.setVisibility(View.GONE);
                    ConnectURL connectURL = new ConnectURL();
                    connectURL.execute();
                }
            }
        });

        return view;
    }

  /*  public static void dialog3(boolean value){
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
            report.setEnabled(true);
        }else {
            tv_check_connection.setVisibility(View.VISIBLE);
            tv_check_connection.setText("No Internet Connection!!");
            tv_check_connection.setBackgroundColor(Color.RED);
            tv_check_connection.setTextColor(Color.WHITE);
            report.setEnabled(false);
        }
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       /* MenuInflater mi = getActivity().getMenuInflater();
        mi.inflate(R.menu.toolbar_menu, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        super.onCreateOptionsMenu(menu, inflater);*/
        MenuInflater mi = getActivity().getMenuInflater();
        mi.inflate(R.menu.collection, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setQueryHint(Html.fromHtml("<font color = #212121>" + "Search by Counter.." + "</font>"));
        search(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public class ConnectURL extends AsyncTask<String, String, String> {
        String response = "";

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("subdivcode", "540038");
            datamap.put("FromDate", date1);
            datamap.put("ToDate", date2);
            try {
                response = UrlPostConnection("http://bc_service.hescomtrm.com/Service.asmx/CollectionSummary", datamap);
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
                        /**************Whenever we are using getter setter for setting values we have to initialize getter setter inside for loop*********/
                        getsetvalues = new GetSetValues();
                        Counter = jsonObject.getString("Counter");
                        ReceiptCount = jsonObject.getString("ReceiptCount");
                        Amount = jsonObject.getString("Amount");
                        Mode = jsonObject.getString("Mode");
                        MRNAME = jsonObject.getString("MRNAME");
                        MOBILENO = jsonObject.getString("MOBILENO");
                        LATITUDE = jsonObject.getString("LATITUDE");
                        LONGITUDE = jsonObject.getString("LONGITUDE");
                        //Approved flag and date is not there in service
                        Date = jsonObject.getString("date");
                        Approved_flag = jsonObject.getString("Approved_flag");
                        /*********Submit status**********/
                        //String submit = jsonObject.getString("");
                        if (StringUtils.startsWithIgnoreCase(Approved_flag, "A"))
                            Flag = "Approved";
                        else
                            Flag = "Not Approved";

                        Log.d("Debug", "Counter" + Counter);
                        Log.d("Debug", "ReceiptCount" + ReceiptCount);
                        Log.d("Debug", "Amount" + Amount);
                        Log.d("Debug", "Mode" + Mode);

                        if (!Counter.equals("")) {
                            getsetvalues.setCounter(Counter);
                        } else getsetvalues.setCounter("NA");
                        if (!ReceiptCount.equals("")) {
                            getsetvalues.setReceiptcount(ReceiptCount);
                        } else getsetvalues.setReceiptcount("NA");
                        if (!Amount.equals("")) {
                            getsetvalues.setReceipt_amount(Amount);
                        } else getsetvalues.setReceipt_amount("NA");
                        if (!Mode.equals("")) {
                            getsetvalues.setMode(Mode);
                        } else getsetvalues.setMode("NA");
                        if (!MRNAME.equals("")) {
                            getsetvalues.setMrname(MRNAME);
                        } else getsetvalues.setMrname("NA");
                        if (!MOBILENO.equals("")) {
                            getsetvalues.setMobileno(MOBILENO);
                        } else getsetvalues.setMobileno("NA");
                        if (!LATITUDE.equals("")) {
                            getsetvalues.setLatitude(LATITUDE);
                        } else getsetvalues.setLatitude("NA");
                        if (!LONGITUDE.equals("")) {
                            getsetvalues.setLongitude(LONGITUDE);
                        } else getsetvalues.setLongitude("NA");

                        if (!Date.equals("")) {
                            getsetvalues.setDate(Date);
                        } else getsetvalues.setDate(Date);
                        if (!Approved_flag.equals("")) {
                            getsetvalues.setApproved_flag(Flag);
                        } else getsetvalues.setApproved_flag("NA");

                        arrayList.add(getsetvalues);
                        collectionAdapter.notifyDataSetChanged();
                    }

                    Toast.makeText(getActivity(), "Success..", Toast.LENGTH_SHORT).show();

                    /*************Listview columns needs to be updated****************/

                } else Toast.makeText(getActivity(), "Failure!!", Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                //Code for going back to previous fragment here addtoBackStack will not work cause listview and full design are in same page
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                //Toast.makeText(getActivity(), "No Values found!!", Toast.LENGTH_SHORT).show();
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
            }
            super.onPostExecute(s);
        }
    }

    public void DateDialog1() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                dd = (year + "-" + (month + 1) + "-" + dayOfMonth);
                date1 = functionsCall.Parse_Date2(dd);
                fromdate.setText(date1);
            }
        };
        DatePickerDialog dpdialog = new DatePickerDialog(getActivity(), listener, year, month, day);
        dpdialog.show();
    }

    public void DateDialog2() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dd = (year + "-" + (month + 1) + "-" + dayOfMonth);
                date2 = functionsCall.Parse_Date2(dd);
                todate.setText(date2);
            }
        };
        DatePickerDialog dpdialog = new DatePickerDialog(getActivity(), listener, year, month, day);
        dpdialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpdialog.show();
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

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                collectionAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

  /*  private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getActivity().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
    protected void unregisterNetworkChanges()
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
        super.onDestroy();
        unregisterNetworkChanges();
    }*/
}
