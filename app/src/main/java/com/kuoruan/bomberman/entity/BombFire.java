package com.kuoruan.bomberman.entity;

import android.graphics.Bitmap;
import android.graphics.Point;

import java.util.List;

/**
 * 炸弹火焰（待实现）
 *
 */
public class BombFire extends GameTile {

    public static final int TYPE_UP = 1;
    public static final int TYPE_DOWN = 2;
    public static final int TYPE_LEFT = 3;
    public static final int TYPE_RIGHT = 4;
    public static final int TYPE_VERTICAL = 5;
    public static final int TYPE_HORIZONTAL = 6;
    public static final int TYPE_CENTER = 7;

    public BombFire(List<Bitmap> frameBitmap, Point point) {
        super(frameBitmap, point, GameTile.TYPE_BOMB_FIRE);
    }
}
