/*
 *
 *  *                                     /@
 *  *                      __        __   /\/
 *  *                     /==\      /  \_/\/
 *  *                   /======\    \/\__ \__
 *  *                 /==/\  /\==\    /\_|__ \
 *  *              /==/    ||    \=\ / / / /_/
 *  *            /=/    /\ || /\   \=\/ /
 *  *         /===/   /   \||/   \   \===\
 *  *       /===/   /_________________ \===\
 *  *    /====/   / |                /  \====\
 *  *  /====/   /   |  _________    /      \===\
 *  *  /==/   /     | /   /  \ / / /         /===/
 *  * |===| /       |/   /____/ / /         /===/
 *  *  \==\             /\   / / /          /===/
 *  *  \===\__    \    /  \ / / /   /      /===/   ____                    __  _         __  __                __
 *  *    \==\ \    \\ /____/   /_\ //     /===/   / __ \__  ______  ____ _/ /_(_)____   / / / /__  ____ ______/ /_
 *  *    \===\ \   \\\\\\\/   ///////     /===/  / / / / / / / __ \/ __ `/ __/ / ___/  / /_/ / _ \/ __ `/ ___/ __/
 *  *      \==\/     \\\\/ / //////       /==/  / /_/ / /_/ / / / / /_/ / /_/ / /__   / __  /  __/ /_/ / /  / /_
 *  *      \==\     _ \\/ / /////        |==/   \___\_\__,_/_/ /_/\__,_/\__/_/\___/  /_/ /_/\___/\__,_/_/   \__/
 *  *        \==\  / \ / / ///          /===/
 *  *        \==\ /   / / /________/    /==/
 *  *          \==\  /               | /==/
 *  *          \=\  /________________|/=/
 *  *            \==\     _____     /==/
 *  *           / \===\   \   /   /===/
 *  *          / / /\===\  \_/  /===/
 *  *         / / /   \====\ /====/
 *  *        / / /      \===|===/
 *  *        |/_/         \===/
 *  *                       =
 *  *
 *  * Copyright(c) Developed by John Alves at 2019/7/8 at 1:58:29 for quantic heart studios
 *
 */

