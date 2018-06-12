package com.android.bitglobal.entity;

import com.android.bitglobal.view.MarketChartData;

import java.util.ArrayList;
import java.util.List;


public class KDJEntity {
	private ArrayList<Double> Ks;
	private ArrayList<Double> Ds;
	private ArrayList<Double> Js;

	public KDJEntity(List<MarketChartData> OHLCData) {
		Ks = new ArrayList<Double>();
		Ds = new ArrayList<Double>();
		Js = new ArrayList<Double>();

		ArrayList<Double> ks = new ArrayList<Double>();
		ArrayList<Double> ds = new ArrayList<Double>();
		ArrayList<Double> js = new ArrayList<Double>();

		double k = 0.0;
		double d = 0.0;
		double j = 0.0;
		double rSV = 0.0;

		if (OHLCData != null && OHLCData.size() > 0) {

			MarketChartData oHLCEntity = OHLCData.get(OHLCData.size() - 1);
			double high = oHLCEntity.getHighPrice();
			double low = oHLCEntity.getLowPrice();

			for (int i = OHLCData.size() - 1; i >= 0; i--) {
				if (i < OHLCData.size() - 1) {
					oHLCEntity = OHLCData.get(i);
					high = high > oHLCEntity.getHighPrice() ? high : oHLCEntity.getHighPrice();
					low = low < oHLCEntity.getLowPrice() ? low : oHLCEntity.getLowPrice();
				}
				if (high != low) {
					rSV = (oHLCEntity.getClosePrice() - low) / (high - low) * 100;
				}
				if (i == OHLCData.size() - 1) {
					/*k = rSV;
					d = k;*/
					k = 50;
					d = 50;

				} else {
					k = k * 2 / 3 + rSV / 3;
					d = d * 2 / 3 + k / 3;
				}
				j = 3 * k - 2 * d;

				ks.add(k);
				ds.add(d);
				js.add(j);
			}
			for (int i = ks.size() - 1; i >= 0; i--) {
				Ks.add(ks.get(i));
				Ds.add(ds.get(i));
				Js.add(js.get(i));
			}
		}
	}

	public ArrayList<Double> getK() {
		return Ks;
	}

	public ArrayList<Double> getD() {
		return Ds;
	}

	public ArrayList<Double> getJ() {
		return Js;
	}
}
