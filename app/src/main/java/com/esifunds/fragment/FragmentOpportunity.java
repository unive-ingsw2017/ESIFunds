package com.esifunds.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esifunds.R;
import com.esifunds.model.IconTags;
import com.esifunds.model.Opportunity;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeoutException;

public class FragmentOpportunity extends Fragment
{
    public FragmentOpportunity()
    {
    }

    private String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        }
        catch (Exception e) {}
        return "";
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

            TextView textViewOpportunityExpirationDateFrom = rootView.findViewById(R.id.textViewOpportunityExpirationDateFrom);
            TextView textViewOpportunityExpirationDateTo = rootView.findViewById(R.id.textViewOpportunityExpirationDateTo);

            if(opportunity.getDATA_PUBBLICAZIONE() == 0 && opportunity.getDATA_SCADENZA() == 0)
            {
                TextView textViewOpportunityExpirationDateFromLabel = rootView.findViewById(R.id.textViewOpportunityExpirationDateFromLabel);
                textViewOpportunityExpirationDateFromLabel.setVisibility(View.GONE);

                TextView textViewOpportunityExpirationDateToLabel = rootView.findViewById(R.id.textViewOpportunityExpirationDateToLabel);
                textViewOpportunityExpirationDateToLabel.setVisibility(View.GONE);

                textViewOpportunityExpirationDateFrom.setVisibility(View.GONE);
                textViewOpportunityExpirationDateTo.setVisibility(View.GONE);

                TextView textViewOpportunityNoExpiration = rootView.findViewById(R.id.textViewOpportunityNoExpiration);
                textViewOpportunityNoExpiration.setVisibility(View.VISIBLE);
            }
            else if(opportunity.getDATA_PUBBLICAZIONE() == 0)
            {
                textViewOpportunityExpirationDateTo.setText(getDateCurrentTimeZone(opportunity.getDATA_SCADENZA()));
                textViewOpportunityExpirationDateFrom.setVisibility(View.INVISIBLE);

                if(System.currentTimeMillis() > opportunity.getDATA_SCADENZA())
                {
                    RelativeLayout relativeLayoutOpportunityExpiration = rootView.findViewById(R.id.relativeLayoutOpportunityExpiration);
                    relativeLayoutOpportunityExpiration.setBackgroundResource(R.color.md_red_700);
                }
            }
            else if(opportunity.getDATA_SCADENZA() == 0)
            {
                textViewOpportunityExpirationDateFrom.setText(getDateCurrentTimeZone(opportunity.getDATA_PUBBLICAZIONE()));
                textViewOpportunityExpirationDateTo.setVisibility(View.INVISIBLE);
            }
            else
            {
                textViewOpportunityExpirationDateFrom.setText(getDateCurrentTimeZone(opportunity.getDATA_PUBBLICAZIONE()));
                textViewOpportunityExpirationDateTo.setText(getDateCurrentTimeZone(opportunity.getDATA_SCADENZA()));

                if(System.currentTimeMillis() > opportunity.getDATA_SCADENZA())
                {
                    RelativeLayout relativeLayoutOpportunityExpiration = rootView.findViewById(R.id.relativeLayoutOpportunityExpiration);
                    relativeLayoutOpportunityExpiration.setBackgroundResource(R.color.md_red_700);
                }
            }

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

            TextView textViewOpportunityPayee = rootView.findViewById(R.id.textViewOpportunityPayee);
            textViewOpportunityPayee.setText(opportunity.getTIPOLOGIA_BENEFICIARI());

            ImageButton imageButtonOpportunityAvatarInternal = rootView.findViewById(R.id.imageButtonOpportunityAvatarInternal);

            imageButtonOpportunityAvatarInternal.setImageResource(IconTags.getInstance().getIconForTheme(opportunity.getContext(), opportunity.getTEMA_SINTETICO(), false));

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
