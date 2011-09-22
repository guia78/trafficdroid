package it.localhost.trafficdroid.adapter.item;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.adapter.BadNewsAdapter;
import it.localhost.trafficdroid.common.Const;
import it.localhost.trafficdroid.dto.StreetDTO;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class BadNewsItem extends AbstractChildItem {
	private StreetDTO streetDTO;

	public BadNewsItem(Context context, StreetDTO streetDTO) {
		super(context);
		this.streetDTO = streetDTO;
	}

	public int getType() {
		return Const.itemTypes[1];
	}

	public View inflateView() {
		return inflater.inflate(R.layout.main_item_badnews, null, false);
	}

	public void fillView(View view) {
		((TextView) view.findViewById(R.id.streetDirLeft)).setText(streetDTO.getDirectionLeft());
		((TextView) view.findViewById(R.id.streetDirRight)).setText(streetDTO.getDirectionRight());
		if (streetDTO.getBadNews().size() != 0) {
			((TextView) view.findViewById(R.id.BNTNumber)).setText(Const.badNews + streetDTO.getBadNews().size());
			((TextView) view.findViewById(R.id.BNTNumber)).setVisibility(View.VISIBLE);
		} else
			((TextView) view.findViewById(R.id.BNTNumber)).setVisibility(View.INVISIBLE);
	}

	public void onClick() {
		if (streetDTO.getBadNews().size() != 0) {
			Dialog dialog = new Dialog(context);
			dialog.setTitle(streetDTO.getName());
			ListView listview = (ListView) LayoutInflater.from(context).inflate(R.layout.dialog_badnews, null);
			listview.setAdapter(new BadNewsAdapter(context, R.layout.dialog_item_badnews, streetDTO.getBadNews()));
			dialog.setContentView(listview);
			dialog.show();
		}
	}
}