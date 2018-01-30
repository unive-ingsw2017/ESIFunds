package com.esifunds.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esifunds.R;
import com.esifunds.model.IconTags;
import com.esifunds.model.Opportunity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class FragmentOpportunitySingleton extends Fragment
{
    public FragmentOpportunitySingleton()
    {
    }

    private String getDateCurrentTimeZone(long timestamp)
    {
        try
        {
            Calendar calendar = Calendar.getInstance();
            TimeZone timeZone = TimeZone.getDefault();

            calendar.setTimeInMillis(timestamp);
            calendar.add(Calendar.MILLISECOND, timeZone.getOffset(calendar.getTimeInMillis()));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
            Date timeZoneCurrent = calendar.getTime();

            return simpleDateFormat.format(timeZoneCurrent);
        }
        catch(Exception exceptionError)
        {
            exceptionError.printStackTrace();
        }

        return "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.fragment_opportunity_singleton, container, false);

        Bundle bundleArguments = getArguments();
        final Opportunity mainOpportunity;
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

        if(bundleArguments != null && bundleArguments.containsKey("opportunity") && (mainOpportunity = bundleArguments.getParcelable("opportunity")) != null)
        {
            ImageButton imageButtonSingletonIcon = viewRoot.findViewById(R.id.imageButtonSingletonIcon);
            imageButtonSingletonIcon.setImageResource(IconTags.getInstance().getIconForTheme(mainOpportunity.getContext(), mainOpportunity.getTEMA_SINTETICO(), false));

            imageButtonSingletonIcon.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View v)
                {
                    Intent externalBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mainOpportunity.getLINK()));
                    startActivity(externalBrowserIntent);
                }
            });

            TextView textViewSingletonProgram = viewRoot.findViewById(R.id.textViewSingletonProgram);
            textViewSingletonProgram.setText(mainOpportunity.getPROGRAMMA());

            TextView textViewSingletonMoney = viewRoot.findViewById(R.id.textViewSingletonMoney);
            if(mainOpportunity.getIMPORTO() == 0)
                textViewSingletonMoney.setText("0,00");
            else
                textViewSingletonMoney.setText(decimalFormat.format(mainOpportunity.getIMPORTO()));

            TextView textViewSingletonDateStart = viewRoot.findViewById(R.id.textViewDateStart);
            TextView textViewSingletonDateEnd = viewRoot.findViewById(R.id.textViewDateEnd);

            // Date Parsing Code
            if(mainOpportunity.getDATA_PUBBLICAZIONE() == 0 && mainOpportunity.getDATA_PUBBLICAZIONE() == 0)
            {
                LinearLayout linearLayoutSingletonSecondary = viewRoot.findViewById(R.id.linearLayoutSingletonSecondary);
                linearLayoutSingletonSecondary.setVisibility(View.GONE);
            }
            else if(mainOpportunity.getDATA_PUBBLICAZIONE() == 0)
            {
                RelativeLayout relativeLayoutSingletonDateStart = viewRoot.findViewById(R.id.relativeLayoutDateStart);
                relativeLayoutSingletonDateStart.setVisibility(View.GONE);
            }
            else if(mainOpportunity.getDATA_SCADENZA() == 0)
            {
                RelativeLayout relativeLayoutSingletonDateEnd = viewRoot.findViewById(R.id.relativeLayoutDateEnd);
                relativeLayoutSingletonDateEnd.setVisibility(View.GONE);
            }

            if(System.currentTimeMillis() > mainOpportunity.getDATA_SCADENZA())
            {
                RelativeLayout relativeLayoutSingletonDateEnd = viewRoot.findViewById(R.id.relativeLayoutDateEnd);
                relativeLayoutSingletonDateEnd.setBackgroundResource(R.color.md_red_700);
            }

            textViewSingletonDateStart.setText(getDateCurrentTimeZone(mainOpportunity.getDATA_PUBBLICAZIONE()));
            textViewSingletonDateEnd.setText(getDateCurrentTimeZone(mainOpportunity.getDATA_SCADENZA()));

            TextView textViewSingletonLocation = viewRoot.findViewById(R.id.textViewSingletonLocation);
            if(mainOpportunity.getLUOGO().isEmpty())
                textViewSingletonLocation.setText(R.string.string_no_location);
            else
                textViewSingletonLocation.setText(mainOpportunity.getLUOGO());

            TextView textViewSingletonSummary = viewRoot.findViewById(R.id.textViewSingletonSummary);
            textViewSingletonSummary.setText(mainOpportunity.getOGGETTO());

            TextView textViewSingletonTheme = viewRoot.findViewById(R.id.textViewSingletonTheme);
            textViewSingletonTheme.setText(mainOpportunity.getTEMA_SINTETICO());
            TextView textViewSingletonType = viewRoot.findViewById(R.id.textViewSingletonType);
            textViewSingletonType.setText(mainOpportunity.getTIPO_OPPORTUNITA());
            TextView textViewSingletonPayee = viewRoot.findViewById(R.id.textViewSingletonPayee);
            textViewSingletonPayee.setText(mainOpportunity.getTIPOLOGIA_BENEFICIARI());
        }

        return viewRoot;
    }
}
