package sspu.qiu.aichat.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sspu.qiu.aichat.Bean.User;
import sspu.qiu.aichat.Database.UserInfoManager;
import sspu.qiu.aichat.R;

public class ListViewAdapter extends BaseAdapter
        implements AdapterView.OnItemClickListener {
    private Context context;
    private List<User> allUser;
    public ListViewAdapter(Context context, List<User> allUser) {
        this.context = context;
        this.allUser = allUser;
    }
    private onClickListener listener;
    public static interface onClickListener {
        void onClick();
    }
    public void setOnClickListener(onClickListener l) {
        this.listener = l;
    }
    @Override
    public int getCount() {
        return allUser.size();
    }
    @Override
    public Object getItem(int position) {
        return allUser.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = convertView != null ? convertView : View.inflate(context,
                R.layout.listview_item, null);
        TextView tv_username = (TextView) item.findViewById(R.id.tv_userName);
        ImageView iv_deleteUser = (ImageView) item
                .findViewById(R.id.iv_deleteUser);
        tv_username.setText(allUser.get(position).getName());
        iv_deleteUser
                .setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = allUser.get(position).getName();
                        UserInfoManager.getInstance(context).delete(name);
                        allUser.remove(position);
                        notifyDataSetChanged();
                        if (listener != null) {
                            listener.onClick();
                        }
                    }
                });
        return item;
    }
    @Override
    public void onItemClick(AdapterView<?>parent,View view,int position,long id) {
        final User user = (User) parent.getItemAtPosition(position);
        Toast.makeText(context, "账号： " + user.getName() + "\n" + "密码： " +
                user.getPassword(),Toast.LENGTH_SHORT).show();
    }
}