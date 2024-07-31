package agv.kaak.vn.kaioken.fragment.finance;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import agv.kaak.vn.kaioken.R;
import agv.kaak.vn.kaioken.activity.FinanceChartActivity;
import agv.kaak.vn.kaioken.adapter.MoneyListAdapter;
import agv.kaak.vn.kaioken.dialog.DialogAddMoney;
import agv.kaak.vn.kaioken.dialog.DialogBillDetail;
import agv.kaak.vn.kaioken.dialog.DialogCalendar;
import agv.kaak.vn.kaioken.dialog.DialogMoneyInMonth;
import agv.kaak.vn.kaioken.dialog.DialogMoneyLimit;
import agv.kaak.vn.kaioken.entity.BillDetailInfo;
import agv.kaak.vn.kaioken.entity.DetailWallet;
import agv.kaak.vn.kaioken.entity.MoneyLimit;
import agv.kaak.vn.kaioken.entity.SpentMoney;
import agv.kaak.vn.kaioken.fragment.base.BaseFragment_;
import agv.kaak.vn.kaioken.mvp.contract.FinanceDetailContract;
import agv.kaak.vn.kaioken.mvp.contract.FinanceManagementContract;
import agv.kaak.vn.kaioken.mvp.presenter.FinanceDetailPresenter;
import agv.kaak.vn.kaioken.mvp.presenter.FinanceManagementPresenter;
import agv.kaak.vn.kaioken.utils.Constraint;
import agv.kaak.vn.kaioken.entity.response.wallet.ItemWallet;
import agv.kaak.vn.kaioken.utils.recyclerview.RecyclerTouchListener;
import agv.kaak.vn.kaioken.widget.GridView_ShowAll;


/**
 * A simple {@link Fragment} subclass.
 */
public class FinanceFragment
        extends BaseFragment_
        implements FinanceManagementContract.View, View.OnClickListener, FinanceDetailFragment.OnDeleteMoneyListener,
        DialogMoneyLimit.DialogMoneyLimitCallback, MoneyListAdapter.MoneyListAdapterCallback,
        FinanceDetailContract.View {

    View view, loader, viewMenuTask;
    TextView tvContentMoneySpend, tvContentMoneyLoan, tvContentMoneyDebt, tvContentMoneyGet, tvMonth,
            tvWatchViaMonth;
    FrameLayout llCalendar;
    ImageView imgBack, imgCalendar;
    TabLayout tabLayout;
    GridView_ShowAll gvMonth;
    RelativeLayout rlMoneyMain;
    RecyclerView rvDetail;
    ViewPager viewpager, vpFinanceDetail;
    BottomNavigationView bottomNavigation;
    FinanceDetailFragment financeDetailFragment;

    CaldroidSampleCustomFragment caldroidFragment = new CaldroidSampleCustomFragment();
    FinanceDetailPagerAdapter financeDetailPagerAdapter;
    Calendar cal = Calendar.getInstance();
    HashMap<Date, ArrayList<ItemWallet>> data = new HashMap<>();

    ArrayList<ItemWallet> itemWallets = new ArrayList<>();
    ArrayList<ItemWallet> spentMoneyListInDay = new ArrayList<>();
    ArrayList<ItemWallet> getMoneyListInDay = new ArrayList<>();
    ArrayList<ItemWallet> loanMoneyListInDay = new ArrayList<>();
    ArrayList<ItemWallet> debtMoneyListInDay = new ArrayList<>();

    private int FINANCE_CUSTOMER = 1;
    private int FINANCE_CASHIER = 2;

    long totalSpendMoney = 0;
    long totalGetMoney = 0;
    long totalLoanMoney = 0;
    long totalDebtMoney = 0;

    SimpleDateFormat sdfDateFull = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfMonth = new SimpleDateFormat("MM/yyyy");
    SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

    Date selectedDate = Calendar.getInstance().getTime();
    Date currentDate = Calendar.getInstance().getTime();
    String dateText = "";
    String selectedDayText, selectedMonthText, selectedYearText;

    int walletType = 1;

    private FinanceManagementPresenter financeManagementPresenter = new FinanceManagementPresenter(this);
    private FinanceDetailPresenter financeDetailPresenter = new FinanceDetailPresenter(this);

//    public static FinanceFragment newInstance() {
//        FinanceFragment financeGuestFragment = new FinanceFragment();
//        return financeGuestFragment;
//    }

    public FinanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_finance_guest, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            Constraint.Companion.setIS_LIVING_CASHIER_TABLE(savedInstanceState.getBoolean("TAG_IS_LIVING_CASHIER_TABLE", false));
        }

        initControls();

        //  hide viewMenuTask when app is running on tablet
        /*if (Constraint.Companion.getUSER_ROLE() == TypeUser.CASHIER && Constraint.Companion.isTablet())
            viewMenuTask.setVisibility(View.GONE);
        else if (Constraint.Companion.getUSER_ROLE() == TypeUser.CUSTOMER && Constraint.Companion.isTablet())
            viewMenuTask.setVisibility(View.VISIBLE);*/

