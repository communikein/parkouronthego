package it.communikein.waveonthego;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.crash.FirebaseCrash;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Unbinder;

/**
 *
 * Created by Elia Maracani on 16/04/2017.
 */
public class LocalMediaFragment extends android.support.v4.app.Fragment {

    private static final String ARG_MEDIA = "media";
    private File media;

    @BindView(R.id.imageView)
    public ImageView mediaImage;
    private Unbinder unbinder;

    private OnMediaClick mMediaClickCallBack;
    private OnMediaLongClick mMediaLongClickCallBack;

    public interface OnMediaClick {
        void onMediaClick(File media);
    }
    public interface OnMediaLongClick {
        void onMediaLongClick(File media);
    }

    private final Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            FirebaseCrash.report(ex);
            ex.getMessage();
        }
    };

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        Thread.setDefaultUncaughtExceptionHandler(handler);

        if (getArguments() != null && getArguments().getString(ARG_MEDIA) != null)
            media = new File(getArguments().getString(ARG_MEDIA));
    }

    public static LocalMediaFragment newInstance(File media) {
        LocalMediaFragment fragment = new LocalMediaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MEDIA, media.getAbsolutePath());
        fragment.setArguments(args);
        return fragment;
    }

    public LocalMediaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.simple_image, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Glide.with(this)
                .load(media)
                .placeholder(R.drawable.ic_image)
                .into(mediaImage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void setOnMediaLongClickListener(OnMediaClick listener) {
        mMediaClickCallBack = listener;
    }

    public void setOnMediaClickListener(OnMediaLongClick listener) {
        mMediaLongClickCallBack = listener;
    }

    @OnClick(R.id.imageView)
    public void fireClickEvent(){
        if (mMediaClickCallBack != null)
            mMediaClickCallBack.onMediaClick(media);
    }

    @OnLongClick(R.id.imageView)
    public boolean fireLongClickEvent() {
        if (mMediaLongClickCallBack != null)
            mMediaLongClickCallBack.onMediaLongClick(media);

        return true;
    }
}