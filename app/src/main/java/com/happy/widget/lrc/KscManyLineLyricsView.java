package com.happy.widget.lrc;

import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.happy.common.Constants;
import com.happy.model.app.KscLyricsLineInfo;
import com.happy.model.app.MessageIntent;
import com.happy.model.app.SongMessage;
import com.happy.observable.ObserverManage;
import com.happy.ui.R;
import com.happy.util.KscLyricsParserUtil;


public class KscManyLineLyricsView extends View implements Observer {

    private boolean hasKsc = false;

    private boolean mIsDrawTimeLine = false;


    private Paint paint;

    private Paint paintHLDEF;

    private Paint paintHLED;


    // private Paint paintBackgruond;


    private Paint mPaintForTimeLine;

    private int SCALEIZEWORDDEF = 15;

    private int SIZEWORD = SCALEIZEWORDDEF;

    private int SIZEWORDHL = 0;


    private int INTERVAL = 30;

    private Context context;

    private KscLyricsParserUtil kscLyricsParser;


    private TreeMap<Integer, KscLyricsLineInfo> lyricsLineTreeMap;

    private int lyricsLineNum = -1;

    private int oldLyricsLineNum = -1;

    private int lyricsWordIndex = -1;


    private int lyricsWordHLEDTime = 0;


    private float lineLyricsHLWidth = 0;


    private float highLightLrcMoveX;


    private float mCurFraction = 1.0f;


    private float offsetY = 0;

    private float oldOffsetY = 0;


    private int scrollMaxYProgress;

    private boolean blScroll = false;

    private int progress = 0;


    private int fontSizeScale = 0;

    private int oldFontSizeScale = 0;

