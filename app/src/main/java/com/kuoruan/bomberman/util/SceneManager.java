package com.kuoruan.bomberman.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;

import com.kuoruan.bomberman.dao.GameDataDao;
import com.kuoruan.bomberman.dao.GameLevelDataDao;
import com.kuoruan.bomberman.entity.Bomb;
import com.kuoruan.bomberman.entity.GameTile;
import com.kuoruan.bomberman.entity.data.GameData;
import com.kuoruan.bomberman.entity.data.GameLevelData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Liao on 2016/5/8 0008.
 */

public class SceneManager {

    private final String TAG = "SceneManager";

    /**
     *
     * 默认炸弹数量
     */
    public final int DEFAULT_BOMB_COUNT = 2;

    private int mTotalBombCount = DEFAULT_BOMB_COUNT;
    private int mPlayerBombCount = 0;

    private int mGameStage = 0;

    private Map<Integer, GameData> mTileData = null;
    private Map<Integer, GameData> mBombData = null;
    private Map<Integer, GameData> mFireData = null;

    private Map<Integer, List<Bitmap>> mBombTemplates = new HashMap<>();
    private Map<Integer, List<Bitmap>> mFireTemplates = new HashMap<>();

    private GameTile[][] mGameTiles = null;
    private Context mContext;

    private GameDataDao mGameDataDao;
    private GameLevelDataDao mGameLevelDataDao;
    private GameLevelData mGameLevelData;

    public SceneManager(Context context, int stage) {
        mContext = context;
        mGameStage = stage;
        mGameDataDao = GameDataDao.getInstance(context);
        this.mTileData = mGameDataDao.getGameTileData();
        this.mBombData = mGameDataDao.getBombData();
        mGameLevelDataDao = GameLevelDataDao.getInstance(context);
    }

    /**
     * 处理地图数据
     *
     */
    public GameTile[][] parseGameTileData() {
        mGameLevelData = mGameLevelDataDao.getGameLevelData(mGameStage);
        String levelTileData = mGameLevelData.getLevelTiles();

        if (levelTileData == null) {
            return null;
        }

        int mTileWidth = 0;
        int mTileHeight = 0;
        int mSceneXMax = 0;
        int mSceneYMax = 0;

        Bitmap bitmap;
        Point tilePoint = new Point();
        int tileX = 0;
        int tileY = 0;

        String[] tileLines = levelTileData.split(GameLevelDataDao.TILE_DATA_LINE_BREAK);
        int rows = tileLines.length;
        int cols = 0;

        for (int i = 0; i < rows; i++) {
            String[] tiles = tileLines[i].split(",");

            //如果没有列数目
            if (cols == 0 && mGameTiles == null) {
                cols = tiles.length;
                mGameTiles = new GameTile[rows][cols];
            }

            for (int j = 0; j < cols; j++) {
                int tileNum = Integer.parseInt(tiles[j]);
                GameData gameTileData = mTileData.get(tileNum);

                if ((mGameTiles.length > 0) && (gameTileData != null)) {
                    tilePoint.x = tileX;
                    tilePoint.y = tileY;

                    bitmap = BitmapManager.setAndGetBitmap(mContext, gameTileData.getDrawable());
                    GameTile gameTile = new GameTile(bitmap, tilePoint, gameTileData.getSubType());
                    gameTile.setVisible(gameTileData.isVisible());

                    if (mTileWidth == 0) {
                        mTileWidth = gameTile.getWidth();
                    }
                    if (mTileHeight == 0) {
                        mTileHeight = gameTile.getHeight();
                    }

                    if (mSceneXMax == 0 && cols > 0) {
                        mSceneXMax = cols * mTileWidth;
                    }
                    if (mSceneYMax == 0 && rows > 0) {
                        mSceneYMax = rows * mTileWidth;
                    }

                    mGameTiles[i][j] = gameTile;
                }

                tileX += mTileWidth;
            }
            tileX = 0;
            tileY += mTileHeight;
        }

        Scene.mSceneXMax = mSceneXMax;
        Scene.mSceneYMax = mSceneYMax;
        Scene.mTileWidth = mTileWidth;
        Scene.mTileHeight = mTileHeight;

        return mGameTiles;
    }

    /**
     * 获取炸弹模版
     *
     * @param bombType
     */
    public List<Bitmap> setAndGetBombTemplates(int bombType) {
        if (!mBombTemplates.containsKey(bombType)) {
            GameData data = mBombData.get(bombType);

            Bitmap baseBitmap = BitmapManager.setAndGetBitmap(mContext, data.getDrawable());
            List<Bitmap> bitmaps = new ArrayList<>();

            int baseWidth = baseBitmap.getWidth();
            int baseHeight = baseBitmap.getHeight();
            int width = baseWidth / 2;

            for (int x = 0; x < baseWidth; x += width) {
                Bitmap bitmap = Bitmap.createBitmap(baseBitmap, x, 0, width, baseHeight);
                bitmaps.add(bitmap);
            }

            mBombTemplates.put(bombType, bitmaps);
        }

        return mBombTemplates.get(bombType);
    }

    /**
     * 炸弹爆炸，创建火焰
     *
     * @param bomb
     */
    public void explosionBomb(Bomb bomb) {

    }

    public void setBomb() {
        Log.i(TAG, "setBomb: 放置炸弹");
    }
}
