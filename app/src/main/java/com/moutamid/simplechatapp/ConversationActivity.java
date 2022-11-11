package com.moutamid.simplechatapp;

import static com.moutamid.simplechatapp.R.id.recyclerviewConvo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class ConversationActivity extends AppCompatActivity {
    private static final int LEFT_MESSAGE_LAYOUT = 1;
    private static final int RIGHT_MESSAGE_LAYOUT = 2;
    private static final int IMAGE_LAYOUT = 3;

    EditText editText;
    TextView countTextView;
    ImageView sendBtn;
    boolean isSend = false;
    String currentImagePath = "";
    String PARAMS;
    private DBHandler dbHandler;
    int selectionCount = 0;
    private ArrayList<MessageModel> messagesModelArrayList = new ArrayList<>();
    private ArrayList<MessageModel> messagesModelArrayListAll = new ArrayList<>();

    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterMessages adapter;

    boolean isMultiple = false;

    String MESSAGES = "messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Objects.requireNonNull(getSupportActionBar()).hide();
        PARAMS = getIntent().getStringExtra(Constants.PARAMS);

        TextView topTextView = findViewById(R.id.topi);
        topTextView.setText(PARAMS);
        MESSAGES += PARAMS;
        Stash.put("db", PARAMS);

        // creating a new dbhandler class
        // and passing our context to it.
        dbHandler = new DBHandler(ConversationActivity.this);

        findViewById(R.id.deleteBtn).setOnClickListener(v -> {
            ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Deleting...");
            progressDialog.show();
            isMultiple = false;
//            for (int i = 0; i < messagesModelArrayList.size(); i++) {
            /*for (Iterator<MessageModel> it = messagesModelArrayList.iterator(); it.hasNext(); ) {
                if (it.next().isSelected) {
                    dbHandler.deleteMessage(it.next().message);
                    Toast.makeText(this, it.next().message, Toast.LENGTH_SHORT).show();
                    it.remove();
                }
            }*/
            for (int i = messagesModelArrayList.size() - 1; i >= 0; i--) {
                if (messagesModelArrayList.get(i).isSelected) {
                    dbHandler.deleteMessage(messagesModelArrayList.get(i).message);
                    messagesModelArrayList.remove(i);
                }
            }
            for (int i = 0; i < messagesModelArrayList.size(); i++) {
                if (messagesModelArrayList.get(i).isSelected) {
                    messagesModelArrayList.get(i).isSelected = false;
                }
            }
            countTextView.setText("0");
            selectionCount = 0;
            findViewById(R.id.actionBar).setVisibility(View.VISIBLE);
            findViewById(R.id.deleteBar).setVisibility(View.GONE);
            isMultiple = false;
            adapter.notifyDataSetChanged();
            Stash.put(MESSAGES, messagesModelArrayList);

            progressDialog.dismiss();
        });

        findViewById(R.id.menuBtn).setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(ConversationActivity.this, v);
            popupMenu.getMenuInflater().inflate(
                    R.menu.popup_menu,
                    popupMenu.getMenu()
            );
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.search) {
                        findViewById(R.id.actionBar).setVisibility(View.GONE);
                        findViewById(R.id.searchBar).setVisibility(View.VISIBLE);

                    }
                    if (menuItem.getItemId() == R.id.delete_multiple) {
                        findViewById(R.id.actionBar).setVisibility(View.GONE);
                        findViewById(R.id.deleteBar).setVisibility(View.VISIBLE);
                        isMultiple = true;
                    }

                    return true;
                }
            });
            popupMenu.show();
        });

        findViewById(R.id.backBtn).setOnClickListener(v -> {
            finish();
        });
        findViewById(R.id.backBtnSearchBar).setOnClickListener(v -> {
            findViewById(R.id.actionBar).setVisibility(View.VISIBLE);
            findViewById(R.id.searchBar).setVisibility(View.GONE);
        });
        findViewById(R.id.backBtnDeleteBar).setOnClickListener(v -> {
            findViewById(R.id.actionBar).setVisibility(View.VISIBLE);
            findViewById(R.id.deleteBar).setVisibility(View.GONE);
            isMultiple = false;
            adapter.notifyDataSetChanged();
        });

        EditText searchEdittext = findViewById(R.id.searchBarEditText);
        searchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        messagesModelArrayList.add(new MessageModel("message", LEFT_MESSAGE_LAYOUT, ""));
