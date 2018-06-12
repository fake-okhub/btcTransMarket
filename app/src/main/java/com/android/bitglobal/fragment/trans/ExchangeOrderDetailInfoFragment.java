package com.android.bitglobal.fragment.trans;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.tool.Utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExchangeOrderDetailInfoFragment#newFrg} factory method to
 * create an instance of this fragment.
 */
public class ExchangeOrderDetailInfoFragment extends BaseFragment {
    private static final String ARG_INFO = "order_info";
    private static final String ARG_CANCEL = "order_cancel";

    private ExchangeOrderDetailInfoData mInfo;
    private OnExchangeOrderDetailCallback mListener;

    public ExchangeOrderDetailInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param b
     * @return A new instance of fragment ExchangeOrderDetailInfoFragment.
     */
    public static ExchangeOrderDetailInfoFragment newFrg(ExchangeOrderDetailInfoData param1, boolean b) {
        ExchangeOrderDetailInfoFragment fragment = new ExchangeOrderDetailInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_INFO, param1);
        args.putBoolean(ARG_CANCEL, b);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mInfo = (ExchangeOrderDetailInfoData) getArguments().getSerializable(ARG_INFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exchange_order_detail_info, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        // 日期
        TextView view;
        String str;
        view = (TextView) rootView.findViewById(R.id.fragment_exchange_order_detail_info_date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        view.setText(simpleDateFormat.format(new Date(Long.valueOf(mInfo.getDate()))));
        // 类型
        view = (TextView) rootView.findViewById(R.id.fragment_exchange_order_detail_info_type);
        if ("0".equals(mInfo.getType())) {
            view.setText(getResources().getString(R.string.exchange_order_detail_info_type_sell));
            view.setBackground(ContextCompat.getDrawable(getActivity(),
                    R.drawable.bnt_trans_sell_solid));
        } else {
            view.setText(getResources().getString(R.string.exchange_order_detail_info_type_buy));
            view.setBackground(ContextCompat.getDrawable(getActivity(),
                    R.drawable.bnt_trans_buy_solid));

        }
        // 价格
        view = (TextView) rootView.findViewById(R.id.fragment_exchange_order_detail_info_price);
        view.setText(format(mInfo.getPrice(), Utils.getPrecisionExchange(mInfo.getCurrencyType(), mInfo.getExchangeType())));
        view = (TextView) rootView.findViewById(R.id.fragment_exchange_order_detail_info_price_title);
        str = getResources().getString(R.string.exchange_order_detail_info_price)
                + "(" + mInfo.getExchangeType().toUpperCase() + ")";
        view.setText(str);
        // 均价
        view = (TextView) rootView.findViewById(R.id.fragment_exchange_order_detail_info_avg_price);
        view.setText(format(mInfo.getAvgPrice(), Utils.getPrecisionExchange(mInfo.getCurrencyType(), mInfo.getExchangeType())));
        view = (TextView) rootView.findViewById(R.id.fragment_exchange_order_detail_info_avg_price_title);
        str = getResources().getString(R.string.exchange_order_detail_info_avg_price) + "("
                + mInfo.getExchangeType().toUpperCase() + ")";
        view.setText(str);
        // 总数
        view = (TextView) rootView.findViewById(R.id.fragment_exchange_order_detail_info_amount);
        view.setText(format(mInfo.getAmount(), Utils.getPrecisionAmount(mInfo.getCurrencyType(), mInfo.getExchangeType())));
        view = (TextView) rootView.findViewById(R.id.fragment_exchange_order_detail_info_amount_title);
        str = getResources().getString(R.string.exchange_order_detail_info_amount) + "("
                + mInfo.getCurrencyType().toUpperCase() + ")";
        view.setText(str);
        // 完成度
        view = (TextView) rootView.findViewById(R.id.fragment_exchange_order_detail_info_finish);
        view.setText(mInfo.getFinished());
        view = (TextView) rootView.findViewById(R.id.fragment_exchange_order_detail_info_finished_title);
        str = getResources().getString(R.string.exchange_order_detail_info_finished) + "("
                + mInfo.getCurrencyType().toUpperCase() + ")";
        view.setText(str);
        // 总额
        view = (TextView) rootView.findViewById(R.id.fragment_exchange_order_detail_info_total);
        //2017.8.7 修改总额不拦截小数位数
        view.setText(format(mInfo.getTotal(), Utils.getPrecisionExchange(mInfo.getCurrencyType(), mInfo.getExchangeType())));
//        view.setText(mInfo.getTotal());
        view = (TextView) rootView.findViewById(R.id.fragment_exchange_order_detail_info_total_title);
        str = getResources().getString(R.string.exchange_order_detail_info_total) + "("
                + mInfo.getExchangeType().toUpperCase() + ")";
        view.setText(str);

        if (getArguments() != null && getArguments().getBoolean(ARG_CANCEL)) {
            rootView.findViewById(R.id.fragment_exchange_order_detail_info_cancel_order)
                    .setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.fragment_exchange_order_detail_info_cancel_order)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showCancelDialog();
                        }
                    });
        }
    }

    private void showCancelDialog() {
        try {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View root = inflater.inflate(R.layout.dialog_confirm, null);
            final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setView(root);
            dialog.setCancelable(false);
            TextView description = (TextView) root.findViewById(R.id.dialog_confirm_content);
            String str = getString(R.string.dialog_cancel_order_content);
            description.setText(str);
            root.findViewById(R.id.dialog_confirm_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            TextView confirm = (TextView) root.findViewById(R.id.dialog_confirm_confirm);
            confirm.setText(getString(R.string.dialog_confirm));
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCancel();
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExchangeOrderDetailCallback) {
            mListener = (OnExchangeOrderDetailCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnExchangeOrderDetailCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static class ExchangeOrderDetailInfoData implements Serializable {
        private String mDate = "";
        private String mType = "";
        private String mPrice = "";
        private double mAvgPrice;
        private String mAmount = "";
        private String mFinished = "";
        private String mTotal = "";
        private String mCurrencyType = "";
        private String mExchangeType = "";
        private String mOrderId = "";

        public String getDate() {
            return mDate;
        }

        public void setDate(String mDate) {
            this.mDate = mDate;
        }

        public String getType() {
            return mType;
        }

        public void setType(String mType) {
            this.mType = mType;
        }

        public String getPrice() {
            return mPrice;
        }

        public void setPrice(String mPrice) {
            this.mPrice = mPrice;
        }

        double getAvgPrice() {
            return mAvgPrice;
        }

        public void setAvgPrice(double mAvgPrice) {
            this.mAvgPrice = mAvgPrice;
        }

        public String getAmount() {
            return mAmount;
        }

        public void setAmount(String mAmount) {
            this.mAmount = mAmount;
        }

        String getFinished() {
            return mFinished;
        }

        public void setFinished(String mFinished) {
            this.mFinished = mFinished;
        }

        public String getTotal() {
            return mTotal;
        }

        public void setTotal(String mTotal) {
            this.mTotal = mTotal;
        }

        public String getCurrencyType() {
            return mCurrencyType;
        }

        public void setCurrencyType(String mCurrencyType) {
            this.mCurrencyType = mCurrencyType;
        }

        public String getExchangeType() {
            return mExchangeType;
        }

        public void setExchangeType(String mExchangeType) {
            this.mExchangeType = mExchangeType;
        }

        public String getOrderId() {
            return mOrderId;
        }

        public void setOrderId(String mOrderId) {
            this.mOrderId = mOrderId;
        }
    }

    public interface OnExchangeOrderDetailCallback {
        void onCancel();
    }
}
