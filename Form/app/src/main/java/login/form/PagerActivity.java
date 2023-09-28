package login.form;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

public class PagerActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private Button skipButton, doneButton;
    private ImageButton leftButton, rightButton;
    private int currentPage = 0;
    private Handler autoScrollHandler = new Handler();

    private LinearLayout dotLayout;
    private PagerAdapter pagerAdapter;
    private int numPages = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        viewPager = findViewById(R.id.view_pager2);
        skipButton = findViewById(R.id.skipButton);
        doneButton = findViewById(R.id.doneButton);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);


        PagerAdapter pagerAdapter = new PagerAdapter();
        viewPager.setAdapter(pagerAdapter);

        // Set up ViewPager page change listener to control button visibility
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                updateButtonsVisibility();
            }
        });

        // Set up click listeners for Skip and Done buttons
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the user is on the last page
                if (currentPage == viewPager.getAdapter().getItemCount() - 1) {
                    goToMainActivity(); // Navigate to MainActivity
                }
            }
        });

        // Set up click listeners for left and right buttons
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the user is not on the first page
                if (currentPage > 0) {
                    viewPager.setCurrentItem(currentPage - 1);
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the user is not on the last page
                if (currentPage < viewPager.getAdapter().getItemCount() - 1) {
                    viewPager.setCurrentItem(currentPage + 1);
                }
            }
        });

        dotLayout = findViewById(R.id.dotLayout);
        pagerAdapter = new PagerAdapter();
        viewPager.setAdapter(pagerAdapter);

        // Add the dots
        addDots();

        // Set up ViewPager page change listener to update dot selection
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                updateDots();
                updateButtonsVisibility();
            }
        });


        // Start automatic page scrolling after 5 seconds
        startAutoScroll();
    }

    private void updateButtonsVisibility() {
        int pageCount = viewPager.getAdapter().getItemCount();

        if (currentPage < pageCount - 1) {
            skipButton.setVisibility(View.VISIBLE);
            doneButton.setVisibility(View.GONE);
            if (currentPage > 0) {
                leftButton.setVisibility(View.VISIBLE);
            } else {
                leftButton.setVisibility(View.INVISIBLE);
            }
        } else {
            skipButton.setVisibility(View.GONE);
            doneButton.setVisibility(View.VISIBLE);
            leftButton.setVisibility(View.VISIBLE);
        }

        if (currentPage == pageCount - 1) {
            rightButton.setVisibility(View.INVISIBLE);
        } else {
            rightButton.setVisibility(View.VISIBLE);
        }
    }


    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish the PagerActivity so that the user cannot go back to it.
    }

    private void startAutoScroll() {
        autoScrollHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentPage < viewPager.getAdapter().getItemCount() - 1) {
                    viewPager.setCurrentItem(currentPage + 1);
                    startAutoScroll(); // Schedule the next automatic scroll
                }
            }
        }, 3000); // Delay for 5 seconds before scrolling to the next page
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending auto-scroll callbacks when the activity is destroyed
        autoScrollHandler.removeCallbacksAndMessages(null);
    }


    private void addDots() {
        for (int i = 0; i < numPages; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_selector));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotLayout.addView(dot, params);

            final int dotPosition = i;

            // Set a click listener for the dot
            dot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to the page corresponding to the clicked dot
                    viewPager.setCurrentItem(dotPosition);
                }
            });
        }
        updateDots();
    }

    private void updateDots() {
        for (int i = 0; i < numPages; i++) {
            ImageView dot = (ImageView) dotLayout.getChildAt(i);
            dot.setSelected(i == currentPage);
        }
    }


}
