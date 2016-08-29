package br.com.ti.aricelio.cfi;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by aricelio on 29/08/16.
 */
public class SettingFragment extends PreferenceFragment{

    // OnCreate

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
