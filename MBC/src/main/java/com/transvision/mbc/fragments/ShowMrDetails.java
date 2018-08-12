package com.transvision.mbc.fragments;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.transvision.mbc.R;
import com.transvision.mbc.adapters.MRDetailsAdapter;
import com.transvision.mbc.values.FunctionsCall;
import com.transvision.mbc.values.GetSetValues;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

/**
 * Created by Sourav
 */
public class ShowMrDetails extends Fragment {
    RecyclerView recyclerView;
    Button pdf;
    TextView show_date, show_subdiv_code, show_mr_count,show_total_records, show_download_count, show_billed_count, show_unbilled_count, holiday;
    FunctionsCall functioncalls;

    boolean previous = false, next = false, oncreate = false;
    int count = 0;

    ListView lv;

    JSONArray arr;

    ArrayList<HashMap<String, String>> contactList;

    ProgressDialog progressDialog;

    LinearLayout linsubdiv, linsubdivhide, table_layout;

    String requestUrl = "", subdivisioncode, dum, dd, date;
    int totalmr;
    int total_download = 0, total_billed = 0, total_unbilled = 0, total_records=0, pd_billed_records=0, d_billed_records=0;
    String dummy1 = "";
    String datestore = "";
    String day;
    String storecurrentday = "";
    String substringstorecurrentprevday;
    String substringstorecurrentnextday;
    private View mRootView;
    List<Bitmap> bmaps = new ArrayList<Bitmap>();
    GetSetValues getsetvalues;
    ArrayList<GetSetValues> arrayList;
    private MRDetailsAdapter mrdetailsAdapter;
    public ShowMrDetails() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_mr_details, container, false);
        getActivity().setTitle("MR Time Stamp");
        // pdf = (Button) view.findViewById(R.id.btn_pdf);

        show_date = (TextView) view.findViewById(R.id.txt_showdate);
        show_subdiv_code = (TextView) view.findViewById(R.id.txt_show_subdiv_code);
        show_mr_count = (TextView) view.findViewById(R.id.txt_total_mr);

        show_download_count = (TextView) view.findViewById(R.id.txt_total_download);
        show_billed_count = (TextView) view.findViewById(R.id.txt_total_billed);
        show_unbilled_count = (TextView) view.findViewById(R.id.txt_total_unbilled);
        // holiday = (TextView) view.findViewById(R.id.txt_holiday);
        show_total_records = (TextView) view.findViewById(R.id.txt_total_records);

        table_layout = (LinearLayout) view.findViewById(R.id.lin_table_layout);
        lv = (ListView) view.findViewById(R.id.list);
        getsetvalues = new GetSetValues();
        contactList = new ArrayList<>();


        arrayList = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.mrdetails_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        mrdetailsAdapter = new MRDetailsAdapter(getActivity(), arrayList);

        //setting the adapter
        recyclerView.setAdapter(mrdetailsAdapter);

        Bundle bundle = getArguments();

        if (bundle != null) {
            subdivisioncode = bundle.getString("subdivcode");
            dum = bundle.getString("date");
            dd = bundle.getString("dd");
            day = bundle.getString("daycount");
        }

        final BottomNavigationView buttomnavigationview = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);
        Menu menunav = buttomnavigationview.getMenu();

        buttomnavigationview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_previous:
                        storecurrentday = date;
                        String converted1 = "";
                        String cutday = "";
                        /*********HERE CONVERTING STRING TO DATE FORMAT AND DECEMENTING*************/
                        try {
                            String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                            cutday = today.substring(0, 2);
                            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                            Date d3 = df.parse(today);
                            onPrevious();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        int cutdayint = Integer.parseInt(cutday);
                        int daytocompare = Integer.parseInt(day);
                        break;

                    case R.id.action_print:
                        pdfgeneration();
                        //Toast.makeText(getActivity(), "Print Success..", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_next:
                        onNext();
                        break;
                }
                return false;
            }
        });

        /*********For the below code it will call ConnectURL for once***************/
        if (count == 0) {
            ConnectURL connectURL = new ConnectURL(dd);
            connectURL.execute();
            count++;
        } else {
        }

        return view;
    }

    public void onPrevious() {
        previous = true;
        pd_billed_records = 0;
        d_billed_records = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(storecurrentday));
            c.add(Calendar.DATE, -1);  // number of days to subtract
            storecurrentday = sdf.format(c.getTime());
            //String storecurrentdayconverted = functioncalls.Parse_date3(storecurrentday);
            substringstorecurrentprevday = storecurrentday.substring(8, 10);
            int updateprevint = Integer.parseInt(substringstorecurrentprevday);
            //updateprevint = updateprevint + 1;
            Log.d("PREVIOUS VALUE", "PREVIOUS VALUE" + updateprevint);
            if (updateprevint == 31 || updateprevint == 28 || updateprevint == 30 || updateprevint == 31) {
                Toast.makeText(getActivity(), "Before 1st no Billing..", Toast.LENGTH_SHORT).show();
            } else {
                ConnectURL connectURL = new ConnectURL(storecurrentday);
                connectURL.execute();
                Fragment currentfragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container_main);
                FragmentTransaction fragmenttransaction = getFragmentManager().beginTransaction();
                fragmenttransaction.detach(currentfragment);
                fragmenttransaction.attach(currentfragment);
                fragmenttransaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onNext() {
        pd_billed_records = 0;
        d_billed_records = 0;
        next = true;
        String storecurrentday = "";
        storecurrentday = date;
        String converted1 = "";

        /*********HERE CONVERTING STRING TO DATE FORMAT AND DECEMENTING*************/
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(storecurrentday));
            c.add(Calendar.DATE, 1);  // number of days to add
            storecurrentday = sdf.format(c.getTime());
            substringstorecurrentnextday = storecurrentday.substring(8, 10);
            int updatenextint = Integer.parseInt(substringstorecurrentnextday);
            int max_val = Integer.parseInt(day);//18


            Log.d("Max val", "val" + max_val);
            if (updatenextint == 16||updatenextint == 17|| updatenextint == 18|| updatenextint == 19||updatenextint == 20|| updatenextint ==21||updatenextint ==22||updatenextint==23||updatenextint==24||updatenextint ==25||updatenextint ==26||updatenextint ==27||updatenextint ==28||updatenextint ==29||updatenextint == 30||updatenextint ==31) {
                Toast.makeText(getActivity(), "After 15 no billing!!", Toast.LENGTH_SHORT).show();
            } else {
                ConnectURL connectURL = new ConnectURL(storecurrentday);
                connectURL.execute();
                Fragment currentfragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container_main);
                FragmentTransaction fragmenttransaction = getFragmentManager().beginTransaction();
                fragmenttransaction.detach(currentfragment);
                fragmenttransaction.attach(currentfragment);
                fragmenttransaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**************PDF GENERATION PART*************/
    public void pdfgeneration() {
       /* //getWholeListViewItemsToBitmap(lv);
        String FILE = Environment.getExternalStorageDirectory().toString()
                + "/PDF/" + "Trm.pdf";
        com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/PDF");
        myDir.mkdir();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            //addMetadata(document);
            addTitlePage(document);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
        Toast.makeText(getActivity(), "PDF file is created.." + FILE, Toast.LENGTH_SHORT).show();
        //For displaying pdf after creation*/
        Toast.makeText(getActivity(), "Will be Implemented..", Toast.LENGTH_SHORT).show();
    }

    public void addTitlePage(com.itextpdf.text.Document document) throws DocumentException {
        // Font Style for Document
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD
                | Font.UNDERLINE, BaseColor.GRAY);
        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

       /* Anchor anchor = new Anchor("First Chapter",catFont);
        anchor.setName("First Chapter");

        Chapter catPart = new Chapter(new Paragraph(anchor), 1);*/

        Paragraph hascom = new Paragraph("", catFont);

        hascom.add("SUBDIV CODE" + "   " + ":" + subdivisioncode + "\n");
        hascom.add("DATE" + "   " + ":" + date + "\n");
        hascom.add("TOTAL MR_COUNT" + "   " + ":" + totalmr + "\n");
        hascom.add("TOTAL DOWNLOAD" + "   " + ":" + total_download + "\n");
        hascom.add("TOTAL BILLED" + "   " + ":" + total_billed + "\n");
        hascom.add("TOTAL UNBILLED" + "   " + ":" + total_unbilled + "\n");
        hascom.setAlignment(Element.ALIGN_CENTER);

        /************ADDING BORDER TO PDF FORMAT***************/

        Rectangle rect = new Rectangle(577, 825, 18, 25);
        rect.enableBorderSide(1);
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBorderColor(BaseColor.BLACK);
        rect.setBorderWidth(1);
        document.add(rect);

        createTable(hascom, contactList);
        document.add(hascom);

        Rectangle rect2 = new Rectangle(577, 825, 18, 25);
        rect2.enableBorderSide(1);
        rect2.enableBorderSide(2);
        rect2.enableBorderSide(4);
        rect2.enableBorderSide(8);
        rect2.setBorderColor(BaseColor.BLACK);
        rect2.setBorderWidth(1);
        document.add(rect);

        document.newPage();
    }

    public void createTable(Paragraph subCatPart, ArrayList<HashMap<String, String>> contactList)
            throws BadElementException {
        String mrcode = "", billed = "", download = "", download_time = "", billed_record = "", billed_time = "", unbilled_record = "";
        PdfPTable table = new PdfPTable(7);


        PdfPCell c1 = new PdfPCell(new Phrase("Mrcode"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Download"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Time"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);


        c1 = new PdfPCell(new Phrase("Billed"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Time"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Call mr"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Unbilled"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);
        List mr = new ArrayList();
        List down = new ArrayList();
        List down_time = new ArrayList();
        List bill = new ArrayList();
        List bill_time = new ArrayList();
        List unbilled = new ArrayList();

        /*********GETTING VALUES FROM ARRAYLIST*************/

        for (int i = 0; i < contactList.size(); i++) {
            HashMap<String, String> hasmap = contactList.get(i);
            mrcode = hasmap.get("mrcode");
            mr.add(mrcode);

            download = hasmap.get("downlodrecord");
            down.add(download);

            download_time = hasmap.get("download_time");
            down_time.add(download_time);

            billed_record = hasmap.get("billed_record");
            bill.add(billed_record);

            billed_time = hasmap.get("billed_time");
            bill_time.add(billed_time);

            unbilled_record = hasmap.get("unbilled_record");
            unbilled.add(unbilled_record);

            // Toast.makeText(getActivity(), "BILLED"+ billed, Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < contactList.size(); i++) {
            table.addCell((String) mr.get(i));
            table.addCell((String) down.get(i));
            table.addCell((String) down_time.get(i));
            table.addCell((String) bill.get(i));
            table.addCell((String) bill_time.get(i));
            table.addCell("+919675432345");
            table.addCell((String) unbilled.get(i));
        }
        subCatPart.add(table);

    }

    public class ConnectURL extends AsyncTask<String, String, String> {
        public ConnectURL(String passdate) {
            date = passdate;
        }

        String response = "";

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();

            datamap.put("subdivcode", subdivisioncode);
            datamap.put("Ddate", date);

            try {
                response = UrlPostConnection("http://bc_service.hescomtrm.com/Service.asmx/BilledUnbilledDetails", datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            // date = dd + "";
            //showdialog(SUBDIV_DETAILS_SUCCESS);
            total_download = 0;
            total_unbilled = 0;
            total_billed = 0;
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            String res = parseServerXML(s);
            try {
                JSONObject jsonObject = new JSONObject(res);
                String message = jsonObject.getString("message");

                if (StringUtils.startsWithIgnoreCase(message, "Failed")) {

                    /**************Custom Toast***************/
                    Toast toast = Toast.makeText(getActivity(), "Holiday so no Billings....", Toast.LENGTH_LONG);
                    View view = toast.getView();

                    //To change the Background of Toast
                    view.setBackgroundColor(Color.TRANSPARENT);
                    TextView text = (TextView) view.findViewById(android.R.id.message);

                    //Shadow of the Of the Text Color
                    text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
                    text.setTextColor(Color.RED);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                    // text.setTextSize(Integer.valueOf(getResources().getString(R.string.text_size)));
                    toast.show();
                    /***********End of Custom Toast******************/
                    //Toast.makeText(getActivity(), "Holiday so no Billings....", Toast.LENGTH_SHORT).show();
                    table_layout.setVisibility(View.INVISIBLE);
                    holiday.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(getActivity(), "Success....", Toast.LENGTH_SHORT).show();
                    table_layout.setVisibility(View.VISIBLE);
                    holiday.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                arr = new JSONArray(res);
                JSONObject jsonObject;
                for (int i = 0; i < arr.length(); i++) {
                    jsonObject = arr.getJSONObject(i);
                    getsetvalues = new GetSetValues();
                    String subdiv_code = jsonObject.getString("Sub_div_code");
                    String mr_code = jsonObject.getString("MR_Code");

                    Log.d("debug", "mr_code is" + mr_code);

                    String downlod_record = jsonObject.getString("Download_Record");

                    String download_time = jsonObject.getString("Download_DateTime");

                    String billed_record = jsonObject.getString("Billed_Record");

                    String billed_time = jsonObject.getString("Billed_DateTime");
                    String ftp_status = jsonObject.getString("Ftp_Status");
                    String Status = jsonObject.getString("Status");

                 /*   String democount = downlod_record;
                    total_records = total_records + Integer.parseInt(democount);*/

                    if (Status.equals("D"))
                    {
                        total_download = total_download + Integer.parseInt(downlod_record);
                        pd_billed_records = pd_billed_records + Integer.parseInt(downlod_record);
                    }
                    else
                    {
                        //total_download = total_download + 0;
                        d_billed_records = d_billed_records + Integer.parseInt(downlod_record);
                    }

                    //total_records = total_records + (pd_billed_records + d_billed_records);
                  /*  total_download = total_download + Integer.parseInt(downlod_record);
                    if (Status.equals("PD"))
                    {
                        total_records = total_records + Integer.parseInt(downlod_record);
                    }*/
                    //total_records = total_records + Integer.parseInt(downlod_record);

                   // HashMap<String, String> item = new HashMap<>();

                    if (billed_record.equals("")) {
                       // item.put("billed_record", "NA");
                        getsetvalues.setBilled_record("NA");
                        total_billed = total_billed + 0;

                    } else {
                        //item.put("billed_record", billed_record);
                        getsetvalues.setBilled_record(billed_record);
                        total_billed = total_billed + Integer.parseInt(billed_record);
                    }
                    /********FETCHING ONLY DOWNLOADTIME***********/

                    if (download_time.equals("")) {
                       // item.put("download_time", "NA");
                        getsetvalues.setDownload_time("NA");
                    } else {
                        String part2 = download_time.substring(download_time.indexOf(" ") + 1, 16);
                        //item.put("download_time", part2);
                        getsetvalues.setDownload_time(part2);
                    }

                    /******FETCHING ONLY BILLEDTIME*************/

                    if (billed_time.equals("")) {
                        //item.put("billed_time", "NA");
                        getsetvalues.setBilled_time("NA");
                    } else {
                        String part2 = billed_time.substring(billed_time.indexOf(" ") + 1, 16);
                        //item.put("billed_time", part2);
                        getsetvalues.setBilled_time(part2);
                    }

                    /******CHECKING DOWNLOAD RECORD IS EMPTY OR NOT*****/
                    if (downlod_record.equals("")) {
                        //item.put("downlodrecord", "NA");
                        getsetvalues.setDownlod_record("NA");
                    } else {
                        //item.put("downlodrecord", downlod_record);
                        getsetvalues.setDownlod_record(downlod_record);
                    }

                    /*****FOR UNBILLED************/

                      if (billed_record.equals("")) {
                            //item.put("unbilled_record", downlod_record);
                            getsetvalues.setUnbilled_record(downlod_record);
                            total_unbilled = total_unbilled + Integer.parseInt(downlod_record);
                        } else {
                            int val = Integer.parseInt(downlod_record) - Integer.parseInt(billed_record);
                           // item.put("unbilled_record", val + "");
                            getsetvalues.setUnbilled_record(val + "");
                            total_unbilled = total_unbilled + val;
                        }


                    if (Status.equals("D")) {
                        getsetvalues.setStatus("D");
                        //item.put("Status", "D");
                    } else {
                        //item.put("Status", "PD");
                        getsetvalues.setStatus("PD");
                    }
                    String mr_code1 = mr_code.substring(6, 8);
                    String mr_code2 = ".." + mr_code1;
                    //item.put("mrcode", mr_code2);
                    getsetvalues.setMrcode(mr_code2);
                    //item.put("ftp_status", ftp_status);
                    //contactList.add(item);
                    arrayList.add(getsetvalues);
                    mrdetailsAdapter.notifyDataSetChanged();
                }
                totalmr = arr.length();
            } catch (Exception e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                //Toast.makeText(getActivity(), "No values found!!!", Toast.LENGTH_SHORT).show();
            }

          /*  ListAdapter adapter = new SimpleAdapter(getActivity(), contactList,
                    R.layout.list_item, new String[]{"mrcode", "Status", "downlodrecord", "download_time", "billed_record", "billed_time", "unbilled_record"},
                    new int[]{R.id.txt_mrcode, R.id.txt_status, R.id.txt_downloaded_record, R.id.txt_download_time, R.id.txt_billed_record, R.id.txt_billed_time, R.id.txt_unbilled_record});
            lv.setAdapter(adapter);*/

            show_subdiv_code.setText(":" + subdivisioncode);
            show_mr_count.setText(":" + totalmr + "");
            show_total_records.setText(":" + (pd_billed_records + d_billed_records)  + "");
            Log.d("ARRAY LENGTH", "LENGTH" + totalmr);

            show_download_count.setText(":" + total_download + "");
            //total_download = 0;
            show_billed_count.setText(":" + total_billed + "");
            //total_billed = 0;
            show_unbilled_count.setText(":" + total_unbilled + "");
            //total_unbilled = 0;
            show_date.setText(":" + date);
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