    public KscManyLineLyricsView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public KscManyLineLyricsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KscManyLineLyricsView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {

        this.context = context;

        Typeface typeFace = Typeface.createFromAsset(context.getAssets(),
                "fonts/weiruanyahei14M.ttf");

        paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setTypeface(typeFace);

        paintHLDEF = new Paint();
        paintHLDEF.setDither(true);
        paintHLDEF.setAntiAlias(true);
        paintHLDEF.setColor(Color.WHITE);
        paintHLDEF.setTypeface(typeFace);

        paintHLED = new Paint();
        paintHLED.setDither(true);
        paintHLED.setAntiAlias(true);
        paintHLED.setTypeface(typeFace);

        // paintBackgruond = new Paint();
        // paintBackgruond.setColor(Color.BLACK);
        // paintBackgruond.setTypeface(typeFace);
        // paintBackgruond.setDither(true);
        // paintBackgruond.setAntiAlias(true);

        mPaintForTimeLine = new Paint();
        mPaintForTimeLine.setDither(true);
        mPaintForTimeLine.setAntiAlias(true);
        mPaintForTimeLine.setTypeface(typeFace);

        initColor();
        initSizeWord();

        ObserverManage.getObserver().addObserver(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (!hasKsc) {

            String tip = context.getString(R.string.lrc_tip);
            drawDefText(canvas, tip);
        } else {
            drawLrcText(canvas);
        }


        if (mIsDrawTimeLine) {

            mPaintForTimeLine.setTextSize(SIZEWORDHL);

            String timeStr = kscLyricsParser.timeParserString(progress);
            FontMetrics fm = mPaintForTimeLine.getFontMetrics();
            int height = (int) Math.ceil(fm.descent - fm.top) + 2;
            float y = getHeight() / 2 + getScrollY();

            // paintBackgruond.setTextSize(SIZEWORDHL);
            // drawBackground(canvas, timeStr, 0, y + height);

            canvas.drawText(timeStr, 0, y + height, mPaintForTimeLine);
            canvas.drawLine(0, y, getWidth(), y, mPaintForTimeLine);
        }

        super.onDraw(canvas);
    }


    private void initSizeWord() {

        fontSizeScale = Constants.lrcFontSize + 100;
        SCALEIZEWORDDEF = (int) ((float) fontSizeScale / 100 * SIZEWORD);
        SIZEWORDHL = SCALEIZEWORDDEF + 5;
    }


    private void initColor() {
        paintHLED
                .setColor(parserColor(Constants.lrcColorStr[Constants.lrcColorIndex]));
        mPaintForTimeLine
                .setColor(parserColor(Constants.lrcColorStr[Constants.lrcColorIndex]));

    }


    private void drawDefText(Canvas canvas, String tip) {

        paintHLDEF.setTextSize(SIZEWORDHL);
        paintHLED.setTextSize(SIZEWORDHL);

        FontMetrics fm = paintHLDEF.getFontMetrics();
        float textWidth = paintHLDEF.measureText(tip);
        int textHeight = (int) Math.ceil(fm.descent - fm.top) + 2;
        //
        // LinearGradient linearGradient = new LinearGradient(0, 0, 0,
        // height,
        // new int[] { 0x33ffffff, 0xffffffff, 0x33ffffff }, new float[] {
        // 0, 0.5f, 1 }, Shader.TileMode.MIRROR);

        // LinearGradient linearGradient = new LinearGradient(0, 0, 0,
        // height,
        // new int[] { parserColor("#fe0000"), parserColor("#ff8041"),
        // parserColor("#ffff00") }, null, Shader.TileMode.MIRROR);

        // paint.setShader(linearGradient);

        // paint.setColor(Color.WHITE);

        //
        // LinearGradient linearGradient = new LinearGradient(0, 0, 0,
        // textHeight,
        // new int[] { parserColor("#00348a"), parserColor("#0080c0"),
        // parserColor("#03cafc") }, null, Shader.TileMode.MIRROR);
        // paintHLDEF.setShader(linearGradient);

        // drawBackground(canvas, tip, (getWidth() - textWidth) / 2,
        // (getHeight() + height) / 2);


        canvas.save();

        float leftX = (getWidth() - textWidth) / 2;
        float heightY = (getHeight() + textHeight) / 2;

        canvas.drawText(tip, leftX, heightY, paintHLDEF);


        // LinearGradient linearGradiented = new LinearGradient(0, 0, 0,
        // textHeight, new int[] { parserColor("#82f7fd"),
        // parserColor("#ffffff"), parserColor("#03e9fc") }, null,
        // Shader.TileMode.MIRROR);
        // paintHLED.setShader(linearGradiented);


        canvas.clipRect(leftX, heightY - textHeight, leftX + textWidth / 2,
                heightY + textHeight);

        // drawBackground(canvas, tip, (getWidth() - textWidth) / 2,
        // (getHeight() + height) / 2);

        canvas.drawText(tip, leftX, heightY, paintHLED);
        canvas.restore();
    }

    // /**
    // *
    // *
    // * @param canvas
    // * @param string
    // * @param x
    // * @param y
    // */
    // private void drawBackground(Canvas canvas, String string, float x, float
    // y) {
    // canvas.drawText(string, x - 1, y, paintBackgruond);
    // canvas.drawText(string, x + 1, y, paintBackgruond);
    // canvas.drawText(string, x, y + 1, paintBackgruond);
    // canvas.drawText(string, x, y - 1, paintBackgruond);
    // }


    private void drawLrcText(Canvas canvas) {
        int alphaValue = 5;

        for (int i = lyricsLineNum - 1; i >= 0; i--) {
            if (offsetY + (SCALEIZEWORDDEF + INTERVAL) * i < (SCALEIZEWORDDEF)) {
                break;
            }

            if (i == oldLyricsLineNum) {

                float textSize = SIZEWORDHL - (SIZEWORDHL - SCALEIZEWORDDEF)
                        * mCurFraction;
                paint.setTextSize(textSize);
            } else {
                paint.setTextSize(SCALEIZEWORDDEF);
            }

            String text = lyricsLineTreeMap.get(i).getLineLyrics();
            float textWidth = paint.measureText(text);
            float textX = (getWidth() - textWidth) / 2;

            textX = Math.max(textX, 10);

            paint.setColor(Color.argb(255 - alphaValue, 255, 255, 255));

            canvas.drawText(text, textX, offsetY + (SCALEIZEWORDDEF + INTERVAL)
                    * i, paint);

            alphaValue += 10;

        }

        alphaValue = 5;

        for (int i = lyricsLineNum + 1; i < lyricsLineTreeMap.size(); i++) {
            if (offsetY + (SCALEIZEWORDDEF + INTERVAL) * i > getHeight()
                    - (SCALEIZEWORDDEF)) {
                break;
            }

            if (i == oldLyricsLineNum) {

                float textSize = SIZEWORDHL - (SIZEWORDHL - SCALEIZEWORDDEF)
                        * mCurFraction;
                paint.setTextSize(textSize);
            } else {
                paint.setTextSize(SCALEIZEWORDDEF);
            }

            String text = lyricsLineTreeMap.get(i).getLineLyrics();
            float textWidth = paint.measureText(text);
            float textX = (getWidth() - textWidth) / 2;

            textX = Math.max(textX, 10);

            paint.setColor(Color.argb(255 - alphaValue, 255, 255, 255));
            canvas.drawText(text, textX, offsetY + (SCALEIZEWORDDEF + INTERVAL)
                    * i, paint);

            alphaValue += 10;
        }

        if (lyricsLineNum != -1) {

            // float textX = (getWidth() - lineLyricsWidth) / 2;

            // canvas.drawText(lineLyrics, textX, offsetY + (SIZEWORD +
            // INTERVAL)
            // * lyricsLineNum, paintHLED);

            float textSize = SCALEIZEWORDDEF + (SIZEWORDHL - SCALEIZEWORDDEF)
                    * mCurFraction;
            paintHLDEF.setTextSize(textSize);
            paintHLED.setTextSize(textSize);

            KscLyricsLineInfo kscLyricsLineInfo = lyricsLineTreeMap
                    .get(lyricsLineNum);

            String lineLyrics = kscLyricsLineInfo.getLineLyrics();
            float lineLyricsWidth = paintHLED.measureText(lineLyrics);


            if (lyricsWordIndex == -1) {
                lineLyricsHLWidth = lineLyricsWidth;
            } else {
                String lyricsWords[] = kscLyricsLineInfo.getLyricsWords();
                int wordsDisInterval[] = kscLyricsLineInfo
                        .getWordsDisInterval();

                String lyricsBeforeWord = "";
                for (int i = 0; i < lyricsWordIndex; i++) {
                    lyricsBeforeWord += lyricsWords[i];
                }

                String lyricsNowWord = lyricsWords[lyricsWordIndex].trim();


                float lyricsBeforeWordWidth = paintHLED
                        .measureText(lyricsBeforeWord);


                float lyricsNowWordWidth = paintHLED.measureText(lyricsNowWord);

                float len = lyricsNowWordWidth
                        / wordsDisInterval[lyricsWordIndex]
                        * lyricsWordHLEDTime;
                lineLyricsHLWidth = lyricsBeforeWordWidth + len;
            }

            float textX = 0;
            if (lineLyricsWidth > getWidth()) {
                if (lineLyricsHLWidth >= getWidth() / 2) {
                    if ((lineLyricsWidth - lineLyricsHLWidth) >= getWidth() / 2) {
                        highLightLrcMoveX = (getWidth() / 2 - lineLyricsHLWidth);
                    } else {
                        highLightLrcMoveX = getWidth() - lineLyricsWidth - 10;
                    }
                } else {
                    highLightLrcMoveX = 10;
                }

                textX = highLightLrcMoveX;
            } else {

                textX = (getWidth() - lineLyricsWidth) / 2;
            }


            canvas.save();


            canvas.drawText(lineLyrics, textX, offsetY
                    + (SCALEIZEWORDDEF + INTERVAL) * lyricsLineNum, paintHLDEF);


            FontMetrics fm = paintHLDEF.getFontMetrics();
            int height = (int) Math.ceil(fm.descent - fm.top) + 2;
            canvas.clipRect(textX, offsetY + (SCALEIZEWORDDEF + INTERVAL)
                            * lyricsLineNum - height, textX + lineLyricsHLWidth,
                    offsetY + (SCALEIZEWORDDEF + INTERVAL) * lyricsLineNum
                            + height);


            canvas.drawText(lineLyrics, textX, offsetY
                    + (SCALEIZEWORDDEF + INTERVAL) * lyricsLineNum, paintHLED);
            canvas.restore();
        }
    }

    private float startRawX = 0, startRawY = 0;

    private float touchY = 0;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!hasKsc) {
            return super.onTouchEvent(event);
        }
        float tt = event.getY();
        float rawX = event.getRawX();
        int sumX = (int) (rawX - startRawX);
        int sumY = (int) (event.getRawY() - startRawY);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startRawX = event.getRawX();
                startRawY = event.getRawY();

