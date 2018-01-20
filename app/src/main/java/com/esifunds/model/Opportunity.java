package com.esifunds.model;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.esifunds.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Opportunity extends AbstractItem<Opportunity, Opportunity.ViewHolder>
{
    public String opportunityName;
    public String opportunityDescription;

    public Opportunity()
    {
    }

    public Opportunity(String opportunityName, String opportunityDescription)
    {
        this.opportunityName = opportunityName;
        this.opportunityDescription = opportunityDescription;
    }

    @Override
    public int getType()
    {
        return R.id.linearLayoutListOpportunity;
    }

    @Override
    public int getLayoutRes()
    {
        return R.layout.list_opportunity;
    }

    @Override
    public ViewHolder getViewHolder(@NonNull View itemView)
    {
        return new ViewHolder(itemView);
    }

    protected static class ViewHolder extends FastAdapter.ViewHolder<Opportunity>
    {
        @BindView(R.id.imageViewOpportunityAvatar) ImageView opportunityAvatar;
        @BindView(R.id.imageButtonOpportunityIcon) ImageButton opportunityIcon;

        @BindView(R.id.textViewOpportunityPrimary) TextView opportunityName;
        @BindView(R.id.textViewOpportunitySecondary) TextView opportunityDescription;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView(final Opportunity item, List<Object> payloads)
        {
            // @TODO: This Icon should be parametric
            opportunityAvatar.setImageResource(R.drawable.common_google_signin_btn_icon_dark);

            opportunityName.setText(item.opportunityName);
            opportunityDescription.setText(item.opportunityDescription);

            opportunityIcon.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View v)
                {
                    // @TODO: Implement favourite button functionality
                }
            });
        }

        @Override
        public void unbindView(Opportunity item)
        {
            opportunityName.setText(null);
            opportunityDescription.setText(null);
        }
    }
}
