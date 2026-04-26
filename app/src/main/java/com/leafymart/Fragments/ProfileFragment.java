package com.leafymart.Fragments;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.leafymart.Database.DatabaseHelper;
import com.leafymart.R;

import java.io.ByteArrayOutputStream;

public class ProfileFragment extends Fragment {

    private ShapeableImageView profileImage;
    private View btnEditImage;
    private TextInputEditText nameET, emailET, phoneET, addressET;
    private TextInputLayout nameLayout, emailLayout, phoneLayout, addressLayout;
    private MaterialButton btnSaveProfile, btnLogout;
    private DatabaseHelper dbHelper;
    private byte[] currentImageBytes;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        dbHelper = new DatabaseHelper(requireContext());
        loadProfileData();

        setupListeners();
    }

    private void initViews(View view) {
        profileImage = view.findViewById(R.id.profile_image);
        btnEditImage = view.findViewById(R.id.btn_edit_image);
        nameET = view.findViewById(R.id.name_edittext);
        emailET = view.findViewById(R.id.email_edittext);
        phoneET = view.findViewById(R.id.phone_edittext);
        addressET = view.findViewById(R.id.address_edittext);

        nameLayout = view.findViewById(R.id.name_layout);
        emailLayout = view.findViewById(R.id.email_layout);
        phoneLayout = view.findViewById(R.id.phone_layout);
        addressLayout = view.findViewById(R.id.address_layout);

        btnSaveProfile = view.findViewById(R.id.btn_save_profile);
        btnLogout = view.findViewById(R.id.btn_logout);
    }

    private void loadProfileData() {
        Cursor cursor = dbHelper.getProfileData();
        if (cursor != null && cursor.moveToFirst()) {
            nameET.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME)));
            emailET.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL)));
            phoneET.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE)));
            addressET.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ADDRESS)));

            byte[] imageBlob = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IMAGE));
            if (imageBlob != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);
                profileImage.setImageBitmap(bitmap);
                currentImageBytes = imageBlob;
            }
            cursor.close();
        }
    }

    private void setupListeners() {
        // Edit Image Dialog
        View.OnClickListener showImageOptions = v -> showProfileImageOptions();
        profileImage.setOnClickListener(showImageOptions);
        btnEditImage.setOnClickListener(showImageOptions);

        // Individual Edit Buttons
        setupEditToggle(nameLayout, nameET);
        setupEditToggle(emailLayout, emailET);
        setupEditToggle(phoneLayout, phoneET);
        setupEditToggle(addressLayout, addressET);

        // Save All Button
        btnSaveProfile.setOnClickListener(v -> saveAllData());

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Logging out...", Toast.LENGTH_SHORT).show();
            // Implement logout logic (e.g., clear preferences and navigate to Login)
        });
    }

    private void setupEditToggle(TextInputLayout layout, TextInputEditText editText) {
        layout.setEndIconOnClickListener(v -> {
            if (!editText.isEnabled()) {
                // Enable editing
                editText.setEnabled(true);
                editText.requestFocus();
                layout.setEndIconDrawable(R.drawable.ic_check);
            } else {
                // Disable editing (Confirm individual field)
                editText.setEnabled(false);
                layout.setEndIconDrawable(R.drawable.ic_edit);
            }
        });
    }

    private void showProfileImageOptions() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_profile_image, null);
        dialog.setContentView(bottomSheetView);

        MaterialButton btnView = bottomSheetView.findViewById(R.id.btn_view_photo);
        MaterialButton btnUpload = bottomSheetView.findViewById(R.id.btn_upload_photo);
        MaterialButton btnSaveToPhone = bottomSheetView.findViewById(R.id.btn_save_to_phone);

        btnView.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Viewing picture...", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnUpload.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Upload feature coming soon!", Toast.LENGTH_SHORT).show();
            // In a real app, you would start an Intent for gallery/camera here
            dialog.dismiss();
        });

        btnSaveToPhone.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Saved to gallery!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void saveAllData() {
        String name = nameET.getText().toString();
        String email = emailET.getText().toString();
        String phone = phoneET.getText().toString();
        String address = addressET.getText().toString();

        boolean success = dbHelper.updateProfile(name, email, phone, address, currentImageBytes);
        if (success) {
            Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            // Reset all fields to non-editable look
            disableAllFields();
        } else {
            Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void disableAllFields() {
        nameET.setEnabled(false);
        nameLayout.setEndIconDrawable(R.drawable.ic_edit);
        emailET.setEnabled(false);
        emailLayout.setEndIconDrawable(R.drawable.ic_edit);
        phoneET.setEnabled(false);
        phoneLayout.setEndIconDrawable(R.drawable.ic_edit);
        addressET.setEnabled(false);
        addressLayout.setEndIconDrawable(R.drawable.ic_edit);
    }
}