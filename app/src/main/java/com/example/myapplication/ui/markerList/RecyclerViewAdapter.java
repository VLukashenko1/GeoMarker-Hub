package com.example.myapplication.ui.markerList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.App;
import com.example.myapplication.Const;
import com.example.myapplication.R;
import com.example.myapplication.ui.EditNoteDialogFragment;
import com.example.myapplication.ui.map.MapsFragment;
import com.example.myapplication.data.distance.PointListManager;
import com.example.myapplication.data.distance.PointWithDistance;

import java.util.Arrays;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    List<PointWithDistance> pointWithDistanceList;
    private boolean[] expandedArr;
    private Context context;
    FragmentManager fragmentManager;

    public RecyclerViewAdapter(Context context, List<PointWithDistance> pointWithDistanceList, FragmentManager fragmentManager) {
        this.inflater = LayoutInflater.from(context);
        this.pointWithDistanceList = pointWithDistanceList;
        this.context = context;
        this.fragmentManager = fragmentManager;
        expandedArr = new boolean[pointWithDistanceList.size()];
        Arrays.fill(expandedArr, false);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.point_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!expandedArr[position]) {
            holder.cardView.setVisibility(View.GONE);
            holder.distance.setText(pointWithDistanceList.get(position).getDestinationString());
            holder.name.setText(pointWithDistanceList.get(position).getPoint().name);
            holder.note.setText(pointWithDistanceList.get(position).getPoint().note);
            Glide.with(holder.imageView).load(pointWithDistanceList.get(position).getPoint().photoLink).into(holder.imageView);
        } else {
            holder.cardView.setVisibility(View.VISIBLE);
            holder.bigDistance.setText(pointWithDistanceList.get(position).getDestinationString());
            holder.bigName.setText(pointWithDistanceList.get(position).getPoint().name);
            holder.bigNote.setText(pointWithDistanceList.get(position).getPoint().note);
            Glide.with(holder.bigImageView).load(pointWithDistanceList.get(position).getPoint().photoLink).into(holder.bigImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (pointWithDistanceList == null) {
            return 0;
        } else return pointWithDistanceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView distance, name, note;
        ImageView imageView;

        CardView cardView;
        ImageView bigImageView;
        TextView bigDistance, bigName, bigNote;
        ImageButton route, noteEdit;

        ViewHolder(View view) {
            super(view);

            distance = view.findViewById(R.id.rvDestinationTextView);
            name = view.findViewById(R.id.rvPointName);
            note = view.findViewById(R.id.rvPointNote);
            imageView = view.findViewById(R.id.rvPointPhoto);

            cardView = view.findViewById(R.id.rvCardView);
            bigImageView = view.findViewById(R.id.rvBigImageView);
            bigDistance = view.findViewById(R.id.rvBigDistanceTV);
            bigNote = view.findViewById(R.id.rvBigNoteTV);
            bigName = view.findViewById(R.id.rvBigNameTV);

            route = view.findViewById(R.id.rvLayRoute);
            noteEdit = view.findViewById(R.id.rvNoteEdit);

            route.setOnClickListener(v -> onRouteClick(getAdapterPosition()));
            noteEdit.setOnClickListener(v -> onNoteEditClick(getAdapterPosition()));

            view.setOnClickListener(v -> onItemViewClick(getAdapterPosition()));
        }
    }

    private void onRouteClick(int position) {
        if (position == RecyclerView.NO_POSITION) return;
        //todo show message if current location not exist for inApp route
        //useless because it`s paid feature
        if (false) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Const.ROUTE_KEY, true);
            bundle.putDouble(Const.ROUTE_DESTINATION_LAT_KEY, pointWithDistanceList.get(position).getPoint().lat);
            bundle.putDouble(Const.ROUTE_DESTINATION_LNG_KEY, pointWithDistanceList.get(position).getPoint().lng);
            bundle.putDouble(Const.ROUTE_LOCATION_LAT_KEY, PointListManager.getInstance().getCurrentLocation().getValue().latitude);
            bundle.putDouble(Const.ROUTE_LOCATION_LNG_KEY, PointListManager.getInstance().getCurrentLocation().getValue().longitude);
            MapsFragment mapsFragment = new MapsFragment();
            mapsFragment.setArguments(bundle);

        } else {
            String destination = pointWithDistanceList.get(position).getPoint().lat
                    + "," + pointWithDistanceList.get(position).getPoint().lng;
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destination + "&mode=d");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(App.getInstance().getPackageManager()) != null) {
                App.getInstance().startActivity(mapIntent);
            } else {
                Toast.makeText(context,"it is not possible to build a route", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onItemViewClick(int position) {
        if (position == RecyclerView.NO_POSITION) return;

        expandedArr[position] = !expandedArr[position];

        notifyItemChanged(position);
    }

    private void onNoteEditClick(int position){
        EditNoteDialogFragment dialogFragment = new EditNoteDialogFragment(pointWithDistanceList.get(position).getPoint());
        dialogFragment.show(fragmentManager, "CustomDialogFragment");
    }
    public void changeDataSet(List<PointWithDistance> pointWithDistanceList) {
        this.pointWithDistanceList = pointWithDistanceList;
        Arrays.fill(expandedArr, false);
        notifyDataSetChanged();
    }
}
