package first.javatpoint.com.tnauagri;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        // Initialize the VideoView
        videoView = findViewById(R.id.video_view);

        // Get the video URL from the intent
        String videoUrl = getIntent().getStringExtra("VIDEO_URL");

        // Load and play the video
        playVideo(videoUrl);
    }

    private void playVideo(String videoUrl) {
        // Check if the video URL is not null
        if (videoUrl != null && !videoUrl.isEmpty()) {
            Uri videoUri = Uri.parse(videoUrl);
            videoView.setVideoURI(videoUri);
            videoView.start();
        } else {
            // Handle the case where the video URL is empty or null
            // You can show an error message or take appropriate action here
        }
    }
}
