package com.zunairah.planetarium.ui.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import com.zunairah.planetarium.models.CelestialObject;
import com.zunairah.planetarium.models.HorizontalCoords;
import com.zunairah.planetarium.utils.CelestialCalculator;
import com.zunairah.planetarium.utils.SettingsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SkyView extends View {
    private static final String TAG = "SkyView";

    // General Constants
    private static final float MIN_ZOOM = 0.15f;
    private static final float MAX_ZOOM = 15.0f; // << INCREASED for more zoom-in capability
    private static final long ANIMATION_DURATION = 500;
    private static final float HIT_RADIUS_BASE = 25f; // << RENAMED and slightly adjusted base hit radius

    // Drawing Constants for INVERTED ZOOM (smaller zoomFactor = more space, labels show)
    private static final float SHOW_LABELS_WHEN_ZOOM_FACTOR_LESS_THAN = 0.7f;
    private static final float LABEL_FADE_OUT_START_ZOOM_FACTOR = 1.0f;
    private static final float LABEL_CENTER_REGION_FACTOR = 0.4f; // << NEW: Labels only in central 40% of screen width/height

    private static final float FAINT_STAR_MAGNITUDE_THRESHOLD = 4.0f;
    private static final float BRIGHT_STAR_GLOW_MAGNITUDE_THRESHOLD = 2.0f;
    private static final float STAR_GLOW_BLUR_RADIUS = 15f;
    private static final float PLANET_GLOW_BLUR_RADIUS = 20f;
    private static final float DEFAULT_TEXT_SIZE = 24f;
    private static final float LABEL_X_OFFSET = 15f;
    private static final float LABEL_Y_OFFSET = -5f;
    private static final float STAR_RADIUS_BASE_SCALE = 6.0f;
    private static final float STAR_RADIUS_MIN = 1.0f;
    private static final float PLANET_RADIUS_BASE = 8.0f;
    private static final float SUN_RADIUS_BASE = 12.0f;
    private static final float MOON_RADIUS_BASE = 10.0f;
    private static final float OBJECT_RADIUS_SCALE_EFFECT_AT_MIN_ZOOM = 2.0f;
    private static final float OBJECT_RADIUS_SCALE_EFFECT_AT_MAX_ZOOM = 0.3f; // << Allow objects to get smaller when highly magnified


    // Paint objects
    private Paint starPaint;
    private Paint starGlowPaint;
    private Paint planetPaint;
    private Paint planetGlowPaint;
    private Paint textPaint;
    private Paint backgroundPaint;

    // Observer parameters
    private double observerLatitude = 33.6844;
    private double observerLongitude = 73.0479;
    private long observationTimeMillis = System.currentTimeMillis();

    // View transformation variables
    private float zoomFactor = 1.0f;
    private float panX = 0f;
    private float panY = 0f;

    // Gesture detectors
    private ScaleGestureDetector scaleDetector;
    private GestureDetector gestureDetector;

    // Data
    private List<CelestialObject> celestialObjects = new ArrayList<>();
    private Map<String, PointF> objectScreenPositions = new HashMap<>();

    // Settings
    private SettingsManager settingsManager;

    // Interaction
    private OnObjectClickListener onObjectClickListener;

    // Background gradient
    private LinearGradient skyGradient;

    public SkyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        settingsManager = SettingsManager.getInstance(context);
        initializePaints();
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, new GestureListener());
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    private void initializePaints() {
        starPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        starPaint.setColor(Color.WHITE);
        starGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        starGlowPaint.setColor(Color.WHITE);
        planetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        planetPaint.setColor(0xFF57C5B6);
        planetGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        planetGlowPaint.setColor(0xFF57C5B6);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0xFFCCCCCC);
        textPaint.setTextSize(DEFAULT_TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        backgroundPaint = new Paint();
        backgroundPaint.setColor(0xFF0A0A1F);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        skyGradient = new LinearGradient(0, 0, 0, h,
                new int[]{0xFF0A0A1F, 0xFF16213E, 0xFF0A0A1F},
                new float[]{0f, 0.5f, 1.0f}, Shader.TileMode.CLAMP);
        backgroundPaint.setShader(skyGradient);
        if (celestialObjects != null && !celestialObjects.isEmpty()) {
            calculateScreenPositions();
        }
    }

    public void updateCelestialObjects(List<CelestialObject> objects) {
        this.celestialObjects = (objects != null) ? new ArrayList<>(objects) : new ArrayList<>();
        calculateScreenPositions();
        invalidate();
    }

    public void setObserverLocation(double latitude, double longitude) {
        this.observerLatitude = latitude; this.observerLongitude = longitude;
        calculateScreenPositions(); invalidate();
    }

    public void setObservationTime(long timeMillis) {
        this.observationTimeMillis = timeMillis;
        calculateScreenPositions(); invalidate();
    }

    private void calculateScreenPositions() {
        objectScreenPositions.clear();
        if (celestialObjects == null || celestialObjects.isEmpty() || getWidth() == 0 || getHeight() == 0) return;

        double lst = CelestialCalculator.calculateLST(observerLongitude, observationTimeMillis);
        final int viewWidth = getWidth(); final int viewHeight = getHeight();

        for (CelestialObject object : celestialObjects) {
            if (object == null) continue;
            HorizontalCoords horizontal = CelestialCalculator.convertEquatorialToHorizontal(
                    object.getMockRA(), object.getMockDec(), observerLatitude, lst);
            PointF screenPos = CelestialCalculator.mapHorizontalToScreen(
                    horizontal.getAzimuthDeg(), horizontal.getAltitudeDeg(),
                    viewWidth, viewHeight, zoomFactor);
            screenPos.x += panX;
            screenPos.y += panY;
            objectScreenPositions.put(object.getId(), screenPos);
        }
    }

    private void updatePaintEffects() {
        boolean showGlow = settingsManager.isShowGlowEffects();
        if (showGlow) {
            if (starGlowPaint.getMaskFilter() == null)
                starGlowPaint.setMaskFilter(new BlurMaskFilter(STAR_GLOW_BLUR_RADIUS, BlurMaskFilter.Blur.NORMAL));
            if (planetGlowPaint.getMaskFilter() == null)
                planetGlowPaint.setMaskFilter(new BlurMaskFilter(PLANET_GLOW_BLUR_RADIUS, BlurMaskFilter.Blur.NORMAL));
        } else {
            starGlowPaint.setMaskFilter(null);
            planetGlowPaint.setMaskFilter(null);
        }
    }

    private void updatePaintAlphas() {
        int baseAlpha = (int) (255 * settingsManager.getBrightness());
        starPaint.setAlpha(baseAlpha);
        starGlowPaint.setAlpha(baseAlpha / 3);
        planetPaint.setAlpha(baseAlpha);
        planetGlowPaint.setAlpha(baseAlpha / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);
        updatePaintEffects();
        updatePaintAlphas();
        drawCelestialObjects(canvas);
    }

    private void drawCelestialObjects(Canvas canvas) {
        if (celestialObjects == null || celestialObjects.isEmpty()) return;

        final int currentViewWidth = getWidth();
        final int currentViewHeight = getHeight();
        final boolean showLabelsSetting = settingsManager.isShowLabels();

        // Define the central region for labels
        final float centerX = currentViewWidth / 2f;
        final float centerY = currentViewHeight / 2f;
        final float labelRegionWidth = currentViewWidth * LABEL_CENTER_REGION_FACTOR;
        final float labelRegionHeight = currentViewHeight * LABEL_CENTER_REGION_FACTOR;
        final float minXForLabel = centerX - labelRegionWidth / 2f;
        final float maxXForLabel = centerX + labelRegionWidth / 2f;
        final float minYForLabel = centerY - labelRegionHeight / 2f;
        final float maxYForLabel = centerY + labelRegionHeight / 2f;


        for (CelestialObject object : celestialObjects) {
            if (object == null) continue;
            PointF screenPos = objectScreenPositions.get(object.getId());

            // Adjust HIT_RADIUS for culling based on visual size to be more generous
            float visualObjectRadius = calculateObjectRadiusVisual(object.getMockMagnitude(), object.getType() == CelestialObject.Type.STAR, object.getType());
            float cullMargin = visualObjectRadius * 1.5f; // Margin based on visual size

            if (screenPos == null ||
                    screenPos.x < -cullMargin || screenPos.y < -cullMargin ||
                    screenPos.x > currentViewWidth + cullMargin ||
                    screenPos.y > currentViewHeight + cullMargin) {
                continue;
            }
            if (!shouldDrawObject(object)) continue;

            switch (object.getType()) {
                case STAR: drawStar(canvas, object, screenPos); break;
                case PLANET: case SUN: case MOON: drawPlanet(canvas, object, screenPos); break;
            }

            if (showLabelsSetting) {
                // Check if object is within the central region for labels
                boolean isInCenterRegion = screenPos.x >= minXForLabel && screenPos.x <= maxXForLabel &&
                        screenPos.y >= minYForLabel && screenPos.y <= maxYForLabel;

                if (isInCenterRegion) { // << NEW: Only consider labels for objects in the center
                    if (zoomFactor < SHOW_LABELS_WHEN_ZOOM_FACTOR_LESS_THAN) {
                        drawObjectLabel(canvas, object, screenPos, true); // Fully visible
                    } else if (zoomFactor < LABEL_FADE_OUT_START_ZOOM_FACTOR) {
                        drawObjectLabel(canvas, object, screenPos, false); // Fading
                    }
                }
            }
        }
    }

    private boolean shouldDrawObject(CelestialObject object) {
        switch (object.getType()) {
            case STAR:
                if (!settingsManager.isShowStars()) return false;
                return settingsManager.isShowFaintStars() || object.getMockMagnitude() <= FAINT_STAR_MAGNITUDE_THRESHOLD;
            case PLANET: case SUN: case MOON: return settingsManager.isShowPlanets();
        }
        return true;
    }

    private void drawStar(Canvas canvas, CelestialObject star, PointF position) {
        float radius = calculateObjectRadiusVisual(star.getMockMagnitude(), true);
        int starColor = getStarColor(star.getMockSpectralType());
        starPaint.setColor(starColor); starGlowPaint.setColor(starColor);
        if (settingsManager.isShowGlowEffects() && star.getMockMagnitude() < BRIGHT_STAR_GLOW_MAGNITUDE_THRESHOLD) {
            canvas.drawCircle(position.x, position.y, radius * 1.5f + STAR_GLOW_BLUR_RADIUS / 2f, starGlowPaint);
        }
        canvas.drawCircle(position.x, position.y, radius, starPaint);
    }

    private void drawPlanet(Canvas canvas, CelestialObject planet, PointF position) {
        int planetColor = getPlanetColor(planet);
        planetPaint.setColor(planetColor); planetGlowPaint.setColor(planetColor);
        float radius = calculateObjectRadiusVisual(0, false, planet.getType());
        if (settingsManager.isShowGlowEffects()) {
            canvas.drawCircle(position.x, position.y, radius * 1.2f + PLANET_GLOW_BLUR_RADIUS / 2f, planetGlowPaint);
        }
        canvas.drawCircle(position.x, position.y, radius, planetPaint);
    }

    private void drawObjectLabel(Canvas canvas, CelestialObject object, PointF position, boolean fullyVisible) {
        float alphaFactor;
        if (fullyVisible) {
            alphaFactor = 1.0f;
        } else {
            float fadeRange = LABEL_FADE_OUT_START_ZOOM_FACTOR - SHOW_LABELS_WHEN_ZOOM_FACTOR_LESS_THAN;
            if (fadeRange <= 0.01f) fadeRange = 0.01f;
            alphaFactor = (LABEL_FADE_OUT_START_ZOOM_FACTOR - zoomFactor) / fadeRange;
            alphaFactor = Math.max(0f, Math.min(1.0f, alphaFactor));
        }
        int finalAlpha = (int) (255 * alphaFactor * settingsManager.getBrightness());
        textPaint.setAlpha(finalAlpha);
        if (finalAlpha > 20) {
            canvas.drawText(object.getName(), position.x + LABEL_X_OFFSET, position.y + LABEL_Y_OFFSET, textPaint);
        }
    }

    private float calculateObjectRadiusVisual(float magnitude, boolean isStar, CelestialObject.Type... type) {
        float baseRadius;
        if (isStar) {
            baseRadius = Math.max(STAR_RADIUS_MIN, STAR_RADIUS_BASE_SCALE - magnitude);
        } else {
            baseRadius = PLANET_RADIUS_BASE;
            if (type.length > 0) {
                if (type[0] == CelestialObject.Type.SUN) baseRadius = SUN_RADIUS_BASE;
                else if (type[0] == CelestialObject.Type.MOON) baseRadius = MOON_RADIUS_BASE;
            }
        }
        float visualScale;
        if (zoomFactor == 1.0f) {
            visualScale = 1.0f;
        } else if (zoomFactor < 1.0f) {
            float range = 1.0f - MIN_ZOOM; if (range == 0) range = 0.01f;
            float normalized = (1.0f - zoomFactor) / range;
            visualScale = 1.0f + normalized * (OBJECT_RADIUS_SCALE_EFFECT_AT_MIN_ZOOM - 1.0f);
        } else {
            float range = MAX_ZOOM - 1.0f; if (range == 0) range = 0.01f;
            float normalized = (zoomFactor - 1.0f) / range;
            visualScale = 1.0f + normalized * (OBJECT_RADIUS_SCALE_EFFECT_AT_MAX_ZOOM - 1.0f);
        }
        visualScale = Math.max(OBJECT_RADIUS_SCALE_EFFECT_AT_MAX_ZOOM, Math.min(OBJECT_RADIUS_SCALE_EFFECT_AT_MIN_ZOOM, visualScale));
        return baseRadius * visualScale;
    }

    private int getStarColor(String spectralType) {
        if (spectralType == null || spectralType.isEmpty()) return Color.WHITE;
        switch (spectralType.toUpperCase().charAt(0)) {
            case 'O': return 0xFF9BB0FF; case 'B': return 0xFFAABFFF;
            case 'A': return 0xFFCAD7FF; case 'F': return 0xFFF8F7FF;
            case 'G': return 0xFFFFF4EA; case 'K': return 0xFFFFD2A1;
            case 'M': return 0xFFFFAD51; default: return Color.WHITE;
        }
    }

    private int getPlanetColor(CelestialObject planet) {
        if (planet.getType() != CelestialObject.Type.STAR && planet.getMockSpectralType() != null) {
            String colorHex = planet.getMockSpectralType();
            if (colorHex.startsWith("#")) {
                try { return Color.parseColor(colorHex); }
                catch (IllegalArgumentException e) { Log.w(TAG, "Invalid color: " + colorHex + " for " + planet.getName()); }
            }
        }
        String nameLower = planet.getName().toLowerCase();
        switch (nameLower) {
            case "sun": return 0xFFFFFFA0; case "moon": return 0xFFB0B0B0;
            case "mercury": return 0xFF9E8966; case "venus": return 0xFFFFF8DC;
            case "mars": return 0xFFE07B58; case "jupiter": return 0xFFD8C0A0;
            case "saturn": return 0xFFF0E68C; case "uranus": return 0xFFAFECF0;
            case "neptune": return 0xFF87CEFA; default: return 0xFF57C5B6;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean gestureConsumed = false; // Flag to see if any detector consumed the event
        if (scaleDetector != null) {
            gestureConsumed = scaleDetector.onTouchEvent(event) || gestureConsumed;
        }
        if (gestureDetector != null) {
            gestureConsumed = gestureDetector.onTouchEvent(event) || gestureConsumed;
        }

        // Handle tap only if it's an ACTION_UP and no other gesture was consuming
        if (event.getAction() == MotionEvent.ACTION_UP && !gestureConsumed) {
            // Check if this UP event could be part of a scale or scroll that just ended
            // This is a bit tricky. A simpler way is to rely on GestureDetector's onSingleTapUp.
            // For now, let's assume if scale/scroll didn't consume the general stream, it's a tap.
            // The current onTouchEvent logic for tap might be okay if gestureDetector handles single tap.
            // Let's refine handleTap to be more robust
        }
        return gestureConsumed || super.onTouchEvent(event);
    }


    private void handleTap(float x, float y) {
        Log.d(TAG, "handleTap called at x: " + x + ", y: " + y + " with zoomFactor: " + zoomFactor);
        CelestialObject closestObject = null;
        float closestDistance = Float.MAX_VALUE;

        // Calculate visual scale factor for tap radius (consistent with object visual size)
        float visualScaleForTap;
        if (zoomFactor == 1.0f) {
            visualScaleForTap = 1.0f;
        } else if (zoomFactor < 1.0f) { // Spaced out view, objects visually larger
            float range = 1.0f - MIN_ZOOM; if (range == 0) range = 0.01f;
            float normalized = (1.0f - zoomFactor) / range;
            visualScaleForTap = 1.0f + normalized * (OBJECT_RADIUS_SCALE_EFFECT_AT_MIN_ZOOM - 1.0f);
        } else { // Magnified view, objects visually smaller
            float range = MAX_ZOOM - 1.0f; if (range == 0) range = 0.01f;
            float normalized = (zoomFactor - 1.0f) / range;
            visualScaleForTap = 1.0f + normalized * (OBJECT_RADIUS_SCALE_EFFECT_AT_MAX_ZOOM - 1.0f);
        }
        visualScaleForTap = Math.max(0.25f, Math.min(2.5f, visualScaleForTap)); // Cap the tap scale factor

        float tapRadius = HIT_RADIUS_BASE * visualScaleForTap;
        Log.d(TAG, "handleTap: tapRadius calculated as: " + tapRadius);


        for (CelestialObject object : celestialObjects) {
            if (object == null) continue;
            PointF screenPos = objectScreenPositions.get(object.getId());
            if (screenPos == null) continue;

            // More generous broad phase check
            if (Math.abs(screenPos.x - x) > tapRadius * 2 || Math.abs(screenPos.y - y) > tapRadius * 2) {
                // Log.v(TAG, "Skipping " + object.getName() + " (too far in broad phase)");
                continue;
            }

            float distance = CelestialCalculator.calculateDistance(x, y, screenPos.x, screenPos.y);
            // Log.v(TAG, "Checking " + object.getName() + " at (" + screenPos.x + "," + screenPos.y + "), dist: " + distance);

            if (distance < tapRadius && distance < closestDistance) {
                closestObject = object;
                closestDistance = distance;
                Log.d(TAG, "Candidate for tap: " + object.getName() + " at distance: " + distance);
            }
        }

        if (closestObject != null) {
            Log.i(TAG, "Object tapped: " + closestObject.getName());
            if (onObjectClickListener != null) {
                onObjectClickListener.onObjectClick(closestObject);
            } else {
                Log.w(TAG, "onObjectClickListener is null, cannot report tap.");
            }
        } else {
            Log.d(TAG, "No object tapped within radius.");
        }
    }


    public void centerOnObject(CelestialObject object) {
        // ... (centerOnObject remains the same)
        if (object == null || objectScreenPositions == null) return;
        PointF screenPos = objectScreenPositions.get(object.getId());
        if (screenPos == null) return;
        final float finalTargetPanX = getWidth() / 2f - screenPos.x + panX;
        final float finalTargetPanY = getHeight() / 2f - screenPos.y + panY;
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        final float startPanX = this.panX; final float startPanY = this.panY;
        animator.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            this.panX = startPanX + (finalTargetPanX - startPanX) * fraction;
            this.panY = startPanY + (finalTargetPanY - startPanY) * fraction;
            calculateScreenPositions(); invalidate();
        });
        animator.start();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // ... (inverted scale listener remains the same)
            float prevZoomFactor = zoomFactor;
            float rawScaleFactor = detector.getScaleFactor();
            float invertedBaseScale;
            if (rawScaleFactor != 0 && rawScaleFactor != 1.0f) {
                invertedBaseScale = 1.0f / rawScaleFactor;
            } else {
                invertedBaseScale = 1.0f;
            }
            float sensitivity = settingsManager.getZoomSensitivity();
            float delta = invertedBaseScale - 1.0f;
            float finalEffectiveScaleFactor = 1.0f + (delta * sensitivity);
            zoomFactor *= finalEffectiveScaleFactor;
            zoomFactor = Math.max(MIN_ZOOM, Math.min(zoomFactor, MAX_ZOOM));
            calculateScreenPositions();
            invalidate();
            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override public boolean onDown(MotionEvent e) { return true; }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // ... (scroll remains the same)
            panX -= distanceX; panY -= distanceY;
            calculateScreenPositions(); invalidate(); return true;
        }

        // It's often better to use onSingleTapUp for tap actions
        // to avoid firing on a scroll or long press.
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d(TAG, "onSingleTapUp detected at x: " + e.getX() + ", y: " + e.getY());
            handleTap(e.getX(), e.getY());
            return true; // Consume the event
        }


        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // ... (double tap remains the same)
            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.setDuration(ANIMATION_DURATION);
            animator.setInterpolator(new DecelerateInterpolator());
            final float startZoom = zoomFactor; final float targetZoom = 1.0f;
            final float startPanX = panX; final float targetPanX = 0f;
            final float startPanY = panY; final float targetPanY = 0f;
            animator.addUpdateListener(animation -> {
                float fraction = animation.getAnimatedFraction();
                zoomFactor = startZoom + (targetZoom - startZoom) * fraction;
                panX = startPanX + (targetPanX - startPanX) * fraction;
                panY = startPanY + (targetPanY - startPanY) * fraction;
                calculateScreenPositions(); invalidate();
            });
            animator.start(); return true;
        }
    }

    public interface OnObjectClickListener { void onObjectClick(CelestialObject object); }
    public void setOnObjectClickListener(OnObjectClickListener listener) {
        Log.d(TAG, "OnObjectClickListener " + (listener == null ? "cleared" : "set"));
        this.onObjectClickListener = listener;
    }
    public void onSettingsChanged() { calculateScreenPositions(); invalidate(); }
}