                // if (hideTime < 0) {
                // hideTime = 200;
                // mHandler.post(upDateTime);
                // } else {
                // hideTime = 200;
                // }

                break;
            case MotionEvent.ACTION_MOVE:

                if (sumX < -10 || sumX > 10 || sumY < -10 || sumY > 10) {

                    blScroll = true;
                    mIsDrawTimeLine = true;

                    touchY = tt - touchY;
                    progress = (int) (progress - touchY * 100);
                    if (progress < 0) {
                        progress = 0;
                    }
                    if (progress > scrollMaxYProgress) {
                        progress = scrollMaxYProgress;
                    }

                    showLrc(progress);
                    invalidate();
                }

                // if (hideTime < 0) {
                // hideTime = 200;
                // mHandler.post(upDateTime);
                // } else {
                // hideTime = 200;
                // }

                break;
            case MotionEvent.ACTION_UP:
                blScroll = false;
                mIsDrawTimeLine = false;
                invalidate();
                if (sumX > -10 && sumX < 10 && sumY > -10 && sumY < 10) {
                    if (onLrcClickListener != null) {
                        onLrcClickListener.onClick();
                    }
                } else {

                    SongMessage songMessage = new SongMessage();
                    songMessage.setType(SongMessage.SEEKTOMUSIC);
                    songMessage.setProgress(progress);
                    ObserverManage.getObserver().setMessage(songMessage);
                }

