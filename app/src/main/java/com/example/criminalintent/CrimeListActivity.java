package com.example.criminalintent;

import android.text.style.TtsSpan;

import androidx.fragment.app.Fragment;

public class CrimeListActivity extends  SingleFragmentActivity{
    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}
