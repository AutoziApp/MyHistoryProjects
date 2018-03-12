package com.mapuni.mobileenvironment.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mapuni.mobileenvironment.R;
import java.util.List;

public class ExecuteListAdapter extends RecyclerView.Adapter<ExecuteListAdapter.ViewHolder>  {
    private List<String> data;
    private Context con;
    private View.OnClickListener itemListener;
    public ExecuteListAdapter(Context context, List<String> list, View.OnClickListener listener){
        data = list;
        con = context;
        itemListener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(con).inflate(R.layout.execute_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(data.get(position));
        holder.mView.setOnClickListener(itemListener);
    }

    @Override
    public int getItemCount(){
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView text;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            text = (TextView) view.findViewById(R.id.name);

        }
    }
}
