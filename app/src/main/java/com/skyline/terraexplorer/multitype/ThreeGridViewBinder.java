package com.skyline.terraexplorer.multitype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.utils.RxViewAction;

import me.drakeet.multitype.ItemViewBinder;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/1/16.
 */
public class ThreeGridViewBinder extends ItemViewBinder<ThreeGrid, ThreeGridViewBinder.ViewHolder> {
    public OnThreeGridItemClickListener listener;

    public void setListener(OnThreeGridItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_three_grid, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final ThreeGrid threeGrid) {
        if (threeGrid.getText2().equals("资源名称") || threeGrid.getText2().equals("无")){
            holder.chooseView.setVisibility(View.GONE);
            holder.daohangView.setText("");
        }else {
            holder.chooseView.setVisibility(View.VISIBLE);
            holder.daohangView.setText("到这里");
        }

        holder.text1View.setText(threeGrid.getText1());
        holder.text2View.setText(threeGrid.getText2());
        holder.text3View.setText(threeGrid.getText3());
        if (threeGrid.hasBottomView){
            holder.bottomView.setVisibility(View.VISIBLE);
        }else {
            holder.bottomView.setVisibility(View.GONE);
        }
        if (threeGrid.isChoose){
            holder.chooseView.setImageResource(R.drawable.ic_choose);
        }else {
            holder.chooseView.setImageResource(R.drawable.ic_choose_no);
        }
        RxViewAction.clickNoDouble(holder.daohangView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (!threeGrid.getText2().equals("资源名称") && !threeGrid.getText2().equals("无")){
                            listener.onDaoHangViewClick(threeGrid);
                        }

                    }
                });
        RxViewAction.clickNoDouble(holder.chooseLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (threeGrid.isChoose){
                            listener.onChooseItemClick(threeGrid,false);
                         //   holder.chooseView.setImageResource(R.drawable.ic_choose_no);
                        }else {
                            listener.onChooseItemClick(threeGrid,true);
                           // holder.chooseView.setImageResource(R.drawable.ic_choose);
                        }

                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView text1View;
        private final TextView text2View;
        private final TextView text3View;
        private final View bottomView;
        private final ImageView chooseView;
        private final TextView daohangView;
        private final FrameLayout chooseLayout;

        ViewHolder(View itemView) {
            super(itemView);
            text1View = ((TextView) itemView.findViewById(R.id.text1_view));
            text2View = ((TextView) itemView.findViewById(R.id.text2_view));
            text3View = ((TextView) itemView.findViewById(R.id.text3_view));
            bottomView = ((View) itemView.findViewById(R.id.bottom_view));
            chooseView = ((ImageView) itemView.findViewById(R.id.choose_view));
            daohangView = ((TextView) itemView.findViewById(R.id.daohang_view));
            chooseLayout = ((FrameLayout) itemView.findViewById(R.id.choose_layout));
        }
    }

    public interface OnThreeGridItemClickListener{
        void onDaoHangViewClick(ThreeGrid threeGrid);
        void onChooseItemClick(ThreeGrid threeGrid,boolean isChoose);
    }
}
