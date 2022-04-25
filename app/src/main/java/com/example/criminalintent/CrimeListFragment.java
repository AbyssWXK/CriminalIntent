package com.example.criminalintent;

import android.app.Activity;
import android.bluetooth.le.AdvertisingSetParameters;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CrimeListFragment extends Fragment {
    public static String TAG = "CrimeListFragment";
    public static final int REQUEST_ITEM_POS = 0;
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private int ChangedPos = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_crime_list,container,
                false);
        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if(mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else{
            // mAdapter.notifyDataSetChanged();
            mAdapter.notifyItemChanged(ChangedPos);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_ITEM_POS){
            if(data == null){
                return;
            }else{
                ChangedPos = CrimeFragment.getChangedPos(data);
                // ChangedPos =  data.getIntExtra(CrimeFragment.ARG_CRIME_ID,0);
            }
        }
    }

    private abstract class ItemHolder extends RecyclerView.ViewHolder{

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
        }
        public abstract void bind(Crime crime);
    }


    private class CrimeHolder extends ItemHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_crime,parent,false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
        }

        @Override
        public void onClick(View view){
//            Toast.makeText(getActivity(),mCrime.getTitle()+" Clicked!", Toast.LENGTH_SHORT)
//            .show();
            Intent intent = CrimeActivity.newIntent(getActivity(),mCrime.getId());
            // startActivity(intent);REQUEST_ITEM_POS
            startActivityForResult(intent,REQUEST_ITEM_POS);
        }

        @Override
        public void bind(Crime crime){
            mCrime = crime;


            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
        }


    }

    private class CrimePoliceHolder extends ItemHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Button mPoliceButton;
        private Crime mCrime;
        public CrimePoliceHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_police,parent,false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mPoliceButton = (Button) itemView.findViewById(R.id.call_police_button);
            mPoliceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),"110",Toast.LENGTH_SHORT);
                }
            });
        }

        @Override
        public void onClick(View view){
            Intent intent = CrimeActivity.newIntent(getActivity(),mCrime.getId());
            startActivity(intent);
        }
        @Override
        public void bind(Crime crime){
            mCrime = crime;


            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List<Crime> mCrimes;

        public static final int VIEW_TYPE_CRIME = 1;
        public static final int VIEW_TYPE_POLICE = 2;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            switch (viewType){
                case 1:
                    return new CrimeHolder(layoutInflater, parent);
                case 2:
                    return new CrimePoliceHolder(layoutInflater, parent);
                default:
                    return new CrimeHolder(layoutInflater, parent);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }
        @Override
        public int getItemViewType(int position){
            return mCrimes.get(position).getViewType();
        }
        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
