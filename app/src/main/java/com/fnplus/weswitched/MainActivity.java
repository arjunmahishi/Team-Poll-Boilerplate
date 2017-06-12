package com.fnplus.weswitched;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference quoteRef;
    FirebaseRecyclerAdapter<PostData, ViewHolder> adapter;
    int voteCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_quotes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        quoteRef = FirebaseDatabase.getInstance().getReference("/quotes/");

        adapter = new FirebaseRecyclerAdapter<PostData, ViewHolder>(
                PostData.class,
                R.layout.card_view_quote,
                ViewHolder.class,
                quoteRef ) {
            @Override
            protected void populateViewHolder(final ViewHolder viewHolder, final PostData model, final int position) {
                viewHolder.quote.setText(model.quote);
                if(model.category.equals("Category (optional)")){
                    viewHolder.category.setText("Category not specified");
                }else{
                    viewHolder.category.setText(model.category);
                }

                viewHolder.vote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance()
                                .getReference("/quotes/" + adapter.getRef(position).getKey() + "/votes")
                                .setValue(model.votes + 1);
                    }
                });

                viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openComments(adapter.getRef(position).getKey());
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            openPostActivity(view);
            }
        });
    }

    private void openPostActivity(View view){
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView quote, category;
        Button vote, comment;

        public ViewHolder(View v) {
            super(v);
            quote = (TextView) v.findViewById(R.id.card_quote);
            category = (TextView) v.findViewById(R.id.card_category);
            vote = (Button) v.findViewById(R.id.card_vote);
            comment = (Button) v.findViewById(R.id.card_comment);
        }
    }

    private void openComments(String keyString){
        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra("key", keyString);
        startActivity(intent);
    }

}
