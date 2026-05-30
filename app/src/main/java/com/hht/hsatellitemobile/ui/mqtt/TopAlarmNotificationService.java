package com.hht.hsatellitemobile.ui.mqtt;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hht.hsatellitemobile.R;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

public class TopAlarmNotificationService {
    public interface AlarmClickListener {
        void onAlarmClick(MqttAlarmData alarmData);
    }

    private static final int MAX_QUEUE_LENGTH = 50;
    private static final long DEDUPE_WINDOW_MS = 1500L;
    private static final long DISPLAY_DURATION_MS = 5000L;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Queue<MqttAlarmData> queue = new ArrayDeque<MqttAlarmData>();
    private final Map<String, Long> dedupeMap = new HashMap<String, Long>();
    private final AlarmClickListener clickListener;

    private FrameLayout currentView;
    private boolean isShowing;
    private boolean closing;
    private boolean dragging;
    private float downX;
    private float downY;
    private float dragOffsetY;

    public TopAlarmNotificationService(AlarmClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void showNotification(Activity activity, MqttAlarmData data) {
        if (activity == null || activity.isFinishing() || data == null) {
            return;
        }
        clearExpiredDedupe();
        String dedupeKey = data.getDedupeKey();
        if (!TextUtils.isEmpty(dedupeKey)) {
            Long lastAt = dedupeMap.get(dedupeKey);
            long now = System.currentTimeMillis();
            if (lastAt != null && now - lastAt < DEDUPE_WINDOW_MS) {
                return;
            }
            dedupeMap.put(dedupeKey, now);
        }
        if (queue.size() >= MAX_QUEUE_LENGTH) {
            queue.poll();
        }
        queue.offer(data);
        showNext(activity);
    }

    public void clearAllNotifications() {
        queue.clear();
        dedupeMap.clear();
    }

    public void release() {
        handler.removeCallbacksAndMessages(null);
        queue.clear();
        dedupeMap.clear();
        if (currentView != null) {
            ViewGroup parent = (ViewGroup) currentView.getParent();
            if (parent != null) {
                parent.removeView(currentView);
            }
        }
        currentView = null;
        isShowing = false;
        closing = false;
    }

    private void showNext(final Activity activity) {
        if (isShowing || queue.isEmpty() || activity.isFinishing()) {
            return;
        }
        final MqttAlarmData data = queue.poll();
        final ViewGroup root = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (root == null) {
            return;
        }

        isShowing = true;
        closing = false;
        dragging = false;
        dragOffsetY = 0;
        currentView = buildNotificationView(activity, data);
        root.addView(currentView);
        currentView.post(new Runnable() {
            @Override
            public void run() {
                currentView.setTranslationY(-currentView.getHeight() - dp(activity, 24));
                currentView.setAlpha(0f);
                currentView.animate()
                        .translationY(0f)
                        .alpha(1f)
                        .setDuration(320L)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissCurrent(activity);
                                    }
                                }, DISPLAY_DURATION_MS);
                            }
                        })
                        .start();
            }
        });
    }

    private FrameLayout buildNotificationView(final Activity activity, final MqttAlarmData data) {
        final FrameLayout wrapper = (FrameLayout) LayoutInflater.from(activity)
                .inflate(R.layout.view_top_alarm_notification, null, false);
        FrameLayout.LayoutParams wrapperParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        wrapperParams.gravity = Gravity.TOP;
        wrapperParams.leftMargin = dp(activity, 20);
        wrapperParams.rightMargin = dp(activity, 20);
        wrapperParams.topMargin = getStatusBarHeight(activity) + dp(activity, 0);
        wrapper.setLayoutParams(wrapperParams);

        TextView title = (TextView) wrapper.findViewById(R.id.top_alarm_title);
        TextView time = (TextView) wrapper.findViewById(R.id.top_alarm_time);
        TextView message = (TextView) wrapper.findViewById(R.id.top_alarm_message);
        final View content = wrapper.findViewById(R.id.top_alarm_content);
        final View clearAll = wrapper.findViewById(R.id.top_alarm_clear_all);
        final View close = wrapper.findViewById(R.id.top_alarm_close);
        title.setText(data.getTitle());
        time.setText(data.getTimeText());
        message.setText(data.getMessage());

        View.OnTouchListener notificationTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (closing) {
                    return true;
                }
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        handler.removeCallbacksAndMessages(null);
                        dragging = false;
                        dragOffsetY = 0;
                        downX = event.getRawX();
                        downY = event.getRawY();
                        wrapper.animate().cancel();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float offsetY = event.getRawY() - downY;
                        float offsetX = event.getRawX() - downX;
                        if (Math.abs(offsetY) > dp(activity, 4) || Math.abs(offsetX) > dp(activity, 4)) {
                            dragging = true;
                        }
                        dragOffsetY = Math.max(-dp(activity, 140), Math.min(0, offsetY));
                        wrapper.setTranslationY(dragOffsetY);
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (dragOffsetY <= -dp(activity, 48)) {
                            dismissCurrent(activity);
                            return true;
                        }
                        wrapper.animate().translationY(0).setDuration(180L).start();
                        if (!dragging && event.getActionMasked() == MotionEvent.ACTION_UP) {
                            if (v == clearAll) {
                                clearAllNotifications();
                                dismissCurrent(activity);
                                return true;
                            }
                            if (v == close) {
                                dismissCurrent(activity);
                                return true;
                            }
                            if (v == content) {
                                if (clickListener != null) {
                                    clickListener.onAlarmClick(data);
                                }
                                dismissCurrent(activity);
                                return true;
                            }
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dismissCurrent(activity);
                            }
                        }, DISPLAY_DURATION_MS);
                        return true;
                    default:
                        return true;
                }
            }
        };
        wrapper.setOnTouchListener(notificationTouchListener);
        content.setOnTouchListener(notificationTouchListener);
        clearAll.setOnTouchListener(notificationTouchListener);
        close.setOnTouchListener(notificationTouchListener);
        return wrapper;
    }

    private void dismissCurrent(final Activity activity) {
        if (currentView == null || closing) {
            return;
        }
        closing = true;
        handler.removeCallbacksAndMessages(null);
        final View view = currentView;
        ViewPropertyAnimator animator = view.animate()
                .translationY(-view.getHeight() - dp(activity, 24))
                .alpha(0f)
                .setDuration(260L);
        animator.withEndAction(new Runnable() {
            @Override
            public void run() {
                ViewGroup parent = (ViewGroup) view.getParent();
                if (parent != null) {
                    parent.removeView(view);
                }
                if (currentView == view) {
                    currentView = null;
                }
                isShowing = false;
                closing = false;
                showNext(activity);
            }
        });
        animator.start();
    }

    private void clearExpiredDedupe() {
        if (dedupeMap.isEmpty()) {
            return;
        }
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String, Long>> iterator = dedupeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            if (now - entry.getValue() >= DEDUPE_WINDOW_MS) {
                iterator.remove();
            }
        }
    }

    private int dp(Activity activity, int value) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                activity.getResources().getDisplayMetrics()
        );
    }

    private int getStatusBarHeight(Activity activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return activity.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
