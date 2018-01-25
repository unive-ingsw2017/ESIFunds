package com.esifunds.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esifunds.R;
import com.esifunds.model.Opportunity;

import java.util.Locale;

public class FragmentOpportunity extends Fragment
{
    public FragmentOpportunity()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_opportunity, container, false);
        Bundle args = getArguments();
        Opportunity opportunity;
        if(args != null && args.containsKey("opportunity") && (opportunity = args.getParcelable("opportunity")) != null)
        {
            TextView textViewProgram = rootView.findViewById(R.id.textViewProgram);
            textViewProgram.setText(opportunity.getPROGRAMMA());

            TextView textViewOpportunityMoneyValue = rootView.findViewById(R.id.textViewOpportunityMoneyValue);
            textViewOpportunityMoneyValue.setText(opportunity.getIMPORTO());

            TextView textViewOpportunityExpirationDate = rootView.findViewById(R.id.textViewOpportunityExpirationDate);
            textViewOpportunityExpirationDate.setText(String.format(Locale.ITALIAN, "%d", opportunity.getDATA_SCADENZA()));

            TextView textViewOpportunityLocation = rootView.findViewById(R.id.textViewOpportunityLocation);
            textViewOpportunityLocation.setText("TODO");

            TextView textViewOpportunitySummary = rootView.findViewById(R.id.textViewOpportunitySummary);
            textViewOpportunitySummary.setText(opportunity.getOGGETTO());

            TextView textViewOpportunityTheme = rootView.findViewById(R.id.textViewOpportunityTheme);
            textViewOpportunityTheme.setText(opportunity.getTEMA_SINTETICO());

            TextView textViewOpportunityType = rootView.findViewById(R.id.textViewOpportunityType);
            textViewOpportunityType.setText(opportunity.getTIPO_OPPORTUNITA());
        }

        return rootView;
    }
}
