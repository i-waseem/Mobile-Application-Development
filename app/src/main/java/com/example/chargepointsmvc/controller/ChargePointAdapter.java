package com.example.chargepointsmvc.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chargepointsmvc.R;
import com.example.chargepointsmvc.model.ChargePoint;
import com.example.chargepointsmvc.model.DBHelper;

import java.util.ArrayList;

public class ChargePointAdapter extends ArrayAdapter<ChargePoint> {
    private DBHelper dbHelper;

    public ChargePointAdapter(@NonNull Context context, ArrayList<ChargePoint> chargePoints) {
        super(context, 0, chargePoints);
        dbHelper = new DBHelper(context); // Initialize DBHelper
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // ViewHolder pattern for better performance
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_charge_point, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChargePoint chargePoint = getItem(position);

        if (chargePoint != null) {
            holder.referenceIdText.setText("Ref: " + chargePoint.getReferenceId());
            holder.townText.setText("Town: " + chargePoint.getTown());

            // Set details button click listener
            holder.detailsButton.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), ChargePointDetailsActivity.class);
                intent.putExtra("referenceId", chargePoint.getReferenceId());
                intent.putExtra("latitude", chargePoint.getLatitude());
                intent.putExtra("longitude", chargePoint.getLongitude());
                intent.putExtra("town", chargePoint.getTown());
                intent.putExtra("county", chargePoint.getCounty());
                intent.putExtra("postcode", chargePoint.getPostcode());
                intent.putExtra("chargeDeviceStatus", chargePoint.getChargeDeviceStatus());
                intent.putExtra("connectorID", chargePoint.getConnectorID());
                intent.putExtra("connectorType", chargePoint.getConnectorType());
                getContext().startActivity(intent);
            });

            // Set favorite button icon and listener
            holder.favoriteButton.setImageResource(chargePoint.isFavorite() ?
                    R.drawable.ic_favorite :
                    R.drawable.ic_favorite_border);

            holder.favoriteButton.setOnClickListener(v -> {
                boolean isFavorite = !chargePoint.isFavorite();
                chargePoint.setFavorite(isFavorite); // Update the model
                dbHelper.updateFavoriteStatus(chargePoint.getReferenceId(), isFavorite); // Update database

                // Update button icon
                holder.favoriteButton.setImageResource(isFavorite ?
                        R.drawable.ic_favorite :
                        R.drawable.ic_favorite_border);

                // Feedback message
                Toast.makeText(getContext(),
                        isFavorite ? "Added to Favorites" : "Removed from Favorites",
                        Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }

    // ViewHolder for caching views
    public class ViewHolder {
        TextView referenceIdText;
        TextView townText;
        Button detailsButton;
        ImageButton favoriteButton;

        public ViewHolder(View view) {
            referenceIdText = view.findViewById(R.id.referenceId);
            townText = view.findViewById(R.id.town);
            detailsButton = view.findViewById(R.id.detailsButton);
            favoriteButton = view.findViewById(R.id.favoriteButton);
        }
    }
}