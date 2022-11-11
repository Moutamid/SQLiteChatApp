package com.moutamid.simplechatapp;

import static android.view.LayoutInflater.from;
import static com.moutamid.simplechatapp.R.id.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxn.stash.Stash;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tasksArrayList.clear();

        tasksArrayList.add(new ChatModel(Constants.Dentist, Stash.getString(Constants.Dentist, Constants.Dentist), R.drawable.dentist));
        tasksArrayList.add(new ChatModel(Constants.Dinosaur, Stash.getString(Constants.Dinosaur, Constants.Dinosaur), R.drawable.dinosaur));
        tasksArrayList.add(new ChatModel(Constants.Pirate, Stash.getString(Constants.Pirate, Constants.Pirate), R.drawable.pirate));
        tasksArrayList.add(new ChatModel(Constants.Princess, Stash.getString(Constants.Princess, Constants.Princess), R.drawable.princess));

        initRecyclerView();

    }

    private ArrayList<ChatModel> tasksArrayList = new ArrayList<>();

    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterMessages adapter;

    private void initRecyclerView() {

        conversationRecyclerView = findViewById(recyclerview);
        conversationRecyclerView.addItemDecoration(new DividerItemDecoration(conversationRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new RecyclerViewAdapterMessages();
        //        LinearLayoutManager layoutManagerUserFriends = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        //    int numberOfColumns = 3;
        //int mNoOfColumns = calculateNoOfColumns(getApplicationContext(), 50);
        //  recyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        conversationRecyclerView.setLayoutManager(linearLayoutManager);
        conversationRecyclerView.setHasFixedSize(true);
        conversationRecyclerView.setNestedScrollingEnabled(false);

        conversationRecyclerView.setAdapter(adapter);

        //    if (adapter.getItemCount() != 0) {

        //        noChatsLayout.setVisibility(View.GONE);
        //        chatsRecyclerView.setVisibility(View.VISIBLE);

        //    }

    }

    private class RecyclerViewAdapterMessages extends Adapter
            <RecyclerViewAdapterMessages.ViewHolderRightMessage> {

        @NonNull
        @Override
        public ViewHolderRightMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = from(parent.getContext()).inflate(R.layout.layout_item_chat, parent, false);
            return new ViewHolderRightMessage(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolderRightMessage holder, int position) {
            ChatModel model = tasksArrayList.get(position);

            holder.name.setText(model.name);
            holder.lastMessage.setText(model.last_message);
            holder.imageView.setImageResource(model.image);

            holder.parentLayout.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, ConversationActivity.class)
                        .putExtra(Constants.PARAMS, model.name));
            });

        }

        @Override
        public int getItemCount() {
            if (tasksArrayList == null)
                return 0;
            return tasksArrayList.size();
        }

        public class ViewHolderRightMessage extends ViewHolder {

            TextView name, lastMessage;
            ImageView imageView;
            RelativeLayout parentLayout;

            public ViewHolderRightMessage(@NonNull View v) {
                super(v);
                name = v.findViewById(R.id.name);
                lastMessage = v.findViewById(R.id.last_message);
                imageView = v.findViewById(R.id.imagee);
                parentLayout = v.findViewById(R.id.parent_layout);

            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        tasksArrayList.clear();
        tasksArrayList.add(new ChatModel(Constants.Dentist, Stash.getString(Constants.Dentist, Constants.Dentist), R.drawable.dentist));
        tasksArrayList.add(new ChatModel(Constants.Dinosaur, Stash.getString(Constants.Dinosaur, Constants.Dinosaur), R.drawable.dinosaur));
        tasksArrayList.add(new ChatModel(Constants.Pirate, Stash.getString(Constants.Pirate, Constants.Pirate), R.drawable.pirate));
        tasksArrayList.add(new ChatModel(Constants.Princess, Stash.getString(Constants.Princess, Constants.Princess), R.drawable.princess));

        initRecyclerView();
    }
}