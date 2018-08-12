package com.transvision.mbc.values;

import java.io.Serializable;

public class GetSetValues implements Serializable {
    private String login_role, tab_name;
    private String mrcode,mrname,mobileno,deviceid;
    private String counter,receiptcount,receipt_amount,date,mode,approved_flag,latitude,longitude;
    private  String startaddress;
    private String billed_record,download_time,billed_time,downlod_record,unbilled_record,Status;
    public GetSetValues() {
    }

    public String getBilled_record() {
        return billed_record;
    }

    public String getUnbilled_record() {
        return unbilled_record;
    }

    public void setUnbilled_record(String unbilled_record) {
        this.unbilled_record = unbilled_record;
    }

    public void setBilled_record(String billed_record) {
        this.billed_record = billed_record;
    }

    public String getDownload_time() {
        return download_time;
    }

    public void setDownload_time(String download_time) {
        this.download_time = download_time;
    }

    public String getBilled_time() {
        return billed_time;
    }

    public void setBilled_time(String billed_time) {
        this.billed_time = billed_time;
    }

    public String getDownlod_record() {
        return downlod_record;
    }

    public void setDownlod_record(String downlod_record) {
        this.downlod_record = downlod_record;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStartaddress() {
        return startaddress;
    }

    public void setStartaddress(String startaddress) {
        this.startaddress = startaddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getReceiptcount() {
        return receiptcount;
    }

    public void setReceiptcount(String receiptcount) {
        this.receiptcount = receiptcount;
    }

    public String getReceipt_amount() {
        return receipt_amount;
    }

    public void setReceipt_amount(String receipt_amount) {
        this.receipt_amount = receipt_amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getApproved_flag() {
        return approved_flag;
    }

    public void setApproved_flag(String approved_flag) {
        this.approved_flag = approved_flag;
    }

    public String getMrcode() {
        return mrcode;
    }

    public void setMrcode(String mrcode) {
        this.mrcode = mrcode;
    }

    public String getMrname() {
        return mrname;
    }

    public void setMrname(String mrname) {
        this.mrname = mrname;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getLogin_role() {
        return login_role;
    }

    public void setLogin_role(String login_role) {
        this.login_role = login_role;
    }

    public String getTab_name() {
        return tab_name;
    }

    public void setTab_name(String tab_name) {
        this.tab_name = tab_name;
    }
}