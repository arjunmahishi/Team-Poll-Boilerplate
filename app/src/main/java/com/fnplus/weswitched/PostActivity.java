package com.fnplus.weswitched;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostActivity extends AppCompatActivity {

    Spinner category;
    TextView quote;
    Button submit;
    PostData data;
    FirebaseAuth auth;
    FirebaseDatabase quoteDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        quote = (TextView) findViewById(R.id.post_quote);
        submit = (Button) findViewById(R.id.post_submit);
        category = (Spinner) findViewById(R.id.post_spinner);

        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(
                this, R.array.catagory, R.layout.support_simple_spinner_dropdown_item);

        category.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(validateForm()){
                        data = new PostData(currUser.getDisplayName(), quote.getText().toString(), category.getSelectedItem().toString());
                        quoteDB.getInstance().getReference("/quotes/"
                                + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.getDefault())
                                .format(new Date()).replace(".", ""))
                                .setValue(data)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PostActivity.this, "Unable to post at the moment. Try again"
                                                , Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        quote.setText("");
                                        Toast.makeText(PostActivity.this, "Quote posted successfully"
                                                , Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                    }else{
                        Toast.makeText(PostActivity.this, "Your quote should be bigger",
                                Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validateForm(){
        if(quote.getText().toString().equals("")){
            return false;
        }
        return true;
    }
}
