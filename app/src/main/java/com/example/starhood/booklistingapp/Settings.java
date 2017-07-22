package com.example.starhood.booklistingapp;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


       /**   get preference value
        *  SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String s=sharedPreferences.getString("min","min");*/

    }

    public static class AppSetting extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate( Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference preference =findPreference("min");
            bindPreferenceSummaryToValue(preference);

            Preference preferences =findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(preferences);
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences  sharedPreferences=PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String s = sharedPreferences.getString(preference.getKey(),"");
            onPreferenceChange(preference,s);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String s=newValue.toString();

            if(preference instanceof ListPreference)
            {
                ListPreference listPreference=(ListPreference) preference;
                int index =listPreference.findIndexOfValue(s);
                if(index>=0)
                {
                    CharSequence[] labels=listPreference.getEntries();
                    preference.setSummary(labels[index]);
                }
            }
            else
                preference.setSummary(s);

            return true;

        }
    }
}
