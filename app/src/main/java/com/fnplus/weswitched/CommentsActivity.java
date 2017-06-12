package com.fnplus.weswitched;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommentsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference commentRef;
    FirebaseRecyclerAdapter<CommentData, CommentViewHolder> adapter;
    EditText commentInput;
    ImageButton sendBtn;
    CommentData newData;
    FirebaseUser currUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);

        recyclerView = (RecyclerView) findViewById(R.id.comments_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        commentRef = FirebaseDatabase.getInstance().getReference(
                "/quotes/"
                + getIntent().getStringExtra("key")
                + "/comments");

        adapter = new FirebaseRecyclerAdapter<CommentData, CommentViewHolder>(
                CommentData.class,
                R.layout.card_view_comment,
                CommentViewHolder.class,
                commentRef) {
            @Override
            protected void populateViewHolder(CommentViewHolder viewHolder, CommentData model, int position) {
                viewHolder.username.setText(model.username);
                viewHolder.message.setText(model.message);
            }
        };

        recyclerView.setAdapter(adapter);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currUser = FirebaseAuth.getInstance().getCurrentUser();
                newData = new CommentData(
                        currUser.getDisplayName(),
                        currUser.getPhotoUrl().toString(),
                        commentInput.getText().toString());
                commentRef.child("comm"
                        + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss",
                        Locale.getDefault())
                        .format(new Date()).replace(".", ""))
                        .setValue(newData);
            }
        });

    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView username, message;

        public CommentViewHolder(View v) {
            super(v);
            username = (TextView) v.findViewById(R.id.comment_username);
            message = (TextView) v.findViewById(R.id.comment_message);
        }
    }
}
