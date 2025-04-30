package com.ruyiruyi.rylibrary.ui.cell;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.ruyiruyi.rylibrary.image.ui.ImageProgressBarDrawable;

import java.io.File;

public class CloudImageView extends SimpleDraweeView {
    public CloudImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public CloudImageView(Context context) {
        super(context);
    }

    public CloudImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CloudImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CloudImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public static GenericDraweeHierarchy createHierarchy(Context context, Drawable defaultImage, Drawable errorImage, boolean enableProgress, int radius) {
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
        builder.setFadeDuration(300);
        if(defaultImage != null) {
            builder.setPlaceholderImage(defaultImage);
        }

        if(errorImage != null) {
            builder.setFailureImage(errorImage);
        }

        if(enableProgress) {
            builder.setProgressBarImage(new ImageProgressBarDrawable(context));
        }

        GenericDraweeHierarchy hierarchy = builder.build();
       // hierarchy.setActualImageScaleType(ScaleType.FOCUS_CROP);
        if(radius > 0) {
            RoundingParams roundingParams = hierarchy.getRoundingParams();
            roundingParams.setCornersRadius((float)radius);
            hierarchy.setRoundingParams(roundingParams);
        }

        return hierarchy;
    }

    public static CloudImageView create(Context context, Drawable defaultImage, Drawable errorImage, boolean enableProgress, int radius) {
        GenericDraweeHierarchy hierarchy = createHierarchy(context, defaultImage, errorImage, enableProgress, radius);
        CloudImageView cloudImageView = new CloudImageView(context, hierarchy);
        return cloudImageView;
    }

    public static CloudImageView create(Context context, Drawable defaultImage, Drawable errorImage, boolean enableProgress) {
        return create(context, defaultImage, errorImage, enableProgress, 0);
    }

    public static CloudImageView create(Context context, Drawable defaultImage) {
        return create(context, defaultImage, (Drawable)null, false, 0);
    }

    public static CloudImageView create(Context context) {
        return create(context, (Drawable)null, (Drawable)null, false, 0);
    }

    public void setPlaceholderImage(Drawable image) {
        ((GenericDraweeHierarchy)this.getHierarchy()).setPlaceholderImage(image);
    }

    public void setActualImageScaleType(ScaleType type) {
       // ((GenericDraweeHierarchy)this.getHierarchy()).setActualImageScaleType(type);
    }

    public void setActualImageColorFilter(ColorFilter colorFilter) {
        ((GenericDraweeHierarchy)this.getHierarchy()).setActualImageColorFilter(colorFilter);
    }

    public void setRound(float radius) {
        RoundingParams roundingParams = ((GenericDraweeHierarchy)this.getHierarchy()).getRoundingParams();
        if(roundingParams == null) {
            roundingParams = new RoundingParams();
        }

        roundingParams.setCornersRadius(radius);
        ((GenericDraweeHierarchy)this.getHierarchy()).setRoundingParams(roundingParams);
    }

    public void setImagePath(int resId) {
        Uri uri = Uri.parse("res:///" + resId);
        this.setImagePath((Uri)uri, (ControllerListener)null, false, false);
    }

    public void setImagePath(File file) {
        Uri uri = Uri.fromFile(file);
        this.setImagePath((Uri)uri, (ControllerListener)null, false, false);
    }

    public void setImagePath(Uri uri) {
        this.setImagePath((Uri)uri, (ControllerListener)null, false, false);
    }

    public void setAssetImagePath(String fileName) {
        Uri uri = Uri.parse("asset://" + fileName);
        this.setImagePath((Uri)uri, (ControllerListener)null, false, false);
    }

    public void setImagePath(String url) {
        Uri uri = Uri.parse(url);
        this.setImagePath((Uri)uri, (ControllerListener)null, false, false);
    }

    public void setImagePath(String url, int width, int height) {
        Uri uri = Uri.parse(url);
        this.setImagePath(uri, (ControllerListener)null, false, false, width, height);
    }

    public void setImagePath(String url, ControllerListener listener) {
        Uri uri = Uri.parse(url);
        this.setImagePath(uri, listener, false, false);
    }

    public void setGifOrWebPImagePath(String url, boolean tapRetry, boolean gifAutoPlay) {
        this.setImagePath((Uri)Uri.parse(url), (ControllerListener)null, tapRetry, gifAutoPlay);
    }

    public void setImagePath(@NonNull Uri uri, @Nullable ControllerListener listener, boolean tapRetry, boolean gifAutoPlay) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setProgressiveRenderingEnabled(true).build();
        this.setImagePath(request, listener, tapRetry, gifAutoPlay);
    }

    protected void setImagePath(@NonNull Uri uri, @Nullable ControllerListener listener, boolean tapRetry, boolean gifAutoPlay, int width, int height) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(new ResizeOptions(width, height)).setProgressiveRenderingEnabled(true).build();
        this.setImagePath(request, listener, tapRetry, gifAutoPlay);
    }

    protected void setImagePath(ImageRequest request, @Nullable ControllerListener listener, boolean tapRetry, boolean gifAutoPlay) {
        AbstractDraweeController controller = ((PipelineDraweeControllerBuilder)((PipelineDraweeControllerBuilder)((PipelineDraweeControllerBuilder)((PipelineDraweeControllerBuilder)((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setImageRequest(request)).setTapToRetryEnabled(tapRetry)).setAutoPlayAnimations(gifAutoPlay)).setOldController(this.getController())).setControllerListener(listener)).build();
        this.setController(controller);
    }
}
