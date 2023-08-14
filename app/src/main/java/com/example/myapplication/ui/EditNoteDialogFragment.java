package com.example.myapplication.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.App;
import com.example.myapplication.R;
import com.example.myapplication.data.room.Point;
import com.example.myapplication.data.distance.PointListManager;
import com.example.myapplication.data.distance.PointWithDistance;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class EditNoteDialogFragment extends DialogFragment {
    String note;
    LatLng cord;
    Point point;

    public EditNoteDialogFragment(Point point) {
        this.note = point.note;
        this.cord = new LatLng(point.lat, point.lng);
        this.point = point;
    }

    public EditNoteDialogFragment(Point point, boolean isWithLink) {
        this.note = point.note;
        this.cord = new LatLng(point.lat, point.lng);
        this.point = point;
        findPoint();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_map_notate_edit, null);

        EditText editText = dialogView.findViewById(R.id.dialogNote);
        if (note != null && !note.isEmpty()) {
            editText.setText(note);
        } else editText.setText("");

        TextView textView = dialogView.findViewById(R.id.dialogDestinationTV);
        String fullString = getString(R.string.distance_to_this_point);
        int start = fullString.toCharArray().length;
        fullString = fullString + " " + PointListManager.getInstance().getStringDestinationByPoint(point);
        int end = fullString.toCharArray().length;
        SpannableString spannableString = new SpannableString(fullString);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);

        ImageView imageView = dialogView.findViewById(R.id.dialogPointIcon);
        Glide.with(getContext()).load(point.photoLink).into(imageView);
        builder.setView(dialogView)
                .setTitle("Notate editor")
                .setPositiveButton("OK", (dialog, which) -> checkInput(editText))
                .setNegativeButton("Cancel", (dialog, which) -> {
                });

        return builder.create();
    }

    private void checkInput(EditText editText) {
        if (editText != null) {
            if (point != null) {
                Runnable runnable = () -> {
                    List<Point> localList = App.getInstance().getPointsDAO().getAll();
                    for (Point roomPoint : localList) {
                        if (point.lat == roomPoint.lat && point.lng == roomPoint.lng) {
                            roomPoint.note = editText.getText().toString();
                            App.getInstance().getPointsDAO().updatePoint(roomPoint);
                        }
                    }

                };
                new Thread(runnable).start();
            }
        }
    }

    private void findPoint() {
        if (PointListManager.getInstance().getPointsLiveData().getValue() == null) return;

        for (PointWithDistance pwd : PointListManager.getInstance().getPointsLiveData().getValue()) {
            if (pwd.getPoint().lng == point.lng && pwd.getPoint().lat == point.lat) {
                point = pwd.getPoint();
                break;
            }
        }
    }
}
