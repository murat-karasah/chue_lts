package com.marun.chue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Test extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mCostomerDatabase;
    private String userId;
    private String chars;
    RadioGroup radioAsk1,radioAsk2,radioAsk3,radioAsk4,radioAsk5,
    radioAsk6,radioAsk7,radioAsk8,radioAsk9,radioAsk10,radioAsk11,radioAsk12,radioAsk13,radioAsk14,radioAsk15,radioAsk16,radioAsk17,radioAsk18,radioAsk19,radioAsk20;
    RadioButton selectedradioAsk1,selectedradioAsk2,selectedradioAsk3,selectedradioAsk4,selectedradioAsk5,selectedradioAsk6,selectedradioAsk7,selectedradioAsk8,selectedradioAsk9,
            selectedradioAsk10,selectedradioAsk11,selectedradioAsk12,selectedradioAsk13,selectedradioAsk14,selectedradioAsk15,selectedradioAsk16,selectedradioAsk17,selectedradioAsk18,selectedradioAsk19,
            selectedradioAsk20;
    Button userAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mCostomerDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        userAdd = (Button) findViewById(R.id.userAdd);
        radioAsk1 = (RadioGroup) findViewById(R.id.radioAsk1);
        radioAsk2 = (RadioGroup) findViewById(R.id.radioAsk2);
        radioAsk3 = (RadioGroup) findViewById(R.id.radioAsk3);
        radioAsk4 = (RadioGroup) findViewById(R.id.radioAsk4);
        radioAsk5 = (RadioGroup) findViewById(R.id.radioAsk5);
        radioAsk6 = (RadioGroup) findViewById(R.id.radioAsk6);
        radioAsk7 = (RadioGroup) findViewById(R.id.radioAsk7);
        radioAsk8 = (RadioGroup) findViewById(R.id.radioAsk8);
        radioAsk9 = (RadioGroup) findViewById(R.id.radioAsk9);
        radioAsk10 = (RadioGroup) findViewById(R.id.radioAsk10);
        radioAsk11 = (RadioGroup) findViewById(R.id.radioAsk11);
        radioAsk12 = (RadioGroup) findViewById(R.id.radioAsk12);
        radioAsk13 = (RadioGroup) findViewById(R.id.radioAsk13);
        radioAsk14 = (RadioGroup) findViewById(R.id.radioAsk14);
        radioAsk15 = (RadioGroup) findViewById(R.id.radioAsk15);
        radioAsk16 = (RadioGroup) findViewById(R.id.radioAsk16);
        radioAsk17 = (RadioGroup) findViewById(R.id.radioAsk17);
        radioAsk18 = (RadioGroup) findViewById(R.id.radioAsk18);
        radioAsk19 = (RadioGroup) findViewById(R.id.radioAsk19);
        radioAsk20 = (RadioGroup) findViewById(R.id.radioAsk20);

        userAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedradioAsk1  = (RadioButton)findViewById(radioAsk1.getCheckedRadioButtonId());
                selectedradioAsk2  = (RadioButton)findViewById(radioAsk2.getCheckedRadioButtonId());
                selectedradioAsk3  = (RadioButton)findViewById(radioAsk3.getCheckedRadioButtonId());
                selectedradioAsk4  = (RadioButton)findViewById(radioAsk4.getCheckedRadioButtonId());
                selectedradioAsk5  = (RadioButton)findViewById(radioAsk5.getCheckedRadioButtonId());
                selectedradioAsk6  = (RadioButton)findViewById(radioAsk6.getCheckedRadioButtonId());
                selectedradioAsk7  = (RadioButton)findViewById(radioAsk7.getCheckedRadioButtonId());
                selectedradioAsk8  = (RadioButton)findViewById(radioAsk8.getCheckedRadioButtonId());
                selectedradioAsk9  = (RadioButton)findViewById(radioAsk9.getCheckedRadioButtonId());
                selectedradioAsk10  = (RadioButton)findViewById(radioAsk10.getCheckedRadioButtonId());
                selectedradioAsk11  = (RadioButton)findViewById(radioAsk11.getCheckedRadioButtonId());
                selectedradioAsk12  = (RadioButton)findViewById(radioAsk12.getCheckedRadioButtonId());
                selectedradioAsk13  = (RadioButton)findViewById(radioAsk13.getCheckedRadioButtonId());
                selectedradioAsk14  = (RadioButton)findViewById(radioAsk14.getCheckedRadioButtonId());
                selectedradioAsk15  = (RadioButton)findViewById(radioAsk15.getCheckedRadioButtonId());
                selectedradioAsk16  = (RadioButton)findViewById(radioAsk16.getCheckedRadioButtonId());
                selectedradioAsk17  = (RadioButton)findViewById(radioAsk17.getCheckedRadioButtonId());
                selectedradioAsk18  = (RadioButton)findViewById(radioAsk18.getCheckedRadioButtonId());
                selectedradioAsk19  = (RadioButton)findViewById(radioAsk19.getCheckedRadioButtonId());
                selectedradioAsk20  = (RadioButton)findViewById(radioAsk20.getCheckedRadioButtonId());
                float yourVoteAsk1=Float.parseFloat(selectedradioAsk1.getText().toString());
                float yourVoteAsk2=Float.parseFloat(selectedradioAsk2.getText().toString());
                float yourVoteAsk3=Float.parseFloat(selectedradioAsk3.getText().toString());
                float yourVoteAsk4=Float.parseFloat(selectedradioAsk4.getText().toString());
                float yourVoteAsk5=Float.parseFloat(selectedradioAsk5.getText().toString());
                float yourVoteAsk6=Float.parseFloat(selectedradioAsk6.getText().toString());
                float yourVoteAsk7=Float.parseFloat(selectedradioAsk7.getText().toString());
                float yourVoteAsk8=Float.parseFloat(selectedradioAsk8.getText().toString());
                float yourVoteAsk9=Float.parseFloat(selectedradioAsk9.getText().toString());
                float yourVoteAsk10=Float.parseFloat(selectedradioAsk10.getText().toString());
                float yourVoteAsk11=Float.parseFloat(selectedradioAsk11.getText().toString());
                float yourVoteAsk12=Float.parseFloat(selectedradioAsk12.getText().toString());
                float yourVoteAsk13=Float.parseFloat(selectedradioAsk13.getText().toString());
                float yourVoteAsk14=Float.parseFloat(selectedradioAsk14.getText().toString());
                float yourVoteAsk15=Float.parseFloat(selectedradioAsk15.getText().toString());
                float yourVoteAsk16=Float.parseFloat(selectedradioAsk16.getText().toString());
                float yourVoteAsk17=Float.parseFloat(selectedradioAsk17.getText().toString());
                float yourVoteAsk18=Float.parseFloat(selectedradioAsk18.getText().toString());
                float yourVoteAsk19=Float.parseFloat(selectedradioAsk19.getText().toString());
                float yourVoteAsk20=Float.parseFloat(selectedradioAsk20.getText().toString());

                float neu= (yourVoteAsk4+((-1)*((-6)+yourVoteAsk9))+yourVoteAsk14+((-1)*((-6)+yourVoteAsk19))); //Neuroticism
                float ext= (yourVoteAsk1+((-1)*((-6)+yourVoteAsk6))+yourVoteAsk11+((-1)*((-6)+yourVoteAsk16))); //Extraversion
                float ope= (yourVoteAsk5+((-1)*((-6)+yourVoteAsk10))+((-1)*((-6)+yourVoteAsk15))+((-1)*((-6)+yourVoteAsk20))); //Openness:
                float agr= (yourVoteAsk2+((-1)*((-6)+yourVoteAsk7))+yourVoteAsk12+((-1)*((-6)+yourVoteAsk17))); //Agreeableness: :
                float con= (yourVoteAsk3+((-1)*((-6)+yourVoteAsk8))+yourVoteAsk13+((-1)*((-6)+yourVoteAsk18))); //Conscientiousness: : :
                float sonuc = neu + ext + ope + agr +con ;
                String sExt = new String("");
                String sNeu = new String("");
                String sOpe = new String("");
                String sAgr = new String("");
                String sCon = new String("");

                if(ext>=11){
                     sExt= "E";
                }
                else{
                     sExt= "I";

                }

                if(ope>=13){
                    sOpe= "N";
                }
                else{
                    sOpe= "S";

                }
                if(agr>=15){
                    sAgr= "F";
                }
                else{
                    sAgr= "T";

                }
                if(con>=11){
                    sCon= "J";
                }
                else{
                    sCon= "P";

                }
                if(neu>=9){
                    sNeu= "-";
                }
                else{
                    sNeu= "+";

                }
               chars = sExt +sOpe+sAgr+ sCon;
                Toast.makeText(Test.this, "Selected Radio Button is:" + sExt +sOpe+sAgr+ sCon , Toast.LENGTH_LONG).show();

                saveUserInfromation();

            }

            private void saveUserInfromation() {
                Map userInfo = new HashMap();
                userInfo.put("Char",chars);
                mCostomerDatabase.updateChildren(userInfo);
                finish();}


        });


    }


}
