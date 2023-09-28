package login.form;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class PagerAdapter extends RecyclerView.Adapter<PagerAdapter.PagerViewHolder> {
    private final int[] pageLayouts = {
            R.layout.page_layout_1,
            R.layout.page_layout_2,
            R.layout.page_layout_3
    };

    @NonNull
    @Override
    public PagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(pageLayouts[viewType], parent, false);
        return new PagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PagerViewHolder holder, int position) {
        // Customize the content of each page here if needed
    }

    @Override
    public int getItemCount() {
        return pageLayouts.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class PagerViewHolder extends RecyclerView.ViewHolder {
        PagerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