package com.quanticheart.myapplication;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("all")
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //==============================================================================================
    //
    // ** Vars
    //
    //==============================================================================================

    //Pagination
    private int pageNumber = 1;

    //Create adapter
    private Activity activity; // Context
    private List<DataModel> database = new ArrayList<>(); // List Dados referenciado
    private LayoutInflater inflater;

    //Views Type
    private final int viewDefault = 0;

    //==============================================================================================
    //
    // ** Constructor
    //
    //==============================================================================================

    public Adapter(Activity activity, RecyclerView recyclerView, boolean horizontalScroll) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        recyclerView.setLayoutManager(RecyclerViewUtil.ListLayout(activity, horizontalScroll));
        recyclerView.addOnScrollListener(scrollListener);
    }

    public Adapter(Activity activity, RecyclerView recyclerView) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        recyclerView.setLayoutManager(RecyclerViewUtil.GridLayout(activity));
        recyclerView.addOnScrollListener(scrollListener);
    }

    //==============================================================================================
    //
    // ** onCreateViewHolder
    //
    //==============================================================================================

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == viewDefault) {
            return new DataViewHolder(getView(R.layout.cell_list, parent));
        } else if (viewType == idEndView) {
            return new EndViewHolder(getView(R.layout.cell_end_list, parent));
        } else {
            return new LoadViewHolder(getView(R.layout.cell_load_list, parent));
        }
    }

    //==============================================================================================
    //
    // ** Adapter Config Utils
    //
    //==============================================================================================

    @Override
    public int getItemCount() {
        return database.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (database.get(position).getViewType() == viewDefault) {
            return viewDefault;
        } else if (database.get(position).getViewType() == idEndView) {
            return idEndView;
        } else {
            return idLoadView;
        }
    }

    //==============================================================================================
    //
    // ** onBindViewHolder
    //
    //==============================================================================================

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof DataViewHolder) {
            DataViewHolder viewHolder = (DataViewHolder) holder;
            viewHolder.bindViewData(database.get(position), position);
        }

        if (holder instanceof EndViewHolder) {
            EndViewHolder viewHolder = (EndViewHolder) holder;
        }

        if (holder instanceof LoadViewHolder) {
            LoadViewHolder viewHolder = (LoadViewHolder) holder;
        }

    }

    //==============================================================================================
    //
    // ** GetView
    //
    //==============================================================================================

    private View getView(int cellForInflate, ViewGroup parent) {
        return inflater.inflate(cellForInflate, parent, false);
    }

    //==============================================================================================
    //
    // ** Utils for Add and Remove data on Adapter
    //
    //==============================================================================================

    public void add(DataModel data) {
        database.add(data);
        notifyItemInserted(database.size() - 1);
    }

    public void remove(int position) {
        database.remove(position);
        notifyItemRemoved(position);
    }

    public void addItems(List<DataModel> dataModelList) {

        if (dataModelList != null)
            if (dataModelList.size() == 0) {
                addEndView();
            } else {
                removeLoadView();
                database.addAll(dataModelList);
                addLoadView();
                scrollContinue();
                notifyDataSetChanged();
                nextPagination();
            }
    }

    public void removeAll() {
        database.clear();
        notifyDataSetChanged();
    }

    //==============================================================================================
    //
    // ** Layout Cell Utils
    //
    //==============================================================================================

    // ** add EndView
    //==============================================================================================
    private int idEndView = 1;

    private void addEndView() {
        removeLoadView();
        database.add(new DataModel(idEndView));
        notifyItemInserted(database.size() - 1);
    }

    private void removeEndView() {
        removeLoadView();
        addLoadView();
    }

    // ** add LoadView
    //==============================================================================================
    private int idLoadView = 2;

    public void addLoadView() {
        if (database.get(database.size() - 1).getViewType() == idEndView) {
            remove(database.size() - 1);
        }

        database.add(new DataModel(idLoadView));
        notifyItemInserted(database.size() - 1);
    }

    private void removeLoadView() {
        if (database.size() > 0)
            database.remove(database.size() - 1);
        notifyItemRemoved(database.size());
    }

    //==============================================================================================
    //
    // ** Data Holder
    //
    //==============================================================================================

    private class DataViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        // view
        private TextView name_item;

        private DataViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            name_item = itemView.findViewById(R.id.text);
        }

        void bindViewData(final DataModel dataModel, final int position) {

            name_item.setText(dataModel.getText().trim());

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return true;
                }
            });

        }
    }


    //==============================================================================================
    //
    // ** End Holder
    //
    //==============================================================================================

    private class EndViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        // view
        private Button btnTry;

        private EndViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            btnTry = itemView.findViewById(R.id.btn);
            btnTry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tryLoad(pageNumber);
                }
            });
        }
    }

    //==============================================================================================
    //
    // ** Load Holder
    //
    //==============================================================================================

    private class LoadViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        private LoadViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
    }

    //==============================================================================================
    //
    // ** Scroll Listener
    //
    //==============================================================================================

    //load
    private boolean loading;
    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                final GridLayoutManager layout = (GridLayoutManager) recyclerView.getLayoutManager();
                assert layout != null;
                if (!loading && layout.getItemCount() == (layout.findLastVisibleItemPosition() + 1)) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callScrollListener(pageNumber);
                        }
                    }, 1500);
                    scrollStop();
                }
            } else {
                final LinearLayoutManager layout = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert layout != null;
                if (!loading && layout.getItemCount() == (layout.findLastVisibleItemPosition() + 1)) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callScrollListener(pageNumber);
                        }
                    }, 1500);
                    scrollStop();
                }
            }

        }
    };

    private void scrollStop() {
        loading = true;
    }

    private void scrollContinue() {
        loading = false;
    }

    // ** Interface Scroll
    //==============================================================================================

    private void callScrollListener(int pageNumber) {
        if (scrollListenerCallcabk != null) {
            scrollListenerCallcabk.scrollCallback(pageNumber);
        }
    }

    public void addPaginationScrollListener(ScrollPaginationListener scrollListenerCallcabk) {
        this.scrollListenerCallcabk = scrollListenerCallcabk;
    }

    private ScrollPaginationListener scrollListenerCallcabk;

    public interface ScrollPaginationListener {
        void scrollCallback(int pageNumber);
    }

    // ** Interface Try Load
    //==============================================================================================

    private void tryLoad(int pageNumber) {
        if (onTryLoadListener != null)
            onTryLoadListener.tryLoad(pageNumber);
    }

    private OnTryLoadListener onTryLoadListener;

    public void setOnTryLoadListener(OnTryLoadListener onTryLoadListener) {
        this.onTryLoadListener = onTryLoadListener;
    }

    public interface OnTryLoadListener {
        void tryLoad(int pageNumber);
    }

    //==============================================================================================
    //
    // ** Paginations Utils
    //
    //==============================================================================================

    private void nextPagination() {
        pageNumber++;
    }

    private void backPagination() {
        pageNumber--;
    }

    public int getFirstPagination() {
        return pageNumber = 1;
    }

    public void setPaginationNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPaginationNumber() {
        return pageNumber;
    }

}
