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
    private long ID_OPPORTUNITA;
    private String CODICE_PROGRAMMA;
    private String PROGRAMMA;
    private String FONDO;
    private long DATA_PUBBLICAZIONE;
    private long DATA_SCADENZA;
    private String PROROGA_SCADENZA;
    private String OGGETTO;
    private String IMPORTO;
    private String TIPO_OPPORTUNITA;
    private String TEMA_SINTETICO;
    private String TIPOLOGIA_BENEFICIARI;
    private String LINK;

    public Opportunity()
    {
    }

    public Opportunity(int ID_OPPORTUNITA, String CODICE_PROGRAMMA, String PROGRAMMA, String FONDO, long DATA_PUBBLICAZIONE, long DATA_SCADENZA, String PROROGA_SCADENZA, String OGGETTO, String IMPORTO, String TIPO_OPPORTUNITA, String TEMA_SINTETICO, String TIPOLOGIA_BENEFICIARI, String LINK)
    {
        this.ID_OPPORTUNITA = ID_OPPORTUNITA;
        this.CODICE_PROGRAMMA = CODICE_PROGRAMMA;
        this.PROGRAMMA = PROGRAMMA;
        this.FONDO = FONDO;
        this.DATA_PUBBLICAZIONE = DATA_PUBBLICAZIONE;
        this.DATA_SCADENZA = DATA_SCADENZA;
        this.PROROGA_SCADENZA = PROROGA_SCADENZA;
        this.OGGETTO = OGGETTO;
        this.IMPORTO = IMPORTO;
        this.TIPO_OPPORTUNITA = TIPO_OPPORTUNITA;
        this.TEMA_SINTETICO = TEMA_SINTETICO;
        this.TIPOLOGIA_BENEFICIARI = TIPOLOGIA_BENEFICIARI;
        this.LINK = LINK;
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

            opportunityName.setText(item.getCODICE_PROGRAMMA());
            opportunityDescription.setText(item.getOGGETTO());

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

    public long getID_OPPORTUNITA()
    {
        return ID_OPPORTUNITA;
    }

    public String getCODICE_PROGRAMMA()
    {
        return CODICE_PROGRAMMA;
    }

    public String getPROGRAMMA()
    {
        return PROGRAMMA;
    }

    public String getFONDO()
    {
        return FONDO;
    }

    public long getDATA_PUBBLICAZIONE()
    {
        return DATA_PUBBLICAZIONE;
    }

    public long getDATA_SCADENZA()
    {
        return DATA_SCADENZA;
    }

    public String getPROROGA_SCADENZA()
    {
        return PROROGA_SCADENZA;
    }

    public String getOGGETTO()
    {
        return OGGETTO;
    }

    public String getIMPORTO()
    {
        return IMPORTO;
    }

    public String getTIPO_OPPORTUNITA()
    {
        return TIPO_OPPORTUNITA;
    }

    public String getTEMA_SINTETICO()
    {
        return TEMA_SINTETICO;
    }

    public String getTIPOLOGIA_BENEFICIARI()
    {
        return TIPOLOGIA_BENEFICIARI;
    }

    public String getLINK()
    {
        return LINK;
    }
}
