package first.javatpoint.com.tnauagri;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView riceInfoRecyclerView;
    private RecyclerView videoRecyclerView;
    private String[] riceInfoList = {
            "Rice Growing Districts of Tamil Nadu",
            "Crop Protection",
            "Nutrient Deficiency Symptoms",
            "Nutrient Deficiency Symptoms",
            "Farm Mechanization",
            "Seed Production",
            "Post Harvest Technology",
            "Cost of Cultivation",
            "Organic Rice Cultivation",
            "Rice Related Research Institutes"
    };

    private String[] videoData = {
            "Video 1",
            "Video 2",
            "Video 3",
            "Video 4",
            "Video 5"
    };

    private String[] videoDurations = {
            "14.5 mins",
            "14.5 mins",
            "14.5 mins",
            "14.5 mins",
            "14.5 mins"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the RecyclerView for rice info
        riceInfoRecyclerView = findViewById(R.id.rice_info_recyclerview);
        riceInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        riceInfoRecyclerView.setAdapter(new RiceInfoAdapter(riceInfoList, new RiceInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String itemName) {
                // Handle rice info item click here
            }
        }));

        // Initialize the RecyclerView for videos
        videoRecyclerView = findViewById(R.id.video_recyclerview);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        videoRecyclerView.setAdapter(new VideoAdapter(videoData, videoDurations));
    }

    private static class RiceInfoAdapter extends RecyclerView.Adapter<RiceInfoAdapter.ViewHolder> {

        private String[] riceInfoList;
        private OnItemClickListener itemClickListener;

        public interface OnItemClickListener {
            void onItemClick(String itemName);
        }

        RiceInfoAdapter(String[] data, OnItemClickListener listener) {
            riceInfoList = data;
            itemClickListener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_rice_info, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.riceInfoText.setText(riceInfoList[position]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Display a toast message when an item is clicked
                    String toastMessage = "You clicked: " + riceInfoList[position];
                    Toast.makeText(v.getContext(), toastMessage, Toast.LENGTH_SHORT).show();

                    // Call the onItemClick method to notify the listener (if needed)
                    itemClickListener.onItemClick(riceInfoList[position]);
                }
            });
        }

        @Override
        public int getItemCount() {
            return riceInfoList.length;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView riceInfoText;

            ViewHolder(View itemView) {
                super(itemView);
                riceInfoText = itemView.findViewById(R.id.rice_info_text);
            }
        }
    }

    private class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

        private String[] videoData;
        private String[] videoDurations;

        VideoAdapter(String[] data, String[] durations) {
            videoData = data;
            videoDurations = durations;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_video_info, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.videoNameText.setText(videoData[position]);
            holder.videoDurationText.setText(videoDurations[position]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Play the internal video when an item is clicked
                    int videoResourceId = getVideoResourceId(position);
                    if (videoResourceId != 0) {
                        playInternalVideo(videoResourceId);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return videoData.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView videoThumbnail;
            TextView videoNameText;
            TextView videoDurationText;

            ViewHolder(View itemView) {
                super(itemView);
                videoThumbnail = itemView.findViewById(R.id.video_thumbnail);
                videoNameText = itemView.findViewById(R.id.video_name_text);
                videoDurationText = itemView.findViewById(R.id.video_duration_text);
            }
        }

        // Function to get the resource ID of the internal video
        private int getVideoResourceId(int position) {
            switch (position) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                    return R.raw.sample;
                default:
                    return 0;
            }
        }

        // Function to play an internal video
        private void playInternalVideo(int videoResourceId) {
            String videoUrl = "android.resource://" + getPackageName() + "/" + videoResourceId;
            startVideoPlayerActivity(videoUrl);
        }
    }

    private void startVideoPlayerActivity(String videoUrl) {
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra("VIDEO_URL", videoUrl);
        startActivity(intent);
    }
}
