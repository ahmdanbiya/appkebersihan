package com.ccdp.appkebersihan5.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.Target;
import com.ccdp.appkebersihan5.Constants;
import com.ccdp.appkebersihan5.R;
import com.ccdp.appkebersihan5.model.Kebersihan;

import java.util.ArrayList;

/**
 * Created by Susi on 3/14/2017.
 */

public class KebersihanAdapter extends ArrayAdapter<Kebersihan> {

    private ArrayList<Kebersihan> dataset;
    Context context;

    public KebersihanAdapter(Context context, int resource, ArrayList<Kebersihan> objects) {
        super(context, resource, objects);
        this.context = context;
        this.dataset = objects;
    }
    //ViewHolder

    private static class ViewHolder {
        TextView txtId;
        TextView txtKeterangan;
        TextView txtUserid;
        TextView txtTgllapor;
        TextView txtLokasi;
        ImageView photo;
    }

    //getView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Kebersihan kebersihan = getItem(position);

        viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.row_item, parent, false);
        viewHolder.txtId = (TextView) convertView.findViewById(R.id.id);
        viewHolder.txtUserid = (TextView) convertView.findViewById(R.id.userid);
        viewHolder.txtKeterangan = (TextView) convertView.findViewById(R.id.keterangan);
        viewHolder.txtTgllapor = (TextView) convertView.findViewById(R.id.tgllapor);
        viewHolder.txtLokasi = (TextView) convertView.findViewById(R.id.lokasi);
        viewHolder.photo = (ImageView) convertView.findViewById(R.id.photo);

        try {
            String kebersihanId = String.valueOf(kebersihan.getId());
            viewHolder.txtId.setText(kebersihanId);
            viewHolder.txtKeterangan.setText(kebersihan.getKeterangan());
            viewHolder.txtUserid.setText(kebersihan.getUserid());
            viewHolder.txtTgllapor.setText(kebersihan.getTgllapor());
            viewHolder.txtLokasi.setText(kebersihan.getLokasi());
            String photoUrl = Constants.BASE_ASSETS+kebersihan.getId()+".jpg";
            //https://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en
            Target<GlideDrawable> into = Glide.with(context)
                    .load(photoUrl)
                    .error(R.mipmap.ic_launcher)
                    .override(150, 150)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(viewHolder.photo);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return convertView;
    }


}
