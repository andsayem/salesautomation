package com.ssgbd.salesautomation.adapters;

import static android.view.View.GONE;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.ssgbd.salesautomation.OfferImageShowActivity;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.fragment.SmartOrderManageFragment;
import com.ssgbd.salesautomation.dtos.SmartOrderDTO;
import com.ssgbd.salesautomation.utils.Utility;
import com.ssgbd.salesautomation.visit.OrderActivity;

import java.io.IOException;
import java.util.ArrayList;

public class SmartManageOrderListRecyclerAdapter extends RecyclerView.Adapter<SmartManageOrderListRecyclerAdapter.NewReleasesItemViewHolder> {

    private ArrayList<SmartOrderDTO> orderList;
    private ArrayList<SmartOrderDTO> arrayList;
    private Context context;
    private MediaPlayer mp;
    private int currentPlayingPosition = -1;
    SmartOrderManageFragment smartOrderManageFragment;
    public SmartManageOrderListRecyclerAdapter(ArrayList<SmartOrderDTO> items, Context context,SmartOrderManageFragment smartOrderManageFragment) {
        this.orderList = items;
        this.context = context;
        this.arrayList = new ArrayList<>(orderList);
        this.smartOrderManageFragment = smartOrderManageFragment;
        this.mp = new MediaPlayer();
    }