                startRawX = 0;
                startRawY = 0;

                break;
        }
        touchY = tt;
        return false;
    }

    // private int hideTime = -1;

    // private Handler mHandler = new Handler() {
    //
    // };

    // Runnable upDateTime = new Runnable() {
    //
    // @Override
    // public void run() {
    // if (hideTime >= 0) {
    // hideTime -= 50;
    // mHandler.postDelayed(upDateTime, 10);
    // } else {
    // blScroll = false;
    // mIsDrawTimeLine = false;
    // invalidate();
    // }
    //
    // }
    // };


    public void init(int scrollMaxYProgress, KscLyricsParserUtil kscLyricsParser) {

        this.scrollMaxYProgress = scrollMaxYProgress;
        this.kscLyricsParser = kscLyricsParser;
        lyricsLineTreeMap = kscLyricsParser.getLyricsLineTreeMap();
        offsetY = getHeight() / 2 + (SCALEIZEWORDDEF + INTERVAL);
        oldLyricsLineNum = -1;
        lyricsLineNum = -1;
        lyricsWordIndex = -1;
        lineLyricsHLWidth = 0;
        lyricsWordHLEDTime = 0;
        highLightLrcMoveX = 0;
        mCurFraction = 1.0f;

        invalidate();
    }


    public void showLrc(int playProgress) {

        if (!blScroll) {
            this.progress = playProgress;
        }
        if (kscLyricsParser == null)
            return;
        int newLyricsLineNum = kscLyricsParser
                .getLineNumberFromCurPlayingTime(playProgress);
        if (newLyricsLineNum == -1) {
            offsetY = getHeight() / 2 + (SCALEIZEWORDDEF + INTERVAL);

            lyricsLineNum = -1;
            lyricsWordIndex = -1;
            lineLyricsHLWidth = 0;
            lyricsWordHLEDTime = 0;
            highLightLrcMoveX = 0;
            mCurFraction = 1.0f;
        } else {

            int sy = (SCALEIZEWORDDEF + INTERVAL);
            if (lyricsLineNum != newLyricsLineNum
                    || oldFontSizeScale != fontSizeScale) {
                lyricsLineNum = newLyricsLineNum;
                oldOffsetY = getHeight() / 2 - (SCALEIZEWORDDEF + INTERVAL)
                        * lyricsLineNum + sy;
            }

            float dy = kscLyricsParser.getOffsetDYFromCurPlayingTime(
                    lyricsLineNum, playProgress, sy);
            offsetY = oldOffsetY - dy;

            highLightLrcMoveX = 0;

            if (newLyricsLineNum > lyricsLineNum) {
                oldLyricsLineNum = newLyricsLineNum + 1;
            } else {
                oldLyricsLineNum = newLyricsLineNum - 1;
            }
            mCurFraction = dy / sy;
        }

        lyricsWordIndex = kscLyricsParser.getDisWordsIndexFromCurPlayingTime(
                lyricsLineNum, playProgress);

        lyricsWordHLEDTime = kscLyricsParser.getLenFromCurPlayingTime(
                lyricsLineNum, playProgress);

        if (!blScroll && oldFontSizeScale == fontSizeScale) {
            invalidate();
        }
        if (oldFontSizeScale != fontSizeScale) {
            oldFontSizeScale = fontSizeScale;
        }
    }


    public String getTimeLrc(int playProgress) {
        String lrc = "";
        if (!hasKsc)
            return lrc;
        if (kscLyricsParser == null)
            return lrc;
        int index = kscLyricsParser
                .getLineNumberFromCurPlayingTime(playProgress);
        if (lyricsLineTreeMap == null || index >= lyricsLineTreeMap.size())
            return lrc;
        KscLyricsLineInfo kscLyricsLineInfo = lyricsLineTreeMap.get(index);
        if (kscLyricsLineInfo == null)
            return lrc;
        lrc = kscLyricsLineInfo.getLineLyrics();
        return lrc;
    }

    public boolean getBlScroll() {
        return blScroll;
    }

    public void setBlScroll(boolean blScroll) {
        this.blScroll = blScroll;
        mIsDrawTimeLine = false;
        invalidate();
    }

    public boolean getHasKsc() {
        return hasKsc;
    }

    public void setHasKsc(boolean hasKsc) {
        this.hasKsc = hasKsc;
        invalidate();
    }


    private int parserColor(String value) {
        String regularExpression = ",";
        if (value.contains(regularExpression)) {
            String[] temp = value.split(regularExpression);

            int color = Color.parseColor(temp[0]);
            int alpha = Integer.valueOf(temp[1]);
            int red = (color & 0xff0000) >> 16;
            int green = (color & 0x00ff00) >> 8;
            int blue = (color & 0x0000ff);

            return Color.argb(alpha, red, green, blue);
        }
        return Color.parseColor(value);
    }

    private OnLrcClickListener onLrcClickListener;

    public void setOnLrcClickListener(OnLrcClickListener onLrcClickListener) {
        this.onLrcClickListener = onLrcClickListener;
    }

    public interface OnLrcClickListener {
        void onClick();
    }

    @Override
    public void update(Observable arg0, Object data) {
        if (data instanceof MessageIntent) {
            MessageIntent messageIntent = (MessageIntent) data;
            if (messageIntent.getAction().equals(
                    MessageIntent.KSCMANYLINELRCCOLOR)) {
                initColor();
                invalidate();
            } else if (messageIntent.getAction().equals(
                    MessageIntent.KSCMANYLINEFONTSIZE)) {
                initSizeWord();
                showLrc(progress);
                invalidate();
            }
        }
    }

}
