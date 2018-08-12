package com.transvision.mbc;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.transvision.mbc.adapters.RoleAdapter;
import com.transvision.mbc.values.FunctionsCall;
import com.transvision.mbc.values.GetSetValues;

import java.util.ArrayList;
/**
 * Created by Sourav
 */
public class SubdivisionActivity extends AppCompatActivity {
    Spinner role_spinner;
    ArrayList<GetSetValues> roles_list;
    RoleAdapter roleAdapter;
    GetSetValues getSetValues;
    FunctionsCall fcall;
    Button login_btn;
    String main_role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subdivision);
        initialize();

        for (int i = 0; i < getResources().getStringArray(R.array.subdivision_values).length; i++) {
            getSetValues = new GetSetValues();
            getSetValues.setLogin_role(getResources().getStringArray(R.array.subdivision_values)[i]);
            roles_list.add(getSetValues);
            roleAdapter.notifyDataSetChanged();
        }
        role_spinner.setSelection(0);
        role_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tvrole = (TextView) findViewById(R.id.spinner_txt);
                String role = tvrole.getText().toString();
                if (!role.equals("--SELECT--"))
                {
                    main_role = role;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (main_role.equals("AEE")||main_role.equals("AO")||main_role.equals("AOO")||main_role.equals("JAO")||main_role.equals("CW"))
                        {
                            Intent intent = new Intent(SubdivisionActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                },1000L);
            }
        });
    }

    public void initialize()
    {
        role_spinner = (Spinner) findViewById(R.id.login_users_spin);
        roles_list = new ArrayList<>();
        roleAdapter = new RoleAdapter(roles_list, this);
        role_spinner.setAdapter(roleAdapter);
        login_btn = (Button) findViewById(R.id.login_btn);
        fcall = new FunctionsCall();
    }
}