//        showCalendar();
        initMoneyListAdapter();
        showToolbarWithDate(currentDate, true);
        showTabsMoney();
        initSelectMonth();
        getCurrentDateFinance();

//        views click
        imgCalendar.setOnClickListener(this);
        tvWatchViaMonth.setOnClickListener(this);
        /*if (Constraint.Companion.isPhone())*/
            imgBack.setOnClickListener(this);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_limit:
                        showMoneyLimitDialog();
                        break;
                    case R.id.action_add:
                        addSpendMoney();
                        break;
                    case R.id.action_chart:
                        Bundle bundle = new Bundle();
                        if (!Constraint.Companion.getIS_LIVING_CASHIER_TABLE()) {
                            bundle.putInt("OWNER_ID", Integer.parseInt(Constraint.Companion.getUid()));
                            bundle.putInt("OWNER_TYPE_ID", FINANCE_CUSTOMER);
                        } else {
                            bundle.putInt("OWNER_ID", Integer.parseInt(Constraint.Companion.getSTORE_WORKING_ID()));
                            bundle.putInt("OWNER_TYPE_ID", FINANCE_CASHIER);
                        }
//                        String year = tvMonth.getText().toString();
//                        if (year.contains("/"))
//                            year = year.split("/")[1];
//                        bundle.putInt("YEAR", Integer.parseInt(year));
                        bundle.putInt("YEAR", Integer.parseInt(sdfYear.format(currentDate)));
                        routeToNewActivity(FinanceChartActivity.class, bundle);
                        break;
                }
                return true;
            }
        });

    }

    private void showToolbarWithDate(Date currentDate, boolean isFullDate) {
        if (isFullDate)
            tvMonth.setText(sdfDateFull.format(currentDate));
        else tvMonth.setText(sdfMonth.format(currentDate));
    }

    private void getCurrentDateFinance() {
        dateText = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);

        showLoading();
        /*financeManagementPresenter.getWallet(
                new SimpleDateFormat("yyyy-MM-dd").format(currentDate),
                FINANCE_CASHIER,
                Integer.parseInt(Constraint.Companion.getSTORE_WORKING_ID()),
                Constraint.Companion.getOFFSET(),
                Constraint.Companion.getLIMIT());*/
        financeManagementPresenter.getWallet(
                new SimpleDateFormat("yyyy-MM-dd").format(currentDate),
                FINANCE_CUSTOMER,
                Integer.parseInt(Constraint.Companion.getSTORE_WORKING_ID()),
                Constraint.Companion.getOFFSET(),
                Constraint.Companion.getLIMIT());
    }

    private void showMoneyLimitDialog() {

        DialogMoneyLimit dialogMoneyLimit = new DialogMoneyLimit(getContext());
        dialogMoneyLimit.show();

        dialogMoneyLimit.getCallbackDialogMoneyLimit(this);

    }

    private void initSelectMonth() {

        SelectMonthAdapter mPagerAdapter = new SelectMonthAdapter(getActivity().getSupportFragmentManager());
        viewpager.setAdapter(mPagerAdapter);
        viewpager.setCurrentItem(500, true);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int tempPosition = 500;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position > tempPosition) {
                    tvMonth.setText(Integer.parseInt(tvMonth.getText().toString()) + 1 + "");
                }
                if (position < tempPosition) {
                    tvMonth.setText(Integer.parseInt(tvMonth.getText().toString()) - 1 + "");
                }
                tempPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void showTabsMoney() {

//        Get money
        View tabGetMoney = getActivity().getLayoutInflater().from(getContext()).inflate(R.layout.item_tabs_money, null);
        TextView tvTitleMoneyGet = tabGetMoney.findViewById(R.id.tvTitleMoney);
        tvContentMoneyGet = tabGetMoney.findViewById(R.id.tvTotalMoney);
        tvTitleMoneyGet.setText(getString(R.string.finance_get_money));

//        Spent money
        View tabSpendMoney = getActivity().getLayoutInflater().from(getContext()).inflate(R.layout.item_tabs_money, null);
        TextView tvTitleMoneySpend = tabSpendMoney.findViewById(R.id.tvTitleMoney);
        tvContentMoneySpend = tabSpendMoney.findViewById(R.id.tvTotalMoney);
        tvTitleMoneySpend.setText(getString(R.string.finance_spent_money));

//        Loan money
        View tabLoanMoney = getActivity().getLayoutInflater().from(getContext()).inflate(R.layout.item_tabs_money, null);
        TextView tvTitleMoneyLoan = tabLoanMoney.findViewById(R.id.tvTitleMoney);
        tvContentMoneyLoan = tabLoanMoney.findViewById(R.id.tvTotalMoney);
        tvTitleMoneyLoan.setText(getString(R.string.finance_loan));

//        Debt money
        View tabDebtMoney = getActivity().getLayoutInflater().from(getContext()).inflate(R.layout.item_tabs_money, null);
        TextView tvTitleMoneyDebt = tabDebtMoney.findViewById(R.id.tvTitleMoney);
        tvContentMoneyDebt = tabDebtMoney.findViewById(R.id.tvTotalMoney);
        tvTitleMoneyDebt.setText(getString(R.string.finance_debt));

        tabLayout.addTab(tabLayout.newTab().setCustomView(tabGetMoney));
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabSpendMoney));
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabDebtMoney));
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabLoanMoney));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

