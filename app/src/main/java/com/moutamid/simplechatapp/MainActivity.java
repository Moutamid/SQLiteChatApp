package com.moutamid.simplechatapp;

import static android.view.LayoutInflater.from;
import static com.moutamid.simplechatapp.R.id.parent_layout;
import static com.moutamid.simplechatapp.R.id.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxn.stash.Stash;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<ChatModel> tasksArrayList = Stash.getArrayList("list", ChatModel.class);

    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterMessages adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Stash.getBoolean("isFirstTime", true)) {
            Stash.put("isFirstTime", false);

            tasksArrayList.clear();
            tasksArrayList.add(new ChatModel(Constants.Dentist, Stash.getString(Constants.Dentist, Constants.Dentist), R.drawable.dentist));

            Stash.put("list", tasksArrayList);

        }


        findViewById(R.id.addChatBtn).setOnClickListener(v -> {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_new_chat);
            dialog.setCancelable(true);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

            EditText editTextd = dialog.findViewById(R.id.edittextdialognewchat);
            AppCompatButton button = dialog.findViewById(R.id.addBtndialog);
            RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);

            button.setOnClickListener(v1 -> {

                String name = editTextd.getText().toString();
                int drawable = R.drawable.dentist;

                if (name.isEmpty())
                    return;

                if (radioGroup.getCheckedRadioButtonId() == R.id.radioBtnDentist)
                    drawable = R.drawable.dentist;

                if (radioGroup.getCheckedRadioButtonId() == R.id.radioBtnDinosaur)
                    drawable = R.drawable.dinosaur;

                if (radioGroup.getCheckedRadioButtonId() == R.id.radioBtnPirate)
                    drawable = R.drawable.pirate;

                if (radioGroup.getCheckedRadioButtonId() == R.id.radioBtnPrincess)
                    drawable = R.drawable.princess;

                tasksArrayList.add(new ChatModel(name, name, drawable));
                adapter.notifyDataSetChanged();
                Stash.put("list", tasksArrayList);
                dialog.dismiss();

            });

            dialog.show();
            dialog.getWindow().setAttributes(layoutParams);

            /*if (tasksArrayList.size() == 1) {
                tasksArrayList.add(new ChatModel(Constants.Dinosaur, Stash.getString(Constants.Dinosaur, Constants.Dinosaur), R.drawable.dinosaur));
                adapter.notifyDataSetChanged();
                Stash.put("list", tasksArrayList);

            }

            if (tasksArrayList.size() == 2) {
                tasksArrayList.add(new ChatModel(Constants.Pirate, Stash.getString(Constants.Pirate, Constants.Pirate), R.drawable.pirate));
                adapter.notifyDataSetChanged();
                Stash.put("list", tasksArrayList);

            }

            if (tasksArrayList.size() == 3) {
                tasksArrayList.add(new ChatModel(Constants.Princess, Stash.getString(Constants.Princess, Constants.Princess), R.drawable.princess));
                adapter.notifyDataSetChanged();
                Stash.put("list", tasksArrayList);

            }
*/

        });

        initRecyclerView();

    }

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
            ChatModel model = tasksArrayList.get(holder.getAdapterPosition());

            holder.name.setText(model.name);

            holder.lastMessage.setText(Stash.getString(model.name, model.name));

            holder.imageView.setImageResource(model.image);

            holder.parentLayout.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, ConversationActivity.class)
                        .putExtra(Constants.PARAMS, model.name));
            });

            holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Are you sure?")
                            .setMessage("Do you really want to delete this chat?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                tasksArrayList.remove(holder.getAdapterPosition());
                                adapter.notifyDataSetChanged();
                                Stash.put("list", tasksArrayList);
                                dialog.dismiss();
                            })
                            .setNegativeButton("No", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();

                    return false;
                }
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
        /*tasksArrayList.clear();
        tasksArrayList = Stash.getArrayList("list", ChatModel.class);
        initRecyclerView();*/
        adapter.notifyDataSetChanged();
    }
}