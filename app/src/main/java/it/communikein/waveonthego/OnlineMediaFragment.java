package it.communikein.waveonthego;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OnlineMediaFragment extends android.support.v4.app.Fragment {

    private static final String ARG_MEDIA = "media";

    private StorageReference mediaRef;

    @BindView(R.id.imageView)
    public ImageView mediaImage;
    private Unbinder unbinder;

    private OnMediaClick mMediaClickCallBack;

    public interface OnMediaClick {
        void onMediaClick(StorageReference media);
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

        String src = getArguments().getString(ARG_MEDIA);
        mediaRef = FirebaseStorage.getInstance().getReference(src);
    }

    public static OnlineMediaFragment newInstance(StorageReference media) {
        OnlineMediaFragment fragment = new OnlineMediaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MEDIA, media.getPath());
        fragment.setArguments(args);
        return fragment;
    }

    public OnlineMediaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simple_image, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(view);

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(mediaRef)
                .into(mediaImage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unbinder.unbind();
    }

    public void setOnMediaClickListener(OnMediaClick listener) {
        mMediaClickCallBack = listener;
    }

    @OnClick(R.id.imageView)
    public void fireMediaClickEvent(){
        if (mMediaClickCallBack != null)
            mMediaClickCallBack.onMediaClick(mediaRef);
    }
}