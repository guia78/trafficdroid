package it.localhost.trafficdroid.dao;

import it.localhost.trafficdroid.dto.BaseDTO;
import it.localhost.trafficdroid.dto.PedaggioDTO;

import java.net.URL;
import java.util.Scanner;

import android.os.AsyncTask;

public class PedaggioService extends AsyncTask<String, Void, BaseDTO> {
	private static final String url = "http://autostrade.it/autostrade/ricercaPercorso.do?tipo=G&dtxpDa=";
	private static final String arg = "&dtxpA=";
	private static final String spanAperto = "<span class=\"km\">";
	private static final String and = "&";
	private static final String delimiter = "\\A";
	private static final String badParams = "Parametri non validi";

	@Override
	protected BaseDTO doInBackground(String... args) {
		try {
			BaseDTO out;
			Scanner sc = new Scanner(new URL(url + args[0] + arg + args[1]).openStream());
			String s = sc.useDelimiter(delimiter).next();
			int start = s.indexOf(spanAperto);
			if (start != -1) {
				start = start + spanAperto.length();
				out = new PedaggioDTO(true, s.substring(start, s.indexOf(and, start)).trim());
			} else
				out = new BaseDTO(false, badParams);
			sc.close();
			return out;
		} catch (Exception e) {
			return new BaseDTO(false, e.getMessage());
		}
	}
}