//        set First tab is selected
        tabLayout.getTabAt(0).select();

        //  tabLayout select tab listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        walletType = Integer.parseInt(Constraint.Companion.getWALLET_TYPE_COLLECT());
                        break;
                    case 1:
                        walletType = Integer.parseInt(Constraint.Companion.getWALLET_TYPE_SPEND());
                        break;
                    case 2:
                        walletType = Integer.parseInt(Constraint.Companion.getWALLET_TYPE_DEBT());
                        break;
                    case 3:
                        walletType = Integer.parseInt(Constraint.Companion.getWALLET_TYPE_LOAN());
                        break;
                }

                refreshRecyclerView();

                showLoading();
                /*financeManagementPresenter.getWalletListViaType(
                        dateText,
                        FINANCE_CASHIER,
                        Integer.parseInt(Constraint.Companion.getSTORE_WORKING_ID()),
                        walletType,
                        Constraint.Companion.getOFFSET(),
                        Constraint.Companion.getLIMIT());*/
                financeManagementPresenter.getWalletListViaType(
                        dateText,
                        FINANCE_CUSTOMER,
                        Integer.parseInt(Constraint.Companion.getSTORE_WORKING_ID()),
                        walletType,
                        Constraint.Companion.getOFFSET(),
                        Constraint.Companion.getLIMIT());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int walletType = 0;
                switch (tab.getPosition()) {
                    case 0:
                        walletType = Integer.parseInt(Constraint.Companion.getWALLET_TYPE_COLLECT());
                        break;
                    case 1:
                        walletType = Integer.parseInt(Constraint.Companion.getWALLET_TYPE_SPEND());
                        break;
                    case 2:
                        walletType = Integer.parseInt(Constraint.Companion.getWALLET_TYPE_DEBT());
                        break;
                    case 3:
                        walletType = Integer.parseInt(Constraint.Companion.getWALLET_TYPE_LOAN());
                        break;
                }

                refreshRecyclerView();

                showLoading();
                /*financeManagementPresenter.getWalletListViaType(
                        dateText,
                        FINANCE_CASHIER,
                        Integer.parseInt(Constraint.Companion.getSTORE_WORKING_ID()),
                        walletType,
                        Constraint.Companion.getOFFSET(),
                        Constraint.Companion.getLIMIT());*/
                financeManagementPresenter.getWalletListViaType(
                        dateText,
                        FINANCE_CUSTOMER,
                        Integer.parseInt(Constraint.Companion.getSTORE_WORKING_ID()),
                        walletType,
                        Constraint.Companion.getOFFSET(),
                        Constraint.Companion.getLIMIT());
            }
        });

    }

    private void refreshRecyclerView() {
        //  delete old data
        itemWallets.clear();
        moneyListAdapter.notifyDataSetChanged();
        rvDetail.invalidate();
        rvDetail.requestLayout();
    }

    private void showCalendar() {

        Bundle args = new Bundle();

        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, false);
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
        t.replace(R.id.layout_calendar, caldroidFragment);
        t.commit();

