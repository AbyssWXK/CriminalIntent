package com.example.criminalintent;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    public static String TAG = "CrimeLab";
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            Log.d(TAG,"get");
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
    private CrimeLab(Context context){
        mCrimes = new ArrayList<>();
        for(int i = 0; i<100; i++){
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 ==0);
            crime.setViewType(i % 2 + 1);
            mCrimes.add(crime);
            Log.d(TAG,crime.getTitle());
        }


    }
    public List<Crime> getCrimes(){
        return mCrimes;
    }
    public Crime getCrime(UUID id){
        for (Crime crime : mCrimes){
            if(crime.getId() == id){
                Log.d(TAG,crime.getId()+crime.getTitle());
                return crime;
            }
        }
        return null;
    }
}
