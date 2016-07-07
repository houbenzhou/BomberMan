package com.kuoruan.bomberman.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.kuoruan.bomberman.util.PlayerManager;
import com.kuoruan.bomberman.util.SceneManager;
import com.kuoruan.bomberman.view.GameView;

/**
 * Wifi
 */
public class WifiGameActivity extends BaseActivity {

    private GameView mGameView;
    private PlayerManager mPlayerManager;
    private SceneManager mSceneManager;

    private Context mContext;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    public static void startActivity(Context context, boolean server, String dstIp) {
        Intent intent = new Intent(context, WifiGameActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("isServer", server);
        b.putString("ip", dstIp);
        intent.putExtras(b);
        context.startActivity(intent);
    }

    @Override
    protected void onPause() {
        mGameView.getGameThread().setState(GameView.STATE_PAUSED);
        super.onPause();
    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    protected void onBeforeSetContentView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
