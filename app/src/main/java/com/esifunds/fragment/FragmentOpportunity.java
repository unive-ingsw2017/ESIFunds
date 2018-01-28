package com.esifunds.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.esifunds.R;
import com.esifunds.model.Opportunity;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
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
        final Opportunity opportunity;
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        if(args != null && args.containsKey("opportunity") && (opportunity = args.getParcelable("opportunity")) != null)
        {
            TextView textViewProgram = rootView.findViewById(R.id.textViewProgram);
            textViewProgram.setText(opportunity.getPROGRAMMA());

            TextView textViewOpportunityMoneyValue = rootView.findViewById(R.id.textViewOpportunityMoneyValue);
            textViewOpportunityMoneyValue.setText(formatter.format(opportunity.getIMPORTO()));

            TextView textViewOpportunityExpirationDate = rootView.findViewById(R.id.textViewOpportunityExpirationDate);
            textViewOpportunityExpirationDate.setText(String.format(Locale.ITALIAN, "%d", opportunity.getDATA_SCADENZA()));

            TextView textViewOpportunityLocation = rootView.findViewById(R.id.textViewOpportunityLocation);
            if(opportunity.getLUOGO().isEmpty())
            {
                textViewOpportunityLocation.setText("Nessun luogo");
            }
            else
            {
                textViewOpportunityLocation.setText(opportunity.getLUOGO());
            }

            TextView textViewOpportunitySummary = rootView.findViewById(R.id.textViewOpportunitySummary);
            textViewOpportunitySummary.setText(opportunity.getOGGETTO());

            TextView textViewOpportunityTheme = rootView.findViewById(R.id.textViewOpportunityTheme);
            textViewOpportunityTheme.setText(opportunity.getTEMA_SINTETICO());

            ImageButton imageButtonOpportunityAvatarInternal = rootView.findViewById(R.id.imageButtonOpportunityAvatarInternal);
            imageButtonOpportunityAvatarInternal.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(opportunity.getLINK()));
                    startActivity(browserIntent);
                }
            });

            TextView textViewOpportunityType = rootView.findViewById(R.id.textViewOpportunityType);
            textViewOpportunityType.setText(opportunity.getTIPO_OPPORTUNITA());
        }

        return rootView;
    }
}
