package agv.kaak.vn.kaioken.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import agv.kaak.vn.kaioken.R;
import agv.kaak.vn.kaioken.adapter.MoneyInMonthAdapter;
import agv.kaak.vn.kaioken.entity.SpentMoney;


/**
 * Created by shakutara on 10/8/17.
 */

public class DialogMoneyInMonth extends Dialog implements View.OnClickListener{

    TextView tvHeader;
    RecyclerView rvMoney;
    Button btnClose;

    String header;
    ArrayList<SpentMoney> spentMoneyList = new ArrayList<>();
    MoneyInMonthAdapter moneyInMonthAdapter;

    public DialogMoneyInMonth(@NonNull Context context, String header, ArrayList<SpentMoney> spentMoneyList) {
        super(context);
        this.header = header;
        this.spentMoneyList = spentMoneyList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_money_in_month);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        initControls();

        initEvents();

    }

    private void initEvents() {

        showHeader();
        showMoneyList();

        btnClose.setOnClickListener(this);

    }

    private void showMoneyList() {

        moneyInMonthAdapter = new MoneyInMonthAdapter(getContext(), spentMoneyList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvMoney.setLayoutManager(mLayoutManager);
        rvMoney.setItemAnimator(new DefaultItemAnimator());
        rvMoney.setAdapter(moneyInMonthAdapter);

    }

    private void showHeader() {

        if (!header.isEmpty())
            tvHeader.setText(header);

    }

    private void initControls() {

        tvHeader = (TextView) findViewById(R.id.tvHeader);
        rvMoney = (RecyclerView) findViewById(R.id.rvMoney);
        btnClose = (Button) findViewById(R.id.btnClose);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnClose:
                closeDialog();
                break;
        }
    }

    private void closeDialog() {

        dismiss();

    }
}