    @NonNull
    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_manage_smart_order, parent, false);
        return new NewReleasesItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewReleasesItemViewHolder holder, final int position) {
        final SmartOrderDTO item = orderList.get(position);

        // Load image with Picasso
        Picasso.with(context)
                .load(item.getImage())
                .placeholder(R.mipmap.list_placeholder).into(holder.row_smart_image);

        holder.row_smart_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OfferImageShowActivity.class);
                intent.putExtra("imagelink",item.getImage());

                context.startActivity(intent);
            }
        });
        holder.row_retailer_name.setText("Name: " + item.getRetailer_name());
        holder.row_retailer_phone.setText("Phone: " + item.getRetailer_phone());
        holder.row_text.setText("Order: " + item.getText());
        holder.row_order_status.setText( item.getOrder_status());
        holder.row_retailer_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:"+item.getRetailer_phone()));
                context.startActivity(dialIntent);
            }
        });
        holder.row_route_name.setText("Route: " + item.getRoute_name());
        holder.row_btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utility.ROUTE_ID = item.getRoute_id();
                Intent intent = new Intent(context, OrderActivity.class);
                intent.putExtra("retailerId",item.getRetailer_id());
                intent.putExtra("retailerName",item.getRetailer_name());
                intent.putExtra("poient_id",item.getPoint_id());
                intent.putExtra("imagelink",item.getImage());
                intent.putExtra("voice",item.getVoice());
                intent.putExtra("smartOrderId",item.getId());

                intent.putExtra("from","smartorder");
                context.startActivity(intent);
            }
        }); holder.row_btn_acknowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              smartOrderManageFragment.getSmartOrderStatus(item.getId());
            }
        });


        String voice = item.getVoice();
        if (voice == null || voice.trim().isEmpty() || voice.trim().equalsIgnoreCase("null")) {
            holder.row_image_sound.setVisibility(View.GONE);
        } else {
            holder.row_image_sound.setVisibility(View.VISIBLE);
        }

        // Set play button click listener
        holder.row_image_sound.setOnClickListener(v -> {
            if (currentPlayingPosition == position && mp.isPlaying()) {
                pauseAudio();
            } else {
                playAudio(item.getVoice(), position);
            }
            updatePlayButtonIcon(holder.row_image_sound, position);
        });

        // Set stop button click listener
        holder.row_sound_stop.setOnClickListener(v -> {
            stopAudio();
            updatePlayButtonIcon(holder.row_image_sound, -1);
        });

        // Update play button icon based on current state
        updatePlayButtonIcon(holder.row_image_sound, position);
    }

    private void playAudio(String audioUrl, int position) {
//        try {
//            mp.reset();
//
//            // Configure audio attributes
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                AudioAttributes attributes = new AudioAttributes.Builder()
//                        .setUsage(AudioAttributes.USAGE_MEDIA)
//                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                        .build();
//                mp.setAudioAttributes(attributes);
//            } else {
//                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            }
//
//            mp.setDataSource(audioUrl);
//            mp.prepareAsync();
//
//            mp.setOnPreparedListener(mp -> {
//                currentPlayingPosition = position;
//                mp.start();
//                notifyDataSetChanged(); // Update all play buttons
//            });
//
//            mp.setOnCompletionListener(mp -> {
//                currentPlayingPosition = -1;
//                notifyDataSetChanged();
//            });
//
//            mp.setOnErrorListener((mp, what, extra) -> {
//                Log.e("MediaPlayer", "Error what=" + what + " extra=" + extra);
//                currentPlayingPosition = -1;
//                notifyDataSetChanged();
//                return true;
//            });
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//

        try {
            // Release previous MediaPlayer if exists
            if (mp != null) {
                mp.release();
                mp = null;
            }

            // Create new MediaPlayer instance
            mp = new MediaPlayer();

            // Configure for AAC playback
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                        .build();
                mp.setAudioAttributes(attributes);
            } else {
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }

            // Enable streaming mode for network sources
            mp.setDataSource(audioUrl);

            // Prepare asynchronously to avoid UI freezing
            mp.prepareAsync();

            mp.setOnPreparedListener(mp -> {
                currentPlayingPosition = position;
                mp.start();
                notifyDataSetChanged();
            });

            mp.setOnCompletionListener(mp -> {
                currentPlayingPosition = -1;
                notifyDataSetChanged();
            });

            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // Handle error (404 or others)
                    if (what == MediaPlayer.MEDIA_ERROR_IO) {
                        // Network or file error
                    }
                    return true; // Error was handled
                }
            });

            mp.setOnInfoListener((mp, what, extra) -> {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    Log.d("MediaPlayer", "Buffering started");
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    Log.d("MediaPlayer", "Buffering ended");
                }
                return false;
            });

        } catch (IOException e) {
            Log.e("MediaPlayer", "Error setting data source", e);
            currentPlayingPosition = -1;
            notifyDataSetChanged();
            Toast.makeText(context, "Error loading audio", Toast.LENGTH_SHORT).show();
        }

    }

    private void tryAlternativeAudioPlayback(String audioUrl, int position) {
        // Alternative approach using Intent to use system's default player
        if (audioUrl.startsWith("http") || audioUrl.startsWith("https")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(audioUrl), "audio/*");
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No app available to play audio", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void pauseAudio() {
        if (mp.isPlaying()) {
            mp.pause();
            currentPlayingPosition = -1;
        }
    }

    private void stopAudio() {
        mp.reset();
        currentPlayingPosition = -1;
    }

    private void updatePlayButtonIcon(ImageView playButton, int position) {
        if (position == currentPlayingPosition && mp.isPlaying()) {
            playButton.setImageResource(R.mipmap.sound); // Your pause icon
        } else {
            playButton.setImageResource(R.mipmap.sound); // Your play icon
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        orderList.clear();
        if (charText.isEmpty()) {
            orderList.addAll(arrayList);
        } else {
            for (SmartOrderDTO item : arrayList) {
                if (item.getRetailer_name().toLowerCase().contains(charText)) {
                    orderList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }



    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder {
        public TextView row_retailer_name, row_retailer_phone, row_route_name,row_text, row_sound_stop,row_order_status;
        public ImageView row_smart_image, row_image_sound;
        public Button row_btn_acknowledge,row_btn_order;

        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_smart_image = itemView.findViewById(R.id.row_smart_image);
            row_image_sound = itemView.findViewById(R.id.row_image_sound);
            row_retailer_name = itemView.findViewById(R.id.row_retailer_name);
            row_text = itemView.findViewById(R.id.row_text);
            row_retailer_phone = itemView.findViewById(R.id.row_retailer_phone);
            row_route_name = itemView.findViewById(R.id.row_route_name);
            row_sound_stop = itemView.findViewById(R.id.row_sound_stop);
            row_sound_stop = itemView.findViewById(R.id.row_sound_stop);
            row_btn_acknowledge = itemView.findViewById(R.id.row_btn_acknowledge);
            row_btn_order = itemView.findViewById(R.id.row_btn_order);
            row_order_status = itemView.findViewById(R.id.row_order_status);
        }
    }
}