//        init custom data to CalDroid
        Map<String, Object> extraData = caldroidFragment.getExtraData();
        extraData.put("DATA_CALDROID", data);
        caldroidFragment.refreshView();

    }

    private void initControls() {
        //viewMenuTask = view.findViewById(R.id.viewMenuTask);
        tabLayout = view.findViewById(R.id.tab_layout);
        tvMonth = view.findViewById(R.id.tvMonth);
        tvWatchViaMonth = view.findViewById(R.id.tvWatchViaMonth);
        llCalendar = view.findViewById(R.id.layout_calendar);
        gvMonth = view.findViewById(R.id.gvMonth);
        viewpager = view.findViewById(R.id.viewpager);
        vpFinanceDetail = view.findViewById(R.id.vpFinanceDetail);
        loader = view.findViewById(R.id.loader);
        bottomNavigation = view.findViewById(R.id.navigation);
        rlMoneyMain = view.findViewById(R.id.rlMoneyMain);
        imgBack = view.findViewById(R.id.imgBack);
        imgCalendar = view.findViewById(R.id.imgCalendar);
        rvDetail = view.findViewById(R.id.rvDetail);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvWatchViaMonth:
                showFinanceViaMonth();
                break;
            case R.id.imgBack:
                if (getActivity() != null)
                    getActivity().onBackPressed();
                break;
            case R.id.imgCalendar:
                showDialogCalendar();
                break;
        }
    }

    private void showDialogCalendar() {
        if (getActivity() == null)
            return;

        DialogCalendar dialogCalendar = new DialogCalendar();
        dialogCalendar.show(getActivity().getSupportFragmentManager(), DialogCalendar.class.getSimpleName());
        dialogCalendar.getDialogCalendarListener(new DialogCalendar.DialogCalendarListener() {
            @Override
            public void getSelectedDate(String dateText) {

                //  reset some fields load more
                previousTotal = 0;
                offset = Constraint.Companion.getOFFSET();
                limit = Constraint.Companion.getLIMIT();

                //  auto select first tab
                TabLayout.Tab tab = tabLayout.getTabAt(0);
                if (tab != null)
                    tab.select();

                FinanceFragment.this.dateText = dateText;
                updateTitle(dateText);

                showLoading();
                /*financeManagementPresenter.getWallet(
                        dateText,
                        FINANCE_CASHIER,
                        Integer.parseInt(Constraint.Companion.getSTORE_WORKING_ID()),
                        Constraint.Companion.getOFFSET(),
                        Constraint.Companion.getLIMIT());*/
                financeManagementPresenter.getWallet(
                        dateText,
                        FINANCE_CUSTOMER,
                        Integer.parseInt(Constraint.Companion.getSTORE_WORKING_ID()),
                        Constraint.Companion.getOFFSET(),
                        Constraint.Companion.getLIMIT());
//                if (wallet.getDetail() == null)
//                    return;
//
//                FinanceFragment.this.selectedDate = selectedDate;
//                showToolbarWithDate(selectedDate, isFullDate);
//
//                String[] splitDate;
//
//                for (ItemWallet itemWallet : wallet.getDetail()) {
//                    if (itemWallet.getDate() != null) {
//                        splitDate = itemWallet.getDate().split(" ");
//
//                        data.put(parseDate(splitDate[0]), (ArrayList<ItemWallet>) wallet.getDetail());
//                        caldroidFragment.refreshView();
//
//                        caldroidFragment.clearSelectedDates();
//                        caldroidFragment.setSelectedDate(selectedDate);
//                    }
//                }
//
//                totalGetMoney = Long.parseLong(wallet.getTotalThu());
//                totalSpendMoney = Long.parseLong(wallet.getTotalChi());
//                totalLoanMoney = Long.parseLong(wallet.getTotalChomuon());
//                totalDebtMoney = Long.parseLong(wallet.getTotalMuon());
//                updateTotalMoneySpan();
//
//                showMoneyFromCalendar((ArrayList<ItemWallet>) wallet.getDetail());
//
//                //  change wallet limit 's title
//                if (wallet.getDinhMuc() != null)
//                    bottomNavigation.getMenu().getItem(0).setTitle(getString(R.string.format_money_long, wallet.getDinhMuc()));
//                else
//                    bottomNavigation.getMenu().getItem(0).setTitle("");
            }
        });
    }

    private void updateTitle(String dateText) {
        String[] a = dateText.split("-");
        if (a.length == 2)
            tvMonth.setText(a[1] + "/" + a[0]);
        else tvMonth.setText(a[2] + "/" + a[1] + "/" + a[0]);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("TAG_IS_LIVING_CASHIER_TABLE", Constraint.Companion.getIS_LIVING_CASHIER_TABLE());
        super.onSaveInstanceState(outState);
    }

    boolean isFinanceMonth = true;

    private void showFinanceViaMonth() {

        if (!isFinanceMonth) {
            tvMonth.setText(sdfMonth.format(currentDate));
            llCalendar.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.GONE);
            tvWatchViaMonth.setText(getString(R.string.finance_watch_via_month));
            isFinanceMonth = true;
        } else {
            tvMonth.setText(sdfYear.format(currentDate));
            llCalendar.setVisibility(View.GONE);
            viewpager.setVisibility(View.VISIBLE);
            tvWatchViaMonth.setText(getString(R.string.finance_watch_via_day));
            isFinanceMonth = false;
        }

    }

    private void showMoneyInMonthDialog(String header, ArrayList<SpentMoney> spentMoneyList) {

        DialogMoneyInMonth dialogMoneyInMonth = new DialogMoneyInMonth(getContext(),
                header, spentMoneyList);
        dialogMoneyInMonth.show();

    }

    private void addSpendMoney() {

////        check whether selected day is today
//        String selectedDateFormatted = "";
//        if (selectedDate != null)
//            selectedDateFormatted = sdfDateFull.format(selectedDate);
//        String currentDateFormatted = sdfDateFull.format(currentDate);
//        if (selectedDateFormatted.isEmpty() && llCalendar.getVisibility() == View.VISIBLE) {
//            showMessage(getString(R.string.you_have_to_select_days));
//            return;
//        } else if (selectedDateFormatted.compareTo(currentDateFormatted) != 0 && llCalendar.getVisibility() == View.VISIBLE) {
//            showMessage(getString(R.string.you_have_not_to_select_difference_from_today));
//            return;
//        }

        String currentDateFormatted = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
        if (dateText.compareTo(currentDateFormatted) != 0) {
            showMessage(getString(R.string.finance_you_have_not_to_select_difference_from_today));
            return;
        }

        DialogAddMoney dialogAddMoney = new DialogAddMoney(getContext());
        dialogAddMoney.show();

        dialogAddMoney.getCallbackDialogAddMoney(money -> {

            showLoading();
            //if (Constraint.Companion.getUSER_ROLE() == TypeUser.CUSTOMER)
                financeManagementPresenter.saveWallet(money.getWalletType(), money.getTitle(), money.getAmount(), money.getDate(), FINANCE_CUSTOMER, null);
            //else
            //    financeManagementPresenter.saveWallet(money.getWalletType(), money.getTitle(), money.getAmount(), money.getDate(), FINANCE_CASHIER, Integer.parseInt(Constraint.Companion.getSTORE_WORKING_ID()));

        });

    }

    private MoneyListAdapter moneyListAdapter;

    //    recycler variables
    private boolean loading = true;
    private int firstVisibleItem = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int previousTotal = 0;
    private int offset = Constraint.Companion.getOFFSET();
    private int limit = Constraint.Companion.getLIMIT();

    LinearLayoutManager layoutManager;

    private void initMoneyListAdapter() {

        if (getContext() == null)
            return;

        //  init adapter
        moneyListAdapter = new MoneyListAdapter(getContext(), itemWallets);
        layoutManager = new LinearLayoutManager(getContext());
        rvDetail.setLayoutManager(layoutManager);
        rvDetail.setItemAnimator(new DefaultItemAnimator());
        rvDetail.setAdapter(moneyListAdapter);

        //  items click
        rvDetail.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvDetail, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(@NotNull View view, int position) {
                if (itemWallets.get(position).getInvoiceNo() != null) {
                    showLoading();
                    financeDetailPresenter.getBillDetail(itemWallets.get(position).getInvoiceNo());
                } else showMessage(getString(R.string.all_cannot_process));
            }

            @Override
            public void onLongClick(@NotNull View view, int position) {

            }
        }));

        //  load more
        rvDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = rvDetail.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + Constraint.Companion.getVISIBLE_THRESHOLD())) {
                    int initialSize = itemWallets.size();
                    offset += Constraint.Companion.getLIMIT();
                    limit = offset + Constraint.Companion.getLIMIT();

                    showLoading();
                    financeManagementPresenter.getWalletListViaType(
                            dateText,
                            FINANCE_CASHIER,
                            Integer.parseInt(Constraint.Companion.getSTORE_WORKING_ID()),
                            walletType,
                            offset,
                            limit
                    );

                    int updatedSize = itemWallets.size();
                    moneyListAdapter.notifyItemRangeInserted(initialSize, updatedSize);
                    loading = true;
                }
            }
        });
    }

    private void showMoneyFromCalendar(ArrayList<ItemWallet> itemWallets) {
        this.itemWallets.clear();
        this.itemWallets.addAll(itemWallets);
        moneyListAdapter.notifyDataSetChanged();

        spentMoneyListInDay.clear();
        getMoneyListInDay.clear();
        loanMoneyListInDay.clear();
        debtMoneyListInDay.clear();

        for (ItemWallet money : itemWallets) {
            if (Objects.equals(money.getWalletType(), Constraint.Companion.getWALLET_TYPE_SPEND())) {
                spentMoneyListInDay.add(money);
            } else if (Objects.equals(money.getWalletType(), Constraint.Companion.getWALLET_TYPE_COLLECT())) {
                getMoneyListInDay.add(money);
            } else if (Objects.equals(money.getWalletType(), Constraint.Companion.getWALLET_TYPE_LOAN())) {
                loanMoneyListInDay.add(money);
            } else {
                debtMoneyListInDay.add(money);
            }
        }

//        // setup Finance viewpager
//        financeDetailPagerAdapter = new FinanceDetailPagerAdapter(getActivity().getSupportFragmentManager());
//        vpFinanceDetail.setAdapter(financeDetailPagerAdapter);
//        tabLayout.setupWithViewPager(vpFinanceDetail);

//        //  setup custom tab
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            assert tab != null;
//            tab.setCustomView(getTabView(i));
//        }
    }

