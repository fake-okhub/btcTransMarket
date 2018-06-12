package com.android.bitglobal.fragment.trans;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.entity.EntrustOrder;
import com.android.bitglobal.entity.EntrustOrderResult;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.tool.Utils;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExchangeOrderDetailTradeFragment#newFrg(String, String, String)} factory method to
 * create an instance of this fragment.
 */
public class ExchangeOrderDetailTradeFragment extends BaseFragment {
    private static final String ARG_ID = "id";
    private static final String ARG_CURRENCY_TYPE = "c_type";
    private static final String ARG_EXCHANGE_TYPE = "e_type";

    private String mOrderId;
    private String mCurrencyType;
    private String mExchangeType;

    private TextView mTipTv;
    private FrameLayout mTitleFl;
    private List<EntrustOrder> mOrderDataList = new ArrayList<>();
    private QuickAdapter<EntrustOrder> mAdapter;

    public ExchangeOrderDetailTradeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ExchangeOrderDetailTradeFragment.
     */
    public static ExchangeOrderDetailTradeFragment newFrg(String param1, String c_type, String e_type) {
        ExchangeOrderDetailTradeFragment fragment = new ExchangeOrderDetailTradeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, param1);
        args.putString(ARG_CURRENCY_TYPE, c_type);
        args.putString(ARG_EXCHANGE_TYPE, e_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOrderId = getArguments().getString(ARG_ID);
            mCurrencyType = getArguments().getString(ARG_CURRENCY_TYPE);
            mExchangeType = getArguments().getString(ARG_EXCHANGE_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exchange_order_detail_trade, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        String str;
        TextView view = (TextView) rootView
                .findViewById(R.id.fragment_exchange_order_detail_trade_amount_title);
        str = getResources().getString(R.string.market_detail_trade_amount) + "("
                + mCurrencyType.toUpperCase() + ")";
        view.setText(str);
        view = (TextView) rootView
                .findViewById(R.id.fragment_exchange_order_detail_trade_price_title);
        str = getResources().getString(R.string.exchange_order_detail_info_price) + "("
                + mExchangeType.toUpperCase() + ")";
        view.setText(str);

        mTitleFl = (FrameLayout) rootView.findViewById(R.id.fragment_exchange_order_detail_trade_title);
        mTipTv = (TextView) rootView.findViewById(R.id.fragment_exchange_order_detail_trade_no_ts);
        ListView mOrderListView = (ListView) rootView.findViewById(R.id.fragment_exchange_order_detail_trade_listview);
        mAdapter = new QuickAdapter<EntrustOrder>(getActivity(), R.layout.exchange_order_detail_trade_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final EntrustOrder item) {

                if ((helper.getPosition() % 2) == 1) {
                    helper.setBackgroundColor(R.id.fragment_exchange_order_detail_trade_item_layout,
                            Color.rgb(0xf5, 0xf6, 0xf7));
                } else {
                    helper.setBackgroundColor(R.id.fragment_exchange_order_detail_trade_item_layout,
                            Color.rgb(0xff, 0xff, 0xff));
                }
                helper.setText(R.id.fragment_exchange_order_detail_trade_item_amount,
                        format(item.getTransactionNumber(), Utils.getPrecisionAmount(mCurrencyType, mExchangeType)));
                helper.setText(R.id.fragment_exchange_order_detail_trade_item_time,
                        StringUtils.dateFormat8(item.getTransactionTime()));
                helper.setText(R.id.fragment_exchange_order_detail_trade_item_price,
                        format(item.getTransactionPrice(), Utils.getPrecisionExchange(mCurrencyType, mExchangeType)));

            }
        };
        mOrderListView.setAdapter(mAdapter);
        SubscriberOnNextListener mEntrustDetailsOnNext = new SubscriberOnNextListener<EntrustOrderResult>() {
            @Override
            public void onNext(EntrustOrderResult entrustOrderResult) {
                mAdapter.clear();
                mOrderDataList.clear();
                mOrderDataList.addAll(entrustOrderResult.getEntrustOrders());
                mAdapter.addAll(mOrderDataList);
                if (mOrderDataList.size() > 0) {
                    mTipTv.setVisibility(View.GONE);
                    mTitleFl.setVisibility(View.VISIBLE);
                } else {
                    mTipTv.setVisibility(View.VISIBLE);
                    mTitleFl.setVisibility(View.GONE);
                }
            }
        };
        if (StringUtils.isEmpty(mOrderId)) {
            mTipTv.setVisibility(View.VISIBLE);
            mTitleFl.setVisibility(View.GONE);
        } else {
            ArrayList<String> list = new ArrayList<>();
            list.add(mCurrencyType);
            list.add(mExchangeType);
            list.add(mOrderId);
            HttpMethods.getInstance(1).entrustDetails(new ProgressSubscriber(mEntrustDetailsOnNext,
                    getActivity()), list);
        }
    }

}
