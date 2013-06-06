package com.savoirfairelinux.sflphone.fragments;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Profile;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.savoirfairelinux.sflphone.R;
import com.savoirfairelinux.sflphone.adapters.ContactPictureLoader;
import com.savoirfairelinux.sflphone.adapters.MenuAdapter;
import com.savoirfairelinux.sflphone.client.SFLPhoneHomeActivity;
import com.savoirfairelinux.sflphone.client.SFLPhonePreferenceActivity;

public class MenuFragment extends Fragment {

    private static final String TAG = MenuFragment.class.getSimpleName();
    public static final String ARG_SECTION_NUMBER = "section_number";

    MenuAdapter mAdapter;
    String[] mProjection = new String[] { Profile._ID, Profile.DISPLAY_NAME_PRIMARY, Profile.LOOKUP_KEY, Profile.PHOTO_THUMBNAIL_URI };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new MenuAdapter(getActivity());

        String[] categories = getResources().getStringArray(R.array.menu_categories);
        ArrayAdapter<String> paramAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_menu, getResources().getStringArray(
                R.array.menu_items_param));
        ArrayAdapter<String> helpAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_menu, getResources().getStringArray(
                R.array.menu_items_help));

        // Add Sections
        mAdapter.addSection(categories[0], paramAdapter);
        mAdapter.addSection(categories[1], helpAdapter);

    }

    private ExecutorService infos_fetcher = Executors.newCachedThreadPool();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.frag_menu, parent, false);

        ((ListView) inflatedView.findViewById(R.id.listView)).setAdapter(mAdapter);
        ((ListView) inflatedView.findViewById(R.id.listView)).setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                if (pos == 1 || pos == 2 || pos == 3) {
                    Intent launchPreferencesIntent = new Intent().setClass(getActivity(), SFLPhonePreferenceActivity.class);
                    getActivity().startActivityForResult(launchPreferencesIntent, SFLPhoneHomeActivity.REQUEST_CODE_PREFERENCES);
                }
            }
        });

        Cursor mProfileCursor = getActivity().getContentResolver().query(Profile.CONTENT_URI, mProjection, null, null, null);
        mProfileCursor.moveToFirst();

        ((ImageView) inflatedView.findViewById(R.id.user_photo)).setImageURI(Uri.parse(mProfileCursor.getString(mProfileCursor
                .getColumnIndex(Profile.PHOTO_THUMBNAIL_URI))));
        ((TextView) inflatedView.findViewById(R.id.user_name)).setText(mProfileCursor.getString(mProfileCursor
                .getColumnIndex(Profile.DISPLAY_NAME_PRIMARY)));

        return inflatedView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

}