//    CaldroidListener listener = new CaldroidListener() {
//        View previousView = null;
//
//        @Override
//        public void onSelectDate(Date date, View view) {
//            if (previousView != null)
//                previousView.setBackgroundColor(Color.WHITE);
//            previousView = view;
//            view.setBackgroundColor(Color.GREEN);
//
//            selectedDate = date;
//            selectedDayText = sdfDateFull.format(date).split("/")[0];
//            selectedMonthText = sdfDateFull.format(date).split("/")[1];
//            selectedYearText = sdfDateFull.format(date).split("/")[2];
//
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//            showLoading();
//            if (!Constraint.Companion.getIS_LIVING_CASHIER_TABLE())
//                financeManagementPresenter.getWallet(sdf.format(date), FINANCE_CUSTOMER, null, null, null);
//            else
//                financeManagementPresenter.getWallet(sdf.format(date), FINANCE_CASHIER, Integer.parseInt(Constraint.Companion.getSTORE_WORKING_ID()), Constraint.Companion.getOFFSET(), Constraint.Companion.getLIMIT());
//
//        }
//
//        @Override
//        public void onChangeMonth(int month, int year) {
//
//            showLoading();
//            if (!Constraint.Companion.getIS_LIVING_CASHIER_TABLE())
//                financeManagementPresenter.getWallet(year + "-" + month, FINANCE_CUSTOMER, null, null, null);
//            else
//                financeManagementPresenter.getWallet(year + "-" + month, FINANCE_CASHIER, Integer.parseInt(Constraint.Companion.getSTORE_WORKING_ID()), Constraint.Companion.getOFFSET(), Constraint.Companion.getLIMIT());
//
//            String textTime = tvMonth.getText().toString();
//            if (textTime.contains(Constraint.Companion.getMONTH_YEAR()))
//                tvMonth.setText(month + Constraint.Companion.getMONTH_YEAR() + year);
//        }
//
//        @Override
//        public void onLongClickDate(Date date, View view) {
//
//        }
//
//        @Override
//        public void onCaldroidViewCreated() {
//            if (getActivity() == null)
//                return;
//
//            getActivity().findViewById(R.id.calendar_title_view).setVisibility(View.GONE);
//
//            //  show today's finance
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            showLoading();
//            if (!Constraint.Companion.getIS_LIVING_CASHIER_TABLE())
//                financeManagementPresenter.getWallet(sdf.format(currentDate), FINANCE_CUSTOMER, null, null, null);
//            else
//                financeManagementPresenter.getWallet(sdf.format(currentDate), FINANCE_CASHIER, Integer.parseInt(Constraint.Companion.getSTORE_WORKING_ID()), Constraint.Companion.getOFFSET(), 1000);
//        }
//
//    };

    @Override
    public void showLoading() {
        loader.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loader.setVisibility(View.GONE);
    }

    @Override
    public void getWalletSuccess(@org.jetbrains.annotations.Nullable DetailWallet wallet) {
        hideLoading();
        itemWallets.clear();
        itemWallets.addAll(wallet.getDetail());
        moneyListAdapter.notifyDataSetChanged();

//        if (wallet == null || wallet.getDetail() == null)
//            return;
//
//        String[] splitDate;
//
//        for (ItemWallet itemWallet : wallet.getDetail()) {
//            if (itemWallet.getDate() != null) {
//                splitDate = itemWallet.getDate().split(" ");
//
//                data.put(parseDate(splitDate[0]), (ArrayList<ItemWallet>) wallet.getDetail());
//                caldroidFragment.refreshView();
//
//                caldroidFragment.clearSelectedDates();
//                caldroidFragment.setSelectedDate(selectedDate);
//            }
//        }
//
        totalGetMoney = Long.parseLong(wallet.getTotalThu());
        totalSpendMoney = Long.parseLong(wallet.getTotalChi());
        totalLoanMoney = Long.parseLong(wallet.getTotalChomuon());
        totalDebtMoney = Long.parseLong(wallet.getTotalMuon());
        updateTotalMoneySpan();
//
//        showMoneyFromCalendar((ArrayList<ItemWallet>) wallet.getDetail());
//
        //  change wallet limit's title
        if (wallet.getDinhMuc() != null)
            bottomNavigation.getMenu().getItem(0).setTitle(getString(R.string.format_money_long, wallet.getDinhMuc()));
        else
            bottomNavigation.getMenu().getItem(0).setTitle("");
    }


    void updateTotalMoneySpan() {
        tvContentMoneySpend.setText(getString(R.string.format_money_long, totalSpendMoney));
        tvContentMoneyGet.setText(getString(R.string.format_money_long, totalGetMoney));
        tvContentMoneyLoan.setText(getString(R.string.format_money_long, totalLoanMoney));
        tvContentMoneyDebt.setText(getString(R.string.format_money_long, totalDebtMoney));
    }

    @Override
    public void getWalletFailed(@org.jetbrains.annotations.Nullable String msg) {
        hideLoading();
        showMessage(msg);
    }

    @Override
    public void saveWalletLimitSuccess(@org.jetbrains.annotations.Nullable MoneyLimit moneyLimit) {
        hideLoading();
        showMessage(getString(R.string.all_saved));

        if (moneyLimit == null || moneyLimit.getWalletLimit() == null)
            return;

        bottomNavigation.getMenu().getItem(0).setTitle(getString(R.string.format_money_long, Integer.parseInt(moneyLimit.getWalletLimit())));
    }

    @Override
    public void saveWalletLimitFailed(@org.jetbrains.annotations.Nullable String msg) {
        hideLoading();
        showMessage(msg);
    }

    @Override
    public void getMoneyLimit(@org.jetbrains.annotations.Nullable String money) {
        showLoading();
        if (money == null)
            return;
        if (!Constraint.Companion.getIS_LIVING_CASHIER_TABLE())
            financeManagementPresenter.saveWalletLimit(money, null, null);
        else
            financeManagementPresenter.saveWalletLimit(money, Integer.parseInt(Constraint.Companion.getSTORE_WORKING_ID()), Constraint.Companion.getWALLET_LIMIT_STORE_TYPE());
    }

    @Override
    public void saveWalletSuccess(@org.jetbrains.annotations.Nullable ItemWallet itemWallet) {
        hideLoading();

        if (itemWallet == null || itemWallet.getWalletType() == null)
            return;

        //  check whether over money limit
        /*if (Constraint.Companion.getUSER_ROLE() == TypeUser.CUSTOMER &&
                itemWallet.getMessage() != null &&
                !TextUtils.isEmpty(itemWallet.getMessage())) {
            showMessage(itemWallet.getMessage());
        }*/

        if (itemWallet.getMessage() != null &&
                !TextUtils.isEmpty(itemWallet.getMessage())) {
            showMessage(itemWallet.getMessage());
        }

        //  add new data to local list
        if (Objects.equals(String.valueOf(walletType), itemWallet.getWalletType())) {
            itemWallets.add(itemWallet);
            moneyListAdapter.notifyDataSetChanged();
        }

        //  update money header tabs
        switch (itemWallet.getWalletType()) {
            case "1":
                getMoneyListInDay.add(itemWallet);
                totalGetMoney += Long.parseLong(itemWallet.getAmount());
                break;
            case "2":
                spentMoneyListInDay.add(itemWallet);
                totalSpendMoney += Long.parseLong(itemWallet.getAmount());
                break;
            case "3":
                debtMoneyListInDay.add(itemWallet);
                totalDebtMoney += Long.parseLong(itemWallet.getAmount());
                break;
            case "4":
                loanMoneyListInDay.add(itemWallet);
                totalLoanMoney += Long.parseLong(itemWallet.getAmount());
                break;
        }

        updateTotalMoneySpan();

//        //  refresh finance detail viewpager
//        vpFinanceDetail.getAdapter().notifyDataSetChanged();
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            if (tab != null) {
//                tab.setCustomView(getTabView(i));
//            }
//        }
    }

    @Override
    public void saveWalletFailed(@org.jetbrains.annotations.Nullable String msg) {
        hideLoading();
        showMessage(msg);
    }

    @Override
    public void deleteMoneyItem(@NotNull ItemWallet itemWallet) {
        showLoading();
        financeManagementPresenter.deleteMoney(itemWallet.getId());
    }

    @Override
    public void deleteMoneySuccess(ItemWallet itemWallet) {
        hideLoading();
        showMessage(getString(R.string.all_deleted_successfully));

        if (itemWallet == null || itemWallet.getWalletType() == null)
            return;

        switch (itemWallet.getWalletType()) {
            case "1":
                for (int i = 0; i < getMoneyListInDay.size(); i++) {
                    if (getMoneyListInDay.get(i).getId().equals(itemWallet.getId()))
                        getMoneyListInDay.remove(i);
                }
                totalGetMoney -= Long.parseLong(itemWallet.getAmount());
                break;
            case "2":
                for (int i = 0; i < spentMoneyListInDay.size(); i++) {
                    if (spentMoneyListInDay.get(i).getId().equals(itemWallet.getId()))
                        spentMoneyListInDay.remove(i);
                }
                totalSpendMoney -= Long.parseLong(itemWallet.getAmount());
                break;
            case "3":
                for (int i = 0; i < debtMoneyListInDay.size(); i++) {
                    if (debtMoneyListInDay.get(i).getId().equals(itemWallet.getId()))
                        debtMoneyListInDay.remove(i);
                }
                totalDebtMoney -= Long.parseLong(itemWallet.getAmount());
                break;
            case "4":
                for (int i = 0; i < loanMoneyListInDay.size(); i++) {
                    if (loanMoneyListInDay.get(i).getId().equals(itemWallet.getId()))
                        loanMoneyListInDay.remove(i);
                }
                totalLoanMoney -= Long.parseLong(itemWallet.getAmount());
                break;
        }
        updateTotalMoneySpan();

//        //  refresh finance detail viewpager
//        vpFinanceDetail.getAdapter().notifyDataSetChanged();
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            if (tab != null) {
//                tab.setCustomView(getTabView(i));
//            }
//        }
    }

    @Override
    public void deleteMoneyFailed(@org.jetbrains.annotations.Nullable String msg) {
        hideLoading();
        showMessage(msg);
    }

    @Override
    public void getMoneyDeleted(@org.jetbrains.annotations.Nullable ItemWallet itemWallet) {
        showLoading();
        financeManagementPresenter.deleteMoney(itemWallet.getId());
    }

    @SuppressLint("SetTextI18n")
    View getTabView(int position) {

        ArrayList<Long> priceList = new ArrayList<>();
        priceList.add(totalSpendMoney);
        priceList.add(totalGetMoney);
        priceList.add(totalLoanMoney);
        priceList.add(totalDebtMoney);

        ArrayList<String> titleList = new ArrayList<>();
        titleList.add(getString(R.string.finance_spent_money));
        titleList.add(getString(R.string.finance_get_money));
        titleList.add(getString(R.string.finance_loan));
        titleList.add(getString(R.string.finance_debt));

        View v = LayoutInflater.from(getContext()).inflate(R.layout.item_tabs_money, null);
        TextView tvTitleMoney = v.findViewById(R.id.tvTitleMoney);
        TextView tvTotalMoney = v.findViewById(R.id.tvTotalMoney);
        tvTitleMoney.setText(titleList.get(position));
        tvTotalMoney.setText(getString(R.string.format_money_long, Integer.parseInt(priceList.get(position).toString())));
        return v;
    }

    @Override
    public void getWalletListViaTypeSuccess(@org.jetbrains.annotations.Nullable List<ItemWallet> itemWalletList) {
        hideLoading();
        itemWallets.addAll(itemWalletList);
        moneyListAdapter.notifyDataSetChanged();
//        showMoneyFromCalendar((ArrayList<ItemWallet>) itemWalletList);
    }

    @Override
    public void getWalletListViaTypeFailed(@org.jetbrains.annotations.Nullable String msg) {
        hideLoading();
        showMessage(msg);
    }

    @Override
    public void getBillDetailSuccess(@org.jetbrains.annotations.Nullable BillDetailInfo billDetailInfo) {
        hideLoading();

        if (getActivity() == null)
            return;

        DialogBillDetail dialogBillDetail = new DialogBillDetail();
        Bundle bundle = new Bundle();
        bundle.putSerializable("BILL_DETAIL", billDetailInfo);
        dialogBillDetail.setArguments(bundle);
        dialogBillDetail.show(getActivity().getSupportFragmentManager(), DialogBillDetail.class.getSimpleName());
    }


    @Override
    public void getBillDetailFailed(@org.jetbrains.annotations.Nullable String msg) {
        hideLoading();
        showMessage(msg);
    }

    private class SelectMonthAdapter extends FragmentStatePagerAdapter {

        int LOOPS_COUNT = 1000;

        SelectMonthAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            SelectMonthFragment selectMonthFragment = new SelectMonthFragment();
            selectMonthFragment.getCallbackSelectMonthFragment(new SelectMonthFragment.SelectMonthFragmentCallback() {
                @Override
                public void getSelectedMonth(@org.jetbrains.annotations.Nullable String month) {

                    selectedMonthText = month;
                    selectedYearText = tvMonth.getText().toString();

                    showLoading();
                    if (!Constraint.Companion.getIS_LIVING_CASHIER_TABLE())
                        financeManagementPresenter.getWallet(
                                tvMonth.getText().toString() + "-" + month,
                                FINANCE_CUSTOMER,
                                null,
                                null,
                                null);
                    else
                        financeManagementPresenter.getWallet(
                                tvMonth.getText().toString() + "-" + month,
                                FINANCE_CASHIER,
                                Integer.parseInt(Constraint.Companion.getSTORE_WORKING_ID()),
                                Constraint.Companion.getOFFSET(),
                                Constraint.Companion.getLIMIT());
                }
            });
            return selectMonthFragment;
        }

        @Override
        public int getCount() {
            return LOOPS_COUNT;
        }
    }

    private class FinanceDetailPagerAdapter extends FragmentStatePagerAdapter {

        FinanceDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ArrayList<ItemWallet> itemWallets = new ArrayList<>();
            switch (position) {
                case 0: {
                    itemWallets.addAll(getMoneyListInDay);
                    break;
                }
                case 1: {
                    itemWallets.addAll(spentMoneyListInDay);
                    break;
                }
                case 2: {
                    itemWallets.addAll(loanMoneyListInDay);
                    break;
                }
                case 3: {
                    itemWallets.addAll(debtMoneyListInDay);
                    break;
                }
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("ITEM_WALLET_LIST", itemWallets);
            financeDetailFragment = new FinanceDetailFragment();
            financeDetailFragment.setArguments(bundle);
            financeDetailFragment.setOnDeleteMoneyListener(FinanceFragment.this::getMoneyDeleted);
            return financeDetailFragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

    }
}
