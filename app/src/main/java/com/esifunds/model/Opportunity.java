package com.esifunds.model;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.esifunds.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialize.holder.StringHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Opportunity extends AbstractItem<Opportunity, Opportunity.ViewHolder>
{
    public StringHolder opportunityName;
    public StringHolder opportunityDescription;

    public Opportunity()
    {
    }

    public Opportunity(String opportunityName, String opportunityDescription)
    {
        this.opportunityName = new StringHolder(opportunityName);
        this.opportunityDescription = new StringHolder(opportunityDescription);
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
        public void bindView(Opportunity item, List<Object> payloads)
        {
            // @TODO: This Icon should be parametric
            opportunityAvatar.setImageResource(R.drawable.common_google_signin_btn_icon_dark);

            StringHolder.applyTo(item.opportunityName, opportunityName);
            StringHolder.applyToOrHide(item.opportunityDescription, opportunityDescription);
        }

        @Override
        public void unbindView(Opportunity item)
        {
            opportunityName.setText(null);
            opportunityDescription.setText(null);
        }
    }
}
