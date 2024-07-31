package agv.kaak.vn.kaioken.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import agv.kaak.vn.kaioken.R;
import agv.kaak.vn.kaioken.entity.response.wallet.ItemWallet;
import agv.kaak.vn.kaioken.widget.InputText;


/**
 * Created by shakutara on 10/8/17.
 */

public class DialogAddMoney extends BaseDialog implements View.OnClickListener {

    Button btnSave;
    RadioGroup rgMoney;
    RadioButton rbSpend, rbGet, rbLoan, rbDebt;
    InputText edtContentMoney, edtMoney;

    DialogAddMoneyCallback callback;

    public DialogAddMoney(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_money);

        if (getWindow() != null)
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        initControls();

        initEvents();

    }

    private void initEvents() {

        showHeaderTitle(getContext().getString(R.string.finance_add_money));

        rbSpend.setChecked(true);

        btnSave.setOnClickListener(this);

    }

    private void initControls() {

        btnSave = findViewById(R.id.btnSave);
        rgMoney = findViewById(R.id.rgMoney);
        rbDebt = findViewById(R.id.rbDebtMoney);
        rbGet = findViewById(R.id.rbGetMoney);
        rbLoan = findViewById(R.id.rbLoanMoney);
        rbSpend = findViewById(R.id.rbSpendMoney);
        edtContentMoney = findViewById(R.id.edtContentMoney);
        edtMoney = findViewById(R.id.edtMoney);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                saveMoney();
                break;
        }
    }

    private void saveMoney() {

        String content = edtContentMoney.getContentText();
        String money = edtMoney.getContentText();

        int radioSelectedID = rgMoney.getCheckedRadioButtonId();

//        get current day
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateText = sdf.format(date);

//        get wallet type
        String type = "1";
        if (rbGet.isChecked())
            type = "1";
        else if (rbSpend.isChecked())
            type = "2";
        else if (rbDebt.isChecked())
            type = "3";
        else if (rbLoan.isChecked())
            type = "4";

        if (!content.isEmpty() && !money.isEmpty() && radioSelectedID != 0) {
            callback.getNewMoney(new ItemWallet(type, edtContentMoney.getContentText(), edtMoney.getContentText(), dateText));
            dismiss();
        } else
            showMessage(getContext().getString(R.string.all_must_to_enter));

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

//    ////////////////

    public interface DialogAddMoneyCallback {
        void getNewMoney(ItemWallet itemWallet);
    }

    public void getCallbackDialogAddMoney(DialogAddMoneyCallback callback) {
        this.callback = callback;
    }

//    ////////////////
}
