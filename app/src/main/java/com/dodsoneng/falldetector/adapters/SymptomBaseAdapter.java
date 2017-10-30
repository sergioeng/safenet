package com.dodsoneng.falldetector.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.dodsoneng.falldetector.R;
import com.dodsoneng.falldetector.models.SymptomData;

import java.util.ArrayList;
/**
 * Created by sergio.eng on 10/18/17.
 */

public class SymptomBaseAdapter extends BaseAdapter {

    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;


    public SymptomBaseAdapter(Context context, ArrayList myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public SymptomData getItem(int position) {

        return (SymptomData) myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SymptomViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.symptom_layout_item, parent, false);
            mViewHolder = new SymptomViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (SymptomViewHolder) convertView.getTag();
        }

        SymptomData symptomData = getItem(position);

        mViewHolder.cbSymptom.setText(symptomData.getName());

        if (symptomData.getHasEntry()) {
            mViewHolder.etEntry.setText("      ");
            mViewHolder.tvUnit.setText(symptomData.getUnit());
        }
        else {
            mViewHolder.etEntry.setVisibility(View.INVISIBLE);
            mViewHolder.tvUnit.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private class SymptomViewHolder {
        CheckBox cbSymptom;
        EditText etEntry;
        TextView tvUnit;
        ;

        public SymptomViewHolder(View item) {
            cbSymptom = (CheckBox) item.findViewById(R.id.cbSymptom);
            etEntry = (EditText) item.findViewById(R.id.etEntry);
            tvUnit = (TextView) item.findViewById(R.id.tvUnit);
        }
    }


} /// SymptomBaseAdapter
