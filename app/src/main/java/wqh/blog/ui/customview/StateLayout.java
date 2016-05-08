package wqh.blog.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import wqh.blog.R;

/**
 * Created by WQH on 2016/5/8  21:52.
 * A StateLayout that can holds four state.
 * ---Content
 * ---Empty
 * ---Error
 * ---Loading
 */
public class StateLayout extends FrameLayout {
    private View contentView;
    private View emptyView;
    private View errorView;
    private View progressView;

    private View currentShowingView;

    public StateLayout(Context context) {
        this(context, null);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void showContentView() {
        switchWithAnimation(contentView);
    }

    public void showEmptyView() {
        switchWithAnimation(emptyView);
    }

    public void showErrorView() {
        switchWithAnimation(errorView);
    }

    public void showLoadingView() {
        switchWithAnimation(progressView);
    }


    public void setErrorListener(final OnClickListener onErrorButtonClickListener) {
        errorView.setOnClickListener(onErrorButtonClickListener);
    }

    public void setEmptyListener(final OnClickListener onEmptyButtonClickListener) {
        emptyView.setOnClickListener(onEmptyButtonClickListener);
    }


    private void init(Context context, AttributeSet attrs) {
        parseAttrs(context, attrs);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        currentShowingView = contentView;
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        /*
         * Init.
         */
        LayoutInflater inflater = LayoutInflater.from(context);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StateLayout, 0, 0);
        String loadingText;
        String errorText;
        String emptyText;
        try {
            errorText = a.getString(R.styleable.StateLayout_errorText);
            emptyText = a.getString(R.styleable.StateLayout_emptyText);
            loadingText = a.getString(R.styleable.StateLayout_loadingText);
        } finally {
            a.recycle();
        }

        /*
         * Loading View here.
         */
        progressView = inflater.inflate(R.layout.view_loading, this, false);
        if (loadingText != null && !loadingText.isEmpty()) {
            ((TextView) progressView.findViewById(R.id.loading_text)).setText(loadingText);
        }

        addView(progressView);

        /*
         * Error View here.
         */
        errorView = inflater.inflate(R.layout.view_error, this, false);
        if (errorText != null && !errorText.isEmpty()) {
            ((TextView) progressView.findViewById(R.id.error_text)).setText(errorText);
        }

        addView(errorView);
        /*
         * Empty View here.
         */
        emptyView = inflater.inflate(R.layout.view_empty, this, false);
        if (emptyText != null && !emptyText.isEmpty()) {
            ((TextView) progressView.findViewById(R.id.empty_text)).setText(emptyText);
        }
        addView(emptyView);
    }

    private void checkIsContentView(View view) {
        if (contentView == null && view != errorView && view != progressView && view != emptyView) {
            contentView = view;
            currentShowingView = contentView;
        }
    }

    private void switchWithAnimation(final View toBeShown) {
        final View toBeHided = currentShowingView;
        if (toBeHided == toBeShown)
            return;

        if (toBeHided != null) {
            toBeHided.setVisibility(GONE);
        }

        if (toBeShown != null) {
            currentShowingView = toBeShown;
            toBeShown.setVisibility(VISIBLE);
        }
    }

    @Override
    public void addView(View child) {
        checkIsContentView(child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        checkIsContentView(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        checkIsContentView(child);
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        checkIsContentView(child);
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }
}
