package com.kuoruan.bomberman.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;

import com.kuoruan.bomberman.dao.GameDataDao;
import com.kuoruan.bomberman.entity.Player;
import com.kuoruan.bomberman.entity.data.GameData;
import com.kuoruan.bomberman.net.ConnectedService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Window10 on 2016/5/3.
 */

public class PlayerManager {

    private int mId = 1;
    private ConnectedService mConnectedService;
    private GameDataDao mGameDataDao;

    private Map<Integer, Player> mPlayerMap = new HashMap<>();
    private Map<Integer, GameData> mPlayerData = null;
    private Map<Integer, Map<Integer, List<Bitmap>>> mPlayerTemplates = new HashMap<>();
    private static final Point[] positions = {new Point(1, 1), new Point(15, 11), new Point(1, 11), new Point(15, 1)};

    private Context mContext;

    public PlayerManager() {

    }

    public PlayerManager(Context context, int id) {
        mContext = context;
        mId = id;
        mGameDataDao = GameDataDao.getInstance(context);
        mPlayerData = mGameDataDao.getPlayerData();
        prepareMyPlayer();
    }

    /**
     * 初始化玩家
     *
     * @return
     */
    public void prepareMyPlayer() {
        Point mapPoint = positions[mId];
        addPlayer(mId, mapPoint);
    }

    /**
     * 添加一个新的玩家
     */
    public void addPlayer(int id, Point mapPoint) {
        Bitmap bitmap = getFirstBitmap(id);
        mapPoint.x *= bitmap.getWidth();
        mapPoint.y = bitmap.getHeight();
        Player player = new Player(bitmap, mPlayerTemplates.get(id), mapPoint);
        player.setId(id);
        mPlayerMap.put(id, player);
    }

    private Bitmap getFirstBitmap(int id) {
        Map<Integer, List<Bitmap>> playerTemplate = setAndGetPlayerTemplates(id);
        return playerTemplate.get(Player.DIRECTION_DOWN).get(0);
    }

    /**
     * 准备玩家图片模板
     *
     * @param id
     */
    private Map<Integer, List<Bitmap>> setAndGetPlayerTemplates(int id) {
        if (!mPlayerTemplates.containsKey(id)) {
            GameData data = mPlayerData.get(id);

            Bitmap baseBitmap = BitmapManager.setAndGetBitmap(mContext, data.getDrawable());

            int width = baseBitmap.getWidth() / 3;
            int height = baseBitmap.getHeight() / 7;

            Map<Integer, List<Bitmap>> map = new HashMap<>();
            for (int y = 0; y < 4; y++) {
                List<Bitmap> list = new ArrayList<>();
                for (int x = 0; x < 3; x++) {
                    Bitmap newBitmap = Bitmap.createBitmap(baseBitmap, x * width, y * height, width, height);
                    list.add(newBitmap);
                }

                map.put(y + 1, list);
            }

            List<Bitmap> dieBitmaps = new ArrayList<>();
            for (int y = 4; y < 7; y++) {
                for (int x = 0; x < 3; x++) {
                    Bitmap newBitmap = Bitmap.createBitmap(baseBitmap, x * width, y * height, width, height);
                    dieBitmaps.add(newBitmap);
                }
            }

            map.put(Player.PLAYER_DIE, dieBitmaps);
            mPlayerTemplates.put(id, map);
        }

        return mPlayerTemplates.get(id);
    }

    public Map<Integer, Player> getPlayerMap() {
        return mPlayerMap;
    }

    public int getPlayerCount() {
        return mPlayerMap.size();
    }

    public void handlePlayerMove(int playerId, int newX, int newY) {
        if (playerId == mId) {
            return;
        }

        Player netPlayer = mPlayerMap.get(playerId);
        netPlayer.setX(newX);
        netPlayer.setY(newY);
    }

    public Player getMyPlayer() {
        return mPlayerMap.get(mId);
    }

    public void handlePlayerAdd(int playerId, Point point) {
        if (mPlayerMap.containsKey(playerId)) {
            return;
        }
        addPlayer(playerId, point);
        //通知新加入用户我已存在
        //UdpClient.noticeAddPlayer(getMyPlayer());
    }

    //设置我的ID
    public void setId(int id) {
        mId = id;
    }

    public void noticeMyMove() {

    }
}
