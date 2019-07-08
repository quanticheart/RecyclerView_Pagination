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
 *  * Copyright(c) Developed by John Alves at 2019/7/8 at 2:48:9 for quantic heart studios
 *
 */

package com.quanticheart.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.widget.RadioGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Main extends AppCompatActivity {

    Adapter adapter;
    int n = 1;
    int testLoad = 0;
    ArrayList<DataModel> list;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        RadioGroup radioGroup = findViewById(R.id.group);

        verticalMode();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.vertical:
                        verticalMode();
                        return;
                    case R.id.horizontal:
                        horizontalMode();
                        return;
                    case R.id.grid:
                        gridMode();
                }
            }
        });

    }


    //==============================================================================================
    //
    // ** Create list
    //
    //==============================================================================================

    private void verticalMode() {
        adapter = new Adapter(this, recyclerView, false);
        addDefaultParans();
    }

    private void horizontalMode() {
        adapter = new Adapter(this, recyclerView, true);
        addDefaultParans();
    }

    private void gridMode() {
        adapter = new Adapter(this, recyclerView);
        addDefaultParans();
    }

    private void addDefaultParans() {
        cleanList();
        list = createFirstList();
        adapter.addItems(list);

        recyclerView.setAdapter(adapter);

        adapter.setOnTryLoadListener(new Adapter.OnTryLoadListener() {
            @Override
            public void tryLoad(int pageNumber) {
                adapter.addLoadView();
                getListAfter();
            }
        });

        adapter.addPaginationScrollListener(new Adapter.ScrollPaginationListener() {
            @Override
            public void scrollCallback(int pageNumber) {
                getList();
            }
        });
    }


    //==============================================================================================
    //
    // ** TESTs
    //
    //==============================================================================================

    private void getList() {
        ArrayList<DataModel> list = new ArrayList<>();
        if (testLoad < 2) {
            for (int i = 1; i < 11; i++) {
                list.add(new DataModel("item " + n++));
            }
            testLoad++;
        }
        adapter.addItems(list);
    }

    private void getListAfter() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getList();
            }
        }, 2000);
    }

    private ArrayList<DataModel> createFirstList() {
        ArrayList<DataModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new DataModel("item " + n++));
        }
        return list;
    }

    private void cleanList() {
        if (list != null)
            list.clear();

        n = 1;
        testLoad = 0;
    }
}