//        messagesModelArrayList.add(new MessageModel("message", LEFT_MESSAGE_LAYOUT, ""));
//        messagesModelArrayList.add(new MessageModel("message", LEFT_MESSAGE_LAYOUT, ""));
//        messagesModelArrayList.add(new MessageModel("message", LEFT_MESSAGE_LAYOUT, ""));
//        messagesModelArrayList.add(new MessageModel("message", RIGHT_MESSAGE_LAYOUT, ""));
//        messagesModelArrayList.add(new MessageModel("message", RIGHT_MESSAGE_LAYOUT, ""));
//        messagesModelArrayList.add(new MessageModel("message", RIGHT_MESSAGE_LAYOUT, ""));
//        messagesModelArrayList.add(new MessageModel("message", RIGHT_MESSAGE_LAYOUT, ""));

        editText = findViewById(R.id.edittext);
        sendBtn = findViewById(R.id.sendBtn);
        countTextView = findViewById(R.id.delete_count);

        try {
            messagesModelArrayList = dbHandler.readAllMessages();
            messagesModelArrayListAll = dbHandler.readAllMessages();
            messagesModelArrayList = Stash.getArrayList(MESSAGES, MessageModel.class);
            messagesModelArrayListAll = Stash.getArrayList(MESSAGES, MessageModel.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        sendBtn.setOnClickListener(v -> {
            if (isSend) {
                String message = editText.getText().toString();

                if (message.isEmpty())
                    return;

                Stash.put(PARAMS, message);

                MessageModel messageModel = new MessageModel(messagesModelArrayList.size(), message, RIGHT_MESSAGE_LAYOUT, "");

                messagesModelArrayList.add(messageModel);
                dbHandler.addMessage(messageModel);

                adapter.notifyItemInserted(messagesModelArrayList.size() - 1);
                conversationRecyclerView.smoothScrollToPosition(messagesModelArrayList.size() + 1);
                Stash.put(MESSAGES, messagesModelArrayList);
                editText.setText("");
            } else {
                Dialog dialog = new Dialog(ConversationActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog);
                dialog.setCancelable(true);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                EditText editTextd = dialog.findViewById(R.id.edittextdialog);
                ImageView imageView = dialog.findViewById(R.id.imagedialog);
                AppCompatButton button = dialog.findViewById(R.id.sendBtndialog);

                button.setOnClickListener(v1 -> {
                    String msg = editTextd.getText().toString();

                    if (msg.isEmpty() || currentImagePath.isEmpty())
                        return;

                    Stash.put(PARAMS, msg);

                    MessageModel messageModel = new MessageModel(messagesModelArrayList.size(), msg, IMAGE_LAYOUT, currentImagePath);

                    messagesModelArrayList.add(messageModel);
                    dbHandler.addMessage(messageModel);

                    adapter.notifyItemInserted(messagesModelArrayList.size() - 1);

                    dialog.dismiss();
                    conversationRecyclerView.smoothScrollToPosition(messagesModelArrayList.size() + 1);
                    Stash.put(MESSAGES, messagesModelArrayList);

                    currentImagePath = "";
                });

                imageView.setOnClickListener(v1 -> {
                    Album.image(this)
                            .singleChoice()
                            .onResult(result -> {
//                                Toast.makeText(ConversationActivity.this, result.get(0).getPath() + "", Toast.LENGTH_SHORT).show();
                                Glide.with(getApplicationContext())
                                        .load(result.get(0).getPath())
                                        .error(R.color.red)
                                        .placeholder(R.color.grey)
                                        .into(imageView);
                                currentImagePath = result.get(0).getPath();
                            })
                            .onCancel(result -> {
                                Toast.makeText(ConversationActivity.this, result + "", Toast.LENGTH_SHORT).show();
                            })
                            .start();
                });

                dialog.show();
                dialog.getWindow().setAttributes(layoutParams);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    isSend = false;
                    sendBtn.setImageResource(R.drawable.attachment);
                } else {
                    isSend = true;
                    sendBtn.setImageResource(R.drawable.send);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyboardShown(editText.getRootView())) {
                    Log.d("keyboard", "keyboard UP");

                    if (keyboardUp == false) {
                        if (messagesModelArrayList.size() > 0)
                            conversationRecyclerView.smoothScrollToPosition(messagesModelArrayList.size() + 1);
                        keyboardUp = true;
                    }

                } else {
                    Log.d("keyboard", "keyboard Down");
                    keyboardUp = false;
                }
            }
        });

        initRecyclerView();

        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new MediaLoader())
                .build());
    }

    public void shareImage(String message, String path) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.parse(path);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, message);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        startActivity(Intent.createChooser(sharingIntent, "Share..."));
    }

    public void shareText(String message) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(sharingIntent, "Share..."));
    }

    public class MediaLoader implements AlbumLoader {

        @Override
        public void load(ImageView imageView, AlbumFile albumFile) {
            load(imageView, albumFile.getPath());
        }

        @Override
        public void load(ImageView imageView, String url) {
            Glide.with(imageView.getContext())
                    .load(url)
                    .error(R.color.red)
                    .placeholder(R.color.grey)
                    .into(imageView);
        }
    }

    private boolean keyboardUp = false;

    private boolean keyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    private void initRecyclerView() {

        conversationRecyclerView = findViewById(recyclerviewConvo);
//        conversationRecyclerView.addItemDecoration(new DividerItemDecoration(conversationRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
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
//        conversationRecyclerView.scrollToPosition(conversationRecyclerView.getAdapter().getItemCount() - 1);
        conversationRecyclerView.scrollToPosition(messagesModelArrayList.size() + 1);

        //    if (adapter.getItemCount() != 0) {

        //        noChatsLayout.setVisibility(View.GONE);
        //        chatsRecyclerView.setVisibility(View.VISIBLE);

        //    }

    }

    private class RecyclerViewAdapterMessages extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    ArrayList<MessageModel> filteredList = new ArrayList<>();

                    if (constraint.length() == 0) {
                        filteredList.addAll(messagesModelArrayListAll);
                    } else {
                        String filterPattern = constraint.toString().toLowerCase().trim();

                        for (MessageModel item : messagesModelArrayListAll) {
                            if (item.message != null)
                                if (item.message.toLowerCase().contains(filterPattern)) {
                                    filteredList.add(item);
                                }
                        }

                    }

                    FilterResults results = new FilterResults();
                    results.values = filteredList;

                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    messagesModelArrayList.clear();
                    messagesModelArrayList.addAll((ArrayList<MessageModel>) filterResults.values);
                    notifyDataSetChanged();

                }
            };

        }

        @Override
        public int getItemViewType(int position) {
            if (messagesModelArrayList.get(position).sent_by == IMAGE_LAYOUT) {
                return IMAGE_LAYOUT;

            } else if (messagesModelArrayList.get(position).sent_by == RIGHT_MESSAGE_LAYOUT) {
                return RIGHT_MESSAGE_LAYOUT;

            } else
                return LEFT_MESSAGE_LAYOUT;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case IMAGE_LAYOUT:
                    View view3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_video, parent, false);
                    return new ViewHolderImage(view3);
                case LEFT_MESSAGE_LAYOUT:
                    View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_messages_left, parent, false);
                    return new ViewHolderLeftMcg(view1);
                default: //RIGHT_MESSAGE_LAYOUT
                    View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_messages_right, parent, false);
                    return new ViewHolderRightMcg(view2);
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position1) {
            int position = holder.getAdapterPosition();
            MessageModel messageModel = messagesModelArrayList.get(position);

            switch (holder.getItemViewType()) {
                case LEFT_MESSAGE_LAYOUT:
                    ViewHolderLeftMcg viewHolderLeftMcg = (ViewHolderLeftMcg) holder;

                    if (messageModel.isSelected) {
                        viewHolderLeftMcg.selection.setVisibility(View.VISIBLE);
                    } else {
                        viewHolderLeftMcg.selection.setVisibility(View.GONE);
                    }

                    viewHolderLeftMcg.mcg.setText(messageModel.message);
                    viewHolderLeftMcg.parentLeftMessage.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            showEditDelDialog(messageModel, position);
                            return false;
                        }
                    });
                    viewHolderLeftMcg.parentLeftMessage.setOnClickListener(v -> {
                        if (isMultiple) {
                            if (messageModel.isSelected) {
                                viewHolderLeftMcg.selection.setVisibility(View.GONE);
                                messagesModelArrayList.get(position).isSelected = false;
                                selectionCount--;
                            } else {
                                viewHolderLeftMcg.selection.setVisibility(View.VISIBLE);
                                messagesModelArrayList.get(position).isSelected = true;
                                selectionCount++;
                            }
                            countTextView.setText(selectionCount + "");

                        }
                    });
                    break;

                case RIGHT_MESSAGE_LAYOUT:
                    ViewHolderRightMcg viewHolderRightMcg = (ViewHolderRightMcg) holder;
                    if (messageModel.isSelected) {
                        viewHolderRightMcg.selection.setVisibility(View.VISIBLE);
                    } else {
                        viewHolderRightMcg.selection.setVisibility(View.GONE);
                    }
                    viewHolderRightMcg.mcg.setText(messageModel.message);
                    viewHolderRightMcg.parentRightMessage.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            showEditDelDialog(messageModel, position);
                            return false;
                        }
                    });
                    viewHolderRightMcg.parentRightMessage.setOnClickListener(v -> {
                        if (isMultiple) {
                            if (messageModel.isSelected) {
                                viewHolderRightMcg.selection.setVisibility(View.GONE);
                                messagesModelArrayList.get(position).isSelected = false;
                                selectionCount--;
                            } else {
                                viewHolderRightMcg.selection.setVisibility(View.VISIBLE);
                                messagesModelArrayList.get(position).isSelected = true;
                                selectionCount++;
                            }
                            countTextView.setText(selectionCount + "");

                        }
                    });

                    break;
                case IMAGE_LAYOUT:
                    ViewHolderImage viewHolderImage = (ViewHolderImage) holder;
                    if (messageModel.isSelected) {
                        viewHolderImage.selection.setVisibility(View.VISIBLE);
                    } else {
                        viewHolderImage.selection.setVisibility(View.GONE);
                    }
                    viewHolderImage.text.setText(messageModel.message);
                    Glide.with(getApplicationContext())
                            .load(messageModel.image)
                            .error(R.color.red)
                            .placeholder(R.color.grey)
                            .into(viewHolderImage.imageView);

                    viewHolderImage.parent.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            showEditDelDialog(messageModel, position);
                            return false;
                        }
                    });
                    viewHolderImage.parent.setOnClickListener(v -> {
                        if (isMultiple) {
                            if (messageModel.isSelected) {
                                viewHolderImage.selection.setVisibility(View.GONE);
                                messagesModelArrayList.get(position).isSelected = false;
                                selectionCount--;
                            } else {
                                viewHolderImage.selection.setVisibility(View.VISIBLE);
                                messagesModelArrayList.get(position).isSelected = true;
                                selectionCount++;
                            }
                            countTextView.setText(selectionCount + "");
                        }
                    });

                    break;
            }
        }

        @Override
        public int getItemCount() {
            if (messagesModelArrayList == null)
                return 0;
            return messagesModelArrayList.size();
        }

        public class ViewHolderLeftMcg extends RecyclerView.ViewHolder {
            TextView mcg;
            RelativeLayout parentLeftMessage;
            ImageView selection;

            public ViewHolderLeftMcg(View v) {
                super(v);
                mcg = v.findViewById(R.id.mcg_left);
                parentLeftMessage = v.findViewById(R.id.parent_left_message);
                selection = v.findViewById(R.id.selection_left);
            }
        }

        public class ViewHolderRightMcg extends RecyclerView.ViewHolder {
            TextView mcg;
            RelativeLayout parentRightMessage;
            ImageView selection;

            public ViewHolderRightMcg(View v) {
                super(v);
                mcg = v.findViewById(R.id.mcg_right);
                parentRightMessage = v.findViewById(R.id.parent_right_message);
                selection = v.findViewById(R.id.selection_right);
            }
        }

        public class ViewHolderImage extends RecyclerView.ViewHolder {
            ImageView imageView, selection;
            TextView text;
            RelativeLayout parent;

            public ViewHolderImage(View v) {
                super(v);
                imageView = v.findViewById(R.id.messageImageVideolayout);
                text = v.findViewById(R.id.textviewVideolayout);
                parent = v.findViewById(R.id.parent_layout_image);
                selection = v.findViewById(R.id.selection_image);
            }
        }

    }

    private void showEditDelDialog(MessageModel messageModel, int position1) {
        AlertDialog dialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(ConversationActivity.this);
        final CharSequence[] items = {"Edit", "Delete", "Share"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {

                if (position == 0) {//EDIT

                    if (messageModel.image.isEmpty()) {//SIMPLE MESSAGE
                        editSimpleMessage(messageModel, position1);
                    } else {// IMAGE MESSAGE
                        editImageMessage(messageModel, position1);
                    }

                }
                if (position == 1) {//DELETE
                    messagesModelArrayList.remove(position1);
                    messagesModelArrayListAll.remove(position1);
                    adapter.notifyItemRemoved(position1);
                    dbHandler.deleteMessage(messageModel.message);
                    Stash.put(MESSAGES, messagesModelArrayList);
                }
                if (position == 2) {//SHARE
                    if (messageModel.image.isEmpty()) {//SIMPLE MESSAGE
                        shareText(messageModel.message);
                    } else {// IMAGE MESSAGE
                        shareImage(messageModel.message, messageModel.image);
                    }
                    dialog.dismiss();
                }
                //              Toast.makeText(AdminActivityApproveReferrals.this, key + "\n" + items[position], Toast.LENGTH_LONG).show();
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    private void editImageMessage(MessageModel messageModel, int position) {
        Dialog dialog = new Dialog(ConversationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        EditText editTextd = dialog.findViewById(R.id.edittextdialog);
        ImageView imageView = dialog.findViewById(R.id.imagedialog);
        AppCompatButton button = dialog.findViewById(R.id.sendBtndialog);
        button.setText("edit");

        Glide.with(getApplicationContext())
                .load(messageModel.image)
                .error(R.color.red)
                .placeholder(R.color.grey)
                .into(imageView);
        editTextd.setText(messageModel.message);

        button.setOnClickListener(v1 -> {
            String msg = editTextd.getText().toString();

            if (msg.isEmpty() || currentImagePath.isEmpty())
                return;

            messagesModelArrayList.get(position).message = msg;
            messagesModelArrayList.get(position).image = currentImagePath;

            dbHandler.updateMessage(messageModel);

            adapter.notifyItemChanged(position);
            Stash.put(MESSAGES, messagesModelArrayList);

            dialog.dismiss();

            currentImagePath = "";
        });

        imageView.setOnClickListener(v1 -> {
            Album.image(this)
                    .singleChoice()
                    .onResult(result -> {
//                                Toast.makeText(ConversationActivity.this, result.get(0).getPath() + "", Toast.LENGTH_SHORT).show();
                        Glide.with(getApplicationContext())
                                .load(result.get(0).getPath())
                                .error(R.color.red)
                                .placeholder(R.color.grey)
                                .into(imageView);
                        currentImagePath = result.get(0).getPath();
                    })
                    .onCancel(result -> {
                        Toast.makeText(ConversationActivity.this, result + "", Toast.LENGTH_SHORT).show();
                    })
                    .start();
        });

        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);
    }

    private void editSimpleMessage(MessageModel messageModel, int position) {
        Dialog dialog = new Dialog(ConversationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edittext);
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        EditText editTextd = dialog.findViewById(R.id.edittextsimpledialog);
        AppCompatButton button = dialog.findViewById(R.id.sendBtnsimpledialog);

        editTextd.setText(messageModel.message);

        button.setOnClickListener(v -> {
            String msg = editTextd.getText().toString();

            if (msg.isEmpty())
                return;

            messagesModelArrayList.get(position).message = msg;

            dbHandler.updateMessage(messageModel);

            adapter.notifyItemChanged(position);
            Stash.put(MESSAGES, messagesModelArrayList);

            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);

    }


}