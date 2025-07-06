package com.leafymart.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.leafymart.Activities.ApiConfig;
import com.leafymart.Activities.MySingleton;
import com.leafymart.Adapter.ImageSliderAdapter;
import com.leafymart.Adapter.PlantAdapter;
import com.leafymart.Model.PlantModel;
import com.leafymart.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExploreFragment extends Fragment {

    private PlantAdapter plantAdapter;
    private final List<PlantModel> plants = new ArrayList<>();
    private ViewPager2 viewPager2;

    public ExploreFragment() {}

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup c, Bundle b) {
        return inf.inflate(R.layout.fragment_explore, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);

        // ── Toolbar ───────────────────────────────
        Toolbar tb = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(tb);
        setHasOptionsMenu(true);

        // ── RecyclerView for Trending plants ──────
        RecyclerView rv = v.findViewById(R.id.trending_plants_recyclerView);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        plantAdapter = new PlantAdapter(getContext(), plants);
        rv.setAdapter(plantAdapter);

        // ── Image slider (unchanged) ──────────────
        viewPager2 = v.findViewById(R.id.imageSlider);
        List<Integer> imgs = Arrays.asList(
                R.drawable.image_plant1, R.drawable.image_plant2,
                R.drawable.image_plant3, R.drawable.image_plant4);
        viewPager2.setAdapter(new ImageSliderAdapter(requireContext(), imgs));
        autoSlideImages();

        // ── Click “View All” (already yours) ──────
        v.findViewById(R.id.trending_plants_viewAll_TV).setOnClickListener(btn -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                            R.anim.slide_in_left,  R.anim.slide_out_right)
                    .replace(R.id.frame_layout, new View_Tranding())
                    .addToBackStack(null)
                    .commit();
        });

        // ── Finally: fetch real data ──────────────

        fetchTrending();

        final Handler handler = new Handler();
        final int interval = 5000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchTrending();
                handler.postDelayed(this, interval);

            }
        }, interval);

    }

    /** Call GET /products/trending and fill RecyclerView */
    private void fetchTrending() {
        String url = ApiConfig.BASE_URL + "/products/trending";

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    plants.clear();
                    parseJsonArray(response);
                    plantAdapter.notifyDataSetChanged();  // refresh RecyclerView
                },
                error -> Toast.makeText(getContext(), "Load error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        MySingleton.get(requireContext()).add(req);
    }



    private void parseJsonArray(JSONArray arr) {
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.optJSONObject(i);
            if (o == null) continue;

            String imageUrl = o.optString("image_url");
            if (imageUrl == null || imageUrl.trim().isEmpty() || imageUrl.equalsIgnoreCase("null")) {
                imageUrl = null; // Or fallback image URL
            }

            plants.add(new PlantModel(
                    o.optInt("id"),
                    o.optString("name"),
                    o.optDouble("price"),
                    o.optDouble("rating"),
                    o.optInt("sold"),
                    imageUrl
            ));
        }
    }




    /** Simple auto‑slide for ViewPager2 */
    private void autoSlideImages() {
        Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override public void run() {
                int next = (viewPager2.getCurrentItem() + 1) % viewPager2.getAdapter().getItemCount();
                viewPager2.setCurrentItem(next, true);
                h.postDelayed(this, 2500);  // 2.5 s
            }
        };
        h.postDelayed(r, 2500);
    }

    // ── Toolbar menu inflating (kept from your code) ─────
    @Override public void onCreateOptionsMenu(Menu m, MenuInflater i) {
        i.inflate(R.menu.menu_top, m);
        super.onCreateOptionsMenu(m, i);
    }
    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // TODO handle clicks
        return super.onOptionsItemSelected(item);
    }
}
